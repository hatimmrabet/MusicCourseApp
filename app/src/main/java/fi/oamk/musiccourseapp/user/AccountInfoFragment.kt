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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AccountInfoFragment : Fragment() {
    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        database = Firebase.database.reference
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            database.child("users").child("${auth.currentUser.uid}").get().addOnSuccessListener {
                val profil = it.value as HashMap<String, Any>
                binding.name.text = profil.get("fullname").toString()
                if (profil.get("role").toString() == "0"){
                    binding.role.text = "Teacher"
                }
                else
                    binding.role.text = "Student"
            }

        binding.email.text=auth.currentUser.email

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