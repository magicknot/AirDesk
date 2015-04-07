package pt.ulisboa.tecnico.cmov.airdesk;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.TagsAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.util.AirdeskDataHolder;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.WorkspaceTag;


public class CreateWorkspaceFragment extends DialogFragment {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String TAG = "CreateWorkspaceFragment";

    private EditText tName, tQuota, tTag;
    private Switch sPrivacy;
    private ImageButton bAddTag;

    private Button bCreate, bCancel;
    private ListView listViewTags;

    private TagsAdapter mTagListAdapter;

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

        tTag = (EditText)rootView.findViewById(R.id.editTextNewTag);
        bAddTag = (ImageButton)rootView.findViewById(R.id.buttonAddTag);

        bCreate = (Button)rootView.findViewById(R.id.buttonCreate);
        bCancel = (Button)rootView.findViewById(R.id.buttonCancel);

        this.setWorkspacePrivate();

//        mAdapter = new TagsAdapter(inflater, R.layout.item_meat_grid);
        mTagListAdapter = new TagsAdapter(inflater, R.layout.item_tag_grid);

        // Get ListView object from xml
        listViewTags = (ListView) rootView.findViewById(R.id.tagList);
        // Assign adapter to ListView
        listViewTags.setAdapter(mTagListAdapter);

        //Setting Switch Listener
        sPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Log.i(TAG, "[onCheckedChanged] Checked");
//                    mTagListAdapter.init();
                    setWorkspacePublic();
                } else {
                    // The toggle is disabled
                    Log.i(TAG, "[onCheckedChanged] unChecked");
                    setWorkspacePrivate();
                    mTagListAdapter.clear();
                    //TODO: Clean Tags List
                }
            }
        });

        //Setting Button addTag Listener
        bAddTag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                if(!tTag.getText().toString().trim().isEmpty()) {
                    WorkspaceTag tag = new WorkspaceTag(tTag.getText().toString().trim());
                    mTagListAdapter.add(tag);
                }
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                        // Do something in response to button click
                        Log.i(TAG, "[onCheckedChanged] Cancel");
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
                AirdeskDataHolder.getInstance().addLocalWorkspace(email, tName.getText().toString().trim(), Integer.valueOf(tQuota.getText().toString()), sPrivacy.isChecked(), mTagListAdapter.getListWorkspacesTags());
                dismiss();
            }
        });
        return rootView;
    }

    private void setWorkspacePublic(){
        tTag.setVisibility(View.VISIBLE);
        bAddTag.setVisibility(View.VISIBLE);
    }

    private void setWorkspacePrivate(){
        tTag.setVisibility(View.INVISIBLE);
        bAddTag.setVisibility(View.INVISIBLE);

    }


}
