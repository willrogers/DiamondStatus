package uk.ac.diamond.status.fragments;

import android.content.Context;
import android.graphics.Bitmap;

import java.net.URL;


public interface ITextFragment {
	
	public void updateText(StringBuilder sb);
	
	public String getTextFileUrl();
	
	public Context getContext();
	
}