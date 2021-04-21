package fi.oamk.musiccourseapp.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.musiccourseapp.R

class HoursAdapter (private val hoursList: ArrayList<Hour>): RecyclerView.Adapter<HoursAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursAdapter.MyViewHolder {
        val myView = LayoutInflater.from(parent.context).inflate(R.layout.post_info_disponibility,parent,false)
        // order list by start hour
        hoursList.sortBy { it.start }
        return MyViewHolder(myView)
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val hour: CheckBox = itemView.findViewById(R.id.post_hour)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = hoursList[position]
        var endTime = (currentItem.start.substring(0,2).toInt()+1).toString()
        if (endTime.length == 1) { endTime = "0"+endTime }
        val end = ( endTime + currentItem.start.substring(2,5))
        holder.hour.text = "From "+ currentItem.start + " to " + end

        holder.hour.setOnCheckedChangeListener { buttonView, isChecked ->
            currentItem.checked = isChecked
        }

    }

    override fun getItemCount() = hoursList.size
}
