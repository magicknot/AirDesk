package pt.ulisboa.tecnico.cmov.airdesk.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.UserTagsAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.data.DataHolder;

public class UserTagsFragment extends DialogFragment {
    private static final String TAG = UserTagsFragment.class.getSimpleName();

    private TextView tName;
    private EditText tItem;
    private ImageButton bAddItem;
    private Button bCreate, bCancel;
    private ListView listViewItems;

    private UserTagsAdapter mTagListAdapter;

    public UserTagsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_user_tags, container, false);
        getDialog().setTitle("User Tags Preferences");

        // Get ListView object from xml
        listViewItems = (ListView) rootView.findViewById(R.id.tagList);
        mTagListAdapter = new UserTagsAdapter(inflater, R.layout.item_tag_grid);
        mTagListAdapter.setListUserTags(DataHolder.getInstance().getPreferenceTags());
        listViewItems.setAdapter(mTagListAdapter);

        tName = (TextView) rootView.findViewById(R.id.textViewEmailNickname);
        tName.setText(DataHolder.getInstance().getEmail());

        tItem = (EditText) rootView.findViewById(R.id.editTextNewItem);
        bAddItem = (ImageButton) rootView.findViewById(R.id.buttonAddItem);

        bCreate = (Button) rootView.findViewById(R.id.buttonCreate);
        bCancel = (Button) rootView.findViewById(R.id.buttonCancel);

        //Setting Button addTag Listener
        bAddItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                    if (!tItem.getText().toString().trim().isEmpty()) {
                        mTagListAdapter.add(tItem.getText().toString().trim());
                        tItem.getText().clear();
                    }
                }
        });

        bCancel.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {
                                           // Do something in response to button click
                                           Log.i(TAG, "[onCheckedChanged] Cancel");
                                           //getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, getActivity().getIntent());
                                           dismiss();
                                       }
                                   }
        );

        bCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                Log.i(TAG, "[onCheckedChanged] Create");

                DataHolder.getInstance().updatePreferenceTag(mTagListAdapter.getListUserTags());
                // FIXME Update Foreign Workspaces from network
//                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                dismiss();
            }
        });

        return rootView;
    }
}

