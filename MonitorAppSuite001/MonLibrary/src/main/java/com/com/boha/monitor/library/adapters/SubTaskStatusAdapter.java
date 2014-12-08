package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.SubTaskStatusDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SubTaskStatusAdapter extends ArrayAdapter<SubTaskStatusDTO>  {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<SubTaskStatusDTO> mList;
    private Context ctx;

   public SubTaskStatusAdapter(Context context, int textViewResourceId,
                               List<SubTaskStatusDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        TextView txtName, txtTime, txtStaff;
        TextView txtNumber, txtLastStatus, txtDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.SSS_taskName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.SSS_number);
            item.txtStaff = (TextView) convertView
                    .findViewById(R.id.SSS_staffName);
            item.txtDate = (TextView) convertView
                    .findViewById(R.id.SSS_date);
            item.txtTime = (TextView) convertView
                    .findViewById(R.id.SSS_time);
            item.txtLastStatus = (TextView) convertView
                    .findViewById(R.id.SSS_status);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final SubTaskStatusDTO p = mList.get(position);
        item.txtName.setText(p.getSubTaskName());
        item.txtNumber.setText(""+(position+ 1));
        if (p.getStatusDate() != null) {
            item.txtDate.setText(y.format(p.getStatusDate()));
            item.txtTime.setText(y2.format(p.getStatusDate()));
            item.txtStaff.setText(p.getStaffName());
            item.txtStaff.setVisibility(View.VISIBLE);
        } else {
            item.txtDate.setText(ctx.getString(R.string.status_not));
            item.txtTime.setText("00:00:00");
            item.txtStaff.setVisibility(View.GONE);
        }
        int color = 0;
        if (p.getTaskStatus() != null) {
            color = p.getTaskStatus().getStatusColor();
            item.txtLastStatus.setText(p.getTaskStatus().getTaskStatusName());
            item.txtLastStatus.setVisibility(View.VISIBLE);
        } else {
            item.txtLastStatus.setVisibility(View.GONE);
        }
        switch (color) {
            case 0:
                item.txtNumber.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgrey_oval_small));
                break;
            case TaskStatusDTO.STATUS_COLOR_GREEN:
                item.txtNumber.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                break;
            case TaskStatusDTO.STATUS_COLOR_YELLOW:
                item.txtNumber.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xorange_oval_small));
                break;
            case TaskStatusDTO.STATUS_COLOR_RED:
                item.txtNumber.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                break;
        }

        Statics.setRobotoFontLight(ctx,item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);
        Statics.setRobotoFontLight(ctx, item.txtStaff);
        Statics.setRobotoFontLight(ctx, item.txtName);


        return (convertView);
    }


    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final SimpleDateFormat y2 = new SimpleDateFormat("HH:mm:ss", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
