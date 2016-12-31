package com.poc.sdolib;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.PublicKey;

/**
 * Created by sachin_chauhan on 12/28/16.
 */
public interface SDOLibInterface {

    void initialize(PublicKeyFileVerifier publicKeyFileVerifier, PublicKey publicKey);

    File getVerifiedFile(FilePath filePath) throws FileNotFoundException;
}
