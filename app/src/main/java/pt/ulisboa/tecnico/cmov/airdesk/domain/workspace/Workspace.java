package pt.ulisboa.tecnico.cmov.airdesk.domain.workspace;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.util.FileManager;

//FIXME This should be abstract
public abstract class Workspace implements Parcelable {

    private long workspaceId;
    private long quota;
    private String name;
    private String owner;
    private boolean isPrivate;
    protected List<WorkspaceTag> tags;
    private List<TextFile> files;

    protected Workspace() {
        // Nothing to do here...
    }

    public Workspace(long quota, String name, String owner, boolean isPrivate) {
        super();
        this.workspaceId = -1;
        this.quota = quota;
        this.name = name;
        this.owner = owner;
        this.isPrivate = isPrivate;
        this.tags = new ArrayList<WorkspaceTag>();
        this.files = new ArrayList<TextFile>();
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
        long minimumValue = FileManager.getWorkspaceUsedSpace(name, context);
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

    public void createFile(String filename, Context context) throws IOException {
        FileManager.saveFile(this.name, filename, "", context);
    }

    public void writeFile(String filename, String content , Context context) throws IOException {
        Long usedSpace = FileManager.getWorkspaceUsedSpace(name, context);
        if (usedSpace + 2 * content.length() < quota) {
            FileManager.saveFile(this.name, filename, content, context);
        } else {
           throw new IOException("Quota exceeded (" + usedSpace + " + 2 * " + content.length() + " < " + quota + " = false)");
        }
    }

    public String readFile(String filename, Context context) throws IOException {
        return FileManager.readFile(this.name, filename, context);
    }

    public void deleteFile(String filename, Context context) {
        FileManager.deleteFile(this.name, filename, context);
    }

    public void deleteWorkspaceDirectory(Context context) {
        FileManager.deleteWorkspace(this.name, context);
    }

    public long getUsedSpaceByWorkspace(Context context) {
        return FileManager.getWorkspaceUsedSpace(this.name, context);
    }

    public String[] listFiles(Context context) {
        return FileManager.listFiles(this.name, context);
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
    }
}
