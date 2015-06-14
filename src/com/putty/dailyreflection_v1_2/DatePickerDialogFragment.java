package com.putty.dailyreflection_v1_2;

import java.util.Calendar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * Date Picker class displays appropriate dialog fragment. Allows user to select
 * their sobriety date.
 */
public class DatePickerDialogFragment extends DialogFragment 
	implements android.app.DatePickerDialog.OnDateSetListener{


	/**
	 * Called when fragment is first attached to activity, precedes onCreate()
	 */
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			dateEnteredListener = (OnDateEnteredListener) activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement OnDateENteredListener");
		}	
	}
	
	
	/**
	 * Initializes the DatePickerDialog and assigns current values
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		DatePickerDialog dateDialog = new DatePickerDialog(this.getActivity(), this, tempDate.get(Calendar.YEAR),
				tempDate.get(Calendar.MONTH), tempDate.get(Calendar.DAY_OF_MONTH));
		dateDialog.setTitle("Enter Sobriety Date");
		return dateDialog;
	}
	
	
	/**
	 * Returns dateEntered data to the Listener activity (HomePage Activity)
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		dateEnteredListener.OnDateEntered(year, monthOfYear, dayOfMonth);
	}
	

	/**
	 * Set the DatePickerDialog's date that will be viewed when View displays
	 * @param newDate
	 */
	public void setDate(Calendar newDate){
		tempDate = newDate;
	}

	
	/**
	 * OnDateEntered is executed by HomePage Activity
	 */
	interface OnDateEnteredListener{
		public void OnDateEntered(int year, int monthOfYear, int dayOfMonth);
	}
	
	
	private OnDateEnteredListener dateEnteredListener;
	private Calendar tempDate;
}
