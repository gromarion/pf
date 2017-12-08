package lib;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class StringUtils {
	
	private static final String[] HTTP_SCHEMES = {"http","https"};
	
	private static final double A_FLOOR = 0.9;
	private static final double B_FLOOR = 0.8;
	private static final double B_CEIL = 0.89;
	private static final double C_FLOOR = 0.7;
	private static final double C_CEIL = 0.79;
	private static final double D_FLOOR = 0.6;
	private static final double D_CEIL = 0.69;

	public static boolean containsURL(String inputStr) {
		for (int i = 0; i < HTTP_SCHEMES.length; i++) {
			if (inputStr.contains(HTTP_SCHEMES[i])) {
				return true;
			}
		}
		return false;
	}
	
	public static String formatDouble(double number, int decimals) {
		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.CEILING);

        return df.format(number);
	}
	
	public static char letterQualification(double score) {
		if (score >= A_FLOOR) {
			return 'A';
		} else if (score <= B_CEIL && score >= B_FLOOR) {
			return 'B';
		} else if (score <= C_CEIL && score >= C_FLOOR) {
			return 'C';
		} else if (score <= D_CEIL && score >= D_FLOOR) {
			return 'D';
		} else {
			return 'F';
		}
	}
}
