package lml.snir.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author fanou
 */
public class DateConverter {
    private static final SimpleDateFormat sdfTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
    
    /**
     * 
     * @param time hh:mm:ss
     * @return
     * @throws ParseException 
     */
    public static Date parseTime(String time) throws ParseException {
        return sdfTime.parse(time);
    }
    
    /**
     * 
     * @param time Date time format
     * @return hh:mm:ss string time
     * @throws ParseException 
     */
    public static String formatTime(Date time) throws ParseException {
        return sdfTime.format(time);
    }
    
    /**
     * 
     * @param timeStamp yyyy-MM-dd hh:mm:ss
     * @return
     * @throws ParseException 
     */
    public static Date parseTimeStamp(String timeStamp) throws ParseException {
        return sdfTimeStamp.parse(timeStamp);
    }
    
    public static String formatTimeStamp(Date timeStamp) throws ParseException {
        return sdfTimeStamp.format(timeStamp);
    }
    
    /**
     * 
     * @param date yyyy-MM-dd
     * @return
     * @throws ParseException 
     */
    public static Date parseDate(String date) throws ParseException {
        return sdfDate.parse(date);
    }
    
    public static String formatDate(Date date) throws ParseException {
        return sdfDate.format(date);
    }
    
    
}
