package pt.ulisboa.tecnico.cmov.airdesk.data;

import android.provider.BaseColumns;

public final class AirdeskDbContract {

    public static final  int    DATABASE_VERSION   = 8;
    public static final  String DATABASE_NAME      = "airdesk.db";

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private AirdeskDbContract() {}

    /* Inner class for Users table */
    public static abstract class UsersTable implements BaseColumns {
        public static final String TABLE_NAME       = "user";
        //columns
        public static final String COLUMN_NICKNAME = "nickname";
        public static final String COLUMN_EMAIL = "email";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                COLUMN_NICKNAME + " TEXT NOT NULL " +
                " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }


    /* Inner class for relation between Workspaces and Tags table */
    public static abstract class UserTagsTable implements BaseColumns {
        public static final String TABLE_NAME       = "user_tags";
        //columns
        public static final String COLUMN_TAG = "tag_name";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_TAG + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + COLUMN_TAG + ") " +
                ")";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final String[] userTagsAllColls =  {UserTagsTable.COLUMN_TAG};


    /* Inner class for Workspaces table */
    public static abstract class WorkspacesTable implements BaseColumns {
        public static final String TABLE_NAME       = "workspace";
        //columns
        public static final String COLUMN_OWNER = "owner";
        public static final String COLUMN_WORKSPACE_NAME = "name";
        public static final String COLUMN_QUOTA = "quota";
        public static final String COLUMN_PRIVACY = "privacy";
        public static final String COLUMN_LOCATION = "location";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  +
                COLUMN_OWNER + " TEXT NOT NULL, " +
                COLUMN_WORKSPACE_NAME + " TEXT NOT NULL, " +
                COLUMN_QUOTA + " INTEGER, " +
                COLUMN_PRIVACY + " INTEGER, "  +
                COLUMN_LOCATION + " INTEGER, "  +
                "FOREIGN KEY (" + COLUMN_OWNER  + ") REFERENCES " + UsersTable.TABLE_NAME + "( " + UsersTable.COLUMN_EMAIL + " )" +
                " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final String[] workspaceAllColls =  {AirdeskDbContract.WorkspacesTable._ID, AirdeskDbContract.WorkspacesTable.COLUMN_WORKSPACE_NAME,
            AirdeskDbContract.WorkspacesTable.COLUMN_OWNER, AirdeskDbContract.WorkspacesTable.COLUMN_QUOTA, AirdeskDbContract.WorkspacesTable.COLUMN_PRIVACY};

    /* Inner class for relation between Workspaces and Clients table */
    public static abstract class WorkspaceClientsTable implements BaseColumns {
        public static final String TABLE_NAME       = "workspaces_clients";
        //columns
        public static final String COLUMN_WORKSPACE_KEY = "workspace_id";
        public static final String COLUMN_CLIENT_KEY = "client_id";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_WORKSPACE_KEY + " INTEGER NOT NULL, " +
                COLUMN_CLIENT_KEY + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + COLUMN_WORKSPACE_KEY  + ", " + COLUMN_CLIENT_KEY + ") " +
                "FOREIGN KEY (" + COLUMN_WORKSPACE_KEY  + ") REFERENCES " + WorkspacesTable.TABLE_NAME + "( " + WorkspacesTable._ID + " )" +
                //"FOREIGN KEY (" + COLUMN_CLIENT_KEY  + ") REFERENCES " + UsersTable.TABLE_NAME + "( " + UsersTable.COLUMN_EMAIL + " )" +
                " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final String[] workspaceClientsAllColls =  {WorkspaceClientsTable.COLUMN_WORKSPACE_KEY, WorkspaceClientsTable.COLUMN_CLIENT_KEY};

    /* Inner class for relation between Workspaces and Tags table */
    public static abstract class WorkspaceTagsTable implements BaseColumns {
        public static final String TABLE_NAME       = "workspace_tags";
        //columns
        public static final String COLUMN_WORKSPACE_KEY = "workspace_id";
        public static final String COLUMN_TAG = "tag_name";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_WORKSPACE_KEY + " INTEGER NOT NULL, " +
                COLUMN_TAG + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + COLUMN_WORKSPACE_KEY  + ", " + COLUMN_TAG + "), " +
                "FOREIGN KEY (" + COLUMN_WORKSPACE_KEY  + ") REFERENCES " + WorkspacesTable.TABLE_NAME + "( " + WorkspacesTable._ID + " )" +
                " )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static final String[] workspaceTagsAllColls =  {WorkspaceTagsTable.COLUMN_WORKSPACE_KEY, WorkspaceTagsTable.COLUMN_TAG};

    /* Inner class for relation between Workspaces and Files table */
    public static abstract class WorkspaceFilesTable implements BaseColumns {
        public static final String TABLE_NAME       = "workspace_files";
        //columns
        public static final String COLUMN_WORKSPACE_KEY = "workspace_id";
        public static final String COLUMN_FILE_NAME = "file_name";
        public static final String COLUMN_PATH = "path";
        public static final String COLUMN_ACL = "acl";

        public static final String CREATE_TABLE = "CREATE TABLE " +
                TABLE_NAME + " (" +
                COLUMN_WORKSPACE_KEY + " INTEGER NOT NULL, " +
                COLUMN_FILE_NAME + " TEXT NOT NULL, " +
                COLUMN_PATH + " TEXT NOT NULL, " +
                COLUMN_ACL + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + COLUMN_WORKSPACE_KEY  + ", " + COLUMN_FILE_NAME + "), " +
                "FOREIGN KEY (" + COLUMN_WORKSPACE_KEY  + ") REFERENCES " + WorkspacesTable.TABLE_NAME + "( " +  WorkspacesTable._ID + " )" +
                " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
