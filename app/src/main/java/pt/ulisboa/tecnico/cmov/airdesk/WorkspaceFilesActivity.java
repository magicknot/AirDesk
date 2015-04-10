package pt.ulisboa.tecnico.cmov.airdesk;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.FileWorkspaceAdapter;


public class WorkspaceFilesActivity extends ActionBarActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar1);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        setContentView(R.layout.activity_workspace_files);

        String teste[] = {"ola", "ole"};
        // This is the adapter we use to populate the grid.
        FileWorkspaceAdapter fileWorkspaceAdapter = new FileWorkspaceAdapter(this, R.layout.item_workspace_grid, teste);

        // Inflate the layout with a GridView in it.

        ListView listView = (ListView) findViewById(R.id.filesList);
        listView.setAdapter(fileWorkspaceAdapter);

        Log.i("WorkspaceFilesActivity", "onCreate");
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
