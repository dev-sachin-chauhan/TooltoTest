package com.poc.sdolib;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.PublicKey;

/**
 * Created by sachin_chauhan on 12/26/16.
 */
public interface PublicKeyFileVerifier {
    void initialize(PublicKey publicKey);

    boolean verifyFile(File file, File signedFile) throws FileNotFoundException;
}
