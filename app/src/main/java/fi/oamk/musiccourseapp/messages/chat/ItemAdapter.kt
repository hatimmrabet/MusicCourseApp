package fi.oamk.musiccourseapp.messages.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.musiccourseapp.databinding.ItemChatBinding
import fi.oamk.musiccourseapp.messages.model.Chat


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
//        holder.binding.let{
//            it.titleTextView.text = chat.lastMessage
//            if(chat.sender == "user1") {
//                it.lastMessageTextView.text = "You"
//            } else {it.lastMessageTextView.text = chat.sender}
//            it.cardView.setOnClickListener {
//                val action = ChatsFragmentDirections.actionChatsFragmentToMessagesFragment(chat.id, chat.id)
//                navController.navigate(action)
//            }
//        }
    }
}