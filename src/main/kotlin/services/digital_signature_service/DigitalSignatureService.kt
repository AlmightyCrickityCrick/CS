package services.digital_signature_service

import ciphers.implementations.modern.asymmetrical.RSA
import services.hashing_service.HashingService
import java.security.PublicKey

class DigitalSignatureService {
    var encryptionService : RSA = RSA()
    fun getDigitalSignature(s:String): Triple<String, String, PublicKey> {
        var hash = HashingService.hashString(s)
        var digitalSignature = encryptionService.encrypt(hash, "private")
        return Triple<String, String, PublicKey>(s, digitalSignature, encryptionService.publicKey)

    }
    fun verifyDigitalSignature(signature: Triple<String, String, PublicKey>){
        var decryptedHash = encryptionService.decrypt(signature.second, "public")
        var hash = HashingService.hashString(signature.first)
        if (hash == decryptedHash) {
            println(hash)
            println(decryptedHash)
            println("The message hasn't been tampered")
        }
    }
}