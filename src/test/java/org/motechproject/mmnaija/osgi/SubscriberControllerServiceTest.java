/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.osgi;

import java.util.Date;
import javax.inject.Inject;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Status;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.service.SubscriberControllerService;
import org.motechproject.mmnaija.service.SubscriberService;

/**
 *
 * @author seth
 */
public class SubscriberControllerServiceIT {

    @Inject
    SubscriberService subscriberService;

    @Inject
    SubscriberControllerService subscriberControllerService;

    public SubscriberControllerServiceIT() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testSubscriptionController() {
        Subscriber subscriber = subscriberService.createAndSubscribe("233201063177", 1, 22, 1, "en", "pregnancy", 5);

        boolean expected = subscriberControllerService.addSubscription(subscriber, "1", 5);
        assertEquals(expected, true);

        MessageService service = new MessageService(1, "pregnancy", "pregnancy", "sms", 1, 242, new Date(), 1, "1 day", Status.Active);
        int currentPosition = 0;
        int expResult = 5;
        int result = subscriberControllerService.getSMSStartPoint(service, currentPosition);
        assertEquals(expResult, result);

        expected = false;
        boolean unsubscribeResult = subscriberControllerService.unSubscribe(subscriber.getMsisdn(), "1");
        assertEquals(expected, unsubscribeResult);

    }

}
