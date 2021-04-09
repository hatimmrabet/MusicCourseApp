package fi.oamk.musiccourseapp.posts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fi.oamk.musiccourseapp.R

class MyAdapter(private val myDataset: ArrayList<Post>, private val listener: OnPostListener): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        val title: TextView = itemView.findViewById(R.id.post_title)
        val instrument: TextView = itemView.findViewById(R.id.post_instrument)
        val description: TextView = itemView.findViewById(R.id.post_desc)
        val price: TextView = itemView.findViewById(R.id.post_price)
        val author: TextView = itemView.findViewById(R.id.post_author)
        val time: TextView = itemView.findViewById(R.id.post_time)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION)
            {
                listener.onPostClick(position)
            }
        }
    }

    interface OnPostListener{
        fun onPostClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val myView = LayoutInflater.from(parent.context).inflate(R.layout.post_row,parent,false)
        return MyViewHolder(myView)
    }

    @SuppressLint("SetTextI18n")
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