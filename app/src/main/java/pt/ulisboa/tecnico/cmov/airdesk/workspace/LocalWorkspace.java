package pt.ulisboa.tecnico.cmov.airdesk.workspace;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.File;
import pt.ulisboa.tecnico.cmov.airdesk.user.User;

/**
 * Created by oliveira on 27/03/15.
 */
public class LocalWorkspace extends Workspace implements Parcelable {
    private List<User> listClients;

    public LocalWorkspace() {
        super();
        listClients= new ArrayList<User>();
    }

    public LocalWorkspace(String owner, String workspaceName, long workspaceQuota) {
        super(workspaceName, workspaceQuota, owner);
    }

    public List<User> getListClients() {
        return listClients;
    }

    public void setListClients(List<User> listClients) {
        this.listClients = listClients;
    }

    public void addClient(User client) {
        if (this.listClients == null)
            listClients= new ArrayList<User>();
        this.listClients.add(client);
    }

    /////////////////////////////////////////
    // Implementation of Parcelable interface
    @SuppressWarnings("unused")

    public LocalWorkspace(Parcel source) {
        source.readList( listClients, User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(listClients);
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
