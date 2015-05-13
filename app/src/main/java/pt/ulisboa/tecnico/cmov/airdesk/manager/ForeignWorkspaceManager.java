package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.WorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.data.AirdeskDataSource;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.WorkspaceTag;

public class ForeignWorkspaceManager extends WorkspaceManager {
    private static final String TAG = ForeignWorkspaceManager.class.getSimpleName();

    private static ForeignWorkspaceManager holder = null;


    private Map<String, WorkspaceAdapter<ForeignWorkspace>> activeUsers;

    public static ForeignWorkspaceManager getInstance() {
        if (holder == null) {
            holder = new ForeignWorkspaceManager();
        }
        return holder;
    }

    public void init(Context context) {
        context = context;

        workspaces = new ArrayList<>();

        this.activeUsers = new HashMap<>();
    }

    @Override
    public void addWorkspace(String owner, String name, long quota, boolean isNotPrivate, List<WorkspaceTag> tags, List<String> clients) {
        for (Workspace ws : workspaces) {
            if (ws.getName().toLowerCase().equals(name.toLowerCase())
                    && ws.getOwner().toLowerCase().equals(owner.toLowerCase())) {
                // TODO Show a popup message saying that name is already in use
                return;
            }
        }
        workspaces.add(new Workspace(quota, name, owner, false));
    }

    @Override
    public boolean removeWorkspace(Workspace workspace) {
        Workspace localWs = null;

        for (Workspace ws : workspaces) {
            if (ws.getName().equals(workspace.getName())) {
                localWs = ws;
                break;
            }
        }
        if (localWs != null) {
            workspaces.remove(workspace);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateWorkspaceTags(Workspace workspace, List<WorkspaceTag> listTags) {
        int i;

        for (i = 0; i < workspaces.size()
                && workspaces.get(i).getWorkspaceId() != workspace.getWorkspaceId(); i++)
            ; // Do no delete this semicolon

        if (i < workspaces.size()
                && workspaces.get(i).getWorkspaceId() == workspace.getWorkspaceId()) {
            workspaces.get(i).setTags(listTags);
        }
    }

}
