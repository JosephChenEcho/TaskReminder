package com.example.taskreminder;

import android.os.Bundle;
import android.preference.*;
import android.text.method.DigitsKeyListener;

public class TaskPreferences extends PreferenceActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.task_preferences);
		
		EditTextPreference timeDefault = (EditTextPreference)findPreference(getString(R.string.pref_default_time_from_now_key));
		timeDefault.getEditText().setKeyListener(DigitsKeyListener.getInstance());
	}

}
