package pt.ulisboa.tecnico.cmov.airdesk.util;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.user.User;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.ForeignWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;
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
    private ArrayList<LocalWorkspace> localWorkspaces;
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

    public List<LocalWorkspace> getLocalWorkspaces(User owner){
        return this.localWorkspaces;
    }

    public void addLocalWorkspace(String owner, String name, int quota, boolean isNotPrivate, List <WorkspaceTag>listofTags, List<User> listofClients ){
        LocalWorkspace lw = new LocalWorkspace(owner, name, quota);
        //TODO:Validate Workspace name already exists
        if(isNotPrivate) {
            Log.i(TAG, "isPrivate: Public (Adding Tags to Workspace)");
            lw.setListTags(listofTags);
        } else {
            Log.i(TAG, "isPrivate: Private (Adding Clients to Workspace)");
            lw.setListClients(listofClients);
        }
        //TODO:Create Workspace Folder
        db.open();
        lw = (LocalWorkspace)db.insertWorkspace(lw);
        db.close();
        localWorkspaces.add(lw);
    }

    public boolean removeLocalWorkspace(Workspace workspace){
        boolean isDeleted;

        db.open();
        isDeleted = db.deleteWorkspace(workspace.getWorkspaceId());
        db.close();
        Log.i(TAG, "isDeleted: "+Boolean.valueOf(isDeleted));
        localWorkspaces.remove(workspace);
        return isDeleted;
    }

    public void updateLocalWorkspaceClients(LocalWorkspace workspace){
        db.open();
        db.updateLocalWorkspace(workspace.getWorkspaceId(),workspace.getName(),workspace.getOwner(),workspace.getQuota(),workspace.isPrivate());
        db.updateLocalWorkspaceTags(workspace.getWorkspaceId(), workspace.getListTags());
        db.updateLocalWorkspaceClients(workspace.getWorkspaceId(), workspace.getListClients());
        db.close();
    }

    public void updateLocalWorkspaceClients(LocalWorkspace workspace, List<User> listClients){
        int i;

        db.open();
        db.updateLocalWorkspaceClients(workspace.getWorkspaceId(), listClients);
        db.close();
        for (i = 0; i<localWorkspaces.size()&& localWorkspaces.get(i).getWorkspaceId()!=workspace.getWorkspaceId() ; i++);
        if(i<localWorkspaces.size()&&localWorkspaces.get(i).getWorkspaceId()==workspace.getWorkspaceId())
            localWorkspaces.get(i).setListClients(listClients);
    }

    public void updateLocalWorkspaceTags(LocalWorkspace workspace, List<WorkspaceTag> listTags){
        int i;

        db.open();
        db.updateLocalWorkspaceTags(workspace.getWorkspaceId(), listTags);
        db.close();
        for (i = 0; i<localWorkspaces.size()&& localWorkspaces.get(i).getWorkspaceId()!=workspace.getWorkspaceId(); i++);
        if(i<localWorkspaces.size()&&localWorkspaces.get(i).getWorkspaceId()==workspace.getWorkspaceId())
            localWorkspaces.get(i).setListTags(listTags);
    }

}
