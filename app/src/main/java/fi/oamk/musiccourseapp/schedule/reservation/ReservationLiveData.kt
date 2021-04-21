package fi.oamk.musiccourseapp.schedule.reservation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.findteacher.reservation.Reservation

class ReservationLiveData (private val reservationUsersRef: DatabaseReference)
    : MutableLiveData<ArrayList<Reservation>>() {

    private val TAG = "ReservationLiveData"
    private var listenerRegistration = DataEventListener()
    private var reservations = arrayListOf<Reservation>()

    private inner class DataEventListener: ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Error: ${error.toException()}")
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            reservations.clear()
            snapshot.children.forEach { child ->
                Firebase.database.getReference("reservations/${child.key}").get().addOnSuccessListener {
                    reservations.add(Reservation.from(it.value as HashMap<String, String>))
                    Log.d(TAG, "Reservations: ${reservations}")
                    setValue(reservations)
                }
            }
        }
    }

    override fun onActive() {
        super.onActive()
        reservationUsersRef.addValueEventListener(listenerRegistration)
    }

    override fun onInactive() {
        super.onInactive()
        reservationUsersRef.removeEventListener(listenerRegistration)
    }



}