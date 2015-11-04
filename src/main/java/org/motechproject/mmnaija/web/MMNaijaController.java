/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.mmnaija.domain.Message;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Schedule;
import org.motechproject.mmnaija.domain.ScheduleStatus;
import org.motechproject.mmnaija.domain.Status;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.MessageDataService;
import org.motechproject.mmnaija.repository.ServiceDataService;
import org.motechproject.mmnaija.repository.SubscriptionDataService;
import org.motechproject.mmnaija.service.ScheduleService;
import org.motechproject.mmnaija.service.SubscriberService;
import org.motechproject.mmnaija.web.util.HTTPCommunicator;
import org.motechproject.mmnaija.web.util.MMConstants;
import org.motechproject.mmnaija.web.util.MMNaijaUtil;
import org.motechproject.mmnaija.web.util.MessageResponse;
import org.motechproject.mmnaija.web.util.SimpleMail;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.sms.SmsEventSubjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author seth
 */
@Controller
@RequestMapping("/web-api")
public class MMNaijaController {

    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private ServiceDataService messageService;
    @Autowired
    private SubscriptionDataService dataService;
    @Autowired
    private ServiceDataService serviceData;

    @Autowired
    MessageDataService msgDataService;
    @Autowired
    ScheduleService scheduleService;

    @Autowired
    MotechSchedulerService schedulerService;

    @RequestMapping("/status")
    @ResponseBody
    public String status() {

        HTTPCommunicator.sendSMS("233277143521", "OK Msg I just did", 2l);
        return MMConstants.MMNAIJA_OK;
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public ResponseEntity<String> sen(HttpServletRequest request) {

        try {
            List<String> recipient = new ArrayList<>();
            recipient.add("kwasett@gmail.com");
            new SimpleMail().send("Welcome Seh", "Scheduled Message Detail<br /><strong>MSISDN       :</strong> 2347010276732<br /><strong>Content Type :</strong> Voice<br/><strong>Content</strong> <br />-------------------------------------<br />1_FEEDING<br /><br />", "SAKwakwa", recipient);
        } catch (Exception e) {
            System.out.println("Eror : " + e.getLocalizedMessage());
        }
        return new ResponseEntity<String>("success", HttpStatus.OK);

    }

    @RequestMapping(value = "/due_msg", method = RequestMethod.GET)
    public ResponseEntity<String> senD(HttpServletRequest request) {

        try {
            String campaignKey = request.getParameter("campaign");
            String numbers = request.getParameter("msisdns");
            String[] telNums = numbers.split(",");
            MessageService msr = serviceData.findServiceBySkey(campaignKey);
            if (campaignKey.startsWith("mmnaija_") && (campaignKey.contains("SMS") || campaignKey.contains("IVR"))) {

                int cnt = 20;
                for (String externalId : telNums) {

                    String msgType = "sms";

                    String lang = "1";
                    String config = "mmnaijaSMS_";
                    Subscriber sub = subscriberService.findRecordByMsisdn(externalId);
                    if (null != sub) {
                        Subscription subscription = dataService.findRecordByEnrollmentService(externalId, msr.getContentId());
                        if (subscription.getCurrentPoint() >= msr.getMinEntryPoint() && subscription.getCurrentPoint() <= msr.getMaxEntryPoint()) {
                            if (campaignKey.toUpperCase().endsWith("IVR")) {
                                msgType = "voice";
                                lang = sub.getLanguage_id();
                                config = "mmnaijaIVR";
                            } else {
                                //SMS configuration in motech mmnaijaSMS_1 # for airtel
                                config += sub.getProvider();
                            }
                            System.out.println("Provider for  " + externalId + " - " + sub.getProvider());
                            cnt++;
                            Message msg = msgDataService.findByContentCurrentPositionLanguage(msr.getContentId(), lang, subscription.getCurrentPoint(), msgType);
//        Schedule sch = create(sub, msg, ScheduleStatus.SENT);
                            System.out.println("MSg  to send  : " + msg.getContent());
                            System.out.println("Content Type  : " + msg.getContentType());
                            System.out.println("MSISDN        : " + sub.getMsisdn());
                            System.out.println("Cnfig         : " + config);
                            System.out.println("\n====================================================\n");
                            subscription.setCurrentPoint(subscription.getCurrentPoint() + 1);
                            subscriberService.updateSubscription(subscription);

                            scheduleMessage(getMessage(msg.getContent(), sub.getMsisdn(), config), cnt);
//        scheduledRequest();
//        return sch;
//                scheduleService.playMessage(subscription, sub, msr, msgType);
                        } else {
                            if (subscription.getCurrentPoint() > msr.getMaxEntryPoint()) {
                                subscriberService.completeSubscribeUser(subscription);
                            } else {
                                subscription.setCurrentPoint(subscription.getCurrentPoint() + 1);
                                subscriberService.updateSubscription(subscription);
                            }
                        }
                    } else {
                        System.out.println("No SUbscriber Found ");
                    }
                }
            } else {
                System.out.println("Invalid MSG key " + campaignKey);
            }
        } catch (Exception e) {
            System.out.println("Eror : " + e.getLocalizedMessage());
        }
        return new ResponseEntity<String>("success", HttpStatus.OK);

    }

    @RequestMapping(value = "/reset", method = RequestMethod.GET)
    public ResponseEntity<String> reset(HttpServletRequest request) {
        System.out.println("Reset camapings");

        String[] userList = {"mmnaija_pregnancy_IVR", "mmnaija_pregnancy_SMS", "mmnaija_child_IVR", "mmnaija_child_SMS"};//"NYVRS SUNDAY IVR CAMPAIGN", "NYVRS KIKI IVR CAMPAIGN", "NYVRS RONALD IVR CAMPAIGN", "NYVRS RITA IVR CAMPAIGN", "NYVRS RITA SMS CAMPAIGN", "NYVRS KIKI SMS CAMPAIGN", "NYVRS RONALD SMS CAMPAIGN"};

        for (String string : userList) {
            try {
                System.out.println("Resetting  : " + string);
                subscriberService.deleteCampaign(string);
            } catch (Exception e) {
            }
        }
        try {
//               new CampaignAssignment().doAssignment();
        } catch (Exception e) {
            System.out.println("Assignment Error : " + e.getLocalizedMessage());;
            e.printStackTrace();
        }

        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/v1/subscriber/assign", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String assignSubscription() {
        List<Subscription> subscriptions = subscriberService.findByStatus("Active");
        int cnt = -0;
        for (Subscription subscribe : subscriptions) {
            try {
                cnt++;
                System.out.println("Subscribing  : " + cnt);
                Subscriber sub = subscriberService.findRecordByMsisdn(subscribe.getSubscriber());
                subscriberService.enrolUsersSubscribed(subscribe);
            } catch (Exception e) {
                System.out.println("Assign Subscription " + e.getLocalizedMessage());
            }
        }
        return MMConstants.MMNAIJA_OK;
    }

    @RequestMapping(value = "/v1/subscriber/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(HttpServletRequest request) {
        List<RegistrationRequest> reg = new ArrayList<>();
        try {

            String theString = IOUtils.toString(request.getInputStream(), "UTF-8");
            System.out.println("JSON REceived : " + theString);
            JSONArray ar = new JSONArray(theString);

            for (int i = 0; i < ar.length(); i++) {
                JSONObject json = ar.getJSONObject(i);
                reg.add(new RegistrationRequest(json.getString("msisdn"), json.getString("gender"), json.getString("language"), json.getString("service"), json.getString("start_point"), json.getString("age"), json.getString("pregnant"), json.getString("provider_id")));
            }
        } catch (Exception e) {
        }
//        if (result.hasErrors()) {
//            System.out.println("Error ::: " + result.toString());
//            return "Error";
//        }
//

        try {
            JSONObject json = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            for (RegistrationRequest regRequest1 : reg) {
                FormatedRegRequest regRequest = new FormatedRegRequest(regRequest1);
                Subscriber subscriber = subscriberService.findRecordByMsisdn(regRequest.getMsidn());
                System.out.println("Subscriber  register:" + subscriber);

                 MessageService contentSMS = messageService.findServiceByContentId((regRequest.getService() + 2));
                   MessageService content = messageService.findServiceByContentId((regRequest.getService()));

//        System.out.println("ID : "+subscriber.getMsisdn());
                //Check of subscriber is already registered
                if (null == subscriber) {
                    System.out.println("Subscripiton OK");
                    subscriber = subscriberService.createAndSubscribe(regRequest.getMsidn(), regRequest.getGender(), regRequest.getAge(), regRequest.getPregnant(), regRequest.getLanguage(), String.valueOf(regRequest.getService()), regRequest.getService(), regRequest.getProvider_id());

                         Subscription s = subscriberService.findActiveSubscription(subscriber, contentSMS);
                        if(null==s)
                            sendMSg(contentSMS, subscriber, s);//contentSMS,s);
                        
                    String responsive = (null == subscriber)
                            ? MMConstants.SUBSCRIBER_UNABLE_REGISTERD
                            : MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED;
                    JSONObject obj = new JSONObject();
                    obj.put("msisdn", regRequest.getMsidn());
                    obj.put("status", responsive);
//                  obj.put("statuserror", responsive);
                    jSONArray.put(obj);
                } else {
                    System.out.println("Subscripiton NOT OK");

                   
//                    MessageService contentSMS = messageService.findServiceByContentId((regRequest.getService() + 2));

//                    String response = HTTPCommunicator.sendSMS(subscriber.getMsisdn(), contentSMS., subscriber.getProvider());
                   
                    //Check for existing subscriptions
                    Subscription subscription = subscriberService.findSubscription(subscriber, contentSMS);
                    //not subscribed
                    if (null == subscription) {
                        //add subscription
                        subscriberService.subscribeUser(subscriber, String.valueOf(regRequest.getService()), regRequest.getStart_point());
                        Subscription s = subscriberService.findActiveSubscription(subscriber, contentSMS);
                        if(null==s)
                            sendMSg(contentSMS, subscriber, s);
//                        subscriberService.subscribeUser(subscriber, String.valueOf(regRequest.getService()), regRequest.getStart_point());
//                    return ()
//                            ? MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD)
//                            : MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED);

                    } else {
                        System.out.println("Subscripiton Already OK");

                        if (subscription.getStatus().equals(Status.Active)) {
                            //already subscribed
//                        return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIPTION_ALREADY_EXIST);
                        }
                        if (subscription.getStatus().equals(Status.Paused)) {
                            subscriberService.resumeSubscribeUser(subscription);
                          
                            sendMSg(contentSMS, subscriber, subscription);
//                        return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.RESUME_SUCCESSFUL);
                        } else if (subscription.getStatus().equals(Status.InActive)) {
                            subscriberService.reactivateSubscribe(subscription);
                             sendMSg(contentSMS, subscriber, subscription);
//                        return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.REACTIVATION_SUCCESSFUL);
                        } else {
//                        return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD);
                        }

                    }

                }
                System.out.println("Subscripiton FinalOK");
            }
            System.out.println("Subscripiton REq completed");
        } catch (Exception e) {
            System.out.println("Excception : " + e.getLocalizedMessage());
        }

        System.out.println("Before Return");
        if (reg.size() > 0) {
            return new ResponseEntity<String>(MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(MMNaijaUtil.getDefaultResponseMessage(true, "Empty Request"), HttpStatus.OK);
        }

    }

    @RequestMapping(value = "/v1/subscriber/update", method = RequestMethod.POST)
    public ResponseEntity<String> update(HttpServletRequest request) {
        List<RegistrationRequest> reg = new ArrayList<>();
        try {

            String theString = IOUtils.toString(request.getInputStream(), "UTF-8");
            System.out.println(new Date() + ") JSON Update REceived : " + theString);
            JSONArray ar = new JSONArray(theString);

            for (int i = 0; i < ar.length(); i++) {
                JSONObject json = ar.getJSONObject(i);
                reg.add(new RegistrationRequest(json.getString("msisdn"), json.getString("gender"), json.getString("language"), json.getString("service"), json.getString("start_point"), json.getString("age"), json.getString("pregnant"), json.getString("provider_id")));
            }
        } catch (Exception e) {
        }
//        if (result.hasErrors()) {
//            System.out.println("Error ::: " + result.toString());
//            return "Error";
//        }
//

        try {
            JSONObject json = new JSONObject();
            JSONArray jSONArray = new JSONArray();
            for (RegistrationRequest regRequest1 : reg) {
                FormatedRegRequest regRequest = new FormatedRegRequest(regRequest1);
                Subscriber subscriber = subscriberService.findRecordByMsisdn(regRequest.getMsidn());
                System.out.println("Subscriber  register:" + subscriber);

//        System.out.println("ID : "+subscriber.getMsisdn());
                //Check of subscriber is already registered
                if (null == subscriber) {
                    System.out.println("Subscripiton OK");
                    subscriber = subscriberService.createAndSubscribe(regRequest.getMsidn(), regRequest.getGender(), regRequest.getAge(), regRequest.getPregnant(), regRequest.getLanguage(), String.valueOf(regRequest.getService()), regRequest.getService(), regRequest.getProvider_id());

                    String responsive = (null == subscriber)
                            ? MMConstants.SUBSCRIBER_UNABLE_REGISTERD
                            : MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED;
                    JSONObject obj = new JSONObject();
                    obj.put("msisdn", regRequest.getMsidn());
                    obj.put("status", responsive);
//                  obj.put("statuserror", responsive);
                    jSONArray.put(obj);
                } else {
                    System.out.println("Subscripiton NOT OK");
                    subscriber.setAge(regRequest.getAge());
                    subscriber.setGender(regRequest.getGender());
                    subscriber.setLanguage_id(regRequest.getLanguage());
                    subscriber.setPregnant(regRequest.getPregnant());
                    subscriberService.updateSubscriber(subscriber);
                }
            }
        } catch (Exception e) {
            System.out.println("Excception : " + e.getLocalizedMessage());
        }
        System.out.println("Before Return");
        if (reg.size() > 0) {
            return new ResponseEntity<String>(MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_EDITED), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(MMNaijaUtil.getDefaultResponseMessage(true, "Empty Request"), HttpStatus.OK);
        }

    }

    /**
     *
     * @param msisdn
     * @param age
     * @param maleOrFemale d * @param language
     * @param preg
     * @param campaign
     * @param start
     * @param current
     * @return
     */
    @RequestMapping(value = "/v1/subscriber/sregister", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String registerSubscriber(
            @RequestParam(value = "msisdn") String msisdn,
            @RequestParam(value = "age") int age,
            @RequestParam(value = "gender") String maleOrFemale,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "pregnant") int preg,
            @RequestParam(value = "service") String campaign,
            @RequestParam(value = "start_point") int start,
            @RequestParam(value = "provider") Long provider) {
///msisdn=233277143521&age=28&gender=m&language=en&pregnant=1&service=1&start_point=1
        System.out.println(String.format("Registration String "
                + "msisdn=%s&age=%s&gender=%s&language=%s&pregnant=%s&serive=%s&start_point=%s", msisdn,
                age, maleOrFemale, language, preg, campaign, start));
        Subscriber subscriber = subscriberService.findRecordByMsisdn(msisdn);
        System.out.println("Subscriber :" + subscriber);
//        System.out.println("ID : "+subscriber.getMsisdn());
        //Check of subscriber is already registered          
        MessageService content = messageService.findServiceByContentId(Integer.parseInt(campaign));

        if (null == subscriber) {
            subscriber = subscriberService.createAndSubscribe(msisdn, (maleOrFemale.equalsIgnoreCase("m")) ? 1 : 2, age, preg, language, campaign, start, provider);

            Subscription subscription = subscriberService.findSubscription(subscriber, content);
            if (null == subscriber) {
                return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD);
            } else {

                Subscription subscription1 = subscriberService.findSubscription(subscriber, content);
                sendMSg(content, subscriber, subscription1);
                return MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED);
            }
        } else {
            Subscription subscription = subscriberService.findSubscription(subscriber, content);
            //not subscribed
            if (null == subscription) {
                //add subscription
                if (subscriberService.subscribeUser(subscriber, campaign, start)) {
                    
                
                return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD);
            } else {

                Subscription subscription1 = subscriberService.findSubscription(subscriber, content);
                sendMSg(content, subscriber, subscription1);
                return MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED);
            }
//                    
//                     ? MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD)
//                            : MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED);
//                }

            } else {
                if (subscription.getStatus().equals(Status.Active)) {
                    //already subscribed
                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIPTION_ALREADY_EXIST);
                }
                if (subscription.getStatus().equals(Status.Paused)) {
                    subscriberService.resumeSubscribeUser(subscription);
                    //SMS configuration in motech mmnaijaSMS_1 # for airtel
//                    Message msg = msgDataService.findByContentCurrentPositionLanguage(content.getContentId(), "1", subscription.getCurrentPoint(), "sms");
                    sendMSg(content, subscriber, subscription);
                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.RESUME_SUCCESSFUL);
                } else if (subscription.getStatus().equals(Status.InActive)) {
                    subscriberService.reactivateSubscribe(subscription);
                     sendMSg(content, subscriber, subscription);
                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.REACTIVATION_SUCCESSFUL);
                } else {
                    
                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD);
                }

            }
        }

    }

    public void sendMSg(MessageService content, Subscriber subscriber, Subscription sub) {
        Message msg = msgDataService.findByContentCurrentPositionLanguage(content.getContentId(), "1", sub.getCurrentPoint(), "sms");

        Schedule s = null;
        try {
            s = scheduleService.create(sub, msg, ScheduleStatus.PROCESSING, "");
        } catch (Exception e) {
        }

       
        try {

             String response = HTTPCommunicator.sendSMS(subscriber.getMsisdn(), msg.getContent(), subscriber.getProvider());

        System.out.println("===============================================");
//                System.out.println("Config   : " + config);
        System.out.println("Message  : " + msg.getContent());
        System.out.println("Recipient: " + subscriber.getMsisdn());
        System.out.println("================================================");
            ScheduleStatus st = ScheduleStatus.FAILED;
            if (response.isEmpty()) {

            } else {
                if (msg.getContentType().equalsIgnoreCase("sms")) {
                    if (response.startsWith("OK")) {
                        st = ScheduleStatus.DELIVERED;
                    }
                } else {
                    if (response.startsWith("1")) {
                        st = ScheduleStatus.DELIVERED;
                    }
                }

            }

            if (null != s) {
                s.setScheduleResponse(response);
                s.setStatus(st);
                scheduleService.update(s);
            }
        } catch (Exception e) {
        }
    }

    @RequestMapping(value = "/v1/subscriber/isregistered", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String checkRegistration(HttpServletRequest request) {
        String msisdn = request.getParameter("msisdn");
//        String service = request.getParameter("service");

        List<Subscription> subscriptions = subscriberService.findByStatusByUser(msisdn);
        if (subscriptions.size() > 0) {
            return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.ALREADY_SUBSCRIBED);
        } else {
            return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.NOT_SUBSCRIBED);
        }

    }

    /**
     * For nsubscription users
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/v1/subscriber/unsubscribe", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String unSubscribe(HttpServletRequest request) {
        String msisdn = "";
//        String service = request.getParameter("service");

        try {

            String theString = IOUtils.toString(request.getInputStream(), "UTF-8");
            System.out.println(new Date() + ") JSON Update unsubscribe : " + theString);
            JSONArray ar = new JSONArray(theString);

            for (int i = 0; i < ar.length(); i++) {
                JSONObject json = ar.getJSONObject(i);
                msisdn = json.getString("msisdn");
                System.out.println("Unsubscribing : " + msisdn);

                List<Subscription> subscriptions = subscriberService.findByStatusByUser(msisdn);
                for (Subscription subscription : subscriptions) {
                    if (subscriberService.unsubscribeUser(subscription)) {
                        MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.UNSUBSCRIPTION_SUCCESSFUL);
                    } else {

                    }
                }
            }

        } catch (Exception e) {
        }

        return MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.UNSUBSCRIPTION_SUCCESSFUL);
    }

    @RequestMapping(value = "/v1/subscriber/pause", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String pauseSubscription(HttpServletRequest request) {
        String msisdn = request.getParameter("msisdn");
//        String service = request.getParameter("service");
        System.out.println("MSISDN  : " + msisdn);
        List<Subscription> subscriptions = subscriberService.findByStatusByUser(msisdn);
        for (Subscription subscription : subscriptions) {
            if (subscriberService.pauseSubscribeUser(subscription)) {
                return MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.PAUSE_SUCCESSFUL);
            } else {

            }
        }
        return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.PAUSE_FAILED);

    }

    @RequestMapping(value = "/v1/subscriber/resume", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String resumeSubscription(HttpServletRequest request) {
        String msisdn = request.getParameter("msisdn");
//        String service = request.getParameter("service");

//        String service = request.getParameter("service");
        System.out.println("MSISDN  : " + msisdn);
        List<Subscription> subscriptions = subscriberService.findByStatusByUser(msisdn);
        for (Subscription subscription : subscriptions) {
            if (subscriberService.resumeSubscribeUser(subscription)) {
                return MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.RESUME_SUCCESSFUL);
            } else {
                return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.RESUME_FAILED);
            }
        }

        return MMNaijaUtil.getDefaultResponseMessage(true, "Not paused to Resume");

    }

    public MessageResponse checkSubscription(String msisdn, String service) {
        Subscriber subscriber = subscriberService.findRecordByMsisdn(msisdn);

        MessageResponse msgResponse = new MessageResponse();
        //check if subscriber exist
        if (null == subscriber) {
            return new MessageResponse(MMConstants.SUBSCRIBER_NOT_FOUND_EXCEPTION, null);
        } else {
            System.out.println("Msg Service Start");
            //checking service
            MessageService msgService = messageService.findServiceByContentId(Integer.parseInt(service));
            System.out.println("Msg Service : " + msgService.getName());
            if (null == msgService) {
                return new MessageResponse(MMConstants.SERVICE_NOT_FOUND_EXCEPTION, null);
//                return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SERVICE_NOT_FOUND_EXCEPTION);

            } else {
                //Checking subscription
                Subscription subscriptions = subscriberService.findSubscription(subscriber, msgService);
                if (null == subscriptions) {
                    return new MessageResponse(MMConstants.SUBSCRIBER_NOT_FOUND_EXCEPTION, null);
//                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIPTION_NOT_FOUND_EXCEPTION);
                } else {
                    return new MessageResponse("00", subscriptions);
                }
            }

        }
    }

    public MessageResponse checkSubscription(String msisdn) {
        Subscriber subscriber = subscriberService.findRecordByMsisdn(msisdn);

        MessageResponse msgResponse = new MessageResponse();
        //check if subscriber exist
        if (null == subscriber) {
            return new MessageResponse(MMConstants.SUBSCRIBER_NOT_FOUND_EXCEPTION, null);
        } else {
            System.out.println("Msg Service Start");
            List<Subscription> subscriptions = subscriberService.findByStatusByUser(msisdn);
            //checking service
            MessageService msgService = messageService.findServiceByContentId(subscriptions.get(0).getService());
            System.out.println("Msg Service : " + msgService.getName());
            if (null == msgService) {
                return new MessageResponse(MMConstants.SERVICE_NOT_FOUND_EXCEPTION, null);
//                return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SERVICE_NOT_FOUND_EXCEPTION);

            } else {
                //Checking subscription
                Subscription subscription = subscriptions.get(0);
                if (null == subscriptions) {
                    return new MessageResponse(MMConstants.SUBSCRIBER_NOT_FOUND_EXCEPTION, null);
//                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIPTION_NOT_FOUND_EXCEPTION);
                } else {
                    return new MessageResponse("00", subscription);
                }
            }

        }
    }

    @RequestMapping("/mmnaija")
    @ResponseBody
    public String hiMMNaija() {
        return "MM Naija dey run";
    }

    @RequestMapping(value = "/v1/subscriber/sendmsg", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String simulateEventhandler(HttpServletRequest request) {
        //?MessageKey=IVR_1&CampaignName=mmnaija_2nd_IVR&ExternalID=233244126634&JobID=142536
        String msgKey = request.getParameter(EventKeys.MESSAGE_KEY);
        String campaignKey = request.getParameter(EventKeys.CAMPAIGN_NAME_KEY);
        String externalId = request.getParameter(EventKeys.EXTERNAL_ID_KEY);
        String jobId = request.getParameter(EventKeys.SCHEDULE_JOB_ID_KEY);
        System.out.println(String.format("message Details  msgkey %s externalId %s", msgKey, externalId));
        if (MMNaijaUtil.validateMMNaijaMsgKey(campaignKey)) {

            MessageService msr = serviceData.findServiceBySkey(campaignKey);
            System.out.println("Msg Found  : " + msr.getContentId());
            Subscription subscription = dataService.findRecordByEnrollmentService(externalId, msr.getContentId());

            scheduleService.playMessage(subscription, msgKey);
            return MMNaijaUtil.getDefaultResponseMessage(false, "msg handled at " + msgKey);
        } else {
            return MMNaijaUtil.getDefaultResponseMessage(false, "Not HandledHer :" + campaignKey);
        }

//        return MMNaijaUtil.getDefaultResponseMessage(false, msgKey);
    }

    public MotechEvent getMessage(String message, String recipient, String config) {
        Map<String, Object> params = new HashMap();
        params.put("message", message);
        List<String> recipients = new ArrayList<String>();
        recipients.add(recipient);
        params.put("recipients", recipients);
        params.put("config", config);

        return new MotechEvent(SmsEventSubjects.SEND_SMS, params);
//        return new MotechEvent("send_SMS_now", params);
    }

    public void scheduleMessage(MotechEvent motechEvent, int sec) {

        Calendar calendar = Calendar.getInstance();
        Date sendTime = calendar.getTime();
        calendar.add(Calendar.SECOND, (60 + sec));
        sendTime = calendar.getTime();
        //Now let' create our job
        RunOnceSchedulableJob job = new RunOnceSchedulableJob(motechEvent, sendTime);

        schedulerService.safeScheduleRunOnceJob(job);
    }

}
