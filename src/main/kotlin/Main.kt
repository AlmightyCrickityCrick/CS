import implementations.classical.CaesarCypher
import implementations.classical.CaesarPermutation
import implementations.classical.Playfair
import implementations.classical.VigenereCypher
import implementations.modern.symmetrical.A51Cipher
import implementations.modern.symmetrical.ConstantsLab2
import implementations.modern.symmetrical.DES

fun main() {
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