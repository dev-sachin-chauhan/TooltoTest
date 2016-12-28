package com.poc.securedeveloperoptions;

import android.os.CountDownTimer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by sachin_chauhan on 12/28/16.
 */
public class SDOAdminTest {
    private final static String TAG = "SDOAdminTest";
    static private PublicKey publicKey;
    static private PrivateKey privateKey;

    @BeforeClass
    public static void beforeSuite() throws Exception {
        System.out.println(TAG + " :Before Suite");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA", "SUN");
        kpg.initialize(1024);
        KeyPair keys = kpg.generateKeyPair();
        publicKey = keys.getPublic();
        privateKey = keys.getPrivate();
    }

    @Before
    public void set_Up() {
        System.out.println(TAG + " :Before Each Test");
    }

    @Test
    public void testInit() {
        SDOAdminInterface sdoAdminInterface = new SDOAdmin(new SignedFileHierarchicalManager());
        boolean exceptionFlag = false;
        try {
            sdoAdminInterface.initialize(new FileAsymmetricCrypto(), null);
        } catch (NullPointerException e) {
            exceptionFlag = true;
        }
        assertTrue("Exception not thrown", exceptionFlag);
    }

    @Test
    public void testSignedFile() throws FileNotFoundException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        SDOAdminInterface sdoAdminInterface = new SDOAdmin(new SignedFileHierarchicalManager());

        sdoAdminInterface.initialize(new PrivateKeyFileSigner() {
            @Override
            public void initialize(PrivateKey privateKey) {

            }

            @Override
            public void signFile(File file, File signedFile) throws FileNotFoundException {
                assertTrue(file.equals(new SignedFileHierarchicalManager().getFile(FilePath.BASE_ENGINE, FilePath.BASE_ENGINE.toString() + ".txt")));
                assertTrue(signedFile
                        .equals(new SignedFileHierarchicalManager().getFile(FilePath.BASE_ENGINE, FilePath.BASE_ENGINE.toString() + ".ext")));
                countDownLatch.countDown();
            }
        }, privateKey);
        sdoAdminInterface.signFile(FilePath.BASE_ENGINE);
        countDownLatch.await();
    }

    @Test
    public void testSignedAllFile() throws FileNotFoundException {
        final CountDownLatch countDownLatch = new CountDownLatch(FilePath.values().length);
        SDOAdminInterface sdoAdminInterface = new SDOAdmin(new SignedFileHierarchicalManager());

        sdoAdminInterface.initialize(new PrivateKeyFileSigner() {
            @Override
            public void initialize(PrivateKey privateKey) {
            }

            @Override
            public void signFile(File file, File signedFile) throws FileNotFoundException {
                countDownLatch.countDown();
            }
        }, privateKey);
        sdoAdminInterface.signAllFiles();
        try {
            countDownLatch.await(2000, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {

        }
        assertTrue(countDownLatch.getCount() == 0);
    }

    @Test
    public void testVerifyFile() throws FileNotFoundException, InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        SDOLibInterface sdoLibInterface = new SDOAdmin(new SignedFileHierarchicalManager());

        sdoLibInterface.initialize(new PublicKeyFileVerifier() {
            @Override
            public void initialize(PublicKey privateKey) {
            }

            @Override
            public boolean verifyFile(File file, File signedFile) throws FileNotFoundException {
                assertTrue(file.equals(new SignedFileHierarchicalManager().getFile(FilePath.BASE_ENGINE, FilePath.BASE_ENGINE.toString() + ".txt")));
                assertTrue(signedFile
                        .equals(new SignedFileHierarchicalManager().getFile(FilePath.BASE_ENGINE, FilePath.BASE_ENGINE.toString() + ".ext")));
                countDownLatch.countDown();
                return true;
            }
        }, publicKey);
        File testFile = sdoLibInterface.getVerifiedFile(FilePath.BASE_ENGINE);
        countDownLatch.await();
        assertTrue(testFile.equals(new SignedFileHierarchicalManager().getFile(FilePath.BASE_ENGINE, FilePath.BASE_ENGINE.toString() + ".txt")));
    }
}
