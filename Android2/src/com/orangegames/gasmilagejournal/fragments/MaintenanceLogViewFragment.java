package com.orangegames.gasmilagejournal.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orangegames.gasmilagejournal.R;

public class MaintenanceLogViewFragment extends Fragment
{
	public MaintenanceLogViewFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.maintenance_log_view_fragment, container, false);
		return rootView;
	}
}
