package fi.oamk.musiccourseapp.user

data class User(val uid: String? ,val credit : String ?, val email : String?, val fullname : String?, val picture : String?, val role : String?) {
    companion object {
        fun from(map: HashMap<String, String>) = object {
            val uid by map
            val credit by map
            val email by map
            val fullname by map
            val picture by map
            val role by map

            val data = User(uid,credit, email, fullname, picture, role)
        }.data
    }
}