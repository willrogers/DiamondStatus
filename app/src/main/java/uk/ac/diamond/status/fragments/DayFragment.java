package uk.ac.diamond.status.fragments;

import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.diamond.status.R;
import uk.ac.diamond.status.ImageTask;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class DayFragment extends Fragment implements IImageFragment,
		Refreshable {

	private ImageView imageView = null;
	private Bitmap dayImage = null;
	private String imageUrl = null;
	private View view = null;

	public DayFragment() {
	}

	@Override
	public Context getContext() {
		System.out.println("Getting activity");
		return getActivity();
		//return null;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		imageUrl = getResources().getString(R.string.day_url);
		System.out.println("creating day");
 //       System.out.println(getResources().getIdentifier("cube", "cube", null));
 //       updateImage(BitmapFactory.decodeFile(getResources().getResourceName(R.drawable.cube)));
		new ImageTask(getActivity()).execute(this);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (view == null) {
			view = inflater.inflate(R.layout.image_fragment, null);
			imageView = (ImageView) view.findViewById(R.id.image);
			imageView.setScaleType(ScaleType.FIT_CENTER);
		}

		return view;
	}

	public void refresh() {
		if (dayImage != null) {
			BitmapDrawable bd = new BitmapDrawable(getResources(), dayImage);
			// bd.setGravity(Gravity.FILL_HORIZONTAL);
			imageView.setImageDrawable(bd);
		} else {
			System.out.println("Not yet loaded.");
		}
	}

	@Override
	public void updateImage(Bitmap bm) {
		dayImage = bm;
		BitmapDrawable bd = new BitmapDrawable(getResources(), dayImage);
		imageView.setImageDrawable(bd);
	}

	@Override
	public URL getUrl() {
		URL url = null;
		try {
			url = new URL(imageUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

}
