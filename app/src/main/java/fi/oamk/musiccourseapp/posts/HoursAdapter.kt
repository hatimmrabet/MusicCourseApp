package fi.oamk.musiccourseapp.posts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import fi.oamk.musiccourseapp.R

class HoursAdapter (private val hoursList: ArrayList<Hour>): RecyclerView.Adapter<HoursAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursAdapter.MyViewHolder {
        val myView = LayoutInflater.from(parent.context).inflate(R.layout.post_info_disponibility,parent,false)
        return MyViewHolder(myView)
    }

    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val hour: TextView = itemView.findViewById(R.id.post_hour)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = hoursList[position]
        val end = (currentItem.start.substring(0,2).toInt()+1).toString() + currentItem.start.substring(2,5)
        holder.hour.text = "From "+ currentItem.start + " to " + end
    }

    override fun getItemCount() = hoursList.size
}
