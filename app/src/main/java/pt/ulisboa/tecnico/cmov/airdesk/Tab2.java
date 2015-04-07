package pt.ulisboa.tecnico.cmov.airdesk;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.util.AirdeskDataSource;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;

/**
 * Created by oliveira on 31/03/15.
 */
public class Tab2 extends Fragment {

    private MeatAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // This is the adapter we use to populate the grid.
        mAdapter = new MeatAdapter(inflater, R.layout.item_meat_grid);

        // Inflate the layout with a GridView in it.
        View v =inflater.inflate(R.layout.tab_2,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Get ListView object from xml
        ListView listViewOwned = (ListView) view.findViewById(R.id.list_owned_workspaces);

        // Assign adapter to ListView
//        listViewOwned.setAdapter(mAdapter);
        Toast.makeText(view.getContext(), mAdapter.getItem(1).title, Toast.LENGTH_SHORT).show();

        // ListView Item Click Listener
//        listViewOwned.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    }

}
