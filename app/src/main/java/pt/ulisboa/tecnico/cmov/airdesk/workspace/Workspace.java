package pt.ulisboa.tecnico.cmov.airdesk.workspace;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.File;
import pt.ulisboa.tecnico.cmov.airdesk.user.User;

/**
 * Created by oliveira on 27/03/15.
 */
public class Workspace {

    private long workspaceId;
    private String name;
    private String owner;
    private long quota;
    private boolean isPrivate;

    private ArrayList<File> listFiles;
    private ArrayList<WorkspaceTag> listTags;

    public Workspace() {
    }

    public Workspace(String name, long quota, String owner) {
        this.setName(name);
        this.setQuota(quota);
        this.setOwner(owner);
        this.setPrivate(true);
    }

    public Workspace(String name, long quota, String owner, ArrayList<WorkspaceTag> listTags) {
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

    public ArrayList<File> getListFiles() {
        return listFiles;
    }

    public void addFile(File file) {
        this.listFiles.add(file);
    }

    public void setListFiles(ArrayList<File> listFiles) {
        this.listFiles = listFiles;
    }

    public ArrayList<WorkspaceTag> getListTags() {
        return listTags;
    }

    public void setListTags(ArrayList<WorkspaceTag> listTags) {
        this.setPrivate(false);
        this.listTags = listTags;
    }

    public void addTag(WorkspaceTag tag) {
        this.setPrivate(false);
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
                "name='" + getName() + '\'' +
                ", owner=" + getOwner() +
                ", quota=" + getQuota() +
                ", listFiles=" + getListFiles() +
                ", listTags=" + getListTags() +
                '}';
    }
}
