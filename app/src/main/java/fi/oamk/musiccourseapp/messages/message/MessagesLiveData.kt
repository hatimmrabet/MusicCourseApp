package fi.oamk.musiccourseapp.messages.message

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import fi.oamk.musiccourseapp.messages.model.Message

class MessagesLiveData(private var messagesRef: DatabaseReference)
    : MutableLiveData<ArrayList<Message>>() {
    private val TAG = "MessagesLiveData"

    private var listenerRegistration = DataEventListener()
    private var messages = arrayListOf<Message>()


    private inner class DataEventListener: ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "Error: ${error.toException()}")

        }

        override fun onDataChange(snapshot: DataSnapshot) {
            messages.clear()
            snapshot.children.forEach{child ->
                messages.add(Message.from(child.value as HashMap<String, String>))
            }
            setValue(messages)
            Log.d(TAG, "Got some messages")
        }
    }

    override fun onActive() {
        super.onActive()
        messagesRef.addValueEventListener(listenerRegistration)
    }

    override fun onInactive() {
        super.onInactive()
        messagesRef.removeEventListener(listenerRegistration)
    }
}