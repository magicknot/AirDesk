package pt.ulisboa.tecnico.cmov.airdesk.util.WifiDirect;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.inesc.termite.wifidirect.service.SimWifiP2pService;
import pt.inesc.termite.wifidirect.sockets.SimWifiP2pSocketManager;

public class WiFiDirectNetwork
        implements SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener {

    private static String TAG = "WiFiDirectNetwork";
    private static WiFiDirectNetwork holder;

    private Context appContext;
    private ServiceConnection appConnection;

    //WiFi Direct
    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;
    private boolean mBound = false;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    private String deviceName;
    private List<PeerDevice> peerDevices;
    private List<PeerDevice> grouPeerDevices;

    public WiFiDirectNetwork(Context context) {
        this.appContext = context;

        appConnection = new ServiceConnection() {
            // callbacks for service binding, passed to bindService()

            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                mService = new Messenger(service);
                mManager = new SimWifiP2pManager(mService);
                mChannel = mManager.initialize(appContext, Looper.getMainLooper(), null);
                mBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mService = null;
                mManager = null;
                mChannel = null;
                mBound = false;
            }
        };

        // initialize the WDSim API
        SimWifiP2pSocketManager.Init(appContext);

        // register broadcast receiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION);
        filter.addAction(SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION);
        SimWifiP2pBroadcastReceiver receiver = new SimWifiP2pBroadcastReceiver(context);
        context.registerReceiver(receiver, filter);

        this.peerDevices = new ArrayList<>();
        this.grouPeerDevices = new ArrayList<>();
        deviceName = new String();

    }

    public static WiFiDirectNetwork getInstance() {
        return holder;
    }

    public static synchronized void init(Context context) {
        holder = new WiFiDirectNetwork(context);
    }

    public SimWifiP2pManager getManager() {
        return mManager;
    }

    public ServiceConnection getConnection() {
        return appConnection;
    }

    public void setWiFiDirectOn(){
        if (!isWiFiDirectOn()) {
            Intent intent = new Intent(appContext, SimWifiP2pService.class);
            appContext.bindService(intent, appConnection, Context.BIND_AUTO_CREATE);
            mBound = true;
        }
    }

    public void setWiFiDirectOff(){
        if (mBound) {
            this.appContext.unbindService(getConnection());
            mBound = false;
        }
    }

    public boolean isWiFiDirectOn(){
        return mBound;
    }


    public void refreshPeerDevices(){
        if(isWiFiDirectOn())
            mManager.requestPeers(mChannel, this);
    }

    public void refreshGroupDevices(){
        if (isWiFiDirectOn())
            mManager.requestGroupInfo(mChannel, this);
    }

    public void addPeerDevices(PeerDevice peerDevice){
        peerDevices.add(peerDevice);
    }

//    public void remove PeerDevices(PeerDevice peerDevice){
//
//    }

    public List<PeerDevice> getPeerDevices(){
        return this.peerDevices;
    }

    public List<PeerDevice> getGroupDevices(){
        return this.grouPeerDevices;
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList, SimWifiP2pInfo simWifiP2pInfo) {
        PeerDevice peerDevice;

        StringBuilder peersStr = new StringBuilder();

        this.grouPeerDevices.clear();

        //Print Group Information
        //simWifiP2pInfo.print();
        this.setDeviceName(simWifiP2pInfo.getDeviceName());

        Log.i(TAG + "deviceName", this.getDeviceName());

        for (String deviceName : simWifiP2pInfo.getDevicesInNetwork()) {

            SimWifiP2pDevice device = simWifiP2pDeviceList.getByName(deviceName);
            String devstr = "" + deviceName + " (" + ((device == null)?"??":device.getVirtIp()) + ":"+ device.getVirtPort() +"); ";
            peersStr.append(devstr);

            peerDevice = new PeerDevice();
            peerDevice.setDeviceName(deviceName);
            peerDevice.setIp(device.getVirtIp());
            peerDevice.setPort(device.getVirtPort());
            this.grouPeerDevices.add(peerDevice);
        }
        Log.i(TAG + " - onGroupInfoAvailable", peersStr.toString());

    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        PeerDevice peerDevice;
        //StringBuilder peersStr = new StringBuilder();

       this.peerDevices.clear();

        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ", " + device.realDeviceAddress + ")\n";
            //peersStr.append(devstr);
            Log.i(TAG + " - onPeersAvailable", devstr);

            peerDevice = new PeerDevice();
            peerDevice.setDeviceName(device.deviceName);
            peerDevice.setIp(device.getVirtIp());
            peerDevice.setPort(device.getVirtPort());
            addPeerDevices(peerDevice);
        }
    }
}
