package ofs.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Does a few datatype conversions
 * @author shreyasvalmiki
 *
 */
public class GeneralUtils {
	
	/**
	 * Returns the "long" form of a date to store into the filesystem
	 * 
	 * Reference: http://stackoverflow.com
	 * @param date
	 * @return
	 */
	public static long getLongFromDate(Date date){
		SimpleDateFormat format = new SimpleDateFormat("yyMMddhhmmss");
		String strDate = new String();
		strDate = format.format(date);
		return Long.parseLong(strDate);
	}
	
	/**
	 * Returns a "Date" from the long form of the date
	 * 
	 * Reference: http://stackoverflow.com
	 * @param date
	 * @return
	 */
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
