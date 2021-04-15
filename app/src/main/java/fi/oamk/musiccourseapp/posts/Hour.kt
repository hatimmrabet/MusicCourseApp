package fi.oamk.musiccourseapp.posts

data class Hour(val postkey: String, val reserved: Boolean, val start: String)
{
    companion object {
        fun from(map: HashMap<String, Any>) = object {
            val postkey by map
            val reserved by map
            val start by map
            val data = Hour(postkey.toString(),reserved.toString().toBoolean(),start.toString())
        }.data
    }
}