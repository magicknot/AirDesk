package pt.ulisboa.tecnico.cmov.airdesk.util;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.user.User;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.WorkspaceTag;

public class AirdeskDataHolder {
    private static final String TAG = "AirdeskDataHolder";

    private static AirdeskDataHolder holder;

    private AirdeskDataSource db;
    private Context context;
    private User currentUser;
    private List<LocalWorkspace> localWorkspaces;
    private List<ForeignWorkspace> foreignWorkspaces;

    public AirdeskDataHolder(Context context, User currentUser) {
        this.context = context;
        this.currentUser = currentUser;
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

    public List<LocalWorkspace> getLocalWorkspaces(User owner) {
        return this.localWorkspaces;
    }

    public List<ForeignWorkspace> getForeignWorkspaces() {
        fetchForeignWorkspaces();
        return this.foreignWorkspaces;
    }

    public void addLocalWorkspace(String owner, String name, int quota, boolean isNotPrivate,
                                  List<WorkspaceTag> tags, List<User> clients) {
        for (LocalWorkspace ws : localWorkspaces) {
            if (ws.getName().toLowerCase().equals(name.toLowerCase())) {
                // TODO Show a popup message saying that name is already in use
                return;
            }
        }

        LocalWorkspace lw = new LocalWorkspace(owner, name, quota);

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

    public void addForeignWorkspace(String owner, String name) {
        for (ForeignWorkspace ws : foreignWorkspaces) {
            if (ws.getName().toLowerCase().equals(name.toLowerCase())
                    && ws.getOwner().toLowerCase().equals(owner.toLowerCase())) {
                // TODO Show a popup message saying that name is already in use
                return;
            }
        }

        foreignWorkspaces.add(new ForeignWorkspace(name, owner));
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

    public void fetchForeignWorkspaces() {
        for (LocalWorkspace ws : localWorkspaces) {
            if (ws.containClient(currentUser)) {
                addForeignWorkspace(ws.getOwner(), ws.getName());
            }
        }
    }
}
