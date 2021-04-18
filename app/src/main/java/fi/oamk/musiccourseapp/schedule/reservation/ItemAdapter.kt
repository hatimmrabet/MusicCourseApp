package fi.oamk.musiccourseapp.schedule.reservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.databinding.ItemReservationBinding
import fi.oamk.musiccourseapp.findteacher.reservation.Reservation

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
        holder.binding.studentTextView.text = "by ${reservation.studentId}"
        holder.binding.dateTextView.text = reservation.uid
        holder.binding.acceptButton.setOnClickListener {
            acceptReservation(reservation)
        }
    }

    private val auth = Firebase.auth.currentUser
    private val dateUsersDB = Firebase.database.getReference("dateUsers")
    private val datesDB = Firebase.database.getReference("dates")
    private val reservationUsersDB = Firebase.database.getReference("reservationUsers")
    private val reservationsDB = Firebase.database.getReference("reservations")

    fun acceptReservation(reservation: Reservation) {
        // Add Date
        val dateKey = reservation.date
        dateUsersDB.child(reservation.studentId).child(dateKey).setValue(true)
        dateUsersDB.child(auth.uid).child(dateKey).setValue(true)
        datesDB.child(dateKey).setValue(Date(reservation.start, reservation.end, reservation.studentId, auth.uid))

        // Remove Reservation
        val resKey = reservation.uid
        reservationUsersDB.child(reservation.studentId).child(resKey).setValue(null)
        reservationUsersDB.child(auth.uid).child(resKey).setValue(null)
        reservationsDB.child(resKey).setValue(null)
    }

}