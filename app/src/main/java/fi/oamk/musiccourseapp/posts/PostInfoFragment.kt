package fi.oamk.musiccourseapp.posts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentPostInfoBinding
import fi.oamk.musiccourseapp.findteacher.reservation.Reservation

class PostInfoFragment : Fragment() {

    private var _binding: FragmentPostInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var post: HashMap<String, Any> = HashMap<String, Any>()
    private lateinit var rcDispoList : RecyclerView
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
        auth = Firebase.auth
        val postkey = arguments?.getString("postkey")
        rcDispoList = binding.disponibilityList
        hours = ArrayList<Hour>()

        val postListner = database.child("posts/${postkey}").get().addOnSuccessListener {
            if(it.value != null)
            {
                post = it.value as HashMap<String, Any>
                binding.postInfoInstrument.text = post.get("instrument").toString()
                binding.postInfoDesc.text = post.get("description").toString()
                binding.postInfoDate.text = post.get("date").toString()
                binding.postInfoPrice.text = post.get("price").toString() + " €"

                val profileListner = database.child("users/${post.get("userkey").toString()}").get().addOnSuccessListener {
                    if (it.value != null) {
                        val user = it.value as HashMap<String,Any>
                        binding.postInfoFullname.text = user.get("fullname").toString()
                        Picasso.get().load(user.get("picture").toString()).into(binding.postInfoImg)
                    }
                }
            }
        }

        /*
        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null)
                {
                    post = snapshot.value as HashMap<String, Any>
                    binding.postInfoInstrument.text = post.get("instrument").toString()
                    binding.postInfoDesc.text = post.get("description").toString()
                    binding.postInfoDate.text = post.get("date").toString()
                    binding.postInfoPrice.text = post.get("price").toString() + " €"

                    val profilListener = object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.value != null) {
                                val user = snapshot.value as HashMap<String,Any>
                                binding.postInfoFullname.text = user.get("fullname").toString()
                                Picasso.get().load(user.get("picture").toString()).into(binding.postInfoImg)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.d("Profil",error.toString())
                        }
                    }
                    database.child("users").child(post.get("userkey").toString()).addValueEventListener(profilListener)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Post",error.toString())
            }
        }
        database.child("posts").child("$postkey").addValueEventListener(postListener)
        */

        //GET all hours from databse
        database.child("hours").get().addOnSuccessListener {
            if(it.value != null)
            {
                val hoursdata = (it.value as HashMap<String,HashMap<String,Any>>)
                hours.clear()

                hoursdata?.map { (key, value) ->
                    val hour = Hour.from(value)
                    // GET only hours related to the post and not reserved yet
                    //println("==> ${hour.postkey} ${hour.hourkey} ${hour.reserved}")
                    if(!hour.reserved && hour.postkey == postkey){
                        hours.add(hour)
                    }
                }
                if(hours.size == 0)
                {
                    binding.noDispo.visibility = VISIBLE
                    binding.reserveBtn.visibility = INVISIBLE
                }
                else
                {
                    binding.noDispo.visibility = INVISIBLE
                    binding.reserveBtn.visibility = VISIBLE
                }
                rcDispoList.adapter?.notifyDataSetChanged()
            }
        }
        rcDispoList.adapter = HoursAdapter(hours)
        rcDispoList.setLayoutManager(LinearLayoutManager(view.getContext()));

        /*
        val hoursListner = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.value != null)
                {
                    val Hoursdata = (snapshot.value as HashMap<String,HashMap<String,Any>>).get("hours")
                    hours.clear()

                    Hoursdata?.map { (key, value) ->
                        val hour = Hour.from(value as HashMap<String, Any>)
                        // GET only hours related to the post and not reserved yet
                        println("==> ${hour.postkey} ${hour.hourkey} ${hour.reserved}")
                        if(!hour.reserved && hour.postkey == postkey){
                            hours.add(hour)
                        }
                    }
                    if(hours.size == 0)
                    {
                        binding.noDispo.visibility = VISIBLE
                        binding.reserveBtn.visibility = INVISIBLE
                    }else
                    {
                        binding.noDispo.visibility = INVISIBLE
                        binding.reserveBtn.visibility = VISIBLE
                    }
                    rcDispoList.adapter?.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("Hours",error.toString())
            }
        }
        database.addValueEventListener(hoursListner)
        rcDispoList.adapter = HoursAdapter(hours)
        rcDispoList.setLayoutManager(LinearLayoutManager(view.getContext()));
        */

        //Reservation Button
        binding.reserveBtn.setOnClickListener{
            val checkedHours : ArrayList<Hour> = ArrayList()

            for (hour in hours) {
                if (hour.checked) {
                    hour.reserved = true
                    database.child("hours").child(hour.hourkey).child("reserved").setValue(true)
                    //database.child("hours").child(hour.hourkey).setValue(hour)
                    //println("------- ${hour.start} ${hour.postkey}")
                    checkedHours.add(hour)
                }
            }

            if(checkedHours.size != 0)
            {
                val postInfo = postListner.result?.value as HashMap<String, *>
                for( hour in checkedHours) {
                    val reskey = database.child("allReservations").child("${auth.currentUser.uid}")
                        .push().key
                    val reservation = reskey?.let { it1 ->
                        fi.oamk.musiccourseapp.posts.Reservation(
                            it1,
                            postInfo.get("userkey").toString(),
                            hour.postkey,
                            hour.hourkey,
                            postInfo.get("date").toString(),
                            hour.start,
                            postInfo.get("price").toString()
                        )
                    }
                    database.child("allReservations").child("${auth.currentUser.uid}")
                        .child("${reskey}").setValue(reservation)
                }
                view.findNavController().navigate(R.id.action_postInfoFragment_to_reservationRecapFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

