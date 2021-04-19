package fi.oamk.musiccourseapp.messages.message

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.databinding.ItemMessageLeftBinding
import fi.oamk.musiccourseapp.messages.model.Message

class ItemAdapter(private val dataset: ArrayList<Message>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    private val TAG = "ChatFragmentItemAdapter"
    private val MSG_TYPE_LEFT = 0
    private val MSG_TYPE_RIGHT = 1

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val contentTextView : TextView
        val authorTextView : TextView

        init {
            // Define click listener for the ViewHolder's View.
            contentTextView = view.findViewById(R.id.content_text_view)
            authorTextView = view.findViewById(R.id.author_text_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        if(viewType == MSG_TYPE_RIGHT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_right, parent, false)
            Log.d(TAG ,"Will return RIGHT with ${viewType}")
            return ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message_left, parent, false)
            Log.d(TAG ,"Will return LEFT with ${viewType}")
            return ItemViewHolder(view)
        }
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val message = dataset[position]
        holder.contentTextView.text = message.content
        holder.authorTextView.text = "Sent by "+message.sender + " at" +
                message.time
    }

    override fun getItemViewType(position: Int): Int {
        val user = Firebase.auth.currentUser
        if(dataset[position].sender == user.email) {
            return MSG_TYPE_RIGHT
            Log.d(TAG ,"Will return RIGHT")
        } else {
            return MSG_TYPE_LEFT
            Log.d(TAG ,"Will return LEFT")
        }
    }
}