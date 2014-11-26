package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
        TextView txtName, txtEmail, txtCellphone;
        ImageView imgHistory, imgEdit, imgCamera, imgInvite, photo, imgMessage;
        TextView txtNumber, txtCount, txtStaffType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.PSN_txtName);
            item.txtEmail = (TextView) convertView
                    .findViewById(R.id.PSN_txtEmail);
            item.txtCount = (TextView) convertView
                    .findViewById(R.id.PSN_txtCounter);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.PSN_txtNum);
            item.txtCellphone = (TextView) convertView
                    .findViewById(R.id.PSN_txtCell);
            item.txtStaffType = (TextView) convertView
                    .findViewById(R.id.PSN_txtStaffType);
//            item.imgHistory = (ImageView) convertView
//                    .findViewById(R.id.PA_imgStaffHistory);
            item.imgCamera = (ImageView) convertView
                    .findViewById(R.id.PA_imgCamera);
            item.imgEdit = (ImageView) convertView
                    .findViewById(R.id.PA_imgEdit);
            item.imgInvite = (ImageView) convertView
                    .findViewById(R.id.PA_imgInvite);
            item.imgMessage = (ImageView) convertView
                    .findViewById(R.id.PA_imgMessage);
            item.photo = (ImageView) convertView
                    .findViewById(R.id.PSN_imagex);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final CompanyStaffDTO p = mList.get(position);
        item.txtName.setText(p.getFirstName() + " " + p.getLastName());
        item.txtEmail.setText(p.getEmail());
        item.txtStaffType.setText(p.getCompanyStaffType().getCompanyStaffTypeName());
        if (p.getCellphone() == null) {
            item.txtCellphone.setVisibility(View.GONE);
        } else {
            item.txtCellphone.setText(p.getCellphone());
        }
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
        Statics.setRobotoFontLight(ctx, item.txtEmail);
        Statics.setRobotoFontLight(ctx, item.txtCellphone);
        Statics.setRobotoFontLight(ctx, item.txtName);
        Statics.setRobotoFontLight(ctx,item.txtStaffType);

        StringBuilder sb = new StringBuilder();
        sb.append(Statics.IMAGE_URL);
        sb.append("company").append(SharedUtil.getCompany(ctx).getCompanyID());
        sb.append("/companyStaff/t").append(p.getCompanyStaffID()).append(".jpg");

        System.out.println(sb.toString());
        ImageLoader.getInstance().displayImage(sb.toString(),item.photo, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                item.photo.setImageDrawable(ctx.getResources().getDrawable(R.drawable.black_woman));
                item.photo.setAlpha(0.25f);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                try {
//                    File f = ImageUtil.getFileFromBitmap(bitmap, "file.jpg");
//                    Location loc = Util.getLocationFromExif(f.getAbsolutePath());
//                    if (loc != null) {
//                        Log.e("StaffAdapter", "### lat: " + loc.getLatitude()
//                                + " lng: " + loc.getLongitude());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


        animateView(convertView);
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
}
