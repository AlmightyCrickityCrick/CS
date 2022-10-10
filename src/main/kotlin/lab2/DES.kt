package lab2

import lab1.Cypher
import java.util.*


class DES(var key:String) :Cypher() {
    fun permutation(sequence: IntArray, input: String): String {
        var input = input
        var output: String = ""
        input = ConstantsLab2.hextoBin(input)
        for (i in sequence.indices) output += input[sequence[i] - 1]
        output = ConstantsLab2.binToHex(output)
        return output
    }

    fun leftCircularShift(input: String, numBits: Int): String {
        var input = input
        var numBits = numBits
        val n = input.length * 4
        val perm = IntArray(n)
        for (i in 0 until n - 1) perm[i] = i + 2
        perm[n - 1] = 1
        while (numBits-- > 0) input = permutation(perm, input)
        return input
    }

    fun getKeys(key: String): Array<String?> {
        var key = key
        val keys = arrayOfNulls<String>(16)
        // first key permutation
        key = permutation(ConstantsLab2.PC1, key)
        for (i in 0..15) {
            key = (leftCircularShift(
                key.substring(0, 7),
                ConstantsLab2.shiftBits.get(i)
            )
                    + leftCircularShift(
                key.substring(7, 14),
                ConstantsLab2.shiftBits.get(i)
            ))
            // second key permutation
            keys[i] = permutation(ConstantsLab2.PC2, key)
        }
        return keys
    }

    fun sBox(input: String): String {
        var input = input
        var output = ""
        input = ConstantsLab2.hextoBin(input)
        var i = 0
        while (i < 48) {
            val temp = input.substring(i, i + 6)
            val num = i / 6
            val row = (
                    temp[0].toString() + "" + temp[5]).toInt(
                2
            )
            val col =
                temp.substring(1, 5).toInt(2)
            output += Integer.toHexString(
                ConstantsLab2.sbox.get(num).get(row).get(col)
            )
            i += 6
        }
        return output
    }

    fun round(input: String, key: String, num: Int): String {
        // fk
        var left = input.substring(0, 8)
        var temp = input.substring(8, 16)
        val right = temp
        // Expansion permutation
        temp = permutation(ConstantsLab2.EP, temp)
        // xor temp and round key
        temp = ConstantsLab2.xor(temp, key)
        // lookup in s-box table
        temp = sBox(temp)
        // Straight D-box
        temp = permutation(ConstantsLab2.P, temp)
        // xor
        left = ConstantsLab2.xor(left, temp)
        // swapper
        return right + left
    }

    override fun encrypt(m: String): String {
        var message = ConstantsLab2.strToHex(m)
        if (message.length == 16) return ConstantsLab2.hexToStr(encryptSegment(message))
        else if (message.length <= 16){
            while((message.length % 16) !=0) message+= "0"
            return ConstantsLab2.hexToStr(encryptSegment(message))
        }
        else{
            while((message.length % 16) !=0) message+= "0"
            var cypher = ""
            for (i in 15 until message.length step 16) {
                cypher+= encryptSegment(message.substring(i - 15, i+1))
            }
            return cypher
        }
    }

    fun encryptSegment(pt:String):String{
        var plainText = pt
        val keys: Array<String?> = getKeys(this.key)
        plainText = permutation(ConstantsLab2.IP, plainText)
        var i: Int = 0
        while (i < 16) {
            plainText = keys[i]?.let { round(plainText, it, i) }.toString()
            i++
        }
        plainText = (plainText.substring(8, 16)
                + plainText.substring(0, 8))
        plainText = permutation(ConstantsLab2.IP1, plainText)
        return plainText
    }

    override fun decrypt(m: String): String {
        var message = m
        //var message = ConstantsLab2.strToHex(m)
        if (message.length <= 16) return ConstantsLab2.hexToStr(decryptSegment(message))
        else{
            while((message.length % 16) !=0) message+= "0"
            var cypher = ""
            for (i in 15 until message.length step 16) {
                cypher+= decryptSegment(message.substring(i - 15, i+1))
            }
            return ConstantsLab2.hexToStr(cypher)
        }

    }

    fun decryptSegment(pt: String):String{
        var i:Int
        // get round keys
        val keys :Array<String?> = getKeys(this.key)

        // initial permutation
        var plainText = permutation(ConstantsLab2.IP, pt)
        // 16-rounds
        for (i in  15 downTo 0) {
            plainText= keys[i]?.let { round(plainText, it, 15 - i) }.toString()
        }

        // 32-bit swap
        plainText = plainText.substring(8, 16) + plainText.substring(0, 8)
        plainText = permutation(ConstantsLab2.IP1, plainText)
        return plainText
    }

}