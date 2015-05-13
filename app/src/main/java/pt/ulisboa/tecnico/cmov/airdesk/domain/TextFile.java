package pt.ulisboa.tecnico.cmov.airdesk.domain;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class TextFile {
    public static final String TAG = "TEXT_FILE";

    private String name;

    private String path;

    private String acl;

    //TODO ACL - ACL will be a a Map[User, Permissions] and this will be used to know when a File
    //           should not be sent to the user. When externalizing this class, the ACL externalized
    //           should be the one matching the remote user

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
            obj.put("path", path);
            obj.put("acl", path);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add type to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }


}
