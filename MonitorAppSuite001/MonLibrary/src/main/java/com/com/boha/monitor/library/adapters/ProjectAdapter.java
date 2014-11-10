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
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ProjectAdapter extends ArrayAdapter<ProjectDTO> {

    public interface ProjectAdapterListener {
        public void onEditRequested(ProjectDTO project);
        public void onProjectSitesRequested(ProjectDTO project);
        public void onPictureRequested(ProjectDTO project);
        public void onGalleryRequested(ProjectDTO project);
        public void onMapRequested(ProjectDTO project);
    }
    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ProjectDTO> mList;
    private Context ctx;
    private ProjectAdapterListener listener;

   public ProjectAdapter(Context context, int textViewResourceId,
                         List<ProjectDTO> list,
                         ProjectAdapterListener listener) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.listener = listener;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtStatusCount;
        TextView txtSiteCount, txtImageCount;
        TextView txtNumber, txtDesc, txtClient;
        ImageView imgCamera, imgMap;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.PROJ_txtName);
            item.txtClient = (TextView) convertView
                    .findViewById(R.id.PROJ_txtClientName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.PROJ_image);
            item.txtSiteCount = (TextView) convertView
                    .findViewById(R.id.PROJ_txtCount);
            item.txtStatusCount = (TextView) convertView
                   .findViewById(R.id.PROJ_txtStatusCount);
            item.txtDesc = (TextView) convertView
                    .findViewById(R.id.PROJ_txtDesc);
            item.txtImageCount = (TextView) convertView
                    .findViewById(R.id.PROJ_imageCount);
            item.imgCamera = (ImageView) convertView
                    .findViewById(R.id.PROJ_camera);
            item.imgMap = (ImageView) convertView
                    .findViewById(R.id.PROJ_map);
            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ProjectDTO p = mList.get(position);
        item.txtName.setText(p.getProjectName());
        item.txtClient.setText(p.getClientName());
        item.txtNumber.setText(""+(position+ 1));
        item.txtSiteCount.setText("" + p.getProjectSiteList().size());
        if (p.getDescription() == null) {
            item.txtDesc.setText("");
        } else {
            item.txtDesc.setText(p.getDescription());
        }
        int count = 0;
        List<ProjectSiteTaskStatusDTO> statusList = new ArrayList<>();
        for (ProjectSiteDTO ps: p.getProjectSiteList()) {
            for (ProjectSiteTaskDTO task: ps.getProjectSiteTaskList()) {
                count += task.getProjectSiteTaskStatusList().size();
                statusList.addAll(task.getProjectSiteTaskStatusList());
            }
        }
        Collections.sort(statusList);
        item.txtStatusCount.setText("" + count);
        if (!statusList.isEmpty()) {
            ProjectSiteTaskStatusDTO status = statusList.get(0);
            switch (status.getTaskStatus().getStatusColor()) {
                case TaskStatusDTO.STATUS_COLOR_RED:
                    item.txtSiteCount.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval));
                    item.txtStatusCount.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_GREEN:
                    item.txtSiteCount.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                    item.txtStatusCount.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_YELLOW:
                    item.txtSiteCount.setBackground(ctx.getResources().getDrawable(R.drawable.xorange_oval));
                    item.txtStatusCount.setBackground(ctx.getResources().getDrawable(R.drawable.xorange_oval));
                    break;
            }
        }
        item.txtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditRequested(p);
            }
        });
        item.txtSiteCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProjectSitesRequested(p);
            }
        });
        item.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPictureRequested(p);
            }
        });
        item.txtImageCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onGalleryRequested(p);
            }
        });
        item.imgMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMapRequested(p);
            }
        });
        Statics.setRobotoFontLight(ctx,item.txtDesc);
        Statics.setRobotoFontBold(ctx,item.txtName);


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
