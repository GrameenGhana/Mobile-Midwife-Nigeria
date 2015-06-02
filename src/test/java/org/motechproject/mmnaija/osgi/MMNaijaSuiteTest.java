/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.motechproject.mmnaija.osgi;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author seth
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({SubscriberControllerServiceTest.class,
    SubscriberServiceTest.class,
    MMNaijaControllerTest.class})
public class MMNaijaSuiteTest {

    public static final int BUNDLE_MS_WAIT_TIME = 1000;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

}
