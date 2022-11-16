import ciphers.testCiphers
import services.login_service.LoginService
import services.messaging_service.MessagingService

fun main() {
    testCiphers()
    LoginService().start()
    MessagingService().start()
}

