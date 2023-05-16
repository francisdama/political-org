package com.xclusive.political_web_app.Event.Listener;

import com.xclusive.political_web_app.Event.RegistrationCompleteEvent;
import com.xclusive.political_web_app.User.AppUser;
import com.xclusive.political_web_app.User.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationCompleteListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserService userService;

    private  final JavaMailSender mailSender;
    private AppUser appUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get newly registered User.
        appUser = event.getAppUser();
        // 2. Create a verification token for user
        String verificationToken = UUID.randomUUID().toString();
        // 3. save the verification token of the user
        userService.saveVerificationTokenForUser(verificationToken, appUser);
        // 4. build a verification url to be sent the user
        String url = event.getApplicationUrl()+ "/register/verifyEmail?token=" + verificationToken;
        // 5. send the email
        try {
            sendVerificationEmail(url);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
//
//        log.info("Click the link to verify your account: {}",
//                url);
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, "+ appUser.getFirstname()+ ", </p>"+
                "<p>Thank you for registering with us,"+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("dailycodework@gmail.com", senderName);
        messageHelper.setTo(appUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
