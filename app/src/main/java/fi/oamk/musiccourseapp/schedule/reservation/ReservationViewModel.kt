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

    private var _reservations = MutableLiveData<ArrayList<Reservation>>()
    val reservations: LiveData<ArrayList<Reservation>> get() = _reservations

    fun getReservations() {
        var newReservations = arrayListOf<Reservation>()
        _reservations.value?.clear()
        reservationUsersDB.get().addOnSuccessListener {
            Log.d(TAG, it.value.toString())
            it.children.forEach{reservationUID ->
                Log.d(TAG, reservationUID.key.toString())
                //reservations from "search by teacher"
                val reservationsDB = Firebase.database.getReference("reservations/${reservationUID.key.toString()}")
                reservationsDB.get().addOnSuccessListener { reservation ->
                    Log.d(TAG, reservation.value.toString())
                    newReservations.add(Reservation.from(reservation.value as HashMap<String, String>))
                    _reservations.value = newReservations
                    Log.d(TAG, newReservations.toString())
                }

                //reservations from teachers posts
                /*
                Firebase.database.getReference("allReservations").get().addOnSuccessListener {
                    if(it.value != null)
                    {
                        Log.d(TAG, it.value.toString())
                        val UsersReservations = it.value as HashMap<String,HashMap<String,Any>>
                        UsersReservations.map { (key, userskey) ->
                            userskey as HashMap<String,HashMap<String,HashMap<String,Any>>>
                            userskey.map { (key2 , value) ->
                                if(value.get("teacherkey").toString() == auth.uid) {
                                    val uid = value.get("teacherkey").toString()
                                    val date = value.get("date").toString()
                                    val start = value.get("start").toString()
                                    var end = (start.substring(0,2).toInt()+1).toString()
                                    if (end.length == 1) { end = "0"+end }
                                    end = ( end + start.substring(2,5))
                                    val res = Reservation(uid,date,start,end,key)
                                    newReservations.add(res)
                                }
                            }
                        }
                    }
                }
                 */


            }
        }
    }



}
