package fi.oamk.musiccourseapp.findteacher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fi.oamk.musiccourseapp.databinding.ItemUserBinding
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
        }

        val storage = Firebase.storage
        val httpsReference = storage.getReferenceFromUrl(user.picture!!)
        Glide.with(holder.itemView.context)
            .load(httpsReference)
            .into(holder.binding.imageView)
    }

}