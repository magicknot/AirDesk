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

/**
 * Created by oliveira on 08/04/15.
 */
public class ClientsAdapter extends BaseAdapter {

    private static final String TAG = "ClientsAdapter";

    private final LayoutInflater mLayoutInflater;
    private final int mResourceId;
    private List<User> mListWorkspaceClients;

    public ClientsAdapter(LayoutInflater inflater, int resourceId) {
        mListWorkspaceClients = new ArrayList<>();

        mLayoutInflater = inflater;
        mResourceId = resourceId;
    }

    @Override
    public int getCount() {
        return mListWorkspaceClients.size();
    }

    @Override
    public Object getItem(int position) {
        return mListWorkspaceClients.get(position);
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
                Log.i(TAG, "[onCheckedChanged] Remove User " + position);
                mListWorkspaceClients.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }
    public void bindView(ViewHolder holder, int position) {
        User client = (User)getItem(position);

//        holder.image.setImageResource(w.getWorkspaceId());
        holder.title.setText(client.getEmail());
    }

    public static class ViewHolder {
        public ImageView image;
        public TextView title;
    }


    public void add(User cliente){
        mListWorkspaceClients.add(cliente);
        notifyDataSetChanged();
    }

    public void clear(){
        mListWorkspaceClients.clear();
        notifyDataSetChanged();
    }

    public List<User> getListWorkspaceClients(){
        return mListWorkspaceClients;
    }

    public void setListWorkspaceClients(List<User> listClients){
        if (listClients == null)
            mListWorkspaceClients = new ArrayList<>();
        else
            mListWorkspaceClients=listClients;
        Log.i(TAG, "setListWorkspaceClients " + mListWorkspaceClients.size() + " clients");
    }

}
