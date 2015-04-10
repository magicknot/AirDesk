package pt.ulisboa.tecnico.cmov.airdesk;


import android.app.Activity;
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

import pt.ulisboa.tecnico.cmov.airdesk.adapter.ClientsAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.TagsAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.user.User;
import pt.ulisboa.tecnico.cmov.airdesk.util.AirdeskDataHolder;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.WorkspaceTag;


public class EditLocalWorkspaceFragment extends DialogFragment {

    private static final String TAG = "EditLocalWorkspaceFrag";

    private LocalWorkspace mWorkspace;

    private TextView tItemTitlePrivacy, tName;
    private EditText tQuota, tItem;
    private Switch sPrivacy;
    private ImageButton bAddItem;

    private Button bCreate, bCancel;
    private ListView listViewItems;

    private LinearLayout layoutItems;

    private TagsAdapter mTagListAdapter;
    private ClientsAdapter mClientsListAdapter;

    public static EditLocalWorkspaceFragment newInstance(LocalWorkspace inWorkspace) {
        EditLocalWorkspaceFragment fragment = new EditLocalWorkspaceFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("workspace", inWorkspace);
        fragment.setArguments(args);

        return fragment;
    }

    public EditLocalWorkspaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWorkspace = getArguments().getParcelable("workspace");
        Log.i(TAG, "isPrivate: " + String.valueOf(mWorkspace.isPrivate()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_local_workspace, container, false);

        // Get ListView object from xml
        listViewItems = (ListView) rootView.findViewById(R.id.tagList);

///////////
        mTagListAdapter = new TagsAdapter(inflater, R.layout.item_tag_grid);
        mClientsListAdapter = new ClientsAdapter(inflater, R.layout.item_tag_grid);

        tName = (TextView)rootView.findViewById(R.id.textViewName);

        tQuota = (EditText)rootView.findViewById(R.id.editTextQuota);
        sPrivacy = (Switch)rootView.findViewById(R.id.switchPrivacy);

        layoutItems = (LinearLayout)rootView.findViewById(R.id.layoutNewTag);
        tItemTitlePrivacy = (TextView)rootView.findViewById(R.id.textViewPrivacy);
        tItem = (EditText)rootView.findViewById(R.id.editTextNewTag);
        bAddItem = (ImageButton)rootView.findViewById(R.id.buttonAddTag);

        bCreate = (Button)rootView.findViewById(R.id.buttonCreate);
        bCancel = (Button)rootView.findViewById(R.id.buttonCancel);

        getDialog().setTitle("Edit Workspace");
        tName.setText(mWorkspace.getName());
        tQuota.setText(String.valueOf(mWorkspace.getQuota()));
        sPrivacy.setChecked(!mWorkspace.isPrivate());
        if(mWorkspace.isPrivate())
            setWorkspacePrivate();
        else
            setWorkspacePublic();

        //Setting Switch Listener
        sPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //TODO: Cache changed values
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Log.i(TAG, "[onCheckedChanged] Checked");
                    mWorkspace.setPrivate(false);
                    setWorkspacePublic();
                } else {
                    // The toggle is disabled
                    Log.i(TAG, "[onCheckedChanged] unChecked");
                    mWorkspace.setPrivate(true);
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
                mWorkspace.setListClients(mClientsListAdapter.getListWorkspaceClients());
                mWorkspace.setListTags(mTagListAdapter.getListWorkspacesTags());
                Log.i(TAG, "onClick - updateLocalWorkspaceClients - isPrivate: " + String.valueOf(mWorkspace.isPrivate()));
                AirdeskDataHolder.getInstance().updateLocalWorkspaceClients(mWorkspace);
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                dismiss();
            }
        });

        return rootView;
    }

    private void setWorkspacePrivate(){
        tItemTitlePrivacy.setText("Clients");
        tItem.setHint("new email client");
        mTagListAdapter.clear();
        mClientsListAdapter.setListWorkspaceClients(mWorkspace.getListClients());
        listViewItems.setAdapter(mClientsListAdapter);
    }

    private void setWorkspacePublic(){
        tItemTitlePrivacy.setText("Tags");
        tItem.setHint("new tag");
        mClientsListAdapter.clear();
        mTagListAdapter.setListWorkspaceTags(mWorkspace.getListTags());
        listViewItems.setAdapter(mTagListAdapter);
    }

}
