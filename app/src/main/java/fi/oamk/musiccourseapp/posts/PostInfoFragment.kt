package fi.oamk.musiccourseapp.posts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentBookingBinding
import fi.oamk.musiccourseapp.databinding.FragmentMessagesBinding
import fi.oamk.musiccourseapp.databinding.FragmentPostInfoBinding

class PostInfoFragment : Fragment() {

    private var _binding: FragmentPostInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postInfoDesc.text = "hatimito"

    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}