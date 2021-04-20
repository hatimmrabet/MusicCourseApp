package fi.oamk.musiccourseapp.schedule.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.databinding.ItemReservationBinding
import fi.oamk.musiccourseapp.findteacher.reservation.Reservation
import fi.oamk.musiccourseapp.user.User

class ItemAdapter (private val dataset: ArrayList<Reservation>, private val navController: NavController)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val TAG = "ScheduleReservationFragmentItemAdapter"

    class ItemViewHolder(val binding: ItemReservationBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
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
        holder.binding.dateTextView.text = "Date: " + reservation.date + "; start: " + reservation.start + "; end: " +reservation.end
        holder.binding.acceptButton.setOnClickListener {
            acceptReservation(reservation)
        }

        // Check if it is teacher
        usersDB.child(auth.uid).get().addOnSuccessListener {
            val user = User.from(it.value as HashMap<String, String>)
            if(user.role == "1")
                holder.binding.acceptButton.visibility = View.GONE
        }

        usersDB.child(reservation.studentId).get().addOnSuccessListener {
            val user = User.from(it.value as HashMap<String, String>)
            holder.binding.studentTextView.text = "by ${user.fullname}"
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
        datesDB.child(auth.uid).child(dateKey).child(key!!).setValue(Date(reservation.start, reservation.end, reservation.studentId, auth.uid))
        datesDB.child(reservation.studentId).child(dateKey).child(key!!).setValue(Date(reservation.start, reservation.end, reservation.studentId, auth.uid))

        // Remove Reservation
        val resKey = reservation.uid
        reservationUsersDB.child(reservation.studentId).child(resKey).setValue(null)
        reservationUsersDB.child(auth.uid).child(resKey).setValue(null)
        reservationsDB.child(resKey).setValue(null)
    }
}