package fi.oamk.musiccourseapp.schedule.calendar

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarDayLiveData (private val dateUsersRef: DatabaseReference)
    : MutableLiveData<MutableSet<CalendarDay>>() {

    private val TAG = "CalendarDayLiveData"
    private var listenerRegistration = DataEventListener()
    private var calendarDays = mutableSetOf<CalendarDay>()
    private val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    private inner class DataEventListener: ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Error: ${error.toException()}")
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            calendarDays.clear()
            snapshot.children.forEach {child ->
                val date = LocalDate.parse(child.key.toString(), formatter)
                calendarDays.add(CalendarDay.from(date.year, date.monthValue, date.dayOfMonth))
            }
            setValue(calendarDays)
        }
    }

    override fun onActive() {
        super.onActive()
        dateUsersRef.addValueEventListener(listenerRegistration)
    }

    override fun onInactive() {
        super.onInactive()
        dateUsersRef.removeEventListener(listenerRegistration)
    }
}