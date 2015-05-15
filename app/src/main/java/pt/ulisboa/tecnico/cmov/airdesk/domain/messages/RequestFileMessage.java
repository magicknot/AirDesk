package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestFileMessage extends Message {
    public static final String TAG = "REQUEST_FILE";

    private String name;
    private String workspace_name;
    private String requestor_email;

    public RequestFileMessage(String name, String workspace_name, String requestor_email) {
        super(TAG);
        this.name = name;
        this.workspace_name = workspace_name;
        this.requestor_email = requestor_email;

    }


    public String getName() {
        return this.name;
    }

    public String getWorkspace_name() {
        return this.workspace_name;
    }
    public String getRequestor_email() {
        return requestor_email;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        try {
            obj.put("name", name);
            obj.put("workspace_name", workspace_name);
            obj.put("requestor_email", requestor_email);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }



}
