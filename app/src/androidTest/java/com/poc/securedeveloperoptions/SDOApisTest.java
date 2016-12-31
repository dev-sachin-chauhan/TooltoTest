package com.poc.securedeveloperoptions;

import java.io.FileNotFoundException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.poc.sdolib.FileAsymmetricCrypto;
import com.poc.sdolib.FilePath;
import com.poc.sdolib.SDOAdmin;
import com.poc.sdolib.SDOLibInterface;
import com.poc.sdolib.SignedFileHierarchicalManager;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.SmallTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

/**
 * Created by sachin_chauhan on 12/30/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class SDOApisTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @Before
    public void test_setUp() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidKeySpecException {
        publicKey = new Utils().getPublicKeyFromResource(mainActivityActivityTestRule.getActivity().getApplicationContext());
        privateKey = new Utils().getPrivateKeyFromResource(mainActivityActivityTestRule.getActivity().getApplicationContext());
    }

    @Test
    public void testCreateFileApi() throws InterruptedException, FileNotFoundException {
        Matcher<View> spinner = ViewMatchers.withId(R.id.spinner);
        Espresso.onView(spinner).perform(ViewActions.click());
        Thread.sleep(500);
        Matcher<View> select = ViewMatchers.withText("BASE_MOBILE");
        Espresso.onView(select).perform(ViewActions.click());
        Thread.sleep(500);
        Matcher<View> buttonSign = ViewMatchers.withId(R.id.btn_sign);
        Espresso.onView(buttonSign).perform(ViewActions.click());

    }

    @Test
    public void testSDOSignatureApi() throws InterruptedException, FileNotFoundException {
        Thread.sleep(500);
        Matcher<View> spinner = ViewMatchers.withId(R.id.spinner);
        Espresso.onView(spinner).perform(ViewActions.click());
        Thread.sleep(500);
        Matcher<View> select = ViewMatchers.withText("BASE_MOBILE");
        Espresso.onView(select).perform(ViewActions.click());
        Thread.sleep(500);
        Matcher<View> buttonSign = ViewMatchers.withId(R.id.btn_sign);
        Espresso.onView(buttonSign).perform(ViewActions.click());
        Thread.sleep(500);
        SDOLibInterface sdoLibInterface = new SDOAdmin(new SignedFileHierarchicalManager());
        sdoLibInterface.initialize(new FileAsymmetricCrypto(), publicKey);
        Assert.assertNotNull(sdoLibInterface.getVerifiedFile(FilePath.BASE_MOBILE));
    }
}
