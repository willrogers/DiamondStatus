package uk.ac.diamond.status;

import uk.ac.diamond.status.fragments.DayFragment;
import uk.ac.diamond.status.fragments.BeamlineFragment;
import uk.ac.diamond.status.fragments.IRefreshable;
import uk.ac.diamond.status.fragments.WeekFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String DAY_FRAGMENT = "day fragment";
    private static final String WEEK_FRAGMENT = "week fragment";
    private static final String FE_FRAGMENT = "front end fragment";
    private static IRefreshable currentFragment = null;
    private static final String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setTitle(R.string.app_title);
        actionBar.setDisplayShowTitleEnabled(true);


        Log.d(LOG_TAG, "Creating day fragment");
        // One TabListener for every tab
        Tab tab = actionBar.newTab()
                .setText(R.string.day_title)
                .setTabListener(new TabListener<DayFragment>(
                        this, DAY_FRAGMENT, DayFragment.class));
        Log.d(LOG_TAG, "Created day fragment");
        actionBar.addTab(tab);
        tab = actionBar.newTab()
                .setText(R.string.week_title)
                .setTabListener(new TabListener<WeekFragment>(
                        this, WEEK_FRAGMENT, WeekFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.front_end_title)
                .setTabListener(new TabListener<BeamlineFragment>(
                        this, FE_FRAGMENT, BeamlineFragment.class));
        actionBar.addTab(tab);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void refresh() {
        Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "Refreshing " + currentFragment);
        if (currentFragment != null) {
            currentFragment.refresh();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_refresh:
                refresh();
                return true;
//		case R.id.action_settings:
            //		openSettings();
            //		return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendMessage(View view) {
//		Intent intent = new Intent(this, DisplayMessageActivity.class);
//		EditText editText = (EditText) findViewById(R.id.edit_message);
//		String message = editText.getText().toString();
//		intent.putExtra(EXTRA_MESSAGE, message);
//		startActivity(intent);
    }

    /**
     * One created per tab.
     */
    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final Class<T> mClass;
        private final String mTag;

        /**
         * Constructor used each time a new tab is created.
         *
         * @param activity The host Activity, used to instantiate the fragment
         * @param tag      The identifier tag for the fragment
         * @param clz      The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mTag = tag;
            mActivity = activity;
            mClass = clz;
        }

        /* The following are each of the ActionBar.TabListener callbacks */
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }

            currentFragment = (IRefreshable) mFragment;
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }

    }

}
