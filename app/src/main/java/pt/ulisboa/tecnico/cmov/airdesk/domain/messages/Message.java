package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

abstract public class Message {
    public static final String TAG = "MESSAGE";

    private String type;

    public Message(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("type", type);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add type to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }
}
