package fi.oamk.musiccourseapp.messages.model

data class Message (val content: String, val sender: String = "Unknown", val time: String = "01.01.2021 14:40"){
    companion object {
        fun from(map: HashMap<String, String>) = object {
            val content by map
            val sender by map
            val time by map

            val data = Message(content, sender, time)
        }.data
    }
}