package fi.oamk.musiccourseapp.user

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.databinding.FragmentCreatePostBinding
import fi.oamk.musiccourseapp.posts.Post
import java.util.*

class CreatePostFragment : Fragment() {
    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var title : EditText
    private lateinit var description : EditText
    private lateinit var instrument : EditText
    private lateinit var price : EditText
    private lateinit var datePicker : TextInputLayout
    private lateinit var hour1: CheckBox
    private lateinit var hour2: CheckBox
    private lateinit var hour3: CheckBox
    private lateinit var hour4: CheckBox
    private lateinit var hour5: CheckBox
    private lateinit var hour6: CheckBox
    private lateinit var hour7: CheckBox
    private lateinit var hour8: CheckBox
    private lateinit var hour9: CheckBox
    private lateinit var hour10: CheckBox
    private lateinit var hour11: CheckBox
    private lateinit var hour12: CheckBox
    private lateinit var submit: Button

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        title = binding.editText
        description= binding.editTextTextPersonName
        instrument= binding.editText1
        //datePicker = binding.datePicker
        price= binding.price

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val constraintsBuilder = CalendarConstraints.Builder()
                                    .setValidator(DateValidatorPointForward.now())

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                .setCalendarConstraints(constraintsBuilder.build())
                .build()


        binding.selectButton.setOnClickListener {
            datePicker.show(requireFragmentManager(), "tag");
            datePicker.addOnPositiveButtonClickListener {
                // Respond to positive button click.
                //binding.displayDate.hint = datePicker.headerText
                val calendar = Calendar.getInstance()
                calendar.time = Date(it)
                var day : String = calendar.get(Calendar.DAY_OF_MONTH).toString()
                var month: String = (calendar.get(Calendar.MONTH) + 1).toString()
                var year: String = calendar.get(Calendar.YEAR).toString()
                if(day.length == 1) {day="0"+day}
                if(month.length == 1) {month="0"+month}
                if(year.length != 4) {year="0"+year}

                binding.displayDate.setText("$year/$month/$day")
            }
            datePicker.addOnNegativeButtonClickListener {
                // Respond to negative button click.
            }
            datePicker.addOnCancelListener {
                // Respond to cancel button click.
            }
            datePicker.addOnDismissListener {
                // Respond to dismiss events.
            }
        }

        /*
        binding.buttonPickDate.setOnClickListener {

            // Create the date picker builder and set the title
            val builder = MaterialDatePicker.Builder.datePicker()
                .also {
                    title.text = "Pick Date"
                }


            // create the date picker
            val datePicker = builder.build()

            // set listener when date is selected
            datePicker.addOnPositiveButtonClickListener {

                // Create calendar object and set the date to be that returned from selection
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.time = Date(it)
                binding.textView.text = "${calendar.get(Calendar.DAY_OF_MONTH)}- " +
                        "${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.YEAR)}"

            }

            datePicker.show(supportFragmentManager, "MyTAG")

        }
*/
        /*
        binding.datePicker.setOnClickListener {
            val tpd = DatePickerDialog(it.context, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                var dateDay : String = mDay.toString()
                var dateMonth : String = mMonth.toString()
                var dateYear : String = mYear.toString()
                if (dateMonth.toString().length == 1)
                {
                    dateMonth="0"+dateMonth
                }
                if (dateDay.length == 1)
                {
                    dateDay="0"+dateDay
                }
                binding.datePicker.hint = "$dateYear/$dateMonth/$dateDay"
            }, year, month, day)
            tpd.show()
        }
        */

        database = Firebase.database.reference
        hour1 = binding.checkBox5
        hour2 = binding.checkBox6
        hour3 = binding.checkBox7
        hour4 = binding.checkBox9
        hour5= binding.checkBox10
        hour6 = binding.checkBox11
        hour7 = binding.checkBox12
        hour8 = binding.checkBox13
        hour9 = binding.checkBox14
        hour10 = binding.checkBox15
        hour11 = binding.checkBox16
        hour12 = binding.checkBox17
        submit = binding.button3

        val submit = binding.button3

        submit.setOnClickListener{
            val title = title.text.toString()
            val description= description.text.toString()
            val instrument = instrument.text.toString()
            val price = price.text.toString()
            val date= datePicker.toString()
            if (title == "" || description == "" || instrument == "" || price == "" || date == ""){
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
            else {
            val key = database.child("posts").push().key.toString()
            val key1 = database.child("hours").push().key.toString()
            val key2 = database.child("hours").push().key.toString()
            val key3 = database.child("hours").push().key.toString()
            val key4 = database.child("hours").push().key.toString()
            val key5 = database.child("hours").push().key.toString()
            val key6 = database.child("hours").push().key.toString()
            val key7 = database.child("hours").push().key.toString()
            val key8 = database.child("hours").push().key.toString()
            val key9 = database.child("hours").push().key.toString()
            val key10 = database.child("hours").push().key.toString()
            val key11 = database.child("hours").push().key.toString()
            val key12 = database.child("hours").push().key.toString()
            val user = Firebase.auth.currentUser

                    val post = Post(key,auth.currentUser.uid, title,instrument,description,price.toDouble(),date)
                    database.child("posts").child(post.postkey).setValue(post)
                    //database.child("posts").child(key).child("postkey").setValue(key)
                    //database.child("posts").child(key).child("description").setValue(description)
                    //database.child("posts").child(key).child("instrument").setValue(instrument)
                    //database.child("posts").child(key).child("price").setValue(price)
                    //database.child("posts").child(key).child("date").setValue(date)
                   /* user?.let {
                        val uid = user.uid
                        database.child("posts").child(key).child("userkey").setValue(uid)
                    }*/
                    if (hour1.isChecked) {

                        database.child("hours").child(key1).child("start")
                            .setValue(hour1.text.toString())
                        database.child("hours").child(key1).child("reserved").setValue(false)
                        database.child("hours").child(key1).child("hourkey").setValue(key1)
                        database.child("hours").child(key1).child("postkey").setValue(key)
                    }



                    if (hour2.isChecked) {
                        database.child("hours").child(key2).child("start")
                            .setValue(hour2.text.toString())
                        database.child("hours").child(key2).child("reserved").setValue(false)
                        database.child("hours").child(key2).child("hourkey").setValue(key2)
                        database.child("hours").child(key2).child("postkey").setValue(key)
                    }


                    if (hour3.isChecked) {
                        database.child("hours").child(key3).child("start")
                            .setValue(hour3.text.toString())
                        database.child("hours").child(key3).child("reserved").setValue(false)
                        database.child("hours").child(key3).child("hourkey").setValue(key3)
                        database.child("hours").child(key3).child("postkey").setValue(key)
                    }


                    if (hour4.isChecked) {
                        database.child("hours").child(key4).child("start")
                            .setValue(hour4.text.toString())
                        database.child("hours").child(key4).child("reserved").setValue(false)
                        database.child("hours").child(key4).child("hourkey").setValue(key4)
                        database.child("hours").child(key4).child("postkey").setValue(key)
                    }



                    if (hour5.isChecked) {
                        database.child("hours").child(key5).child("start")
                            .setValue(hour5.text.toString())
                        database.child("hours").child(key5).child("reserved").setValue(false)
                        database.child("hours").child(key5).child("hourkey").setValue(key5)
                        database.child("hours").child(key5).child("postkey").setValue(key)
                    }



                    if (hour6.isChecked) {
                        database.child("hours").child(key6).child("start")
                            .setValue(hour6.text.toString())
                        database.child("hours").child(key6).child("reserved").setValue(false)
                        database.child("hours").child(key6).child("hourkey").setValue(key6)
                        database.child("hours").child(key6).child("postkey").setValue(key)
                    }



                    if (hour7.isChecked) {
                        database.child("hours").child(key7).child("start")
                            .setValue(hour7.text.toString())
                        database.child("hours").child(key7).child("reserved").setValue(false)
                        database.child("hours").child(key7).child("hourkey").setValue(key7)
                        database.child("hours").child(key7).child("postkey").setValue(key)
                    }



                    if (hour8.isChecked) {
                        database.child("hours").child(key8).child("start")
                            .setValue(hour8.text.toString())
                        database.child("hours").child(key8).child("reserved").setValue(false)
                        database.child("hours").child(key8).child("hourkey").setValue(key8)
                        database.child("hours").child(key8).child("postkey").setValue(key)
                    }



                    if (hour9.isChecked) {
                        database.child("hours").child(key9).child("start")
                            .setValue(hour9.text.toString())
                        database.child("hours").child(key9).child("reserved").setValue(false)
                        database.child("hours").child(key9).child("hourkey").setValue(key9)
                        database.child("hours").child(key9).child("postkey").setValue(key)
                    }



                    if (hour10.isChecked) {
                        database.child("hours").child(key10).child("start")
                            .setValue(hour10.text.toString())
                        database.child("hours").child(key10).child("reserved").setValue(false)
                        database.child("hours").child(key10).child("hourkey").setValue(key10)
                        database.child("hours").child(key10).child("postkey").setValue(key)
                    }



                    if (hour11.isChecked) {
                        database.child("hours").child(key11).child("start")
                            .setValue(hour11.text.toString())
                        database.child("hours").child(key11).child("reserved").setValue(false)
                        database.child("hours").child(key11).child("hourkey").setValue(key11)
                        database.child("hours").child(key11).child("postkey").setValue(key)
                    }



                    if (hour12.isChecked) {
                        database.child("hours").child(key12).child("start")
                            .setValue(hour12.text.toString())
                        database.child("hours").child(key12).child("reserved").setValue(false)
                        database.child("hours").child(key12).child("hourkey").setValue(key12)
                        database.child("hours").child(key12).child("postkey").setValue(key)
                    }
                }
            }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

