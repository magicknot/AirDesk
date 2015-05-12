package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FileMessage extends Message {
    public static final String TAG = "FILE";

    private String name;

    private String content;

    private String acl;

    public FileMessage(String name, String content, String acl) {
        super(TAG);
        this.name = name;
        this.content = content;
        this.acl = acl;
    }

    public String getName() {
        return this.name;
    }

    public String getContent() {
        return this.content;
    }

    public String getAcl() {
        return this.acl;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        try {
            obj.put("name", name);
            obj.put("content", content);
            obj.put("acl", acl);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }
}
