package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLHelper {
	
	private static final String URL_PATTERN = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\'\".,<>???“”‘’]))";

	public static String transformURLs(String object) {
		Pattern patt = Pattern.compile(URL_PATTERN);
		Matcher matcher = patt.matcher(object);

		return matcher.replaceAll("<a href=\"$1\" target=\"_blank\">$1</a>");
	}
}
