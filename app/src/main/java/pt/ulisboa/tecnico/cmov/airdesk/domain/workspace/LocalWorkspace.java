package pt.ulisboa.tecnico.cmov.airdesk.domain.workspace;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;

public class LocalWorkspace extends Workspace implements Parcelable {
    private List<String> clients;

    public LocalWorkspace() {
        super();
        this.clients = new ArrayList<>();
    }

    public LocalWorkspace(long quota, String name, String owner, boolean isPrivate) {
        super(quota, name, owner, isPrivate);
        this.clients = new ArrayList<>();
    }

    public List<String> getClients() {
        return clients;
    }

    public void setClients(List<String> clients) {
        if (clients == null) {
            this.clients = new ArrayList<>();
        } else {
            this.clients = clients;
        }
    }

    public void addClient(String client) {
        if (this.clients == null) {
            clients = new ArrayList<>();
        }

        this.clients.add(client);
    }

    public boolean containClient(String client) {
        for (String c : clients) {
            if (c.toLowerCase().equals(client.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void removeClient(String client) {
        int i = 0;

        for (String user : clients) {
            if (user.toLowerCase().equals(client.toLowerCase())) {
                clients.remove(i);
            }
            i++;
        }
    }

    @Override
     public String toString() {
        return super.toString() +
                " LocalWorkspace{" +
                ", clients=" + clients +
                '}';
    }

    public LocalWorkspace(Parcel source) {
        this();
        super.setWorkspaceId(source.readLong());
        super.setQuota(source.readLong());
        super.setName(source.readString());
        super.setOwner(source.readString());
        super.setPrivate(source.readInt() == 1);
        source.readList(tags, WorkspaceTag.class.getClassLoader());
        source.readList(files, TextFile.class.getClassLoader());
        source.readList(clients, String.class.getClassLoader());
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
        dest.writeList(super.getTextFiles());
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
