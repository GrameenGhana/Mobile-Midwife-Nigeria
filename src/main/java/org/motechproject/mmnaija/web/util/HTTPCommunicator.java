/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web.util;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.motechproject.mmnaija.domain.Message;
import org.motechproject.mmnaija.domain.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author seth
 */
public class HTTPCommunicator {
    @Autowired
//    private SettingsFacade settingsFacade;
    private static final Logger log = LoggerFactory.getLogger(HTTPCommunicator.class);

    public static String IVR_URL = "http://airtel1.v2nmobile.co.uk/mmarkHTTPCommunicatoret/GM_PLAY_FILE?msisdn=%s&callfile=%s.wav";
    public static String SMS_URL = "http://83.138.190.168:8080/pls/vas2nets.inbox_pkg.schedule_sms?password=5C6739D81C1E3AFC5A859B27D2AA9CBC&"
            + "username=dhutchful@grameenfoundation.org&sender=561&receiver=%s&message=%s&message_type=1";
    public static String SUCCESSFUL_RESPONSE = "1";

    public static String sendSMS(String msisdn, String message) {
//        return (doGet(
//                String.format(SMS_URL, msisdn, message)));
 //for the interim
        return sendSimpleMail(msisdn, message, "SMS");
    }

    public static String sendSMS(Schedule schedule,Message msg) {
        return sendSMS(schedule.getSubscriber(), msg.getMessageKey());
    }

    public static String sendVoice(Schedule schedule,Message msg) {
        return sendVoice(schedule.getSubscriber(), msg.getMessageKey());
    }

    public static String sendVoice(String msisdn, String messageKey) {
//        return (doGet(
//                String.format(IVR_URL, msisdn, messageKey)));
        
        //for the interim
        return sendSimpleMail(msisdn, messageKey, "Voice");
    }

    public static String doGet(String urlStr) {
        URLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();

            // Open the connection
            connection = url.openConnection();
        } catch (Exception e) {
        }

        try {
            InputStream in = connection.getInputStream();

            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer, "utf-8");
            String theString = writer.toString();
            return theString;
        } catch (Exception e) {
        }
        return "";
    }

    public static String doPost(String serviceUrl, String queryString) {
        URLConnection connection = null;
        try {

            URL url = new URL(serviceUrl);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            // Open the connection
            connection = url.openConnection();
            connection.setDoInput(true);
            connection.setUseCaches(false); // Disable caching the document
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Content-Type",
                    "text/html");

            OutputStreamWriter writer = null;

            log.info("About to write");
            try {
                if (null != connection.getOutputStream()) {
                    writer = new OutputStreamWriter(connection.getOutputStream());
                    writer.write(queryString); // Write POST query

                } else {
                    log.warn("connection Null");
                }
                // string.
            } catch (ConnectException ex) {
                log.warn("Exception : " + ex);
                // ex.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Exception lg) {
                        log.warn("Exception lg: " + lg.toString());
                        //lg.printStackTrace();
                    }
                }
            }

            InputStream in = connection.getInputStream();

//            StringWriter writer = new StringWriter();
            IOUtils.copy(in, writer, "utf-8");
            String theString = writer.toString();
            return theString;
        } catch (Exception e) {
            //e.printStackTrace();
            log.warn("Error URL " + e.toString());
            return "";
        }
    }

    public static String sendSimpleMail(String number, String content, String contentType) {

        String msg = String.format("Scheduled Message Detail<br />"
                + "MSISDN       : %s<br />"
                + "Content Type : %s<br/>"
                + "Content <br />"
                + "%s<br /><br />"
                + "", number, contentType, content);
        List<String> recipient = new ArrayList<>();
        recipient.add("kwasett@gmail.com");
        recipient.add("skwakwa@grameenfoundation.org");
        try {
       
                    new SimpleMail().send(String.format("Motech Message %s for %s", contentType, number), msg, number, recipient);
        } catch (Exception e) {
            return "01";
        }
        return "00";
    }

}
