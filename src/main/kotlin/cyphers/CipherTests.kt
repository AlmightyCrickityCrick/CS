package cyphers

import cyphers.implementations.classical.CaesarCypher
import cyphers.implementations.classical.CaesarPermutation
import cyphers.implementations.classical.Playfair
import cyphers.implementations.classical.VigenereCypher
import cyphers.implementations.modern.asymmetrical.Constants3
import cyphers.implementations.modern.asymmetrical.DiffieHellman
import cyphers.implementations.modern.symmetrical.A51Cipher
import cyphers.implementations.modern.symmetrical.ConstantsLab2
import cyphers.implementations.modern.symmetrical.DES

fun testCiphers(){
    //Classic Ciphers
    var c = CaesarCypher(1)
    var j = c.encrypt("HellozZ")
    println("Caesar encrypt $j")
    var m = c.decrypt(j)
    println("Caesar decrypt $m")

    var cp = CaesarPermutation(1, "yes")
    j = cp.encrypt("Hello")
    println("Caesar Permutation encrypt $j")
    m = cp.decrypt(j)
    println("Caesar Permutation decrypt $m")

    var vc = VigenereCypher("super")
    j = vc.encrypt("perasperaadastra")
    println("Vigenere encrypt $j")
    m = vc.decrypt(j)
    println("Vigenere decrypt $m")

    var pf = Playfair("keyword")
    j = pf.encrypt("The Big WHEEl")
    println("Playfair encrypt $j")
    m = pf.decrypt(j)
    println("Playfair decrypt $m")

    //Block and Stream Ciphers
    var a51 = A51Cipher()
    a51.setKey(GenerateBinKey(63))
    var cyphertext = a51.encrypt("Hello world")
    println(a51.toStr(cyphertext))
    var plaintext = a51.decrypt(cyphertext)
    println(plaintext)

    var des = DES(generateHexKey(15))
    cyphertext = des.encrypt("I have been eating Bananas")
    println(ConstantsLab2.hexToStr(cyphertext))
    plaintext = des.decrypt(cyphertext)
    println(plaintext)

    println("Diffie Hellman Key Exchange")
    var commonP = Constants3.getPrime(1, 20000)
    var commonG = Constants3.getPrimitiveRoot(commonP)
    var dh1 = DiffieHellman(commonG, commonP)
    var dh2 = DiffieHellman(commonG, commonP)
    dh1.createPublicKey()
    dh2.createPublicKey()
    println("PK1 ${dh1.public_key}  PK2 ${dh2.public_key}")
    dh1.generateSecretKey(dh2.public_key)
    dh2.generateSecretKey(dh1.public_key)
    println("Privk1 ${dh1.shared_secret_key} Privk2 ${dh2.shared_secret_key}")
}

fun GenerateBinKey(i:Int):String{
    var s = ""
    for (j in 0 .. i){
        s += (0..1).random().toString()
    }
    return s
}

fun generateHexKey(i:Int):String{
    var s = ""
    for (j in 0 .. i) s+= arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" ).random()
    return s
}