package com.example.travelapplication.infrastructure.utils;

import android.util.Base64;

import java.io.IOException;
import java.util.Date;

import com.example.travelapplication.exception.ThrowableException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtUtils {

    private static String token = "";
    public static void setToken(String token) {
        JwtUtils.token = token;
    }

    public static String getToken() {
        return JwtUtils.token;
    }

    public static String getPayload() {
        if (token == null || token.isEmpty()) {
            throw new UnsupportedOperationException("token为空");
        }

        String[] code = token.split("\\.");
        return new String(Base64.decode(code[1], Base64.URL_SAFE));
    }


    public static JsonNode convertPayloadToJsonNode() {
        try {
            return new ObjectMapper().readTree(getPayload());
        } catch (IOException e) {
            throw new ThrowableException(e);
        }
    }

    public static boolean isExpiration() {
        return checkExpirationTime(0);
    }

    public static boolean isTokenNeedRefresh() {
        long timestampOf20Min = 1800000;
        return checkExpirationTime(timestampOf20Min);
    }

    private static boolean checkExpirationTime(long expirationTimeThreshold) {
        long expirationTime = convertPayloadToJsonNode().path("exp").longValue();
        return expirationTime * 1000 - new Date().getTime()< expirationTimeThreshold;
    }
}
