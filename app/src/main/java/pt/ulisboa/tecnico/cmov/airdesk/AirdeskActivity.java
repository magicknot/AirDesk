package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.ViewPagerAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.util.AirdeskDataHolder;
import pt.ulisboa.tecnico.cmov.airdesk.view.SlidingTabLayout;


public class AirdeskActivity extends ActionBarActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "AirdeskActivity";
    private static final int ACTIVITY_USER_REGISTRATION = 0;

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Owned Workspaces","Foreign Workspaces"};
    int Numboftabs =2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airdesk);

        ValidateUserRegistration();
        AirdeskDataHolder.init(this);
        createWorkspaceTabs();
    }

    private void ValidateUserRegistration(){
        String email;
        String nickname;

        // Restore preferences
        SharedPreferences myPrefs = getSharedPreferences(PREFS_NAME, 0);
        if (!myPrefs.contains("userEmail")) {
            Log.i(TAG, "1st Login");
            Intent intent = new Intent(this, UserRegistrationActivity.class);
            startActivityForResult(intent, ACTIVITY_USER_REGISTRATION);
        } else {
            email = myPrefs.getString("userEmail", "userEmail");
            nickname = myPrefs.getString("userNickname", "userNickname");
            Log.i(TAG, "User Login: " + nickname + "/" + email);
        }
    }

    private void createWorkspaceTabs(){
        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

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
            SharedPreferences myPrefs = getSharedPreferences(PREFS_NAME, 0);
            String email = myPrefs.getString("userEmail", "userEmail");
            String nickname = myPrefs.getString("userNickname", "userNickname");

            Log.i(TAG, "User Login " + email + " - " + nickname);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){
            case R.id.action_settings:
                SharedPreferences myPrefs = getSharedPreferences(PREFS_NAME, 0);
                String email = myPrefs.getString("userEmail", "userEmail");
                String nickname = myPrefs.getString("userNickname", "userNickname");
                Log.i(TAG, "User Login: " + nickname + "/" + email);
                Toast.makeText(getBaseContext(), "User Login: " + nickname + "/" + email, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_user:
                Toast.makeText(getBaseContext(), "You selected action_user", Toast.LENGTH_SHORT).show();
/*
                FragmentManager fm = getSupportFragmentManager();
                CreateWorkspaceFragment dFragment = new CreateWorkspaceFragment();
                dFragment.show(fm, "Dialog Fragment");
*/
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
