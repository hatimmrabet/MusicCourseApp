package fi.oamk.musiccourseapp.schedule

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import fi.oamk.musiccourseapp.schedule.calendar.ScheduleCalendarFragment
import fi.oamk.musiccourseapp.schedule.reservation.ScheduleReservationFragment

class ViewPageAdapter(supportFragmentManager: FragmentManager, private val totalTabs : Int): FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return ScheduleCalendarFragment()
            1 -> return ScheduleReservationFragment()
            else -> return ScheduleCalendarFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}