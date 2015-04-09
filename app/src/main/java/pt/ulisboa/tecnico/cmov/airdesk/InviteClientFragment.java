package pt.ulisboa.tecnico.cmov.airdesk;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;


public class InviteClientFragment extends DialogFragment {

    private static final String TAG = "InviteClientFragment";

    private TextView tvWorkspaceName;
    private LocalWorkspace mWorkspace;

    public static InviteClientFragment newInstance(LocalWorkspace inWorkspace) {
        InviteClientFragment fragment = new InviteClientFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("workspace", inWorkspace);
        fragment.setArguments(args);

        return fragment;
    }

    public InviteClientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWorkspace = getArguments().getParcelable("workspace");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_invite_client, container, false);
        getDialog().setTitle("Invite Clients");
        tvWorkspaceName = (TextView)rootView.findViewById(R.id.textViewWorksName);
        tvWorkspaceName.setText(mWorkspace.getName());
//        Log.i(TAG, mWorkspace.getListClients().get(0).getEmail());
        //tvWorkspaceName.setText(String.valueOf(mNum));

        return rootView;
    }

}
