package fi.oamk.musiccourseapp.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentPostsBinding
import fi.oamk.musiccourseapp.databinding.FragmentUserProfileBinding


class UserProfileFragment : Fragment() {
    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    private val TAG : String = fi.oamk.musiccourseapp.MainActivity::class.java.name

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = Firebase.auth
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser == null){
            findNavController().navigate(R.id.action_userProfileFragment_to_loginFragment)
        }
        else{
            Log.d(TAG, auth.currentUser.email.toString())
            findNavController().navigate(R.id.action_userProfileFragment_to_accountInfoFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}