package cyphers.implementations.modern.asymmetrical

abstract class KeyGenerator {
    abstract fun createPublicKey()
    abstract fun generateSecretKey(pk:Long)
}