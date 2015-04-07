package pt.ulisboa.tecnico.cmov.airdesk.user;

/**
 * Created by oliveira on 27/03/15.
 */
public class User {
    private String email;
    private String nickname;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public String
    toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
