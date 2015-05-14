package pt.ulisboa.tecnico.cmov.airdesk.domain.messages;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnnounceMessage extends Message {
    public static final String TAG = "ANNOUNCE";

    private String email;

    private String nickname;

    private JSONArray tags;

    public AnnounceMessage(String email, String nickname, JSONArray tags) {
        super(TAG);
        this.email = email;
        this.nickname = nickname;
        this.tags = tags;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNickname() {
        return this.nickname;
    }

    public JSONArray getTags() {
        return this.tags;
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj = super.toJson();
        try {
            obj.put("e-mail", email);
            obj.put("nickname", nickname);
            obj.put("tags", tags);
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" + e.getCause().toString());
        }

        return obj;
    }
}
