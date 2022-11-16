package services.login_service

import services.hashing_service.HashingService

data class User(var id:Int, var username:String, var password:String)

class Database {
    var db = ArrayList<User>()

    fun addUser(username: String, password: String):Int{
        db.add(User(db.size, username, HashingService.hashString(password)))
        return db.size - 1
    }

    fun getUser(id: Int):User?{
        for (u in db)
            if (u.id == id) return u
        return null
    }

}