package pt.ulisboa.tecnico.cmov.airdesk.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


import java.lang.reflect.Field;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.WorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.data.DataHolder;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;

public class LocalWorkspaceTab extends Tab {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i(getLogTag(), "onCreateView started");
        // This is the adapter we use to populate the grid.
        WorkspaceAdapter adapter = new WorkspaceAdapter(this, getActivity(),
                R.layout.item_workspace_grid,
                DataHolder.getInstance().getLocalWorkspaces());

        setWorkspaceAdapter(adapter);
        // Inflate the layout with a GridView in it.
        View v = inflater.inflate(R.layout.tab_1, container, false);

        Log.i(getLogTag(), "onCreateView: v = null? " + String.valueOf(v == null));

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(getLogTag(), "onViewCreated started");
        super.onViewCreated(view, savedInstanceState);
        // Get ListView object from xml
        ListView listViewOwned = (ListView) view.findViewById(R.id.list_owned_workspaces);
        Log.i(getLogTag(), "onViewCreated: listViewOwned = null? " + String.valueOf(listViewOwned == null));
        // Assign adapter to ListView
        WorkspaceAdapter adapter = getWorkspaceAdapter();
        listViewOwned.setAdapter(adapter);
        listViewOwned.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(getLogTag(), "onOptionsItemSelected started");
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_new_local_workspace:
                Toast.makeText(getActivity().getBaseContext(),
                        "You selected action_new_local_workspace", Toast.LENGTH_SHORT).show();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                CreateWorkspaceFragment dFragment = new CreateWorkspaceFragment();
                dFragment.setTargetFragment(this, DIALOG_FRAGMENT_NEW_WORKSPACE);
                dFragment.show(fm, "Dialog Fragment");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final int DIALOG_FRAGMENT_NEW_WORKSPACE = 4;
        final int position = Integer.valueOf(v.getTag().toString());

        Log.w(getLogTag(), "onClick" + position);
        PopupMenu popupMenu = new PopupMenu(getActivity(), v){
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                FragmentManager fm;

                switch (item.getItemId()) {
                    case R.id.workspace_overflow_about:
                        Log.i(getLogTag(), " clicked. about ");
                        //deleteAlbum(mAlbum);
                        return true;

                    case R.id.workspace_overflow_edit:
                        Log.i(getLogTag(), " clicked. edit ");
                        Toast.makeText(getActivity().getBaseContext(),
                                "You selected workspace_overflow_edit", Toast.LENGTH_SHORT).show();
                        fm = getActivity().getSupportFragmentManager();
                        EditLocalWorkspaceFragment dFragmentEditLocalWorkspace =
                                EditLocalWorkspaceFragment.newInstance(getWorkspaceAdapter()
                                        .getItem(position));
                        dFragmentEditLocalWorkspace.setTargetFragment(LocalWorkspaceTab.this,
                                DIALOG_FRAGMENT_NEW_WORKSPACE);
                        dFragmentEditLocalWorkspace.show(fm, "Dialog Fragment");
                        return true;

                    case R.id.workspace_overflow_delete:
                        Log.i(getLogTag(), " clicked. delete "+ getWorkspaceAdapter()
                                .getItem(position).toString());

                        Workspace ws = (Workspace) getWorkspaceAdapter().getItem(position);

                        DataHolder.getInstance().removeLocalWorkspace(ws);

                        // TODO Update this to fetch user location from network
                        for (String client : ws.getClients()) {
                            WorkspaceAdapter workspace = DataHolder.getInstance()
                                    .getWorkspaceAdapterByUser(client);

                            if (workspace != null) {
                                workspace.reloadForeignWorkspaces();
                                workspace.notifyDataSetChanged();
                            }
                        }

                        getWorkspaceAdapter().notifyDataSetChanged();
                        return true;

                    case R.id.workspace_overflow_invite:
                        Log.i(getLogTag(), " clicked. invite ");
                        Toast.makeText(getActivity().getBaseContext(),
                                "You selected action_new_local_workspace", Toast.LENGTH_SHORT).show();
                        fm = getActivity().getSupportFragmentManager();
                        InviteClientFragment dFragmentInviteClient =
                                InviteClientFragment.newInstance(getWorkspaceAdapter().getItem(position));
                        dFragmentInviteClient.setTargetFragment(LocalWorkspaceTab.this,
                                DIALOG_FRAGMENT_NEW_WORKSPACE);
                        dFragmentInviteClient.show(fm, "Dialog Fragment");
                        return true;

                    default:
                        return super.onMenuItemSelected(menu, item);
                }
            }
        };
        popupMenu.inflate(R.menu.menu_item_local_workspace);

        // Force icons to show
        Object menuHelper;
        Class[] argTypes;
        try {
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popupMenu);
            argTypes = new Class[] { boolean.class };
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper,
                    true);
        } catch (Exception e) {
            Log.w(getLogTag(), "error forcing menu icons to show", e);
            popupMenu.show();
            return;
        }
        popupMenu.show();
    }

    @Override
    public String getLogTag() {
        return "LocalWorkspaceTab";
    }
}
