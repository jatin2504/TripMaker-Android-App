package com.example.tripmaker.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringValidation {

    private static final String regexName = "^[\\p{L} .'-]+$";
    private static final String regexEmail = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static boolean validarteName(String name){
        if(TextUtils.isEmpty(name.trim())) return false;

        Pattern pattern = Pattern.compile(regexName,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.find();

    }

    public static boolean validateEmailAddress(String email){
        if(TextUtils.isEmpty(email.trim())) return false;

        Pattern pattern = Pattern.compile(regexEmail,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();

    }
}
