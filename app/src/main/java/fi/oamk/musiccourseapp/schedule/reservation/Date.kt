package fi.oamk.musiccourseapp.schedule.reservation

data class Date(val start: String, val end: String, val studentId: String, val teacherId: String) {
    companion object {
        fun from(map: HashMap<String, String>) = object {
            val start by map
            val end by map
            val studentId by map
            val teacherId by map
            val data = Date(start, end, studentId, teacherId)
        }.data
    }
}