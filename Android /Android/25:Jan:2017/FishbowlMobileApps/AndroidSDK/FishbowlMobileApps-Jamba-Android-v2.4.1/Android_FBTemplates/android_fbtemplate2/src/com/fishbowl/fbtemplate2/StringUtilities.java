package com.fishbowl.fbtemplate2;


/**
 * Created by Digvijay Chauhan on 7/12/15.
 */
public class StringUtilities {

	public static boolean isValidString(String string) {
		if ( string == null || string.equals("") ) {
			return false;
		}
		return true;
	}

	public static boolean isValidDoubleToString(String string) {
		if ( string == null || string.equals("") || string.equals("0")||string.equals(" ")||string.equals("0.0")||string.equals("US$0.0") ) {
			return false;
		}
		return true;
	}
	public static boolean isValidIntergerToString(String string) {

		if ( string == null || string.equals("") || string.equals("0.0") ) {
			return false;
		}
		return true;
	}



}
