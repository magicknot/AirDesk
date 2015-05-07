package pt.ulisboa.tecnico.cmov.airdesk.view.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;


public class showFileFragment extends DialogFragment {

    private TextView tName;
    private Button bCreate;

    private Workspace mWorkspace;
    private String fileName;
    private String content;

    public showFileFragment() {
        // Required empty public constructor
    }

    public static showFileFragment newInstance(Workspace inWorkspace, String fileName) {
        showFileFragment fragment = new showFileFragment();

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
            View rootView = inflater.inflate(R.layout.fragment_show_file, container, false);
            getDialog().setTitle(fileName);

            tName = (TextView)rootView.findViewById(R.id.textViewFileName);

            try {
                content = mWorkspace.readFile(fileName, getActivity().getBaseContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            tName.setText(content);
        return rootView;
    }


}
