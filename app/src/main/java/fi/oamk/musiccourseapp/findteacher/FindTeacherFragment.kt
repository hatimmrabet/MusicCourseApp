package fi.oamk.musiccourseapp.findteacher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentFindTeacherBinding

class FindTeacherFragment : Fragment() {

    private var _binding: FragmentFindTeacherBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FindTeacherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFindTeacherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUsers()
        viewModel.users.observe(viewLifecycleOwner){
            binding.recyclerView.adapter = ItemAdapter(it, findNavController())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}