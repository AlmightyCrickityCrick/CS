package com.example.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

//Install Content Negotiations
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}

//Models of requests
@Serializable
data class RegisterRequest(var username:String, var email:String, var password:String)
@Serializable
data class SimpleLoginRequest(var email:String, var password: String)

@Serializable
data class MFALoginRequest(var email: String, var code:String)

@Serializable
data class LoginResponse(var token:String)

@Serializable
data class MessageEncodeRequest(var token: String, var cipher:String, var message:String)

@Serializable
data class MessageEncodeResponse(var message: String, var key:String)

@Serializable
data class MessageDecodeRequest(var token: String, var cipher: String, var message: String, var key: String)

@Serializable
data class MessageDecodeResponse(var message: String)

@Serializable
data class AdminRequest(var token: String, var id:Int, var operation:String)
//Where operation is promote/demote/delete