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


public class UserTagsAdapter extends BaseAdapter {
    private static final String TAG = UserTagsAdapter.class.getSimpleName();
    private final LayoutInflater mLayoutInflater;
    private final int mResourceId;
    private List<String> mListUserTags;

    public UserTagsAdapter(LayoutInflater inflater, int resourceId) {
        mListUserTags = new ArrayList<>();
        mLayoutInflater = inflater;
        mResourceId = resourceId;
    }

    @Override
    public int getCount() {
        return mListUserTags.size();
    }

    @Override
    public Object getItem(int position) {
        return mListUserTags.get(position);
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
                mListUserTags.remove(position);
                notifyDataSetChanged();
            }
        });
        return view;
    }

    public void bindView(ViewHolder holder, int position) {
        String tag = (String)getItem(position);
//        holder.image.setImageResource(w.getWorkspaceId());
        holder.title.setText(tag);
    }

    public static class ViewHolder {
        public ImageView image;
        public TextView title;
    }

    public void add(String tag){
        mListUserTags.add(tag);
        notifyDataSetChanged();
    }

    public void clear(){
        mListUserTags.clear();
        notifyDataSetChanged();
    }

    public void setListUserTags(List<String> listTags){
        if (listTags == null)
            mListUserTags = new ArrayList<>();
        else
            mListUserTags=listTags;
        Log.i(TAG, "setListWorkspaceClients " + mListUserTags.size() + " clients");
    }

    public List<String> getListUserTags(){
        return mListUserTags;
    }



}
