package pt.ulisboa.tecnico.cmov.airdesk.fragment;


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
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;


public class EditFileFragment extends DialogFragment {

    private EditText tName;
    private Button bCreate;

    private Workspace mWorkspace;
    private String fileName;
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
        mWorkspace = getArguments().getParcelable("workspace");
        fileName = getArguments().getString("filename");
        Log.i("EditFileFragment", "file name: " + fileName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_file, container, false);
        getDialog().setTitle(fileName);

        tName = (EditText)rootView.findViewById(R.id.textEditFileName);
        bCreate = (Button)rootView.findViewById(R.id.buttonSave);

        try {
            content = mWorkspace.readFile(fileName, getActivity().getBaseContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        tName.setText(content);

        bCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                if (!tName.getText().toString().trim().isEmpty()) {
                    content = tName.getText().toString();
                    try {
                        mWorkspace.writeFile(fileName, content, getActivity().getBaseContext());
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
