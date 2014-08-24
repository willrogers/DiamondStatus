package uk.ac.diamond.status.fragments;

import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;


public interface IImageFragment extends INetworkFragment {
	
	public void updateImage(Bitmap bm);
	
	public URL getUrl();
	
	public Context getContext();
	
}