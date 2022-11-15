# Topic: Hash functions and Digital Signatures.

### Course: Cryptography & Security

### Student: Scripca Lina
### Professor: Vasile Drumea

----

## Theory
&ensp;&ensp;&ensp; Hashing is a technique used to compute a new representation of an existing value, message or any piece of text. The new representation is also commonly called a digest of the initial text, and it is a one way function meaning that it should be impossible to retrieve the initial content from the digest.

&ensp;&ensp;&ensp; Such a technique has the following usages:
* Offering confidentiality when storing passwords,
* Checking for integrity for some downloaded files or content,
* Creation of digital signatures, which provides integrity and non-repudiation.

#### Examples of hashes
1. Argon2
2. BCrypt
3. MD5 (Deprecated due to collisions)
4. RipeMD
5. SHA256 (And other variations of SHA)
6. Whirlpool

### SHA-256
&ensp;&ensp;&ensp; SHA 256 is a part of the SHA 2 family of algorithms, where SHA stands for Secure Hash Algorithm. Published in 2001, it was a joint effort between the NSA and NIST to introduce a successor to the SHA 1 family, which was slowly losing strength against brute force attacks.
The significance of the 256 in the name stands for the final hash digest value, i.e. irrespective of the size of plaintext/cleartext, the hash value will always be 256 bits.

&ensp;&ensp;&ensp; The steps used in the hashing of message with SHA-256 are the following:
1. Pre-processing of the text:
   * transforming into binary, 
   * appending 1 to the end, 
   * padding with 0's until 64 bits remain till reaching multiple of 512 bits, 
   * adding  64 bits to the end, where the 64 bits are a big-endian integer representing the length of the original input in binary.
2. Initialization of hash values (8 hardcoded constants that represent the first 32 bits of the fractional parts of the square roots of the first 8 primes)
3. Initialization Round constants (Each value (0-63) is the first 32 bits of the fractional parts of the cube roots of the first 64 primes (2 - 311))
4. Chunk loop (for each chunk of 512 bits from the processed message, iterate through a loop to mutate the hash values)
5. Creation of message schedule
6. Compression the message
7. Modification of final results.

### Digital Signature

&ensp;&ensp;&ensp; A digital signature is an electronic, encrypted, stamp of authentication on digital information such as email messages, macros, or electronic documents. A signature confirms that the information originated from the signer and has not been altered.

&ensp;&ensp;&ensp; In order to create digital signatures, the initial message or text needs to be hashed to get the digest. After that, the digest is to be encrypted using a public key encryption cipher with the private key of the user. Having this, the obtained digital signature can be decrypted with the public key and the hash can be compared with an additional hash computed from the received plaintext message to check the integrity of it.


## Objectives:
1. Get familiar with the hashing techniques/algorithms.
2. Use an appropriate hashing algorithms to store passwords in a local in memory DB.
3. Use an asymmetric cipher to implement a digital signature process for a user message.

## Implementation:
### Password Storage using Hashing
&ensp;&ensp;&ensp; The current implementation utilizes an in memory Database for password storage.
The user information described by id, username and password are kept within a 
list of Users inside the Database as follows:

```
data class User(var id:Int, var username:String, var password:String)

class Database {
    var db = ArrayList<User>()
```
&ensp;&ensp;&ensp; The Database itself is initialized during runtime within the LoginService.
```
class LoginService {
    lateinit var db :Database
    fun start(){
        db = Database()
```
&ensp;&ensp;&ensp; The Database allows usage of get and add methods regarding users. The addUser method takes the username and password of the user as arguments, performs a SHA-256 hashing on the password, and generates and returns the id of the newly added user back to the LoginService.
```
db.add(User(db.size, username, HashService.hashString(password)))
return db.size - 1
```
while the getUser method takes the id of the user searched within the database, and returns the User structure.
```
for (u in db)
   if (u.id == id) return u
```
&ensp;&ensp;&ensp; The Hashing of the password is executed within the HashService 
singleton object, within the function hashString. It utilizes the java.security 
library for obtaining the Digest of the string. To hash the password introduced by the user, an instance of MessageDigest is generated 
based on the SHA-256 algorithm. Then the message is encoded into a byte array and the encoded hash is produced based on the values within the array, according to the declared algorithm.

```
val digest = MessageDigest.getInstance(algorithm)
val encodedhash = digest.digest(
         string.toByteArray(StandardCharsets.UTF_8)
        )
        
```
&ensp;&ensp;&ensp;After this the hash is transformed into a string of hex for ease of reading.  

```
var hexString = ""
        for (i in hash.indices) {
            val hex = Integer.toHexString(0xff and hash[i].toInt())
            if (hex.length == 1) {
                hexString+='0'
            }
            hexString+=hex
        }
```
&ensp;&ensp;&ensp; After this the hashing may be checked by hashing once again one of the passwords sent by the clients and comparing the freshly hashed representation with the one that is kept within the Database.

```
var x = db.addUser("Lina S", "123456f")
var hashedPassword = HashService.hashString("123456f")
println("Password in database ${db.getUser(x)?.password}  password hash $hashedPassword")
if(db.getUser(x)?.password == hashedPassword) println("The hashes of passwords are equal")
```

### Digital Signature Verification
&ensp;&ensp;&ensp; Within this implementation, the java.security's RSA algorithm is used for asymmetrical encryption and SHA-256 for hashing.
As described within the theory subchapter, the creation and verification of Digital Signatures requires following a number of steps.

&ensp;&ensp;&ensp; First, within the MessagingService a message is generated and a user's object of DigitalSignature class is instantiated. Then the message is sent to the DigitalSignature's getDigitalSignature method to obtain the components for verifying the integrity of a user's message.
This verification may also take place within the DigitalSignature's verifyDigitalSignature method.

```
var message = "HelloWorld"
var dss = DigitalSignatureService()
var signature = dss.getDigitalSignature(message)
dss.verifyDigitalSignature(signature)
```
&ensp;&ensp;&ensp; Upon the initialization of the DigitalSignature, an instance of the RSAEncryptionService is created.
```
var encryptionService :RSAEncryptionService = RSAEncryptionService()
```
&ensp;&ensp;&ensp; which will later be used for encryption and decryption of the digitalSignature(encryption/decryption of the hashing of the original massage)

&ensp;&ensp;&ensp; Within the getDigitalSignature method, the previously mentioned HashService is called to receive a digest of the message sent by users, after which it is encrypted using RSA with the user's private key and then sent as a digitalSignature component, along with the plaintext message and the user's public key within a Triple data structure.

```
var hash = HashService.hashString(s)
var digitalSignature = encryptionService.encrypt(hash, "private")
return Triple<String, String, PublicKey>(s, digitalSignature, encryptionService.publicKey)
```
&ensp;&ensp;&ensp; Within the RSAEncryptionSevice, during an object's initialization the public and private key of a user are generated 
through the KeyPairGenerator.
```
var generator = KeyPairGenerator.getInstance(algorithm)
generator.initialize(size)
var pair = generator.genKeyPair()
publicKey = pair.public
privateKey = pair.private
```
&ensp;&ensp;&ensp; For encryption of a text an RSA Cipher instance is created and initialized in ENCRYPT_MODE with the selected key.
Then the message is transformed into a byte array and encrypted through the doFinal method.
```
var useKey = if(key == "private") privateKey else publicKey
val encryptCipher = Cipher.getInstance(algorithm)
encryptCipher.init(Cipher.ENCRYPT_MODE, useKey)
var secretMessageBytes = m.toByteArray(StandardCharsets.UTF_8)
var encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes)
```
&ensp;&ensp;&ensp; A similar approach is also used within the decryption method with the single difference being in the mode of operation of the Cipher, which was switched to DECRYPT_MODE.

```
var useKey = if(key == "private") privateKey else publicKey
val decryptCipher = Cipher.getInstance(algorithm)
decryptCipher.init(Cipher.DECRYPT_MODE, useKey)
var decryptedMessageBytes = decryptCipher.doFinal(message)
```
&ensp;&ensp;&ensp; For the verification of the DigitalSignature, the Triple obtained through the getDigitalSignature method is decomposed into its elements.
The digitalSignature itself is decrypted through the RSA algorithm using the public key of the user, the plain message is hashed through SHA-256 and then the two values of the hash are compared to determine if the message has been tempered with.

```
var decryptedHash = encryptionService.decrypt(signature.second, "public")
var hash = HashService.hashString(signature.first)
 if (hash == decryptedHash) {
        println(hash)
        println(decryptedHash)
        println("The message hasn't been tampered")
        }
```
## Evaluation:
&ensp;&ensp;&ensp; As can be seen below, the algorithms perform appropriately with the same string values hashing and encrypting to the same digest and ciphertext for the runtime generated keys.

![algorithms at work](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img3.png)

![algorithms at work](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img4.png)

## Conclusion:
&ensp;&ensp;&ensp; Hashing algorithms play an important role in cryptography, allowing us to get unique representations for each string given and creating
strings of characters whose original form is irretrievable, making them a perfect tool to use within Digital Signatures to ensure the integrity of the message.