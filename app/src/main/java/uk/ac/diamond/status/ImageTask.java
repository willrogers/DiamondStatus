package uk.ac.diamond.status;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.diamond.status.fragments.IImageFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ImageTask extends AsyncTask<IImageFragment, Void, Bitmap> {

	private Context context = null;
	private Exception exception = null;
	private Bitmap image = null;
	private IImageFragment imageFragment = null;
	private ProgressDialog mDialog = null;
	private static boolean firstRun = true;
	
	public ImageTask(Context context) {
		this.context = context;
	}

	@Override
	protected Bitmap doInBackground(IImageFragment... image_fragments) {
		Bitmap bm = null;
		imageFragment = image_fragments[0];

		try {

			try {
				URL url = imageFragment.getUrl();
				InputStream in = url.openConnection().getInputStream();
				bm = BitmapFactory.decodeStream(in);
				System.out.println("Finished getting the image.");
				image = bm;
			} catch (MalformedURLException e) {
				this.exception = e;
			} catch (IOException e) {
				this.exception = e;
			}

			return bm;
		} catch (Exception e) {
			this.exception = e;
			System.out.println("Whoopsies.");
			return null;
		}
	}
	
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (firstRun) {
        mDialog = new ProgressDialog(context);
        mDialog.setMessage("Please wait...");
        mDialog.show();
        firstRun = false;
        }


    }

	protected void onPostExecute(Bitmap bitmap) {

		if (mDialog != null) {
		mDialog.hide();
		}
        imageFragment.updateImage(bitmap);
	}
}
