package fi.oamk.musiccourseapp.user

import androidx.fragment.app.FragmentPagerAdapter
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class Adapter(fm : FragmentManager, private var totalTabs : Int) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position : Int) : Fragment {
        when(position){
            0 -> return LoginTab()
            1 -> return SignupTab()
            else -> return LoginTab()
        }
    }

}