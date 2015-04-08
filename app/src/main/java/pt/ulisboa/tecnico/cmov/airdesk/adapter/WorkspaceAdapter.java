package pt.ulisboa.tecnico.cmov.airdesk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import pt.ulisboa.tecnico.cmov.airdesk.R;
import pt.ulisboa.tecnico.cmov.airdesk.WorkspaceFilesActivity;
import pt.ulisboa.tecnico.cmov.airdesk.util.AirdeskDataHolder;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.LocalWorkspace;
import pt.ulisboa.tecnico.cmov.airdesk.workspace.Workspace;

/**
 * Created by oliveira on 03/04/15.
 */
public class WorkspaceAdapter extends BaseAdapter {

    private static final int ACTIVITY_WORKSPACE_FILES = 2;

    private static final String TAG = "WorkspaceAdapter";

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private final int mResourceId;
    private ArrayList<LocalWorkspace> mListWorkspaces;

    public WorkspaceAdapter(Context context, int resourceId) {
//        mListWorkspaces = new ArrayList<>();
//        init();
        mListWorkspaces = AirdeskDataHolder.getInstance().getLocalWorkspaces(null);
Log.i(TAG, mListWorkspaces.get(0).toString());

        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mResourceId = resourceId;
    }


    @Override
    public int getCount() {
        Log.i(TAG, " getCount() " + mListWorkspaces.size());
        return mListWorkspaces.size();
    }

    @Override
    public Workspace getItem(int position) {
        Log.i(TAG, " getItem(int position) " + position);
        return mListWorkspaces.get(position);
    }

    @Override
    public long getItemId(int position) {
        Log.i(TAG, " getItemId(int position) " + position);
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
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        bindView(holder, position);


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

                PopupMenu popupMenu = new PopupMenu(mContext, view){
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
                                //setAlbumCover(mAlbum);
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

    public void init(){
        LocalWorkspace w = new LocalWorkspace("x", "SnowTrip", 10);
        w.setWorkspaceId(1);
        mListWorkspaces.add(w);
        w = new LocalWorkspace("x", "BeachTrip", 11);
        w.setWorkspaceId(2);
        mListWorkspaces.add(w);
        w = new LocalWorkspace("x", "CampTrip", 112);
        w.setWorkspaceId(3);
        mListWorkspaces.add(w);
        w = new LocalWorkspace("x", "CityTrip", 15);
        w.setWorkspaceId(4);
        mListWorkspaces.add(w);
        w = new LocalWorkspace("x", "OtherTrip", 2);
        w.setWorkspaceId(5);
        mListWorkspaces.add(w);
    }
}