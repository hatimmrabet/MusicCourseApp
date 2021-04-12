package fi.oamk.musiccourseapp.user

import android.graphics.Color
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.SignupTabBinding

class SignupTab : Fragment(){

    private var _binding: SignupTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private val TAG : String = fi.oamk.musiccourseapp.MainActivity::class.java.name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = SignupTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference
        auth = Firebase.auth

        binding.signupButton.setOnClickListener{
            database.child("users").get().addOnSuccessListener {
                var switch = 0
                if(binding.email.text == null ||  binding.name.text == null ||  binding.password.text == null || (!binding.studentSwitch.isChecked && !binding.teacherSwitch.isChecked)){
                    binding.textError.text = "Missing one or more informations"
                    binding.textError.setTextColor(Color.RED)
                }
                else if(binding.confPassword.text.toString() != binding.password.text.toString()){
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
                    auth.createUserWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString()).addOnCompleteListener { task : Task<AuthResult> ->
                        if(task.isSuccessful){
                            Log.d(TAG, "Create user : success")
                            database.child("users").child(auth.currentUser.uid).setValue(user)
                            if(switch == 2){
                                database.child("roles").child("teacher").child(auth.currentUser.uid).setValue(true)
                                database.child("roles").child("student").child(auth.currentUser.uid).setValue(true)
                            }
                            else if (switch == 1){
                                database.child("roles").child("student").child(auth.currentUser.uid).setValue(true)
                            }
                            else{
                                database.child("roles").child("teacher").child(auth.currentUser.uid).setValue(true)
                            }
                        }
                        else{
                            Log.w(TAG, "Create user : failure", task.exception)
                        }
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_postsFragment)
                }
            }.addOnFailureListener{
                Log.e("firebase","Error getting data", it)
            }
        }
    }

}