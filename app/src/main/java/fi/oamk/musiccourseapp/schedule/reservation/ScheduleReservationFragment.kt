package fi.oamk.musiccourseapp.schedule.reservation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentScheduleReservationBinding

class ScheduleReservationFragment : Fragment() {

    private val TAG = "ReservationFragment"

    private var _binding: FragmentScheduleReservationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReservationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentScheduleReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.reservations.observe(viewLifecycleOwner){
            binding.recyclerView.adapter = ItemAdapter(it, findNavController())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}