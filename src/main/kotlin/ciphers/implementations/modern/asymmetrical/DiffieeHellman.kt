package ciphers.implementations.modern.asymmetrical

class DiffieHellman(var generator:Int, var P:Int): KeyGenerator() {
    private var self_key = 0
    var public_key :Long= 0
    var shared_secret_key:Long = 0
    init {
        self_key = Constants3.getPrime(1, 2000)
        createPublicKey()
    }

    override fun createPublicKey() {
        public_key = Constants3.manualPower(generator.toLong(), self_key.toLong(), P.toLong())
    }

    override fun generateSecretKey(pk:Long) {
        shared_secret_key = Constants3.manualPower(pk, self_key.toLong(), P.toLong())
    }

}