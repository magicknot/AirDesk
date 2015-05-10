package pt.ulisboa.tecnico.cmov.airdesk.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.airdesk.util.FileManager;
import pt.ulisboa.tecnico.cmov.airdesk.util.WifiDirect.PeerDevice;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.WorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.domain.User;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.WorkspaceTag;

public class AirdeskDataHolder {
    private static final String TAG = "AirdeskDataHolder";

    private static AirdeskDataHolder holder;

    private AirdeskDataSource db;
    private Context context;
    private User currentUser;
    private List<LocalWorkspace> localWorkspaces;
    private List<ForeignWorkspace> foreignWorkspaces;
    private Map<String, WorkspaceAdapter<ForeignWorkspace>> activeUsers;

    public AirdeskDataHolder(Context context, User currentUser) {
        this.context = context;
        this.currentUser = currentUser;
        this.activeUsers = new HashMap<>();

        this.db = new AirdeskDataSource(this.context);
        this.db.open();
        this.localWorkspaces = this.db.fetchAllWorkspaces();
        Log.i(TAG, "this.db.fetchAllWorkspaces(): " + this.localWorkspaces.size());
        this.foreignWorkspaces = new ArrayList<>();
        db.close();
    }

    public static AirdeskDataHolder getInstance() {
        return holder;
    }

    public static synchronized void init(Context context, User user) {
        holder = new AirdeskDataHolder(context, user);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public List<LocalWorkspace> getLocalWorkspaces(User owner) {
        return this.localWorkspaces;
    }

    public List<ForeignWorkspace> getForeignWorkspaces() {
        fetchForeignWorkspaces();
        return this.foreignWorkspaces;
    }

    public void addLocalWorkspace(String owner, String name, long quota, boolean isNotPrivate,
                                  List<WorkspaceTag> tags, List<User> clients) {
        for (LocalWorkspace ws : localWorkspaces) {
            if (ws.getName().toLowerCase().equals(name.toLowerCase())) {
                // TODO Show a popup message saying that name is already in use
                return;
            }
        }

        LocalWorkspace lw = new LocalWorkspace(owner, name, Math.min(quota,
                FileManager.getFreeSpace(context)/1024));
//                FileManager.getFreeSpace(context)));

        if (isNotPrivate) {
            Log.i(TAG, "isPrivate: Public (Adding Tags to Workspace)");
            lw.setTags(tags);
        } else {
            Log.i(TAG, "isPrivate: Private (Adding Clients to Workspace)");
            lw.setClients(clients);
        }

        //TODO:Create Workspace Folder
        db.open();
        lw = (LocalWorkspace) db.insertWorkspace(lw);
        db.close();
        localWorkspaces.add(lw);
    }

    public boolean removeLocalWorkspace(Workspace workspace) {
        boolean isDeleted;

        db.open();
        isDeleted = db.deleteWorkspace(workspace.getWorkspaceId());
        db.close();
        Log.i(TAG, "isDeleted: " + Boolean.valueOf(isDeleted));
        localWorkspaces.remove(workspace);
        return isDeleted;
    }

    public void updateLocalWorkspaceClients(LocalWorkspace workspace) {
        db.open();
        db.updateLocalWorkspace(workspace.getWorkspaceId(), workspace.getName(),
                workspace.getOwner(), workspace.getQuota(), workspace.isPrivate());
        db.updateLocalWorkspaceTags(workspace.getWorkspaceId(), workspace.getTags());
        db.updateLocalWorkspaceClients(workspace.getWorkspaceId(), workspace.getClients());
        db.close();
    }

    public void updateLocalWorkspaceClients(LocalWorkspace workspace, List<User> listClients) {
        int i;

        db.open();
        db.updateLocalWorkspaceClients(workspace.getWorkspaceId(), listClients);
        db.close();
        for (i = 0; i < localWorkspaces.size()
                && localWorkspaces.get(i).getWorkspaceId() != workspace.getWorkspaceId(); i++)
            ; // Do no delete this semicolon

        if (i < localWorkspaces.size()
                && localWorkspaces.get(i).getWorkspaceId() == workspace.getWorkspaceId()) {
            localWorkspaces.get(i).setClients(listClients);
        }
    }

    public void updateLocalWorkspaceTags(LocalWorkspace workspace, List<WorkspaceTag> listTags) {
        int i;

        db.open();
        db.updateLocalWorkspaceTags(workspace.getWorkspaceId(), listTags);
        db.close();
        for (i = 0; i < localWorkspaces.size()
                && localWorkspaces.get(i).getWorkspaceId() != workspace.getWorkspaceId(); i++)
            ; // Do no delete this semicolon

        if (i < localWorkspaces.size()
                && localWorkspaces.get(i).getWorkspaceId() == workspace.getWorkspaceId()) {
            localWorkspaces.get(i).setTags(listTags);
        }
    }

    public void addForeignWorkspace(String owner, String name, long quota) {
        for (ForeignWorkspace ws : foreignWorkspaces) {
            if (ws.getName().toLowerCase().equals(name.toLowerCase())
                    && ws.getOwner().toLowerCase().equals(owner.toLowerCase())) {
                // TODO Show a popup message saying that name is already in use
                return;
            }
        }

        foreignWorkspaces.add(new ForeignWorkspace(name, owner, quota));
    }

    public void removeForeignWorkspace(Workspace workspace) {
        LocalWorkspace localWs = null;

        for (LocalWorkspace ws : localWorkspaces) {
            if (ws.getName().equals(workspace.getName())) {
                localWs = ws;
                break;
            }
        }

        if (localWs != null) {
            localWs.removeClient(this.currentUser);
            db.open();
            db.updateLocalWorkspaceClients(localWs.getWorkspaceId(), localWs.getClients());
            db.close();
        }

        foreignWorkspaces.remove(workspace);
    }

    public void fetchForeignWorkspaces() {
        this.foreignWorkspaces = new ArrayList<>();
        for (LocalWorkspace ws : localWorkspaces) {
            if (ws.containClient(currentUser)) {
                addForeignWorkspace(ws.getOwner(), ws.getName(), ws.getQuota());
            }
        }
    }

    public void registerActiveUser(String email, WorkspaceAdapter<ForeignWorkspace> workspace) {
        Log.i(TAG, "registerActiveUser - adding:\n\t email " + email + "\n\tworkspace: "
                + workspace.toString());
        activeUsers.put(email, workspace);
    }

    public WorkspaceAdapter<ForeignWorkspace> getWorkspaceAdapterByUser(String email) {
        return activeUsers.get(email);
    }


}