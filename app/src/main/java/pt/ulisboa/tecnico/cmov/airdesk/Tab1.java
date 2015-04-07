package pt.ulisboa.tecnico.cmov.airdesk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.WorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;

/**
 * Created by oliveira on 31/03/15.
 */
public class Tab1 extends Fragment  implements AdapterView.OnItemClickListener{

    private static final String TAG = "AirDesk[Tab1]";

//    private MeatAdapter mAdapter;
    private WorkspaceAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // This is the adapter we use to populate the grid.
//        mAdapter = new MeatAdapter(inflater, R.layout.item_meat_grid);
        mAdapter = new WorkspaceAdapter(inflater, R.layout.item_workspace_grid);

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

        // ListView Item Click Listener
        listViewOwned.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Workspace w = mAdapter.getItem(position);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        CreateWorkspaceFragment dFragment = new CreateWorkspaceFragment();


        Log.i(TAG, w.getName() + " clicked. Replacing fragment.");

        // We start the fragment transaction here. It is just an ordinary fragment transaction.

//        fragmentManager.beginTransaction().replace(R.id.container, WorkspaceFragment.newInstance()).commit();

        // Show DialogFragment
        dFragment.show(fm, "Dialog Fragment");
/*
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.tabs, CreateWorkspaceFragment.newInstance()).commit();
*/

//                )
//                        DetailFragment.newInstance(meat.resourceId, meat.title,
//                                (int) view.getX(), (int) view.getY(),
//                                view.getWidth(), view.getHeight())
//                )
                        // We push the fragment transaction to back stack. User can go back to the
                        // previous fragment by pressing back button.
//                .addToBackStack("detail")
//                .commit();

    }
}

