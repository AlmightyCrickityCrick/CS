package services.login_service

import services.hashing_service.HashingService

class LoginService {
    lateinit var db :Database
    fun start(){
        db = Database()
        var x = db.addUser("Lina S", "123456f")
        var hashedPassword = HashingService.hashString("123456f")
        println("Password in database ${db.getUser(x)?.password}  password hash $hashedPassword")
        if(db.getUser(x)?.password == hashedPassword) println("The hashes of passwords are equal")
    }
}