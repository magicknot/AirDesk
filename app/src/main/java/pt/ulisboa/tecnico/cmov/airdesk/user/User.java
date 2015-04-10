package pt.ulisboa.tecnico.cmov.airdesk.user;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String email;
    private String nickname;

    public User(String email) {
        this.email = email;
    }

    public User(String email, String nickname) {
        this(email);
        this.nickname = nickname;
    }

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

    /////////////////////////////////////////
    // Implementation of Parcelable interface
    @SuppressWarnings("unused")
    public User(Parcel source) {
        email = source.readString();
        nickname = source.readString();
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(nickname);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
