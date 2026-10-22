package com.example.microbankingproject.utils;

import java.security.SecureRandom;

public class OTPCodeGenerator {

    private static final SecureRandom random = new SecureRandom();

    public static String generateCode() {
        Integer code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
}
