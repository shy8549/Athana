package com.nsn.bighead.glassfish.filterse.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * MyDateUtil
 * @author Delen.Yin
 * @Created 2019年5月30日
 */
public class MyDateUtil {
	
	// lock 
	private static final Lock lock = new ReentrantLock();
	
	/**
	 * key = pattern; value = SimpleDateFormat
	 */
	private static final Map<String, ThreadLocal<SimpleDateFormat>> secureDateFormatMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();
	
	/**
	 * get SimpleDateFormat
	 * @param pattern 格式 
	 * @return SimpleDateFormat
     */
	public static SimpleDateFormat getDateFormat(final String pattern) throws Exception {
		ThreadLocal<SimpleDateFormat> localDataFormat = secureDateFormatMap.get(pattern);
		if(localDataFormat == null) {
			try {
				lock.lock();
				localDataFormat = secureDateFormatMap.get(pattern);
				if(localDataFormat == null) {
					localDataFormat = new ThreadLocal<SimpleDateFormat>() {
					    @Override 
					    protected SimpleDateFormat initialValue() {
					    	//TimeZone.setDefault(TimeZone.getTimeZone(zone));
                            return new SimpleDateFormat(pattern);
					    }
					};
					secureDateFormatMap.put(pattern, localDataFormat);
				}
			} catch (Exception e) {
				throw new Exception("MyDateUtil getDateFormat error : ",e);
			} finally {
				lock.unlock();
			}
		}
		return localDataFormat.get();
	}

	/**
	 * main
     */
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = MyDateUtil.getDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		System.out.println(sdf.format(new Date()));
		SimpleDateFormat sdf1 = MyDateUtil.getDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf1.setTimeZone(TimeZone.getTimeZone("GMT+6"));
		System.out.println(sdf1.format(new Date()));
	}
    
}

