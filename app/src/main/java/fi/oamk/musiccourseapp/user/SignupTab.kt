package fi.oamk.musiccourseapp.user

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.SignupTabBinding
import java.io.ByteArrayOutputStream

class SignupTab : Fragment() {

    private var _binding: SignupTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private val TAG: String = fi.oamk.musiccourseapp.MainActivity::class.java.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignupTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var url : String = "https://firebasestorage.googleapis.com/v0/b/music-course-app-78f79.appspot.com/o/unknown-person-icon-27.jpg?alt=media&token=e83ec35f-2cb6-4347-af85-2c8ae32814db"

        // Images buttons click listeners
        binding.takePictureButton.setOnClickListener { takePicture() }
        binding.choosePictureButton.setOnClickListener { choosePicture() }

        database = Firebase.database.reference
        auth = Firebase.auth

        var visible = 1
        var visiblec = 1

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

        binding.showPassBtn2.setOnClickListener{
            if(visiblec == 1){
                binding.confPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                visiblec = 0
            }
            else{
                binding.confPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                visiblec = 1
            }

        }

        binding.signupButton.setOnClickListener {
            database.child("users").get().addOnSuccessListener {
                var switch = 0
                if (binding.email.text == null || binding.name.text == null || binding.password.text == null) {
                    binding.textError.text = "Missing one or more informations"
                    binding.textError.setTextColor(Color.RED)
                } else if (binding.confPassword.text.toString() != binding.password.text.toString()) {
                    binding.textError.text = "Passwords are not matching"
                    binding.textError.setTextColor(Color.RED)
                } else {
                    if (binding.switchButton.isChecked) {
                        switch = 1
                    }
                    val imageData = getImageByteArray()
                    if(imageData != null){
                        val storageRef =
                            Firebase.storage.reference.child("images/${binding.email.text.toString()}")
                        var uploadTask = storageRef.putBytes(imageData as ByteArray)
                        uploadTask.addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                storageRef.downloadUrl.addOnCompleteListener { task ->
                                    url = task.result.toString()
                                }
                            }else {
                                Log.w(TAG, "Upload task was not succesful")
                            }
                        }
                    }
                    auth.createUserWithEmailAndPassword(
                        binding.email.text.toString(),
                        binding.password.text.toString()
                    ).addOnCompleteListener { task: Task<AuthResult> ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Create user : success")
                            var credit = (10..300).random()
                            var user: User = User(
                                auth.currentUser.uid,
                                credit.toString(),
                                binding.email.text.toString(),
                                binding.name.text.toString(),
                                url,
                                switch.toString()
                            )
                            database.child("users").child(auth.currentUser.uid)
                                .setValue(user)
                            if (switch == 1) {
                                database.child("roles").child("student")
                                    .child(auth.currentUser.uid).setValue(true)
                            } else {
                                database.child("roles").child("teacher")
                                    .child(auth.currentUser.uid).setValue(true)
                            }
                        } else {
                            Log.w(TAG, "Create user : failure", task.exception)
                        }
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_postsFragment)
                }

            }

        }

        binding.name.setOnClickListener{
            it.hideKeyboardFrom()
        }
        binding.email.setOnClickListener{
            it.hideKeyboardFrom()
        }
        binding.password.setOnClickListener{
            it.hideKeyboardFrom()
        }
        binding.confPassword.setOnClickListener{
            it.hideKeyboardFrom()
        }
    }

    //Image activity variables
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_FILE = 2

    private fun choosePicture() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {
            startActivityForResult(gallery, REQUEST_IMAGE_FILE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    private fun takePicture() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.upload.setImageBitmap(imageBitmap)
        }

        if (requestCode == REQUEST_IMAGE_FILE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            binding.upload.setImageURI(imageUri)
        }
    }

    private fun getImageByteArray(): ByteArray? {
        // Extract byteArray from imageView
        if(binding.upload.drawable == null){
            return null
        }
        else{
            val bitmap = (binding.upload.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            return data
        }
    }

    fun View.hideKeyboardFrom(){
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

}