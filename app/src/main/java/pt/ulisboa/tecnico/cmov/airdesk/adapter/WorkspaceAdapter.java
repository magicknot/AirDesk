package pt.ulisboa.tecnico.cmov.airdesk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.data.DataHolder;
import pt.ulisboa.tecnico.cmov.airdesk.view.fragment.Tab;
import pt.ulisboa.tecnico.cmov.airdesk.domain.workspace.Workspace;

public class WorkspaceAdapter extends BaseAdapter {

    public static class ViewHolder {
        public ImageView image;
        public TextView title;
    }

    private final LayoutInflater layoutInflater;

    private final Context context;

    private final int resourceId;

    private final Tab tab;

    private List<Workspace> workspaces;

    public WorkspaceAdapter(Tab tab, Context context, int resourceId, List<Workspace> workspaces) {
        this.tab = tab;
        this.context = context;
        this.resourceId = resourceId;
        this.workspaces = workspaces;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return workspaces.size();
    }

    @Override
    public Workspace getItem(int i) {
        return workspaces.get(i);
    }

    @Override
    public long getItemId(int i) {
        return workspaces.get(i).getWorkspaceId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View currentView;
        final ViewHolder holder;
        if (null == view) {
            currentView = layoutInflater.inflate(resourceId, viewGroup, false);
            holder = new ViewHolder();
            assert currentView != null;
            holder.title = (TextView) currentView.findViewById(R.id.title);
            holder.image = (ImageView) currentView.findViewById(R.id.workspace_overflow);

            holder.image.setTag(i);
            holder.image.setOnClickListener(tab);

            currentView.setTag(holder);
        } else {
            currentView = view;
            holder = (ViewHolder) currentView.getTag();
        }
        bindView(holder, i);
        return currentView;
    }

    public void bindView(ViewHolder holder, int position) {
        Workspace w = getItem(position);

        holder.image.setImageResource(R.drawable.ic_action_overflow);
        holder.title.setText(w.getName());
    }

    public void reloadForeignWorkspaces() {
        workspaces = (ArrayList<Workspace>) DataHolder.getInstance().getForeignWorkspaces();
    }
}
