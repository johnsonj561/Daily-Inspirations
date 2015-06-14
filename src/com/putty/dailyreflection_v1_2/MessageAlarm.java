
package com.putty.dailyreflection_v1_2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


/**
 * Class is responsible for handling the notification alarm trigger and 
 * starting the appropriate activity on selection of the notification.
 * @author Pustikins
 *
 */
public class MessageAlarm extends BroadcastReceiver{

	/**
	 * Handles AlarmNotification Alarm signal
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("MessageAlarm", "onReceive()");
		showNotification(context);	
		
	}
	
	/**
	 * Create the notification and assign an Activity to start upon selection.
	 * @param context 
	 */
	private void showNotification(Context context) {
		Log.i("MessageAlarm" , "showNotification()"); 
	    Intent resultIntent = new Intent(context, GetMessageActivity.class);
	    
	    //TaskStackBuilder creates an artificial backstack
	    //This allows application to return to Parent Activity when the back button is pushed
	    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
	    stackBuilder.addParentStack(GetMessageActivity.class);
	    stackBuilder.addNextIntent(resultIntent);
	    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
	    
	    //Notification Builder adds the various elements to the notification
	    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
	            .setSmallIcon(R.drawable.ic_launcher)
	            .setContentTitle("Daily Inspirations")
	            .setContentText("Click to view today's message!");
	    
	    //tell the notification builder what activity to start when selected
	    mBuilder.setContentIntent(resultPendingIntent);
	    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
	    mBuilder.setAutoCancel(true);
	    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	    mNotificationManager.notify(1, mBuilder.build());
	}  


}
