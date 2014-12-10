package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StaffAdapter extends ArrayAdapter<CompanyStaffDTO> {

    public interface StaffAdapterListener {
        public void onPictureRequested(CompanyStaffDTO staff);
        public void onStatusUpdatesRequested(CompanyStaffDTO staff);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<CompanyStaffDTO> mList;
    private Context ctx;
    private StaffAdapterListener listener;

    public StaffAdapter(Context context, int textViewResourceId,
                        List<CompanyStaffDTO> list,
                        StaffAdapterListener listener) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        this.listener = listener;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName;
        ImageView photo;
        TextView txtNumber, txtCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.PSN_txtName);

            item.txtCount = (TextView) convertView
                    .findViewById(R.id.PSN_txtCounter);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.PSN_txtNum);
            item.photo = (ImageView) convertView
                    .findViewById(R.id.PSN_imagex);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final CompanyStaffDTO p = mList.get(position);
        item.txtName.setText(p.getFirstName() + " " + p.getLastName());
        item.txtNumber.setText("" + (position + 1));
        item.txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStatusUpdatesRequested(p);
            }
        });
        item.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPictureRequested(p);
            }
        });
        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);

        StringBuilder sb = new StringBuilder();
        sb.append(Statics.IMAGE_URL);
        sb.append("monitor_images/company").append(SharedUtil.getCompany(ctx).getCompanyID());
        sb.append("/companyStaff/t").append(p.getCompanyStaffID()).append(".jpg");

        System.out.println(sb.toString());
        ImageLoader.getInstance().displayImage(sb.toString(),item.photo, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                item.photo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.boy));
                item.photo.setAlpha(0.4f);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                item.photo.setAlpha(1.0f);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


        return (convertView);
    }

    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
