package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.WorkspaceTag;

public class ForeignWorkspaceManager extends WorkspaceManager {
    private static final String TAG = ForeignWorkspaceManager.class.getSimpleName();

    private static ForeignWorkspaceManager holder = null;

    public ForeignWorkspaceManager() {
        super();
    }

    public static ForeignWorkspaceManager getInstance() {
        if (holder == null) {
            holder = new ForeignWorkspaceManager();
        }
        return holder;
    }

    public void init(Context context) {
        super.context = context;
        this.workspaces = new ArrayList<>();
    }

    public List<Workspace> getWorkspaces() {
        return workspaces;
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
        // mark as value changed
        setChanged();
        // trigger notification
        notifyObservers();
    }

    @Override
    public boolean removeWorkspace(Workspace workspace) {
        Workspace localWs = null;

        //TODO: Send Message unSubscribe
        //apenas remove localmente, necessário notificar owner para remover workspace
        for (Workspace ws : workspaces) {
            if (ws.getName().equals(workspace.getName())) {
                workspaces.remove(ws);
                setChanged();
                notifyObservers();
                return true;
            }
        }
        return false;
    }

    public void removeWorkspaceByOwner(String email) {
        Log.d(TAG, "removeWorkspaceByOwner(" + email + ")");
        for (Workspace ws : workspaces) {
            Log.d(TAG, "removeWorkspaceByOwner() - checking " + ws.toString());
            if (ws.getOwner().equals(email)) {
                workspaces.remove(ws);
                // mark as value changed
                setChanged();
                // trigger notification
            }
        }

        if (hasChanged()) {
            notifyObservers();
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
    public void createFile(String workspaceName, String filename) {
        Workspace ws = holder.getWorkspaceByName(workspaceName);
        NetworkManager.getInstance().sendCreateFileMessage(ws.getOwner(), workspaceName, filename);
    }

    @Override
    public void writeFile(Workspace workspace, TextFile file, String content) throws IOException {
        // TODO
    }

    @Override
    public String readFile(TextFile file) {
        return NetworkManager.getInstance().sendRequestFileMessage(file.getName(),
                this.getWorkspaceByName(file.getPath()));
    }

    @Override
    public void deleteFile(Workspace workspace, TextFile file) {
        NetworkManager.getInstance().sendDeleteFileMessage(workspace.getOwner(), workspace.getName(),
                file.getName());
    }

    public static void fromJson(String owner, JSONArray array) {
        List<Workspace> newWorkspaces = new ArrayList<>();
        holder.removeWorkspaceByOwner(owner);

        try {
            for (int i = 0; i < array.length(); i++) {
                newWorkspaces.add(Workspace.fromJson(owner, array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            Log.e(TAG, "fromJson() - could not read attribute to Json object\n\t" +
                    e.getCause().toString());
        }

        holder.workspaces.addAll(newWorkspaces);
        // mark as value changed
        holder.setChanged();
        // trigger notification
        holder.notifyObservers();

    }
}
