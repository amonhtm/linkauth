package dev.amonhtm.linkauth.util;

import java.util.Random;

public class Generator {

    private static String tokenChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public static String token(int chars) {
        StringBuilder token = new StringBuilder();

        for(int i = 0; i < chars; i++) {
            token.append(tokenChars.charAt(new Random().nextInt(tokenChars.length())));
        }

        return token.toString();
    }
}
