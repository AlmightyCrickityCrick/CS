package services.login_service

import services.hashing_service.HashingService

//User 0 - simple user, can access ciphers
//User 1 - admin, can create, delete, demote or promote users
data class User(var id:Int, var username:String, var email:String, var password:String, var role:Int)

class Database {
    var db = ArrayList<User>()

    fun addUser(username: String, email: String, password: String):Int{
        db.add(User(db.size, username, email, HashingService.hashString(password), 0))
        return db.size - 1
    }

    fun addSpecialUser(username: String, email: String, password: String):Int{
        db.add(User(db.size, username, email, HashingService.hashString(password), 1))
        return db.size - 1
    }

    fun exists(email: String):Boolean{
        for (u in db){
            if (u.email == email) return true
        }
        return false
    }

    fun getUser(id: Int):User?{
        for (u in db)
            if (u.id == id) return u
        return null
    }

    fun getUserByEmail(email: String):User?{
        for (u in db)
            if (u.email == email) return u
        return null
    }

    fun deleteUser(user:User){
        db.remove(user)
    }

    fun promoteUser(id:Int){
        db[id].role = 1
    }

    fun demoteUser(id:Int){
        db[id].role = 0
    }

}