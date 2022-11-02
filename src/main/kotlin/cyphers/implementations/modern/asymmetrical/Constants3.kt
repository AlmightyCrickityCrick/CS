package cyphers.implementations.modern.asymmetrical

import kotlin.math.sqrt

object Constants3 {
    fun getPrime(start:Int, stop:Int):Int{
        var i = (start .. stop).random()
        while(!isPrime(i)) i++
        return i
    }

    private fun isPrime(i:Int):Boolean{
        if(i <=3) return true
        if((i % 2 == 0 ) || ( i % 3 == 0)) return false
        var n = 5
        val stop = sqrt(i.toDouble())
        while (n<=stop) if ((i %n ==0) || (i%(n+2) ==0 )) return false else n+=6
        return true
    }
    fun manualPower(mes:Long, pow:Long, mod:Long):Long{
        var res:Long = 1
        for(i in 1 .. pow) {
            res*=mes
            if(res>=mod) res %= mod
        }
        return res
    }

    fun getPrimitiveRoot(p:Int):Int{
        var r = 2
        var rootFound = false
        while(!rootFound)
        for (x in 0..p-2){
            var vals = ArrayList<Long>()
            var tmp=manualPower(r.toLong(), x.toLong(), p.toLong())
            if( tmp !in vals) vals.add(tmp) else {
                r++
                break
            }
            if(x == p-2){
                rootFound = true
                break}
        }
        return r
    }
}