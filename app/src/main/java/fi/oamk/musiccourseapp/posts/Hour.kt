package fi.oamk.musiccourseapp.posts

data class Hour(val postkey: String, val hourkey: String, var reserved: Boolean, val start: String)
{
    var checked: Boolean = false
    companion object {
        fun from(map: HashMap<String, Any>) = object {
            val postkey by map
            val hourkey by map
            var reserved by map
            val start by map
            val data = Hour(postkey.toString(),hourkey.toString(),reserved.toString().toBoolean(),start.toString())
        }.data
    }
}