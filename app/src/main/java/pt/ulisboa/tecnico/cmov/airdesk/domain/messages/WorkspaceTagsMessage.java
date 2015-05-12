package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class WorkspaceTagsMessage extends Message {
    public static final String TAG = "WORKSPACE_TAGS";

    private String workspace;

    private JSONObject tags;

    public WorkspaceTagsMessage(String workspace, JSONObject tags) {
        super(TAG);
        this.workspace = workspace;
        this.tags = tags;
    }

    public String getWorkspace() {
        return this.workspace;
    }

    public JSONObject getTags() {
        return this.tags;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        try {
            obj.put("workspace", workspace);
            obj.put("tags", tags);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }
}
