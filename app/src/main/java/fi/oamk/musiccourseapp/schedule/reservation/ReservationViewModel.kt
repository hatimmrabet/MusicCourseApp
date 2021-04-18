package fi.oamk.musiccourseapp.schedule.reservation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.findteacher.reservation.Reservation
import fi.oamk.musiccourseapp.messages.model.Chat

class ReservationViewModel: ViewModel() {
    private val TAG = "ReservationViewModels"

    val auth = Firebase.auth.currentUser
    val reservationUsersDB = Firebase.database.getReference("reservationUsers/${auth.uid}")

    private var _reservations = MutableLiveData<ArrayList<Reservation>>()
    val reservations: LiveData<ArrayList<Reservation>> get() = _reservations

    fun getReservations() {
        var newReservations = arrayListOf<Reservation>()
        _reservations.value?.clear()
        reservationUsersDB.get().addOnSuccessListener {
            Log.d(TAG, it.value.toString())
            it.children.forEach{reservationUID ->
                Log.d(TAG, reservationUID.key.toString())
                val reservationsDB = Firebase.database.getReference("reservations/${reservationUID.key.toString()}")
                reservationsDB.get().addOnSuccessListener { reservation ->
                    newReservations.add(Reservation.from(reservation.value as HashMap<String, String>))
                    _reservations.value = newReservations
                    Log.d(TAG, newReservations.toString())
                }

            }
        }
    }



}