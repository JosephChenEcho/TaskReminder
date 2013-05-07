package com.example.taskreminder;

import java.util.Calendar;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ReminderListActivity extends ListActivity {
			
	private static final int ACTIVITY_CREATE=0;
	private static final int ACTIVITY_EDIT=1;
	
	private RemindersDbAdapter mDbHelper;
	
	/*Called when the activity is first created*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_list);
						
		mDbHelper = new RemindersDbAdapter(this);
		mDbHelper.open();
		fillData();
		
		registerForContextMenu(getListView());		
	}

	// Menu Code removed for brevity
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);
		Intent i = new Intent(this,ReminderEditActivity.class);
		i.putExtra(RemindersDbAdapter.KEY_ROWID, id);
		startActivityForResult(i, ACTIVITY_EDIT);
/*		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_TEXT, "Hey Everybody!");
		i.putExtra(Intent.EXTRA_SUBJECT, "My Subject");
		Intent chooser = Intent.createChooser(i, "Who Should Handle this");
		startActivity(chooser);*/
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.list_menu_item_longpress, menu);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.list_menu, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featured, MenuItem item){
		switch(item.getItemId()){
		case R.id.menu_insert:
			createReminder();
			return true;
		case R.id.menu_settings:
			Intent i = new Intent(this,TaskPreferences.class);
			startActivity(i);
			return true;
		}
		return super.onMenuItemSelected(featured, item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent){
		super.onActivityResult(requestCode, resultCode, intent);
		// Reload the list here
		fillData();
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.menu_delete:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			mDbHelper.deleteReminder(info.id);
			fillData();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	private void createReminder(){
		Intent i = new Intent(this,ReminderEditActivity.class);
		startActivityForResult(i,ACTIVITY_CREATE);
	}
	
	private void fillData(){
		Cursor remindersCursor = mDbHelper.fetchAllReminders();
		startManagingCursor(remindersCursor);
		
		// Create an array to specify the fields we want only tht TITLE
		String[] from = new String[]{RemindersDbAdapter.KEY_TITLE};
		
		// and an array of the fields we want to bing in the view
		int[] to = new int[]{R.id.text1};
		
		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter reminders = new SimpleCursorAdapter(this, R.layout.reminder_row,remindersCursor,from,to);
		setListAdapter(reminders);
	}
	
	

}
