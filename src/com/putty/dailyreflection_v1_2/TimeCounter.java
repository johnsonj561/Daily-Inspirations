package com.putty.dailyreflection_v1_2;
import java.util.Calendar;

public class TimeCounter {
	
	
	/**
	 * Calculates the difference in seconds between two dates
	 * @param day1
	 * @param day2
	 * @return long representation of the difference in seconds
	 */
	public long getSecondsDifference(Calendar day1){
		tempTime = Calendar.getInstance();
		long diffInMilliSec = tempTime.getTimeInMillis() - day1.getTimeInMillis();
		return (diffInMilliSec)/(1000);
	}
	
	
	/**
	 * Calculates the difference in minutes between two dates
	 * @param day1
	 * @param day2
	 * @return integer representation of the difference in minutes
	 */
	public long getMinutesDifference(Calendar day1){
		tempTime = Calendar.getInstance();
		long diffInMilliSec = tempTime.getTimeInMillis() - day1.getTimeInMillis();
		return (diffInMilliSec)/(1000*60);
	}
	
	
	/**
	 * Calculates the difference in hours between two dates
	 * @param day1
	 * @param day2
	 * @return integer representation of the difference in minutes
	 */
	public long getHoursDifference(Calendar day1){
		tempTime = Calendar.getInstance();
		long diffInMilliSec = tempTime.getTimeInMillis() - day1.getTimeInMillis();
		return (diffInMilliSec)/(1000*60*60);
	}
	
	
	/**
	 * Calculates the difference in days between two dates
	 * @param day1
	 * @param day2
	 * @return The difference = day1 - day2
	 */
	public int getDaysDifference(Calendar day1){
		tempTime = Calendar.getInstance();
		long diffInMilliSec = tempTime.getTimeInMillis() - day1.getTimeInMillis();
		return (int) ((diffInMilliSec) / (1000 * 60 * 60 * 24));    
	}

	
	/**
	 * Calculates the difference in years between two dates
	 * @param day1
	 * @param day2
	 * @return double representation of the difference in years between day1 and day2
	 */
	public float getYearsDifference(Calendar day1){
		tempTime = Calendar.getInstance();
		long diffInMilliSec = tempTime.getTimeInMillis() - day1.getTimeInMillis();
		float diffInYears = (float) ((diffInMilliSec) / (milliSecInYear));
		return diffInYears;
	}
	
	
	Calendar tempTime;
	final double milliSecInYear = 3155695.2E4; 
}
