package com.dmsBackend.security;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {

    private final Map<String, String> otpStore = new HashMap<>();

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(email, otp);
        return otp;
    }

    public boolean validateOtp(String email, String otp) {
        return otp.equals(otpStore.get(email));
    }

    public void clearOtp(String email) {
        otpStore.remove(email);
    }
}
