package com.putty.dailyreflection_v1_2;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity that is responsible for displaying AA/NA literature text.
 * White background with vertical scroll to view text.
 */
public class DisplayLiteratureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_literature);

		
		//link literature TextView and handle lit selections
		literatureTitleView = (TextView) findViewById(R.id.literatureTitleTextView);
		literatureView = (TextView) findViewById(R.id.literatureTextView);
		literatureView.setMovementMethod(new ScrollingMovementMethod());
		//litSelection is stored as an extra in the Intent
		litSelection = getIntent().getExtras().getString("litSelection");
		
		//switch statement dispays the appropriate text based off of user selection
		switch(litSelection){
		case "aaPromises":
			literatureTitleView.setText(R.string.promises_text);
			literatureView.setText(R.string.aa_promises_lit);
			break;
		case "aaPreamble":
			literatureTitleView.setText(R.string.aa_preamble_text);
			literatureView.setText(R.string.aa_preamble_lit);
			break;
		case "aaTwelveTraditions":
			literatureTitleView.setText(R.string.traditions_text);
			literatureView.setText(R.string.aa_twelve_traditions_lit);
			break;
		case "serenityPrayer":
			literatureTitleView.setText(R.string.serenity_prayer_text);
			literatureView.setGravity(Gravity.CENTER_HORIZONTAL);
			literatureView.setText(R.string.serenity_prayer_lit);
			break;
		case "aaHowItWorks":
			literatureTitleView.setText(R.string.how_it_works_text);
			literatureView.setText(R.string.aa_how_it_works_lit);
			break;
		default:
			Toast.makeText(getApplicationContext(), "Error, Please Try Again", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	
	/**
	 * Display menu options
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu1, menu);
		return true;
	}

	/**
	 * Handle menu selections
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

	
	
	private static TextView literatureTitleView;
	private static TextView literatureView;
	private String litSelection;
	
}
