package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.util.Statics;

import java.util.List;

public class PopupListAdapter extends ArrayAdapter<String> {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<String> mList;
    private Context ctx;
    private String title;
    private boolean hideBanner;
    public static final int ENGINEER_LIST = 1, TASK_LIST = 2,
            STAFF_ACTIONS = 3, INVOICE_ACTIONS = 4, SITE_LIST = 5, PROJECT_LIST = 6;

    static final String LOG = PopupListAdapter.class.getSimpleName();

    public PopupListAdapter(Context context, int textViewResourceId,
                            List<String> list, boolean hideBanner) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.hideBanner = hideBanner;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public PopupListAdapter(Context context, int textViewResourceId,
                            List<String> list, String title, boolean hideBanner) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.title = title;
        this.hideBanner = hideBanner;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


        item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_input_add));
        final String p = mList.get(position);
        item.txtString.setText(p);
        Statics.setRobotoFontLight(ctx, item.txtString);
        return (convertView);
    }

}
