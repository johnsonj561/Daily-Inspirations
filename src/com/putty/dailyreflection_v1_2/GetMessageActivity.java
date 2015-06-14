package com.putty.dailyreflection_v1_2;

import java.util.Calendar;
import java.util.Random;
import android.support.v7.app.ActionBarActivity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity responsible for displaying daily message and corresponding image.
 */
public class GetMessageActivity extends ActionBarActivity 
	implements TimePickerDialogFragment.OnTimeEnteredListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_message);

		//provide access to sharedPreference data
		sharedPreferences = getApplicationContext().getSharedPreferences(user_preferences, MODE_PRIVATE);
		editor = sharedPreferences.edit();

		//initialize dates
		today = Calendar.getInstance();
		dailyAlarm = Calendar.getInstance();
		
		//initialize random generator
		generator = new Random();

		//link background container
		background = findViewById(R.id.container);
		
		//link TextView
		messageView = (TextView) findViewById(R.id.messageTextView);

		//restore alarmHour and alarmMinute from memory
		//if no alarm is stored in memory it defaults to current time
		alarmHour = sharedPreferences.getInt("alarmHour", today.get(Calendar.HOUR_OF_DAY));
		alarmMinute = sharedPreferences.getInt("alarmMinute", today.get(Calendar.MINUTE));
		
		
		//if savedInstance is available - use same image and quote
		if(savedInstanceState != null){	
			backgroundImage = savedInstanceState.getInt("backgroundImage");
			background.setBackgroundResource(myBackgrounds[backgroundImage]);
			dailyMessage = savedInstanceState.getString("dailyMessage");
			messageView.setText(dailyMessage);
			messagesUsed = savedInstanceState.getBooleanArray("messagesUsed");
		}
		
	
		//else application must re-fresh resources
		else{
			
			//load array with quotes
			dailyMessageArray = getResources().getStringArray(R.array.reflectionQuotes);
			//initialize messagesUsed[] to all false values (false = not used)
			messagesUsed = new boolean[dailyMessageArray.length];
			for(int i = 0; i < messagesUsed.length; i++){
				messagesUsed[i] = false;
			}
			//load messagesUsed values that have been stored in sharedPreferences
			loadMessagesUsed();
		
			//load background image and set display
			backgroundImage = loadBackgroundImage();
		
			//load new message and display text
			dailyMessage = getMessage();
			messageView.setText(dailyMessage);	
			
		}
		
		
		
	}
	
	
	/**
	 * Store persistent data when life cycle terminates
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		today = Calendar.getInstance();
		Log.i("getMessage", "onDestroy()");
		Log.i("getMessage", "Storing current date in preferences" + 
				today.get(Calendar.MONTH) + "/" + today.get(Calendar.DAY_OF_MONTH) + "/" +
				today.get(Calendar.YEAR));
		editor.putInt("storedDayOfMonth", today.get(Calendar.DAY_OF_MONTH));
		editor.putInt("storedMonthOfYear", today.get(Calendar.MONTH));
		editor.putInt("storedYear", today.get(Calendar.YEAR));
		editor.putInt("backgroundImage", backgroundImage);
		editor.putString("dailyMessage", dailyMessage);
		editor.putInt("alarmHour", alarmHour);
		editor.putInt("alarmMinute", alarmMinute);
		editor.commit();
	}
	
	
	/**
	 * Save all data needed for life cycle interrupts
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBooleanArray("messagesUsed", messagesUsed);
		outState.putString("dailyMessage", dailyMessage);
		outState.putStringArray("dailyMessageArray", dailyMessageArray);
		outState.putInt("backgroundImage", backgroundImage);
	}
	
	
	/**
	 * Create options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_message, menu);
		return true;
	}

	
	/**
	 * Handle options menu selections
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_exitapp){
			System.exit(0);
			return true;
		}
		if (id == R.id.action_notificationAlarm){
			showTimeDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * @return dailyMessage that has not already been marked as used
	 */
	private String getMessage(){	
		
		//if it's a new day -> select a random message that has not been used
		if(isNewDay()){
			int q;
			do{
				q = (generator.nextInt(dailyMessageArray.length) * 51) % dailyMessageArray.length;
				Log.i(TAG, "Selecting Quote # " + q);;
				Log.i(TAG, "messagesUsed[" + q + "] = " + messagesUsed[q]);
			}while(messagesUsed[q]);
			markMessageUsed(q);
			Log.i(TAG, "messagesUsed[" + q + "] = " + messagesUsed[q]);
			return dailyMessageArray[q];
		}
		
		//if it's not a new day - load the message stored in preferences
		//if there is an error loading stored dailyMessage, a random message will be selected
		else{
			Log.i(TAG, "Selecting stored quote from sharedPreferences");
			return sharedPreferences.getString("dailyMessage", 
					dailyMessageArray[generator.nextInt(dailyMessageArray.length)]);
		}
		
	}

	/**
	 * Load background image
	 */
	private int loadBackgroundImage(){
		int b;
		//if it's a new day -> select a random image to display
		if(isNewDay()){
			b = generator.nextInt(myBackgrounds.length);
			background.setBackgroundResource(myBackgrounds[b]);
			Log.i(TAG, "isNewDay() - Background set to image # " + b);
			return b;
		}
		else{
			b = sharedPreferences.getInt("backgroundImage", generator.nextInt(myBackgrounds.length));
			background.setBackgroundResource(myBackgrounds[b]);
			Log.i(TAG, "!isNewDay() - Background set to image # " + b);
			return b;
		}
		
	}
	
	
	/**
	 * Update value of messagesUsed[] at index 'i' to 'true'
	 * @param i Index of the message being updated
	 */
	private void markMessageUsed(int i){
		messagesUsed[i] = true;
	}

	
	/**
	 * Compares current date with the date stored in user_preferences
	 * @return true if current date does not equal saved date
	 */
	public Boolean isNewDay(){
		if(sharedPreferences.contains("lastYear")){
			if((today.get(Calendar.DAY_OF_MONTH) != sharedPreferences.getInt("storedDayOfMonth", -1)) ||
					(today.get(Calendar.MONTH) != sharedPreferences.getInt("storedMonthOfYear", -1)) ||
					(today.get(Calendar.YEAR) != sharedPreferences.getInt("storedYear", -1))){
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
	
	/**
	 * Store the saved messages array in sharedPreferences
	 */
	public void saveMessagesUsed(){
		Log.i("getMessage", "storeMessagesUsed");
		for(int i = 0; i < messagesUsed.length; i ++){
			editor.putBoolean("messagesUsed" + i, messagesUsed[i]);
		}
	}
	
	/**
	 * Load the saved messages array from sharedPreferences
	 */
	public void loadMessagesUsed(){
		Log.i("getMessage", "loadMessagesUsed");
		for(int i = 0; i < messagesUsed.length; i++){
			messagesUsed[i] = sharedPreferences.getBoolean("messagesUsed" + i, false);
		}
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
		Intent alarmIntent = new Intent(GetMessageActivity.this, MessageAlarm.class);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		dailyAlarm.set(Calendar.HOUR_OF_DAY, alarmHour);
		dailyAlarm.set(Calendar.MINUTE, alarmMinute);
		long time = dailyAlarm.getTimeInMillis();
		long interval = AlarmManager.INTERVAL_DAY;
		if(today.get(Calendar.HOUR_OF_DAY) > dailyAlarm.get(Calendar.HOUR_OF_DAY)){
			time += interval;
		}
		else if(today.get(Calendar.HOUR_OF_DAY) == dailyAlarm.get(Calendar.HOUR_OF_DAY)
			&& today.get(Calendar.MINUTE) >= dailyAlarm.get(Calendar.MINUTE)){
			time += interval;
		}
		
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,time, interval, 
        		PendingIntent.getBroadcast(this,1,  alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT));	
	}
	
	
	private int alarmHour;
	private int alarmMinute;
	private Calendar today;
	private Calendar dailyAlarm;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	private final String TAG = "GetMessage.java";
	private final String user_preferences = "private_preferences";
	private View background;
	private TextView messageView;
	private Random generator;
	private String dailyMessage;
	private int backgroundImage;
	private String[] dailyMessageArray;
	private boolean[] messagesUsed;
	
	//array of integer values representing the 11 different background images available
	//each drawable object is available in standard and landscape to enhance appearance
	private int[] myBackgrounds = {R.drawable.hill1, R.drawable.meadow3, R.drawable.mountain1, R.drawable.rain1,
					R.drawable.rain2, R.drawable.sunset1, R.drawable.sunset2, R.drawable.sunset3, R.drawable.water1,
					R.drawable.water2, R.drawable.woods1};
}
