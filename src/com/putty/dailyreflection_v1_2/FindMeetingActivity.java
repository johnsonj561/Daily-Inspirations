package com.putty.dailyreflection_v1_2;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Class designed to search device's default web browser for AA and NA meetings.
 * User input or device location is used as search criteria.
 */
public class FindMeetingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_meeting);
		
		//set up View elements
		stateEditText = (EditText) findViewById(R.id.enterStateEditText);
		cityEditText = (EditText) findViewById(R.id.enterCityEditText);
		currentLocationCheckBox = (CheckBox) findViewById(R.id.useCurrentLocationCheckBox);
		currentLocationCheckBox.setClickable(false);
		
		//flag used to prevent displaying device location on every location update
		locationFlag = 0;
		
		//start searching for device's locaton
		startLocationService();
		
		if(savedInstanceState != null){
			stateString = savedInstanceState.getString("stateString");
			cityString = savedInstanceState.getString("cityString");
			stateEditText.setText(stateString);
			cityEditText.setText(cityString);
		}
		

		//Link AA and NA meeting buttons
		findNAButton = (Button) findViewById(R.id.findNAButton);
		findNAButton.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				launchNASearch();
				
			}
		});	
		findAAButton = (Button) findViewById(R.id.findAAButton);
		findAAButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				launchAASearch();
			}
		});
	}

	
	/**
	 * Save data to outState bundle on Activity pause
	 * @param outState Bundle used to store data
	 */
	protected void onSavedInstanceState(Bundle outState){
		cityString = cityEditText.getText().toString();
		stateString = stateEditText.getText().toString();
		outState.putString("cityString", cityString);
		outState.putString("stateString", stateString);
	}
		
	
	/**
	 * create menu options
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_meeting, menu);
		return true;
	}

	
	/**
	 * handle menu selections
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
	 * Collect data from EditText fields and use to search web for NA meeting.
	 */
	public void launchNASearch(){
		stateString = stateEditText.getText().toString();
		cityString = cityEditText.getText().toString();
		if(currentLocationCheckBox.isChecked()){
			Intent i = new Intent(Intent.ACTION_WEB_SEARCH); 
			i.putExtra(SearchManager.QUERY, naMeeting + " " + postalCode);
			startActivity(i);
		}
		else if(!(stateString.equals("")) || !(cityString.equals(""))){
			Intent i = new Intent(Intent.ACTION_WEB_SEARCH); 
			i.putExtra(SearchManager.QUERY, cityString + " " + stateString +
					" " + naMeeting);
			startActivity(i);
		}
		else{
			Toast.makeText(getApplicationContext(), "Enter City & State First", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * Collect data from EditText fields and use to search web for AA meeting.
	 */
	public void launchAASearch(){
		stateString = stateEditText.getText().toString();
		cityString = cityEditText.getText().toString();
		if(currentLocationCheckBox.isChecked()){
			Intent i = new Intent(Intent.ACTION_WEB_SEARCH); 
			i.putExtra(SearchManager.QUERY, aaMeeting + " " + postalCode);
			startActivity(i);
		}
		else if(!(stateString.equals("")) || !(cityString.equals(""))){
			Intent i = new Intent(Intent.ACTION_WEB_SEARCH); 
			i.putExtra(SearchManager.QUERY, cityString + " " + stateString +
					" " + aaMeeting);
			startActivity(i);
		}
		else{
			Toast.makeText(getApplicationContext(), "Please select your location", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	/**
	 * Return devices current location in String representation.
	 */
	public String getCurrentLocation(){
		return postalCode;
	}
	
	

	/**
	 * Start location services
	 */
	public void startLocationService(){
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
			// Called when a new location is found by the network location provider.
			try {
				makeUseOfNewLocation(location);
			} catch (IOException e) {e.printStackTrace();}
		    }
			public void onStatusChanged(String provider, int status, Bundle extras) {}
			public void onProviderEnabled(String provider) {}
			public void onProviderDisabled(String provider) {}};
			
			// Register the listener with the Location Manager to receive location updates
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);		
	}
	
	
	/**
	 * Handle new location updates
	 * @param location
	 * @throws IOException 
	 */
	public void makeUseOfNewLocation(Location location) throws IOException{	
		Geocoder geo = new Geocoder(getApplicationContext(), Locale.getDefault());
		postalCode = "Error";
		if(!Geocoder.isPresent()){
			Toast.makeText(getApplicationContext(), "Location Services Not Available", Toast.LENGTH_SHORT).show();
		}
		else{
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
			if(addresses.size() > 0){
				postalCode = addresses.get(0).getPostalCode();
			}			
			//if device is located - display message to user and make checkbox clickable
			if(locationFlag==0){
				Toast.makeText(getApplicationContext(), "Device Located", Toast.LENGTH_SHORT).show();
				locationFlag = 1;
			}
			currentLocationCheckBox.setClickable(true);
		}
	}
	
	
	private int locationFlag;
	private String postalCode;
	private Button findNAButton;
	private Button findAAButton;
	private EditText cityEditText;
	private String cityString;
	private String stateString;
	private EditText stateEditText;
	private CheckBox currentLocationCheckBox;
	private final String aaMeeting = "Alcoholics Anonymous meeting";
	private final String naMeeting = "Narcotics Anonymous meeting";
	
	
	
}
