package com.putty.dailyreflection_v1_2;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SobrietyCounterActivity extends Activity implements 
	DatePickerDialogFragment.OnDateEnteredListener{

	/**
	 * onCreate()
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sobriety_counter);

		//DayCounter object will calculate sobriety time
		myTimeCounter = new TimeCounter();		
		
		//initialize sharedPreferences storage
		sharedPreferences = getApplicationContext().getSharedPreferences(user_preferences, MODE_PRIVATE);
		editor = sharedPreferences.edit();
		
		//load stored sobriety date, default to today's date if no date in storage
		sobrietyDate = Calendar.getInstance();
		sobrietyDate.set(Calendar.DAY_OF_MONTH, sharedPreferences.getInt("sobrietyDayOfMonth", 
				sobrietyDate.get(Calendar.DAY_OF_MONTH)));
		sobrietyDate.set(Calendar.MONTH, sharedPreferences.getInt("sobrietyMonthOfYear", 
				sobrietyDate.get(Calendar.MONTH)));
		sobrietyDate.set(Calendar.YEAR, sharedPreferences.getInt("sobrietyYear", 
				sobrietyDate.get(Calendar.YEAR)));
		
		
		//link time text view
		timeTextView = (TextView) findViewById(R.id.counterText);
		
		
		changeDateButton = (Button) findViewById(R.id.changeDateButon);
		changeDateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDateDialog();
			}
		});
		
		//link spinner and set adapter
		timeSpinner = (Spinner) findViewById (R.id.counterOptions);
		setupSpinnerAdapter();
		timeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				 if(timeSpinner.getItemAtPosition(position).equals("Days")){
			    	 Toast.makeText(getApplicationContext(), "DAYS", Toast.LENGTH_LONG).show();
			    	 refreshTimeTextView(0);
			     }
				 if(timeSpinner.getItemAtPosition(position).equals("Seconds")){
			    	 Toast.makeText(getApplicationContext(), "SECONDS", Toast.LENGTH_LONG).show();
			    	 refreshTimeTextView(1);
			     }
				 if(timeSpinner.getItemAtPosition(position).equals("Minutes")){
			    	 Toast.makeText(getApplicationContext(), "MINUTES", Toast.LENGTH_LONG).show();
			    	 refreshTimeTextView(2);
			     }
				 if(timeSpinner.getItemAtPosition(position).equals("Hours")){
			    	 Toast.makeText(getApplicationContext(), "HOURS", Toast.LENGTH_LONG).show();
			    	 refreshTimeTextView(3);
			     }
			     if(timeSpinner.getItemAtPosition(position).equals("Years")){
			    	 Toast.makeText(getApplicationContext(), "YEARS", Toast.LENGTH_LONG).show();
			    	 refreshTimeTextView(4);
			     }    
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				//do nothing
			}
			
		});
	}
	
	/**
	 * Save all data when life cycle interrupts
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("myTime", myTime);
		outState.putInt("sobrietyYear", sobrietyYear);
		outState.putInt("sobrietyMonthOfYear", sobrietyMonthOfYear);
		outState.putInt("sobrietyDayOfMonth", sobrietyDayOfMonth);
	}

	
	/**
	 * Sets and displays options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sobriety_counter, menu);
		return true;
	}

	
	/**
	 * Handles menu options selection
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
		return super.onOptionsItemSelected(item);
	}

	
	/**
	 * Link array of counter options to adapter and set adapter to Spinner object
	 */
	public void setupSpinnerAdapter(){
		spinnerAdapter = ArrayAdapter.createFromResource(this,
		        R.array.counter_options_array, android.R.layout.simple_spinner_item);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeSpinner.setAdapter(spinnerAdapter);
	}
	

	/**
	 * Update SobrietyTime TextView 
	 * @param i
	 */
	public void refreshTimeTextView(int i){
		switch(i){
		case 0:
			formatter = new DecimalFormat("#,###");
			formatter.setRoundingMode(RoundingMode.DOWN);
			timeTextView.setText(formatter.format(myTimeCounter.getDaysDifference(sobrietyDate)));
			break;
		case 1:
			formatter = new DecimalFormat("#,###");
			formatter.setRoundingMode(RoundingMode.DOWN);
			timeTextView.setText(formatter.format(myTimeCounter.getSecondsDifference(sobrietyDate)));
			break;
		case 2:
			formatter = new DecimalFormat("#,###");
			formatter.setRoundingMode(RoundingMode.DOWN);
			timeTextView.setText(formatter.format(myTimeCounter.getMinutesDifference(sobrietyDate)));
			break;
		case 3:
			formatter = new DecimalFormat("#,###");
			formatter.setRoundingMode(RoundingMode.DOWN);
			timeTextView.setText(formatter.format(myTimeCounter.getHoursDifference(sobrietyDate)));
			break;
		case 4:
			formatter = new DecimalFormat("#.##");
			formatter.setRoundingMode(RoundingMode.DOWN);
			timeTextView.setText(formatter.format(myTimeCounter.getYearsDifference(sobrietyDate)));
			break;
		default:
			timeTextView.setText(0 +"");
			break;
		}
	}
	
	
	/**
	 * Display date dialog fragment to userr.
	 */
	private void showDateDialog(){
		FragmentManager fragManager = getFragmentManager();
		datePicker = new DatePickerDialogFragment();
		datePicker.setDate(sobrietyDate);
		datePicker.show(fragManager, "date_dialog_fragment");
	}

	
	/**
	 * Handles DatePickerDialog call back by updating sobriety date with selection
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 */	
	@Override
	public void OnDateEntered(int year, int monthOfYear, int dayOfMonth) {
		sobrietyDate.set(Calendar.YEAR, year);
		sobrietyDate.set(Calendar.MONTH, monthOfYear);
		sobrietyDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);		
		timeSpinner.setSelection(0);
		refreshTimeTextView(0);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();	
		Log.i("sobrietyCounter", "Storing sobriety date");
		editor.putInt("sobrietyDayOfMonth", sobrietyDate.get(Calendar.DAY_OF_MONTH));
		editor.putInt("sobrietyMonthOfYear", sobrietyDate.get(Calendar.MONTH));
		editor.putInt("sobrietyYear", sobrietyDate.get(Calendar.YEAR));		
		editor.commit();
	}
	
	
	private DatePickerDialogFragment datePicker;
	private DecimalFormat formatter;
	private Calendar sobrietyDate;
	private int sobrietyDayOfMonth;
	private int sobrietyMonthOfYear;
	private int sobrietyYear;
	private String myTime;
	private TimeCounter myTimeCounter;
	private Button changeDateButton;
	private Spinner timeSpinner;
	private TextView timeTextView;
	private ArrayAdapter<CharSequence> spinnerAdapter;
	private final String user_preferences = "private_preferences";
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;

}
