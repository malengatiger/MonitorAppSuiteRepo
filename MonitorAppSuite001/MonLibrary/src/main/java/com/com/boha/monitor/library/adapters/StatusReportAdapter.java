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
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StatusReportAdapter extends ArrayAdapter<ProjectSiteTaskStatusDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ProjectSiteTaskStatusDTO> mList;
    private Context ctx;

   public StatusReportAdapter(Context context, int textViewResourceId,
                              List<ProjectSiteTaskStatusDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        TextView txtTaskName, txtStatus,txtDate;
        TextView txtNumber, txtColor;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtTaskName = (TextView) convertView
                    .findViewById(R.id.ROW_task);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.ROW_number);
            item.txtStatus = (TextView) convertView
                    .findViewById(R.id.ROW_status);
            item.txtDate = (TextView) convertView
                    .findViewById(R.id.ROW_date);
            item.txtColor = (TextView) convertView
                    .findViewById(R.id.ROW_color);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ProjectSiteTaskStatusDTO p = mList.get(position);
        item.txtTaskName.setText(p.getTask().getTaskName());
        item.txtNumber.setText(""+(position+ 1));
        item.txtDate.setText(sdf.format(p.getStatusDate()));
        item.txtStatus.setText(p.getTaskStatus().getTaskStatusName());

        switch (p.getTaskStatus().getStatusColor()) {
            case TaskStatusDTO.STATUS_COLOR_GREEN:
                item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                break;
            case TaskStatusDTO.STATUS_COLOR_RED:
                item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval));
                break;
            case TaskStatusDTO.STATUS_COLOR_YELLOW:
                item.txtColor.setBackground(ctx.getResources().getDrawable(R.drawable.xorange_oval));
                break;
        }


        Statics.setRobotoFontLight(ctx,item.txtDate);
        Statics.setRobotoFontLight(ctx, item.txtStatus);

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
