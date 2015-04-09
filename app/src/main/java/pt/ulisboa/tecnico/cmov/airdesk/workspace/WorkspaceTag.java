package pt.ulisboa.tecnico.cmov.airdesk.workspace;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oliveira on 29/03/15.
 */
public class WorkspaceTag implements Parcelable {

    private String tag;

    public WorkspaceTag() {

    }

    public WorkspaceTag(String tag) {
        this.tag = tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "WorkspaceTag{" +
                "tag='" + tag + '\'' +
                '}';
    }

    /////////////////////////////////////////
    // Implementation of Parcelable interface
    @SuppressWarnings("unused")

    public WorkspaceTag(Parcel source) {
        tag = source.readString();
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tag);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<WorkspaceTag>() {
        public WorkspaceTag createFromParcel(Parcel in) {
            return new WorkspaceTag(in);
        }

        public WorkspaceTag[] newArray(int size) {
            return new WorkspaceTag[size];
        }
    };
}
