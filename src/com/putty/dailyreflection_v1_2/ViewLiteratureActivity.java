package com.putty.dailyreflection_v1_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Activity displays the literature available to the user. Activity provides the literature
 * selections, but does not display the literal literature.
 */
public class ViewLiteratureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_literature);
		
		//Link all buttons accordingly
		aaPreambleButton = (Button) findViewById(R.id.aaPreambleButton);
		aaPreambleButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ViewLiteratureActivity.this, DisplayLiteratureActivity.class);
				i.putExtra("litSelection", "aaPreamble");
				startActivity(i);
			}
		});
		
		aaHowItWorksButton = (Button) findViewById(R.id.aaHowItWorksButton);
		aaHowItWorksButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ViewLiteratureActivity.this, DisplayLiteratureActivity.class);
				i.putExtra("litSelection", "aaHowItWorks");
				startActivity(i);
			}
		});
		
		aaTwelveTraditionsButton = (Button) findViewById(R.id.aaTraditionsButton);
		aaTwelveTraditionsButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ViewLiteratureActivity.this, DisplayLiteratureActivity.class);
				i.putExtra("litSelection", "aaTwelveTraditions");
				startActivity(i);
			}
		});
		
		aaPromisesButton = (Button) findViewById(R.id.aaPromisesButton);
		aaPromisesButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ViewLiteratureActivity.this, DisplayLiteratureActivity.class);
				i.putExtra("litSelection", "aaPromises");
				startActivity(i);
			}
		});
		
		serenityPrayerButton = (Button) findViewById(R.id.serenityPrayerButton);
		serenityPrayerButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(ViewLiteratureActivity.this, DisplayLiteratureActivity.class);
				i.putExtra("litSelection", "serenityPrayer");
				startActivity(i);
			}
		});
		
	}

	
	/**
	 * Display menu options
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_literature, menu);
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

	
	private Button aaPreambleButton;
	private Button aaHowItWorksButton;
	private Button aaTwelveTraditionsButton;
	private Button aaPromisesButton;
	private Button serenityPrayerButton;
	

}
