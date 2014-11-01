package uk.ac.diamond.status;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.diamond.status.fragments.ITextFragment;

public class TextFileTask extends AsyncTask<ITextFragment, Void, StringBuilder> {

    private static final String LOG_TAG = "TextFileTask";

    private Exception exception;
    private ProgressDialog mDialog;
    private Context context;
    private static boolean firstRun = true;
    private ITextFragment textFragment;
    private int refreshCount = 0;

    public TextFileTask(Context context) {
        this.context = context;
    }

    @Override
    protected StringBuilder doInBackground(ITextFragment... imfs) {
        StringBuilder sb = null;

        try {

            try {
                Log.d(LOG_TAG, "Running messages");
                textFragment = imfs[0];
                URL url = new URL(textFragment.getTextFileUrl());
                Log.d(LOG_TAG, url.toString());
                String str;
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));

                sb = new StringBuilder(100);
                while ((str = in.readLine()) != null) {
                    sb.append(str + '\n');
                }
                in.close();
                Log.d(LOG_TAG, "Finished getting the string.");

            } catch (MalformedURLException e) {
                Log.d(LOG_TAG, "Malformed URL: " + e);
                this.exception = e;
            } catch (IOException e) {
                Log.d(LOG_TAG, "IO Exception" + e);
                this.exception = e;
            }
            Log.d(LOG_TAG, "returning " + sb);
            return sb;
        } catch (Exception e) {
            this.exception = e;
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
        Log.d(LOG_TAG, "post executing" + sb);
        if (mDialog != null) {
            mDialog.hide();
        }
        if (sb != null) {
            sb.append("update=" + ++refreshCount);
            textFragment.updateText(sb);

        }
    }

}
