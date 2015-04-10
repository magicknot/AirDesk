package pt.ulisboa.tecnico.cmov.airdesk.fragment;

import android.app.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.ClientsAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.TagsAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.user.User;
import pt.ulisboa.tecnico.cmov.airdesk.util.AirdeskDataHolder;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.WorkspaceTag;


public class CreateWorkspaceFragment extends DialogFragment {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "CreateWorkspaceFragment";

    private TextView tItemTitle;
    private EditText tName, tQuota, tItem;
    private Switch sPrivacy;
    private ImageButton bAddItem;

    private Button bCreate, bCancel;
    private ListView listViewItems;

    LinearLayout layoutItems;

    private TagsAdapter mTagListAdapter;
    private ClientsAdapter mClientsListAdapter;

    public static CreateWorkspaceFragment newInstance() {
        return new CreateWorkspaceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_workspace, container, false);
        getDialog().setTitle("Create Workspace");

        tName = (EditText)rootView.findViewById(R.id.editTextName);
        tQuota = (EditText)rootView.findViewById(R.id.editTextQuota);
        sPrivacy = (Switch)rootView.findViewById(R.id.switchPrivacy);

        layoutItems = (LinearLayout)rootView.findViewById(R.id.layoutNewTag);
        tItemTitle = (TextView)rootView.findViewById(R.id.textViewPrivacy);
        tItem = (EditText)rootView.findViewById(R.id.editTextNewTag);
        bAddItem = (ImageButton)rootView.findViewById(R.id.buttonAddTag);

        bCreate = (Button)rootView.findViewById(R.id.buttonCreate);
        bCancel = (Button)rootView.findViewById(R.id.buttonCancel);

        mTagListAdapter = new TagsAdapter(inflater, R.layout.item_tag_grid);
        mClientsListAdapter = new ClientsAdapter(inflater, R.layout.item_tag_grid);

        // Get ListView object from xml
        listViewItems = (ListView) rootView.findViewById(R.id.tagList);
        this.setWorkspacePrivate();

        //Setting Switch Listener
        sPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Log.i(TAG, "[onCheckedChanged] Checked");
                    setWorkspacePublic();
                } else {
                    // The toggle is disabled
                    Log.i(TAG, "[onCheckedChanged] unChecked");
                    setWorkspacePrivate();
                }
            }
        });

        //Setting Button addTag Listener
        bAddItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                if (sPrivacy.isChecked()) {
                    if (!tItem.getText().toString().trim().isEmpty()) {
                        WorkspaceTag tag = new WorkspaceTag(tItem.getText().toString().trim());
                        mTagListAdapter.add(tag);
                        tItem.getText().clear();
                    }
                } else {
                    if (!tItem.getText().toString().trim().isEmpty()) {
                        User client = new User(tItem.getText().toString().trim());
                        mClientsListAdapter.add(client);
                        tItem.getText().clear();
                    }
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                        // Do something in response to button click
                        Log.i(TAG, "[onCheckedChanged] Cancel");
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                    dismiss();
                    }
                }
        );
        //Setting Button createWorkspace Listener
        bCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                Log.i(TAG, "[onCheckedChanged] Create");
                SharedPreferences myPrefs = getActivity().getSharedPreferences(PREFS_NAME, 0);
                String email = myPrefs.getString("userEmail", "userEmail");
                AirdeskDataHolder.getInstance().addLocalWorkspace(email, tName.getText().toString().trim(), Integer.valueOf(tQuota.getText().toString()), sPrivacy.isChecked(), mTagListAdapter.getListWorkspacesTags(), mClientsListAdapter.getListWorkspaceClients());
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                dismiss();
            }
        });
        return rootView;
    }

    private void setWorkspacePublic(){
        tItemTitle.setText("Tags");
        tItem.setHint("new tag");
        mClientsListAdapter.clear();
        listViewItems.setAdapter(mTagListAdapter);
    }

    private void setWorkspacePrivate(){
        tItemTitle.setText("Clients");
        listViewItems.setAdapter(mClientsListAdapter);
        tItem.setHint("new email client");
        mTagListAdapter.clear();
    }
/*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle("R.string.ERROR")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("R.string.ok_button",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                            }
                        }
                )
                .setNegativeButton("R.string.cancel_button", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                    }
                })
                .create();
    }
*/
}