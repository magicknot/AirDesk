package pt.ulisboa.tecnico.cmov.airdesk.domain.workspace;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.util.FileManager;

public class Workspace implements Parcelable {

    private long workspaceId;

    private long quota;

    private String name;

    private String owner;

    private boolean isPrivate;

    private List<String> clients;
    private List<WorkspaceTag> tags;
    private List<TextFile> files;

    public Workspace() {
        this.workspaceId = -1;
        this.quota = -1;
        this.name = new String();
        this.owner = new String();
        this.isPrivate = true;
        this.clients = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.files = new ArrayList<>();
    }

    public Workspace(long quota, String name, String owner, boolean isPrivate) {
        this.workspaceId = -1;
        this.quota = quota;
        this.name = name;
        this.owner = owner;
        this.isPrivate = isPrivate;
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
        long minimumValue = FileManager.getUsedSpace(name, context);
        long maximumValue = FileManager.getFreeSpace(context);

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

    public void createFile(String filename, Context context) throws IOException {
        TextFile file = new TextFile(filename, name, "TODO_ACL"); //FIXME
        if (files.contains(file)) {
            //TODO Throw exception when file exists
            return;
        }

        files.add(file);
        FileManager.save(file, context); //FIXME isto tem que passar para o LW

    }

    public void writeFile(TextFile file, String content, Context context) throws IOException {
        if (files.contains(file)) {
            Long usedSpace = FileManager.getUsedSpace(name, context);
            if (usedSpace + 2 * content.length() < quota) {
                FileManager.save(file, content, context);
            } else {
                throw new IOException("Quota exceeded (" + usedSpace + " + 2 * " +
                        content.length() + " < " + quota + " = false)");
            }
        }
    }

    public String readFile(TextFile file, Context context) throws IOException {
        return FileManager.read(file, context);
    }

    public void deleteFile(TextFile file, Context context) {
        FileManager.delete(file, context);
    }

    public void deleteWorkspaceDirectory(Context context) {
        FileManager.deleteWorkspace(this.name, context);
    }

    public long getUsedSpaceByWorkspace(Context context) {
        return FileManager.getUsedSpace(this.name, context);
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

    @Override
    public String toString() {
        return "Workspace{" +
                "workspaceId=" + getWorkspaceId() +
                "quota=" + quota +
                ", name='" + getName() + '\'' +
                ", owner=" + getOwner() +
                //FIXME:  ", files=" + getFiles() +
                //FIXME:  ", clients=" + getClients() +
                ", tags=" + getTags() +
                '}';
    }

    public Workspace(Parcel source) {
        this();
        workspaceId = source.readLong();
        quota = source.readLong();
        name = source.readString();
        owner = source.readString();
        isPrivate = source.readInt() == 1;
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

}
