package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RemoveWorkspaceMessage extends Message {
    public static final String TAG = "REMOVE_WORKSPACE";

    private String workspace;


    public RemoveWorkspaceMessage(String workspace) {
        super(TAG);
        this.workspace = workspace;
    }

    public String getWorkspace() {
        return this.workspace;
    }


    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        try {
            obj.put("workspace", workspace);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }
}
