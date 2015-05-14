package pt.ulisboa.tecnico.cmov.airdesk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.domain.PeerDevice;

public class PeerDevicesAdapter extends BaseAdapter {
    private static final String TAG = "ClientsAdapter";

    private final LayoutInflater mLayoutInflater;
    private final int mResourceId;
    private List<PeerDevice> mPeerDevices;

    public PeerDevicesAdapter(Context context, int resourceId, List<PeerDevice> peerDevices) {
        this.mPeerDevices = peerDevices;
        this.mResourceId = resourceId;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mPeerDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return mPeerDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        final ViewHolder holder;

        if (null == convertView) {
            view = mLayoutInflater.inflate(mResourceId, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.deviceName = (TextView) view.findViewById(R.id.peer_title);
            holder.deviceAddress = (TextView) view.findViewById(R.id.peer_status);
            holder.image = (ImageView) view.findViewById(R.id.peer_image);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        bindView(holder, position);
/*
        ImageView imgRemoveTag = (ImageView)view.findViewById(R.id.removeTagImage);
        imgRemoveTag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                Log.i(TAG, "[onCheckedChanged] Remove User " + position);
                mListWorkspaceClients.remove(position);
                notifyDataSetChanged();
            }
        });
*/
        return view;
    }
    public void bindView(ViewHolder holder, int position) {
        PeerDevice peer = (PeerDevice)getItem(position);
        holder.deviceName.setText(peer.getDeviceName());
        holder.deviceAddress.setText(peer.getIp()+"("+peer.getPort()+")");
        holder.image.setImageResource(R.drawable.ic_action_network_wifi);
    }

    public static class ViewHolder {
        public TextView deviceName;
        public TextView deviceAddress;
        public ImageView image;
    }

}
