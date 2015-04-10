package pt.ulisboa.tecnico.cmov.airdesk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.WorkspaceFilesActivity;


public class FileWorkspaceAdapter extends BaseAdapter{
    private final LayoutInflater layoutInflater;
    private final Context context;
    private final int resourceId;
    private final WorkspaceFilesActivity filesActivity;

    private String[] mListWorkspaceFiles;

    public FileWorkspaceAdapter(WorkspaceFilesActivity filesActivity, Context context, int resourceId, String[] mListWorkspaceFiles) {
        this.mListWorkspaceFiles = mListWorkspaceFiles;
        this.context = context;
        this.resourceId = resourceId;
        this.filesActivity = filesActivity;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public static class ViewHolder {
        public ImageView image;
        public TextView title;
    }

    @Override
    public int getCount() {
        return mListWorkspaceFiles.length;
    }

    @Override
    public Object getItem(int position) {
        return mListWorkspaceFiles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.i("FileWorkspaceAdapter", "getView started (received i=" + position + ")");

        final View currentView;
        final ViewHolder holder;

        if (null == view) {
            currentView = layoutInflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            assert currentView != null;
            holder.title = (TextView) currentView.findViewById(R.id.title);
            holder.image = (ImageView) currentView.findViewById(R.id.workspace_overflow);

            holder.image.setTag(position);
            holder.image.setOnClickListener(filesActivity);

            currentView.setTag(holder);
        } else {
            currentView = view;
            holder = (ViewHolder) currentView.getTag();
        }
        bindView(holder, position);
        Log.i("WorkspaceAdapter", " int: " + position + ", View: " + currentView.toString() + ", ViewGroup: " + parent.toString());

        return currentView;
    }

    public void bindView(ViewHolder holder, int position) {
        holder.image.setImageResource(R.drawable.ic_action_overflow);
        holder.title.setText(mListWorkspaceFiles[position]);
    }

    public void setListWorkspaceFiles(String[] listWorkspaceFiles){
        mListWorkspaceFiles = listWorkspaceFiles;

    }

}
