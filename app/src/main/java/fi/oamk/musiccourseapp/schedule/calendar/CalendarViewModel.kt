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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarViewModel: ViewModel() {
    private val TAG = "CalendarViewModel"
    private val auth = Firebase.auth.currentUser
    private val dateUsersDB = Firebase.database.getReference("dateUsers/${auth.uid}")
    private val datesDB = Firebase.database.getReference("dates")

    private var _dates = MutableLiveData<MutableSet<CalendarDay>>()
    val dates: LiveData<MutableSet<CalendarDay>> get() = _dates
    fun getDates() {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        dateUsersDB.get().addOnSuccessListener {
            Log.i(TAG, "Got value ${it.value}")
            _dates.value?.clear()
            var newDates = mutableSetOf<CalendarDay>()
            it.children.forEach { child ->
                Log.i(TAG, "Got value ${child.key}")
                val date = LocalDate.parse(child.key.toString(), formatter)
                newDates.add(CalendarDay.from(date.year, date.monthValue, date.dayOfMonth))
            }
            _dates.value = newDates
            Log.i(TAG, "Got dates ${newDates}")
        }.addOnFailureListener {
            Log.e(TAG, "Error getting data", it)
        }
    }

    private var _events = MutableLiveData<ArrayList<Date>>()
    val events: LiveData<ArrayList<Date>> get() = _events

    fun setDate (year: String, month: String, day:String){
        var myMonth = ""
        if(month.length == 1)
            myMonth = "0"+ month
        else {
            myMonth = month
        }

        val key = year+myMonth+day
        Log.d(TAG, "The key is ${key}")
        _events.value?.clear()
        var newEvents= arrayListOf<Date>()
        datesDB.child(auth.uid).child(key).get().addOnSuccessListener {
            Log.d(TAG, it.value.toString())
            it.children.forEach{child ->
                newEvents.add(Date.from(child.value as HashMap<String, String>))
            }
            _events.value = newEvents
            Log.d(TAG, "The events are ${_events.value.toString()}")
        }

    }

}