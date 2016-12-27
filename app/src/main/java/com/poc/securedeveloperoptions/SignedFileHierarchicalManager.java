package com.poc.securedeveloperoptions;

import java.io.File;
import java.util.HashMap;

import android.os.Environment;

/**
 * Created by sachin_chauhan on 12/26/16.
 */
public class SignedFileHierarchicalManager implements FileAccess {

    private final File SDOROOT;
    private final HashMap<FilePath,String> mPathFileHashMap = new HashMap<>();

    SignedFileHierarchicalManager() {
        SDOROOT = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "/.symantec/com.symantec.securedeveloperoptions/");
        SDOROOT.mkdirs();
        mPathFileHashMap.put(FilePath.BASE,"com.base/");
    }

    @Override
    public File getFile(FilePath filePath) {
        return new File(SDOROOT,mPathFileHashMap.get(filePath));
    }

    @Override
    public File getFile(FilePath filePath,String child) {
        final File file = new File(mPathFileHashMap.get(filePath), child);
        return new File(SDOROOT, file.toString());
    }
}
