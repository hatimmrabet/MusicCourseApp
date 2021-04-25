package fi.oamk.musiccourseapp.schedule.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.musiccourseapp.R
import fi.oamk.musiccourseapp.schedule.reservation.Date

class ItemAdapter(private val dataset: ArrayList<ReservationData>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: View) : RecyclerView.ViewHolder(binding) {
        val hour: TextView = itemView.findViewById(R.id.hour)
        val date: TextView = itemView.findViewById(R.id.date)
        val name: TextView = itemView.findViewById(R.id.name)
        val email: TextView = itemView.findViewById(R.id.email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val myView = LayoutInflater.from(parent.context).inflate(R.layout.reservation_row,parent,false)
        dataset.sortByDescending { it.date }
        return ItemViewHolder(myView)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = dataset[position]
        val start = data.start.substring(0,2)+":"+data.start.substring(2,4)
        val end = data.end.substring(0,2)+":"+data.end.substring(2,4)
        val date = data.date.substring(0,4)+"/"+data.date.substring(4,6)+"/"+data.date.substring(6,8)

        holder.date.text = date
        holder.hour.text = "From "+ start + " to " + end
        holder.name.text = data.studentName
        holder.email.text = data.studentEmail
    }


}