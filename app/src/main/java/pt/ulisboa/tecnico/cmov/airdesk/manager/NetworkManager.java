package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.domain.PeerDevice;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.AnnounceMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.FileMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.RemoveWorkspaceMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.UserTagsMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.WorkspaceTagsMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.WorkspacesMessage;
import pt.ulisboa.tecnico.cmov.airdesk.io.WifiDirect.OutgoingCommTask;

public class NetworkManager {
    private static final String TAG = "NetworkManager";

    private List<PeerDevice> groupPeerDevices;
    private String deviceName;

    private static NetworkManager holder = null;

    public static NetworkManager getInstance() {
        if (holder == null) {
            holder = new NetworkManager();
        }
        return holder;
    }

    public void init() {
        this.groupPeerDevices = new ArrayList<>();
    }

    public List<PeerDevice> getGroupDevices() {
        return this.groupPeerDevices;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void addGroupDevice(PeerDevice p) {
        this.groupPeerDevices.add(p);
    }

    /*
    public void sendAnnounce(AnnounceMessage message) {

    }
    */

    public void sendWorkspaces(WorkspacesMessage message) {

    }

    public void sendRemoveWorkspace(RemoveWorkspaceMessage message) {

    }

    public void sendUserTags(UserTagsMessage message) {

    }

    public void sendWorkspaceTags(WorkspaceTagsMessage message) {

    }

    public void sendFile(FileMessage message) {

    }

    public void sendRequestFile(String filename) {

    }

    private void receiveAnnounceMessage(AnnounceMessage message) {
        for(PeerDevice pd: this.groupPeerDevices){
            if (pd.getDeviceName().toLowerCase().equals(message.getDeviceName().toLowerCase())) {
                pd.setEmail(message.getEmail());
                pd.setNickname(message.getNickname());

                WorkspacesMessage wmsg = new WorkspacesMessage(
                    UserManager.getInstance().getEmail(),
                    LocalWorkspaceManager.getInstance().toJson()
                );

                sendMessage(pd, wmsg);
            }
        }

    }

    private void sendMessage(PeerDevice peerDevice, Message message) {
        OutgoingCommTask task;
        task = new OutgoingCommTask(peerDevice.getIp(), peerDevice.getPort(), message.getType(), message.toJson().toString());
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    // updates group peer list and announces to new devices
    public void updateGroupPeerList(List<PeerDevice> newDevices, List<PeerDevice> toRemoveDevices) {
        AnnounceMessage amsg = new AnnounceMessage(
                UserManager.getInstance().getEmail(),
                this.deviceName,
                UserManager.getInstance().getNickname(),
                UserManager.getInstance().tagsToJson()
        );

        // send announces to new devices
        for(PeerDevice pd : newDevices) {
            this.addGroupDevice(pd);
            this.sendMessage(pd, amsg);
        }

        for(PeerDevice pd : toRemoveDevices) {
            ForeignWorkspaceManager.getInstance().removeWorkspaceByOwner(pd.getEmail());
        }

    }

    public boolean groupContainDevice(String deviceName) {
        for (PeerDevice p : this.getGroupDevices()) {
            if (p.getDeviceName().toLowerCase().equals(deviceName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void receiveMessage(String message) {
        JSONObject jsonObj;
        String messageType;

        try {
            jsonObj = new JSONObject(message);
        } catch (JSONException e) {
            Log.i(TAG, "Message string cannot be converted to JSONObject - " + message + "\n");
            return;
        }

        try {
            messageType = jsonObj.getString("type");
        } catch (JSONException e) {
            Log.i(TAG, "Message does not have type field - " + message + "\n");
            return;
        }

        switch (messageType) {
            case AnnounceMessage.TAG:
                try {
                    this.receiveAnnounceMessage(
                            new AnnounceMessage(
                                    jsonObj.getString("email"),
                                    jsonObj.getString("deviceName"),
                                    jsonObj.getString("nickname"),
                                    jsonObj.getJSONArray("tags")
                            )
                    );
                } catch (JSONException e) {
                    Log.i(TAG, "Error extracting message fields - " + message + "\n");
                }
                break;

            case WorkspacesMessage.TAG:
                break;

        }

    }

}
