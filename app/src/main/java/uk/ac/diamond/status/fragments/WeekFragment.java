package uk.ac.diamond.status.fragments;

import android.content.Context;
import android.os.Bundle;

import uk.ac.diamond.status.ImageTask;
import uk.ac.diamond.status.R;

public class WeekFragment extends DayFragment {


	public WeekFragment() {
		// Empty constructor
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.imageUrl = getResources().getString(R.string.week_url);

		new ImageTask(getActivity()).execute(this);
	}
}
