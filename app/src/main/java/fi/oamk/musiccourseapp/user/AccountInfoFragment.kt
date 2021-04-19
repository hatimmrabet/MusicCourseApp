package fi.oamk.musiccourseapp.user

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.R.layout.fragment_account_info
import fi.oamk.musiccourseapp.databinding.FragmentAccountInfoBinding
import fi.oamk.musiccourseapp.databinding.FragmentPostsBinding
import fi.oamk.musiccourseapp.posts.MyAdapter
import fi.oamk.musiccourseapp.posts.Post

class AccountInfoFragment : Fragment(),  MyAdapter.OnPostListener {
    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var posts: ArrayList<Post>
    private lateinit var postsList: RecyclerView

    private var user: HashMap<String, Any> = HashMap<String, Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        database = Firebase.database.reference
        postsList = binding.postsList
        posts = arrayListOf<Post>()

        val userListener = database.child("users/${auth.currentUser.uid}").get().addOnSuccessListener {
            if(it.value != null){
                user = it.value as HashMap<String, Any>
                Picasso.get().load(user.get("picture").toString()).into(binding.userPicture)
                binding.welcomeText.text = "Wevlcome "+user.get("fullname").toString()
                binding.email.text = "Your email : "+user.get("email").toString()
                if(user.get("role").toString() == "0"){
                    binding.role.text = "Your role : teacher"
                }
                else{
                    binding.role.text = "Your role : student"
                    binding.textView2.text = ""
                }
            }
        }

        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_accountInfoFragment_to_editAccountFragment)
        }
        binding.floatingActionButton2.setOnClickListener{
            findNavController().navigate(R.id.action_accountInfoFragment_to_createPostFragment)
        }
        binding.button2.setOnClickListener{
            findNavController().navigate(R.id.action_accountInfoFragment_to_scheduleFragment)
        }
        binding.logoutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_accountInfoFragment_to_postsFragment)
        }

        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null)
                {
                    val postsFromDatabase = (snapshot.value as HashMap<String,HashMap<String,Any>>)["posts"]
                    posts.clear()



                    postsFromDatabase?.map { (key, value) ->
                        val post = Post.from(value as HashMap<String, Any>)
                        if(post.userkey.toString() == auth.currentUser.uid){
                            posts.add(post)
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
        postsList.adapter = MyAdapter(posts, this)
    }

    override fun onPostClick(position: Int) {
        val clickedItem : Post = posts[position]
        postsList.adapter?.notifyItemChanged(position)
        var bundle = bundleOf("postkey" to clickedItem.postkey )
        findNavController().navigate(R.id.action_accountInfoFragment_to_postInfoFragment,bundle )
        //Toast.makeText(this.context, "${clickedItem.postkey} clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}