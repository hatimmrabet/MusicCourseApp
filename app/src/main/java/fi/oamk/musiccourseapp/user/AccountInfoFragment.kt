package fi.oamk.musiccourseapp.user

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
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
import fi.oamk.musiccourseapp.databinding.FragmentAccountInfoBinding
import fi.oamk.musiccourseapp.posts.Hour
import fi.oamk.musiccourseapp.posts.MyPostAdapter
import fi.oamk.musiccourseapp.posts.Post

class AccountInfoFragment : Fragment(), MyPostAdapter.OnPostListener {
    private var _binding: FragmentAccountInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var posts: ArrayList<Post>
    private lateinit var postsList: RecyclerView
    private lateinit var hours: ArrayList<Hour>
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var loggedUser: User
    private lateinit var myMenu: Menu


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_edit_account_menu, menu)
        myMenu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_new_post -> {
                findNavController().navigate(R.id.action_accountInfoFragment_to_createPostFragment)
                return true
            }
            R.id.edit_account -> {
                findNavController().navigate(R.id.action_accountInfoFragment_to_editAccountFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        database = Firebase.database.reference
        postsList = binding.postsLists
        posts = arrayListOf<Post>()
        hours = arrayListOf()

        database.child("users").child("${auth.currentUser.uid}").get().addOnSuccessListener {
            val profil = it.value as HashMap<String, Any>
            binding.name.text = profil.get("fullname").toString()
            if (it.value != null) {
                val user = it.value as HashMap<String, Any>
                Picasso.get().load(user.get("picture").toString()).into(binding.image)
            }

            if (profil.get("role").toString() == "0") {
                binding.role.text = "Teacher"
            } else {
                binding.role.text = "Student"
                binding.posts.setVisibility(View.INVISIBLE)
                myMenu.findItem(R.id.add_new_post).isVisible = false
                myMenu.findItem(R.id.add_new_post).isEnabled = false

            }
        }

        binding.email.text = auth.currentUser.email


        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value != null) {
                    val postsFromDatabase = (snapshot.value as HashMap<String, HashMap<String, Any>>)["posts"]
                    posts.clear()


                    postsFromDatabase?.map { (MapPostKey, MapPostValue) ->
                        val post = Post.from(MapPostValue as HashMap<String, Any>)

                        if (post.userkey == auth.uid) {
                            posts.add(post)
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
        postsList.adapter = MyPostAdapter(posts, this)


        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_accountInfoFragment_to_scheduleFragment)
        }

        binding.logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_accountInfoFragment_to_postsFragment)
        }
    }

    private fun getLoggedUser(userId: String) {
        database.child("users/$userId").get().addOnSuccessListener {
            if (it.value != null) {
                loggedUser = User.from(it.value as HashMap<String, String>)
            }
        }
    }

    override fun onPostClick(position: Int) {
        val clickedItem: Post = posts[position]
        postsList.adapter?.notifyItemChanged(position)
        var bundle = bundleOf("postkey" to clickedItem.postkey)
        findNavController().navigate(R.id.action_accountInfoFragment_to_postInfoFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}