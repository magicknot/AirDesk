package pt.ulisboa.tecnico.cmov.airdesk.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;

/**
 * Created by oliveira on 03/04/15.
 */
public class WorkspaceAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private final int mResourceId;
    private ArrayList<Workspace> mListWorkspaces;

    public WorkspaceAdapter(LayoutInflater inflater, int resourceId) {
        mListWorkspaces = new ArrayList<>();
        init();

        mLayoutInflater = inflater;
        mResourceId = resourceId;
    }


    @Override
    public int getCount() {
        return mListWorkspaces.size();
    }

    @Override
    public Workspace getItem(int position) {
        return mListWorkspaces.get(position);
    }

    @Override
    public long getItemId(int position) {
        mListWorkspaces.get(position).getWorkspaceId();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        final ViewHolder holder;
        if (null == convertView) {
            view = mLayoutInflater.inflate(mResourceId, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.image = (ImageView) view.findViewById(R.id.image);
            holder.title = (TextView) view.findViewById(R.id.title);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        bindView(holder, position);
        return view;
    }

    public void bindView(ViewHolder holder, int position) {
        Workspace w = getItem(position);
//        holder.image.setImageResource(w.getWorkspaceId());
        holder.title.setText(w.getName());
    }

    public static class ViewHolder {
        public ImageView image;
        public TextView title;
    }

    public void init(){
        Workspace w = new Workspace("SnowTrip", 10, "x");
        w.setWorkspaceId(1);
        mListWorkspaces.add(w);
        w = new Workspace("BeachTrip", 11, "x");
        w.setWorkspaceId(2);
        mListWorkspaces.add(w);
        w = new Workspace("CampTrip", 112, "x");
        w.setWorkspaceId(3);
        mListWorkspaces.add(w);
        w = new Workspace("CityTrip", 15, "x");
        w.setWorkspaceId(4);
        mListWorkspaces.add(w);
        w = new Workspace("OtherTrip", 2, "x");
        w.setWorkspaceId(5);
        mListWorkspaces.add(w);
    }
}