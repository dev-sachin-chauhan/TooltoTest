package com.poc.sdolib.Excpetions;

/**
 * Created by sachin_chauhan on 12/26/16.
 */
public class PublicKeyNotInitializedException extends RuntimeException {

    public PublicKeyNotInitializedException(){
        super("Public Key not Initialized");
    }

    public PublicKeyNotInitializedException(String exceptionMsg) {
        super(exceptionMsg);
    }
}
