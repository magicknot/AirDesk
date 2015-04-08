package pt.ulisboa.tecnico.cmov.airdesk;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.LocalWorkspaceAdapter;

/**
 * Created by oliveira on 31/03/15.
 */
public class Tab1 extends Fragment{

    private static final String TAG = "AirDesk[Tab1]";
    private static final int DIALOG_FRAGMENT_NEW_WORKSPACE = 1;

    private LocalWorkspaceAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // This is the adapter we use to populate the grid.
        mAdapter = new LocalWorkspaceAdapter(getActivity(), R.layout.item_workspace_grid);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_local_workspace, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){
            case R.id.action_new_local_workspace:
                Toast.makeText(getActivity().getBaseContext(), "You selected action_new_local_workspace", Toast.LENGTH_SHORT).show();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                CreateWorkspaceFragment dFragment = new CreateWorkspaceFragment();
                dFragment.setTargetFragment(this, DIALOG_FRAGMENT_NEW_WORKSPACE);
                dFragment.show(fm, "Dialog Fragment");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case DIALOG_FRAGMENT_NEW_WORKSPACE:
                if (resultCode == Activity.RESULT_OK) {
                    // After Ok code.
                    mAdapter.notifyDataSetChanged();
                    Log.i(TAG,"After Ok code.");
                } else if (resultCode == Activity.RESULT_CANCELED){
                    // After Cancel code.
                    Log.i(TAG,"After Cancel code.");
                }
                break;
        }    }

}

