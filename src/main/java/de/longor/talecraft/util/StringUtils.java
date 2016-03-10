package de.longor.talecraft.util;

public class StringUtils {
	
	public static String humanReadableByteCount(long bytes) {
		return humanReadableByteCount(bytes, true);
	}
	
	/**
	 * Directly taken from StackOverflow:
	 * http://stackoverflow.com/a/3758880
	 **/
	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		
		if (bytes < unit) {
			return bytes + " B";
		}
		
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	
}
