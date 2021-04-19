package fi.oamk.musiccourseapp.posts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.*

class ReservationRecapFragment : Fragment() {
        private var _binding: FragmentReservationRecapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReservationRecapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.schedule.setOnClickListener{
           // view.findNavController().navigate(R.id.action_reservationRecapFragment_to_scheduleFragment)
        }

        binding.posts.setOnClickListener{
           // view.findNavController().navigate(R.id.action_reservationRecapFragment_to_postsFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}