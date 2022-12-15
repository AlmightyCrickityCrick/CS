package ciphers.implementations.modern.symmetrical

import ciphers.Cipher

class A51Cipher: Cipher() {
    private var key: String? = null
    val REG_X_LENGTH = 19
    val REG_Y_LENGTH = 22
    val REG_Z_LENGTH = 23
    var regX = IntArray(REG_X_LENGTH)
    var regY = IntArray(REG_Y_LENGTH)
    var regZ = IntArray(REG_Z_LENGTH)
    /**
     * If valid 64-bit binary key, set it and return true else just return false
     */
    fun setKey(key: String): Boolean {
        if (key.length == 64 && key.matches("[01]+".toRegex())) {
            this.key = key
            loadRegisters(key)
            return true
        }
        return false
    }
    fun loadRegisters(key: String) {
        for (i in 0 until REG_X_LENGTH) regX[i] = key.substring(i, i + 1).toInt()
        for (i in 0 until REG_Y_LENGTH) regY[i] = key.substring(REG_X_LENGTH + i, REG_X_LENGTH + i + 1).toInt()
        for (i in 0 until REG_Z_LENGTH) regZ[i] = key.substring(
            REG_X_LENGTH + REG_Y_LENGTH + i, REG_X_LENGTH +
                    REG_Y_LENGTH + i + 1
        ).toInt()
    }



    fun getKey(): String? {
        return key
    }
    override fun encrypt(m: String): String {
        var s = ""
        val binary: IntArray = toBinary(m)
        val keystream: IntArray = getKeystream(binary.size)
        for (i in binary.indices) s+=(binary[i] xor keystream[i])
        return s.toString()
    }

    override fun decrypt(m: String): String {
        var s = ""
        val binary = IntArray(m.length)
        val keystream: IntArray = getKeystream(m.length)
        for (i in binary.indices) {
            binary[i] = m.substring(i, i + 1).toInt()
            s+= (binary[i] xor keystream[i])
        }
        return this.toStr(s.toString())
    }

    fun getKeystream(length: Int): IntArray {
        // 1. Instantiate registers and keystream
        val regX = regX.clone()
        val regY = regY.clone()
        val regZ = regZ.clone()
        val keystream = IntArray(length)
        // 2. Generate keystream bits
        for (i in 0 until length) {
            // 2a. Calculate maj(x8, y10 ,z10)​
            val maj = getMajority(regX[8], regY[10], regZ[10])

            // 2b. If necessary, step regX
            if (regX[8] == maj) {
                val newStart = regX[13] xor regX[16] xor regX[17] xor regX[18]
                val temp = regX.clone()
                for (j in 1 until regX.size) regX[j] = temp[j - 1]
                regX[0] = newStart
            }

            // 2c. If necessary, step regY
            if (regY[10] == maj) {
                val newStart = regY[20] xor regY[21]
                val temp = regY.clone()
                for (j in 1 until regY.size) regY[j] = temp[j - 1]
                regY[0] = newStart
            }

            // 2d. If necessary, step regZ
            if (regZ[10] == maj) {
                val newStart = regZ[7] xor regZ[20] xor regZ[21] xor regZ[22]
                val temp = regZ.clone()
                for (j in 1 until regZ.size) regZ[j] = temp[j - 1]
                regZ[0] = newStart
            }
            keystream[i] = regX[18] xor regY[21] xor regZ[22]
        }
        return keystream
    }

    private fun getMajority(x: Int, y: Int, z: Int): Int {
        return if (x + y + z > 1) 1 else 0
    }

    /**
     * Used to convert a string to binary values. Used by encrypt()
     */
    fun toBinary(text: String): IntArray {
        var s = ""
        for (i in 0 until text.length) {
            var temp = Integer.toBinaryString(text[i].code)
            for (j in temp.length..7) temp = "0$temp"
            s += temp
        }
        val binaryStr = s
        val binary = IntArray(binaryStr.length)
        for (i in binary.indices) binary[i] = binaryStr.substring(i, i + 1).toInt()
        return binary
    }

    /**
     * Used to convert a binary string to text equivalent Used by decrypt()
     */
    fun toStr(binary: String): String {
        var s = ""
        var i = 0
        while (i <= binary.length - 8) {
            s+= (binary.substring(i, i + 8).toInt(2).toChar())
            i += 8
        }
        return s
    }
}