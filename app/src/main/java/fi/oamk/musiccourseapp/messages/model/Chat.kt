package fi.oamk.musiccourseapp.messages.model

data class Chat (val members: ArrayList<String>, val lastMessage: String){
    companion object {
        fun from(map: HashMap<String, Any?>) = object{
            val members by map
            val lastMessage by map

            val data = Chat(members as ArrayList<String>, lastMessage as String)
        }.data
    }
}

