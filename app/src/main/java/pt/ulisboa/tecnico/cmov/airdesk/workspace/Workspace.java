package pt.ulisboa.tecnico.cmov.airdesk.workspace;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.File;
import pt.ulisboa.tecnico.cmov.airdesk.user.User;

/**
 * Created by oliveira on 27/03/15.
 */
public class Workspace implements Parcelable {

    private long workspaceId;
    private String name;
    private String owner;
    private long quota;
    private boolean isPrivate;

    private List<File> listFiles;
    private List<WorkspaceTag> listTags;

    public Workspace() {
    }

    public Workspace(String name, long quota, String owner) {
        this.setName(name);
        this.setQuota(quota);
        this.setOwner(owner);
        this.setPrivate(true);

    }

    public Workspace(String name, long quota, String owner, List<WorkspaceTag> listTags) {
        this.setName(name);
        this.setQuota(quota);
        this.setOwner(owner);
        this.setListTags(listTags);
    }

    public long getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(long workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getQuota() {
        return quota;
    }

    public void setQuota(long quota) {
        this.quota = quota;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<File> getListFiles() {
        return listFiles;
    }

    public void addFile(File file) {
        this.setPrivate(false);
        if (listFiles == null)
            listFiles=new ArrayList<File>();
        this.listFiles.add(file);
        this.listFiles.add(file);
    }

    public void setListFiles(List<File> listFiles) {
        this.listFiles = listFiles;
    }

    public List<WorkspaceTag> getListTags() {
        return listTags;
    }

    public void setListTags(List<WorkspaceTag> listTags) {
        if (listTags == null)
            this.listTags= new ArrayList<WorkspaceTag>();
        else
            this.listTags = listTags;
    }

    public void addTag(WorkspaceTag tag) {
        this.setPrivate(false);
        if (listTags == null)
            listTags=new ArrayList<WorkspaceTag>();
        this.listTags.add(tag);
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
                ", name='" + getName() + '\'' +
                ", owner=" + getOwner() +
                ", quota=" + getQuota() +
                ", listFiles=" + getListFiles() +
                ", listTags=" + getListTags() +
                '}';
    }

    /////////////////////////////////////////
    // Implementation of Parcelable interface
    @SuppressWarnings("unused")

    public Workspace(Parcel source) {
        workspaceId = source.readLong();
        name = source.readString();
        owner = source.readString();
        quota = source.readLong();
        isPrivate = source.readInt() == 1 ;
        source.readList( listTags, User.class.getClassLoader());
        source.readList( listFiles, User.class.getClassLoader());

    }
    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(workspaceId);
        dest.writeString(name);
        dest.writeString(owner);
        dest.writeLong(quota);
        dest.writeInt(isPrivate ? 1 : 0);
        dest.writeList(listTags);
        dest.writeList(listFiles);
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
