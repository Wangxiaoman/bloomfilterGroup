package com.paradigm.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.paradigm.log.CommonLogger;

public class DateTools {

    private static final ThreadLocal<SimpleDateFormat> SDF = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };
    
    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    public static Date getDate(String time,String formatStr) throws ParseException{
    	DateFormat sdf = new SimpleDateFormat(formatStr);
    	return sdf.parse(time);
    }     

    public static String formatDate(Date time) {
        return SDF.get().format(time);
    }
    
    public static String formatDate(Date time,String formatStr) {
    	SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(time);
    }

    public static String formatDate(long time) {
        return formatDate(new Date(time));
    }
    
    public static Date getNDate(int nDay){
    	Calendar calendar = Calendar.getInstance();
    	int day = calendar.get(Calendar.DATE); 
    	calendar.set(Calendar.DATE, day+nDay);
    	return calendar.getTime();
    }
    
    public static int getDays(Date beginTime,Date endTime){
    	int days = (int) ((endTime.getTime() - beginTime.getTime()) / (1000*3600*24));
        return days;
    }
    
    // 和当前时间相差30年以上，认为时间无效
    public static boolean validDate(Date date){
    	if(date != null && date.getTime() > 0){
    		try{
	    		Calendar minCalendar = Calendar.getInstance();
	    		minCalendar.add(Calendar.YEAR, -30);
	    		Calendar maxCalendar = Calendar.getInstance();
	    		maxCalendar.add(Calendar.MONTH, 1);
                return date.after(minCalendar.getTime()) && date.before(maxCalendar.getTime());
    		}catch(Exception ex){
    			CommonLogger.error("date parse error,ex", ex);
    		}
    	}
    	return false;
    }
    
    public static long getTimesSecond(Date beginTime,Date endTime){
    	return (endTime.getTime() - beginTime.getTime())/1000;
    }
    
    public static String getWeekOfYear(){
    	Calendar calendar = Calendar.getInstance();
    	int year = calendar.get(Calendar.YEAR);
    	return year+""+calendar.get(Calendar.WEEK_OF_YEAR);
    }
    
    public static String getLastWeekOfYear(){
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.DATE, -7);
    	int year = calendar.get(Calendar.YEAR);
    	return year+""+calendar.get(Calendar.WEEK_OF_YEAR);
    }
    
    public static String getLastNWeekOfYear(int lastNWeek){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7 * lastNWeek);
        int year = calendar.get(Calendar.YEAR);
        return year+""+calendar.get(Calendar.WEEK_OF_YEAR);
    }
    
    //calendar 中 month 范围 [0,11]
    public static String getMonthOfYear(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        return year + "" + (calendar.get(Calendar.MONTH) + 1);
    }
    //calendar 中 month 范围 [0,11]    
    public static String getLastMonthOfYear(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        int year = calendar.get(Calendar.YEAR);
        return year + "" + (calendar.get(Calendar.MONTH) + 1);
    }

    public static String getTimeByDay(Integer day) {
        if (day == -1) {
            return  null;
        }
        SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, 0 - day);
        return simpleDateFormat.format(c.getTime());
    }
    
//    public static void main(String[] args) {
//        System.out.println(getWeekOfYear());
//    }
}
