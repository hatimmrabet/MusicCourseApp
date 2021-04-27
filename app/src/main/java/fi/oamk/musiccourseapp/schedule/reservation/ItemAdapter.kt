package fi.oamk.musiccourseapp.schedule.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.ItemReservationBinding
import fi.oamk.musiccourseapp.findteacher.reservation.Reservation
import fi.oamk.musiccourseapp.user.User

class ItemAdapter(private val dataset: ArrayList<Reservation>, private val navController: NavController)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val TAG = "ScheduleReservationFragmentItemAdapter"
    private lateinit var database: DatabaseReference
    private var student : User ? = null
    private lateinit var teacher : User


    class ItemViewHolder(val binding: ItemReservationBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        database = Firebase.database.reference
        getTeacher(auth.uid)

        return ItemViewHolder(
            ItemReservationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val reservation = dataset[position]
        getStudent(reservation.studentId)

        val start = reservation.start.substring(0,2)+":"+reservation.start.substring(2,4)
        val end = reservation.end.substring(0,2)+":"+reservation.end.substring(2,4)
        val date = reservation.date.substring(0,4)+"/"+reservation.date.substring(4,6)+"/"+reservation.date.substring(6,8)

        holder.binding.date.text = date
        holder.binding.hour.text = "From "+ start + " to " + end

        holder.binding.acceptButton.setOnClickListener {
            acceptReservation(reservation)
        }

        holder.binding.deleteButton.setOnClickListener {
            deleteReservation(reservation)
        }

        // Check if it is teacher
        usersDB.child(auth.uid).get().addOnSuccessListener {
            val user = User.from(it.value as HashMap<String, String>)
            if(user.role == "1")
                holder.binding.acceptButton.visibility = View.GONE
        }

        usersDB.child(reservation.studentId).get().addOnSuccessListener {
            val user = User.from(it.value as HashMap<String, String>)
            holder.binding.name.text = user.fullname
            holder.binding.email.text = user.email
        }
    }

    private val auth = Firebase.auth.currentUser
    private val usersDB = Firebase.database.getReference("users")
    private val dateUsersDB = Firebase.database.getReference("dateUsers")
    private val datesDB = Firebase.database.getReference("dates")
    private val reservationUsersDB = Firebase.database.getReference("reservationUsers")
    private val reservationsDB = Firebase.database.getReference("reservations")

    fun acceptReservation(reservation: Reservation) {
        // Add Date
        val dateKey = reservation.date
        val key = dateUsersDB.child(reservation.studentId).child(dateKey).push().key
        dateUsersDB.child(reservation.studentId).child(dateKey).setValue(true)
        dateUsersDB.child(auth.uid).child(dateKey).setValue(true)
        datesDB.child(auth.uid).child(dateKey).child(key!!).setValue(
            Date(
                reservation.start,
                reservation.end,
                reservation.studentId,
                auth.uid
            )
        )
        datesDB.child(reservation.studentId).child(dateKey).child(key!!).setValue(
            Date(
                reservation.start,
                reservation.end,
                reservation.studentId,
                auth.uid
            )
        )

        // Remove Reservation
        val resKey = reservation.uid
        reservationUsersDB.child(reservation.studentId).child(resKey).setValue(null)
        reservationUsersDB.child(auth.uid).child(resKey).setValue(null)
        reservationsDB.child(resKey).setValue(null)

        //payement
        payement(reservation)

        navController.navigate(R.id.action_scheduleFragment_self)
    }

    fun payement(reservation: Reservation){
        //Money transaction
        val start = reservation.start
        val end = reservation.end
        val price = 30
        val hours = Math.abs((end.toDouble() - start.toDouble()) / 100)
        
        val newCreditStudent = student!!.credit?.toDouble()!! - hours * price

        database.child("users/${student!!.uid}").child("credit").setValue(newCreditStudent.toString())
        val teacherPrice = hours * price * 0.9
        var newCreditTeacher = teacher!!.credit?.toDouble()?.plus(teacherPrice)
        database.child("users/${teacher!!.uid}").child("credit").setValue(newCreditTeacher.toString())

    }

    private fun getStudent(uid: String) {
        database.child("users/$uid").get().addOnSuccessListener {
            student = User.from(it.value as HashMap<String, String>)
        }
    }

    private fun getTeacher(uid: String) {
        database.child("users/$uid").get().addOnSuccessListener {
            teacher = User.from(it.value as HashMap<String, String>)
        }
    }

    fun deleteReservation(reservation: Reservation) {
        // Remove Reservation
        val resKey = reservation.uid
        //reservationUsersDB.child(reservation.studentId).child(resKey).setValue(null)
        //reservationUsersDB.child(auth.uid).child(resKey).setValue(null)
        reservationUsersDB.get().addOnSuccessListener {
            val resUsers = (it.value as HashMap<String, Any> )
            resUsers.map { it ->
                val resUsersData = (it.value as HashMap<String, Any>)
                resUsersData.map { it2 ->
                    if(it2.key == resKey)
                    {
                        reservationUsersDB.child(it.key).child(resKey).setValue(null)
                    }
                }
            }
        }
        reservationsDB.child(resKey).setValue(null)
        navController.navigate(R.id.action_scheduleFragment_self)
    }


}