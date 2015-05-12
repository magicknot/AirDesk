package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class UserTagsMessage extends Message {
    public static final String TAG = "USER_TAGS";

    private String email;

    private JSONObject tags;

    public UserTagsMessage(String email, JSONObject tags) {
        super(TAG);
        this.email = email;
        this.tags = tags;
    }

    public String getEmail() {
        return this.email;
    }

    public JSONObject getTags() {
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
