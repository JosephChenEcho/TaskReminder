package com.example.taskreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

public class ReminderService extends WakeReminderIntentService{

	public ReminderService() {
		super("ReminderService");
	}
	
	@Override
	void doReminderWork(Intent intent){
		Long rowId = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);
		//Status bar notification Code Goes here
		NotificationManager mgr=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		Intent notificationIntent = new Intent(this,ReminderEditActivity.class);
		notificationIntent.putExtra(RemindersDbAdapter.KEY_ROWID, rowId);
		
		PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
		Notification note = new Notification(android.R.drawable.stat_sys_warning,getString(R.string.notify_new_task_message),System.currentTimeMillis());
		
		note.setLatestEventInfo(this, getString(R.string.notify_new_task_title), getString(R.string.notify_new_task_message), pi);
		note.defaults |= Notification.DEFAULT_SOUND;
		note.flags |= Notification.FLAG_AUTO_CANCEL;
		
		// An issue could occur if user ever enters over 2147482647 tasks.
		int id = (int)((long)rowId);
		mgr.notify(id,note);
	}

}
