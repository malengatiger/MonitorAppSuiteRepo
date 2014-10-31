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
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjectSiteTaskAdapter extends ArrayAdapter<ProjectSiteTaskDTO> {

    public interface ProjectSiteTaskAdapterListener {
        public void onCameraRequested(ProjectSiteTaskDTO siteTask);

        public void onStatusRequested(ProjectSiteTaskDTO siteTask);

        public void onDeleteRequested(ProjectSiteTaskDTO siteTask);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ProjectSiteTaskDTO> mList;
    private Context ctx;
    private ProjectSiteTaskAdapterListener listener;


    public ProjectSiteTaskAdapter(Context context, int textViewResourceId,
                                  List<ProjectSiteTaskDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;

        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ProjectSiteTaskAdapter(Context context, int textViewResourceId,
                                  List<ProjectSiteTaskDTO> list, ProjectSiteTaskAdapterListener listener) {
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
        TextView txtName, txtStatus, txtStaff;
        TextView txtNumber, txtLastDate, txtStatusCount;
        ImageView imgStatus, imgDelete, imgCamera;
        View actions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.actions = convertView.findViewById(R.id.TSK_bottom);
            item.txtName = (TextView) convertView
                    .findViewById(R.id.TSK_taskName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.TSK_number);
            item.txtLastDate = (TextView) convertView
                    .findViewById(R.id.TSK_lastStatusDate);
            item.txtStatusCount = (TextView) convertView
                    .findViewById(R.id.TSK_statusCount);
            item.txtStatus = (TextView) convertView
                    .findViewById(R.id.TSK_lastStatus);
            item.txtStaff = (TextView) convertView
                    .findViewById(R.id.TSK_staff);

            item.imgCamera = (ImageView) convertView
                    .findViewById(R.id.TAC_imgCamera);
            item.imgDelete = (ImageView) convertView
                    .findViewById(R.id.TAC_imgRemove);
            item.imgStatus = (ImageView) convertView
                    .findViewById(R.id.TAC_imgStatus);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }


        final ProjectSiteTaskDTO p = mList.get(position);

        item.txtName.setText(p.getTask().getTaskName());
        item.txtNumber.setText("" + (position + 1));
        if (p.getProjectSiteTaskStatusList() != null) {
            item.txtStatusCount.setText("" + p.getProjectSiteTaskStatusList().size());
            if (p.getProjectSiteTaskStatusList().size() > 0) {
                item.txtLastDate.setVisibility(View.VISIBLE);
                item.txtStatus.setVisibility(View.VISIBLE);
                item.txtStaff.setVisibility(View.VISIBLE);
                item.txtLastDate.setText(sdf.format(p.getProjectSiteTaskStatusList().get(0).getStatusDate()));
                item.txtStatus.setText(p.getProjectSiteTaskStatusList().get(0).getTaskStatus().getTaskStatusName());
                item.txtStaff.setText(p.getProjectSiteTaskStatusList().get(0).getStaffName());

            } else {
                item.txtLastDate.setVisibility(View.GONE);
                item.txtStatus.setVisibility(View.GONE);
                item.txtStaff.setVisibility(View.GONE);
            }
        }

        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontRegular(ctx, item.txtName);
        Statics.setRobotoFontBold(ctx, item.txtStatusCount);
        Statics.setRobotoFontRegular(ctx, item.txtLastDate);

        if (listener == null) {
            item.actions.setVisibility(View.GONE);
        } else {
            item.imgStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onStatusRequested(p);
                }
            });
            item.imgCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCameraRequested(p);
                }
            });
            item.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteRequested(p);
                }
            });
        }

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
