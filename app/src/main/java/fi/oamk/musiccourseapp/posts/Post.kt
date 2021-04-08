package fi.oamk.musiccourseapp.posts

import kotlin.collections.HashMap

data class Post(val id: String,
                var title: String,
                val instrument: String,
                val description: String,
                val price: String,
                val time: String,
                val author: String
                ) {
    companion object {
        fun from(map: HashMap<String, String>) = object {
            val id by map
            val title by map
            val instrument by map
            val description by map
            val price by map
            val time by map
            val author by map
            val data = Post(id,title,instrument,description,price,time,author)
        }.data
    }
}
