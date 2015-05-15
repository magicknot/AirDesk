package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class FileMessage extends Message {
    public static final String TAG = "FILE";

    private String name;
    private String owner_email;
    private String workspace_name;
    private String content;

    private String acl;

    public FileMessage(String name, String owner_email, String workspace_name, String content, String acl) {
        super(TAG);
        this.name = name;
        this.owner_email = owner_email;
        this.workspace_name = workspace_name;
        this.content = content;
        this.acl = acl;
    }

    public String getOwner_email() {
        return owner_email;
    }

    public String getWorkspace_name() {
        return workspace_name;
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
            obj.put("owner_email", owner_email);
            obj.put("workspace_name", workspace_name);
            obj.put("content", content);
            obj.put("acl", acl);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }

}
