package fi.oamk.musiccourseapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class ActivityViewModel: ViewModel() {
    private var _user = FirebaseUserLiveData()
    val user: LiveData<FirebaseUser?> get() = _user

}