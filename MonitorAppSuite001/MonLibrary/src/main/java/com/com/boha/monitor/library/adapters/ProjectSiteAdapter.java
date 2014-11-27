package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.util.Statics;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ProjectSiteAdapter extends ArrayAdapter<ProjectSiteDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ProjectSiteDTO> mList;
    private Context ctx;
    private Random random;

    public ProjectSiteAdapter(Context context, int textViewResourceId,
                              List<ProjectSiteDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtLastStatus, txtTaskName;
        TextView txtTaskCount, txtStatusCount;
        TextView txtNumber, txtDate, txtStatusColor;
        ImageView imgHero;
        View statLayout1, statLayout2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        random = new Random(System.currentTimeMillis());
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.SITE_txtName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.SITE_image);
            item.txtTaskCount = (TextView) convertView
                    .findViewById(R.id.SITE_txtTaskCount);
            item.txtStatusColor = (TextView) convertView
                    .findViewById(R.id.SITE_statusColor);
            item.txtTaskName = (TextView) convertView
                    .findViewById(R.id.SITE_task);
            item.statLayout1 = convertView.findViewById(R.id.SITE_bottom);
            item.statLayout2 = convertView.findViewById(R.id.SITE_layoutStatus);
            item.txtStatusCount = (TextView) convertView
                    .findViewById(R.id.SITE_txtStatusCount);
            item.txtDate = (TextView) convertView
                    .findViewById(R.id.SITE_lastStatusDate);
            item.txtLastStatus = (TextView) convertView
                    .findViewById(R.id.SITE_lastStatus);

            item.imgHero = (ImageView) convertView.findViewById(R.id.SITE_heroImage);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ProjectSiteDTO p = mList.get(position);
        item.txtName.setText(p.getProjectSiteName());
        item.txtNumber.setText("" + (position + 1));
        item.txtStatusCount.setText("" + p.getStatusCount());
        Statics.setRobotoFontLight(ctx,item.txtName);
        if (p.getPhotoUploadList() != null && !p.getPhotoUploadList().isEmpty()) {

            String uri = Statics.IMAGE_URL + p.getPhotoUploadList().get(0).getUri();
            item.imgHero.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(uri,item.imgHero);

        } else {
            item.imgHero.setImageDrawable(ctx.getResources().getDrawable(R.drawable.house));
        }
        if (p.getLastTaskStatus() != null) {
            item.txtLastStatus.setText(p.getLastTaskStatus().getTaskStatus().getTaskStatusName());
            item.txtTaskName.setText(p.getLastTaskStatus().getTask().getTaskName());
            item.txtDate.setText(sdf.format(p.getLastTaskStatus().getStatusDate()));
            item.statLayout1.setVisibility(View.VISIBLE);
            item.statLayout2.setVisibility(View.VISIBLE);
            switch (p.getLastTaskStatus().getTaskStatus().getStatusColor()) {
                case TaskStatusDTO.STATUS_COLOR_GREEN:
                    item.txtStatusColor.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgreen_box));
                    item.txtStatusCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                    item.txtTaskCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_YELLOW:
                    item.txtStatusColor.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xorange_box));
                    item.txtStatusCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xorange_oval));
                    item.txtTaskCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xorange_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_RED:
                    item.txtStatusColor.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xred_box));
                    item.txtStatusCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xred_oval));
                    item.txtTaskCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xred_oval));
                    break;
            }

        } else {
            item.statLayout1.setVisibility(View.GONE);
            item.statLayout2.setVisibility(View.GONE);
        }
//        if (p.getPhotoUploadList() != null) {
//            int count = 0;
//            for (PhotoUploadDTO px : p.getPhotoUploadList()) {
//                if (px.getThumbFlag() != null) {
//                    count++;
//                }
//            }
//            item.txtImageCount.setText("" + count);
//        } else
//            item.txtImageCount.setText("0");
        if (p.getProjectSiteTaskList() == null) {
            item.txtTaskCount.setText("0");
        } else {
            item.txtTaskCount.setText("" + p.getProjectSiteTaskList().size());
        }

        if (p.getLastTaskStatus() != null) {
            item.txtDate.setText(sdf.format(p.getLastTaskStatus().getStatusDate()));
            item.txtDate.setVisibility(View.VISIBLE);
        }


        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontBold(ctx, item.txtDate);
        Statics.setRobotoFontLight(ctx, item.txtName);

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
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
