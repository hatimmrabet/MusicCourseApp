package fi.oamk.musiccourseapp.messages.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.musiccourseapp.databinding.ItemChatBinding
import fi.oamk.musiccourseapp.messages.model.Chat
import fi.oamk.musiccourseapp.user.User


class ItemAdapter(private val dataset: ArrayList<Chat>, private val navController: NavController)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val TAG = "ChatFragmentItemAdapter"

    class ItemViewHolder(val binding: ItemChatBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val chat = dataset[position]
        var chatUID = ""
        if(chat.members[0] > chat.members[1]){
            chatUID = chat.members[1] + chat.members[0]
        } else {
            chatUID = chat.members[0] + chat.members[1]
        }

        //Determine other user
        val auth = Firebase.auth.currentUser
        val storage = Firebase.storage
        val userDB = Firebase.database.getReference("users")
        chat.members.forEach{member ->
            if (member != auth.uid)
                userDB.child(member).get().addOnSuccessListener {it ->
                    val user = User.from(it.value as HashMap<String, String>)
                    val httpsReference = storage.getReferenceFromUrl(user.picture!!)
                    Glide.with(holder.itemView.context)
                        .load(httpsReference)
                        .into(holder.binding.imageView)

                    holder.binding.lastMessageTextView.text = chat.lastMessage
                    holder.binding.titleTextView.text = user.fullname
                    holder.binding.cardView.setOnClickListener {
                        val action = ChatsFragmentDirections.actionChatsFragmentToMessagesFragment(chatUID,user.fullname!!)
                        navController.navigate(action)
                    }

                }
        }

    }
}