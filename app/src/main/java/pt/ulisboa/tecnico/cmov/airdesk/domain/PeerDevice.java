package pt.ulisboa.tecnico.cmov.airdesk.domain;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.manager.LocalWorkspaceManager;

public class PeerDevice {
    private static final String TAG = "PeerDevice";
    private String deviceName;
    private String ip;
    private int port;
    private String email;
    private String nickname;
    private List<String> tags;

    public PeerDevice() {

    }

    public PeerDevice(String deviceName, String ip, int port) {
        this.deviceName = deviceName;
        this.ip = ip;
        this.port = port;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void importTagsFromJson(JSONArray tags) {
        List<String> updatedTags = new ArrayList<>();
        try {
            for (int i = 0; i < tags.length(); i++) {
                JSONObject arrayItem = tags.getJSONObject(i);

                updatedTags.add(arrayItem.getString("name"));

            }
            this.setTags(updatedTags);
            LocalWorkspaceManager.getInstance().toJson(this.getEmail(), updatedTags);
        } catch (JSONException e) {
            Log.e(TAG, "importTagsFromJson() - could not read attribute to Json object\n\t" +
                    e.getCause().toString());
        }
    }

    @Override
    public String toString() {
        return "PeerDevice{" +
                "deviceName='" + deviceName + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }
}
