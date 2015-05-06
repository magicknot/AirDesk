package pt.ulisboa.tecnico.cmov.airdesk;


import android.app.Application;
import android.util.Log;


import pt.inesc.termite.wifidirect.SimWifiP2pDevice;
import pt.inesc.termite.wifidirect.SimWifiP2pDeviceList;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;
import pt.inesc.termite.wifidirect.SimWifiP2pManager;
import pt.ulisboa.tecnico.cmov.airdesk.WifiDirect.PeerDevice;
import pt.ulisboa.tecnico.cmov.airdesk.WifiDirect.WiFiDirectNetwork;

public class AirdeskApp extends Application
        implements SimWifiP2pManager.PeerListListener, SimWifiP2pManager.GroupInfoListener {


    @Override
    public void onCreate() {
        super.onCreate();
        WiFiDirectNetwork.init(this);
    }

    @Override
    public void onGroupInfoAvailable(SimWifiP2pDeviceList simWifiP2pDeviceList, SimWifiP2pInfo simWifiP2pInfo) {

    }

    @Override
    public void onPeersAvailable(SimWifiP2pDeviceList peers) {
        PeerDevice peerDevice;
        StringBuilder peersStr = new StringBuilder();

        Log.i("AirdeskApp", "onPeersAvailable-xxxxxxxx");
        WiFiDirectNetwork.getInstance().initPeerDevices();

        // compile list of devices in range
        for (SimWifiP2pDevice device : peers.getDeviceList()) {
            String devstr = "" + device.deviceName + " (" + device.getVirtIp() + ", " + device.realDeviceAddress + ")\n";
            peersStr.append(devstr);
            peerDevice = new PeerDevice();
            peerDevice.setIp(device.deviceName);
            peerDevice.setMac(device.getVirtIp());
            WiFiDirectNetwork.getInstance().addPeerDevices(peerDevice);
        }
    }

}
