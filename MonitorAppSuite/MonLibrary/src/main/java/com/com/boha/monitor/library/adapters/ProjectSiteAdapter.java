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
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjectSiteAdapter extends ArrayAdapter<ProjectSiteDTO> {

    public interface ProjectSiteAdapterListener {
        public void onEditRequested(ProjectSiteDTO site, int index);
        public void onGalleryRequested(ProjectSiteDTO site, int index);
        public void onCameraRequested(ProjectSiteDTO site, int index);

        public void onTasksRequested(ProjectSiteDTO site, int index);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ProjectSiteDTO> mList;
    private Context ctx;
    private ProjectSiteAdapterListener listener;

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
        TextView txtName, txtStaffCount;
        TextView txtTaskCount, txtImageCount;
        TextView txtNumber;
        ImageView imgCamera;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            item.txtStaffCount = (TextView) convertView
                    .findViewById(R.id.SITE_txtStaffCount);
            item.txtImageCount = (TextView) convertView
                    .findViewById(R.id.SITE_imageCount);
            item.imgCamera = (ImageView) convertView.findViewById(R.id.SITE_camera);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ProjectSiteDTO p = mList.get(position);
        item.txtName.setText(p.getProjectSiteName());
        item.txtNumber.setText("" + (position + 1));
        if (p.getPhotoUploadList() != null) {
            int count = 0;
            for (PhotoUploadDTO px: p.getPhotoUploadList()) {
                if (px.getThumbFlag() != null) {
                    count++;
                }
            }
            item.txtImageCount.setText("" + count);
        }
        else
            item.txtImageCount.setText("0");
        if (p.getProjectSiteTaskList() == null) {
            item.txtTaskCount.setText("0");
        } else {
            item.txtTaskCount.setText("" + p.getProjectSiteTaskList().size());
        }

        if (p.getProjectSiteStaffList() == null) {
            item.txtStaffCount.setText("0");
        } else {
            item.txtStaffCount.setText("" + p.getProjectSiteStaffList().size());
        }
        item.txtImageCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGalleryRequested(p,position);
            }
        });
        item.txtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditRequested(p,position);
            }
        });
        item.txtTaskCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTasksRequested(p,position);
            }
        });
        item.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCameraRequested(p,position);
            }
        });
        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtImageCount);
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
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
