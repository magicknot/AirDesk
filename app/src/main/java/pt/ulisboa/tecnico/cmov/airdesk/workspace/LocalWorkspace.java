package pt.ulisboa.tecnico.cmov.airdesk.workspace;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.user.User;

public class LocalWorkspace extends Workspace implements Parcelable {

    private long quota;
    private List<User> clients;

    @Override
    public String toString() {
        return super.toString() +
                " LocalWorkspace{" +
                "quota=" + quota +
                ", clients=" + clients +
                '}';
    }

    public LocalWorkspace() {
        super();
        clients = new ArrayList<>();
    }

    public LocalWorkspace(String owner, String name, long quota) {
        super(name, owner);
        this.quota = quota;
        clients = new ArrayList<>();
    }

    public long getQuota() {
        return quota;
    }

    public void setQuota(long quota) {
        this.quota = quota;
    }

    public List<User> getClients() {
        return clients;
    }

    public void setClients(List<User> clients) {
        if (clients == null) {
            this.clients = new ArrayList<>();
        } else {
            this.clients = clients;
        }
    }

    public void addClient(User client) {
        if (this.clients == null) {
            clients = new ArrayList<>();
        }

        this.clients.add(client);
    }

    public boolean containClient(User client) {
        for (User c : clients) {
            if (c.getEmail().toLowerCase().equals(client.getEmail().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /////////////////////////////////////////
    // Implementation of Parcelable interface
    @SuppressWarnings("unused")

    public LocalWorkspace(Parcel source) {
        super.setWorkspaceId(source.readLong());
        super.setName(source.readString());
        super.setOwner(source.readString());
        super.setPrivate(source.readInt()==1);
        source.readList(super.tags, WorkspaceTag.class.getClassLoader());
        source.readList(super.files, User.class.getClassLoader());
        quota = source.readLong();
        source.readList(clients, User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(super.getWorkspaceId());
        dest.writeString(super.getName());
        dest.writeString(super.getOwner());
        dest.writeInt(super.isPrivate() ? 1 : 0);
        dest.writeList(super.getTags());
        dest.writeList(super.getFiles());
        dest.writeLong(quota);
        dest.writeList(clients);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator<LocalWorkspace>() {
        public LocalWorkspace createFromParcel(Parcel in) {
            return new LocalWorkspace(in);
        }

        public LocalWorkspace[] newArray(int size) {
            return new LocalWorkspace[size];
        }
    };
}
