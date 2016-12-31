package com.poc.sdolib;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.PrivateKey;
import java.security.PublicKey;

import android.util.Log;

/**
 * Created by sachin_chauhan on 12/28/16.
 */
public class SDOAdmin implements SDOAdminInterface, SDOLibInterface {
    private final static String TAG = "SDOAdmin";
    private final FileAccess mFileAccess;
    private PrivateKeyFileSigner mPrivateKeyFileSigner;
    private PublicKeyFileVerifier mPublicKeyFileVerifier;

    public SDOAdmin(FileAccess fileAccess) {
        mFileAccess = fileAccess;
    }

    @Override
    public void initialize(PrivateKeyFileSigner privateKeyFileSigner, PrivateKey privateKey) {
        mPrivateKeyFileSigner = privateKeyFileSigner;
        mPrivateKeyFileSigner.initialize(privateKey);
    }

    @Override
    public void signFile(FilePath filePath) throws FileNotFoundException {
        if (null == filePath) {
            throw new NullPointerException();
        }
        final File originalFile = mFileAccess.getFile(filePath, filePath.toString() + ".txt");
        final File signedFile = mFileAccess.getFile(filePath, filePath.toString() + ".ext");
        Log.d(TAG, String.format("file originalFile %s signed file originalFile %s", originalFile.toString(), signedFile.toString()));
        mPrivateKeyFileSigner.signFile(originalFile, signedFile);
    }

    @Override
    public void signAllFiles() throws FileNotFoundException {
        for (FilePath filePath : FilePath.values()) {
            Log.d(TAG, String.format("file path: %s", filePath.toString()));
            signFile(filePath);
        }
    }

    @Override
    public void initialize(PublicKeyFileVerifier publicKeyFileVerifier, PublicKey publicKey) {
        mPublicKeyFileVerifier = publicKeyFileVerifier;
        mPublicKeyFileVerifier.initialize(publicKey);
    }

    @Override
    public File getVerifiedFile(FilePath filePath) throws FileNotFoundException {
        if (null == filePath) {
            throw new NullPointerException();
        }
        final File originalFile = mFileAccess.getFile(filePath, filePath.toString() + ".txt");
        final File signedFile = mFileAccess.getFile(filePath, filePath.toString() + ".ext");
        Log.d(TAG, String.format("file originalFile %s signed file originalFile %s", originalFile.toString(), signedFile.toString()));
        mPublicKeyFileVerifier.verifyFile(originalFile, signedFile);
        return originalFile;
    }
}
