/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.web;

import javax.servlet.http.HttpServletRequest;
import org.motechproject.messagecampaign.EventKeys;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Status;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.domain.Subscription;
import org.motechproject.mmnaija.repository.ServiceDataService;
import org.motechproject.mmnaija.repository.SubscriptionDataService;
import org.motechproject.mmnaija.service.ScheduleService;
import org.motechproject.mmnaija.service.SubscriberService;
import org.motechproject.mmnaija.web.util.MMConstants;
import org.motechproject.mmnaija.web.util.MMNaijaUtil;
import org.motechproject.mmnaija.web.util.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    ScheduleService scheduleService;
    @RequestMapping("/status")
    @ResponseBody
    public String status() {
        return MMConstants.MMNAIJA_OK;
    }

    /**
     *
     * @param msisdn
     * @param age
     * @param maleOrFemale
     * @param language
     * @param preg
     * @param campaign
     * @param start
     * @param current
     * @return
     */
    @RequestMapping(value = "/v1/subscriber/register", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String registerSubscriber(
            @RequestParam(value = "msisdn") String msisdn,
            @RequestParam(value = "age") int age,
            @RequestParam(value = "gender") String maleOrFemale,
            @RequestParam(value = "language") String language,
            @RequestParam(value = "pregnant") int preg,
            @RequestParam(value = "service") String campaign,
            @RequestParam(value = "start_point") int start) {
///msisdn=233277143521&age=28&gender=m&language=en&pregnant=1&service=1&start_point=1
        Subscriber subscriber = subscriberService.findRecordByMsisdn(msisdn);
        System.out.println("Subscriber :" + subscriber);
//        System.out.println("ID : "+subscriber.getMsisdn());
        //Check of subscriber is already registered
        if (null == subscriber) {
            subscriber = subscriberService.createAndSubscribe(msisdn, (maleOrFemale.equalsIgnoreCase("m")) ? 1 : 2, age, preg, language, campaign, start);

            return (null == subscriber)
                    ? MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD)
                    : MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED);
        } else {
            MessageService content = messageService.findServiceByContentId(Integer.parseInt(campaign));
            Subscription subscription = subscriberService.findSubscription(subscriber, content);
            //not subscribed
            if (null == subscription) {
                //add subscription
                return (subscriberService.subscribeUser(subscriber, campaign, start))
                        ? MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD)
                        : MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.SUBSCRIBER_SUCCESSFULLY_REGISTERED);

            } else {
                if (subscription.getStatus().equals(Status.Active)) {
                    //already subscribed
                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIPTION_ALREADY_EXIST);
                }
                if (subscription.getStatus().equals(Status.Paused)) {
                    subscriberService.resumeSubscribeUser(subscription);
                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.RESUME_SUCCESSFUL);

                } else if (subscription.getStatus().equals(Status.InActive)) {
                    subscriberService.reactivateSubscribe(subscription);
                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.REACTIVATION_SUCCESSFUL);

                } else {
                    return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.SUBSCRIBER_UNABLE_REGISTERD);
                }

            }
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
        String msisdn = request.getParameter("msisdn");
        String service = request.getParameter("service");

        MessageResponse subscriptionResult = checkSubscription(msisdn, service);
        if (subscriptionResult.getMsg().equalsIgnoreCase("00")) {
            if (subscriberService.unsubscribeUser(subscriptionResult.getSubscriptions())) {
                return MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.UNSUBSCRIPTION_SUCCESSFUL);
            } else {
                return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.UNSUBSCRIPTION_SUCCESSFUL);
            }
        } else {
            return MMNaijaUtil.getDefaultResponseMessage(true, subscriptionResult.getMsg());
        }
    }

    @RequestMapping(value = "/v1/subscriber/pause", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String pauseSubscription(HttpServletRequest request) {
        String msisdn = request.getParameter("msisdn");
        String service = request.getParameter("service");
        System.out.println("MSISDN  : " + msisdn);
        System.out.println("service  : " + service);
        MessageResponse subscriptionResult = checkSubscription(msisdn, service);
        System.out.println("After check :" + subscriptionResult);
        if (subscriptionResult.getMsg().equalsIgnoreCase("00")) {
//            System.out.println("Paues : "+subscriptionResult);
            if (subscriberService.pauseSubscribeUser(subscriptionResult.getSubscriptions())) {
                return MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.PAUSE_SUCCESSFUL);
            } else {
                return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.PAUSE_FAILED);
            }
        } else {
            return MMNaijaUtil.getDefaultResponseMessage(true, subscriptionResult.getMsg());
        }
    }

    @RequestMapping(value = "/v1/subscriber/resume", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String resumeSubscription(HttpServletRequest request) {
        String msisdn = request.getParameter("msisdn");
        String service = request.getParameter("service");

        MessageResponse subscriptionResult = checkSubscription(msisdn, service);
        if (subscriptionResult.getMsg().equalsIgnoreCase("00")) {
            if (subscriberService.resumeSubscribeUser(subscriptionResult.getSubscriptions())) {
                return MMNaijaUtil.getDefaultResponseMessage(false, MMConstants.RESUME_SUCCESSFUL);
            } else {
                return MMNaijaUtil.getDefaultResponseMessage(true, MMConstants.RESUME_FAILED);
            }
        } else {
            return MMNaijaUtil.getDefaultResponseMessage(true, subscriptionResult.getMsg());
        }
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
            System.out.println("Msg Found  : "+msr.getContentId());
            Subscription subscription = dataService.findRecordByEnrollmentService(externalId, msr.getContentId());

            scheduleService.playMessage(subscription, msgKey);
        return   MMNaijaUtil.getDefaultResponseMessage(false, "msg handled at "+msgKey);
        } else {
           return MMNaijaUtil.getDefaultResponseMessage(false, "Not HandledHer :" + campaignKey);
        }

//        return MMNaijaUtil.getDefaultResponseMessage(false, msgKey);
    }

}
