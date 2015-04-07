package pt.ulisboa.tecnico.cmov.airdesk.util;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.user.User;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.WorkspaceTag;

/**
 * Created by oliveira on 07/04/15.
 */
public class AirdeskDataHolder {
    private static final String TAG = "AirdeskDataHolder";

    private static AirdeskDataHolder holder;
    public static AirdeskDataHolder getInstance() {return holder;}

    public static synchronized void init(Context context) {
        holder = new AirdeskDataHolder(context);
    }

    private Context mContext = null;
    private AirdeskDataSource db = null;
    private ArrayList localWorkspaces;
    private ArrayList foreignWorkspaces;

    public AirdeskDataHolder(Context context) {
        this.mContext = context;
        this.db = new AirdeskDataSource(mContext);
        this.db.open();
        this.localWorkspaces = this.db.fetchAllWorkspaces();
        Log.i(TAG, "this.db.fetchAllWorkspaces(): "+this.localWorkspaces.size());
        /* --------------------------------------------------------------------*/
        this.foreignWorkspaces = new ArrayList<ForeignWorkspace>();
        db.close();
    }

    public ArrayList<LocalWorkspace> getLocalWorkspaces(User owner){
        return this.localWorkspaces;
    }

    public void addLocalWorkspace(String owner, String name, int quota, boolean isNotPrivate, ArrayList <WorkspaceTag>listofTags){
        LocalWorkspace lw = new LocalWorkspace(owner, name, quota);
        //TODO:Validate Workspace name already exists
        db.open();
        lw = (LocalWorkspace)db.insertWorkspace(lw);
        //TODO:Create Workspace Folder
        if(isNotPrivate) {
            Log.i(TAG, "isPrivate: Public (Adding tags to Workspace)");
            lw.setListTags(listofTags);
        }
        db.close();
    }

}
