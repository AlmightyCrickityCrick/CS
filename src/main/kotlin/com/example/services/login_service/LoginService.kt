package services.login_service

import services.hashing_service.HashingService
import java.util.*
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.collections.ArrayList

data class SessionRequest(var id: Int, var request_time:Long, var code:String)
data class tokenMapping(var id:Int, var token:String)


object LoginService {
    var db = Database()
    var awaitManager = ArrayList<SessionRequest>()
    var sessionManager = ArrayList<tokenMapping>()

    fun register(username:String, email:String, password:String){
        if(!checkIfUserExists(email))
        {
            var hashedPassword = HashingService.hashString(password)
            var x = db.addUser(username, email, hashedPassword)
        }
    }

    fun checkIfUserExists(id:Int):Boolean{
        return db.getUser(id) != null
    }

    fun checkIfUserExists(email: String):Boolean {
        return db.exists(email)
    }

    fun getUserByToken(token:String):User?{
        for (t in sessionManager)if(t.token == token) return db.getUser(t.id)
        return null
    }

    fun checkUserIfDidLogin1(email: String):Boolean{
        var usr = db.getUserByEmail(email)
        if(usr!=null){
            for (u in awaitManager) if (usr.id == u.id) return true
        }
        return false
    }

    //Authentication allowed 0 - normal user
    //Authentication denied -1
    //Authentication first step finished proceed to next 1 - specialUser
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

    fun getToken(email: String): String {
        var id = db.getUserByEmail(email)?.id
        for (t in sessionManager) if (id == t.id) return t.token
        return ""
    }

     private fun addToken(id: Int){
         var token = ""
         for (i in (0..12)) token+= "${(1..9).random()}"
         sessionManager.add(tokenMapping(id, token))
     }

    private fun generateCode():String{
        var code = ""
        for (i in (0..6)) code+= (1..9).random().toString()
        return code
    }
    fun sendCodeToEmail(email: String, code: String){
        val userName =  my_username
        val password =  my_password

        val emailFrom = my_email
        val emailTo = email

        val subject = "Authentication Code"
        val text = "$code"

        val props = Properties()
        putIfMissing(props, "mail.smtp.host", "smtp.office365.com")
        putIfMissing(props, "mail.smtp.port", "587")
        putIfMissing(props, "mail.smtp.auth", "true")
        putIfMissing(props, "mail.smtp.starttls.enable", "true")

        val session = Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(userName, password)
            }
        })

        session.debug = true

        try {
            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress(emailFrom))
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo, false))
            mimeMessage.setText(text)
            mimeMessage.subject = subject
            mimeMessage.sentDate = Date()

            val smtpTransport = session.getTransport("smtp")
            smtpTransport.connect()
            smtpTransport.sendMessage(mimeMessage, mimeMessage.allRecipients)
            smtpTransport.close()
        } catch (messagingException: MessagingException) {
            messagingException.printStackTrace()
        }
    }

    private fun putIfMissing(props: Properties, key: String, value: String) {
        if (!props.containsKey(key)) {
            props[key] = value
        }
    }

    //Returns 0 if corect code introduced
    //Returns -1 if incorrect code or more than 10 minutes since code sent
    fun loginStep2(email: String, code: String):Int{
        var usr = db.getUserByEmail(email)
        if(usr!=null){
            for (x in awaitManager) if (usr.id == x.id){
                if(code == x.code && ((System.currentTimeMillis() - x.request_time) * 0.001 / 60 ) <10) {
                    addToken(usr.id)
                    return 0
                }
            }
        }
        return -1
    }
}