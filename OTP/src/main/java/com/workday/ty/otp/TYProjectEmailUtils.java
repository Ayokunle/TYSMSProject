package com.workday.ty.otp;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;
import java.util.Random;

public class TYProjectEmailUtils {

    private final static String Trusted_Email = "tyexperienceprogramme@gmail.com";
    private final static String Trusted_Email_App_password = "aqsndkxmjkdspvbl";

    public void sendEmail(String username, String personToSendEmailTo, String code) {
        SimpleMailMessage msg = new SimpleMailMessage(new org.springframework.mail.SimpleMailMessage());
        msg.setTo(personToSendEmailTo);
        msg.setSubject("Your TY Workshop One Time Passcode");
        msg.setText("Dear " + username +
                        ", please use the following 6 digit code to log into your TY Project Web Application. " +
                        code);
        try {
              this.getMailSender().send(msg);
        }
        catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private JavaMailSender getMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(Trusted_Email);
        mailSender.setPassword(Trusted_Email_App_password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }



    /**
     * Sends a Randomly generated code to a sender for MFA
     */
    public static String generateOneTimeCodeAndMessage() {
        return generateRandomCode();
    }

    /**
     * Generates a random 6 digit code
     */
    private static String generateRandomCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String. format("%06d", number);
    }
}
