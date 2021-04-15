package fi.oamk.musiccourseapp.posts


data class Post(val postkey: String,
                val userkey: String,
                var title: String,
                val instrument: String,
                val description: String,
                val price: Double,
                val date: String
                )
{
    companion object {
        fun from(map: HashMap<String, Any>) = object {
            val postkey by map
            val userkey by map
            val title by map
            val instrument by map
            val description by map
            val price by map
            val date by map
            val data = Post(postkey.toString(), userkey.toString(), title.toString(), instrument.toString(),
                            description.toString(), price.toString().toDouble(), date.toString())
        }.data
    }
}