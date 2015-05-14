package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.WorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.WorkspaceTag;

public class ForeignWorkspaceManager extends WorkspaceManager {
    private static final String TAG = ForeignWorkspaceManager.class.getSimpleName();

    private static ForeignWorkspaceManager holder = null;

    private Map<String, WorkspaceAdapter> activeUsers;

    public static ForeignWorkspaceManager getInstance() {
        if (holder == null) {
            holder = new ForeignWorkspaceManager();
        }
        return holder;
    }

    public void init(Context context) {
        super.context = context;

        fetchWorkspaces();
        this.activeUsers = new HashMap<>();
    }

    public List<Workspace> getWorkspaces() {
        fetchWorkspaces();
        return workspaces;
    }

    // TODO Network
    public void fetchWorkspaces() {
        if (workspaces == null)
            workspaces = new ArrayList<>();


        for (Workspace ws : LocalWorkspaceManager.getInstance().getWorkspaces()) {
            if (ws.containClient(UserManager.getInstance().getEmail())) {
                addWorkspace(ws.getOwner(), ws.getName(), ws.getQuota(), ws.isPrivate(), null, null);
            }
        }
    }

    @Override
    public void addWorkspace(String owner, String name, long quota, boolean isNotPrivate,
                             List<WorkspaceTag> tags, List<String> clients) {
        for (Workspace ws : workspaces) {
            if (ws.getName().toLowerCase().equals(name.toLowerCase())
                    && ws.getOwner().toLowerCase().equals(owner.toLowerCase())) {
                // TODO Show a popup message saying that name is already in use
                return;
            }
        }
        Workspace ws = new Workspace(quota, name, owner, false, false);
        workspaces.add(ws);
    }

    @Override
    public boolean removeWorkspace(Workspace workspace) {
        Workspace localWs = null;

        //TODO: Send Message unSubscribe
        //apenas remove localmente, necessário notificar owner para remover workspace
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

    @Override
    public void updateWorkspaceFiles(Workspace workspace) {
        // TODO
    }

    @Override
    public void createFile(Workspace workspace, String filename) {
        // TODO
    }

    @Override
    public void writeFile(Workspace workspace, TextFile file, String content) throws IOException {
        // TODO
    }

    @Override
    public String readFile(TextFile file) {
        return "TODO"; //TODO
    }

    @Override
    public void deleteFile(Workspace workspace, TextFile file) {
        // TODO
    }
}
