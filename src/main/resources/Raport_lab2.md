# Topic: Symmetric Ciphers. Stream Ciphers. Block Ciphers.

### Course: Cryptography & Security
### Student: Scripca Lina
### Professor: Vasile Drumea

----

## Theory
&ensp;&ensp;&ensp; Symmetric Cryptography deals with the encryption of plain text when having only one encryption key which needs to remain private. Based on the way the plain text is processed/encrypted there are 2 types of ciphers:
- Stream ciphers:
    - The encryption is done one byte at a time.
    - Stream ciphers use confusion to hide the plain text.
    - Make use of substitution techniques to modify the plain text.
    - The implementation is fairly complex.
    - The execution is fast.
- Block ciphers:
    - The encryption is done one block of plain text at a time.
    - Block ciphers use confusion and diffusion to hide the plain text.
    - Make use of transposition techniques to modify the plain text.
    - The implementation is simpler relative to the stream ciphers.
    - The execution is slow compared to the stream ciphers.

### Stream Ciphers
&ensp;&ensp;&ensp; Some examples of stream ciphers are the following:
- Grain 
- HC-256
- PANAMA
- Rabbit
- Rivest Cipher (RC4): It uses 64 or 128-bit long keys. It is used in TLS/SSL and IEEE 802.11 WLAN.
- Salsa20
- Software-optimized Encryption Algorithm (SEAL)
- Scream
- A5/1 Cipher

#### A5/1 Cipher

&ensp;&ensp;&ensp; The A5/1 Cypher is a stream cipher used to provide 
over-the-air communication privacy in the GSM cellular telephone standard. 
It is one of several implementations of the A5 security protocol. 
It was initially kept secret, but became public knowledge through leaks and reverse engineering.

&ensp;&ensp;&ensp; A5/1 is based around a combination of three linear-feedback shift registers (LFSRs) with irregular clocking. The three shift registers are specified as follows:

| LFSR number | Bits of Length | Feedback Polynomial | Clocking Bit | Tapped Bits    |
|-------------|----------------|---------------------|--------------|----------------|
| 1           | 19             |x^19+x^18+x^17+x^16+x^13+1| 8            | 13, 16, 17, 18 |
| 2           | 22             | x^22+x^21+x^20+1    | 10           | 20,21          |
| 3           | 23             |x^23+x^22+x^21+x^20+x^7+1 | 10           | 7, 20, 21, 22  |

&ensp;&ensp;&ensp; The bits are indexed with the least significant bit (LSB) as 0. 
The registers are clocked in a stop/go fashion using a majority rule. Each register has an associated clocking bit. At each cycle, the clocking bit of all three registers is examined and the majority bit is determined. A register is clocked if the clocking bit agrees with the majority bit. Hence at each step at least two or three registers are clocked, and each register steps with probability 3/4.

### Block Ciphers

&ensp;&ensp;&ensp; The block ciphers may differ in the block size which is a parameter that might be implementation specific. Here are some examples of such ciphers:
- 3DES
- Advanced Encryption Standard (AES): A cipher with 128-bit block length which uses 128, 192 or 256-bit symmetric key.
- Blowfish
- Data Encryption Standard (DES): A 56-bit symmetric key cipher.
- Serpent
- Twofish: A standard that uses Feistel networks. It uses blocks of 128 bits with key sizes from 128-bit to 256-bit.

#### DES Cipher

&ensp;&ensp;&ensp; The Data Encryption Standard (DES) is a symmetric-key algorithm for the encryption of digital data. Although its short key length of 56 bits makes it too insecure for modern applications, it has been highly influential in the advancement of cryptography.

&ensp;&ensp;&ensp; DES is the archetypal block cipher—an algorithm that takes a fixed-length string of plaintext bits and transforms it through a series of complicated operations into another ciphertext bitstring of the same length. In the case of DES, the block size is 64 bits. DES also uses a key to customize the transformation, so that decryption can supposedly only be performed by those who know the particular key used to encrypt. The key ostensibly consists of 64 bits; however, only 56 of these are actually used by the algorithm. Eight 
bits are used solely for checking parity, and are thereafter discarded. Hence the effective key length is 56 bits.

&ensp;&ensp;&ensp; The steps for performing a DES encryption are as follows:
- In the first step, the 64-bit plain text block is handed over to an initial Permutation (IP) function.
- The initial permutation is performed on plain text.
- Next, the initial permutation (IP) produces two halves of the permuted block; saying Left Plain Text (LPT) and Right Plain Text (RPT).
- Now each LPT and RPT go through 16 rounds of the encryption process.
- In the end, LPT and RPT are rejoined and a Final Permutation (FP) is performed on the combined block
The result of this process produces 64-bit ciphertext. 

&ensp;&ensp;&ensp; For the encryption of texts longer than 64-bit, the plaintext must be separated into individual blocks. A better approach would be choosing a different key for each 64-bit segment,
however within this implementation the same key will be selected for all segments for ease of testing.

## Objectives:
1. Get familiar with the symmetric cryptography, stream and block ciphers.

2. Implement an example of a stream cipher.

3. Implement an example of a block cipher.


## Implementation:
### A5/1 Cipher
&ensp;&ensp;&ensp; The A5/1 Cipher is implemented as follows:

&ensp;&ensp;&ensp; First, the object is created and a valid 64 bit binary key is passed to it. This key is then loaded into the registers,
thus finishing the initial preparations needed to encrypt a certain message.

```
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
```

&ensp;&ensp;&ensp; The cipher has an encryption function that it overrides from the Cypher class implemented in the previous laboratory work.

```
override fun encrypt(m: String): String {
        var s = ""
        val binary: IntArray = toBinary(m)
        val keystream: IntArray = getKeystream(binary.size)
        for (i in binary.indices) s+=(binary[i] xor keystream[i])
        return s.toString()
    }
```
&ensp;&ensp;&ensp; Within it, the message is first transformed into binary values. Then, the keystream 
from the three LFSRs is obtained, after which each bit from the message is xor-ed with its corresponding bit from the
keystream. The encrypted message is assembled and then turned back into a String to be returned to the client.

&ensp;&ensp;&ensp; The transformation of the message to binary is pretty basic:
```
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

```
&ensp;&ensp;&ensp; Each character from the message is transformed into its binary representation using the available 
method from the standard Java library, after which it is checked wherever its length is less than 8 so it could be padded accordingly.
After which each digit from the binary representation is stored as a separate value within an Integer Array.

&ensp;&ensp;&ensp; On the other hand, the KeyStream method called from the encryption is a bit more complex, following 
the rules described previously in theory:
```
fun getKeystream(length: Int): IntArray {
        val regX = regX.clone()
        val regY = regY.clone()
        val regZ = regZ.clone()
        val keystream = IntArray(length)
        for (i in 0 until length) {
            val maj = getMajority(regX[8], regY[10], regZ[10])

            if (regX[8] == maj) {
                val newStart = regX[13] xor regX[16] xor regX[17] xor regX[18]
                val temp = regX.clone()
                for (j in 1 until regX.size) regX[j] = temp[j - 1]
                regX[0] = newStart
            }

            if (regY[10] == maj) {
                val newStart = regY[20] xor regY[21]
                val temp = regY.clone()
                for (j in 1 until regY.size) regY[j] = temp[j - 1]
                regY[0] = newStart
            }

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
```
&ensp;&ensp;&ensp; All registers are cloned for ease of use. After which the keystream of same length
as the message is generated. For each character of the message, the majority between registers' clocking bits is determined. 
If a register's clocking bit is equal to the majority value, the register is stepped, meaning its 0 bit is generated from the 
xoring of the registers corresponding to the feedback polynomial, while all the rest registers values are shifted to the left.

&ensp;&ensp;&ensp; After all necessary registers have been stepped, the current keystream value is computed by xoring the most significant beat.

&ensp;&ensp;&ensp; Because the operation at the base of the encryption is xor, the decryption follows the same rules, with slight changes being made if the ciphertext was received in binary format.

```
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
```


### DES Cipher
&ensp;&ensp;&ensp; The DES cypher is implemented as follows:

&ensp;&ensp;&ensp; First, an object of type DES is created and passed a hexadecimal key of length 16.
Then, after the declaration of the permutation tables and other util functions for type conversions, the encryption is ready to begin.

```
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

```
&ensp;&ensp;&ensp; The String message is converted into Hexadecimal digits. If its
length contains less than 16 characters, it is padded with 0. If it contains more than 16 characters, it is separated into 
several segments of size 16 that are going to be encrypted.

&ensp;&ensp;&ensp; The encryption of a segment is as follows:
```
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

```
&ensp;&ensp;&ensp; The keys for the 16 rounds are obtained, after which the 
original segment is permutated using the Initial Permutation Table. Then, for each of the keys, the segment is treated through an encryption round.
After the last round, the message's left and right half are reversed and rejoined for a last permutation using the Final Permutation Table.

&ensp;&ensp;&ensp; The permutations of the message characters is nothing but a shuffling of their order according to the position in the table.
 ```
 fun permutation(sequence: IntArray, input: String): String {
        var input = input
        var output: String = ""
        input = ConstantsLab2.hextoBin(input)
        for (i in sequence.indices) output += input[sequence[i] - 1]
        output = ConstantsLab2.binToHex(output)
        return output
    }
 ```
&ensp;&ensp;&ensp; The key generation is accomplished by performing a key permutation according to the respective table,
and then a series of left circular shifts of the current key halves according to the number of places indicated in the shiftbits array,
followed by a second permutation according to the second key permutation table.
```
fun getKeys(key: String): Array<String?> {
        var key = key
        val keys = arrayOfNulls<String>(16)
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
```
&ensp;&ensp;&ensp; While the rounds are accomplished by separating the message in half, 
permutating the right half using the Expansion Table, xor-ing it with the current key and substituting it
according to the S-box, only to permutate it again and xor it with the left half, and then swapping them with each other.

```
fun round(input: String, key: String, num: Int): String {
        var left = input.substring(0, 8)
        var temp = input.substring(8, 16)
        val right = temp
        temp = permutation(ConstantsLab2.EP, temp)
        temp = ConstantsLab2.xor(temp, key)
        temp = sBox(temp)
        temp = permutation(ConstantsLab2.P, temp)
        left = ConstantsLab2.xor(left, temp)
        return right + left
    }
```

&ensp;&ensp;&ensp; The Decryption for DES is similar with the encryption, with the single difference being that the decryption
starts from the last obtained key and goes towards the first for each segment, meaning the rounds are performed in the reverse order.

```
override fun decrypt(m: String): String {
        var message = m
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
        val keys :Array<String?> = getKeys(this.key)
        var plainText = permutation(ConstantsLab2.IP, pt)
        for (i in  15 downTo 0) {
            plainText= keys[i]?.let { round(plainText, it, 15 - i) }.toString()
        }
        plainText = plainText.substring(8, 16) + plainText.substring(0, 8)
        plainText = permutation(ConstantsLab2.IP1, plainText)
        return plainText
    }

```

## Evaluation:
&ensp;&ensp;&ensp; The algorithms perform appropriately both encrypting and decrypting the 
messages in a manner that preserves their original text for the receiver.
![algorithms at work](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img1.PNG)

## Conclusions:
Within this laboratory work we have studied and implemented two modern symmetric ciphers. One Stream Cipher (A5/1) and
one Block Cipher (DES). Compared to the classic ciphers, the algorithms proved to be more complex and thus less likely to be broken, although the two
ciphers are still less sophisticated than their more recent counterparts, as well as the currently used asymmetric ciphers.
