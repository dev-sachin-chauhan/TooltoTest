package com.poc.securedeveloperoptions;

import java.io.FileNotFoundException;
import java.security.PrivateKey;

/**
 * Created by sachin_chauhan on 12/28/16.
 */
public interface SDOAdminInterface {

    void initialize(PrivateKeyFileSigner privateKeyFileSigner, PrivateKey privateKey);

    void signFile(FilePath filePath) throws FileNotFoundException;

    void signAllFiles() throws FileNotFoundException;
}
