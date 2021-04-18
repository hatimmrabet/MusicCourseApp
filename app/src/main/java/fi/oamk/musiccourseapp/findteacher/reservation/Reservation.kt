package fi.oamk.musiccourseapp.findteacher.reservation

data class Reservation(val date: String, val start: String, val end: String, val studentId: String) {
    companion object {
        fun from(map: HashMap<String, String>) = object {
            val date by map
            val start by map
            val end by map
            val studentId by map

            val data = Reservation(date, start, end, studentId)
        }.data
    }
}