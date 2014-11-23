package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.util.Statics;

import java.util.List;

public class SpinnerListAdapter extends ArrayAdapter<String> {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<String> mList;
    private Context ctx;
    boolean popup;

    static final String LOG = SpinnerListAdapter.class.getSimpleName();
    public SpinnerListAdapter(Context context, int textViewResourceId,
                              List<String> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Log.w(LOG,"constructor completed");
    }
    public SpinnerListAdapter(Context context, int textViewResourceId,
                              List<String> list, boolean isPopup) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popup = isPopup;
    }


    View view;


    static class ViewHolderItem {
        TextView txtString;
        ImageView image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtString = (TextView) convertView
                    .findViewById(R.id.text1);
            item.image = (ImageView) convertView
                    .findViewById(R.id.image1);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        if (popup) {
            Drawable d = ctx.getResources().getDrawable(android.R.drawable.ic_input_add);
            item.image.setImageDrawable(d);
        } else {
            if (position == 0) {
                item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_menu_help));
            } else {
                Drawable d = ctx.getResources().getDrawable(android.R.drawable.ic_dialog_alert);
                item.image.setImageDrawable(d);
            }
        }
        final String p = mList.get(position);
        item.txtString.setText(p);
        Statics.setRobotoFontLight(ctx, item.txtString);
        //Log.w(LOG, "returning the convertView ...........work done: " + p);
        return (convertView);
    }

}
