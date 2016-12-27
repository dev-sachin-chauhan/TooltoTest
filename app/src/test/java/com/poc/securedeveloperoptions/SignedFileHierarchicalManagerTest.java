package com.poc.securedeveloperoptions;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import android.os.Environment;
import android.util.Log;

/**
 * Created by sachin_chauhan on 12/27/16.
 */
public class SignedFileHierarchicalManagerTest {

    private final static String TAG = "FileManagerTest";

    @BeforeClass
    public static void beforeSuite() throws Exception {
        System.out.println(TAG + " :Before Suite");
    }

    @Before
    public void set_Up() {
        System.out.println(TAG + " :Before Each Test");
    }

    @Test
    public void testFilePath() throws NoSuchFieldException, IllegalAccessException {

        File actualPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "/.symantec/com.symantec.securedeveloperoptions/Test/");

        FileAccess fileAccess = new SignedFileHierarchicalManager();
        Field field = SignedFileHierarchicalManager.class.getDeclaredField("mPathFileHashMap");
        field.setAccessible(true);
        HashMap<FilePath,String> hashMap = (HashMap<FilePath, String>) field.get(fileAccess);
        hashMap.put(FilePath.BASE,"Test/");


        File testFile = fileAccess.getFile(FilePath.BASE);
        Log.i(TAG, "File path: "+testFile);
        System.out.println(TAG + " File path: "+ testFile.getAbsolutePath());
        System.out.println(TAG + " SDO path: "+ actualPath.getAbsolutePath());
        Assert.assertTrue(testFile.equals(actualPath));
    }

    @Test
    public void testFilePathwithFile() throws NoSuchFieldException, IllegalAccessException {

        File actualPath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "/.symantec/com.symantec.securedeveloperoptions/Test/Test.txt");

        FileAccess fileAccess = new SignedFileHierarchicalManager();
        Field field = SignedFileHierarchicalManager.class.getDeclaredField("mPathFileHashMap");
        field.setAccessible(true);
        HashMap<FilePath,String> hashMap = (HashMap<FilePath, String>) field.get(fileAccess);
        hashMap.put(FilePath.BASE,"Test/");


        File testFile = fileAccess.getFile(FilePath.BASE,"Test.txt");
        Log.i(TAG, "File path: "+testFile);
        System.out.println(TAG + " File path: "+ testFile.getAbsolutePath());
        System.out.println(TAG + " SDO path: "+ actualPath.getAbsolutePath());
        Assert.assertTrue(testFile.equals(actualPath));
    }
}