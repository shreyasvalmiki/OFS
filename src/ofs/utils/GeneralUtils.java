package ofs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneralUtils {
	public static long getLongFromDate(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmss");
		String strDate = new String();
		strDate = format.format(date);
		return Long.parseLong(strDate);
	}
	
	public static Date getDateFromLong(long date){
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmss");
		
		try {
			return format.parse(Long.toString(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
}
