package implementations.classical

interface Permutable {
    var permutatedAlphabet:String

    fun setAlphabet(permutation:String){
        permutatedAlphabet = ""
        for (c in permutation) if (!(c in permutatedAlphabet)) permutatedAlphabet+=c.lowercase()
        for (c in Constants.ALPHABET) if (!(c in permutatedAlphabet)) permutatedAlphabet+=c
    }

}