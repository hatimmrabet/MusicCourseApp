package fi.oamk.musiccourseapp.posts


data class Post(val userkey: String,
                var title: String,
                val instrument: String,
                val description: String,
                val price: Double,
                val date: String
                )
/*
{
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
*/