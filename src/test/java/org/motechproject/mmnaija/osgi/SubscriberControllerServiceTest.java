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
import org.junit.runner.RunWith;
import org.motechproject.mmnaija.domain.Language;
import org.motechproject.mmnaija.domain.MessageService;
import org.motechproject.mmnaija.domain.Status;
import org.motechproject.mmnaija.domain.Subscriber;
import org.motechproject.mmnaija.service.SubscriberControllerService;
import org.motechproject.mmnaija.service.SubscriberService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

/**
 *
 * @author seth
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class SubscriberControllerServiceTest  extends BasePaxIT {

    @Inject
    SubscriberService subscriberService;

    @Inject
    SubscriberControllerService subscriberControllerService;

    public SubscriberControllerServiceTest() {
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
        boolean expected = false;
        Subscriber subscriber = new Subscriber("23320202020", 1, 1, 1, new Language("English", "en"));
//        boolean expected = subscriberControllerService.addSubscription(subscriber, "1", 5);
//        assertEquals(expected, true);

        MessageService service = new MessageService(Integer.parseInt("1"), "pregnancy", "pregnancy", "sms", Integer.parseInt("1"), Integer.parseInt("242"), "+1 Week", 1, "1 day", Status.Active);
        service.setContentId(1);
        service.setMinEntryPoint(1);
        service.setMaxEntryPoint(242);
                
        System.out.println("Service contentid :"+service.getContentId());
        System.out.println("UUu : "+service.getMaxEntryPoint());
        int currentPosition = 5;
        int expResult = 1;
        int result = subscriberControllerService.getSMSStartPoint(service, currentPosition);
        assertEquals(expResult, result);

        expected = false;
        boolean unsubscribeResult = subscriberControllerService.unSubscribe(subscriber.getMsisdn(), "1");
        assertEquals(expected, unsubscribeResult);

    }

}
