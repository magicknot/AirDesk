package pt.ulisboa.tecnico.cmov.airdesk.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.user.User;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.WorkspaceTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewFileFragment extends DialogFragment {

    private EditText tName;
    private Button bCreate;

    private Workspace mWorkspace;

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
        mWorkspace = getArguments().getParcelable("workspace");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_file, container, false);

        getDialog().setTitle("Create File");

        tName = (EditText)rootView.findViewById(R.id.textViewFileName);
        bCreate = (Button)rootView.findViewById(R.id.buttonCreate);

        //Setting Button addTag Listener


        bCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fileName;
                // Do something in response to button click
                if (!tName.getText().toString().trim().isEmpty()) {
                    fileName= tName.getText().toString().trim();
                    try {
                        mWorkspace.createFile(fileName, getActivity().getBaseContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                dismiss();
            }
        });

        return rootView;
    }

}
