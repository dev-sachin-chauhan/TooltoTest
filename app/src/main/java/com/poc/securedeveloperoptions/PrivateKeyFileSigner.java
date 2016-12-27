package com.poc.securedeveloperoptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.PrivateKey;

/**
 * Created by sachin_chauhan on 12/26/16.
 */
public interface PrivateKeyFileSigner {
    void initialize(PrivateKey privateKey);

    void signFile(File file, File signedFile) throws FileNotFoundException;
}
