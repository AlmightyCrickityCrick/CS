package lab1

class VigenereCypher(var key: String) :Cypher() {
    override fun encrypt(m:String):String{
        var cyphertext =  ""
        var plaintext = m.lowercase()

        for (c in 0 until plaintext.length){
            var ctm = Constants.ALPHABET.indexOf(plaintext[c])
            var k = Constants.ALPHABET.indexOf(key[(c % key.length)])
            cyphertext+= if(m[c].isUpperCase()) Constants.ALPHABET[(ctm + k) % Constants.ALPHABET_SIZE].uppercase()
        else Constants.ALPHABET[(ctm + k) % Constants.ALPHABET_SIZE]
        }
        return cyphertext
    }

    override fun decrypt(m:String):String{
        var plaintext = ""
        var cyphertext = m.lowercase()
        for (c in 0 until cyphertext.length){
            var ctm = Constants.ALPHABET.indexOf(cyphertext[c])
            var k = Constants.ALPHABET.indexOf(key[(c % key.length)])
            plaintext+= if(m[c].isUpperCase()) Constants.ALPHABET[Math.floorMod((ctm - k), Constants.ALPHABET_SIZE)].uppercase()
            else Constants.ALPHABET[Math.floorMod((ctm - k), Constants.ALPHABET_SIZE)]
        }

        return plaintext
    }
}