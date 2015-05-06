package pt.ulisboa.tecnico.cmov.airdesk;


import android.app.Application;
import pt.ulisboa.tecnico.cmov.airdesk.WifiDirect.WiFiDirectNetwork;

public class AirdeskApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        WiFiDirectNetwork.init(this);
    }

}
