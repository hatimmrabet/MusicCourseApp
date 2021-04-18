package fi.oamk.musiccourseapp.findteacher.reservation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.databinding.FragmentReservationBinding
import java.util.*

class ReservationFragment : Fragment() {

    private var _binding: FragmentReservationBinding? = null
    private val binding get() = _binding!!

    val args: ReservationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Calendar
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        binding.date.setOnClickListener{ chooseDate(year, month, day) }
        binding.start.setOnClickListener{ chooseStart(hour, minute) }
        binding.end.setOnClickListener { chooseEnd(hour, minute) }
        binding.reservationButton.setOnClickListener { confirmReservation() }
    }

    private fun confirmReservation() {
        val auth = Firebase.auth.currentUser
        val reservationUsersDB = Firebase.database.getReference("reservationUsers")
        val reservationsDB = Firebase.database.getReference("reservations")

        val date = binding.date.text.toString()
        val start = binding.start.text.toString()
        val end = binding.end.text.toString()
        val studentId = auth.uid


        val key = reservationsDB.push().key
        val reservation = Reservation(key!! ,date, start, end, studentId)
        reservationUsersDB.child(auth.uid).child(key!!).setValue(true)
        reservationUsersDB.child(args.uid).child(key!!).setValue(true)
        reservationsDB.child(key!!).setValue(reservation)

        Log.d("ReservationFragment", reservation.toString())
    }

    private fun chooseStart(hour: Int, minute: Int) {
        val tpd = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { timePicker, mHour, mMinute ->
            if(mMinute < 10){
                binding.start.setText(""+mHour+""+"0"+mMinute)
            } else {
                binding.start.setText(""+mHour+""+mMinute)
            }
        }, hour, minute, true)
        tpd.show()
    }

    private fun chooseEnd(hour: Int, minute: Int) {
        val tpd = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { timePicker, mHour, mMinute ->
            if(mMinute < 10){
                binding.end.setText(""+mHour+""+"0"+mMinute)
            } else {
                binding.end.setText(""+mHour+""+mMinute)
            }

        }, hour, minute, true)
        tpd.show()
    }

    private fun chooseDate(year: Int, month: Int, day: Int) {
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                val monthAndDayString = getMonthAndDay(mMonth, mDay)
                binding.date.setText(""+mYear + monthAndDayString)
            },
            year,
            month,
            day
        )
        dpd.show()
    }

    private fun getMonthAndDay(mMonth: Int, mDay: Int): String {
        var result = ""
        if(mMonth < 10){
            result = result + "0" +mMonth
        } else {
            result = result + mMonth
        }
        if(mDay < 10){
            result = result + "0" +mDay
        } else {
            result = result + mDay
        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





