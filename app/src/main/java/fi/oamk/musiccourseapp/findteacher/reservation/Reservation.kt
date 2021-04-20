package fi.oamk.musiccourseapp.findteacher.reservation

data class Reservation(val uid: String,val date: String, val start: String, val end: String, val studentId: String) {
    companion object {
        fun from(map: HashMap<String, String>) = object {
            val uid by map
            val date by map
            val start by map
            val end by map
            val studentId by map

                val data = Reservation(uid ,date, start, end, studentId)
        }.data
    }
}