package fi.oamk.musiccourseapp.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentPostInfoBinding
import fi.oamk.musiccourseapp.findteacher.reservation.Reservation
import fi.oamk.musiccourseapp.schedule.reservation.Date
import fi.oamk.musiccourseapp.user.User

class PostInfoFragment : Fragment() {

    private var _binding: FragmentPostInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var post: Post
    private lateinit var user: User
    private var loggedUser: User? = null
    private lateinit var rcDispoList: RecyclerView
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

        if (auth.currentUser != null)
        {
            getLoggedUser(auth.currentUser.uid)
        } else {
            binding.reserveBtn.visibility = INVISIBLE
            binding.loginBtn.visibility = VISIBLE
        }

        database.child("posts/${postkey}").get().addOnSuccessListener {
            if (it.value != null) {
                post = Post.from(it.value as HashMap<String, Any>)
                binding.postInfoTitle.text = post.title
                binding.postInfoInstrument.text = post.instrument
                binding.postInfoDesc.text = post.description
                binding.postInfoDate.text = post.date
                binding.postInfoPrice.text = "${post.price} €"

                if (loggedUser?.role == "0") {
                    binding.reserveBtn.visibility = INVISIBLE
                }

                database.child("users/${post.userkey}").get().addOnSuccessListener {
                        if (it.value != null) {
                            user = User.from(it.value as HashMap<String, String>)
                            binding.postInfoFullname.text = user.fullname
                            Picasso.get().load(user.picture).into(binding.postInfoImg)
                        }
                    }

                //GET all hours from databse
                database.child("hours").child(post.postkey).get().addOnSuccessListener {
                    if (it.value != null) {
                        val hoursdata = (it.value as HashMap<String, HashMap<String, Any>>)
                        hours.clear()

                        hoursdata.map { (key, value) ->
                            val hour = Hour.from(value)
                            // GET only hours related to the post and not reserved yet
                            //println("==> ${hour.postkey} ${hour.hourkey} ${hour.reserved}")
                            if (!hour.reserved && hour.postkey == postkey) {
                                hours.add(hour)
                            }
                        }
                        rcDispoList.adapter?.notifyDataSetChanged()
                    }
                }
                rcDispoList.adapter = HoursAdapter(hours)
                //rcDispoList.setLayoutManager(LinearLayoutManager(view.getContext()));
                rcDispoList.layoutManager = GridLayoutManager(view.context, 2)

            }
        }

        //login button
        binding.loginBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_postInfoFragment_to_loginFragment)
        }

        //Reservation Button
        binding.reserveBtn.setOnClickListener {
            val checkedHours: ArrayList<Hour> = ArrayList()

            for (hour in hours) {
                if (hour.checked) {
                    hour.reserved = true
                    checkedHours.add(hour)
                }
            }

            if (loggedUser!!.credit?.toDouble()!! >= checkedHours.size * post.price) {

                for (hour in checkedHours) {
                    database.child("hours").child(hour.postkey).child(hour.hourkey).child("reserved").setValue(true)
                }

                if (checkedHours.size != 0) {
                    val auth = Firebase.auth.currentUser
                    val dateUsersDB = Firebase.database.getReference("dateUsers")
                    val datesDB = Firebase.database.getReference("dates")

                    for (hour in checkedHours) {
                        val start = hour.start.substring(0, 2) + hour.start.substring(3, 5)
                        var endTime = (hour.start.substring(0, 2).toInt() + 1).toString()
                        if (endTime.length == 1) {
                            endTime = "0" + endTime
                        }
                        val end = (endTime + hour.start.substring(3, 5))

                        var getDate = post.date
                        val date = getDate.substring(0, 4) + getDate.substring(5, 7) + getDate.substring(8, 10)
                        val reservation = Reservation(post.userkey, date, start, end, auth.uid)
                        val key = dateUsersDB.child(reservation.studentId).child(date).push().key

                        dateUsersDB.child(reservation.studentId).child(date).setValue(true)

                        dateUsersDB.child(auth.uid).child(date).setValue(true)

                        datesDB.child(auth.uid).child(date).child(key!!).setValue(
                            Date(reservation.start, reservation.end, reservation.studentId, auth.uid)
                        )
                        datesDB.child(reservation.studentId).child(date).child(key)
                            .setValue(
                                Date(reservation.start, reservation.end, reservation.studentId, auth.uid)
                            )
                    }

                    //Money transaction
                    val newCreditLoggedUser = loggedUser!!.credit?.toDouble()!! - checkedHours.size * post.price
                    database.child("users/${loggedUser!!.uid}").child("credit").setValue(newCreditLoggedUser.toString())
                    var newCreditTeacher = user.credit?.toDouble()?.plus(checkedHours.size * post.price)
                    if (newCreditTeacher != null) {
                        newCreditTeacher *= 0.9
                    }
                    database.child("users/${user.uid}").child("credit").setValue(newCreditTeacher.toString())

                    view.findNavController().navigate(R.id.action_postInfoFragment_to_reservationRecapFragment)
                }
            } else {
                Toast.makeText(
                    context,
                    "You don't have enough credit for this operation",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun getLoggedUser(userId: String) {
        database.child("users/$userId").get().addOnSuccessListener {
            if (it.value != null) {
                loggedUser = User.from(it.value as HashMap<String, String>)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

