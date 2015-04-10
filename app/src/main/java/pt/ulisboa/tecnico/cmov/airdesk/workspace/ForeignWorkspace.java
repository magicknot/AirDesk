package pt.ulisboa.tecnico.cmov.airdesk.workspace;

import android.os.Parcel;
import android.os.Parcelable;

import pt.ulisboa.tecnico.cmov.airdesk.user.User;

public class ForeignWorkspace extends Workspace {

    public ForeignWorkspace() {
        super();
    }

    public ForeignWorkspace(String workspaceName, String owner, long quota) {
        super(workspaceName, owner, quota);
    }

    public ForeignWorkspace(Parcel source) {
        this();
        super.setWorkspaceId(source.readLong());
        super.setQuota(source.readLong());
        super.setName(source.readString());
        super.setOwner(source.readString());
        super.setPrivate(source.readInt()==1);
        source.readList(super.tags, WorkspaceTag.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(super.getWorkspaceId());
        dest.writeLong(super.getQuota());
        dest.writeString(super.getName());
        dest.writeString(super.getOwner());
        dest.writeInt(super.isPrivate() ? 1 : 0);
        dest.writeList(super.getTags());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<ForeignWorkspace>() {
        public ForeignWorkspace createFromParcel(Parcel in) {
            return new ForeignWorkspace(in);
        }

        public ForeignWorkspace[] newArray(int size) {
            return new ForeignWorkspace[size];
        }
    };

}
