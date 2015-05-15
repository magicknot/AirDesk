package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.WorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.WorkspaceTag;

public abstract class WorkspaceManager extends Observable {
    private static final String TAG = "AirdeskApp";

    protected Context context;
    protected List<Workspace> workspaces;
    protected List<WorkspaceAdapter> observers;

    public WorkspaceManager() {
        observers = new ArrayList<>();
    }

    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    public Workspace getWorkspaceByName(String name) {
        Log.d(TAG, "getWorkspaceByName(" + name + ")");
        for (Workspace ws : workspaces) {
            Log.d(TAG, "\tgetWorkspaceByName() - " + ws.toString());
            if (ws.getName().equals(name)) {
                return ws;
            }
        }

        return null;
    }

    public abstract void addWorkspace(String owner, String name, long quota, boolean isNotPrivate,
                             List<WorkspaceTag> tags, List<String> clients);

    public abstract boolean removeWorkspace(Workspace workspace);

    public abstract void updateWorkspaceTags(Workspace workspace, List<WorkspaceTag> listTags);

    protected abstract void updateWorkspaceFiles(Workspace workspace);

    public abstract void createFile(String workspaceName, String filename);

    public abstract void writeFile(Workspace workspace, TextFile file, String content) throws IOException;

    public abstract String readFile(TextFile file);

    public abstract void deleteFile(Workspace workspace, TextFile file);
}
