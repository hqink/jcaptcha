/*
 * JCaptcha, the open source java framework for captcha definition and integration
 * Copyright (c)  2007 jcaptcha.net. All Rights Reserved.
 * See the LICENSE.txt file distributed with this package.
 */

package com.octo.captcha.service.captchastore;

import com.octo.captcha.Captcha;
import com.octo.captcha.service.MockCaptcha;
import junit.framework.TestCase;

import java.util.Collection;
import java.util.Locale;

/**
 * <p><ul><li></li></ul></p>
 *
 * @author <a href="mailto:marc.antoine.garrigue@gmail.com">Marc-Antoine Garrigue</a>
 * @version 1.0
 */
public abstract class CaptchaStoreTestAbstract extends TestCase {

    private CaptchaStore store;
    private Captcha capctha;
    public static final int SIZE = 10000;


    protected void setUp() throws Exception {
        super.setUp();
        store = initStore();
        capctha = new MockCaptcha(null);
    }

    public abstract CaptchaStore initStore();


    public void testHasCaptcha() throws Exception {
        assertFalse("should not have", store.hasCaptcha("1"));
        store.storeCaptcha("2", capctha);
        store.storeCaptcha("1 ", capctha);
        store.storeCaptcha(" 1", capctha);
        assertFalse("should not have", store.hasCaptcha("1"));
        store.storeCaptcha("1", capctha);
        assertTrue("should", store.hasCaptcha("1"));

    }

    public void testStoreCaptcha() throws Exception {


        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);
        }
        for (int i = 0; i < SIZE; i++) {
            assertNotNull(store.getCaptcha(String.valueOf(i)));
        }

    }


    public void testStoreCaptchaAndLocale() throws Exception {


          for (int i = 0; i < SIZE; i++) {
              store.storeCaptcha(String.valueOf(i), capctha, Locale.FRENCH);
          }
          for (int i = 0; i < SIZE; i++) {
              assertEquals(Locale.FRENCH,store.getLocale(String.valueOf(i)));
          }

      }



    public void testRemoveCaptcha() throws Exception {

        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);

        }
        assertEquals("should have a size of " + SIZE, store.getSize(), SIZE);

        for (int i = 0; i < SIZE; i++) {
            assertTrue("Should be removed", store.removeCaptcha(String.valueOf(i)));
        }

        for (int i = 0; i < SIZE; i++) {
            assertFalse("Should not be removed", store.removeCaptcha(String.valueOf(i)));
        }

        assertTrue("should be empty now", store.getSize() == 0);

    }

    public void testGetSize() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);
            assertEquals("Size should be : " + i, i + 1, store.getSize());
        }
        assertEquals("should have a size of " + SIZE, store.getSize(), SIZE);

        for (int i = 0; i < SIZE; i++) {
            store.removeCaptcha(String.valueOf(i));
            assertEquals("Size should be : " + (SIZE - i - 1), SIZE - i - 1, store.getSize());
        }

    }

    public void testGetKeys() throws Exception {

        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);

        }
        Collection keys = store.getKeys();


        for (int i = 0; i < SIZE; i++) {
            assertTrue("store should have key ", keys.contains(String.valueOf(i)));
        }

        for (int i = 0; i < SIZE; i++) {
            store.removeCaptcha(String.valueOf(i));
        }
        assertTrue("keys should be empty", store.getKeys().size() == 0);
    }

    public void testGetCaptcha() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);

        }

        for (int i = 0; i < SIZE; i++) {
            assertEquals("store should a captcha for this key ", capctha, store.getCaptcha(String.valueOf(i)));
        }

        assertNull(store.getCaptcha("unknown"));

    }

    public void testEmpty() throws Exception {
        for (int i = 0; i < SIZE; i++) {
            store.storeCaptcha(String.valueOf(i), capctha);
        }
        store.empty();
        assertEquals("Size should be 0", 0, store.getSize());
        assertTrue("keys should be empty", store.getKeys().size() == 0);

    }
}
