package pt.ulisboa.tecnico.cmov.airdesk.workspace;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.user.User;

/**
 * Created by oliveira on 27/03/15.
 */
public class LocalWorkspace extends Workspace {
    private ArrayList<User> listClients;

    public LocalWorkspace() {
        super();
    }

    public LocalWorkspace(String owner, String workspaceName, long workspaceQuota) {
        super(workspaceName, workspaceQuota, owner);
    }

    public ArrayList<User> getListClients() {
        return listClients;
    }

    public void setListClients(ArrayList<User> listClients) {
        this.listClients = listClients;
    }

    public void addClient(User client) {
        this.listClients.add(client);
    }
}
