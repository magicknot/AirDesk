package pt.ulisboa.tecnico.cmov.airdesk.workspace;

import pt.ulisboa.tecnico.cmov.airdesk.user.User;

/**
 * Created by oliveira on 27/03/15.
 */
public class ForeignWorkspace extends Workspace{

    public ForeignWorkspace(String workspaceName, long workspaceQuota, String owner) {
        super(workspaceName, workspaceQuota, owner);
    }
}
