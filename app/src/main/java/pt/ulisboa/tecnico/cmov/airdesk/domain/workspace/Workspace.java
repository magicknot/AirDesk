package pt.ulisboa.tecnico.cmov.airdesk.domain.workspace;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.io.FileStorage;

public class Workspace extends Observable implements Parcelable {
    public static final String TAG = "Workspace";
    private long workspaceId;
    private long quota;
    private String name;
    private String owner;
    private boolean isPrivate;
    private boolean isLocal;
    private List<String> clients;
    private List<WorkspaceTag> tags;
    private List<TextFile> files;

    public Workspace() {
        this(-1, "", "", true, true);
    }

    private Workspace(String name, String owner, List<TextFile> files) {
        this(-1, name, owner, true, false);
        this.files = files;
    }

    public Workspace(long quota, String name, String owner, boolean isPrivate, boolean isLocal) {
        this.workspaceId = -1;
        this.quota = quota;
        this.name = name;
        this.owner = owner;
        this.isPrivate = isPrivate;
        this.isLocal = isLocal;
        this.clients = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public long getQuota() {
        return quota;
    }

    protected void setQuota(long quota) {
        this.quota = quota;
    }

    public void setQuota(long quota, Context context) {
        long minimumValue = FileStorage.getUsedSpace(name, context);
        long maximumValue = FileStorage.getFreeSpace(context);

        if (quota >= minimumValue && quota <= maximumValue) {
            this.quota = quota;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getClients() {
        return clients;
    }

    public void setClients(List<String> clients) {
        if (clients == null) {
            this.clients = new ArrayList<>();
        } else {
            this.clients = clients;
        }
    }

    public void addClient(String client) {
        if (this.clients == null) {
            clients = new ArrayList<>();
        }
        this.clients.add(client);
    }

    public boolean containClient(String client) {
        for (String c : clients) {
            if (c.toLowerCase().equals(client.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void removeClient(String client) {
        int i = 0;

        for (String c : clients) {
            if (c.toLowerCase().equals(client.toLowerCase())) {
                clients.remove(i);
            }
            i++;
        }
    }

    public List<TextFile> getTextFiles() {
        return files;
    }

    public TextFile getTextFile(String name) {
        for (TextFile file : files) {
            if (file.getName().equals(name))
                return file;
        }

        return null;
    }

    public boolean addTextFile(TextFile file) {
        if (files.contains(file)) {
            return false;
        } else {
            boolean result = files.add(file);
            // mark as value changed
            setChanged();
            // trigger notification
            notifyObservers();
            return result;
        }
    }

    public boolean hasTextFile(TextFile file) {
        return files.contains(file);
    }

    public boolean removeTextFile(TextFile file) {
        boolean result = files.remove(file);
        // mark as value changed
        setChanged();
        // trigger notification
        notifyObservers();
        return result;
    }

    public long getUsedSpaceByWorkspace(Context context) {
        return FileStorage.getUsedSpace(this.name, context);
    }

    public List<WorkspaceTag> getTags() {
        return tags;
    }

    public void setTags(List<WorkspaceTag> listTags) {
        if (listTags == null)
            this.tags = new ArrayList<>();
        else
            this.tags = listTags;
    }

    public void addTag(WorkspaceTag tag) {
        this.setPrivate(false);
        if (tags == null)
            tags = new ArrayList<>();
        this.tags.add(tag);
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    @Override
    public String toString() {
        return "Workspace{" +
                "workspaceId=" + getWorkspaceId() +
                ", quota=" + quota +
                ", name='" + getName() + '\'' +
                ", owner=" + getOwner() +
                ", files=" + getTextFiles() +
                ", clients=" + getClients() +
                ", tags=" + getTags() +
                '}';
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();

        try {
            obj.put("name", name);
            for (TextFile file : files) {
                array.put(file.toJson());
            }
            obj.put("files", array);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }

    public static Workspace fromJson(String owner, JSONObject obj) {
        Workspace ws = null;
        List<TextFile> files = new ArrayList<>();

        try {
            String name = obj.getString("name");
            JSONArray array = obj.getJSONArray("files");

            for (int i = 0; i < array.length(); i++) {
                JSONObject arrayItem = array.getJSONObject(i);
                TextFile file = TextFile.fromJson(name, arrayItem);

                if (file != null) {
                    files.add(file);
                }
            }

            ws = new Workspace(name, owner, files);
            Log.d(TAG, "fromJson() - workspaceName: " + name + ", files: " + files.size());
        } catch (JSONException e) {
            Log.e(TAG, "fromJson() - could not read attribute to Json object\n\t" +
                    e.getCause().toString());
        }

        return ws;
    }

    public Workspace(Parcel source) {
        this();
        workspaceId = source.readLong();
        quota = source.readLong();
        name = source.readString();
        owner = source.readString();
        isPrivate = source.readInt() == 1;
        isLocal = source.readInt() == 1;
        source.readList(tags, WorkspaceTag.class.getClassLoader());
        source.readList(files, TextFile.class.getClassLoader());
        source.readList(clients, String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(workspaceId);
        dest.writeLong(quota);
        dest.writeString(name);
        dest.writeString(owner);
        dest.writeInt(isPrivate ? 1 : 0);
        dest.writeInt(isLocal ? 1 : 0);
        dest.writeList(tags);
        dest.writeList(files);
        dest.writeList(clients);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<Workspace>() {
        public Workspace createFromParcel(Parcel in) {
            return new Workspace(in);
        }

        public Workspace[] newArray(int size) {
            return new Workspace[size];
        }
    };

    public boolean containAnyTags(List<String> newTags) {
        for (String newTag : newTags) {
            for (WorkspaceTag tag : tags) {
                if (newTag.equals(tag.getTag())) {
                    return true;
                }
            }
        }

        return false;
    }
}
