package lib;

public class StringUtils {
	
	private static final String[] HTTP_SCHEMES = {"http","https"};

	public static boolean containsURL(String inputStr) {
		for (int i = 0; i < HTTP_SCHEMES.length; i++) {
			if (inputStr.contains(HTTP_SCHEMES[i])) {
				return true;
			}
		}
		return false;
	}
}
