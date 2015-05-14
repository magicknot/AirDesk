package pt.ulisboa.tecnico.cmov.airdesk.view.fragment;

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
import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;
import pt.ulisboa.tecnico.cmov.airdesk.manager.ForeignWorkspaceManager;
import pt.ulisboa.tecnico.cmov.airdesk.manager.LocalWorkspaceManager;

public class EditFileFragment extends DialogFragment {

    private EditText tName;
    private Workspace workspace;
    private TextFile file;
    private String content;

    public EditFileFragment() {
        // Required empty public constructor
    }

    public static EditFileFragment newInstance(Workspace inWorkspace, String fileName) {
        EditFileFragment fragment = new EditFileFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("workspace", inWorkspace);
        args.putString("filename", fileName);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workspace = getArguments().getParcelable("workspace");
        file = workspace.getTextFile(getArguments().getString("filename"));
        Log.i("EditFileFragment", "file name: " + file.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_file, container, false);
        getDialog().setTitle(file.getName());

        tName = (EditText) rootView.findViewById(R.id.textEditFileName);
        Button bCreate = (Button) rootView.findViewById(R.id.buttonSave);

        if (workspace.isLocal()) {
            content = LocalWorkspaceManager.getInstance().readFile(file);
        } else {
            content = ForeignWorkspaceManager.getInstance().readFile(file);
        }

        tName.setText(content);

        bCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                if (!tName.getText().toString().trim().isEmpty()) {
                    content = tName.getText().toString();

                    try {
                        if (workspace.isLocal()) {
                            LocalWorkspaceManager.getInstance().writeFile(workspace, file, content);
                        } else {
                            ForeignWorkspaceManager.getInstance().writeFile(workspace, file, content);
                        }
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
