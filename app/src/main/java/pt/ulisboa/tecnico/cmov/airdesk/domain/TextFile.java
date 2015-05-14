package pt.ulisboa.tecnico.cmov.airdesk.domain;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class TextFile {
    public static final String TAG = "TEXT_FILE";

    private String name;

    private String path;

    private String acl;

//    private String content;

    //TODO ACL - ACL will be a a Map[User, Permissions] and this will be used to know when a File
    //           should not be sent to the user. When externalizing this class, the ACL externalized
    //           should be the one matching the remote user

    public TextFile(String name, String path, String acl) {
        this.name = name;
        this.path = path;
        this.acl = acl;
    }

//    public TextFile(String name, String path, String acl, String content) {
//        this.name = name;
//        this.path = path;
//        this.acl = acl;
//        this.content = content;
//    }

    public String getName() {
        return this.name;
    }

    public String getPath() {
        return this.path;
    }

    public String getACL() {
        return this.acl;
    }

//    public String getContent() {
//        return this.content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }

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
//
//    public JSONObject toJsonWithContent() {
//        JSONObject obj = toJson();
//        try {
//            obj.put("content", content);
//        } catch (JSONException e) {
//            Log.e(TAG, "toJsonWithContent() - could not add attribute to Json object\n\t" +
//                    e.getCause().toString());
//        }
//
//        return obj;
//    }

    public static TextFile fromJson(String path, JSONObject obj) {
        TextFile file = null;
        try {
            String name = obj.getString("name");
            String acl = obj.getString("acl");
            file = new TextFile(name, path, acl);
        } catch (JSONException e) {
            Log.e(TAG, "fromJson() - could not add attribute to Json object\n\t" +
                    e.getCause().toString());
        }

        return file;
    }

//    public static TextFile fromJsonWithFile(String path, String content, JSONObject obj) {
//        TextFile file = fromJson(path, obj);
//
//        if (file != null) {
//            try {
//                file.setContent(obj.getString("content"));
//            } catch (JSONException e) {
//                Log.e(TAG, "fromJsonWithFile() - could not add attribute to Json object\n\t" +
//                        e.getCause().toString());
//            }
//        }
//
//        return file;
//    }

}
