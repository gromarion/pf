package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lib.StringUtils;

public class URLHelper {

	private static final String URL_PATTERN = "(?i)\\b((?:https?://|www\\d{0,3}[.]|[a-z0-9.\\-]+[.][a-z]{2,4}/)(?:[^\\s()<>]+|\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\))+(?:\\(([^\\s()<>]+|(\\([^\\s()<>]+\\)))*\\)|[^\\s`!()\\[\\]{};:\'\".,<>???“”‘’]))";

	public static String transformURLs(String object, boolean shorten) {
		Pattern patt = Pattern.compile(URL_PATTERN);
		Matcher matcher = patt.matcher(object);

		if (shorten) {
			while (matcher.find()) {
				String match = matcher.group();
				object = object.replaceAll(match,
						"<a href=\"" + match + "\" target=\"_blank\">" + StringUtils.shortenText(match) + "</a>");
			}
			return object;
		} else {
			return matcher.replaceAll("<a href=\"$1\" target=\"_blank\">$1</a>");
		}
	}
}
