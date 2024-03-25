package com.ajith.notificationservice.consumer.service;

import com.ajith.notificationservice.event.InviteMemberEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class InvitationService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendMail(InviteMemberEvent event) throws MessagingException, UnsupportedEncodingException {
        MimeMessage emailMessage = javaMailSender.createMimeMessage ();
        MimeMessageHelper helper = new MimeMessageHelper ( emailMessage );
        String URL = "http://localhost:5173/notification?token=";
        String confirmationLink = URL+event.getToken ();

        helper.setFrom ( "plan.b.projectmanager@gmail.com" ,"PLAN - B " );
        helper.setTo ( event.getEmail () );
        String subject = "You have invited to join the project ";
        String content = "<html><body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #007bff;'>Invite for join to project</h2>"
                + "<span>Hello,</span>"+"  " +event.getEmail ()
                + "<p>"+event.getMessage ()+"</p>"
                + "<p>"+"This invitation will expire on :"+event.getExpirationTime ()+"</p>"
                + "<p><a href='" + confirmationLink+ "' style='background-color: #007bff; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Click here to Join</a></p>"
                + "</body></html>";

        helper.setSubject ( subject );
        helper.setText(content, true);
        javaMailSender.send ( emailMessage );
    }
}
