package pt.ulisboa.tecnico.cmov.airdesk.util.WifiDirect;

public class PeerDevice {
    private String deviceName;
    private String mac;
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

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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
                ", mac='" + mac + '\'' +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
