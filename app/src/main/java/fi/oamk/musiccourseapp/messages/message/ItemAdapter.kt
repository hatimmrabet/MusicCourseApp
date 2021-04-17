package fi.oamk.musiccourseapp.messages.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.musiccourseapp.databinding.ItemMessageBinding
import fi.oamk.musiccourseapp.messages.model.Message

class ItemAdapter(private val dataset: ArrayList<Message>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    private val TAG = "ChatFragmentItemAdapter"

    class ItemViewHolder(val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val message = dataset[position]
        holder.binding.contentTextView.text = message.content
        holder.binding.authorTextView.text = "Sent by "+message.sender + " at" +
                message.time
    }
}