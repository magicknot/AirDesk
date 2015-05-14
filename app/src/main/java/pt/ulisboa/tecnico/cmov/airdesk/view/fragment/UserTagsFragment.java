package pt.ulisboa.tecnico.cmov.airdesk.view.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;

public class UserTagsFragment extends DialogFragment {
    private static final String TAG = UserTagsFragment.class.getSimpleName();

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

        return rootView;
    }

}

