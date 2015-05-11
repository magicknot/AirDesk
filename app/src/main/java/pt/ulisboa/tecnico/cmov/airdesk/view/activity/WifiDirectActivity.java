package pt.ulisboa.tecnico.cmov.airdesk.view.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.UnknownHostException;

import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocket;
import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.util.WifiDirect.OutgoingCommTask;
import pt.ulisboa.tecnico.cmov.airdesk.util.WifiDirect.PeerDevice;
import pt.ulisboa.tecnico.cmov.airdesk.util.WifiDirect.WiFiDirectNetwork;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.PeerDevicesAdapter;


public class WifiDirectActivity extends ActionBarActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener{

    private String TAG = WifiDirectActivity.class.getSimpleName();
    private Toolbar toolbar;

    TextView deviceName;
    PeerDevicesAdapter peerDevicesAdapter;
    PeerDevicesAdapter groupDevicesAdapter;


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
        //WiFiDirectNetwork.getInstance().refreshPeerDevices();
        //WiFiDirectNetwork.getInstance().refreshGroupDevices();

        deviceName = (TextView)findViewById(R.id.textViewDeviceName);
        deviceName.setText(WiFiDirectNetwork.getInstance().getDeviceName());

        // This is the adapter we use to populate the Peer Devices grid.
        peerDevicesAdapter = new PeerDevicesAdapter(getBaseContext(),
                R.layout.item_peer_device_list,
                WiFiDirectNetwork.getInstance().getPeerDevices());
        // Inflate the layout with a GridView in it.
        ListView listView = (ListView) findViewById(R.id.peerDevicesList);
        listView.setAdapter(peerDevicesAdapter);

        groupDevicesAdapter = new PeerDevicesAdapter(getBaseContext(),
                R.layout.item_peer_device_list,
                WiFiDirectNetwork.getInstance().getGroupDevices());
        // Inflate the layout with a GridView in it.
        ListView listViewGroupDevices = (ListView) findViewById(R.id.rememberGroupsList);
        listViewGroupDevices.setAdapter(groupDevicesAdapter);
        listViewGroupDevices.setOnItemClickListener(this);

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
            WiFiDirectNetwork.getInstance().refreshGroupDevices();
            groupDevicesAdapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        PeerDevice peer = (PeerDevice)groupDevicesAdapter.getItem(position);
        Log.i(TAG, "title of " + position + "th element clicked ("
                + peer.getDeviceName() + " [" + peer.getIp()+ ":" +peer.getPort() + "])");

        new OutgoingCommTask(peer.getIp(), peer.getPort()).executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR, peer.getIp());

/*
        try {

            SimWifiP2pSocket socket = new SimWifiP2pSocket(peer.getIp(), Integer.parseInt( WiFiDirectNetwork.getInstance().getAppContext().getString(R.string.port)));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Send dto to server
            writer.write(peer.getDeviceName()+": PING");
            Log.i(TAG, "Wrote: " + peer.getDeviceName());
            writer.newLine();
            writer.flush();

            // Close everything
            writer.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }
}
