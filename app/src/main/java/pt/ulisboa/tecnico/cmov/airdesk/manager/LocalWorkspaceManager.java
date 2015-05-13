package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.content.Context;
import android.util.Log;

import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.data.AirdeskDataSource;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.WorkspaceTag;
import pt.ulisboa.tecnico.cmov.airdesk.util.FileManager;

public class LocalWorkspaceManager extends WorkspaceManager {
    private static final String TAG = LocalWorkspaceManager.class.getSimpleName();

    private static LocalWorkspaceManager holder = null;

    private AirdeskDataSource db;

    public static LocalWorkspaceManager getInstance() {
        if (holder == null) {
            holder = new LocalWorkspaceManager();
        }
        return holder;
    }

    public void init(Context context) {
        super.context = context;

        this.db = new AirdeskDataSource(context);
        this.db.open();
        workspaces = this.db.fetchAllWorkspaces();
        Log.i(TAG, "this.db.fetchAllWorkspaces(): " + workspaces.size());
        this.db.close();
    }


    @Override
    public void addWorkspace(String owner, String name, long quota, boolean isNotPrivate,
                                  List<WorkspaceTag> tags, List<String> clients) {
        for (Workspace ws : workspaces) {
            if (ws.getName().toLowerCase().equals(name.toLowerCase())) {
                // TODO Show a popup message saying that name is already in use
                return;
            }
        }
        long newQuota = Math.min(quota, FileManager.getFreeSpace(context) / 1024);
        Workspace lw = new Workspace( newQuota, name, owner, !isNotPrivate);

        if (isNotPrivate) {
            Log.i(TAG, "isPrivate: Public (Adding Tags to Workspace)");
            lw.setTags(tags);
        } else {
            Log.i(TAG, "isPrivate: Private (Adding Clients to Workspace)");
            lw.setClients(clients);
        }

        //TODO:Create Workspace Folder
        db.open();
        lw = db.insertWorkspace(lw);
        db.close();
        workspaces.add(lw);
    }

    @Override
    public boolean removeWorkspace(Workspace workspace) {
        boolean isDeleted;

        db.open();
        isDeleted = db.deleteWorkspace(workspace.getWorkspaceId());
        db.close();
        Log.i(TAG, "isDeleted: " + Boolean.valueOf(isDeleted));
        workspaces.remove(workspace);
        return isDeleted;
    }

    public void updateWorkspaceClients(Workspace workspace) {
        db.open();
        db.updateLocalWorkspace(workspace.getWorkspaceId(), workspace.getName(),
                workspace.getOwner(), workspace.getQuota(), workspace.isPrivate());
        db.updateLocalWorkspaceTags(workspace.getWorkspaceId(), workspace.getTags());
        db.updateLocalWorkspaceClients(workspace.getWorkspaceId(), workspace.getClients());
        db.close();
    }

    public void updateWorkspaceClients(Workspace workspace, List<String> listClients) {
        int i;

        db.open();
        db.updateLocalWorkspaceClients(workspace.getWorkspaceId(), listClients);
        db.close();
        for (i = 0; i < workspaces.size()
                && workspaces.get(i).getWorkspaceId() != workspace.getWorkspaceId(); i++)
            ; // Do no delete this semicolon

        if (i < workspaces.size()
                && workspaces.get(i).getWorkspaceId() == workspace.getWorkspaceId()) {
            workspaces.get(i).setClients(listClients);
        }
    }

    @Override
    public void updateWorkspaceTags(Workspace workspace, List<WorkspaceTag> listTags) {
        int i;

        db.open();
        db.updateLocalWorkspaceTags(workspace.getWorkspaceId(), listTags);
        db.close();
        for (i = 0; i < workspaces.size()
                && workspaces.get(i).getWorkspaceId() != workspace.getWorkspaceId(); i++)
            ; // Do no delete this semicolon

        if (i < workspaces.size()
                && workspaces.get(i).getWorkspaceId() == workspace.getWorkspaceId()) {
            workspaces.get(i).setTags(listTags);
        }
    }
}
