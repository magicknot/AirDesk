package pt.ulisboa.tecnico.cmov.airdesk.user;


import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;

/**
 * Created by oliveira on 27/03/15.
 */
public class LocalUser extends User {
    private ArrayList<LocalWorkspace> lstLocalWorkspaces;
    private ArrayList<Workspace> lstForeignWorkspaces;

}
