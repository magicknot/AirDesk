package pt.ulisboa.tecnico.cmov.airdesk.util;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.workspace.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;

/**
 * Created by oliveira on 07/04/15.
 */
public class AirdeskDataHolder {
    private static AirdeskDataHolder holder;
    public static AirdeskDataHolder getInstance() {return holder;}

    public static synchronized void init() {
        holder = new AirdeskDataHolder();
    }

    private ArrayList localWorkspaces;
    private ArrayList foreignWorkspaces;

    public AirdeskDataHolder() {
        this.localWorkspaces = new ArrayList<LocalWorkspace>();
        this.foreignWorkspaces = new ArrayList<ForeignWorkspace>();
    }


}
