package uk.ac.diamond.status.fragments;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import uk.ac.diamond.status.MainActivity;
import uk.ac.diamond.status.NoConnectionActivity;
import uk.ac.diamond.status.TextFileTask;
import uk.ac.diamond.status.R;
import uk.ac.diamond.status.ImageTask;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class DayFragment extends Fragment implements IImageFragment, ITextFragment,
        IRefreshable {

    private ImageView imageView = null;
    private Bitmap dayImage = null;
    protected String imageUrl = null;
    private String messagesUrl = null;
    private View view = null;

    private static final String LOG_TAG = "DayFragment";
    private Date lastUpdated = null;

    private static LinkedHashMap<String, String> titles = new LinkedHashMap<String, String>();

    static {
        titles.put("energy", "Beam Energy");
        titles.put("current", "Beam current");
        titles.put("life_time", "Beam lifetime");
        titles.put("mode", "Mode");
        titles.put("last_refill", "Last fill");
        titles.put("fill_pattern", "Fill pattern");
    }

    public DayFragment() {
    }

    @Override
    public Context getContext() {
        Log.d(LOG_TAG, "Getting activity");
        return getActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl = getResources().getString(R.string.day_url);
        messagesUrl = getResources().getString(R.string.messages_url);
        Log.d(LOG_TAG, "creating day");
        new ImageTask(getActivity()).execute(this);
        new TextFileTask(getActivity()).execute(this);
        if (lastUpdated == null || (new Date()).getTime() - lastUpdated.getTime() > 60) {
            MainActivity ma = (MainActivity) getActivity();
            ma.needsUpdate();
        }
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
        new ImageTask(getActivity()).execute(this);
        new TextFileTask(getActivity()).execute(this);
    }

    @Override
    public void updateImage(Bitmap bm) {
        Log.d(LOG_TAG, "Updating image.");
        if (bm != null) {
            dayImage = bm;
            BitmapDrawable bd = new BitmapDrawable(getResources(), dayImage);
            imageView.setImageDrawable(bd);
        } else {
            Log.i(LOG_TAG, "updateImage called with null bitmap.");
            noConnection();
        }
    }

    @Override
    public void updateText(StringBuilder sb) {
        String[] lines = sb.toString().split("\n");
        StringBuilder out = new StringBuilder();
        HashMap<String, String> entries = new HashMap<String, String>();
        for (String line : lines) {
            Log.d(LOG_TAG, line);
            String[] parts = line.split("=");
            entries.put(parts[0], parts.length > 1 ? parts[1] : "");
        }
        for (String key : titles.keySet()) {
            if (entries.containsKey(key)) {
                out.append(titles.get(key) + ": " + entries.get(key) + "\n");
            }
        }
        TextView tv = (TextView) view.findViewById(R.id.message_view);
        tv.setText(out.toString());

        Date date = null;
        String dateString = entries.get("updated_at");
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        try {
            date = format.parse(dateString);
            System.out.println("Date ->" + date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((MainActivity) getActivity()).alertUpdate(date);
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

    @Override
    public String getTextFileUrl() {
        return messagesUrl;
    }

    @Override
    public void noConnection() {
        Log.d(LOG_TAG, "Calling noConnection()");
        Intent intent = new Intent(getContext(), NoConnectionActivity.class);
        startActivity(intent);
    }

}
