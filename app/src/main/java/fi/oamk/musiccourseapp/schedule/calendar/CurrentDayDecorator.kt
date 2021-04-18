package fi.oamk.musiccourseapp.schedule.calendar

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import fi.oamk.musiccourseapp.R

class CurrentDayDecorator(context: Activity?, dates: MutableSet<CalendarDay>) : DayViewDecorator {
    private val drawable: Drawable?
    var myDays = HashSet<CalendarDay>(dates)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return myDays.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable!!)
    }

    init {
        // You can set background for Decorator via drawable here
        drawable = ContextCompat.getDrawable(context!!, R.drawable.ic_baseline_circle_24)
    }
}