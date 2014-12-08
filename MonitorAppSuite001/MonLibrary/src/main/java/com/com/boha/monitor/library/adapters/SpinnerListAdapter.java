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

public class SpinnerListAdapter extends ArrayAdapter<String> {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<String> mList;
    private Context ctx;
    private String title;
    private int type;
    private boolean hideBanner;
    public static final int ENGINEER_LIST = 1, TASK_LIST = 2,
            STAFF_ACTIONS = 3, INVOICE_ACTIONS = 4, SITE_LIST = 5, PROJECT_LIST = 6;

    static final String LOG = SpinnerListAdapter.class.getSimpleName();
    public SpinnerListAdapter(Context context, int textViewResourceId,
                              List<String> list, int action, boolean hideBanner) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.hideBanner = hideBanner;
        type = action;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Log.w(LOG,"constructor completed");
    }
    public SpinnerListAdapter(Context context, int textViewResourceId,
                              List<String> list, int action, String title, boolean hideBanner) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.title = title;
        type = action;
        this.hideBanner = hideBanner;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Log.w(LOG,"constructor completed");
    }


    View view;


    static class ViewHolderItem {
        TextView txtString, txtTitle;
        ImageView image, banner;
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
            item.txtTitle = (TextView) convertView
                    .findViewById(R.id.title);
            item.image = (ImageView) convertView
                    .findViewById(R.id.image1);
            item.banner = (ImageView) convertView
                    .findViewById(R.id.banner);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

//        if (title != null) {
//            if (position == 0) {
//                item.txtTitle.setVisibility(View.VISIBLE);
//                item.banner.setVisibility(View.VISIBLE);
//                item.txtTitle.setText(title);
//            } else {
//                item.txtTitle.setVisibility(View.GONE);
//                item.banner.setVisibility(View.GONE);
//            }
//        } else {
//            item.txtTitle.setVisibility(View.GONE);
//            if (position == 0) {
//                item.banner.setVisibility(View.VISIBLE);
//            } else {
//                item.banner.setVisibility(View.GONE);
//            }
//        }
//        if (hideBanner) {
//            item.banner.setVisibility(View.GONE);
//        }
        switch (type) {
            case INVOICE_ACTIONS:
                item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_input_add));
                break;
            case ENGINEER_LIST:
                if (position == 0) {
                    item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_menu_help));
                } else {
                    item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_dialog_alert));
                }
                break;
            case TASK_LIST:
                if (position == 0) {
                    item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_menu_help));
                } else {
                    item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_dialog_alert));
                }
                break;
            case STAFF_ACTIONS:
                item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_menu_edit));
                break;
            case SITE_LIST:
                item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_menu_edit));
                break;
            case PROJECT_LIST:
                item.image.setImageDrawable(ctx.getResources().getDrawable(android.R.drawable.ic_menu_edit));
                break;
        }

        final String p = mList.get(position);
        item.txtString.setText(p);
        Statics.setRobotoFontLight(ctx, item.txtString);
        return (convertView);
    }

}
