package com.fd.mobilelocatornotifier;

import com.kristijandraca.backgroundmaillibrary.Utils;

import java.util.Random;

/**
 * Created by frog on 10/01/15.
 */
public class Token {
    public static boolean tokenIsValid(String token) {
        return Utils.decryptIt(token).matches("[a-zA-Z]+");
    }

    public static String generateToken(){
        Random r = new Random();
        char c = (char)(r.nextInt(26) + 'a');
        return Utils.encryptIt(Character.toString(c));
    }
}
