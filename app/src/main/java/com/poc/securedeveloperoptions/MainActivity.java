package com.poc.securedeveloperoptions;

import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import com.poc.sdolib.FileAsymmetricCrypto;
import com.poc.sdolib.FilePath;
import com.poc.sdolib.SDOAdmin;
import com.poc.sdolib.SDOAdminInterface;
import com.poc.sdolib.SignedFileHierarchicalManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final private static String TAG = "MainActivity";
    private static final int STORAGE_PERMISSION_CODE = 23;

    private Spinner mSpinner;
    private Button mBtnSign;
    private Button mBtnSignAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        mBtnSign = (Button) findViewById(R.id.btn_sign);
        mBtnSignAll = (Button) findViewById(R.id.btn_sign_all);

        //Create and set Adapter for spinner
        final List<String> list = new ArrayList<>();
        for (FilePath filePath : FilePath.values()) {
            list.add(filePath.toString());
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);

        //Initialize sdo with private key
        final SDOAdminInterface sdoAdminInterface = new SDOAdmin(new SignedFileHierarchicalManager());
        try {
            sdoAdminInterface.initialize(new FileAsymmetricCrypto(), new Utils().getPrivateKeyFromResource(getApplicationContext()));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }

        mBtnSign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Log.i(TAG, "Selected item :" + mSpinner.getSelectedItem());
                try {
                    sdoAdminInterface.signFile(getFilePath((String) mSpinner.getSelectedItem()));
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "File not found in :" + mSpinner.getSelectedItem());
                }
            }
        });
        mBtnSignAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String item = (String) mSpinner.getSelectedItem();
                Log.i(TAG, "Selected item :" + item);
                try {
                    sdoAdminInterface.signAllFiles();
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "File Not found " + item, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "File not found in :" + item);
                }
            }
        });

        //Ask of Storage Permission if not granted
        if (!isReadStorageAllowed()) {
            requestStoragePermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Storage Permission Already Granted", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, STORAGE_PERMISSION_CODE);
    }

    private FilePath getFilePath(String stringFilePath) {
        for (FilePath filePath : FilePath.values()) {
            if (filePath.toString().equals(stringFilePath)) {
                return filePath;
            }
        }
        return null;
    }
}
