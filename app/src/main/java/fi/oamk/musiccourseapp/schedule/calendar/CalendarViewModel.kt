package fi.oamk.musiccourseapp.schedule.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.prolificinteractive.materialcalendarview.CalendarDay
import fi.oamk.musiccourseapp.schedule.reservation.Date
import fi.oamk.musiccourseapp.user.User

class CalendarViewModel : ViewModel() {
    private val TAG = "CalendarViewModel"
    private val auth = Firebase.auth.currentUser
    private val dateUsersDB = Firebase.database.getReference("dateUsers/${auth.uid}")
    private val datesDB = Firebase.database.getReference("dates")
    private val usersDB = Firebase.database.getReference("users")
    private var _dates = CalendarDayLiveData(dateUsersDB)
    private var user : User? = null
    val dates: LiveData<MutableSet<CalendarDay>> get() = _dates

    private var _events = MutableLiveData<ArrayList<ReservationData>>()
    val events: LiveData<ArrayList<ReservationData>> get() = _events

    fun setDate(year: String, month: String, day: String) {
        var myMonth = if (month.length == 1)
            "0" + month
        else {
            month
        }
        var myDay = if (day.length == 1)
            "0" + day
        else {
            day
        }

        val key = year + myMonth + myDay
        Log.d(TAG, "The key is ${key}")

        _events.value?.clear()
        var newEvents = arrayListOf<ReservationData>()

        datesDB.child(auth.uid).child(key).get().addOnSuccessListener { it ->
            Log.d(TAG, it.value.toString())

            it.children.forEach { child ->
                val date = Date.from(child.value as HashMap<String, String>)

                if(auth.uid == date.studentId) {
                    usersDB.child(date.teacherId).get().addOnSuccessListener { it2 ->
                        val user = User.from(it2.value as HashMap<String, String>)
                        val resDetail = ReservationData(key, date.start, date.end, user.fullname, user.email)
                        Log.d(TAG, "The resDetail is ${resDetail.toString()}")

                        newEvents.add(resDetail)
                        Log.d(TAG, "The newEvents is ${newEvents.toString()}")

                        _events.value = newEvents
                        Log.d(TAG, "The events are ${_events.value.toString()}")
                    }
                }
                else if(auth.uid == date.teacherId)
                {
                    usersDB.child(date.studentId).get().addOnSuccessListener { it2 ->
                        val user = User.from(it2.value as HashMap<String, String>)
                        val resDetail = ReservationData(key, date.start, date.end, user.fullname, user.email)
                        Log.d(TAG, "The resDetail is ${resDetail.toString()}")

                        newEvents.add(resDetail)
                        Log.d(TAG, "The newEvents is ${newEvents.toString()}")

                        _events.value = newEvents
                        Log.d(TAG, "The events are ${_events.value.toString()}")
                    }
                }
            }
        }
        _events.value = newEvents
        Log.d(TAG, "The events are ${_events.value.toString()}")

    }

}