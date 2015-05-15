package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pt.ulisboa.tecnico.cmov.airdesk.domain.PeerDevice;
import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.AnnounceMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.CreateFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.DeleteFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.FileMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.Message;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.RequestFileMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.UnsubscribeWorkspaceMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.UserTagsMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.WorkspacesMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.io.WifiDirect.OutgoingCommTask;
import pt.ulisboa.tecnico.cmov.airdesk.io.WifiDirect.OutgoingCommTaskWithResponse;

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

    public PeerDevice getPeerDeviceByEmail(String email) {
        for (PeerDevice pd : this.groupPeerDevices) {
            if (pd.getEmail().toLowerCase().equals(email.toLowerCase())) {
                return pd;
            }
        }
        return null;
    }

    public void sendWorkspaces(String client, JSONArray workspaces) {
        WorkspacesMessage wmsg = new WorkspacesMessage(UserManager.getInstance().getEmail(), workspaces);
        PeerDevice pd = getPeerDeviceByEmail(client);

        if (pd != null) {
            this.sendMessage(pd, wmsg);
        }
    }

    public void sendUserTagsMessage() {
        UserTagsMessage amsg = new UserTagsMessage(
                UserManager.getInstance().getEmail(),
                UserManager.getInstance().tagsToJson()
        );
        for (PeerDevice pd : this.groupPeerDevices) {
            sendMessage(pd, amsg);
        }
    }

    public void sendUnsubscribeWorkspace(String workspaceName) {
        UnsubscribeWorkspaceMessage umsg = new UnsubscribeWorkspaceMessage(
                UserManager.getInstance().getEmail(),
                workspaceName
        );

        PeerDevice pd = getPeerDeviceByEmail(UserManager.getInstance().getEmail());

        if (pd != null) {
            this.sendMessage(pd, umsg);
        }

    }

    public void sendFile(Workspace workspace, TextFile file, String content) {
        PeerDevice pd = getPeerDeviceByEmail(workspace.getOwner());
        FileMessage message = new FileMessage(file.getName(), workspace.getOwner(),
                workspace.getName(), content, file.getACL());

        if (pd != null) {
            Log.i(TAG, "sendFile() - sending file " + file.toString() + " to " + pd.getEmail());
            this.sendMessage(pd, message);
        }

    }

    public String sendRequestFileMessage(String filename, Workspace workspace) {
        RequestFileMessage rfmsg = new RequestFileMessage(filename, workspace.getName(),
                UserManager.getInstance().getEmail());

        PeerDevice pd = getPeerDeviceByEmail(workspace.getOwner());
        Message msg;
        FileMessage fmsg;

        if (pd != null) {

            msg = this.sendMessageWithResponse(pd, rfmsg);

            if (msg == null) {
                Log.d(TAG, "sendRequestFileMessage() - IS NULL");
            } else {
                fmsg = (FileMessage) msg;
                Log.d(TAG, "sendRequestFileMessage() - " + fmsg.toJson().toString());
                return fmsg.getContent();
            }

        }

        return null;
    }

    public void sendCreateFileMessage(String email, String workspaceName, String filename) {
        CreateFileMessage cfmsg = new CreateFileMessage(filename, workspaceName);
        PeerDevice pd = this.getPeerDeviceByEmail(email);
        if (pd != null) {
            this.sendMessage(pd, cfmsg);
        }
    }

    public void sendDeleteFileMessage(String email, String workspaceName, String filename) {
        DeleteFileMessage message = new DeleteFileMessage(filename, workspaceName);
        PeerDevice device = this.getPeerDeviceByEmail(email);

        if (device != null) {
            this.sendMessage(device, message);
        }
    }

    public void receiveCreateFileMessage(CreateFileMessage message) {
        LocalWorkspaceManager.getInstance().createFile(message.getWorkspaceName(), message.getName());
    }

    public void receiveDeleteFileMessage(DeleteFileMessage message) {
        Workspace ws = LocalWorkspaceManager.getInstance().getWorkspaceByName(message.getWorkspaceName());
        LocalWorkspaceManager.getInstance().deleteFile(ws, ws.getTextFile(message.getName()));
    }

    private void receiveAnnounceMessage(AnnounceMessage message) {
        Log.d(TAG, "receiveAnnounceMessage() - start");
        for (PeerDevice pd : this.groupPeerDevices) {
            if (pd.getDeviceName().toLowerCase().equals(message.getDeviceName().toLowerCase())) {
                pd.setEmail(message.getEmail());
                pd.setNickname(message.getNickname());
                pd.importTagsFromJson(message.getTags());

                // send updated workspaces list
                if (LocalWorkspaceManager.getInstance().isClient(pd.getEmail())) {
                    WorkspacesMessage wmsg = new WorkspacesMessage(
                            UserManager.getInstance().getEmail(),
                            LocalWorkspaceManager.getInstance().toJson(pd.getEmail(), pd.getTags()));

                    sendMessage(pd, wmsg);
                }
            }
        }

        Log.d(TAG, "receiveAnnounceMessage() - done");
    }

    private void receiveUserTagsMessage(UserTagsMessage message) {
        PeerDevice pd = this.getPeerDeviceByEmail(message.getEmail());

        pd.importTagsFromJson(message.getTags());

        // send updated workspaces list
        if (pd.getTags().size() > 0) {
            WorkspacesMessage wmsg = new WorkspacesMessage(
                    UserManager.getInstance().getEmail(),
                    LocalWorkspaceManager.getInstance().toJson(
                            pd.getEmail(),
                            pd.getTags()
                    )
            );

            sendMessage(pd, wmsg);
        }
    }


    private FileMessage receiveRequestFileMessage(RequestFileMessage message) {
        TextFile file = new TextFile(message.getName(), message.getWorkspace_name(), "TEST_ACL");
        return new FileMessage(message.getName(), UserManager.getInstance().getEmail(),
                message.getWorkspace_name(), LocalWorkspaceManager.getInstance().readFile(file),
                file.getACL());
    }

    // decide what happens when we receive a file
    private void receiveFileMessage(FileMessage message) {
        Workspace ws = LocalWorkspaceManager.getInstance().getWorkspaceByName(message.getWorkspaceName());

        // if workspace AND file exist, then...
        if (ws != null) {
            TextFile textFile = ws.getTextFile(message.getName());
            if (textFile != null) {

                try {
                    // save file locally
                    LocalWorkspaceManager.getInstance().writeFile(ws, textFile, message.getContent());

                } catch (IOException e) {
                    Log.e(TAG, "receiveFileMessage() - CABUM: error saving file - " +
                            message + "\n");
                }
            }
        }
    }

    // generic function to send messages to other peers
    private Message sendMessageWithResponse(PeerDevice peerDevice, Message message) {
        Log.i(TAG, "sending message " + message.toJson().toString() + " to " + peerDevice.getEmail());
        OutgoingCommTaskWithResponse task;

        task = new OutgoingCommTaskWithResponse(peerDevice.getIp(), peerDevice.getPort(),
                message.toJson().toString());

//        task.response = new AsyncResponse() {
//            @Override
//            public void processFinish(String output) {
//                Log.d(TAG, "sendMessageWithResponse() -> processFinish() " + output);
//                holder.message = receiveMessage(output);
//            }
//        };

//        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        try {
            String result = task.execute().get();
            Log.d(TAG, "sendMessageWithResponse() - got result: " + result);
            return receiveMessage(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    // generic function to send messages to other peers
    private void sendMessage(PeerDevice peerDevice, Message message) {
        Log.i(TAG, "sending message " + message.toJson().toString() + " to " + peerDevice.getEmail());
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

    public Message receiveMessage(String message) {
        JSONObject jsonObj;
        String messageType;

        try {
            jsonObj = new JSONObject(message);
        } catch (JSONException e) {
            Log.e(TAG, "receiveMessage() - Message string cannot be converted to JSONObject - " +
                    message + "\n");
            return null;
        }

        try {
            messageType = jsonObj.getString("type");
        } catch (JSONException e) {
            Log.e(TAG, "receiveMessage() - Message does not have type field - " + message + "\n");
            return null;
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
                    Log.e(TAG, "receiveMessage() - Error extracting message fields - " + message + "\n");
                }
                break;

            case WorkspacesMessage.TAG:
                try {
                    ForeignWorkspaceManager.fromJson(jsonObj.getString("owner_email"), jsonObj.getJSONArray("workspaces"));
                } catch (JSONException e) {
                    Log.i(TAG, "receiveMessage() - Error extracting message fields - " + message + "\n");
                    Log.e(TAG, "Error extracting message fields - " + message + "\n");
                }
                break;

            case UserTagsMessage.TAG:
                try {
                    this.receiveUserTagsMessage(new UserTagsMessage(jsonObj.getString("e-mail"),
                            jsonObj.getJSONArray("tags")));
                } catch (JSONException e) {
                    Log.e(TAG, "Error extracting message fields - " + message + "\n");
                }
                break;

            case RequestFileMessage.TAG:
                try { // here we return the message to be sent back in the same socket
                    return this.receiveRequestFileMessage(
                            new RequestFileMessage(
                                    jsonObj.getString("name"),
                                    jsonObj.getString("workspace_name"),
                                    jsonObj.getString("requestor_email")
                            )
                    );
                } catch (JSONException e) {
                    Log.e(TAG, "receiveMessage() - Error extracting message fields - " + message + "\n");
                }
                break;

            case FileMessage.TAG:
                try {
                    FileMessage fmsg = new FileMessage(jsonObj.getString("name"),
                            jsonObj.getString("owner_email"), jsonObj.getString("workspace_name"),
                            jsonObj.getString("content"), jsonObj.getString("acl"));

                    if (LocalWorkspaceManager.getInstance().getWorkspaceByName(fmsg.getWorkspaceName()) != null) {
                        this.receiveFileMessage(fmsg);
                    } else {
                        Log.d(TAG, "receiveMessage() -" + fmsg.toJson().toString());
                        return fmsg;
                    }


                } catch (JSONException e) {
                    Log.e(TAG, "receiveMessage() - Error extracting message fields - " + message + "\n");
                }
                break;

            case CreateFileMessage.TAG:
                try {
                    this.receiveCreateFileMessage(new CreateFileMessage(jsonObj.getString("name"),
                            jsonObj.getString("workspace_name")));
                } catch (JSONException e) {
                    Log.e(TAG, "receiveMessage() - Error extracting message fields - " + message + "\n");
                }
                break;

            case DeleteFileMessage.TAG:
                try {
                    this.receiveDeleteFileMessage(new DeleteFileMessage(jsonObj.getString("name"),
                            jsonObj.getString("workspace_name")));
                } catch (JSONException e) {
                    Log.i(TAG, "Error extracting message fields - " + message + "\n");
                }
                break;

            case UnsubscribeWorkspaceMessage.TAG:
                try {
                    Workspace ws = LocalWorkspaceManager.getInstance().getWorkspaceByName(jsonObj.getString("workspaceName"));
                    ws.removeClient(jsonObj.getString("e-mail"));
                    LocalWorkspaceManager.getInstance().updateWorkspaceClients(ws, ws.getClients());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
        return null;
    }

}
