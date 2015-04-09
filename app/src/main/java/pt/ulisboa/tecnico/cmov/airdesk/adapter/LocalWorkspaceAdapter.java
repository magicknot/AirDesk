package pt.ulisboa.tecnico.cmov.airdesk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.Tab1;
import pt.ulisboa.tecnico.cmov.airdesk.util.AirdeskDataHolder;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;

/**
 * Created by oliveira on 03/04/15.
 */
public class LocalWorkspaceAdapter extends BaseAdapter {



    private static final String TAG = "WorkspaceAdapter";

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final int mResourceId;
    private final Tab1 mTab1;

    private ArrayList<LocalWorkspace> mListWorkspaces;

    public LocalWorkspaceAdapter(Tab1 t, Context context, int resourceId) {
        mListWorkspaces = AirdeskDataHolder.getInstance().getLocalWorkspaces(null);
//        Log.i(TAG, mListWorkspaces.get(0).toString());

        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mResourceId = resourceId;
        mTab1=t;
    }

    @Override
    public int getCount() {
        //Log.i(TAG, " getCount() " + mListWorkspaces.size());
        return mListWorkspaces.size();
    }

    @Override
    public LocalWorkspace getItem(int position) {
        //Log.i(TAG, " getItem(int position) " + position);
        return mListWorkspaces.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Log.i(TAG, " getItemId(int position) " + position);
        return mListWorkspaces.get(position).getWorkspaceId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;
        final ViewHolder holder;
        if (null == convertView) {
            view = mLayoutInflater.inflate(mResourceId, parent, false);
            holder = new ViewHolder();
            assert view != null;
            holder.title = (TextView) view.findViewById(R.id.title);
            holder.image = (ImageView) view.findViewById(R.id.workspace_overflow);

            holder.image.setTag(position);
            holder.image.setOnClickListener(mTab1);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        bindView(holder, position);

/*
        final int pos = position;
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do your work here
                Log.i(TAG, "title of " + pos + "th element clicked");
                Intent intent = new Intent(mContext, WorkspaceFilesActivity.class);
                ((Activity)mContext).startActivityForResult(intent, ACTIVITY_WORKSPACE_FILES);

            }
        });


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do your work here
                Log.i(TAG, "image of " + pos + "th element clicked");

                PopupMenu popupMenu = new PopupMenu(mContext, v){
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.workspace_overflow_about:
                                Log.i(TAG, " clicked. about ");
                                //deleteAlbum(mAlbum);
                                return true;

                            case R.id.workspace_overflow_edit:
                                Log.i(TAG, " clicked. edit ");
                                //renameAlbum(mAlbum);
                                return true;

                            case R.id.workspace_overflow_delete:
                                Log.i(TAG, " clicked. delete "+ mListWorkspaces.get(position).toString());
                                AirdeskDataHolder.getInstance().removeLocalWorkspace(mListWorkspaces.get(position));
                                notifyDataSetChanged();
                                return true;

                            case R.id.workspace_overflow_invite:
                                Log.i(TAG, " clicked. invite ");
                                Toast.makeText(((Activity)mContext).getBaseContext(), "You selected action_new_local_workspace", Toast.LENGTH_SHORT).show();
                                CreateWorkspaceFragment dFragment = CreateWorkspaceFragment.newInstance();

//                                dFragment.setTargetFragment(this, DIALOG_FRAGMENT_INVITE_CLIENTS);
                                return true;

                            default:
                                return super.onMenuItemSelected(menu, item);
                        }
                    }
                };
                popupMenu.inflate(R.menu.menu_item_workspace);

                // Force icons to show
                Object menuHelper;
                Class[] argTypes;
                try {
                    Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                    fMenuHelper.setAccessible(true);
                    menuHelper = fMenuHelper.get(popupMenu);
                    argTypes = new Class[] { boolean.class };
                    menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
                } catch (Exception e) {
                    Log.w(TAG, "error forcing menu icons to show", e);
                    popupMenu.show();
                    return;
                }
                popupMenu.show();
            }
        });
*/
        return view;
    }

    public void bindView(ViewHolder holder, int position) {
        Workspace w = getItem(position);

        holder.image.setImageResource(R.drawable.ic_action_overflow);
        holder.title.setText(w.getName());
    }

    public static class ViewHolder {
        public ImageView image;
        public TextView title;
    }

}