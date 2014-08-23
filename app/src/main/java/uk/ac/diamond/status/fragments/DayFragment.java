package uk.ac.diamond.status.fragments;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import uk.ac.diamond.status.TextFileTask;
import uk.ac.diamond.status.R;
import uk.ac.diamond.status.ImageTask;

import android.app.Fragment;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DayFragment extends Fragment implements IImageFragment, ITextFragment,
		Refreshable {

	private ImageView imageView = null;
	private Bitmap dayImage = null;
	private String imageUrl = null;
    private String messagesUrl = null;
	private View view = null;

    private static HashMap<String, String> titles = new HashMap<String, String>();
    static {
        titles.put("energy", "Beam Energy");
        titles.put("current", "Beam current");
        titles.put("life_time", "Beam lifetime");
        titles.put("mode", "Mode");
        titles.put("refill", "??");
        titles.put("updated_at", "Last updated");
        titles.put("fill_pattern", "Fill pattern");
    }

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
        messagesUrl = getResources().getString(R.string.messages_url);
		System.out.println("creating day");
 //       System.out.println(getResources().getIdentifier("cube", "cube", null));
 //       updateImage(BitmapFactory.decodeFile(getResources().getResourceName(R.drawable.cube)));
		new ImageTask(getActivity()).execute(this);
        new TextFileTask(getActivity()).execute(this);
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
    public void updateText(StringBuilder sb) {
        String[] lines = sb.toString().split("\n");
        StringBuilder out = new StringBuilder();
        HashMap<String, String> entries = new HashMap<String, String>();
        for (String line : lines) {
            System.out.println(line);
            String[] parts = line.split("=");
            entries.put(parts[0], parts.length > 1 ? parts[1] : "");
        }
        for (String key : entries.keySet()) {
            if (titles.containsKey(key)) {
                out.append(titles.get(key) + ": " + entries.get(key) + "\n");
            }

        }
        TextView tv = (TextView) view.findViewById(R.id.message_view);
        System.out.println("setting messages to " + sb.toString());
        tv.setText(out.toString());
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

}
