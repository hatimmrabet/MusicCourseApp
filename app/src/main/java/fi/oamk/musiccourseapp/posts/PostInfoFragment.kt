package fi.oamk.musiccourseapp.posts

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentBookingBinding
import fi.oamk.musiccourseapp.databinding.FragmentMessagesBinding
import fi.oamk.musiccourseapp.databinding.FragmentPostInfoBinding

class PostInfoFragment : Fragment() {

    private var _binding: FragmentPostInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private var post: HashMap<String, Any> = HashMap<String, Any>()
    private lateinit var recycler_view : RecyclerView
    private lateinit var hours: ArrayList<Hour>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.reference
        val postkey = arguments?.getString("postkey")
        recycler_view = binding.disponibilityList
        hours = ArrayList<Hour>()

        //GET all posts from database
        database.child("posts").child(postkey!!).get().addOnSuccessListener {
            if(it.value != null) {
                post = it.value as HashMap<String, Any>
                binding.postInfoInstrument.text = post.get("instrument").toString()
                binding.postInfoDesc.text = post.get("description").toString()
                binding.postDate.text = post.get("date").toString()

                // GET user info
                database.child("users").child(post.get("userkey").toString()).get().addOnSuccessListener {
                    if(it.value != null)
                    {
                        val user = it.value as HashMap<String,Any>
                        binding.postInfoFullname.text = user.get("fullname").toString()
                        Picasso.get().load(user.get("picture").toString()).into(binding.postInfoImg)
                    }
                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        //GET all ours from databse
        database.child("hours").get().addOnSuccessListener {
            if(it.value != null)
            {
                val Hoursdata = it.value as HashMap<String,Any>
                Hoursdata?.map { (key, value) ->
                    val hour = Hour.from(value as HashMap<String, Any>)
                    // GET only hours related to the post and not reserved yet
                    if(hour.reserved != true && hour.postkey == postkey){
                        hours.add(hour)
                    }
                }
                recycler_view.adapter = HoursAdapter(hours)
                recycler_view.setLayoutManager(LinearLayoutManager(view.getContext()));
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}