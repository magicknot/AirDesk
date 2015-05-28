package pt.ulisboa.tecnico.cmov.airdesk.manager;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.io.database.AirdeskDataSource;

public class UserManager {
    private static final String TAG = "AirdeskApp";

    private static UserManager holder = null;

    private Context context;
    private AirdeskDataSource db;

    private String nickname;
    private String email;
    private List<String> userPreferenceTags;

    public static UserManager getInstance() {
        if (holder == null) {
            holder = new UserManager();
        }
        return holder;
    }

    public void init(Context context) {
        this.context = context;
        this.userPreferenceTags = new ArrayList<>();

        this.db = new AirdeskDataSource(context);
        this.db.open();
        this.userPreferenceTags = this.db.fetchAllUserTags();
        db.close();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addPreferenceTag(String newTag) {
        db.open();
        db.insertUserTag(newTag);
        db.close();
        this.userPreferenceTags.add(newTag);
        //TODO notify network

        NetworkManager.getInstance().sendUserTagsMessage();
    }

    public void removePreferenceTag(String preferenceTag) {
        int i = 0;

        for (String tag : this.userPreferenceTags) {
            if (preferenceTag.toLowerCase().equals(tag.toLowerCase())) {
                //TODO notify network
                db.open();
                db.deleteUserTag(tag);
                db.close();
                this.userPreferenceTags.remove(i);
                return;
            }
            i++;
        }

        NetworkManager.getInstance().sendUserTagsMessage();
    }

    public void updatePreferenceTag(List<String> newPreferenceTags) {
        List<String> newTags, removeTags;
        newTags = new ArrayList<>();
        removeTags = new ArrayList<>();

        for (String newTag : newPreferenceTags) {
            if (!this.userPreferenceTags.contains(newTag))
                newTags.add(newTag);
        }
        Log.i(TAG, "[updatePreferenceTag]newTags:" + newTags);

        for (String tag : userPreferenceTags) {
            if (!newPreferenceTags.contains(tag))
                removeTags.add(tag);
        }
        Log.i(TAG, "[updatePreferenceTag]removeTags:" + removeTags);

        //TODO notify network
        NetworkManager.getInstance().sendUserTagsMessage();

        db.open();
        db.updateUserTag(newPreferenceTags);
        db.close();
        this.userPreferenceTags = newPreferenceTags;

    }

    public List<String> getPreferenceTags() {
        return this.userPreferenceTags;
    }

    public JSONArray tagsToJson() {
        JSONArray array = new JSONArray();

        try {
            for (String tag : userPreferenceTags) {
                JSONObject arrayItem = new JSONObject();
                arrayItem.put("name", tag);
                array.put(arrayItem);
            }
        } catch (JSONException e) {
            Log.e(TAG, "toJson() - could not add attribute to Json object\n\t" +
                    e.getCause().toString());
        }

        return array;
    }

}
