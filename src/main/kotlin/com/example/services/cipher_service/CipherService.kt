package com.example.services.cipher_service

import ciphers.generateHexKey
import ciphers.implementations.classical.CaesarCipher
import ciphers.implementations.classical.VigenereCipher
import ciphers.implementations.modern.symmetrical.ConstantsLab2
import ciphers.implementations.modern.symmetrical.DES

object CipherService {
    public fun encryptMessage(m:String, cipher:String):Pair<String, String>{
        when(cipher){
           "caesar" -> {
               var key = (1..25).random()
               var c = CaesarCipher(key)
               var ciphertext = c.encrypt(m)
               return Pair(key.toString(), ciphertext)
           }
            "vigenere" -> {
                var key = generateHexKey(6)
                var c = VigenereCipher(key)
                var ciphertext = c.encrypt(m)
                return Pair(key, ciphertext)
            }
            "des" ->{
                var key = generateHexKey(15)
                var c = DES(key)
                var ciphertext = c.encrypt(m)
                return Pair(key, ConstantsLab2.hexToStr(ciphertext))
            }

            else -> return Pair("", "")

        }

    }

    public fun decryptMessages(m:String, key:String, cipher:String):String{
        when(cipher){
            "caesar" -> {
                var c = CaesarCipher(key.toInt())
                var ciphertext = c.decrypt(m)
                return ciphertext
            }
            "vigenere" -> {
                var c = VigenereCipher(key)
                var ciphertext = c.decrypt(m)
                return ciphertext
            }
            "des" ->{
                var c = DES(key)
                var ciphertext = c.decrypt(m)
                return ciphertext
            }

            else -> return ""

        }
    }

}