import lab1.CaesarCypher
import lab1.CaesarPermutation
import lab1.Playfair
import lab1.VigenereCypher
import kotlin.math.abs

fun main() {
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

}