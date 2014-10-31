package com.com.boha.monitor.library.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.util.Statics;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ProjectSiteAdapter extends ArrayAdapter<ProjectSiteDTO> {

    public interface ProjectSiteAdapterListener {
        public void onEditRequested(ProjectSiteDTO site, int index);

        public void onGalleryRequested(ProjectSiteDTO site, int index);

        public void onCameraRequested(ProjectSiteDTO site, int index);

        public void onTasksRequested(ProjectSiteDTO site, int index);

        public void onDeleteRequested(ProjectSiteDTO site, int index);

        public void onStatusListRequested(ProjectSiteDTO site, int index);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ProjectSiteDTO> mList;
    private Context ctx;
    private ProjectSiteAdapterListener listener;
    private Random random;

    public ProjectSiteAdapter(Context context, int textViewResourceId,
                              List<ProjectSiteDTO> list,
                              ProjectSiteAdapterListener listener) {
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
        TextView txtName, txtLastStatus, txtTaskName;
        TextView txtTaskCount, txtImageCount, txtStatusCount;
        TextView txtNumber, txtDate, txtStatusColor;
        ImageView imgCamera, imgDelete, imgHero;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
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

            item.txtImageCount = (TextView) convertView
                    .findViewById(R.id.SITE_imageCount);
            item.txtStatusCount = (TextView) convertView
                    .findViewById(R.id.SITE_txtStatusCount);
            item.txtDate = (TextView) convertView
                    .findViewById(R.id.SITE_lastStatusDate);
            item.txtLastStatus = (TextView) convertView
                    .findViewById(R.id.SITE_lastStatus);

            item.imgCamera = (ImageView) convertView.findViewById(R.id.SITE_camera);
            item.imgDelete = (ImageView) convertView.findViewById(R.id.SITE_delete);
            item.imgHero = (ImageView) convertView.findViewById(R.id.SITE_heroImage);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ProjectSiteDTO p = mList.get(position);
        item.txtName.setText(p.getProjectSiteName());
        item.txtNumber.setText("" + (position + 1));
        item.txtStatusCount.setText("" + p.getStatusCount());
        if (p.getPhotoUploadList() != null && !p.getPhotoUploadList().isEmpty()) {
            int index = random.nextInt(p.getPhotoUploadList().size() - 1);
            String uri = Statics.IMAGE_URL + p.getPhotoUploadList().get(0).getUri();
            item.imgHero.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(uri,item.imgHero);
            item.imgHero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onGalleryRequested(p,position);
                }
            });
        } else {
            item.imgHero.setVisibility(View.GONE);
        }
        if (p.getLastTaskStatus() != null) {
            item.txtLastStatus.setText(p.getLastTaskStatus().getTaskStatus().getTaskStatusName());
            item.txtTaskName.setText(p.getLastTaskStatus().getTask().getTaskName());
            item.txtDate.setText(sdf.format(p.getLastTaskStatus().getStatusDate()));
            item.txtDate.setVisibility(View.VISIBLE);
            item.txtLastStatus.setVisibility(View.VISIBLE);
            item.txtTaskName.setVisibility(View.VISIBLE);
            switch (p.getLastTaskStatus().getTaskStatus().getStatusColor()) {
                case TaskStatusDTO.STATUS_COLOR_GREEN:
                    item.txtStatusColor.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_box));
                    break;
                case TaskStatusDTO.STATUS_COLOR_YELLOW:
                    item.txtStatusColor.setBackground(ctx.getResources().getDrawable(R.drawable.xorange_box));
                    break;
                case TaskStatusDTO.STATUS_COLOR_RED:
                    item.txtStatusColor.setBackground(ctx.getResources().getDrawable(R.drawable.xred_box));
                    break;
            }

        } else {
            item.txtDate.setVisibility(View.GONE);
            item.txtLastStatus.setVisibility(View.GONE);
            item.txtTaskName.setVisibility(View.GONE);
        }
        if (p.getPhotoUploadList() != null) {
            int count = 0;
            for (PhotoUploadDTO px : p.getPhotoUploadList()) {
                if (px.getThumbFlag() != null) {
                    count++;
                }
            }
            item.txtImageCount.setText("" + count);
        } else
            item.txtImageCount.setText("0");
        if (p.getProjectSiteTaskList() == null) {
            item.txtTaskCount.setText("0");
        } else {
            item.txtTaskCount.setText("" + p.getProjectSiteTaskList().size());
        }

        if (p.getLastTaskStatus() != null) {
            item.txtDate.setText(sdf.format(p.getLastTaskStatus().getStatusDate()));
            item.txtDate.setVisibility(View.VISIBLE);
        } else {
            item.txtDate.setVisibility(View.GONE);
        }

        item.txtStatusCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStatusListRequested(p, position);
            }
        });

        item.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteRequested(p, position);
            }
        });

        item.txtImageCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGalleryRequested(p, position);
            }
        });
        item.txtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditRequested(p, position);
            }
        });
        item.txtTaskCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTasksRequested(p, position);
            }
        });
        item.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCameraRequested(p, position);
            }
        });
        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtImageCount);
        Statics.setRobotoFontBold(ctx, item.txtDate);
        Statics.setRobotoFontBold(ctx, item.txtName);

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
