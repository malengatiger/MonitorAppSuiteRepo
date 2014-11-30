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
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjectSiteTaskAdapter extends ArrayAdapter<ProjectSiteTaskDTO> {

    public interface ProjectSiteTaskAdapterListener {
        public void onDeleteRequested(ProjectSiteTaskDTO siteTask);

        public void onCameraRequested(ProjectSiteTaskDTO siteTask);
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
        TextView txtNumber, txtLastDate, txtStatusCount, txtColor;
        ImageView imgDelete, imgCamera;
        View cameraLayout;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
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
            item.txtColor = (TextView) convertView
                    .findViewById(R.id.TSK_statusColor);

            item.imgCamera = (ImageView) convertView
                    .findViewById(R.id.TSK_camera);
            item.imgDelete = (ImageView) convertView
                    .findViewById(R.id.TSK_delete);
            item.cameraLayout = convertView.findViewById(R.id.TSK_cameraLayout);


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
                List<ProjectSiteTaskStatusDTO> list = p.getProjectSiteTaskStatusList();

                item.txtLastDate.setText(sdf.format(list.get(0).getStatusDate()));
                item.txtStatus.setText(list.get(0).getTaskStatus().getTaskStatusName());
                item.txtStaff.setText(list.get(0).getStaffName());
                int color = list.get(0).getTaskStatus().getStatusColor();
                switch (color) {
                    case 1:
                        item.txtColor.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                        break;
                    case 2:
                        item.txtColor.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xorange_oval_small));
                        break;
                    case 3:
                        item.txtColor.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                        break;
                    default:
                        item.txtColor.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgrey_oval_small));
                        break;
                }

            } else {
                item.txtLastDate.setVisibility(View.GONE);
                item.txtStatus.setVisibility(View.GONE);
                item.txtStaff.setVisibility(View.GONE);
                item.txtColor.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgrey_oval_small));
            }
        }

        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontRegular(ctx, item.txtName);
        Statics.setRobotoFontBold(ctx, item.txtStatusCount);
        Statics.setRobotoFontRegular(ctx, item.txtLastDate);


        if (listener == null) {
            item.imgCamera.setVisibility(View.GONE);
            item.imgDelete.setVisibility(View.GONE);
        } else {
            item.imgCamera.setVisibility(View.VISIBLE);
            item.imgDelete.setVisibility(View.VISIBLE);
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
            item.cameraLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCameraRequested(p);
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
    static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
