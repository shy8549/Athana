package com.nsn.bighead.glassfish.filterse.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.nokia.sai.stream.core.constants.Constants;
import com.nokia.sai.stream.core.exception.AppException;
import com.nokia.sai.stream.core.util.DateTimeUtil;

public class ReportTime2DateUtil {
	/**
	 * reportTime 字段处理 dateFormatType : 10=秒数, 11=毫秒数
	 */
	public static Date translateReportTime2Date(String reportTime, int dateFormatType) throws AppException {
		SimpleDateFormat formatter = null;
		Date reportDate = null;
		switch (dateFormatType) {
		case 1:
			formatter = DateTimeUtil.time_format.get();
			break;
		case 2:
			formatter = DateTimeUtil.rtt_format.get();
			break;
		case 3:
			formatter = DateTimeUtil.time_format_slash.get();
			break;
		case 4:
			formatter = DateTimeUtil.rtt_format_slash.get();
			break;
		case 5:
			formatter = DateTimeUtil.day14_format.get();
		}
		try {
			if (formatter != null) {
				reportDate = formatter.parse(reportTime);
			}
			// 秒 或 毫秒处理
			else if (dateFormatType == 10 | dateFormatType == 11) {
				Calendar c = DateTimeUtil.calendar.get();
				long millTime = dateFormatType == 10 ? Long.parseLong(reportTime) * 1000 : Long.parseLong(reportTime);
				c.setTimeInMillis(millTime);
				reportDate = c.getTime();
			}else if(dateFormatType == 12){
				//微秒数 addby lvhuabing 20170906
				Calendar c = DateTimeUtil.calendar.get();
				long millTime = Long.parseLong(reportTime) / 1000;
				c.setTimeInMillis(millTime);
				reportDate = c.getTime();
			}
		} catch (Exception e) {
			throw new AppException("Inputer : translateReportTime reportTime=" + reportTime + ",formatterType=" + dateFormatType + " error", e);
		}
		return reportDate;
	}
	
	
	/**
	 * @return
	 * <!-- 值：含义,1:1min,5:5min,T:10min,Q:15min,F:30min,H:hour,D:day,W:week,M:month,Y:year -->
	 */
	public static Long getSliceTime(Date date ,String sliceType){
		Long sliceTime =Long.valueOf(DateTimeUtil.long_minute_format.get().format(date));
		int key = 1;
		switch (sliceType){
		case	"1":
			break;
		case	"5":
			key =5;
			sliceTime = getSliceTimeByType(sliceTime,key);
			break;
		case	"T":
			key =10;
			sliceTime = getSliceTimeByType(sliceTime,key);
			break;
		case	"Q":
			key =15;
			sliceTime = getSliceTimeByType(sliceTime,key);
			break;
		case	"F":
			key =30;
			sliceTime = getSliceTimeByType(sliceTime,key);
			break;
		case	"H":
			sliceTime = sliceTime/100;
			break;
		case	"D":
			sliceTime = sliceTime/10000;
			break;
		case	"W":
			sliceTime = DateTimeUtil.getWeekTimeSlice(sliceTime);
			break;
		case	"M":
			sliceTime = sliceTime/1000000;
			break;
		case	"Y":
			sliceTime = sliceTime/100000000;
			break;
		}
		return sliceTime;
	}

	/**
	 * @param sliceTime 格式： yyyyMMddHHmmss 一分钟粒度
	 * @return
	 * <!-- 值：含义,1:1min,5:5min,T:10min,Q:15min,F:30min,H:hour,D:day,W:week,M:month,Y:year -->
	 */
	public static Long getSliceTime(Long sliceTime ,String sliceType){
		int key = 1;
		switch (sliceType){
		case	"1":
			break;
		case	"5":
			key =5;
			sliceTime = getSliceTimeByType(sliceTime,key);
			break;
		case	"T":
			key =10;
			sliceTime = getSliceTimeByType(sliceTime,key);
			break;
		case	"Q":
			key =15;
			sliceTime = getSliceTimeByType(sliceTime,key);
			break;
		case	"F":
			key =30;
			sliceTime = getSliceTimeByType(sliceTime,key);
			break;
		case	"H":
			sliceTime = sliceTime/100;
			break;
		case	"D":
			sliceTime = sliceTime/10000;
			break;
		case	"W":
			sliceTime = DateTimeUtil.getWeekTimeSlice(sliceTime);
			break;
		case	"M":
			sliceTime = sliceTime/1000000;
			break;
		case	"Y":
			sliceTime = sliceTime/100000000;
			break;
		}
		return sliceTime;
	}
	
	/**
	 * 通过毫秒值的时间戳获取时间片
	 * @param sliceTime -- timestamp格式的时间即毫秒值
	 * @param sliceType -- 时间类型
	 * MS:毫秒值,S:秒值,1:1min,5:5min,T:10min,Q:15min,F:30min,H:hour,D:day,W:week,M:month,Y:year
     */
	public static long getSliceTimeByTimestamp(Long sliceTime ,String sliceType) throws Exception{
		if("MS".equals(sliceType)){
			return sliceTime;
		}
		if("S".equals(sliceType)){
			return Long.parseLong(DateTimeUtil.day14_format.get().format(new Date(sliceTime)));
		}else{
			return getSliceTime(new Date(sliceTime), sliceType);
		}
	}

	private static Long getSliceTimeByType(Long sliceTime,int key) {
		long slice = sliceTime;
		long hour = slice/100;
		long min = slice - (hour * 100);
		if(min % key != 0){
			StringBuilder sb = Constants.getLocalStringBuilder();
			sb.append(hour);
			long mm = key * (min / key);
			if(mm < 10){
				sb.append("0").append(mm);
			} else{
				sb.append(mm);
			}
			sliceTime = Long.parseLong(sb.toString());
			sb = null;
		}
		return sliceTime;
	}
	
	public static String getSliceType(String type) throws Exception{
		switch (type) {
		case "[MS]":
			return "MS";
		case "[S]":
			return "S";
		case "[1]":
			return "1";
		case "[5]":
			return "5";
		case "[T]":
			return "T";
		case "[Q]":
			return "Q";
		case "[F]":
			return "F";
		case "[H]":
			return "H";
		case "[D]":
			return "D";
		case "[W]":
			return "W";
		case "[M]":
			return "M";
		case "[Y]":
			return "Y";
		default:
			return "";
		}
	}
	
	public static void main(String[] args){
		Date d = new Date();
		try {
			System.err.println(d.getTime());
			long slice = d.getTime();
			long a = getSliceTimeByTimestamp(slice, "D");
			System.err.println("a="+a);
			long b = getSliceTimeByTimestamp(slice, "H");
			System.err.println("b="+b);
			System.err.println("slice="+slice);
//			d= translateReportTime2Date("1470201095610",11);
			d= translateReportTime2Date("2019-5-12 0:54:3",1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println(d.getTime());
	}
}
