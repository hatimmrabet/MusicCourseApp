package fi.oamk.musiccourseapp.messages.model

data class Chat (val id: String, val lastMessage: String, val sender: String){
    companion object {
        fun from(map: HashMap<String, String>) = object {
            val id by map
            val lastMessage by map
            val sender by map

            val data = Chat(id, lastMessage, sender)
        }.data
    }
}

