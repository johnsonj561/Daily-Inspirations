package com.putty.dailyreflection_v1_2;

import java.util.Calendar;
import android.support.v7.app.ActionBarActivity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Home page contains user's main menu, with links to all application features.
 */
public class HomePage extends ActionBarActivity
	implements TimePickerDialogFragment.OnTimeEnteredListener{

	/**
	 * Instantiates application objects and links all View elements
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);		

		//check to see if it's first time app is being used
		sharedPreferences = getApplicationContext().getSharedPreferences(user_preferences, MODE_PRIVATE);
		editor = sharedPreferences.edit();
		updateAppCounter();
		
		today = Calendar.getInstance();
		dailyAlarm = Calendar.getInstance();
		
		//restore alarmHour and alarmMinute from memory
		//if no alarm is stored in memory it defaults to current time
		alarmHour = sharedPreferences.getInt("alarmHour", today.get(Calendar.HOUR_OF_DAY));
		alarmMinute = sharedPreferences.getInt("alarmMinute", today.get(Calendar.MINUTE));
		
		//links getMessage button to GetMessageActivity
		getMessage = (Button) findViewById(R.id.getMessageButton);
		getMessage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(HomePage.this, GetMessageActivity.class);
				startActivity(i);
			}
		});
		
		
		//link findMeeting button
		findMeeting = (Button) findViewById(R.id.findMeetingButton);
		findMeeting.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(HomePage.this, FindMeetingActivity.class);
				startActivity(i);
			}
		});
		
		
		//links sobrietyCounter button to SobrietyCounterActivity
		sobrietyCounter = (Button) findViewById(R.id.sobrietyCounterButton);
		sobrietyCounter.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(HomePage.this, SobrietyCounterActivity.class);
				startActivity(i);
			}
		});
		
		
		//link viewLiterature button to ViewLiteratureActivity
		viewLiterature = (Button) findViewById(R.id.literatureButton);
		viewLiterature.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(HomePage.this, ViewLiteratureActivity.class);
				startActivity(i);
			}
		});
		
		
		//links find help button to web browser
		findHelp = (Button) findViewById(R.id.getHelpButton);
		findHelp.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				openAmbWebLink();
			}
		});
	}
	
	
	/**
	 * Store persistent data when life cycle terminates
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		counter++;
		Log.i("counter", "Storing counter in preferences: " + counter);
		Log.i("calendar", "Storing current date in preferences" + 
				today.get(Calendar.MONTH) + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" +
				today.get(Calendar.YEAR));
		editor.putInt("counter", counter);
		editor.putInt("lastDay", today.get(Calendar.DAY_OF_MONTH));
		editor.putInt("lastMonth", today.get(Calendar.MONTH));
		editor.putInt("lastYear", today.get(Calendar.YEAR));
		editor.putInt("alarmHour", alarmHour);
		editor.putInt("alarmMinute", alarmMinute);
		editor.commit();
	}
	
	
	/**
	 * Attaches menu options memu.home_page
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

	
	/**
	 * Handles menu selections.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_notificationAlarm){
			showTimeDialog();
			return true;
		}
		if (id == R.id.action_exitapp){
			System.exit(0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	/**
	 * Checks to see if application 'counter' has been established.
	 * Retrieves counter value from storage if established.
	 * Establishes counter value and stores to preferences if not already established.
	 */
	public void updateAppCounter(){
		if(sharedPreferences.contains("counter")){
			counter = sharedPreferences.getInt("counter", 0);
			Log.i("counter", "Counter = " + counter);
		}
		else{
			counter = 1;
			editor.putInt("counter", counter);
			Log.i("counter", "New Counter = " + counter);
			editor.commit();
		}
	}
	
	
	/**
	 * Compares current date with the date stored in user_preferences
	 * @return true if current date does not equal saved date
	 *//*
	public Boolean isNewDay(){
		if(sharedPreferences.contains("lastYear")){
			if((today.get(Calendar.DAY_OF_MONTH) != sharedPreferences.getInt("lastDay", -1)) ||
					(today.get(Calendar.MONTH) != sharedPreferences.getInt("lastMonth", -1)) ||
					(today.get(Calendar.YEAR) != sharedPreferences.getInt("lastYear", -1))){
				Log.i("Calendar",  "isNewDay() case 1 - returning true");
				return true;
			}
			else{
				Log.i("Calendar",  "isNewDay() case 2 - returning false");
				return false;
			}		
		}
		else{
			Log.i("Calendar",  "isNewDay() case 3 - returning true");
			return true;
		}
	}
	*/
	
	/**
	 * Launches Ambrosia Website
	 */
	public void openAmbWebLink(){
		String url = "http://www.ambrosiatreatmentcenter.com";
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}
	
	
	/**
	 * Display time dialog fragment to user
	 */
	public void showTimeDialog(){
		FragmentManager fragManager = getFragmentManager();
		TimePickerDialogFragment timeDialog = new TimePickerDialogFragment();
		timeDialog.show(fragManager, "time_dialog_fragment");
	}
	
	/**
	 * Handle the time dialog return call
	 * @param hour selected by user	
	 * @param minute selected by user
	 */
	@Override
	public void OnTimeEntered(int hour, int minute){
		Toast.makeText(getApplicationContext(), "Time Changed",
				Toast.LENGTH_LONG).show();
		alarmHour = hour;
		alarmMinute = minute;
		scheduleAlarm();
	}
	
	
	/**
	 * Sets the time for alarm event to occur.
	 */
	public void scheduleAlarm(){
		Intent alarmIntent = new Intent(HomePage.this, MessageAlarm.class);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		dailyAlarm.set(Calendar.HOUR_OF_DAY, alarmHour);
		dailyAlarm.set(Calendar.MINUTE, alarmMinute);
		long time = dailyAlarm.getTimeInMillis();
		long interval = AlarmManager.INTERVAL_DAY;
		
		//if dailyAlarm is past the current time of day, we add 1 day to dailyAlarm to prevent
		//the alarm from going off as soon as it is set. By default, if the time is past when an 
		//alarm is set, the AlarmManager will immediately trigger the Broadcast class. This prevents
		//alarm from going off un-intentionally
		if(today.get(Calendar.HOUR_OF_DAY) > dailyAlarm.get(Calendar.HOUR_OF_DAY)){
			time += interval;
		}
		else if(today.get(Calendar.HOUR_OF_DAY) == dailyAlarm.get(Calendar.HOUR_OF_DAY)
			&& today.get(Calendar.MINUTE) >= dailyAlarm.get(Calendar.MINUTE)){
			time += interval;
		}
		
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, interval, 
        		PendingIntent.getBroadcast(this,1,  alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));	
	}
	
	
	private int counter;
	private int alarmHour;
	private int alarmMinute;
	private Calendar dailyAlarm;
	private Calendar today;
	private Button getMessage;
	private Button viewLiterature;
	private Button findMeeting;
	private Button sobrietyCounter;
	private Button findHelp;
	private final String user_preferences = "private_preferences";
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
}
