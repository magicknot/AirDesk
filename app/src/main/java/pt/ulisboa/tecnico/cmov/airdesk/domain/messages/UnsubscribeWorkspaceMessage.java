package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class UnsubscribeWorkspaceMessage extends Message {
    public static final String TAG = "UNSUBSCRIBE_WORKSPACE";

    private String email;

    private String workspaceName;

    public UnsubscribeWorkspaceMessage(String email, String workspaceName) {
        super(TAG);
        this.email = email;
        this.workspaceName = workspaceName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getWorkspaceName() {
        return this.workspaceName;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        try {
            obj.put("e-mail", email);
            obj.put("workspaceName", workspaceName);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }
}
