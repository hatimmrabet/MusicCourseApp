package fi.oamk.musiccourseapp.schedule.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentScheduleCalendarBinding

class ScheduleCalendarFragment : Fragment() {

    private var _binding: FragmentScheduleCalendarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScheduleCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDates()
        viewModel.dates.observe(viewLifecycleOwner){
            binding.calendarView.addDecorator(CurrentDayDecorator(activity, it!!))
        }
        binding.calendarView.setOnDateChangedListener { w, date, selected ->
            viewModel.setDate(date.year.toString(), date.month.toString(), date.day.toString())
        }

        viewModel.events.observe(viewLifecycleOwner){
            if(it.size > 0){
                binding.textView.text = ""
            } else {
                binding.textView.text = "There are no Events for selected date"
            }
            binding.recyclerView.adapter = ItemAdapter(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}