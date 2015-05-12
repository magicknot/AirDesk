package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class WorkspacesMessage extends Message {
    public static final String TAG = "WORKSPACES_MESSAGE";

    private String owner;

    private JSONObject workspaces;

    public WorkspacesMessage(String type, String owner, JSONObject workspaces) {
        super(type);
        this.owner = owner;
        this.workspaces = workspaces;
    }

    public String getOwner() {
        return this.owner;
    }

    public JSONObject getWorkspaces() {
        return this.workspaces;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        try {
            obj.put("owner", owner);
            obj.put("workspaces", workspaces);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }
}
