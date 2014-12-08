package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.util.Statics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ImageAdapter extends ArrayAdapter<PhotoUploadDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<PhotoUploadDTO> mList;
    private Context ctx;

    public ImageAdapter(Context context, int textViewResourceId,
                        List<PhotoUploadDTO> list) {
        super(context, textViewResourceId);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (list != null)
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

        PhotoUploadDTO p = mList.get(position);

        item.txtNumber.setText("" + (position + 1));
        StringBuilder sb = new StringBuilder();
        sb.append(Statics.IMAGE_URL);
        sb.append(p.getUri());

        ImageLoader.getInstance().displayImage(sb.toString(), item.img, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


        Statics.setRobotoFontLight(ctx, item.txtNumber);

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
