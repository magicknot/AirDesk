package pt.ulisboa.tecnico.cmov.airdesk.view.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.util.WifiDirect.WiFiDirectNetwork;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.PeerDevicesAdapter;


public class WifiDirectActivity extends ActionBarActivity {
    private Toolbar toolbar;

    PeerDevicesAdapter peerDevicesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_direct);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("Airdesk");
        toolbar.setSubtitle("WiFi Direct");

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        // This is the adapter we use to populate the Peer Devices grid.
        peerDevicesAdapter = new PeerDevicesAdapter(getBaseContext(),
                R.layout.item_peer_device_list,
                WiFiDirectNetwork.getInstance().getPeerDevices());
        // Inflate the layout with a GridView in it.
        ListView listView = (ListView) findViewById(R.id.peerDevicesList);
        listView.setAdapter(peerDevicesAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wifi_direct, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search_devices) {
            WiFiDirectNetwork.getInstance().refreshPeerDevices();
            peerDevicesAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
