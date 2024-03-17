package com.example.util

import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

class EmailServiceImpl : EmailService {

    override fun sendEmail(subject: String, message: String, sentTo: String) {
        val email = SimpleEmail()
        email.hostName = System.getenv("SMTP_HOST")
        email.setSmtpPort(System.getenv("SMTP_PORT").toInt())
        email.authenticator =
            DefaultAuthenticator(System.getenv("SMTP_LOGIN"), System.getenv("SMTP_PASS"))
        email.isSSLOnConnect = true
        email.setFrom(System.getenv("SMTP_LOGIN"))
        email.subject = subject
        email.setMsg(message)
        email.addTo(sentTo)
        email.send()
    }
}

interface EmailService {
    fun sendEmail(subject: String, message: String, sentTo: String)
}