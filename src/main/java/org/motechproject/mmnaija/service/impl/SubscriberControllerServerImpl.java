/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.LocalDate;
import org.motechproject.event.MotechEvent;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollmentStatus;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.mmnaija.domain.Message;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Status;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.MessageDataService;
import org.motechproject.mmnaija.repository.ServiceDataService;
import org.motechproject.mmnaija.repository.SubscriptionDataService;
import org.motechproject.mmnaija.service.SubscriberControllerService;
import org.motechproject.mmnaija.service.SubscriberService;
import org.motechproject.mmnaija.web.util.HTTPCommunicator;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.sms.SmsEventSubjects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author seth
 */
@Service("subscriberControllerServer")
public class SubscriberControllerServerImpl implements SubscriberControllerService {

    @Autowired
    CampaignEnrollmentDataService enrollmentService;

    @Autowired
    ServiceDataService serviceDataService;

    @Autowired
    MessageCampaignService messageCampaignService;
    @Autowired
    SubscriptionDataService subscriptionDataService;
    public static String Campaign_Nam = "";
//    SMSService smsservice;
    int NUMBER_OF_SMS_PER_WEEK = 5;

    @Autowired
    SubscriberService subscriberService;
    @Autowired
    MotechSchedulerService schedulerService;

    @Autowired
    MessageDataService messageDataService;

    @Override
    public boolean enrolSubscription(Subscription s) {
        System.out.println("status  : " + s.getSubscriber());
        if (s.getStatus().equals("Active")) {

            org.motechproject.mmnaija.domain.MessageService service = serviceDataService.findServiceByContentId((s.getService()));
            System.out.println("Service Found " + service.getName());
            System.out.println("Enrolment Statred : " + new Date().getTime());

            CampaignRequest request = new CampaignRequest(s.getSubscriber(), service.getSkey(), new LocalDate(), null);

            messageCampaignService.enroll(request);

            System.out.println("Enrolment Done : " + new Date().getTime());
            CampaignEnrollment enrolment = enrollmentService.findByExternalIdAndCampaignName(s.getSubscriber(), service.getSkey());
            if (null != enrolment) {
                s.setEnrollment(String.valueOf(enrolment.getId()));
                subscriptionDataService.update(s);
            }
            System.out.println("Enrolment Search UpdateEnrolment : " + new Date().getTime());

        }
        return true;
    }

    @Override
    public boolean addSubscription(Subscriber sub, String campaign, int start, String status) {
        org.motechproject.mmnaija.domain.MessageService service = serviceDataService.findServiceByContentId(Integer.parseInt(campaign));
        System.out.println("Campaign to enrol : " + service.getName());

        CampaignRequest request = new CampaignRequest(String.valueOf(sub.getMsisdn()), service.getSkey(), new LocalDate(), null);
//        CampaignEnrollment enr = new CampaignEnrollment(String.valueOf(sub.getMsisdn()), service.getSkey());
//
//        enr.setReferenceDate(new LocalDate());
        messageCampaignService.enroll(request);

        CampaignEnrollment enrolment = enrollmentService.findByExternalIdAndCampaignName(String.valueOf(sub.getMsisdn()), service.getSkey());
        if (null != enrolment) {
            System.out.println("Start Point");
            if (service.getMinEntryPoint() > start) {
                start = service.getMinEntryPoint();
            } else if (start > service.getMaxEntryPoint()) {
                start = service.getMaxEntryPoint();
            }
//            start = getSMSStartPoint(service, start);
            System.out.println("End start point : " + start);
            Subscription subscription = new Subscription(sub, service, start, start, status, new Date(), enrolment);
            subscriptionDataService.create(subscription);
//          new Subscription(sub, service, start, currentPoint, status, new Date(), enrolment), status, null, enrolment);
            //return true;
        }
        service = serviceDataService.findServiceByContentId(Integer.parseInt(campaign) + 2); //For the sms messages  if campaign is either a 1 or 2  +2 gives sms messages with id 3 or 4
        System.out.println("Campaign to enrol : " + service.getName());

        request = new CampaignRequest(String.valueOf(sub.getMsisdn()), service.getSkey(), new LocalDate(), null);
//        CampaignEnrollment enr = new CampaignEnrollment(String.valueOf(sub.getMsisdn()), service.getSkey());
//
//        enr.setReferenceDate(new LocalDate());
        messageCampaignService.enroll(request);

        enrolment = enrollmentService.findByExternalIdAndCampaignName(String.valueOf(sub.getMsisdn()), service.getSkey());
        if (null != enrolment) {
            System.out.println("Start Point");
            start = start * NUMBER_OF_SMS_PER_WEEK;
            System.out.println("End start point : " + start);
            Subscription subscription = new Subscription(sub, service, start, start, status, new Date(), enrolment);
            subscriptionDataService.create(subscription);
//          new Subscription(sub, service, start, currentPoint, status, new Date(), enrolment), status, null, enrolment);
            return true;
        }

        return false;
    }

    public void deleteCampaign(String campaign) {
        messageCampaignService.deleteCampaign(campaign);
    }

    public CampaignEnrollment enrollSubscriber(Subscription subscription, String campaign, Date refDate) {
//        CampaignEnrollment enr = new CampaignEnrollment(String.valueOf(subscription.getSubscriber()), campaign);
//
//        enr.setReferenceDate(new LocalDate(refDate));
        CampaignRequest request = new CampaignRequest(subscription.getSubscriber(), campaign, new LocalDate(refDate), null);
        messageCampaignService.enroll(request);
        CampaignEnrollment enr = enrollmentService.findByExternalIdAndCampaignName(subscription.getSubscriber(), campaign);
        return enr;
    }

    @Override
    public int getSMSStartPoint(org.motechproject.mmnaija.domain.MessageService service, int startService) {
        System.out.println("From :" + service.getContentId());
//        int startService=0;
        if (service.getContentId() == 4) {
            startService = (5 * startService) - 4;// taking into account SMS goes 5 times a week.
        } else {
            startService = (5 * (startService - 4)) - 4;// taking into account SMS goes 5 times a week and pregnancy content starts at week 5.
        }
        System.out.println("Serive :" + service.getMaxEntryPoint());
        System.out.println("Serive 3:" + service.getMinEntryPoint());

        int smsStartPoint = (startService < service.getMinEntryPoint()) ? service.getMinEntryPoint() : startService;
        smsStartPoint = (startService > service.getMaxEntryPoint()) ? service.getMaxEntryPoint() : startService;

        return smsStartPoint;

    }

    @Override
    public boolean unSubscribe(String msisdn, String service) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Subscriber updateSubscription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addSubscription(Subscriber sub, String campaign, int start) {
        //Set default status to active
        return addSubscription(sub, campaign, start, Status.Active.toString());
    }

    @Override
    public boolean unSubscribe(Subscription subscription) {
        if (!subscription.getStatus().equalsIgnoreCase(Status.Active.toString())) {
            //Already unsubsribed
            return false;
        }
        MessageService messageSer = serviceDataService.findServiceByContentId(subscription.getService());
        subscription.setEndDate(new Date());
        subscription.setStatus(Status.InActive.toString());
//        CampaignEnrollment enroll = enrollmentService.findById(Long.parseLong(subscription.getEnrollment()));

        messageCampaignService.unenroll(subscription.getSubscriber(), messageSer.getSkey());
//        enrollmentService.delete(enroll);
        subscriptionDataService.update(subscription);
        return true;
    }

    public boolean pauseSubscription(Subscription subscription) {
        if (!subscription.getStatus().equalsIgnoreCase(Status.Active.toString())) {
            //Already unsubsribed
            return false;
        }

        MessageService messageSer = serviceDataService.findServiceByContentId(subscription.getService());
//        subscription.setEndDate(new Date());
        subscription.setStatus(Status.Paused.toString());
        System.out.println("Subscriotion : " + subscription.getSubscriber());
        System.out.println("Msg : " + messageSer.getSkey());
        CampaignEnrollment enroll = enrollmentService.findById(Long.parseLong(subscription.getEnrollment()));
        enroll.setStatus(CampaignEnrollmentStatus.INACTIVE);

        messageCampaignService.unenroll(subscription.getSubscriber(), messageSer.getSkey());
//        enrollmentService.update(enroll);
        subscriptionDataService.update(subscription);
        return true;

    }

    public boolean resumeSubscription(Subscription subscription) {
        if (!subscription.getStatus().equalsIgnoreCase(Status.Paused.toString())) {
            //Already unsubsribed
            return false;
        }
        MessageService messageSer = serviceDataService.findServiceByContentId(subscription.getService());
        subscription.setStatus(Status.Active.toString());

        CampaignEnrollment enroll = enrollmentService.findById(Long.parseLong(subscription.getEnrollment()));
        enroll.setStatus(CampaignEnrollmentStatus.ACTIVE);
        enrolSubscription(subscription);
//        enrollmentService.update(enroll);
        subscriptionDataService.update(subscription);
        return true;
    }

    public boolean completeSubscription(Subscription subscription) {
        if (!subscription.getStatus().equalsIgnoreCase(Status.Active.toString())) {
            //Already unsubsribed
            return false;
        }

        MessageService messageSer = serviceDataService.findServiceByContentId(subscription.getService());
//        subscription.setEndDate(new Date());
        subscription.setStatus(Status.Completed.toString());
        CampaignEnrollment enroll = enrollmentService.findById(Long.parseLong(subscription.getEnrollment()));
        enroll.setStatus(CampaignEnrollmentStatus.COMPLETED);
        messageCampaignService.campaignCompleted(subscription.getSubscriber(), messageSer.getSkey());
//        enrollmentService.update(enroll);
        subscriptionDataService.update(subscription);
        return true;

    }

    @Override
    public boolean reactivateSubscription(Subscription subscription) {

        MessageService messageSer = serviceDataService.findServiceByContentId(subscription.getService());

        return null != enrollSubscriber(subscription, messageSer.getSkey(), new Date());

    }

    public MotechEvent getMessage(String message, String recipient, String config) {
        Map<String, Object> params = new HashMap();
        params.put("message", message);
        params.put("recipient", recipient);
        params.put("config", config);

        return new MotechEvent(SmsEventSubjects.SEND_SMS, params);
//        return new MotechEvent("send_SMS_now", params);
    }

    public void subcriptionMessages(Subscriber subscriber, Subscription subscription) {
        MotechEvent motechEvent = null;
        Message msg = null;
        String config = "mmnaijaSMS";
        boolean isSMS = false;
        //IVR Schedule for evening
        if (subscription.getService() < 3) {
            config = "mmnaijaIVR" ;//+ subscriber.getProvider();
            msg = messageDataService.findByContentCurrentPositionLanguage(subscription.getService(), subscriber.getLanguage_id(), subscription.getCurrentPoint(), "voice");
//            HTTPCommunicator.sendSMS(subscriber.getMsisdn(), msg.getContent(), subscriber.getProvider());
        } else {
            isSMS = true;
            config+="_"+subscriber.getProvider();
            msg = messageDataService.findByContentCurrentPositionLanguage(subscription.getService(), "1", subscription.getCurrentPoint(), "sms");
//             HTTPCommunicator.sendSMS(subscriber.getMsisdn(), msg.getContent(), subscriber.getProvider());
     
        }
        
        motechEvent = getMessage(msg.getContent(), subscriber.getMsisdn(), config);
        scheduleMessage(motechEvent, isSMS);
        subscription.setCurrentPoint(subscription.getCurrentPoint() + 1);
        subscriberService.updateSubscription(subscription);
    }

    public void scheduleMessage(MotechEvent motechEvent, boolean isSMS) {

        Calendar calendar = Calendar.getInstance();
        if (isSMS) {
            if (calendar.get(Calendar.HOUR_OF_DAY) < 13) {
                calendar.set(Calendar.HOUR_OF_DAY, 13);
            } else {
                calendar.add(Calendar.MINUTE, 4);
            }
        } else {
            calendar.add(Calendar.MINUTE, 2);
        }
        Date sendTime = calendar.getTime();

        //Now let' create our job
        RunOnceSchedulableJob job = new RunOnceSchedulableJob(motechEvent, sendTime);

        schedulerService.safeScheduleRunOnceJob(job);
    }

}
