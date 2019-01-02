package com.example.boot.util;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE = "yyyy-MM-dd";
	public static final String TIME = "HH:mm:ss";

	public static SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss");
	public static SimpleDateFormat yyyy_MM_ddHH_mm_ss= new SimpleDateFormat(DATE_TIME);
	public static SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat(DATE);
	public static SimpleDateFormat yyyyMMddHHMMss = new SimpleDateFormat("yyyyMMddHHMMss");
	public static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

	public static String hHmmSS(Date date){
		return yyyy_MM_ddHH_mm_ss.format(date).replaceAll("-","").
				replaceAll(":","").replaceAll(" ","").substring(8);
	}
    public static String yyYYmMddHHmmSS(Date date){
		return yyyy_MM_ddHH_mm_ss.format(date).replaceAll("-","").
				replaceAll(":","").replaceAll(" ","");
	}



	public static String getLeftTime(Date now,Date before){
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = now.getTime() - before.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		long sec = diff % nd % nh % nm / ns;
		return String.format("%s天%s小时%s分钟%s秒",day,hour,min,sec);
	}

	public static String getLeftDay(Date now,Object before){
		Calendar calendar = Calendar.getInstance();
		if (before instanceof Date){
			calendar.setTime((Date)before);
		}
		if (before instanceof Long){
			calendar.setTimeInMillis((Long)before);
		}
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		if (calendar.getTime().after(now)){
			return "穿越了";
		}
		long nd = 1000 * 24 * 60 * 60;
		// 获得两个时间的毫秒时间差异
		long diff = now.getTime() - calendar.getTime().getTime();
		// 计算差多少天
		long day = diff / nd;
		String leftDay;
		if (day == 0){
			leftDay = "今天";
		} else if (day == 1){
			leftDay = "昨天";
		}else {
			leftDay = String.format("%s天前",day);
		}
		return leftDay;
	}

	//计算两天之间的间隔
	public static int getIntervalDay(long start,long end){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(start);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		long nd = 1000 * 24 * 60 * 60;
		// 获得两个时间的毫秒时间差异
		long diff = end - calendar.getTime().getTime();
		// 计算差多少天
		long day = diff / nd;
		return Integer.parseInt(String.valueOf(day))+1;
	}

    //返回禁用剩余时间
	public static String getForddienTip(Date now,Date unForbiddenDate){
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
//		long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = unForbiddenDate.getTime() - now.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
//		long sec = diff % nd % nh % nm / ns;
		return String.format("已禁用(剩余%s天%s小时%s分钟)",day,hour,min);
	}

	//返回周几
	public static String getWeekOfMonth(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch (calendar.get(Calendar.DAY_OF_WEEK)){
			case 1:
				return "周日";
			case 2:
				return "周一";
			case 3:
				return "周二";
			case 4:
				return "周三";
			case 5:
				return "周四";
			case 6:
				return "周五";
			case 7:
				return "周六";
			default:
				return "周伯通";
		}
	}
	//返回周几
	public static String getWeekOfMonth(Calendar calendar){
		switch (calendar.get(Calendar.DAY_OF_WEEK)){
			case 1:
				return "周日";
			case 2:
				return "周一";
			case 3:
				return "周二";
			case 4:
				return "周三";
			case 5:
				return "周四";
			case 6:
				return "周五";
			case 7:
				return "周六";
			default:
				return "周伯通";
		}
	}

	//date转LocalDateTime
	public static LocalDateTime dateToLocalDateTime(Date date){
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	//LocalDateTime转date
	public static Date localDateTimeToDate(LocalDateTime localDateTime){
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

}
