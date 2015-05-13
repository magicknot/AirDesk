package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.content.Context;

import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.WorkspaceTag;

public abstract class WorkspaceManager {
    private static final String TAG = "AirdeskApp";

    protected Context context;
    protected List<Workspace> workspaces;

    public List<Workspace> getWorkspaces() {
        return workspaces;
    }

    public abstract void addWorkspace(String owner, String name, long quota, boolean isNotPrivate,
                             List<WorkspaceTag> tags, List<String> clients);

    public abstract boolean removeWorkspace(Workspace workspace);

    public abstract void updateWorkspaceTags(Workspace workspace, List<WorkspaceTag> listTags);

}
