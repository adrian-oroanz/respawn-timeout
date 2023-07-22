package io.github.adrian_oroanz.respawn_timeout.util;


public class TimeUtils {
	
	public static String secondsToHHmmss (int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

		return String.format("%02d:%02d:%02d", hours, minutes, secs);
	}

}
