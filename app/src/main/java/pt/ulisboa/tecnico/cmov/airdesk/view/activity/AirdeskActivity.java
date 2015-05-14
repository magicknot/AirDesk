package pt.ulisboa.tecnico.cmov.airdesk.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.io.database.AirdeskDbContract;
import pt.ulisboa.tecnico.cmov.airdesk.manager.ForeignWorkspaceManager;
import pt.ulisboa.tecnico.cmov.airdesk.io.WifiDirect.WiFiDirectNetwork;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.ViewPagerAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.manager.UserManager;
import pt.ulisboa.tecnico.cmov.airdesk.view.SlidingTabLayout;
import pt.ulisboa.tecnico.cmov.airdesk.view.fragment.UserTagsFragment;

public class AirdeskActivity extends ActionBarActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "AirdeskActivity";
    private static final int ACTIVITY_USER_REGISTRATION = 0;

    private Toolbar toolbar;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private SlidingTabLayout tabs;
    private CharSequence Titles[] = {"Owned Workspaces", "Foreign Workspaces"};
    private int numberOfTabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // initialize the UI
        setContentView(R.layout.activity_airdesk);

        validateUserRegistration();
//        createWorkspaceTabs();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void validateUserRegistration() {

        // Restore preferences
        SharedPreferences myPrefs = getSharedPreferences(PREFS_NAME, 0);
        if (!myPrefs.contains("userEmail")) {
            Log.i(TAG, "1st Login");
            Intent intent = new Intent(this, UserRegistrationActivity.class);
            startActivityForResult(intent, ACTIVITY_USER_REGISTRATION);
        } else {
            initiateApplication();
        }
    }

    private void initiateApplication() {
        String email;
        String nickname;

        // Restore preferences
        SharedPreferences myPrefs = getSharedPreferences(PREFS_NAME, 0);
        email = myPrefs.getString("userEmail", "userEmail");
        nickname = myPrefs.getString("userNickname", "userNickname");
        //currentUser = new User(email, nickname);

        UserManager.getInstance().setEmail(email);
        UserManager.getInstance().setNickname(nickname);
        //FIXME Remove when Network Implementation
        ForeignWorkspaceManager.getInstance().init(this);
        createWorkspaceTabs();
        Log.i(TAG, "User Login: " + nickname + "/" + email);
    }

    private void exitApplication() {
        SharedPreferences pref = getSharedPreferences("MyPrefsFile", 0);
        pref.edit().clear().commit();
        this.deleteDatabase(AirdeskDbContract.DATABASE_NAME);
        finish();
        System.exit(0);

        // delete Files
    }

    private void createWorkspaceTabs() {
        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, numberOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Selecting Activity Result
        if (requestCode == ACTIVITY_USER_REGISTRATION) {
            // Validate user registration
            if (resultCode != RESULT_OK) {
                finish();
            }
            initiateApplication();
        }
        Log.i(TAG, "onActivityResult " + requestCode);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_airdesk, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int DIALOG_FRAGMENT_USER_TAGS = 11;
        Intent intent;
        FragmentManager fm;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                SharedPreferences myPrefs = getSharedPreferences(PREFS_NAME, 0);
                String email = myPrefs.getString("userEmail", "userEmail");
                String nickname = myPrefs.getString("userNickname", "userNickname");
                Log.i(TAG, "User Login: " + nickname + "/" + email);
                Toast.makeText(getBaseContext(), "User Login: " + nickname + "/" + email, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_logout:
                Toast.makeText(getBaseContext(), "Logout...", Toast.LENGTH_SHORT).show();
                this.exitApplication();
                return true;
            case R.id.wifi_on:
                Toast.makeText(getBaseContext(), "Wifi On", Toast.LENGTH_SHORT).show();
                WiFiDirectNetwork.getInstance().setWiFiDirectOn();
                //WiFiDirectNetwork.getInstance().refreshPeerDevices();
                //WiFiDirectNetwork.getInstance().refreshGroupDevices();
                return true;
            case R.id.wifi_off:
                Toast.makeText(getBaseContext(), "Wifi Off", Toast.LENGTH_SHORT).show();
                WiFiDirectNetwork.getInstance().setWiFiDirectOff();
                return true;
            case R.id.wifi_direct:
                WiFiDirectNetwork.getInstance().refreshPeerDevices();
                WiFiDirectNetwork.getInstance().refreshGroupDevices();
                intent = new Intent(this, WifiDirectActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", "WiFi Direct");
                startActivity(intent);
                Toast.makeText(getBaseContext(), "Wifi Direct", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.user_prefs:
                Toast.makeText(getBaseContext(), "User Settings", Toast.LENGTH_SHORT).show();
                //UserTagsFragment
                fm = getSupportFragmentManager();
                UserTagsFragment dFragmentUserTagsFragment = new UserTagsFragment();
                //EditLocalWorkspaceFragment.newInstance(getWorkspaceAdapter().getItem(position));
                //dFragmentUserTagsFragment.setTargetFragment(AirdeskActivity.this, DIALOG_FRAGMENT_USER_TAGS);
                dFragmentUserTagsFragment.show(fm, "Dialog Fragment");

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
