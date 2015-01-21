/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web.util;

/**
 * @author Seth Adarkwa Kwakwa
 *         email : seth.kwakwa@corenett.com, kwasett@gmail.com    
 * @copyright CoreNett Limited Ghana       
 * @Date   Mar 7, 2013
 */

import java.util.List;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

public class SimpleMail {

    private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
    private static final String SMTP_AUTH_USER = "kwasett";
    private static final String SMTP_AUTH_PWD = "$4Sethoo";

//  public static void main(String[] args) throws Exception {
//        List<String> str = new ArrayList<String>();
//        str.add("michael.amankwa@corenett.com");
//        str.add("seth.kwakwa@corenett.com");
//        new SimpleMail().send("Initial Email Trail", "SendGrid Trail <strong>By Corenett</strong>", "noreply@corenett.com", str);
// }
    public void send(String subject, String content, String sender, List<String> recipients) throws Exception {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.port", 587);
            props.put("mail.smtp.auth", "true");

            System.out.println("Sending mail II: " + subject);

            System.out.println("To I: " + recipients.get(0));
            Authenticator auth = new SMTPAuthenticator();
            System.out.println("brak w1");

            
            Session mailSession = null;
            try {
                  mailSession = Session.getDefaultInstance(props, auth);
            } catch (Exception e) {
                mailSession = Session.getInstance(props, auth);
            }
          


            System.out.println("brak r1");
            // uncomment for debugging infos to stdout
            // mailSession.setDebug(true);
            Transport transport = mailSession.getTransport();

            MimeMessage message = new MimeMessage(mailSession);

            Multipart multipart = new MimeMultipart("alternative");

            System.out.println("brak 1");
            BodyPart part2 = new MimeBodyPart();
            System.out.println("Break II");

            part2.setContent(content, "text/html");
            System.out.println("Content : " + content);
            multipart.addBodyPart(part2);

            message.setContent(multipart);
            message.setFrom(new InternetAddress(sender));
            message.setSubject(subject);
            for (String string : recipients) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(string));
            }

            transport.connect();
            transport.sendMessage(message,
                    message.getRecipients(Message.RecipientType.TO));

            transport.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   

    private class SMTPAuthenticator extends javax.mail.Authenticator {

        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }
}
