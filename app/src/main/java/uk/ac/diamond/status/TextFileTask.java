package uk.ac.diamond.status;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.diamond.status.fragments.ITextFragment;

public class TextFileTask extends AsyncTask<ITextFragment, Void, StringBuilder> {

    private Exception exception;
    private ProgressDialog mDialog;
    private Context context;
    private static boolean firstRun = true;
    private ITextFragment textFragment;

	public TextFileTask(Context context) {
		this.context = context;
	}

	@Override
	protected StringBuilder doInBackground(ITextFragment... imfs) {
		StringBuilder sb = null;

		try {

			try {
                System.out.println("Running messages");
                textFragment= imfs[0];
				URL url = new URL(textFragment.getTextFileUrl());
                System.out.println(url);
                String str;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));

                sb = new StringBuilder(100);
                while ((str = in.readLine()) != null) {
                    sb.append(str + '\n');
                }
                in.close();
                System.out.println("Finished getting the string.");

			} catch (MalformedURLException e) {
                System.out.println("mue");
                this.exception = e;
			} catch (IOException e) {
                System.out.println("ioe" + e);
                this.exception = e;
			}
            System.out.println("returning " + sb);
            return sb;
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

    protected void onPostExecute(StringBuilder sb) {
        System.out.println("post executing" + sb);
        if (mDialog != null) {
            mDialog.hide();
        }
        if (sb != null) {
            textFragment.updateText(sb);

        }
    }

}
