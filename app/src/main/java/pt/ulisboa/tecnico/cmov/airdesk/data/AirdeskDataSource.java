package pt.ulisboa.tecnico.cmov.airdesk.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.WorkspaceTag;

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
     * initialization call)
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

    public void insertUserTag(String newTag) {
        ContentValues initialValues;

        initialValues = new ContentValues();
        initialValues.put(AirdeskDbContract.UserTagsTable.COLUMN_TAG, newTag);
        mDb.insert(AirdeskDbContract.UserTagsTable.TABLE_NAME, null, initialValues);
    }

    public void updateUserTag(List<String> newTags) {
        long insertid;
        ContentValues initialValues = new ContentValues();

        mDb.delete(AirdeskDbContract.UserTagsTable.TABLE_NAME, null, null);

        for (String tag : newTags) {
            initialValues.put(AirdeskDbContract.UserTagsTable.COLUMN_TAG, tag);
            insertid = mDb.insert(AirdeskDbContract.UserTagsTable.TABLE_NAME, null, initialValues);

            Log.i(TAG, "Workspace Tag(" + tag + ")created with id " + insertid);
        }
    }

    public void deleteUserTag(String tag) {
        mDb.delete(AirdeskDbContract.UserTagsTable.TABLE_NAME, AirdeskDbContract.UserTagsTable.COLUMN_TAG + "='" + tag + "'", null);
    }

    public ArrayList<String> fetchAllUserTags() {
        String tag;
        Cursor cursor;
        ArrayList<String> userTags = new ArrayList<String>();

        cursor = mDb.query(AirdeskDbContract.UserTagsTable.TABLE_NAME, AirdeskDbContract.userTagsAllColls, null, null, null, null, null);
        Log.i(TAG, "[fetchAllUserTags] Returned " + cursor.getCount() + " rows");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                tag = cursor.getString(cursor.getColumnIndex(AirdeskDbContract.UserTagsTable.COLUMN_TAG));
                userTags.add(tag);
            }
        }

        cursor.close();
        return userTags;
    }

    /**
     * Create a new note using the title and body provided. If the note is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     *
     * @param workspace the workspace object
     * @return rowId or -1 if failed
     */
    public Workspace insertWorkspace(Workspace workspace) {
        long insertid;
        ContentValues initialValues;

        initialValues = new ContentValues();
        initialValues.put(AirdeskDbContract.WorkspacesTable.COLUMN_WORKSPACE_NAME, workspace.getName());
        initialValues.put(AirdeskDbContract.WorkspacesTable.COLUMN_OWNER, workspace.getOwner());
        initialValues.put(AirdeskDbContract.WorkspacesTable.COLUMN_QUOTA, workspace.getQuota());
        initialValues.put(AirdeskDbContract.WorkspacesTable.COLUMN_PRIVACY, workspace.isPrivate() ? 1 : 0);
        initialValues.put(AirdeskDbContract.WorkspacesTable.COLUMN_LOCATION, workspace.isLocal() ? 1 : 0);
        insertid = mDb.insert(AirdeskDbContract.WorkspacesTable.TABLE_NAME, null, initialValues);
        //
        workspace.setWorkspaceId(insertid);
        Log.i(TAG, "Workspace created with id " + insertid);

        initialValues = new ContentValues();
        if (workspace.isPrivate()) {
            //insert Clients
            for (int i = 0; i < workspace.getClients().size(); i++) {
                initialValues.put(AirdeskDbContract.WorkspaceClientsTable.COLUMN_WORKSPACE_KEY, workspace.getWorkspaceId());
                initialValues.put(AirdeskDbContract.WorkspaceClientsTable.COLUMN_CLIENT_KEY, workspace.getClients().get(i));
                insertid = mDb.insert(AirdeskDbContract.WorkspaceClientsTable.TABLE_NAME, null, initialValues);
                Log.i(TAG, "Workspace Client(" + workspace.getClients().get(i) + ")created with id " + insertid);
            }
        } else {
            //insert tags
            for (int i = 0; i < workspace.getTags().size(); i++) {
                initialValues.put(AirdeskDbContract.WorkspaceTagsTable.COLUMN_WORKSPACE_KEY, workspace.getWorkspaceId());
                initialValues.put(AirdeskDbContract.WorkspaceTagsTable.COLUMN_TAG, workspace.getTags().get(i).getTag());
                insertid = mDb.insert(AirdeskDbContract.WorkspaceTagsTable.TABLE_NAME, null, initialValues);
                Log.i(TAG, "Workspace Tag(" + workspace.getTags().get(i).getTag() + ")created with id " + insertid);
            }
        }
        return workspace;
    }

    /**
     * Delete the workspace with the given rowId
     *
     * @param rowId id of workspace to delete
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

    public ArrayList<Workspace> fetchAllWorkspaces() {

        Workspace w;
        String u;
        WorkspaceTag t;
        Cursor cursor;
        String where_clause;

        ArrayList<Workspace> workspaces = new ArrayList<Workspace>();
        cursor = mDb.query(AirdeskDbContract.WorkspacesTable.TABLE_NAME, AirdeskDbContract.workspaceAllColls, null, null, null, null, null);

        Log.i(TAG, "Returned " + cursor.getCount() + " rows");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                w = new Workspace();
                w.setWorkspaceId(cursor.getLong(cursor.getColumnIndex(AirdeskDbContract.WorkspacesTable._ID)));
                w.setName(cursor.getString(cursor.getColumnIndex(AirdeskDbContract.WorkspacesTable.COLUMN_WORKSPACE_NAME)));
                w.setQuota(cursor.getLong(cursor.getColumnIndex(AirdeskDbContract.WorkspacesTable.COLUMN_QUOTA)), mCtx);
                w.setOwner(cursor.getString(cursor.getColumnIndex(AirdeskDbContract.WorkspacesTable.COLUMN_OWNER)));
                if (cursor.getInt(cursor.getColumnIndex(AirdeskDbContract.WorkspacesTable.COLUMN_PRIVACY)) == 1)
                    w.setPrivate(true);
                else
                    w.setPrivate(false);
                workspaces.add(w);
            }
        }

        for (int i = 0; i < workspaces.size(); i++) {
            w = workspaces.get(i);
            if (w.isPrivate()) {
                //insert Clients
                where_clause = AirdeskDbContract.WorkspaceClientsTable.COLUMN_WORKSPACE_KEY + "=" + String.valueOf(w.getWorkspaceId());
                cursor = mDb.query(AirdeskDbContract.WorkspaceClientsTable.TABLE_NAME, AirdeskDbContract.workspaceClientsAllColls, where_clause, null, null, null, null);
                Log.i(TAG, AirdeskDbContract.WorkspaceClientsTable.TABLE_NAME + " Returned " + cursor.getCount() + " rows");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        u = cursor.getString(cursor.getColumnIndex(AirdeskDbContract.WorkspaceClientsTable.COLUMN_CLIENT_KEY));
                        w.addClient(u);
                    }
                }
            } else {
                //insert Tags
                where_clause = AirdeskDbContract.WorkspaceTagsTable.COLUMN_WORKSPACE_KEY + "=" + String.valueOf(w.getWorkspaceId());
                cursor = mDb.query(AirdeskDbContract.WorkspaceTagsTable.TABLE_NAME, AirdeskDbContract.workspaceTagsAllColls, where_clause, null, null, null, null);
                Log.i(TAG, AirdeskDbContract.WorkspaceTagsTable.TABLE_NAME + " Returned " + cursor.getCount() + " rows");
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        t = new WorkspaceTag();
                        t.setTag(cursor.getString(cursor.getColumnIndex(AirdeskDbContract.WorkspaceTagsTable.COLUMN_TAG)));
                        w.addTag(t);
                    }
                }
            }
        }
        cursor.close();
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

        mCursor = mDb.query(true, AirdeskDbContract.WorkspacesTable.TABLE_NAME, new String[]{AirdeskDbContract.WorkspacesTable._ID, AirdeskDbContract.WorkspacesTable.COLUMN_WORKSPACE_NAME,
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
     * @param rowId            id of workspace to update
     * @param workspaceName    the workspace name
     * @param workspaceOwner   the workspace owner
     * @param workspaceQuota   the workspace quota
     * @param workspacePrivacy the workspace privacy (public/private)
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateLocalWorkspace(long rowId, String workspaceName, String workspaceOwner, long workspaceQuota, boolean workspacePrivacy, boolean workspaceLocation) {

        ContentValues args = new ContentValues();
        args.put(AirdeskDbContract.WorkspacesTable.COLUMN_WORKSPACE_NAME, workspaceName);
        args.put(AirdeskDbContract.WorkspacesTable.COLUMN_OWNER, workspaceOwner);
        args.put(AirdeskDbContract.WorkspacesTable.COLUMN_QUOTA, workspaceQuota);
        args.put(AirdeskDbContract.WorkspacesTable.COLUMN_PRIVACY, workspacePrivacy ? 1 : 0);
        args.put(AirdeskDbContract.WorkspacesTable.COLUMN_LOCATION, workspaceLocation ? 1 : 0);

        return mDb.update(AirdeskDbContract.WorkspacesTable.TABLE_NAME, args, AirdeskDbContract.WorkspacesTable._ID + "=" + rowId, null) > 0;
    }

    public void updateLocalWorkspaceClients(long workspaceId, List<String> listClients) {
        long insertid;
        ContentValues clientValues = new ContentValues();

        mDb.delete(AirdeskDbContract.WorkspaceClientsTable.TABLE_NAME, AirdeskDbContract.WorkspaceClientsTable.COLUMN_WORKSPACE_KEY + "='" + workspaceId + "'", null);

        for (int i = 0; i < listClients.size(); i++) {
            clientValues.put(AirdeskDbContract.WorkspaceClientsTable.COLUMN_WORKSPACE_KEY, workspaceId);
            clientValues.put(AirdeskDbContract.WorkspaceClientsTable.COLUMN_CLIENT_KEY, listClients.get(i));

            insertid = mDb.insert(AirdeskDbContract.WorkspaceClientsTable.TABLE_NAME, null, clientValues);
            Log.i(TAG, "Workspace Client(" + listClients.get(i) + ")created with id " + insertid);
        }
    }

    public void updateLocalWorkspaceTags(long workspaceId, List<WorkspaceTag> listTags) {
        long insertid;
        ContentValues clientValues = new ContentValues();

        mDb.delete(AirdeskDbContract.WorkspaceTagsTable.TABLE_NAME, AirdeskDbContract.WorkspaceTagsTable.COLUMN_WORKSPACE_KEY + "='" + workspaceId + "'", null);

        for (int i = 0; i < listTags.size(); i++) {
            clientValues.put(AirdeskDbContract.WorkspaceTagsTable.COLUMN_WORKSPACE_KEY, workspaceId);
            clientValues.put(AirdeskDbContract.WorkspaceTagsTable.COLUMN_TAG, listTags.get(i).getTag());
            insertid = mDb.insert(AirdeskDbContract.WorkspaceTagsTable.TABLE_NAME, null, clientValues);
            Log.i(TAG, "Workspace Tag(" + listTags.get(i).getTag() + ")created with id " + insertid);
        }

    }

    public void updateWorkspaceFiles(long workspaceId, List<TextFile> files) {
        long insertId;
        ContentValues values = new ContentValues();

        mDb.delete(AirdeskDbContract.WorkspaceFilesTable.TABLE_NAME, AirdeskDbContract.WorkspaceTagsTable.COLUMN_WORKSPACE_KEY + "='" + workspaceId + "'", null);

        for (TextFile file : files) {
            values.put(AirdeskDbContract.WorkspaceFilesTable.COLUMN_WORKSPACE_KEY, workspaceId);
            values.put(AirdeskDbContract.WorkspaceFilesTable.COLUMN_FILE_NAME, file.getName());
            values.put(AirdeskDbContract.WorkspaceFilesTable.COLUMN_PATH, file.getPath());
            values.put(AirdeskDbContract.WorkspaceFilesTable.COLUMN_ACL, file.getACL());
            insertId = mDb.insert(AirdeskDbContract.WorkspaceFilesTable.TABLE_NAME, null, values);
            Log.i(TAG, "Workspace Files(" + file.getName() + ")created with id " + insertId);
        }

    }
}
