package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserTagsMessage extends Message {
    public static final String TAG = "USER_TAGS";

    private String email;

    private JSONArray tags;

    public UserTagsMessage(String email, JSONArray tags) {
        super(TAG);
        this.email = email;
        this.tags = tags;
    }

    public String getEmail() {
        return this.email;
    }

    public JSONArray getTags() {
        return this.tags;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        try {
            obj.put("e-mail", email);
            obj.put("tags", tags);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }
}
