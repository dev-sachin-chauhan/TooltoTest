package com.poc.securedeveloperoptions;

import java.io.File;

/**
 * Created by sachin_chauhan on 12/27/16.
 */
public interface FileAccess {

    File getFile(FilePath filePath);

    File getFile(FilePath filePath,String child);
}
