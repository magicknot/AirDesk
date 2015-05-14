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
        super(context, AirdeskDbContract.DATABASE_NAME, null, AirdeskDbContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i(TAG, "Database Created ");
        database.execSQL(AirdeskDbContract.UsersTable.CREATE_TABLE);
        database.execSQL(AirdeskDbContract.UserTagsTable.CREATE_TABLE);
        database.execSQL(AirdeskDbContract.WorkspacesTable.CREATE_TABLE);
        database.execSQL(AirdeskDbContract.WorkspaceClientsTable.CREATE_TABLE);
        database.execSQL(AirdeskDbContract.WorkspaceTagsTable.CREATE_TABLE);
        database.execSQL(AirdeskDbContract.WorkspaceFilesTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        database.execSQL(AirdeskDbContract.UsersTable.DELETE_TABLE);
        database.execSQL(AirdeskDbContract.UserTagsTable.DELETE_TABLE);
        database.execSQL(AirdeskDbContract.WorkspacesTable.DELETE_TABLE);
        database.execSQL(AirdeskDbContract.WorkspaceClientsTable.DELETE_TABLE);
        database.execSQL(AirdeskDbContract.WorkspaceTagsTable.DELETE_TABLE);
        database.execSQL(AirdeskDbContract.WorkspaceFilesTable.DELETE_TABLE);
        onCreate(database);
    }
}
