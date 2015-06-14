package com.putty.dailyreflection_v1_2;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
 
/**
 * Class that is activated on device boots. Notification alarms are reset to most current values.
 */
public class OnReboot extends BroadcastReceiver {

	/**
	 * onReceive() is called when the device completes boot.
	 */
	@Override
	public void onReceive(Context context, Intent i) {
		today = Calendar.getInstance();
		dailyAlarm = Calendar.getInstance();
		//sharedPreferences contains stored daily alarm values
		//retrieve stored alarm values and assign them to dailyAlarm
		sharedPreferences = context.getSharedPreferences(user_preferences, Context.MODE_PRIVATE);
		alarmHour = sharedPreferences.getInt("alarmHour", today.get(Calendar.HOUR_OF_DAY));
		alarmMinute = sharedPreferences.getInt("alarmMinute", today.get(Calendar.MINUTE));
		dailyAlarm.set(Calendar.HOUR_OF_DAY, alarmHour);
		dailyAlarm.set(Calendar.MINUTE, alarmMinute);
		time = dailyAlarm.getTimeInMillis();
		interval = AlarmManager.INTERVAL_DAY;
		scheduleAlarms(context);
	}
	
	/**
	 * Utilizes dailyAlarm values to re-set the application's notification alarm
	 * @param context
	 */
	public void scheduleAlarms(Context context){
		Intent alarmIntent = new Intent(context, MessageAlarm.class);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		//if the time of day is past the alarm, then add 1 day to the time that is to be used for setting alarm
		//this is necessary to prevent alarm from going off when it's not supposed to - otherwise alarm will trigger every time 
		//the device turns on and the dailyAlarm time is already past for that day
		if(today.get(Calendar.HOUR_OF_DAY) > dailyAlarm.get(Calendar.HOUR_OF_DAY)){
			time += interval;
		}
		else if(today.get(Calendar.HOUR_OF_DAY) == dailyAlarm.get(Calendar.HOUR_OF_DAY)
			&& today.get(Calendar.MINUTE) >= dailyAlarm.get(Calendar.MINUTE)){
			time += interval;
		}
		
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, interval, 
        		PendingIntent.getBroadcast(context, 1,  alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));
	
	}

	
	private Calendar dailyAlarm;
	private Calendar today;
	private final String user_preferences = "private_preferences";	
	private SharedPreferences sharedPreferences;
	private int alarmHour;
	private int alarmMinute;
	private long time;
	private long interval;
}
