package hash.security_service

import java.security.PublicKey

class DigitalSignatureService {
    var encryptionService :RSAEncryptionService = RSAEncryptionService()
    fun getDigitalSignature(s:String): Triple<String, String, PublicKey> {
        var hash = HashService.hashString(s)
        var digitalSignature = encryptionService.encrypt(hash, "private")
        return Triple<String, String, PublicKey>(s, digitalSignature, encryptionService.publicKey)

    }
    fun verifyDigitalSignature(signature: Triple<String, String, PublicKey>){
        var decryptedHash = encryptionService.decrypt(signature.second, "public")
        var hash = HashService.hashString(signature.first)
        if (hash == decryptedHash) {
            println(hash)
            println(decryptedHash)
            println("The message hasn't been tampered")
        }
    }
}