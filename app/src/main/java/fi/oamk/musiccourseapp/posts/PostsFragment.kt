package fi.oamk.musiccourseapp.posts

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentBookingBinding
import fi.oamk.musiccourseapp.databinding.FragmentMessagesBinding
import fi.oamk.musiccourseapp.databinding.FragmentPostsBinding

class PostsFragment : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!
    private lateinit var posts: ArrayList<Post>
    private lateinit var database: DatabaseReference
    private lateinit var postsList: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.reference
        postsList = binding.postsList
        posts = arrayListOf<Post>()

        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null)
                {
                    val postsFromDatabase = (snapshot.value as HashMap<Int,ArrayList<Post>>).get("posts")
                    posts.clear()

                    if(postsFromDatabase != null)
                    {
                        for(i in 0..postsFromDatabase.size-1) {
                            if(postsFromDatabase.get(i) != null) {
                                val post: Post = Post.from(postsFromDatabase.get(i) as HashMap<String,String>)
                                posts.add(post)
                            }
                        }
                    }
                    postsList.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Post",error.toString())
            }
        }

        database.addValueEventListener(postListener)
        postsList.setLayoutManager(LinearLayoutManager(view.getContext()));
        postsList.adapter = MyAdapter(posts)
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}