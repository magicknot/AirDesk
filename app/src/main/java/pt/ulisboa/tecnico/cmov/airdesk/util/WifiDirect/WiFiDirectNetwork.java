package pt.ulisboa.tecnico.cmov.airdesk.util.WifiDirect;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;

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

    private static WiFiDirectNetwork holder;

    private Context appContext;
    private ServiceConnection appConnection;

    //WiFi Direct
    private SimWifiP2pManager mManager = null;
    private SimWifiP2pManager.Channel mChannel = null;
    private Messenger mService = null;
    private boolean mBound = false;

    private List<PeerDevice> peerDevices;

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
        Intent intent = new Intent(appContext, SimWifiP2pService.class);
        appContext.bindService(intent, appConnection, Context.BIND_AUTO_CREATE);
        mBound = true;
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
        mManager.requestPeers(mChannel, this);
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

    public void initPeerDevices(){
        PeerDevice tmp = new PeerDevice();

        tmp.setIp("192.168.1.1");
        tmp.setMac("xx:xx:xx:xx:xx");
        tmp.setEmail("xxx@xxx.pt");
        tmp.setNickname("xxx");
        this.peerDevices.add(tmp);

        tmp = new PeerDevice();
        tmp.setIp("192.168.1.2");
        tmp.setMac("yy:xx:xx:xx:xx");
        tmp.setEmail("yyy@yyy.pt");
        tmp.setNickname("yyy");
        this.peerDevices.add(tmp);

        tmp = new PeerDevice();
        tmp.setIp("192.168.1.3");
        tmp.setMac("zz:xx:xx:xx:xx");
        tmp.setEmail("zzz@zzz.pt");
        tmp.setNickname("zzz");
        this.peerDevices.add(tmp);
    }



    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList, SimWifiP2pInfo simWifiP2pInfo) {

    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        PeerDevice peerDevice;
        StringBuilder peersStr = new StringBuilder();

        //WiFiDirectNetwork.getInstance().initPeerDevices();

        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ", " + device.realDeviceAddress + ")\n";
            peersStr.append(devstr);
            peerDevice = new PeerDevice();
            peerDevice.setIp(device.deviceName);
            peerDevice.setMac(device.getVirtIp());
            addPeerDevices(peerDevice);
        }
    }
}
