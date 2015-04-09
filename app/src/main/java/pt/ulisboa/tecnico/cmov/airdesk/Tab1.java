package pt.ulisboa.tecnico.cmov.airdesk;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.LocalWorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.util.AirdeskDataHolder;

/**
 * Created by oliveira on 31/03/15.
 */
public class Tab1 extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    private static final String TAG = "AirDesk[Tab1]";
    private static final int DIALOG_FRAGMENT_NEW_WORKSPACE = 1;
    private static final int DIALOG_FRAGMENT_INVITE_CLIENTS=3;
    private static final int ACTIVITY_WORKSPACE_FILES = 2;

    private LocalWorkspaceAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // This is the adapter we use to populate the grid.
        mAdapter = new LocalWorkspaceAdapter(this, getActivity(), R.layout.item_workspace_grid);

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

        listViewOwned.setOnItemClickListener(this);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG,"onItemClick!. "+position);

        //View listViewRow = mAdapter.getView(position, view, parent);
        //ImageView dbRowView = (ImageView) listViewRow.findViewById(R.id.workspace_overflow);

        Log.i(TAG, "title of " + position + "th element clicked");
        Intent intent = new Intent(getActivity(), WorkspaceFilesActivity.class);
        getActivity().startActivityForResult(intent, ACTIVITY_WORKSPACE_FILES);
    }

    @Override
    public void onClick(View v) {
        final int DIALOG_FRAGMENT_NEW_WORKSPACE = 4;
        final int position = Integer.valueOf(v.getTag().toString());

        Log.w(TAG, "onClick" + position);
        PopupMenu popupMenu = new PopupMenu(getActivity(), v){
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                FragmentManager fm;

                switch (item.getItemId()) {
                    case R.id.workspace_overflow_about:
                        Log.i(TAG, " clicked. about ");
                        //deleteAlbum(mAlbum);
                        return true;

                    case R.id.workspace_overflow_edit:
                        Log.i(TAG, " clicked. edit ");
                        Toast.makeText(getActivity().getBaseContext(), "You selected workspace_overflow_edit", Toast.LENGTH_SHORT).show();
                        fm = getActivity().getSupportFragmentManager();
                        EditLocalWorkspaceFragment dFragmentEditLocalWorkspace = EditLocalWorkspaceFragment.newInstance(mAdapter.getItem(position));
                        dFragmentEditLocalWorkspace.setTargetFragment(Tab1.this, DIALOG_FRAGMENT_NEW_WORKSPACE);
                        dFragmentEditLocalWorkspace.show(fm, "Dialog Fragment");
                        return true;

                    case R.id.workspace_overflow_delete:
                        Log.i(TAG, " clicked. delete "+ mAdapter.getItem(position).toString());
                        AirdeskDataHolder.getInstance().removeLocalWorkspace(mAdapter.getItem(position));
                        mAdapter.notifyDataSetChanged();
                        return true;

                    case R.id.workspace_overflow_invite:
                        Log.i(TAG, " clicked. invite ");
                        Toast.makeText(getActivity().getBaseContext(), "You selected action_new_local_workspace", Toast.LENGTH_SHORT).show();
                        fm = getActivity().getSupportFragmentManager();
                        InviteClientFragment dFragmentInviteClient = InviteClientFragment.newInstance(mAdapter.getItem(position));
                        dFragmentInviteClient.setTargetFragment(Tab1.this, DIALOG_FRAGMENT_NEW_WORKSPACE);
                        dFragmentInviteClient.show(fm, "Dialog Fragment");
                        return true;

                    default:
                        return super.onMenuItemSelected(menu, item);
                }
            }
        };
        popupMenu.inflate(R.menu.menu_item_workspace);

        // Force icons to show
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popupMenu);
            argTypes = new Class[] { boolean.class };
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            Log.w(TAG, "error forcing menu icons to show", e);
            popupMenu.show();
            return;
        }
        popupMenu.show();
    }
}

