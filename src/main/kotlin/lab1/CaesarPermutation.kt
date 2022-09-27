package lab1

class CaesarPermutation(var key:Int, var permutation:String) :Cypher(), Permutable{
    override lateinit var permutatedAlphabet: String
    override fun encrypt(m: String):String{
        if (!this::permutatedAlphabet.isInitialized) setAlphabet(permutation)
        var plaintext = m.lowercase()
        var cyphertext = ""
        for (c in 0 until plaintext.length){
            cyphertext+= if(m[c].isUpperCase()) permutatedAlphabet[(permutatedAlphabet.indexOf(plaintext[c])+key) %Constants.ALPHABET_SIZE].uppercase() else
                permutatedAlphabet[permutatedAlphabet.indexOf(plaintext[c])+key]
        }
        return cyphertext
    }

    override fun decrypt(m:String):String{
        if (!this::permutatedAlphabet.isInitialized) setAlphabet(permutation)
        var cyphertext = m.lowercase()
        var plaintext = ""
        for (c in 0 until cyphertext.length){
            plaintext+= if(m[c].isUpperCase()) permutatedAlphabet[(permutatedAlphabet.indexOf(cyphertext[c])-key) %Constants.ALPHABET_SIZE].uppercase() else
                permutatedAlphabet[(permutatedAlphabet.indexOf(cyphertext[c])-key) %Constants.ALPHABET_SIZE]
        }
        return plaintext
    }



}