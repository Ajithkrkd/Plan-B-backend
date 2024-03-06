package com.ajith.notificationservice.consumer.service;

import com.ajith.notificationservice.event.UserEmailTokenEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
@Service
public class ForgotPasswordService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendMail(UserEmailTokenEvent event) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailMessage = javaMailSender.createMimeMessage ();
        MimeMessageHelper helper = new MimeMessageHelper ( emailMessage );

        String confirmationLink = event.getConfirmation_link ( )+event.getToken ();

        helper.setFrom ( "plan.b.projectmanager@gmail.com" ,"PLAN - B " );
        helper.setTo ( event.getEmail () );
        String subject = "Here's the link to Change your password";
        String content = "<html><body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #007bff;'>Verify Your Email</h2>"
                + "<span>Hello,</span>"+"  " +event.getFullName ()
                + "<p>You have requested to change your password.</p>"
                + "<p><a href='" + confirmationLink+ "' style='background-color: #007bff; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Click here to verify your email</a></p>"
                + "<p>If the above link doesn't work, you can copy and paste this URL into your browser:</p>"
                + "<p><a href='" + confirmationLink +"</a></p>"
                + "<p style='color: #888;'>Ignore this email if you remember your password or if you haven't made this request.</p>"
                + "</body></html>";

        helper.setSubject ( subject );
        helper.setText(content, true);
        javaMailSender.send ( emailMessage );
    }
}