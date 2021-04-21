package fi.oamk.musiccourseapp.schedule.reservation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.findteacher.reservation.Reservation

class ReservationViewModel: ViewModel() {
    private val TAG = "ReservationViewModels"

    val auth = Firebase.auth.currentUser
    val reservationUsersDB = Firebase.database.getReference("reservationUsers/${auth.uid}")

    private var _reservations = ReservationLiveData(reservationUsersDB)
    val reservations: LiveData<ArrayList<Reservation>> get() = _reservations
}
