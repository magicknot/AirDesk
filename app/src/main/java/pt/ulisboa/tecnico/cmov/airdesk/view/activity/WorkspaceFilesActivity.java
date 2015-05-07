package pt.ulisboa.tecnico.cmov.airdesk.view.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Field;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.FileWorkspaceAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.view.fragment.EditFileFragment;
import pt.ulisboa.tecnico.cmov.airdesk.view.fragment.NewFileFragment;
import pt.ulisboa.tecnico.cmov.airdesk.view.fragment.showFileFragment;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;


public class WorkspaceFilesActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener,  NewFileFragment.OnItemSelectedListener {
    private final String ACTIVITY_WORKSPACE_FILES_PARCEL = "ACTIVITY_WORKSPACE_FILES_PARCEL";
    private final int DIALOG_FRAGMENT_EDIT_FILE = 7;
    private final int DIALOG_FRAGMENT_SHOW_FILE = 8;
    private static final String TAG = "WorkspaceFilesActivity";
    private FileWorkspaceAdapter fileWorkspaceAdapter;
    private Workspace workspace;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_files);
        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar1);
        toolbar.setTitle("Airdesk");
        toolbar.setSubtitle("Workspace Files");
        setSupportActionBar(toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        int i;
        Bundle b = this.getIntent().getExtras();
        if(b!=null) {
            //i = b.getInt("EXTRA_SESSION_ID");
            //Log.i("WorkspaceFilesActivity", "onCreate: getParcelableExtra: " + i);
           workspace = b.getParcelable("EXTRA_SESSION_ID");
           Log.i("WorkspaceFilesActivity", "onCreate: getParcelableExtra: " + workspace);
        }

        String filelist[] = this.workspace.listFiles(getBaseContext());


        // This is the adapter we use to populate the grid.
       fileWorkspaceAdapter = new FileWorkspaceAdapter(this, getBaseContext(),R.layout.item_workspace_grid, filelist);

        // Inflate the layout with a GridView in it.

        ListView listView = (ListView) findViewById(R.id.filesList);
        listView.setAdapter(fileWorkspaceAdapter);
        listView.setOnItemClickListener(this);

        Log.i("WorkspaceFilesActivity", "onCreate: end");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_workspace_files, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager fm;
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_new_file:
                Toast.makeText(getBaseContext(), "You selected action_new_file", Toast.LENGTH_SHORT).show();
                fm = getSupportFragmentManager();
                NewFileFragment dFragmentCreateFile = NewFileFragment.newInstance(workspace);
                dFragmentCreateFile.setTargetFragment(dFragmentCreateFile, DIALOG_FRAGMENT_EDIT_FILE);
                dFragmentCreateFile.show(fm, "Dialog Fragment");

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.w(TAG, "onItemClick" + position);

        FragmentManager fm = getSupportFragmentManager();

        showFileFragment dFragmentShowFileFragment = showFileFragment.newInstance(workspace, (String) fileWorkspaceAdapter.getItem(position));
        dFragmentShowFileFragment.setTargetFragment(dFragmentShowFileFragment, DIALOG_FRAGMENT_SHOW_FILE);
        dFragmentShowFileFragment.show(fm, "Dialog Fragment");

    }

    @Override
    public void onClick(View v) {
        final int DIALOG_FRAGMENT_NEW_WORKSPACE = 4;
        final int position = Integer.valueOf(v.getTag().toString());

        Log.w(TAG, "onClick" + position);
        PopupMenu popupMenu = new PopupMenu(this, v){
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                FragmentManager fm;

                switch (item.getItemId()) {
                    case R.id.workspace_overflow_edit:
                        Log.i(TAG, " clicked. edit ");
                        fm = getSupportFragmentManager();

                        EditFileFragment dFragmentEditFileFragment = EditFileFragment.newInstance(workspace, (String)fileWorkspaceAdapter.getItem(position) );
                        dFragmentEditFileFragment.setTargetFragment(dFragmentEditFileFragment, DIALOG_FRAGMENT_NEW_WORKSPACE);
                        dFragmentEditFileFragment.show(fm, "Dialog Fragment");

                        return true;

                    case R.id.workspace_overflow_delete:
                        Log.i(TAG, " clicked. delete " + String.valueOf(position)+ " : "+ (String)fileWorkspaceAdapter.getItem(position));
                        workspace.deleteFile((String)fileWorkspaceAdapter.getItem(position), getBaseContext());
                        fileWorkspaceAdapter.setListWorkspaceFiles(workspace.listFiles(getBaseContext()));
                        fileWorkspaceAdapter.notifyDataSetChanged();
                        return true;

                    default:
                        return super.onMenuItemSelected(menu, item);
                }
            }
        };
        popupMenu.inflate(R.menu.menu_item_files);

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
            Log.w(TAG, "error forcing menu icons to show", e);
            popupMenu.show();
            return;
        }
        popupMenu.show();
    }

    @Override
    public void onNewFileItemSelected(String fileName) {
    /*
        DetailFragment fragment = (DetailFragment) getFragmentManager()
                .findFragmentById(R.id.detailFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setText(link);
        }
    */
        Log.i(TAG, "onRssItemSelected : "+fileName);
        fileWorkspaceAdapter.setListWorkspaceFiles(this.workspace.listFiles(getBaseContext()));
        fileWorkspaceAdapter.notifyDataSetChanged();

    }


}
