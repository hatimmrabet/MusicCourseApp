package fi.oamk.musiccourseapp.schedule.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fi.oamk.musiccourseapp.databinding.ItemDateBinding
import fi.oamk.musiccourseapp.schedule.reservation.Date

class ItemAdapter(private val dataset: ArrayList<Date>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>()
{

    class ItemViewHolder(val binding: ItemDateBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemDateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val date = dataset[position]
        holder.binding.dateTextView.text = date.start+ " "+date.end+ " "+date.studentId+" "+date.teacherId
    }
}