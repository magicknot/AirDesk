package pt.ulisboa.tecnico.cmov.airdesk.domain.workspace;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.domain.User;

public class LocalWorkspace extends Workspace implements Parcelable {
    private List<User> clients;

    @Override
    public String toString() {
        return super.toString() +
                " LocalWorkspace{" +
                ", clients=" + clients +
                '}';
    }

    public LocalWorkspace() {
        super();
        clients = new ArrayList<>();
    }

    public LocalWorkspace(String owner, String name, long quota) {
        super(name, owner, quota);
        clients = new ArrayList<>();
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

    public void removeClient(User client) {
        int i = 0;

        for (User user : clients) {
            if (user.getEmail().toLowerCase().equals(client.getEmail().toLowerCase())) {
                clients.remove(i);
            }
            i++;
        }
    }

    /////////////////////////////////////////
    // Implementation of Parcelable interface
    @SuppressWarnings("unused")

    public LocalWorkspace(Parcel source) {
        this();
        super.setWorkspaceId(source.readLong());
        super.setQuota(source.readLong());
        super.setName(source.readString());
        super.setOwner(source.readString());
        super.setPrivate(source.readInt()==1);
        source.readList(super.tags, WorkspaceTag.class.getClassLoader());
        // FIXME: source.readList(super.files, User.class.getClassLoader());
        source.readList(clients, User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(super.getWorkspaceId());
        dest.writeLong(super.getQuota());
        dest.writeString(super.getName());
        dest.writeString(super.getOwner());
        dest.writeInt(super.isPrivate() ? 1 : 0);
        dest.writeList(super.getTags());
        // FIXME: dest.writeList(super.getFiles());
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