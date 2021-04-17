package fi.oamk.musiccourseapp.messages.message

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.messages.model.Message
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MessageViewModel: ViewModel() {
    private val TAG = "MessageViewModel"

    private val database = Firebase.database
    private val auth = Firebase.auth

    // ChatFragment
    private var messagesRef = database.getReference("messages")
    private var _messages = MutableLiveData<ArrayList<Message>>()
    val messages: LiveData<ArrayList<Message>> get() = _messages
    fun setMessagesRef(id: String){
        Log.d(TAG, "Will set message reference")
        messagesRef = database.getReference("messages").child(id)
        _messages =
            MessagesLiveData(messagesRef)
    }

    fun addMessage(message: String) {
        // Get current time
        val formatted = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))

        // Get user if any
        val userEmail = if (auth.currentUser != null) auth.currentUser.email else "Unknown"
        messagesRef.push().setValue(Message(message, userEmail, formatted))
        Log.i("ChatViewModel", "add message function")
    }
}