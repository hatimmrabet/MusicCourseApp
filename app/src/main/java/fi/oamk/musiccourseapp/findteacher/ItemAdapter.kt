package fi.oamk.musiccourseapp.findteacher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.musiccourseapp.databinding.ItemUserBinding
import fi.oamk.musiccourseapp.messages.model.Chat
import fi.oamk.musiccourseapp.user.User

class ItemAdapter(private val dataset: ArrayList<User>, private val navController: NavController)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private val TAG = "ChatFragmentItemAdapter"

    class ItemViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ItemViewHolder {
        return ItemViewHolder(
            ItemUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ItemAdapter.ItemViewHolder, position: Int) {
        val user = dataset[position]
        holder.binding.let{
            it.nameTextView.text = user.fullname
            it.priceTextView.text = user.email
        }

        holder.binding.messageButton.setOnClickListener { startConversation(user.uid!!, user.fullname!!) }

        val storage = Firebase.storage
        val httpsReference = storage.getReferenceFromUrl(user.picture!!)
        Glide.with(holder.itemView.context)
            .load(httpsReference)
            .into(holder.binding.imageView)
    }

    private fun startConversation(uid: String, name: String) {
        val chatUsersDatabase = Firebase.database.getReference("chatUsers")
        val chatsDatabase = Firebase.database.getReference("chats")
        val auth = Firebase.auth.currentUser

        // Generate key
        var chatUID = ""
        if(auth.uid > uid){
            chatUID = uid + auth.uid
        } else {
            chatUID = auth.uid + uid
        }

        // Populate database
        chatUsersDatabase.child(auth.uid).child(chatUID).setValue(true)
        chatUsersDatabase.child(uid).child(chatUID).setValue(true)

        chatsDatabase.child(chatUID).child("members").setValue(arrayListOf(auth.uid, uid))

        // Navigate to messages
        val action = FindTeacherFragmentDirections.actionFindTeacherFragmentToMessagesFragment(chatUID, name)
        navController.navigate(action)
    }


}