package fi.oamk.musiccourseapp.user

import android.app.Activity
import android.content.Context
import android.graphics.Color
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.red
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.LoginTabBinding

class LoginTab : Fragment(){

    private var _binding: LoginTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var database : DatabaseReference
    private val TAG : String = fi.oamk.musiccourseapp.MainActivity::class.java.name
    private lateinit var auth : FirebaseAuth
    private var currentUser : FirebaseUser? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LoginTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.reference
        auth = Firebase.auth

        var visible = 1

        binding.showPassBtn.setOnClickListener{
            if(visible == 1){
                binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                visible = 0
            }
            else{
                binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                visible = 1
            }

        }

        binding.forgotPassword.setOnClickListener{
            val text = "Follow the email to change your password"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(this.context, text, duration)
            toast.show()
        }

        binding.loginButton.setOnClickListener{
            database.child("users").get().addOnSuccessListener {

                var enteredEmail = binding.email.text.toString()
                var enteredPassword = binding.password.text.toString()

                if(enteredPassword == null || enteredEmail == null){
                    binding.textError.text = "Give email and password"
                    binding.textError.setTextColor(Color.RED)
                }
                else{
                    auth.signInWithEmailAndPassword(enteredEmail, enteredPassword).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Log.d(TAG, "signInWithEmail : success")
                            currentUser = auth.currentUser
                            findNavController().navigate(R.id.action_loginFragment_to_postsFragment)
                        }
                        else{
                            Log.w(TAG, "signInWithEmail : failure", task.exception)
                            binding.textError.text = "Email or password incorrect"
                            binding.textError.setTextColor(Color.RED)
                        }
                    }
                }
            }
        }

        binding.email.setOnClickListener{
            it.hideKeyboardFrom()
        }

        binding.password.setOnClickListener{
            it.hideKeyboardFrom()
        }
    }

    fun View.hideKeyboardFrom(){
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }


}