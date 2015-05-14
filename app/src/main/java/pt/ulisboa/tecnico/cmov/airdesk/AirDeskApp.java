package pt.ulisboa.tecnico.cmov.airdesk;

import android.app.Application;

import pt.ulisboa.tecnico.cmov.airdesk.manager.UserManager;
import pt.ulisboa.tecnico.cmov.airdesk.manager.LocalWorkspaceManager;
import pt.ulisboa.tecnico.cmov.airdesk.util.WifiDirect.WiFiDirectNetwork;

public class AirDeskApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WiFiDirectNetwork.init(this);
        LocalWorkspaceManager.getInstance().init(this);
        UserManager.getInstance().init(this);
    }
}
