package fi.oamk.musiccourseapp.posts

data class Reservation(val reservationkey : String, val teacherkey: String, val postkey: String, val hourkey: String, val date:String,
                       val start:String, val price: String)
