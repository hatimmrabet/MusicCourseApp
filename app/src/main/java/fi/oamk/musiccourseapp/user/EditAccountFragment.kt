package fi.oamk.musiccourseapp.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentEditAccountBinding
import fi.oamk.musiccourseapp.databinding.FragmentPostsBinding

class EditAccountFragment : Fragment() {
    private var _binding: FragmentEditAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var ref : DatabaseReference
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        binding.updateButton.setOnClickListener{
            ref = FirebaseDatabase.getInstance().reference.child("users").child(auth.currentUser.uid)
            if(binding.name.text.toString() != ""){
                ref.child("fullname").setValue(binding.name.text.toString())
            }
            if(binding.email.text.toString() != ""){
                auth.currentUser.updateEmail(binding.email.text.toString())
                ref.child("email").setValue(binding.email.text.toString())
            }
            if(binding.password.text.toString() != ""){
                auth.currentUser.updatePassword(binding.password.text.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}