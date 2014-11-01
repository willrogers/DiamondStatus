package uk.ac.diamond.status.fragments;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import uk.ac.diamond.status.R;
import uk.ac.diamond.status.GridAdapter;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class BeamlineFragment extends Fragment implements IRefreshable {

    private static final String LOG_TAG = "BeamlineFragment";

	private HashMap<String, String[]> map = null;
	private static StringBuilder tableData = null;
	private TextView[][] views = null;
	private GridAdapter gridAdapter = null;
	private GridView gridView = null;
	private static boolean firstRun = true;

	private String tableUrl = null;

	public BeamlineFragment() {
		// Constructor must be empty.
	}

	public void refresh() {
		new GetTableTask().execute(tableUrl);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tableUrl = getResources().getString(R.string.table_url);
		new GetTableTask().execute(tableUrl);
	}

	public void updateTable() {
		views = new TextView[map.size() + 1][3];

		gridAdapter = new GridAdapter(getActivity(), map.size()+1, 3);

		Log.d(LOG_TAG, "In the main process.");

		TextView idView = new TextView(getActivity());
		idView.setText("Insertion Device");
		idView.setTypeface(Typeface.DEFAULT_BOLD);
		// gridLayout.addView(idView);

		TextView gapView = new TextView(getActivity());
		gapView.setText("Gap (mm)");
		gapView.setTypeface(Typeface.DEFAULT_BOLD);
		// .addView(gapView);

		TextView fieldView = new TextView(getActivity());
		fieldView.setText("Field (T)");
		fieldView.setTypeface(Typeface.DEFAULT_BOLD);
		// gridLayout.addView(fieldView);

		views[0][0] = idView;
		views[0][1] = gapView;
		views[0][2] = fieldView;

		int j = 1;
		for (Map.Entry<String, String[]> entry : map.entrySet()) {
			Log.d(LOG_TAG, "In the loop: " + entry.getKey());

			TextView id = new TextView(getActivity());
			id.setText(entry.getKey().toString());
			views[j][0] = id;

			TextView gap = new TextView(getActivity());
			gap.setText(entry.getValue()[0]);
			views[j][1] = gap;

			TextView field = new TextView(getActivity());
			field.setText(entry.getValue()[1]);
			views[j][2] = field;
			j++;
		}
		gridAdapter.setItems(views);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Not providing the ViewGroup, because it is specified elsewhere.
		// Don't understand this yet.
		gridView = (GridView) inflater.inflate(R.layout.front_end_fragment,
				null);

		gridView.setAdapter(gridAdapter);
		gridView.setNumColumns(3);
		return gridView;
	}

	private HashMap<String, String[]> parseTable(String data) {
		HashMap<String, String[]> map = new HashMap<String, String[]>();
		String sep = System.getProperty("line.separator");
		String[] lines = tableData.toString().split("\n");

		for (String line : lines) {
			Log.d(LOG_TAG, line);
			if (line.startsWith("name_")) {
				String l = line.substring(5);
				String[] parts = l.split("=");
				int id = Integer.valueOf(parts[0]);
				String name = parts[1];
				String[] values = new String[10];
				/* Put empty values in the map */
				for (int k = 0; k < 10; k++) {
					values[k] = "";
				}
				map.put(name, values);
			}
		}
		/* Now try and fill the map. */
		for (String line : lines) {
			String[] parts = line.split("=");
			String[] keys = parts[0].split("_");
			if (map.containsKey(keys[0])) {
				String field = keys[1];
				String field_no = keys[2];
				String[] vals = map.get(keys[0]);
				if (field.equals("gap")) {
					vals[0] = parts[1];
				} else if (field.equals("field")) {
					vals[1] = parts[1];
				} else if (field.equals("port")) {
					vals[2] = parts[1];
				} else if (field == "optics") {
					vals[3] = parts[1];
				} else if (field == "exptl") {
					vals[4] = parts[1];
				}
				map.put(keys[0], vals);
			}
		}
		return map;
	}

	class GetTableTask extends AsyncTask<String, Void, StringBuilder> {

		private Exception exception;
		private ProgressDialog mDialog;

		protected StringBuilder doInBackground(String... urls) {
			StringBuilder sb = null;
			try {
				try {
					URL url = new URL(urls[0]);
					BufferedReader in = new BufferedReader(
							new InputStreamReader(url.openStream()));
					String str;
					sb = new StringBuilder(100);
					while ((str = in.readLine()) != null) {
						sb.append(str + '\n');
					}
					in.close();
					Log.d(LOG_TAG, "Finished getting the string.");
					tableData = sb;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return sb;
			} catch (Exception e) {
				this.exception = e;
				Log.d(LOG_TAG, "Whoopsies.");
				return null;
			}
		}

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();

	        if (firstRun) {
	        mDialog = new ProgressDialog(getActivity());
	        mDialog.setMessage("Please wait...");
	        mDialog.show();
	        firstRun = false;
	        }
	    }
		protected void onPostExecute(StringBuilder sb) {
			if (mDialog != null) {
			mDialog.hide();
			}
			if (sb != null) {
				tableData = sb;
				map = parseTable(tableData.toString());
				Log.d(LOG_TAG, "Calling updateTable");
				updateTable();
				gridView.invalidateViews();
				gridView.setAdapter(gridAdapter);
			}
		}
	}
}
