package fi.oamk.musiccourseapp.user

import android.graphics.Color
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.SignupTabBinding

class SignupTab : Fragment(){

    private var _binding: SignupTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var database : DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SignupTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference

        binding.signupButton.setOnClickListener{
            database.child("users").get().addOnSuccessListener {
                var switch = 0
                if(binding.email.text == null ||  binding.name.text == null ||  binding.password.text == null || (!binding.studentSwitch.isChecked && !binding.teacherSwitch.isChecked)){
                    binding.textError.text = "Missing one or more informations"
                    binding.textError.setTextColor(Color.RED)
                }
                else if(binding.email.text.toString() != binding.password.text.toString()){
                    binding.textError.text = "Passwords are not matching"
                    binding.textError.setTextColor(Color.RED)
                }
                else {
                    if (binding.teacherSwitch.isChecked && binding.studentSwitch.isChecked) {
                        switch = 2
                    } else if (binding.studentSwitch.isChecked) {
                        switch = 1
                    }
                    var user: User = User(0, binding.email.text.toString(), binding.name.text.toString(), binding.password.text.toString(), "empty", switch)
                    database.child("users").child("1").setValue(user)
                    findNavController().navigate(R.id.action_loginFragment_self)
                }
            }.addOnFailureListener{
                Log.e("firebase","Error getting data", it)
            }
        }
    }

}