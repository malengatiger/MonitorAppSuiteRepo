package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.util.Statics;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<String> mList;
    private Context ctx;

   public ImageAdapter(Context context, int textViewResourceId,
                       List<String> list) {
        super(context, textViewResourceId);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       Log.e(LOG, "...constructing ImageAdapter ...list: " + list.size());
    }

    @Override
    public int getCount() {
        //Log.e(LOG,"##### items in adapter: " + mList.size());
        return mList.size();
    }
    View view;

    static class ViewHolderItem {
        TextView txtNumber;
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.number);
            item.img = (ImageView) convertView
                    .findViewById(R.id.image);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        String p = mList.get(position);
        //Log.d(LOG, "----path: " + p);
        item.txtNumber.setText("" +(position + 1));
        File f = new File(p);
        Picasso.with(ctx).load(f).into(item.img, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e(LOG,"---------- ERROR picasso file load");
            }
        });

        Statics.setRobotoFontLight(ctx,item.txtNumber);
       // animateView(convertView);
        return (convertView);
    }

    public void animateView(final View view) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.grow_fade_in_center);
        a.setDuration(500);
        if (view == null)
            return;
        view.startAnimation(a);
    }

    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
    static final String LOG = ImageAdapter.class.getSimpleName();
}
