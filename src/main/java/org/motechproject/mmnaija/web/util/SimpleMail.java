/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web.util;

/**
 * @author Seth Adarkwa Kwakwa email : seth.kwakwa@corenett.com,
 * kwasett@gmail.com
 * @copyright CoreNett Limited Ghana
 * @Date Mar 7, 2013
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.*;

public class SimpleMail {

    private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
    private static final String SMTP_AUTH_USER = "kwasett";
    private static final String SMTP_AUTH_PWD = "$4Sethoo";


    public void send(String subject, String content, String sender, List<String> recipients) throws Exception {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "true");
            Session mailSession = null;

            Authenticator auth;
            auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_AUTH_USER, SMTP_AUTH_PWD);
                }
            };

            try {
                mailSession = Session.getDefaultInstance(props, auth);
            } catch (Exception e) {
                mailSession = Session.getInstance(props, auth);
            }

            for (String string : recipients) {
                sendEmail(mailSession, string, subject, content);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public static void sendEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("mmnaija@mmnaija.com", "MM Nigeria"));

            msg.setReplyTo(InternetAddress.parse("mmnaija@mmnaija.com", false));

            msg.setSubject(subject, "UTF-8");
            
            msg.setContent(body,"text/html");
//            msg.setText(body, "text/html; charset=utf-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
