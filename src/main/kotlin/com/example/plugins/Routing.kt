package com.example.plugins

import com.example.services.cipher_service.CipherService
import com.example.services.login_service.AdminBoard
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlinx.serialization.json.Json
import services.login_service.LoginService

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        post("/register"){
            var data = call.receive<String>()
            var request = Json.decodeFromString(RegisterRequest.serializer(), data)
            try{
                LoginService.register(request.username, request.email, request.password)
                call.respondText("Registration complete")
            }catch (e:Error){call.respondText("Couldnt register")}
        }
        post("/login"){
            var data = call.receive<String>()
            var request = Json.decodeFromString(SimpleLoginRequest.serializer(), data)
            if(LoginService.checkIfUserExists(request.email)) {
                var result = LoginService.loginStep1(request.email, request.password)
                if (result == 0) {
                    call.respond(
                        Json.encodeToString(
                            LoginResponse.serializer(),
                            LoginResponse(LoginService.getToken(request.email))
                        )
                    )
                } else if (result == -1) {
                    call.respondText("Email or password incorrect")
                } else {
                    call.respondText("Verification Code has been sent. Verify email and proceed to authentication step 2.")
                }
            } else call.respondText("Email or password incorrect")
        }

        post("/login/step2"){
            var data = call.receive<String>()
            var request = Json.decodeFromString(MFALoginRequest.serializer(), data)
            if(LoginService.checkIfUserExists(request.email) && LoginService.checkUserIfDidLogin1(request.email)) {
                var result = LoginService.loginStep2(request.email, request.code)
                if (result == 0) {
                    call.respond(
                        Json.encodeToString(
                            LoginResponse.serializer(),
                            LoginResponse(LoginService.getToken(request.email))
                        )
                    )
                } else {
                    call.respondText("Code incorrect or expired")
                }
            } else call.respondText("Please complete first step of login")
        }

        post("/encode"){
            var data = call.receive<String>()
            var request = Json.decodeFromString(MessageEncodeRequest.serializer(), data)
            if (LoginService.getUserByToken(request.token)!=null){
                var response = CipherService.encryptMessage(request.message, request.cipher)
                call.respond(Json.encodeToString(MessageEncodeResponse.serializer(), MessageEncodeResponse(response.second, response.first)))
            } else call.respondText("Please login or register")
        }

        post("/decode"){
            var data = call.receive<String>()
            var request = Json.decodeFromString(MessageDecodeRequest.serializer(), data)
            if (LoginService.getUserByToken(request.token)!=null){
                var response = CipherService.decryptMessages(request.message, request.key, request.cipher)
                call.respond(Json.encodeToString(MessageDecodeResponse.serializer(), MessageDecodeResponse(response)))
            } else call.respondText("Please login or register")
        }
        post("/admin/request"){
            var data = call.receive<String>()
            var request = Json.decodeFromString(AdminRequest.serializer(), data)
            var usr = LoginService.getUserByToken(request.token)
            if (usr== null)call.respondText("Please login or register")
            else if (usr.role!=1) call.respondText("Unauthorized action")
            else{
                var response = AdminBoard.execute(request.id, request.operation)
                if(response == -1) call.respondText("User or action doesnt exist")
                else call.respondText("Operation successful")
            }
        }
    }
}
