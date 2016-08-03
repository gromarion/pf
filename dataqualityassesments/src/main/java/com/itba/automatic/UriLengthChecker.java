package com.itba.automatic;

public class UriLengthChecker {
	
	public static int MAX_URI_LENGTH = 2048;

	public static boolean checker(String uri) {
		boolean validUriLength = uri.length() <= MAX_URI_LENGTH;
		boolean noQueryParams = uri.contains("?");
		return validUriLength && noQueryParams;
	}
}
