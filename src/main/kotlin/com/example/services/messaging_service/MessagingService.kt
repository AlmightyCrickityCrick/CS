package services.messaging_service

import services.digital_signature_service.DigitalSignatureService

class MessagingService {

    fun start(){
        var message = "HelloWorld"
        var dss = DigitalSignatureService()
        var signature = dss.getDigitalSignature(message)
        dss.verifyDigitalSignature(signature)
    }
}