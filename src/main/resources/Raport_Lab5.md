# Topic: Web Authentication & Authorisation.

### Course: Cryptography & Security
### Student: Scripca Lina
### Professor: Vasile Drumea

----

## Theory

&ensp;&ensp;&ensp; Authentication & authorization are 2 of the main security goals of IT systems and should not be used interchangibly. Simply put, during authentication the system verifies the identity of a user or service, and during authorization the system checks the access rights, optionally based on a given user role.

&ensp;&ensp;&ensp; There are multiple types of authentication based on the implementation mechanism or the data provided by the user. Some usual ones would be the following:
- Based on credentials (Username/Password);
- Multi-Factor Authentication (2FA, MFA);
- Based on digital certificates;
- Based on biometrics;
- Based on tokens.

&ensp;&ensp;&ensp; Regarding authorization, the most popular mechanisms are the following:
- Role Based Access Control (RBAC): Base on the role of a user;
- Attribute Based Access Control (ABAC): Based on a characteristic/attribute of a user.


## Objectives:
1. Create a webservice containing the work from previous laboratory works.
2. Create and implement basic and MFA within the service.
3. Provide different services based on the authorization level of users.
4. As a service may be proposed previously implemented ciphers.


## Implementation:

&ensp;&ensp;&ensp; The web service is implemented in Kotlin, using the Ktor embedded Server with routing and serialization plugins with the initial setup containing two users - one with simple role and one with admin role.

```
 setUp()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}

fun setUp() {
    LoginService.db.addSpecialUser("Slina", "s02linamd@gmail.com", "pass11")
    LoginService.db.addUser("TestTest", "test@mail.com", "pass12")
}
```
&ensp;&ensp;&ensp; The In memory database contains a list of users with the following structure:
```
data class User(var id:Int, var username:String, var email:String, var password:String, var role:Int)

```
and is available within the LoginService singleton object
```
object LoginService {
    var db = Database()
    var awaitManager = ArrayList<SessionRequest>()
    var sessionManager = ArrayList<tokenMapping>()
```
the two roles available are denoted as 0 and 1. The 0 role requires a simple authentication with just email and password as denoted within the login method and path.

```
fun loginStep1(email: String, password: String):Int{
        val usr = db.getUserByEmail(email)
        if (usr != null) {
            if (usr.password == HashingService.hashString(password)){
                if (usr.role == 0) {
                    addToken(usr.id)
                    return 0
                }
                if(usr.role == 1) {
                    var c = generateCode()
                   sendCodeToEmail(email, c)
                    awaitManager.add(SessionRequest(usr.id, System.currentTimeMillis(), c))
                    return 1
                }
            }
        }
        return -1
    }
```
&ensp;&ensp;&ensp; First, there is a check of whether the user exists in the database. Then the login is performed. If the user is simple, a random token is generated,
and sent back to the user. If the user is admin, he is prompted to continue with the second step of login, while a code that exists only 10 minutes is generated and sent to the email,
with the id of the admin user being saved within the awaitManager.
```
post("/login"){
            var data = call.receive<String>()
            var request = Json.decodeFromString(SimpleLoginRequest.serializer(), data)
            if(LoginService.checkIfUserExists(request.email)) {
                var result = LoginService.loginStep1(request.email, request.password)
                if (result == 0) {
                    call.respond(
                        Json.encodeToString(
                            LoginResponse.serializer(),
                            LoginResponse(LoginService.getToken(request.email))
                        )
                    )
                } else if (result == -1) {
                    call.respondText("Email or password incorrect")
                } else {
                    call.respondText("Verification Code has been sent. Verify email and proceed to authentication step 2.")
                }
            } else call.respondText("Email or password incorrect")
        }
```
&ensp;&ensp;&ensp; For the second step of the 2FA, the user has to introduce their email and code received in the email.
Then it is checked whether the user has attempted the first step, and then it is checked whether the code for this particular user matches the one he sent from client. If yes, he will receive a token that will be used to perform operations within the system.

```
var data = call.receive<String>()
            var request = Json.decodeFromString(MFALoginRequest.serializer(), data)
            if(LoginService.checkIfUserExists(request.email) && LoginService.checkUserIfDidLogin1(request.email)) {
                var result = LoginService.loginStep2(request.email, request.code)
                if (result == 0) {
                    call.respond(
                        Json.encodeToString(
                            LoginResponse.serializer(),
                            LoginResponse(LoginService.getToken(request.email))
                        )
                    )
                } else {
                    call.respondText("Code incorrect or expired")
                }
            } else call.respondText("Please complete first step of login")
        }
```

&ensp;&ensp;&ensp; The possibility to use one of the 3 ciphers to decode, encode messages is available to all users in the system

```
when(cipher){
           "caesar" -> {
               var key = (1..25).random()
               var c = CaesarCipher(key)
               var ciphertext = c.encrypt(m)
               return Pair(key.toString(), ciphertext)
           }
            "vigenere" -> {
                var key = generateHexKey(6)
                var c = VigenereCipher(key)
                var ciphertext = c.encrypt(m)
                return Pair(key, ciphertext)
            }
            "des" ->{
                var key = generateHexKey(15)
                var c = DES(key)
                var ciphertext = c.encrypt(m)
                return Pair(key, ConstantsLab2.hexToStr(ciphertext))
            }

            else -> return Pair("", "")

        }
```
as long as they are logged in.

```
var data = call.receive<String>()
            var request = Json.decodeFromString(MessageEncodeRequest.serializer(), data)
            if (LoginService.getUserByToken(request.token)!=null){
                var response = CipherService.encryptMessage(request.message, request.cipher)
                call.respond(Json.encodeToString(MessageEncodeResponse.serializer(), MessageEncodeResponse(response.second, response.first)))
            } else call.respondText("Please login or register")
        }

```
&ensp;&ensp;&ensp; However, the possibility to promote/demote/delete users is available only to admins. And their role is checked based on the token sent when they try to 
access the /admin/request path.

```
post("/admin/request"){
            var data = call.receive<String>()
            var request = Json.decodeFromString(AdminRequest.serializer(), data)
            var usr = LoginService.getUserByToken(request.token)
            if (usr== null)call.respondText("Please login or register")
            else if (usr.role!=1) call.respondText("Unauthorized action")
            else{
                var response = AdminBoard.execute(request.id, request.operation)
                if(response == -1) call.respondText("User or action doesnt exist")
                else call.respondText("Operation successful")
            }
        }
```

## Evaluation:
&ensp;&ensp;&ensp; The authentication and authorization implemented within this laboratory work properly when used with Postman as shown below.

Login with simple user

![Login with simple user](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img_1.png)

Using ciphers

![Using ciphers](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img_2.png)

Trying to change users from a simple user

![Trying to change users from a simple user](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img_3.png)

Login step 1 with admin

![Login step 1 with admin](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img_4.png)

Login step 2 with admin

![Login step 2 with admin](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img_5.png)

Modifying a user with admin

![Modifying a user with admin](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img_6.png)

Checking modification result

![Checking modification result](https://github.com/AlmightyCrickityCrick/CS/blob/main/src/main/resources/images/img_7.png)




&ensp;&ensp;&ensp; It must be remarked that the email sending system uses environment variables that must be changed to proper email and password if tested on another device. That, or 
the system may be commented out leaving only the creation of the code itself, with the server run in debug mode to catch the created code value.

## Conclusion
&ensp;&ensp;&ensp; Authentication and Authorization are two systems that are vital for any webservice, making sure that malicious actors may not access or perform 
dangerous actions on data kept within the database, so a correct implementation is something worth investing time in.
