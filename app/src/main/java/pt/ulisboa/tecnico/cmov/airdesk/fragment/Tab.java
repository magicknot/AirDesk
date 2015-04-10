package pt.ulisboa.tecnico.cmov.airdesk.fragment;

import android.app.Activity;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.WorkspaceFilesActivity;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.WorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;

abstract public class Tab extends Fragment implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    protected static final int DIALOG_FRAGMENT_NEW_WORKSPACE = 1;
    protected static final int ACTIVITY_WORKSPACE_FILES = 2;
    protected static final int DIALOG_FRAGMENT_INVITE_CLIENTS = 3;

    private WorkspaceAdapter workspace;

    protected void setWorkspaceAdapter(WorkspaceAdapter workspace) {
        this.workspace = workspace;
    }

    protected WorkspaceAdapter getWorkspace() {
        return workspace;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_local_workspace, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DIALOG_FRAGMENT_NEW_WORKSPACE:
                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    workspace.notifyDataSetChanged();
                    Log.i(getLogTag(), "After Ok code.");
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // After Cancel code.
                    Log.i(getLogTag(), "After Cancel code.");
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final String ACTIVITY_WORKSPACE_FILES_PARCEL = "ACTIVITY_WORKSPACE_FILES_PARCEL";
        Workspace workspace = getWorkspace().getItem(i);

        //Workspace workspace = new Workspace(getWorkspace().getItem(i).getName(), getWorkspace().getItem(i).getOwner());
        //LocalWorkspace workspace = new LocalWorkspace(getWorkspace().getItem(i).getOwner(), getWorkspace().getItem(i).getName(), 100);
        //workspace.setWorkspaceId(getWorkspace().getItem(i).getWorkspaceId());
        //workspace.setFiles(getWorkspace().getItem(i).getFiles());

        Log.i(getLogTag(), "title of " + i + "th element clicked ("+ workspace+")");

        Intent intent = new Intent(getActivity(), WorkspaceFilesActivity.class);
        intent.putExtra("EXTRA_SESSION_ID", workspace);
        startActivity(intent);
/*
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ACTIVITY_WORKSPACE_FILES_PARCEL, workspace);
        intent.putExtras(bundle);
        intent.setClass(getActivity(), WorkspaceFilesActivity.class);
        getActivity().startActivityForResult(intent, ACTIVITY_WORKSPACE_FILES);
*/
    }

    public abstract String getLogTag();

}
