package pt.ulisboa.tecnico.cmov.airdesk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by oliveira on 27/03/15.
 */
public class AirdeskSQLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "AirdeskDbAdapter";

    public AirdeskSQLiteHelper(Context context) {
        super(context, AirDeskDbContract.DATABASE_NAME, null, AirDeskDbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i(TAG, "Database Created ");
        database.execSQL(AirDeskDbContract.UsersTable.CREATE_TABLE);
        database.execSQL(AirDeskDbContract.WorkspacesTable.CREATE_TABLE);
        database.execSQL(AirDeskDbContract.WorkspaceClientsTable.CREATE_TABLE);
        database.execSQL(AirDeskDbContract.WorkspaceTagsTable.CREATE_TABLE);
        database.execSQL(AirDeskDbContract.WorkspaceFilesTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        database.execSQL(AirDeskDbContract.UsersTable.DELETE_TABLE);
        database.execSQL(AirDeskDbContract.WorkspacesTable.DELETE_TABLE);
        database.execSQL(AirDeskDbContract.WorkspaceClientsTable.DELETE_TABLE);
        database.execSQL(AirDeskDbContract.WorkspaceTagsTable.DELETE_TABLE);
        database.execSQL(AirDeskDbContract.WorkspaceFilesTable.DELETE_TABLE);
        onCreate(database);
    }
}
