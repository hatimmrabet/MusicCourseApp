package fi.oamk.musiccourseapp.messages.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.util.JsonMapper
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.messages.model.Chat
import fi.oamk.musiccourseapp.messages.model.Message

class ChatViewModel: ViewModel() {
    private val TAG = "ChatViewModel"
    private var temp_uid = "user1"
    val auth = Firebase.auth.currentUser

    val chatUsersDB = Firebase.database.getReference("chatUsers/${auth.uid}")

    private var _chats = MutableLiveData<ArrayList<Chat>>()
    val chats: LiveData<ArrayList<Chat>> get() = _chats

    fun getChats() {
        var newChats = arrayListOf<Chat>()
        _chats.value?.clear()
        chatUsersDB.get().addOnSuccessListener {
            it.children.forEach{chatUID ->
                val chatsDB = Firebase.database.getReference("chats/${chatUID.key.toString()}")
                chatsDB.get().addOnSuccessListener { chat ->
                    newChats.add(Chat.from(chat.value as HashMap<String, Any?>))
                    _chats.value = newChats
                }
                Log.d(TAG+"outside", newChats.toString())
            }
        }
    }
}