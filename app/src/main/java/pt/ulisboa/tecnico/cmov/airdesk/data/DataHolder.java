package pt.ulisboa.tecnico.cmov.airdesk.data;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    private static final String TAG = "AirdeskApp";

    private static DataHolder holder = null;

    private Context context;
    private AirdeskDataSource db;

    private String nickname;
    private String email;
    private List<String> userPreferenceTags;

    public static DataHolder getInstance() {
        if (holder == null) {
            holder = new DataHolder();
        }
        return holder;
    }

    public void init(Context context) {
        this.context = context;
        this.userPreferenceTags = new ArrayList<>();

        this.db = new AirdeskDataSource(context);
        this.db.open();
        this.userPreferenceTags = this.db.fetchAllUserTags();
        db.close();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addPreferenceTag(String newTag){
        db.open();
        db.insertUserTag(newTag);
        db.close();
        this.userPreferenceTags.add(newTag);
        //TODO notify network
    }

    public void removePreferenceTag(String preferenceTag){
        int i=0;

        for(String tag : this.userPreferenceTags){
            if(preferenceTag.toLowerCase().equals(tag.toLowerCase())) {
                //TODO notify network
                db.open();
                db.deleteUserTag(tag);
                db.close();
                this.userPreferenceTags.remove(i);
                return;
            }
            i++;
        }
    }

    public void updatePreferenceTag(List<String> newPreferenceTags){
        List<String> newTags, removeTags;
        newTags = new ArrayList<>();
        removeTags = new ArrayList<>();

        for(String newTag : newPreferenceTags){
            if(!this.userPreferenceTags.contains(newTag))
                newTags.add(newTag);
        }
        Log.i(TAG, "[updatePreferenceTag]newTags:" + newTags);

        for(String tag : userPreferenceTags){
            if(!newPreferenceTags.contains(tag))
                removeTags.add(tag);
        }
        Log.i(TAG, "[updatePreferenceTag]removeTags:" + removeTags);

        //TODO notify network

        db.open();
        db.updateUserTag(newPreferenceTags);
        db.close();
        this.userPreferenceTags = newPreferenceTags;

    }
    public List<String> getPreferenceTags(){
        return this.userPreferenceTags;
    }




//    public List<Workspace> getLocalWorkspaces() {
//        return this.localWorkspaces;
//    }

//    public void addLocalWorkspace(String owner, String name, long quota, boolean isNotPrivate,
//                                  List<WorkspaceTag> tags, List<String> clients) {
//        for (Workspace ws : localWorkspaces) {
//            if (ws.getName().toLowerCase().equals(name.toLowerCase())) {
//                // TODO Show a popup message saying that name is already in use
//                return;
//            }
//        }
//
//        Workspace lw = new Workspace( Math.min(quota,
//                FileManager.getFreeSpace(context) / 1024), name, owner, !isNotPrivate);
////                FileManager.getFreeSpace(context)));
//
//        if (isNotPrivate) {
//            Log.i(TAG, "isPrivate: Public (Adding Tags to Workspace)");
//            lw.setTags(tags);
//        } else {
//            Log.i(TAG, "isPrivate: Private (Adding Clients to Workspace)");
//            lw.setClients(clients);
//        }
//
//        //TODO:Create Workspace Folder
//        db.open();
//        lw = (Workspace) db.insertWorkspace(lw);
//        db.close();
//        localWorkspaces.add(lw);
//    }

//    public boolean removeLocalWorkspace(Workspace workspace) {
//        boolean isDeleted;
//
//        db.open();
//        isDeleted = db.deleteWorkspace(workspace.getWorkspaceId());
//        db.close();
//        Log.i(TAG, "isDeleted: " + Boolean.valueOf(isDeleted));
//        localWorkspaces.remove(workspace);
//        return isDeleted;
//    }

//    public void updateLocalWorkspaceClients(Workspace workspace) {
//        db.open();
//        db.updateLocalWorkspace(workspace.getWorkspaceId(), workspace.getName(),
//                workspace.getOwner(), workspace.getQuota(), workspace.isPrivate());
//        db.updateLocalWorkspaceTags(workspace.getWorkspaceId(), workspace.getTags());
//        db.updateLocalWorkspaceClients(workspace.getWorkspaceId(), workspace.getClients());
//        db.close();
//    }

//    public void updateLocalWorkspaceClients(Workspace workspace, List<String> listClients) {
//        int i;
//
//        db.open();
//        db.updateLocalWorkspaceClients(workspace.getWorkspaceId(), listClients);
//        db.close();
//        for (i = 0; i < localWorkspaces.size()
//                && localWorkspaces.get(i).getWorkspaceId() != workspace.getWorkspaceId(); i++)
//            ; // Do no delete this semicolon
//
//        if (i < localWorkspaces.size()
//                && localWorkspaces.get(i).getWorkspaceId() == workspace.getWorkspaceId()) {
//            localWorkspaces.get(i).setClients(listClients);
//        }
//    }

//    public void updateLocalWorkspaceTags(Workspace workspace, List<WorkspaceTag> listTags) {
//        int i;
//
//        db.open();
//        db.updateLocalWorkspaceTags(workspace.getWorkspaceId(), listTags);
//        db.close();
//        for (i = 0; i < localWorkspaces.size()
//                && localWorkspaces.get(i).getWorkspaceId() != workspace.getWorkspaceId(); i++)
//            ; // Do no delete this semicolon
//
//        if (i < localWorkspaces.size()
//                && localWorkspaces.get(i).getWorkspaceId() == workspace.getWorkspaceId()) {
//            localWorkspaces.get(i).setTags(listTags);
//        }
//    }

//    public List<Workspace> getForeignWorkspaces() {
//        fetchForeignWorkspaces();
//        return this.foreignWorkspaces;
//    }

//    public void addForeignWorkspace(String owner, String name, long quota) {
//        for (Workspace ws : foreignWorkspaces) {
//            if (ws.getName().toLowerCase().equals(name.toLowerCase())
//                    && ws.getOwner().toLowerCase().equals(owner.toLowerCase())) {
//                // TODO Show a popup message saying that name is already in use
//                return;
//            }
//        }
//
//        foreignWorkspaces.add(new Workspace(quota, name, owner, false));
//    }

//    public void removeForeignWorkspace(Workspace workspace) {
//        Workspace localWs = null;
//
//        for (Workspace ws : localWorkspaces) {
//            if (ws.getName().equals(workspace.getName())) {
//                localWs = ws;
//                break;
//            }
//        }
//
//        if (localWs != null) {
//            //localWs.removeClient(this.currentUser);
//            db.open();
//            db.updateLocalWorkspaceClients(localWs.getWorkspaceId(), localWs.getClients());
//            db.close();
//        }
//
//        foreignWorkspaces.remove(workspace);
//    }

//    // TODO Network
//    public void fetchForeignWorkspaces() {
//        this.foreignWorkspaces = new ArrayList<>();
//
//
//        for (Workspace ws : localWorkspaces) {
//            if (ws.containClient(email)) {
//                addForeignWorkspace(ws.getOwner(), ws.getName(), ws.getQuota());
//            }
//        }
//    }

//    public void registerActiveUser(String email, WorkspaceAdapter workspace) {
//        Log.i(TAG, "registerActiveUser - adding:\n\t email " + email + "\n\tworkspace: "
//                + workspace.toString());
//        activeUsers.put(email, workspace);
//    }
//
//    public WorkspaceAdapter getWorkspaceAdapterByUser(String email) {
//        return activeUsers.get(email);
//    }
//

}
