package pt.ulisboa.tecnico.cmov.airdesk;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.airdesk.adapter.ClientsAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.adapter.TagsAdapter;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;


public class InviteClientFragment extends DialogFragment {

    private static final String TAG = "InviteClientFragment";

    private TextView tItemTitle;
    private EditText tItem;
    private ImageButton bAddItem;
    private Button bCreate, bCancel;
    private ListView listViewItems;
    LinearLayout layoutItems;

    private ClientsAdapter mClientsListAdapter;

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

        layoutItems = (LinearLayout)rootView.findViewById(R.id.layoutNewTag);
        tItemTitle = (TextView)rootView.findViewById(R.id.textViewPrivacy);
        tItem = (EditText)rootView.findViewById(R.id.editTextNewItem);
        tItem.setHint("new email client");
        bAddItem = (ImageButton)rootView.findViewById(R.id.buttonAddItem);

        bCreate = (Button)rootView.findViewById(R.id.buttonCreate);
        bCancel = (Button)rootView.findViewById(R.id.buttonCancel);

        mClientsListAdapter = new ClientsAdapter(inflater, R.layout.item_tag_grid);
        mClientsListAdapter.setListWorkspaceClients(mWorkspace.getListClients());

        // Get ListView object from xml
        listViewItems = (ListView) rootView.findViewById(R.id.tagList);
        listViewItems.setAdapter(mClientsListAdapter);

        return rootView;
    }

}
