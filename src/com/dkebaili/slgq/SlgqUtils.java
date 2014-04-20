package com.dkebaili.slgq;

import java.util.Calendar;
import java.util.Date;

import android.graphics.Color;

public class SlgqUtils {
	public enum TimeType {
		S,
		L,
		G,
		Q
	}
	
	public static int colorOf(TimeType t) {
		if (t == null) {
			return Color.BLACK;
		}
		switch (t) {
		case S:
			return Color.BLUE;
		case L:
			return Color.RED;
		case G:
			return 0xFF00BB00; // dark green
		case Q:
			return Color.MAGENTA;
		default:
			return Color.BLACK;
		}
	}
	
	public static long millisPerTick(TimeType t) {
		if (t == null) {
			return 0;
		}
		switch (t) {
		case S:
			return 1000L * 60 * 60 * 3;
		case L:
			return 1000L * 60 * 6;
		case G:
			return 1000L * 12;
		case Q:
			return 400L;
		default:
			return 0;
		}
	}
	
	public static int getMaxValue(TimeType t) {
		if (t == null) {
			return 0;
		}
		switch (t) {
		case S:
			return 8;
		case L:
			return 30;
		case G:
			return 30;
		case Q:
			return 30;
		default:
			return 0;
		}
	}
	
	public static long millisTillNextUpdate(TimeType t, Date d) {
		if (t == null) {
			return Long.MAX_VALUE;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		final long actualVal = calendar.getTimeInMillis();
		switch (t) {
		case S:
			calendar.set(Calendar.HOUR_OF_DAY, (calendar.get(Calendar.HOUR_OF_DAY) / 3) * 3);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			break;
		case L:
			calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE) / 6) * 6);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			break;
		case G:
			calendar.set(Calendar.SECOND, (calendar.get(Calendar.SECOND) / 12) * 12);
			calendar.set(Calendar.MILLISECOND, 0);
			break;
		case Q:
			int millisInQ = (int)(calendar.get(Calendar.SECOND) * 1000 + calendar.get(Calendar.MILLISECOND)) % 400;
			calendar.add(Calendar.MILLISECOND, -millisInQ);
			break;
		default:
			return Long.MAX_VALUE;
		}
		final long representingVal = calendar.getTimeInMillis();
		final long ret = (representingVal + millisPerTick(t)) - actualVal;
		return ret;
	}
	
	public static int currentValue(TimeType t, Date d) {
		if (t == null || d == null) {
			return 0;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		switch (t) {
		case S:
			return calendar.get(Calendar.HOUR_OF_DAY) / 3;
		case L:
			int minutesSinceS = (calendar.get(Calendar.HOUR_OF_DAY) % 3) * 60 + calendar.get(Calendar.MINUTE);
			return minutesSinceS / 6;
		case G:
			int secondsSinceL = (calendar.get(Calendar.MINUTE) % 6) * 60 + calendar.get(Calendar.SECOND);
			return secondsSinceL / 12;
		case Q:
			int tenthSOfsecondsSinceG = (calendar.get(Calendar.SECOND) % 12) * 10 + calendar.get(Calendar.MILLISECOND) / 100;
			return tenthSOfsecondsSinceG / 4;
		default:
			return 0;
		}
	}
	
	public static float currentValueF(TimeType t, Date d) {
		if (t == null || d == null) {
			return 0;
		}
		float floor = (float)currentValue(t, d);
		float fraction = 1.0f - ((float)millisTillNextUpdate(t, d) / (float)millisPerTick(t));
		return floor + fraction;
	}
}
