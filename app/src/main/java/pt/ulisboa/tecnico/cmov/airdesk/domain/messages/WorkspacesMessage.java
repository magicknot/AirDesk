package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class WorkspacesMessage extends Message {
    public static final String TAG = "WORKSPACES";

    private String owner_email;

    private JSONArray workspaces;

    public WorkspacesMessage(String owner_email, JSONArray workspaces) {
        super(TAG);
        this.owner_email = owner_email;
        this.workspaces = workspaces;
    }

    public String getOwnerEmail() {
        return this.owner_email;
    }

    public JSONArray getWorkspaces() {
        return this.workspaces;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        try {
            obj.put("owner_email", owner_email);
            obj.put("workspaces", workspaces);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }
}
