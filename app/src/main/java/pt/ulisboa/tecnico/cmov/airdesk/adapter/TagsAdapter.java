package pt.ulisboa.tecnico.cmov.airdesk.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.user.User;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.WorkspaceTag;

/**
 * Created by oliveira on 06/04/15.
 */
public class TagsAdapter extends BaseAdapter {

    private static final String TAG = "TagsAdapter";

    private final LayoutInflater mLayoutInflater;
    private final int mResourceId;
    private List<WorkspaceTag> mListWorkspacesTags;

    public TagsAdapter(LayoutInflater inflater, int resourceId) {
        mListWorkspacesTags = new ArrayList<>();

        mLayoutInflater = inflater;
        mResourceId = resourceId;
    }

    @Override
    public int getCount() {
        return mListWorkspacesTags.size();
    }

    @Override
    public Object getItem(int position) {
        return mListWorkspacesTags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        final ViewHolder holder;
        Log.i(TAG, "getView()");

        if (null == convertView) {
            view = mLayoutInflater.inflate(mResourceId, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.image = (ImageView) view.findViewById(R.id.removeTagImage);
            holder.title = (TextView) view.findViewById(R.id.tagTitle);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        bindView(holder, position);

        ImageView imgRemoveTag = (ImageView)view.findViewById(R.id.removeTagImage);
        imgRemoveTag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                Log.i(TAG, "[onCheckedChanged] Remove Tag " + position);
                mListWorkspacesTags.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
    public void bindView(ViewHolder holder, int position) {
        WorkspaceTag tag = (WorkspaceTag)getItem(position);
//        holder.image.setImageResource(w.getWorkspaceId());
        holder.title.setText(tag.getTag());
    }

    public static class ViewHolder {
        public ImageView image;
        public TextView title;
    }


    public void add(WorkspaceTag tag){
        mListWorkspacesTags.add(tag);
        notifyDataSetChanged();
    }

    public void clear(){
        mListWorkspacesTags.clear();
        notifyDataSetChanged();
    }

    public void setListWorkspaceTags(List<WorkspaceTag> listTags){
        if (listTags == null)
            mListWorkspacesTags = new ArrayList<>();
        else
            mListWorkspacesTags=listTags;
        Log.i(TAG, "setListWorkspaceClients " + mListWorkspacesTags.size() + " clients");
    }

    public List<WorkspaceTag> getListWorkspacesTags(){
        return mListWorkspacesTags;
    }

}
