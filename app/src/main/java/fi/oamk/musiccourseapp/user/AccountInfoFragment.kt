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
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.R.layout.fragment_account_info
import fi.oamk.musiccourseapp.databinding.FragmentAccountInfoBinding
import fi.oamk.musiccourseapp.databinding.FragmentPostsBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import fi.oamk.musiccourseapp.posts.Hour
import fi.oamk.musiccourseapp.posts.MyAdapter
import fi.oamk.musiccourseapp.posts.Post

class AccountInfoFragment : Fragment(), MyAdapter.OnPostListener {
    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var posts: ArrayList<Post>
    private lateinit var postsList: RecyclerView
    private lateinit var hours: ArrayList<Hour>
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        database = Firebase.database.reference
        postsList = binding.postsLists
        posts = arrayListOf<Post>()
        hours = arrayListOf()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            database.child("users").child("${auth.currentUser.uid}").get().addOnSuccessListener{
                val profil = it.value as HashMap<String, Any>
                binding.name.text = profil.get("fullname").toString()
                if (it.value != null)
                {
                    val user = it.value as HashMap<String, Any>
                    Picasso.get().load(user.get("picture").toString()).into(binding.image)
                }

                if (profil.get("role").toString() == "0")
                {
                    binding.role.text = "Teacher"
                }
                else
                    binding.role.text = "Student"
            }

        binding.email.text=auth.currentUser.email


        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null)
                {
                    val postsFromDatabase = (snapshot.value as HashMap<String, HashMap<String, Any>>)["posts"]
                    val hoursFromDatabase = (snapshot.value as HashMap<String, HashMap<String, Any>>)["hours"]
                    posts.clear()
                    hours.clear()

                    postsFromDatabase?.map { (MapPostKey, MapPostValue) ->
                        val post = Post.from(MapPostValue as HashMap<String, Any>)
                        val postHoursDB = (hoursFromDatabase?.get(MapPostKey) as HashMap<String, Any>)
                        postHoursDB.map { (hourKey, hourValue) ->
                            val hour = Hour.from(hourValue as HashMap<String, Any>)
                            if (!hour.reserved)
                            {
                                hours.add(hour)
                            }
                        }

                        if(hours.size > 0)
                        {
                            posts.add(post)
                            hours.clear()
                        }

                    }
                    postsList.adapter?.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Post", error.toString())
            }
        }
        database.addValueEventListener(postListener)
        postsList.setLayoutManager(LinearLayoutManager(view.getContext()));
        postsList.adapter = MyAdapter(posts, this)


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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPostClick(position: Int) {
        val clickedItem : Post = posts[position]
        postsList.adapter?.notifyItemChanged(position)
        var bundle = bundleOf("postkey" to clickedItem.postkey)
        findNavController().navigate(R.id.action_accountInfoFragment_to_postInfoFragment2, bundle)
    }
}