import cyphers.implementations.classical.CaesarCypher
import cyphers.implementations.classical.CaesarPermutation
import cyphers.implementations.classical.Playfair
import cyphers.implementations.classical.VigenereCypher
import cyphers.implementations.modern.asymmetrical.Constants3
import cyphers.implementations.modern.asymmetrical.DiffieHellman
import cyphers.implementations.modern.symmetrical.A51Cipher
import cyphers.implementations.modern.symmetrical.ConstantsLab2
import cyphers.implementations.modern.symmetrical.DES
import cyphers.testCiphers
import hash.login_service.LoginService
import hash.messaging_service.MessagingService

fun main() {
    testCiphers()
    LoginService().start()
    MessagingService().start()
}

