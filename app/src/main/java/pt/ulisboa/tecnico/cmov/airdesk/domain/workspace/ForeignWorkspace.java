package pt.ulisboa.tecnico.cmov.airdesk.domain.workspace;

import android.os.Parcel;
import android.os.Parcelable;

import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;


public class ForeignWorkspace extends Workspace {

    private ForeignWorkspace() {
        super();
    }

    public ForeignWorkspace(long quota, String name, String owner, boolean isPrivate) {
        super(quota, name, owner, isPrivate);
    }

    public ForeignWorkspace(Parcel source) {
        this();
        super.setWorkspaceId(source.readLong());
        super.setQuota(source.readLong());
        super.setName(source.readString());
        super.setOwner(source.readString());
        super.setPrivate(source.readInt() == 1);
        source.readList(tags, WorkspaceTag.class.getClassLoader());
        source.readList(files, TextFile.class.getClassLoader());
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
        dest.writeList(super.getTextFiles());
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
