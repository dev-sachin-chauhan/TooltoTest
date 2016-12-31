package com.poc.sdolib;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.poc.sdolib.Excpetions.PublicKeyNotInitializedException;

/**
 * Created by sachin_chauhan on 12/26/16.
 */
public class FileAsymmetricCryptoTest {
    private final static String TAG = "AsymmetricCryptoTest";
    static private PublicKey publicKey;
    static private PrivateKey privateKey;

    @BeforeClass
    public static void beforeSuite() throws Exception {
        System.out.println(TAG + " :Before Suite");
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA","BC");
        kpg.initialize(1024);
        KeyPair keys = kpg.generateKeyPair();
        publicKey = keys.getPublic();
        privateKey = keys.getPrivate();

    }

    @Before
    public void set_Up(){
        System.out.println(TAG + " :Before Each Test");
    }

    @Test
    public void publicKeyEncryptInitializeException(){
        PrivateKeyFileSigner privateKeyFileSigner = new FileAsymmetricCrypto();
        boolean exceptionFlag = false;
        try {
            privateKeyFileSigner.initialize(null);
        }catch(NullPointerException e){
            exceptionFlag = true;
        }
        assertTrue("Exception not thrown", exceptionFlag);
    }

    @Test
    public void privateKeyFileSignerNotInitialize(){
        PrivateKeyFileSigner privateKeyFileSigner = new FileAsymmetricCrypto();
        File srcFile = new File("test.txt");
        try {
            if (!srcFile.exists()) {
                srcFile.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(srcFile));
            bufferedOutputStream.write("SampleText".getBytes());
            bufferedOutputStream.close();
        }catch(Exception e){

        }
        File desFile = new File("encryptTest.txt");
        boolean exceptionFlag = false;
        try {
            privateKeyFileSigner.signFile(srcFile, desFile);
        }catch(PublicKeyNotInitializedException e){
            exceptionFlag = true;
        }catch(FileNotFoundException e){

        }
        assertTrue("Exception not thrown", exceptionFlag);
    }

    @Test
    public void isFileSigned() throws Exception {
        PrivateKeyFileSigner privateKeyFileSigner = new FileAsymmetricCrypto();
        privateKeyFileSigner.initialize(privateKey);

        File srcFile = new File("test.txt");
        if(!srcFile.exists()) {
            srcFile.createNewFile();
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(srcFile));
        bufferedOutputStream.write("SampleText".getBytes());
        bufferedOutputStream.close();

        File signFile = new File("SignFile.txt");

        privateKeyFileSigner.signFile(srcFile, signFile);

        assertTrue("Encrypted File does not exist", signFile.exists());
    }

    @Test
    public void verifyFileSigned() throws Exception {
        PrivateKeyFileSigner privateKeyFileSigner = new FileAsymmetricCrypto();
        privateKeyFileSigner.initialize(privateKey);

        File srcFile = new File("test.txt");
        if(!srcFile.exists()) {
            srcFile.createNewFile();
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(srcFile));
        bufferedOutputStream.write("SampleText".getBytes());
        bufferedOutputStream.close();

        File signFile = new File("SignFile.txt");

        privateKeyFileSigner.signFile(srcFile, signFile);

        assertTrue("Encrypted File does not exist", signFile.exists());

        PublicKeyFileVerifier publicKeyFileVerifier = new FileAsymmetricCrypto();
        publicKeyFileVerifier.initialize(publicKey);
        assertTrue(publicKeyFileVerifier.verifyFile(srcFile,signFile));
    }

    @Test
    public void verifyIllegalFileSigned() throws Exception {
        PrivateKeyFileSigner privateKeyFileSigner = new FileAsymmetricCrypto();
        privateKeyFileSigner.initialize(privateKey);

        File srcFile = new File("test.txt");
        if(!srcFile.exists()) {
            srcFile.createNewFile();
        }
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(srcFile));
        bufferedOutputStream.write("SampleText".getBytes());
        bufferedOutputStream.close();

        File signFile = new File("SignFile.txt");

        privateKeyFileSigner.signFile(srcFile, signFile);

        assertTrue("Encrypted File does not exist", signFile.exists());

        PublicKeyFileVerifier publicKeyFileVerifier = new FileAsymmetricCrypto();
        publicKeyFileVerifier.initialize(publicKey);

        bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(srcFile));
        bufferedOutputStream.write("Sample".getBytes());
        bufferedOutputStream.close();

        assertFalse(publicKeyFileVerifier.verifyFile(srcFile,signFile));
    }
}
