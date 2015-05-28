package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.domain.PeerDevice;
import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.WorkspaceTag;
import pt.ulisboa.tecnico.cmov.airdesk.io.FileStorage;
import pt.ulisboa.tecnico.cmov.airdesk.io.database.AirdeskDataSource;

public class LocalWorkspaceManager extends WorkspaceManager {
    private static final String TAG = LocalWorkspaceManager.class.getSimpleName();

    private static LocalWorkspaceManager holder = null;

    private AirdeskDataSource db;

    public LocalWorkspaceManager() {
        super();
    }

    public static LocalWorkspaceManager getInstance() {
        if (holder == null) {
            holder = new LocalWorkspaceManager();
        }
        return holder;
    }

    public void init(Context context) {
        super.context = context;

        this.db = new AirdeskDataSource(context);
        this.db.open();
        workspaces = this.db.fetchAllWorkspaces();
        Log.i(TAG, "this.db.fetchAllWorkspaces(): " + workspaces.size());
        this.db.close();
    }

    //----- WORKSPACES

    @Override
    public void addWorkspace(String owner, String name, long quota, boolean isNotPrivate,
                             List<WorkspaceTag> tags, List<String> clients) {
        for (Workspace ws : workspaces) {
            if (ws.getName().toLowerCase().equals(name.toLowerCase())) {
                // TODO Show a popup message saying that name is already in use
                return;
            }
        }
        long newQuota = Math.min(quota, FileStorage.getFreeSpace(context) / 1024);
        Workspace lw = new Workspace(newQuota, name, owner, !isNotPrivate, true);

        if (isNotPrivate) {
            Log.i(TAG, "isPrivate: Public (Adding Tags to Workspace)");
            lw.setTags(tags);
        } else {
            Log.i(TAG, "isPrivate: Private (Adding Clients to Workspace)");
            lw.setClients(clients);
        }

        //TODO:Create Workspace Folder
        db.open();
        lw = db.insertWorkspace(lw);
        db.close();
        workspaces.add(lw);
        for (String client : lw.getClients()) {
            NetworkManager.getInstance().sendWorkspaces(client, holder.toJson(client,
                    NetworkManager.getInstance().getPeerDeviceByEmail(client).getTags()));
        }
    }

    public void removeAllWorkspaces() {
        for (Workspace ws : workspaces) {
            removeWorkspace(ws);
        }
    }

    @Override
    public boolean removeWorkspace(Workspace workspace) {
        db.open();
        boolean isDeleted = db.deleteWorkspace(workspace.getWorkspaceId());
        if (isDeleted) {
            workspaces.remove(workspace);
            deleteWorkspaceDirectory(workspace);
        }
        db.close();

        return isDeleted;
    }

    private boolean deleteWorkspaceDirectory(Workspace workspace) {
        return FileStorage.deleteWorkspace(workspace.getName(), context);
    }

    public void updateWorkspaceClients(Workspace workspace, List<String> listClients) {
        int i;

        Workspace ws;

        db.open();
        db.updateLocalWorkspace(workspace.getWorkspaceId(), workspace.getName(),
                workspace.getOwner(), workspace.getQuota(), workspace.isPrivate(),
                workspace.isLocal());
        db.updateLocalWorkspaceClients(workspace.getWorkspaceId(), listClients);
        db.close();
        for (i = 0; i < workspaces.size()
                && workspaces.get(i).getWorkspaceId() != workspace.getWorkspaceId(); i++)
            ; // Do no delete this semicolon

        if (i < workspaces.size()
                && workspaces.get(i).getWorkspaceId() == workspace.getWorkspaceId()) {
            ws = workspaces.get(i);

            List<String> oldClients = ws.getClients();
            ws.setClients(listClients);

            oldClients.removeAll(listClients);
            NetworkManager nm = NetworkManager.getInstance();

            for (String client : oldClients) {
                nm.sendWorkspaces(client, holder.toJson(client,
                        nm.getPeerDeviceByEmail(client).getTags()));
            }

            for (String client : workspace.getClients()) {
                nm.sendWorkspaces(client, holder.toJson(client,
                        nm.getPeerDeviceByEmail(client).getTags()));
            }
        }
    }

    @Override
    public void updateWorkspaceTags(Workspace workspace, List<WorkspaceTag> listTags) {
        int i;

        db.open();
        db.updateLocalWorkspace(workspace.getWorkspaceId(), workspace.getName(),
                workspace.getOwner(), workspace.getQuota(), workspace.isPrivate(),
                workspace.isLocal());
        db.updateLocalWorkspaceTags(workspace.getWorkspaceId(), listTags);
        db.close();
        for (i = 0; i < workspaces.size()
                && workspaces.get(i).getWorkspaceId() != workspace.getWorkspaceId(); i++)
            ; // Do no delete this semicolon

        if (i < workspaces.size()
                && workspaces.get(i).getWorkspaceId() == workspace.getWorkspaceId()) {
            Workspace ws = workspaces.get(i);

            List<WorkspaceTag> oldTags = ws.getTags();
            ws.setTags(listTags);

            oldTags.removeAll(listTags);
            NetworkManager nm = NetworkManager.getInstance();

            for (PeerDevice pd : nm.getPeerDevicesByTags(oldTags)) {
                List<String> tags = new ArrayList<>();

                for (WorkspaceTag tag : oldTags)
                    tags.add(tag.getTag());

                nm.sendWorkspaces(pd.getEmail(), holder.toJson(pd.getEmail(), tags));
            }

            for (PeerDevice pd : nm.getPeerDevicesByTags(listTags)) {
                List<String> tags = new ArrayList<>();

                for (WorkspaceTag tag : listTags)
                    tags.add(tag.getTag());

                nm.sendWorkspaces(pd.getEmail(), holder.toJson(pd.getEmail(), tags));
            }
        }
    }

    @Override
    protected void updateWorkspaceFiles(Workspace workspace) {
        db.open();
        db.updateWorkspaceFiles(workspace.getWorkspaceId(), workspace.getTextFiles());
        db.close();

        NetworkManager nm = NetworkManager.getInstance();

        for (String client : workspace.getClients()) {
            nm.sendWorkspaces(client,
                    holder.toJson(client, nm.getPeerDeviceByEmail(client).getTags()));
        }
    }

    //----- FILES

    @Override
    public void createFile(String workspaceName, String filename) {
        Workspace ws = getWorkspaceByName(workspaceName);

        if (ws == null) {
            Log.e(TAG, "createFile() - could not find workspace " + workspaceName);
            return;
        }

        TextFile file = new TextFile(filename, workspaceName, "TODO_ACL"); //FIXME
        if (ws.addTextFile(file)) {
            FileStorage.save(file, context);
            updateWorkspaceFiles(ws);
        }
    }

    @Override
    public void deleteFile(Workspace workspace, TextFile file) {
        if (workspace.removeTextFile(file)) {
            FileStorage.delete(file, context);
            updateWorkspaceFiles(workspace);
        } else {
            Log.e(TAG, "deleteFile() - could not delete file " + file.getName());
        }

    }

    @Override
    public void writeFile(Workspace workspace, TextFile file, String content) throws IOException {
        if (workspace.hasTextFile(file)) {
            Long usedSpace = FileStorage.getUsedSpace(file.getPath(), context);
            if (usedSpace + 2 * content.length() < workspace.getQuota()) {
                FileStorage.save(file, content, context);
            } else {
                throw new IOException("Quota exceeded (" + usedSpace + " + 2 * " +
                        content.length() + " < " + workspace.getQuota() + " = false)");
            }
        }
    }

    @Override
    public String readFile(TextFile file) {
        return FileStorage.read(file, context);
    }

    //----- CLIENTS

    public boolean isClient(String email) {
        for (Workspace ws : workspaces) {
            if (ws.getClients().contains(email))
                return true;
        }

        return false;
    }

    //----- JSON

    public JSONArray toJson(String email, List<String> tags) {
        JSONArray array = new JSONArray();

        for (Workspace ws : workspaces) {
            if (ws.getClients().contains(email) || ws.containAnyTags(tags)) {
                array.put(ws.toJson());
            }
        }

        return array;
    }
}
