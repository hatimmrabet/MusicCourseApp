package fi.oamk.musiccourseapp.posts

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
    private lateinit var postSelected: Post

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


        val postID = arguments?.getString("postID")

        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null)
                {
                    val postsFromDatabase = (snapshot.value as HashMap<String,ArrayList<Post>>).get("posts")

                    if(postsFromDatabase != null)
                    {
                        /*
                        for(i in 0..postsFromDatabase.size-1) {
                            val post: Post = Post.from(postsFromDatabase.get(i) as HashMap<String,String>)
                            if(post.id == postID)
                            {
                                binding.postInfoFullname.text = post.author
                                binding.postInfoInstrument.text = post.instrument
                                binding.postInfoDesc.text = post.description
                                Picasso.get().load(post.image).into(binding.postInfoImg)
                                break
                            }
                        }

                         */
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Post",error.toString())
            }
        }
        database.addValueEventListener(postListener)
        //Toast.makeText(this.context, database.child("posts")., Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}