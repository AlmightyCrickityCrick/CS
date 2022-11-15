package hash.messaging_service

import hash.security_service.DigitalSignatureService

class MessagingService {

    fun start(){
        var message = "HelloWorld"
        var dss = DigitalSignatureService()
        var signature = dss.getDigitalSignature(message)
        dss.verifyDigitalSignature(signature)
    }
}