package pt.ulisboa.tecnico.cmov.airdesk.domain;

public class PeerDevice {
    private String deviceName;
    private String ip;
    private int port;
    private String email;
    private String nickname;


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

    @Override
    public String toString() {
        return "PeerDevice{" +
                "deviceName='"+deviceName + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
