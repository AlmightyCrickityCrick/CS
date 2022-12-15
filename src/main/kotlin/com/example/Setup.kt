import ciphers.testCiphers
import services.login_service.LoginService
import services.messaging_service.MessagingService

fun setUp() {
    LoginService.db.addSpecialUser("Slina", "s02linamd@gmail.com", "pass11")
    LoginService.db.addUser("TestTest", "test@mail.com", "pass12")
}

