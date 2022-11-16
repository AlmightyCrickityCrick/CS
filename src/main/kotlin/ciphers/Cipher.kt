package ciphers

open abstract class Cipher {
    abstract fun encrypt(m :String):String
    abstract fun decrypt(m:String):String
}