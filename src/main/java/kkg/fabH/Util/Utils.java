package kkg.fabH.Util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Utils {
	public static String convertDate(Date date, String dateFormat,String toTimezoneId)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone(toTimezoneId));
		return simpleDateFormat.format(calendar.getTime());
	}
	
	public static Date convertDateToGMTDate(Date date){
		TimeZone timeZone = TimeZone.getDefault();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MILLISECOND, -timeZone.getOffset(Calendar.getInstance().getTimeInMillis()));
		return calendar.getTime();
	}
	
	public static Date convertGMTDateToLocalDate(Date date){
		TimeZone timeZone = TimeZone.getDefault();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MILLISECOND, +timeZone.getOffset(Calendar.getInstance().getTimeInMillis()));
		return calendar.getTime();
	}
	
	public static double round(double value, int places) {
		StringBuilder decimalPlaces = new StringBuilder("#.");
		for (int i=0; i< places; i++) {
			decimalPlaces.append("#");
		}
		DecimalFormat decimalFormat = new DecimalFormat(decimalPlaces.toString());
		return Double.valueOf(decimalFormat.format(value));
	}
	
}