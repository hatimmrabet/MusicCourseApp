package fi.oamk.musiccourseapp.findteacher.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.FragmentInfoBinding
import fi.oamk.musiccourseapp.user.User

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private val auth = Firebase.auth

    val args: InfoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateUI()

        if (auth.currentUser == null) {
            binding.reserveBtn.text = "Login"
            binding.reserveBtn.setOnClickListener {
                findNavController().navigate(R.id.action_infoFragment_to_loginFragment)
            }
        } else {
            val userDB = Firebase.database.getReference("users/${auth.currentUser.uid}")
            userDB.get().addOnSuccessListener {
                val user = User.from(it.value as HashMap<String, String>)
                if (user.role == "0") {
                    binding.reserveBtn.visibility = GONE
                }
            }
            binding.reserveBtn.setOnClickListener { goToReservation() }
        }
    }

    private fun goToReservation() {
        val action = InfoFragmentDirections.actionInfoFragmentToReservationFragment(args.name, args.uid)
        findNavController().navigate(action)
    }

    private fun populateUI() {
        val usersDB = Firebase.database.getReference("users/${args.uid}")
        val storage = Firebase.storage
        usersDB.get().addOnSuccessListener {
            val user = User.from(it.value as HashMap<String, String>)
            binding.fullnameTextView.text = user.fullname
            binding.instrumentTextView.text = user.email

            val httpsReference = storage.getReferenceFromUrl(user.picture!!)
            Glide.with(requireContext())
                .load(httpsReference)
                .into(binding.imageView)
        }
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        _binding = null
    }
}