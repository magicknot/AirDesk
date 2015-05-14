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

import java.io.IOException;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.manager.ForeignWorkspaceManager;
import pt.ulisboa.tecnico.cmov.airdesk.manager.LocalWorkspaceManager;

public class NewFileFragment extends DialogFragment {

    private static final String TAG = NewFileFragment.class.getSimpleName();

    private EditText tName;
    private Button bCreate;

    private Workspace mWorkspace;

    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        public void onNewFileItemSelected(String fileName);
    }

    public NewFileFragment() {
        // Required empty public constructor
    }

    public static NewFileFragment newInstance(Workspace inWorkspace) {
        NewFileFragment fragment = new NewFileFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("workspace", inWorkspace);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Workspace temp = getArguments().getParcelable("workspace");
        if (temp.isLocal()) {
            mWorkspace = LocalWorkspaceManager.getInstance().getWorkspaceByName(temp.getName());
        } else {
            mWorkspace = ForeignWorkspaceManager.getInstance().getWorkspaceByName(temp.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_file, container, false);

        getDialog().setTitle("Create File");

        tName = (EditText) rootView.findViewById(R.id.textViewFileName);
        bCreate = (Button) rootView.findViewById(R.id.buttonCreate);

        bCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createFile();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implemenet MyListFragment.OnItemSelectedListener");
        }

    }

    // May also be triggered from the Activity
    public void createFile() {
        String fileName = "";
        // Do something in response to button click
        if (!tName.getText().toString().trim().isEmpty()) {
            fileName = tName.getText().toString().trim();
            Log.i(TAG, "createFile()\n\tworkspaceName = " + mWorkspace.getName() + "\n\tlocal? " + mWorkspace.isLocal() + "\n\tfilename = " + fileName);
            if (mWorkspace.isLocal()) {
                LocalWorkspaceManager.getInstance().createFile(mWorkspace.getName(), fileName);
            } else {
                ForeignWorkspaceManager.getInstance().createFile(mWorkspace.getName(), fileName);
            }
        }
        dismiss();

        // Send data to Activity
        listener.onNewFileItemSelected(fileName);
    }

}
