package com.wingit.analysisserver;

public class Utils
{

    public static String escapeWhitespace(String s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("SERVICE_ACCOUNT_KEY missing from environment!");
        }

        String newString = "";
        for (Character c : s.toCharArray()) {
            if ("00a0".equals(Integer.toHexString(c | 0x10000).substring(1))) {
                newString += " ";
            } else {
                newString += c;
            }
        }
        return newString;
    }

}
