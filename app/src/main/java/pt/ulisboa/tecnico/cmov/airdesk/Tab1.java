package pt.ulisboa.tecnico.cmov.airdesk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.WorkspaceAdapter;

/**
 * Created by oliveira on 31/03/15.
 */
public class Tab1 extends Fragment{

    private static final String TAG = "AirDesk[Tab1]";

    private WorkspaceAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // This is the adapter we use to populate the grid.
        mAdapter = new WorkspaceAdapter(getActivity(), R.layout.item_workspace_grid);

        // Inflate the layout with a GridView in it.
        View v =inflater.inflate(R.layout.tab_1,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get ListView object from xml
        ListView listViewOwned = (ListView) view.findViewById(R.id.list_owned_workspaces);
        // Assign adapter to ListView
        listViewOwned.setAdapter(mAdapter);

    }

}

