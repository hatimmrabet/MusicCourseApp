package fi.oamk.musiccourseapp.messages.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.util.JsonMapper
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import fi.oamk.musiccourseapp.messages.model.Chat
import fi.oamk.musiccourseapp.messages.model.Message

class ChatViewModel: ViewModel() {
    private val TAG = "ChatViewModel"
    private var temp_uid = "user1"
    val auth = Firebase.auth.currentUser

//    val chatsDB = Firebase.database.getReference("chats")
    val membersDB = Firebase.database.getReference("members")

    val chatUsersDB = Firebase.database.getReference("chatUsers/${auth.uid}")

    private var _chats = MutableLiveData<ArrayList<Chat>>()
    val chats: LiveData<ArrayList<Chat>> get() = _chats


    fun getChats() {
//        var newChats = arrayListOf<Chat>()
//        _chats.value?.clear()
//        chatsDB.child(auth.uid!!).get().addOnSuccessListener {
//            it.children.forEach{child ->
//                newChats.add(Chat.from(child.value as HashMap<String, String>))
//            }
//            _chats.value = newChats
//            Log.d(TAG, _chats.value.toString())
//        }
        chatUsersDB.get().addOnSuccessListener {
            it.children.forEach{chat ->
                Log.d(TAG, chat.key.toString())
                getChatInfo(chat.key.toString())
            }
        }
    }

    private fun getChatInfo(chatUID: String) {
        val chatsDB = Firebase.database.getReference("chats/${chatUID}")
        chatsDB.get().addOnSuccessListener {
            Log.d(TAG, Chat.from(it.value as HashMap<String, Any?>).toString())
        }
    }
}