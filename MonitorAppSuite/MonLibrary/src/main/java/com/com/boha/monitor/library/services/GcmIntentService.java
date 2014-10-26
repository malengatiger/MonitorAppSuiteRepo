package com.com.boha.monitor.library.services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.TaskStatusNotificationActivity;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.util.GCMUtil;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

public class GcmIntentService extends GCMBaseIntentService {
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super(GCMUtil.GCM_SENDER_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		Log.i(TAG, "onError ... " + arg1);

	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		Log.w(TAG, "onMessage ..:..gcm message here... " + intent.getExtras().toString());
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);
		Log.d(TAG, "GCM messageType = " + messageType);
		if (!extras.isEmpty()) { 
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				Log.e(TAG, "GoogleCloudMessaging - MESSAGE_TYPE_SEND_ERROR");
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				Log.e(TAG, "GoogleCloudMessaging - MESSAGE_TYPE_SEND_ERROR");
				
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				//It's a regular GCM message, do some work.
				sendNotification(intent);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);

	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		Log.i(TAG, "onRegistered ... " + arg1);

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.i(TAG, "onUnRegistered ... " + arg1);
	}
	Gson gson = new Gson();
	private void sendNotification(Intent msgIntent) {
		
		mNotificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);
		String message = msgIntent.getExtras().getString("message");
		ProjectSiteTaskStatusDTO dto;
		try {
			dto = gson.fromJson(message, ProjectSiteTaskStatusDTO.class);
		} catch (Exception e) {
			Log.e(TAG, "gcm message: " + message);
			return;
		}

		Intent resultIntent = new Intent(this, TaskStatusNotificationActivity.class);
		resultIntent.putExtra("leaderBoard", dto);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addParentStack(MainPagerActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentIntent(resultPendingIntent)
                .addAction(R.drawable.ic_action_refresh, "More", resultPendingIntent)
                .setSmallIcon(R.drawable.xblack_oval)
				.setContentTitle(dto.getProjectName())
				.setContentText(dto.getProjectSiteName() + " - status updated");

		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
	
	static final String TAG = "GcmIntentService";

}