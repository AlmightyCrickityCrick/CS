package ciphers.implementations.modern.asymmetrical

import ciphers.*
import java.nio.charset.StandardCharsets
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.util.*
import javax.crypto.*

class RSA : ciphers.Cipher(){
    var algorithm = "RSA"
    var size = 2048
    lateinit var publicKey: PublicKey
    private lateinit var privateKey:PrivateKey
    init {
        generateKey()
    }

    fun generateKey(){
        var generator = KeyPairGenerator.getInstance(algorithm)
        generator.initialize(size)
        var pair = generator.genKeyPair()
        publicKey = pair.public
        privateKey = pair.private
    }

    override fun encrypt(m: String): String {
        return "Please specify key in arguments"
    }
    fun encrypt(m:String, key:String):String{
        var useKey = if(key == "private") privateKey else publicKey
        val encryptCipher = javax.crypto.Cipher.getInstance(algorithm)
        encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, useKey)
        var secretMessageBytes = m.toByteArray(StandardCharsets.UTF_8)
        var encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes)
        return Base64.getEncoder().encodeToString(encryptedMessageBytes)
    }

    override fun decrypt(m: String): String {
       return "Please specify key in arguments"
    }

    fun decrypt(m:String, key: String):String{
        var message = Base64.getDecoder().decode(m)
        var useKey = if(key == "private") privateKey else publicKey
        val decryptCipher = javax.crypto.Cipher.getInstance(algorithm)
        decryptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, useKey)
        var decryptedMessageBytes = decryptCipher.doFinal(message)
        return String(decryptedMessageBytes, StandardCharsets.UTF_8)
    }
}