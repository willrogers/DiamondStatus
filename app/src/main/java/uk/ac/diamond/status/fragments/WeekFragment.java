package uk.ac.diamond.status.fragments;

import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.diamond.status.R;
import uk.ac.diamond.status.ImageTask;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class WeekFragment extends Fragment implements IImageFragment,
		Refreshable {

	private ImageView imageView = null;
	private Bitmap dayImage = null;
	private String image_url = null;
	private View view = null;

	public WeekFragment() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Context getContext() {
		return getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		image_url = getResources().getString(R.string.week_url);
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
	public URL getUrl() {
		URL url = null;
		try {
			url = new URL(image_url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

	@Override
	public void updateImage(Bitmap bm) {
		dayImage = bm;
		BitmapDrawable bd = new BitmapDrawable(getResources(), dayImage);
		imageView.setImageDrawable(bd);
	}

	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle
	// savedInstanceState) {
	//
	// // Not providing the ViewGroup, because it is specified elsewhere.
	// // Don't understand this yet.
	//
	// View view = inflater.inflate(R.layout.week_fragment, null);
	// imageView = (ImageView) view.findViewById(R.id.week_image);
	//
	// if (weekImage != null) {
	// imageView.setImageBitmap(weekImage);
	// }
	//
	// return view;
	// }
	//
	// public void refresh() {
	//
	// imageView.refreshDrawableState();
	// // imageView.setVisibility(View.VISIBLE);
	// }
	//
	// class GetImageTask extends AsyncTask<String, Void, Bitmap> {
	//
	// private Exception exception;
	//
	// protected Bitmap doInBackground(String... image_urls) {
	// Bitmap bm = null;
	// try {
	//
	// try {
	// String image_url = image_urls[0];
	// InputStream in = new URL(image_url).openConnection().getInputStream();
	// bm = BitmapFactory.decodeStream(in);
	// System.out.println("Finished getting the image.");
	// weekImage = bm;
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// return bm;
	// } catch (Exception e) {
	// this.exception = e;
	// System.out.println("Whoopsies.");
	// return null;
	// }
	// }
	//
	// protected void onPostExecute(Bitmap bm) {
	// // TODO: check this.exception
	// // TODO: do something with the feed
	// //this.exception.printStackTrace();
	//
	// weekImage = bm;
	// }
	// }
}
