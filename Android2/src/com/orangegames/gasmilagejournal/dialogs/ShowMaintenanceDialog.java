package com.orangegames.gasmilagejournal.dialogs;

import com.orangegames.gasmilagejournal.R;

import android.app.Activity;
import android.os.Bundle;

public class ShowMaintenanceDialog extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detailed_maintenance_form);
	}
}
