package ciphers.implementations.classical

import ciphers.Cipher

class CaesarCipher(var key:Int) :Cipher(){
    override fun encrypt(m:String) : String{
        var cyphertext=""
        var plaintext = m.lowercase()
        for (c in 0 until plaintext.length){
            var letter = if((plaintext[c]+key).isLetter()) (plaintext[c] + key).toString() else ((plaintext[c]+key) -'z' + 96).toChar().toString()
            cyphertext += if(m[c].isUpperCase()) letter.toUpperCase() else letter
        }
        return cyphertext
    }
    
    override fun decrypt(m:String):String {
        var plaintext = ""
        var cypherText = m.lowercase()
        for (c in 0 until cypherText.length) {
            var letter =
                if ((cypherText[c] - key).isLetter()) (cypherText[c] - key).toString() else ((cypherText[c] - key) + 'z'.toInt() - 96).toChar()
                    .toString()
            plaintext += if (m[c].isUpperCase()) letter.toUpperCase() else letter
        }
        return plaintext
    }

}