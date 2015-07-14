/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.osgi;

import java.io.IOException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.methods.HttpGet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class MMNaijaControllerTest extends BasePaxIT {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    public MMNaijaControllerTest() {
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

    @After
    public void tearDown() {
    }

    @Test
    public void testStatusGetRequest() throws IOException, InterruptedException {
//        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/modules/mmnaija/web-api/status",
//                TestContext.getJettyPort()));
//        addAuthHeader(httpGet, ADMIN_USERNAME, ADMIN_PASSWORD);
//
//        HttpResponse response = getHttpClient().execute(httpGet);
//        assertNotNull(response);
//
//        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testRunningGetRequest() throws IOException, InterruptedException {
//        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/modules/mmnaija/web-api/mmnaija",
//                TestContext.getJettyPort()));
//        addAuthHeader(httpGet, ADMIN_USERNAME, ADMIN_PASSWORD);
//
//        HttpResponse response = getHttpClient().execute(httpGet);
//        assertNotNull(response);
//
//        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testRegisterGetRequest() throws IOException, InterruptedException {
//        String msisdn = "23320101010";
//        String gender = "Male";
//        int age = 23;
//        String language = "en";
//        int pregnant = 1;
//        int service = 1;
//        int startPoint = 5;
//        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/modules/mmnaija/web-api/register?"
//                + "msisdn=%s&age=%d&gender=%s&language=%s&pregnant=%d&service=%d&start_point=%d",
//                TestContext.getJettyPort(), msisdn, age, gender, language, pregnant, service, startPoint));
//        addAuthHeader(httpGet, ADMIN_USERNAME, ADMIN_PASSWORD);
//
//        HttpResponse response = getHttpClient().execute(httpGet);
//        assertNotNull(response);
//
//        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    public void testUnsbcriptionGetRequest() throws IOException, InterruptedException {
//        String msisdn = "23320101010";
//
//        int service = 1;
//        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/modules/mmnaija/web-api/v1/subscriber/unsubscribe?"
//                + "msisdn=%s&service=%d",
//                TestContext.getJettyPort(), msisdn, service));
//        addAuthHeader(httpGet, ADMIN_USERNAME, ADMIN_PASSWORD);
//
//        HttpResponse response = getHttpClient().execute(httpGet);
//        assertNotNull(response);
//
//        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    private void addAuthHeader(HttpGet httpGet, String userName, String password) {
        httpGet.addHeader("Authorization",
                "Basic " + new String(Base64.encodeBase64((userName + ":" + password).getBytes())));
    }

}
