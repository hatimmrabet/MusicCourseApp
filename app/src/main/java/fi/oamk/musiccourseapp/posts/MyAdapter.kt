package fi.oamk.musiccourseapp.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.musiccourseapp.R

class MyAdapter (private val myDataset: ArrayList<Post>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val title: TextView = itemView.findViewById(R.id.post_title)
        val instrument: TextView = itemView.findViewById(R.id.post_instrument)
        val description: TextView = itemView.findViewById(R.id.post_desc)
        val price: TextView = itemView.findViewById(R.id.post_price)
        val author: TextView = itemView.findViewById(R.id.post_author)
        val time: TextView = itemView.findViewById(R.id.post_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(parent.context).inflate(R.layout.post_row,parent,false)
        return MyViewHolder(myView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = myDataset.get(position).title
        holder.instrument.text = myDataset.get(position).instrument
        holder.description.text = myDataset.get(position).description
        holder.price.text = myDataset.get(position).price.toString() + " €"
        holder.author.text = "by " + myDataset.get(position).author
        holder.time.text = "on "+ myDataset.get(position).time
    }

    override fun getItemCount() = myDataset.size

}