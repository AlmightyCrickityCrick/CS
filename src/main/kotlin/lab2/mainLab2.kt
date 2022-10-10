package lab2

fun main(){
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