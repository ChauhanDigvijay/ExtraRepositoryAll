package com.fishbowl.apps.olo.Utils;

/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public class Utils
{
    public static String stripExtension(String str)
    {
        // Handle null case specially.
        if (str == null)
        {
            return null;
        }
        // Get position of last '.'.
        int pos = str.lastIndexOf(".");
        // If there wasn't any '.' just return the string as is.
        if (pos == -1)
        {
            return str;
        }
        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }

    public static String getUnescapedString(String actualString) {
        String newLineCharacter = "&#xD;&#xA;";
        if (actualString.startsWith(newLineCharacter)) {
            actualString = actualString.replaceFirst(newLineCharacter, "");//Remove new line character from the starting of the string.
        }
        return actualString.replaceAll("&amp;", "&").replaceAll(newLineCharacter + newLineCharacter, "\n")// Double line break into one
                .replaceAll(newLineCharacter, "\n");
    }

    // get full address
    public static String getFormatedAddress(String streetAddress, String city, String state, String zip) {
        String address = streetAddress;
        if (city != null) // Incase Of Preferred store street address has city, state and zip
        {
            address += "\n" + city + ", " + state + " " + zip;
        }
        return address;
    }
}