package pt.ulisboa.tecnico.cmov.airdesk.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.domain.TextFile;
import pt.ulisboa.tecnico.cmov.airdesk.view.activity.WorkspaceFilesActivity;

public class TextFileAdapter extends BaseAdapter implements Observer {
    public static final String TAG = "TextFileAdapter";
    private final LayoutInflater layoutInflater;
    private final int resourceId;
    private final WorkspaceFilesActivity filesActivity;
    private List<TextFile> files;

    public TextFileAdapter(WorkspaceFilesActivity filesActivity, Context context, int resourceId,
                           List<TextFile> files) {
        this.resourceId = resourceId;
        this.filesActivity = filesActivity;
        this.layoutInflater = LayoutInflater.from(context);
        this.files = files;

    }

    public static class ViewHolder {
        public ImageView image;
        public TextView title;
    }

    public void setTextFiles(List<TextFile> files) {
        this.files = files;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public TextFile getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Log.d(TAG, "getView started (received i=" + position + ")");

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
        Log.d(TAG, " int: " + position + ", View: " + currentView.toString() +
                ", ViewGroup: " + parent.toString());

        return currentView;
    }

    public void bindView(ViewHolder holder, int position) {
        holder.image.setImageResource(R.drawable.ic_action_overflow);
        holder.title.setText(getItem(position).getName());
    }

    @Override
    public void update(Observable observable, Object data) {
        notifyDataSetChanged();
    }

}
