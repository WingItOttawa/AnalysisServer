package com.wingit.analysisserver;

/**
 * Utility class for common functionality
 *
 * @author AveryVine
 * @since September 2018
 */
public class Utils {

    /**
     * Escapes whitespace in an input string
     *
     * @param s the string to be processed
     * @return the newly escaped string
     * @throws IllegalArgumentException when
     */
    public static String escapeWhitespace(String s) throws IllegalArgumentException {
        if (s == null) {
            throw new IllegalArgumentException("No string for which to escape whitespace");
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
