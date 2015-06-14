package com.putty.dailyreflection_v1_2;

import android.widget.TimePicker;
import java.util.Calendar;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;

public class TimePickerDialogFragment extends DialogFragment implements 
	android.app.TimePickerDialog.OnTimeSetListener{	
	
	//OnTimeEnteredListener is implemented by the calling Activity
		interface OnTimeEnteredListener{
			public void OnTimeEntered(int hourOfDay, int minute);
		}
	 
		
		
	@Override
	public Dialog onCreateDialog(Bundle saveInstanceState){
		now = Calendar.getInstance();
		int hour = now.get(Calendar.HOUR_OF_DAY);
		int minute = now.get(Calendar.MINUTE);
		TimePickerDialog timeDialog = new TimePickerDialog(
				this.getActivity(), this, hour, minute, false);
		return timeDialog;		
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		mListener.OnTimeEntered(hourOfDay, minute);
	}
	

	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try{
			mListener = (OnTimeEnteredListener) activity;
		} catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement OnTimeEnteredListener");
		}
	}
	
	private OnTimeEnteredListener mListener;
	private Calendar now;
}
