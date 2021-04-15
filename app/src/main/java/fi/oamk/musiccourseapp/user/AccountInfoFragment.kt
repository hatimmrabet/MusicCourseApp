package fi.oamk.musiccourseapp.user

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.R.layout.fragment_account_info
import fi.oamk.musiccourseapp.databinding.FragmentAccountInfoBinding
import fi.oamk.musiccourseapp.databinding.FragmentPostsBinding

class AccountInfoFragment : Fragment() {
    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener{

            findNavController().navigate(R.id.action_accountInfoFragment_to_editAccountFragment)
        }
        binding.floatingActionButton2.setOnClickListener{
            findNavController().navigate(R.id.action_accountInfoFragment_to_createPostFragment)
        }
        binding.button2.setOnClickListener{
            findNavController().navigate(R.id.action_accountInfoFragment_to_scheduleFragment)
        }
        binding.logoutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_accountInfoFragment_to_postsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}