package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.util.Statics;

import java.util.List;

public class TaskStatusAdapter extends ArrayAdapter<TaskStatusDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<TaskStatusDTO> mList;
    private String title;
    private Context ctx;
    boolean isSmallIcons;

    public TaskStatusAdapter(Context context, int textViewResourceId,
                             List<TaskStatusDTO> list,boolean isSmallIcons) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        isSmallIcons = isSmallIcons;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName;
        TextView txtNumber;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.TST_txtName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.TST_txtNumber);


            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final TaskStatusDTO p = mList.get(position);
        item.txtName.setText(p.getTaskStatusName());
        item.txtNumber.setText("" + (position + 1));

        final int color = p.getStatusColor();
        if (isSmallIcons) {
            switch (color) {
                case TaskStatusDTO.STATUS_COLOR_GREEN:
                    item.txtNumber.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgreen_oval_small));
                    break;
                case TaskStatusDTO.STATUS_COLOR_RED:
                    item.txtNumber.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xred_oval_small));
                    break;
                case TaskStatusDTO.STATUS_COLOR_YELLOW:
                    item.txtNumber.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xorange_oval_small));
                    break;
            }
        } else {
            switch (color) {
                case TaskStatusDTO.STATUS_COLOR_GREEN:
                    item.txtNumber.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_RED:
                    item.txtNumber.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xred_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_YELLOW:
                    item.txtNumber.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xorange_oval));
                    break;
            }
        }

        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);

        return (convertView);
    }
}
