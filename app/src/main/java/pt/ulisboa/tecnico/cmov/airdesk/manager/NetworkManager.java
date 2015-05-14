package pt.ulisboa.tecnico.cmov.airdesk.manager;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.domain.PeerDevice;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.AnnounceMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.FileMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.RemoveWorkspaceMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.UserTagsMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.WorkspaceTagsMessage;
import pt.ulisboa.tecnico.cmov.airdesk.domain.messages.WorkspacesMessage;

public class NetworkManager {
    private static final String TAG = "NetworkManager";

    private List<PeerDevice> groupPeerDevices;

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

    public void addGroupDevice(PeerDevice p) {
        this.groupPeerDevices.add(p);
    }

    public void sendAnnounce(AnnounceMessage message) {

    }

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

}
