package fi.oamk.musiccourseapp.user

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
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
                if(binding.teacherSwitch.isChecked && binding.studentSwitch.isChecked){
                    switch = 2
                }
                else if(binding.studentSwitch.isChecked){
                    switch = 1
                }
                var user : User = User(0, binding.email.text.toString(), binding.name.text.toString(), binding.password.text.toString(), "empty", switch)
                database.child("users").child("1").setValue(user)
            }.addOnFailureListener{
                Log.e("firebase","Error getting data", it)
            }
        }
    }

}