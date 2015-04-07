package pt.ulisboa.tecnico.cmov.airdesk.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.user.User;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;

/**
 * Created by oliveira on 29/03/15.
 */
public class AirdeskDataSource {
    private static final String TAG = "AirdeskDbAdapter";
    private AirdeskSQLiteHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public AirdeskDataSource(Context ctx) {
        this.mCtx = ctx;
        mDbHelper = new AirdeskSQLiteHelper(mCtx);
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws android.database.SQLException if the database could be neither opened or created
     */
    public AirdeskDataSource open() throws SQLException {
        Log.i(TAG, "Database opened ");
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        Log.i(TAG, "Database closed ");
        mDbHelper.close();
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param user the workspace object

     * @return rowId or -1 if failed
     */
    public User insertUser(User user){
        long insertid;

        ContentValues initialValues = new ContentValues();
        initialValues.put(AirdeskDbContract.UsersTable.COLUMN_EMAIL, user.getEmail());
        initialValues.put(AirdeskDbContract.UsersTable.COLUMN_NICKNAME, user.getNickname());

        insertid = mDb.insert(AirdeskDbContract.UsersTable.TABLE_NAME, null, initialValues);
        Log.i(TAG, "User Cretead with email: " + user.getEmail() + "(" +  insertid + ")");

        return user;
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param workspace the workspace object

     * @return rowId or -1 if failed
     */
    public Workspace insertWorkspace(Workspace workspace){
        long insertid;

        ContentValues initialValues = new ContentValues();
        initialValues.put(AirdeskDbContract.WorkspacesTable.COLUMN_WORKSPACE_NAME, workspace.getName());
        initialValues.put(AirdeskDbContract.WorkspacesTable.COLUMN_OWNER, workspace.getOwner());
        initialValues.put(AirdeskDbContract.WorkspacesTable.COLUMN_QUOTA, workspace.getQuota());
        initialValues.put(AirdeskDbContract.WorkspacesTable.COLUMN_PRIVACY, workspace.isPrivate() ? 1 : 0);

        insertid = mDb.insert(AirdeskDbContract.WorkspacesTable.TABLE_NAME, null, initialValues);
        workspace.setWorkspaceId(insertid);
        Log.i(TAG, "Workspace created with id " + insertid);

        return workspace;
    }

    /**
     * Delete the workspace with the given rowId
     *
     * @param rowId id of note to delete
     * @return true if deleted, false otherwise
     */
    public boolean deleteWorkspace(long rowId) {

        // Delete all files association
        mDb.delete(AirdeskDbContract.WorkspaceFilesTable.TABLE_NAME, AirdeskDbContract.WorkspaceFilesTable.COLUMN_WORKSPACE_KEY + "='" + rowId + "'", null);

        // Delete all tag association
        mDb.delete(AirdeskDbContract.WorkspaceTagsTable.TABLE_NAME, AirdeskDbContract.WorkspaceTagsTable.COLUMN_WORKSPACE_KEY + "='" + rowId + "'", null);

        // Delete all user association
        mDb.delete(AirdeskDbContract.WorkspaceClientsTable.TABLE_NAME, AirdeskDbContract.WorkspaceClientsTable.COLUMN_WORKSPACE_KEY + "='" + rowId + "'", null);

        // Delete workspace
        return mDb.delete(AirdeskDbContract.WorkspacesTable.TABLE_NAME, AirdeskDbContract.WorkspacesTable._ID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all notes in the database
     *
     * @return Cursor over all workspaces
     */

//    public Cursor fetchAllWorkspaces() {
//
//        return mDb.query(AirdeskDbContract.WorkspacesTable.TABLE_NAME, new String[] {AirdeskDbContract.WorkspacesTable._ID, AirdeskDbContract.WorkspacesTable.COLUMN_WORKSPACE_NAME,
//                AirdeskDbContract.WorkspacesTable.COLUMN_OWNER, AirdeskDbContract.WorkspacesTable.COLUMN_QUOTA, AirdeskDbContract.WorkspacesTable.COLUMN_PRIVACY}, null, null, null, null, null);
//    }

    public ArrayList<Workspace>fetchAllWorkspaces() {

        Workspace w;

        ArrayList<Workspace> workspaces = new ArrayList<Workspace>();
        Cursor cursor = mDb.query(AirdeskDbContract.WorkspacesTable.TABLE_NAME, AirdeskDbContract.workspaceAllColls, null, null, null, null, null);

        Log.i(TAG, "Returned " + cursor.getCount() + " rows");
        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                w = new Workspace();
                w.setName(cursor.getString(cursor.getColumnIndex(AirdeskDbContract.WorkspacesTable.COLUMN_WORKSPACE_NAME)));
                w.setQuota(cursor.getLong(cursor.getColumnIndex(AirdeskDbContract.WorkspacesTable.COLUMN_QUOTA)));
                w.setOwner(cursor.getString(cursor.getColumnIndex(AirdeskDbContract.WorkspacesTable.COLUMN_OWNER)));
                if(cursor.getInt(cursor.getColumnIndex(AirdeskDbContract.WorkspacesTable.COLUMN_PRIVACY)) == 1)
                    w.setPrivate(true);
                else
                    w.setPrivate(false);
                workspaces.add(w);
            }
        }
        return workspaces;
    }

    /**
     * Return a Cursor positioned at the note that matches the given rowId
     *
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetchWorkspace(long rowId) throws SQLException {

        Cursor mCursor;

        mCursor = mDb.query(true, AirdeskDbContract.WorkspacesTable.TABLE_NAME, new String[]  {AirdeskDbContract.WorkspacesTable._ID, AirdeskDbContract.WorkspacesTable.COLUMN_WORKSPACE_NAME,
                        AirdeskDbContract.WorkspacesTable.COLUMN_OWNER, AirdeskDbContract.WorkspacesTable.COLUMN_QUOTA, AirdeskDbContract.WorkspacesTable.COLUMN_PRIVACY}, AirdeskDbContract.WorkspacesTable._ID + "=" + rowId, null,
                null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed in
     *
     * @param rowId id of workspace to update
     * @param workspaceName the workspace name
     * @param workspaceOwner the workspace owner
     * @param workspaceQuota the workspace quota
     * @param workspacePrivacy the workspace privacy (public/private)
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateWorkspace(long rowId, String workspaceName, String workspaceOwner, int workspaceQuota, boolean workspacePrivacy) {

        ContentValues args = new ContentValues();
        args.put(AirdeskDbContract.WorkspacesTable.COLUMN_WORKSPACE_NAME, workspaceName);
        args.put(AirdeskDbContract.WorkspacesTable.COLUMN_OWNER, workspaceOwner);
        args.put(AirdeskDbContract.WorkspacesTable.COLUMN_QUOTA, workspaceQuota);
        args.put(AirdeskDbContract.WorkspacesTable.COLUMN_PRIVACY, workspacePrivacy ? 1 : 0);

        return mDb.update(AirdeskDbContract.WorkspacesTable.TABLE_NAME, args, AirdeskDbContract.WorkspacesTable._ID + "=" + rowId, null) > 0;
    }

    public void initializeData(){
        User user = new User();
        user.setEmail("xxx");
        user.setNickname("rasteirinho");

        this.insertUser(user);

        LocalWorkspace w = new LocalWorkspace(user.getEmail(), "SnowTrip", 10);
        insertWorkspace(w);
        w = new LocalWorkspace(user.getEmail(), "BeachTrip", 11);
        this.insertWorkspace(w);
        w = new LocalWorkspace(user.getEmail(), "CampTrip", 112);
        this.insertWorkspace(w);
        w = new LocalWorkspace(user.getEmail(), "CityTrip", 15);
        this.insertWorkspace(w);
        w = new LocalWorkspace(user.getEmail(), "OtherTrip", 2);
        this.insertWorkspace(w);
    }


}
