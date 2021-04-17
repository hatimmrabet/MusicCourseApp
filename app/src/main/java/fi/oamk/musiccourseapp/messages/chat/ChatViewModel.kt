package fi.oamk.musiccourseapp.messages.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.messages.model.Chat
import fi.oamk.musiccourseapp.messages.model.Message

class ChatViewModel: ViewModel() {
    private val TAG = "ChatViewModel"
    private val temp_uid = "user1"

    val chatsDB = Firebase.database.getReference("chats")
    val membersDB = Firebase.database.getReference("members")

    private var _chats = MutableLiveData<ArrayList<Chat>>()
    val chats: LiveData<ArrayList<Chat>> get() = _chats


    fun getChats() {
        var newChats = arrayListOf<Chat>()
        _chats.value?.clear()
        chatsDB.child(temp_uid).get().addOnSuccessListener {
            it.children.forEach{child ->
                newChats.add(Chat.from(child.value as HashMap<String, String>))
            }
            _chats.value = newChats
            Log.d(TAG, _chats.value.toString())
        }
    }
}