package lab1

class Playfair(var keyword:String):Cypher(), Permutable {
    override var permutatedAlphabet:String = ""
    var wheel=Array(5){CharArray(5)}

    override fun setAlphabet(permutation: String){
        super.setAlphabet(permutation)
        this.permutatedAlphabet = this.permutatedAlphabet.replace("j", "")
        var i = 0
        for (row in 0 until wheel.size)
            for (col in 0 until wheel[row].size){
                wheel[row][col] = permutatedAlphabet[i]
                i++
            }
    }

    override fun encrypt(m:String):String{
        if (this.permutatedAlphabet.isEmpty()) setAlphabet(keyword)
        var message = m.lowercase().replace(" ", "")
        if ("j" in message) message.replace("j", "i")
        var c :Int= 0
        while (c < message.length){
            if (c + 1 == message.length ) {
                message+= message[c].toString() + if(message[c] != 'q')  "q" else "z"
            break}
            else if(message[c] == message[c+1]) message= message.substring(0, c+1) + (if(message[c] != 'q')  "q" else "z") + message.substring(c+1)
            c+=2
        }

        var cyphertext = ""
        c= 1
        while (c < message.length){
            cyphertext+= getCypher(message[c-1], message[c], false)
            c+=2

        }
        return(cyphertext)
    }

    private fun getCypher(i:Char, j:Char, isDecryption:Boolean):String{
        var move = if(isDecryption) (-1) else 1
        var irow = 0
        var icol = 0
        var jrow = 0
        var jcol = 0

        while(irow< wheel.size){
            if(i in wheel[irow]) {
                icol = wheel[irow].indexOf(i)
                break
            } else irow++
        }

        while(jrow< wheel.size){
            if(j in wheel[jrow]) {
                jcol = wheel[jrow].indexOf(j)
                break
            } else jrow++
        }

        if (jrow == irow) return wheel[irow][Math.floorMod(icol+move, 5)].toString() + wheel[irow][Math.floorMod(jcol+move, 5)].toString()
        else if (jcol == icol) return wheel[Math.floorMod(irow +move, 5)][icol].toString() + wheel[Math.floorMod(jrow+move,5)][jcol].toString()
        else{
            var newi = wheel[irow][jcol]
            var newj = wheel[jrow][icol]
            return newi.toString() + newj.toString()
        }
    }

    override fun decrypt(m:String):String{
        if (this.permutatedAlphabet.isEmpty()) setAlphabet(keyword)
        var plaintext = ""
        var message = if (" " in m) m.lowercase().replace(" ", "") else m.lowercase()
        var c= 1
        while (c < message.length){
            plaintext+= getCypher(message[c-1], message[c], true)
            c+=2

        }
        return(plaintext)
    }

}