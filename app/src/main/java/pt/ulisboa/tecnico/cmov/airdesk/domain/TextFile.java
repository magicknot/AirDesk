package pt.ulisboa.tecnico.cmov.airdesk.domain;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class TextFile implements Parcelable {
    public static final String TAG = "TEXT_FILE";

    private String name;

    private String path;

    private String acl;

    public TextFile(String name, String path, String acl) {
        this.name = name;
        this.path = path;
        this.acl = acl;
    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public String getACL() {
        return this.acl;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("name", name);
            obj.put("acl", acl); // FIXME
        } catch (JSONException e) {
            Log.e(TAG, "toJsonWithContent() - could not add attribute to Json object\n\t" +
                    e.getCause().toString());
        }

        return obj;
    }

    public static TextFile fromJson(String path, JSONObject obj) {
        TextFile file = null;
        try {
            String name = obj.getString("name");
            String acl = obj.getString("acl");
            file = new TextFile(name, path, acl);
        } catch (JSONException e) {
            Log.e(TAG, "fromJson() - could not read attribute to Json object\n\t" +
                    e.getCause().toString());
        }

        return file;
    }

    @Override
    public String toString() {
        return "TextFile{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", acl='" + acl + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(path);
        parcel.writeString(acl);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<TextFile>() {
        public TextFile createFromParcel(Parcel in) {
            return new TextFile(in.readString(), in.readString(), in.readString());
        }

        public TextFile[] newArray(int size) {
            return new TextFile[size];
        }
    };

}
