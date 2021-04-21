package fi.oamk.musiccourseapp.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
import fi.oamk.musiccourseapp.schedule.reservation.Date
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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
        rcDispoList.isNestedScrollingEnabled = false
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
                        val user = it.value as HashMap<String, Any>
                        binding.postInfoFullname.text = user.get("fullname").toString()
                        Picasso.get().load(user.get("picture").toString()).into(binding.postInfoImg)
                    }
                }

                //GET all hours from databse
                database.child("hours").child(post.get("postkey").toString()).get().addOnSuccessListener {
                    if(it.value != null)
                    {
                        val hoursdata = (it.value as HashMap<String, HashMap<String, Any>>)
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
                //rcDispoList.setLayoutManager(LinearLayoutManager(view.getContext()));
                rcDispoList.setLayoutManager(GridLayoutManager(view.context, 2))

            }
        }

        //Reservation Button
        binding.reserveBtn.setOnClickListener{
            val checkedHours : ArrayList<Hour> = ArrayList()

            for (hour in hours) {
                if (hour.checked) {
                    hour.reserved = true
                    database.child("hours").child(hour.hourkey).child("reserved").setValue(true)
                    //database.child("hours").child(hour.hourkey).setValue(hour)
                    checkedHours.add(hour)
                }
            }

            if(checkedHours.size != 0)
            {
                val auth = Firebase.auth.currentUser
                val dateUsersDB = Firebase.database.getReference("dateUsers")
                val datesDB = Firebase.database.getReference("dates")

                for( hour in checkedHours) {
                    val start = hour.start.substring(0, 2)+hour.start.substring(3, 5)
                    var endTime = (hour.start.substring(0, 2).toInt()+1).toString()
                    if (endTime.length == 1) { endTime = "0"+endTime }
                    val end = ( endTime + hour.start.substring(3, 5))

                    postListner.addOnSuccessListener {
                        if(it.value != null)
                        {
                            post = it.value as HashMap<String, Any>
                            var getDate = post.get("date").toString()
                            val date = getDate.substring(0, 4)+getDate.substring(5, 7)+getDate.substring(
                                8,
                                10
                            )
                            val reservation = Reservation(
                                post.get("userkey").toString(),
                                date,
                                start,
                                end,
                                auth.uid
                            )
                            val key = dateUsersDB.child(reservation.studentId).child(date).push().key
                            dateUsersDB.child(reservation.studentId).child(date).setValue(true)
                            dateUsersDB.child(auth.uid).child(date).setValue(true)
                            datesDB.child(auth.uid).child(date).child(key!!).setValue(
                                Date(
                                    reservation.start,
                                    reservation.end,
                                    reservation.studentId,
                                    auth.uid
                                )
                            )
                            datesDB.child(reservation.studentId).child(date).child(key!!).setValue(
                                Date(
                                    reservation.start,
                                    reservation.end,
                                    reservation.studentId,
                                    auth.uid
                                )
                            )
                        }
                    }
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

