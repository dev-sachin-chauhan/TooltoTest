package com.poc.securedeveloperoptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import android.content.Context;

/**
 * Created by sachin_chauhan on 12/30/16.
 */
public class Utils {

    public void createPublicPrivateKeyFiles() {

    }

    public PrivateKey getPrivateKeyFromResource(Context context) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
        InputStream inputStream = context.getResources()
                .openRawResource(context.getResources().getIdentifier("privatekey", "raw", context.getPackageName()));
        ;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(output.toByteArray());
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory kf = KeyFactory.getInstance("DSA", "BC");
        return kf.generatePrivate(spec);
    }

    public PublicKey getPublicKeyFromResource(Context context) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {
        InputStream inputStream = context.getResources()
                .openRawResource(context.getResources().getIdentifier("publickey", "raw", context.getPackageName()));
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        X509EncodedKeySpec spec = new X509EncodedKeySpec(output.toByteArray());
        Security.addProvider(new BouncyCastleProvider());
        KeyFactory kf = KeyFactory.getInstance("DSA", "BC");
        return kf.generatePublic(spec);
    }
}
