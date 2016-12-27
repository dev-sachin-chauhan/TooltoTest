package com.poc.securedeveloperoptions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import com.poc.securedeveloperoptions.Excpetions.PublicKeyNotInitializedException;

/**
 * Created by sachin_chauhan on 12/26/16.
 */
public class FileAsymmetricCrypto implements PrivateKeyFileSigner, PublicKeyFileVerifier {
    private final static String TAG = "FileAsymmetricCrypto";
    private Signature mSignature;
    private PublicKey mPublicKey;
    private PrivateKey mPrivateKey;

    FileAsymmetricCrypto() {
        try {
            mSignature = Signature.getInstance("SHA1withDSA", "SUN");
        } catch (NoSuchProviderException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void signFile(File file, File signedFile) throws FileNotFoundException {
        if (null == mPrivateKey) {
            throw new PublicKeyNotInitializedException();
        }
        if (!file.exists()) {
            throw new FileNotFoundException("Source File not found");
        }
        if (!signedFile.exists()) {
            try {
                signedFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            mSignature.initSign(mPrivateKey);
            final byte outputFileBytes[];
            outputFileBytes = sign(file);
            System.out.println(TAG + "signature length:" + outputFileBytes.length);
            putFileContentInBytes(signedFile, outputFileBytes);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(PrivateKey privateKey) {
        if (null == privateKey) {
            throw new NullPointerException("private Key passed to initialize is null");
        }
        mPrivateKey = privateKey;
    }

    @Override
    public void initialize(PublicKey publicKey) {
        if (null == publicKey) {
            throw new NullPointerException("public Key passed to initialize is null");
        }
        mPublicKey = publicKey;
    }

    @Override
    public boolean verifyFile(File file, File signedFile) throws FileNotFoundException {
        if (null == mPublicKey) {
            throw new PublicKeyNotInitializedException();
        }
        if (!file.exists()) {
            throw new FileNotFoundException("Source File not found");
        }
        if (!signedFile.exists()) {
            throw new FileNotFoundException("Signed File not found");
        }

        try {
            mSignature.initVerify(mPublicKey);
            final byte signedFileByte[] = getFileContentInBytes(signedFile);
            return verify(file, signedFileByte);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private byte[] getFileContentInBytes(File file) throws Exception {
        final byte fileBytes[] = new byte[1024];
        final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        bufferedInputStream.read(fileBytes);
        bufferedInputStream.close();
        return fileBytes;
    }

    private void putFileContentInBytes(File file, byte[] fileBytes) throws Exception {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
        bufferedOutputStream.write(fileBytes);
        bufferedOutputStream.close();
    }

    private byte[] sign(File file) throws IOException, SignatureException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bufferedInputStream.read(buffer)) >= 0) {
            mSignature.update(buffer, 0, len);
        }
        bufferedInputStream.close();
        return mSignature.sign();
    }

    private boolean verify(File file, byte[] sigToVerify) throws IOException, SignatureException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));

        byte[] buffer = new byte[1024];
        int len;
        while (bufferedInputStream.available() != 0) {
            len = bufferedInputStream.read(buffer);
            mSignature.update(buffer, 0, len);
        }
        bufferedInputStream.close();
        return mSignature.verify(sigToVerify);
    }
}
