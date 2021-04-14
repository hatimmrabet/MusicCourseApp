package fi.oamk.musiccourseapp.posts

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
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

class PostsFragment : Fragment(), MyAdapter.OnPostListener {

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
                    val postsFromDatabase = (snapshot.value as HashMap<String,HashMap<String,Any>>).get("posts")
                    posts.clear()

                    postsFromDatabase?.map { (key, value) ->
                        val postFromDb = value as HashMap<String, Any>
                        val postkey = postFromDb.get("postkey").toString()
                        val user = postFromDb.get("userkey").toString()
                        val price = postFromDb.get("price").toString().toDouble()
                        val desc = postFromDb.get("description").toString()
                        val title = postFromDb.get("title").toString()
                        val instrument = postFromDb.get("instrument").toString()
                        val date = postFromDb.get("date").toString()
                        val post = Post(postkey,user,title,instrument,desc,price, date)
                        posts.add(post)
                    }
                    /*
                    if(postsFromDatabase != null)
                    {
                        for(i in 0..postsFromDatabase.size-1) {
                            if(postsFromDatabase.get(i) != null) {
                                val post: Post = Post.from(postsFromDatabase.get(i) as HashMap<String,String>)
                                posts.add(post)
                            }
                        }
                    }
                     */
                    postsList.adapter?.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Post",error.toString())
            }
        }
        database.addValueEventListener(postListener)
        postsList.setLayoutManager(LinearLayoutManager(view.getContext()));
        postsList.adapter = MyAdapter(posts, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPostClick(position: Int) {
        val clickedItem : Post = posts[position]
        postsList.adapter?.notifyItemChanged(position)
        var bundle = bundleOf("postkey" to clickedItem.postkey )
        findNavController().navigate(R.id.action_postsFragment_to_postInfoFragment,bundle )
        Toast.makeText(this.context, "${clickedItem.postkey} clicked", Toast.LENGTH_SHORT).show()
    }
}