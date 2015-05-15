package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.domain.PeerDevice;
import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.AnnounceMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.FileMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.RequestFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.UserTagsMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.WorkspacesMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
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

    public void sendWorkspaces(String email, JSONArray workspaces) {
        WorkspacesMessage wmsg = new WorkspacesMessage(
                email,
                workspaces
        );
        for (PeerDevice pd: this.groupPeerDevices){
            if (pd.getEmail().toLowerCase().equals(email.toLowerCase())) {
                this.sendMessage(pd, wmsg);
            }
        }

    }

    public void sendUserTags() {
        UserTagsMessage amsg = new UserTagsMessage(
                UserManager.getInstance().getEmail(),
                UserManager.getInstance().tagsToJson()
        );
        for(PeerDevice pd: this.groupPeerDevices) {
            sendMessage(pd, amsg);
        }
    }

    public void sendFile(FileMessage message) {

    }

    public void sendRequestFile(String filename, Workspace workspace) {
        RequestFileMessage rfmsg = new RequestFileMessage(
                filename,
                workspace.getName(),
                UserManager.getInstance().getEmail()
        );

        for(PeerDevice pd: this.groupPeerDevices) {
            if(pd.getEmail().toLowerCase().equals(workspace.getOwner().toLowerCase())) {
                sendMessage(pd, rfmsg);
            }
        }
    }

    private void receiveAnnounceMessage(AnnounceMessage message) {
        Log.d(TAG, "receiveAnnounceMessage() - start");
        for (PeerDevice pd : this.groupPeerDevices) {
            if (pd.getDeviceName().toLowerCase().equals(message.getDeviceName().toLowerCase())) {
                pd.setEmail(message.getEmail());
                pd.setNickname(message.getNickname());

                JSONArray array = LocalWorkspaceManager.getInstance().toJson(message.getEmail(),
                        message.getTags());

                if (array.length() > 0) {
                    WorkspacesMessage wmsg = new WorkspacesMessage(
                            UserManager.getInstance().getEmail(), array);

                    sendMessage(pd, wmsg);
                }
            }
        }

        Log.d(TAG, "receiveAnnounceMessage() - done");
    }

    private void receiveUserTagsMessage(UserTagsMessage message) {

    }

    private void receiveRequestFileMessage(RequestFileMessage message) {
        TextFile file = new TextFile(message.getName(), message.getWorkspace_name(), "TEST_ACL");
        FileMessage fmsg = new FileMessage(
                message.getName(),
                LocalWorkspaceManager.getInstance().readFile(file),
                file.getACL()
        );

        for (PeerDevice pd: this.groupPeerDevices) {
            if (pd.getEmail().toLowerCase().equals(message.getRequestor_email().toLowerCase())) {
                this.sendMessage(pd, fmsg);
            }
        }


    }

    private void sendMessage(PeerDevice peerDevice, Message message) {
        Log.i(TAG, "sending message " + message.getType() + " to " + peerDevice.getEmail());
        OutgoingCommTask task;
        task = new OutgoingCommTask(peerDevice.getIp(), peerDevice.getPort(),
                message.toJson().toString());
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
        for (PeerDevice pd : newDevices) {
            this.addGroupDevice(pd);
            this.sendMessage(pd, amsg);
        }

        for (PeerDevice pd : toRemoveDevices) {
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
            Log.e(TAG, "receiveMessage() - Message string cannot be converted to JSONObject - " +
                    message + "\n");
            return;
        }

        try {
            messageType = jsonObj.getString("type");
        } catch (JSONException e) {
            Log.e(TAG, "receiveMessage() - Message does not have type field - " + message + "\n");
            return;
        }

        Log.d(TAG, "receiveMessage() - messageType = " + messageType);

        switch (messageType) {
            case AnnounceMessage.TAG:
                try {
                    this.receiveAnnounceMessage(
                            new AnnounceMessage(
                                    jsonObj.getString("e-mail"),
                                    jsonObj.getString("deviceName"),
                                    jsonObj.getString("nickname"),
                                    jsonObj.getJSONArray("tags")
                            )
                    );
                } catch (JSONException e) {
                    Log.i(TAG, "receiveMessage() - Error extracting message fields - " + message + "\n");
                }
                break;

            case WorkspacesMessage.TAG:
                try {
                    ForeignWorkspaceManager.getInstance().fromJson(jsonObj.getString("owner_email"), jsonObj.getJSONArray("workspaces"));
                } catch (JSONException e) {
                    Log.i(TAG, "Error extracting message fields - " + message + "\n");
                }
                break;

            case UserTagsMessage.TAG:
                try {
                    this.receiveUserTagsMessage(
                            new UserTagsMessage(
                                    jsonObj.getString("e-mail"),
                                    jsonObj.getJSONArray("tags")
                            )
                    );
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case RequestFileMessage.TAG:
                try {
                    this.receiveRequestFileMessage(
                            new RequestFileMessage(
                                    jsonObj.getString("name"),
                                    jsonObj.getString("workspace_name"),
                                    jsonObj.getString("requestor_email")
                            )
                    );
                } catch (JSONException e) {
                    Log.i(TAG, "receiveMessage() - Error extracting message fields - " + message + "\n");
                }
                break;

        }

    }

}
