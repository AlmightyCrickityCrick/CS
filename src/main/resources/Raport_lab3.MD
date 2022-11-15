# Topic: Asymmetric Ciphers.

### Course: Cryptography & Security
### Student: Scripca Lina
### Professor: Vasile Drumea

----

## Theory
&ensp;&ensp;&ensp; Asymmetric Cryptography (a.k.a. Public-Key Cryptography)deals with the encryption of plain text when having 2 keys, one being public and the other one private. The keys form a pair and despite being different they are related.

&ensp;&ensp;&ensp; As the name implies, the public key is available to the public but the private one is available only to the authenticated recipients.

&ensp;&ensp;&ensp; A popular use case of the asymmetric encryption is in SSL/TLS certificates along side symmetric encryption mechanisms. It is necessary to use both types of encryption because asymmetric ciphers are computationally expensive, so these are usually used for the communication initiation and key exchange, or sometimes called handshake. The messages after that are encrypted with symmetric ciphers.


#### Examples
1. RSA
2. Diffie-Helman
3. ECC
4. El Gamal
5. DSA

### Diffie-Helman 

&ensp;&ensp;&ensp; Diffie-Helman is a key exchange algorithm that allows securely exchanging private keys over unsecure communication channels.
DH is one of the earliest practical examples of public key exchange implemented within the field of cryptography. Published in 1976 by Diffie and Hellman, this is the earliest publicly known work that proposed the idea of a private key and a corresponding public key.

Traditionally, secure encrypted communication between two parties required that they first exchange keys by some secure physical means, such as paper key lists transported by a trusted courier. The Diffie–Hellman key exchange method allows two parties that have no prior knowledge of each other to jointly establish a shared secret key over an insecure channel. This key can then be used to encrypt subsequent communications using a symmetric-key cipher.

Diffie–Hellman is used to secure a variety of Internet services. However, research published in October 2015 suggests that the parameters in use for many DH Internet applications at that time are not strong enough to prevent compromise by very well-funded attackers, such as the security services of some countries.


## Objectives:
1. Get familiar with the asymmetric cryptography mechanisms.

2. Implement an example of an asymmetric cipher.


## Implementation:
&ensp;&ensp;&ensp; The Diffie-Hellman algorithm is implemented as follows:

&ensp;&ensp;&ensp; First both interlocutors select a prime number P that is sufficiently big. For this,
the function getPrime generates a random number in the requested interval and then calls the isPrime function to check if it's a prime.
If the number doesnt meet the condition, it is incremeneted and checked again until in eventually does.
```
        var i = (start .. stop).random()
        while(!isPrime(i)) i++
```
&ensp;&ensp;&ensp; Within the isPrime function, it is checked wherever the number is 2 or 3, then whether it is divisible by 
2 and 3. And then it starts checking for other possible prime numbers till the square route of the prime candidate.
 
```
        if(i <=3) return true
        if((i % 2 == 0 ) || ( i % 3 == 0)) return false
        var n = 5
        val stop = sqrt(i.toDouble())
        while (n<=stop) if ((i %n ==0) || (i%(n+2) ==0 )) return false else n+=6
        return true
```
&ensp;&ensp;&ensp; After the P is selected, a generator must be chosen. A generator is a primitive 
root of a modulo n, meaning it is an integer that when raised to a power x and mod n will give a different
result for each x in [0, n-2]. This is accomplished within the following lines of getPrimitive function.

```
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
```
&ensp;&ensp;&ensp; Where manualPower function is a function to calculate the power by multiplying a number by itself the 
exponent number of times, and then using the modulo operation whenever the result is bigger than the modulo.

&ensp;&ensp;&ensp; Then each interlocutor generates a random prime number and using it and the agreed upon generator and p,
creates a public key that will be shared with the other.
```
init {
        self_key = Constants3.getPrime(1, 2000)
        createPublicKey()
    }

    override fun createPublicKey() {
        public_key = Constants3.manualPower(generator.toLong(), self_key.toLong(), P.toLong())
    }
```
&ensp;&ensp;&ensp;Then, using the same operation but with the received public key as base, the interlocutors will generate the 
private key that will be used for further symmetric encryption of messages.

```
override fun generateSecretKey(pk:Long) {
        shared_secret_key = Constants3.manualPower(pk, self_key.toLong(), P.toLong())
    }
```
&ensp;&ensp;&ensp; An important detail is that the shared secret key must be the same for both users.

## Evaluation:
&ensp;&ensp;&ensp; The algorithm performs appropriately with the public and self private keys being different, while the resulted shared private key being the same.
![algorithms at work](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img2.png)

## Conclusion:
&ensp;&ensp;&ensp; Within this laboratory work we studied the mechanism for asymmetric encryption, which uses as base modulo arithmetic.    