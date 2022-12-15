package com.example.services.login_service

import services.login_service.LoginService

object AdminBoard {
    public fun execute(id:Int, action:String):Int{
        if (!LoginService.checkIfUserExists(id)) return -1
        when(action){
            "demote" -> LoginService.db.demoteUser(id)
            "promote" -> LoginService.db.promoteUser(id)
            "delete"-> LoginService.db.getUser(id)?.let { LoginService.db.deleteUser(it) }
            else -> return -1
        }
        return 0

    }
}