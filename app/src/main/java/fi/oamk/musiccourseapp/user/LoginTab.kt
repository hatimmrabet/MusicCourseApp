package fi.oamk.musiccourseapp.user

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.databinding.LoginTabBinding

class LoginTab : Fragment(){

    private var _binding: LoginTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var database : DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = LoginTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.reference

        binding.loginButton.setOnClickListener{
            database.child("users").get().addOnSuccessListener {
                val usersFromDatabase : ArrayList<Any> = it.value as ArrayList<Any>

                var enteredEmail = binding.email.text.toString()
                var enteredPassword = binding.password.text.toString()

                for(i in 0 until usersFromDatabase.size-1){
                    val user = usersFromDatabase[i] as HashMap<String, String>
                    var email = user.get("email").toString()
                    var password = user.get("password").toString()

                    if(enteredEmail == email && enteredPassword == password){
                        Log.e("firebase","Working")
                    }
                }
            }.addOnFailureListener{
                Log.e("firebase","Error getting data", it)
            }

        }


    }
}