package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteFileMessage extends Message {
    public static final String TAG = "DELETE_FILE";

    private String name;
    private String workspace_name;

    public DeleteFileMessage(String name, String workspace_name) {
        super(TAG);
        this.name = name;
        this.workspace_name = workspace_name;

    }

    public String getName() {
        return this.name;
    }

    public String getWorkspaceName() {
        return this.workspace_name;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        try {
            obj.put("name", name);
            obj.put("workspace_name", workspace_name);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }



}
