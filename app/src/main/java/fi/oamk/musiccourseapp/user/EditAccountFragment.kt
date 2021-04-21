package fi.oamk.musiccourseapp.user

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentEditAccountBinding
import fi.oamk.musiccourseapp.databinding.FragmentPostsBinding
import java.io.ByteArrayOutputStream

class EditAccountFragment : Fragment() {
    private var _binding: FragmentEditAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var ref : DatabaseReference
    private lateinit var auth : FirebaseAuth

    private val TAG: String = fi.oamk.musiccourseapp.MainActivity::class.java.name

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

        binding.takePictureButton.setOnClickListener { takePicture() }
        binding.choosePictureButton.setOnClickListener { choosePicture() }

        binding.updateButton.setOnClickListener{
            ref = FirebaseDatabase.getInstance().reference.child("users").child(auth.currentUser.uid)
            val imageData = getImageByteArray()
            val storageRef = Firebase.storage.reference.child("images/${binding.email.text.toString()}")
            var uploadTask = storageRef.putBytes(imageData)
            uploadTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnCompleteListener { task ->
                        val url = task.result.toString()
                        ref.child("picture").setValue(url)
                    }
                } else {
                    Log.w(TAG, "Upload task was not succesful")
                }
            }
            if (binding.name.getText().toString() != "") {
                ref.child("fullname").setValue(binding.name.text.toString())
            }
            if (binding.email.getText().toString() != "") {
                auth.currentUser.updateEmail(binding.email.text.toString())
                ref.child("email").setValue(binding.email.text.toString())
            }
            if (binding.password.getText().toString() != "") {
                auth.currentUser.updatePassword(binding.password.text.toString())
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

    }

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

    private fun getImageByteArray(): ByteArray {
        // Extract byteArray from imageView
        val bitmap = (binding.upload.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        return data
    }

    fun View.hideKeyboardFrom(){
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}