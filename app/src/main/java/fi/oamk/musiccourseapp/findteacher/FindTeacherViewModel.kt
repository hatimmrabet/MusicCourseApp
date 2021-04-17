package fi.oamk.musiccourseapp.findteacher

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.user.User

class FindTeacherViewModel: ViewModel() {
    private val TAG = "FindTeacherViewModel"

    private val usersDB = Firebase.database.getReference("users")

    private var _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>> get() = _users

    fun getUsers() {
        var newUsers = arrayListOf<User>()
        _users.value?.clear()
        usersDB.get().addOnSuccessListener {
            it.children.forEach{child ->
                newUsers.add(User.from(child.value as HashMap<String, String>))
            }
            _users.value = newUsers
            Log.d(TAG, _users.value.toString())
        }
    }
}