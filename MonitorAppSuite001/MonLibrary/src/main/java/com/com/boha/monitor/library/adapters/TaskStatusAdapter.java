package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskStatusAdapter extends ArrayAdapter<TaskStatusDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<TaskStatusDTO> mList;
    private String title;
    private Context ctx;

   public TaskStatusAdapter(Context context, int textViewResourceId,
                            List<TaskStatusDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public TaskStatusAdapter(Context context, int textViewResourceId,
                             List<TaskStatusDTO> list, String title) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.title = title;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        TextView txtName;
        TextView txtNumber, txtTitle;
        View top;
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
            item.txtTitle = (TextView) convertView
                    .findViewById(R.id.TST_title);
            item.top =  convertView
                    .findViewById(R.id.TST_top);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final TaskStatusDTO p = mList.get(position);
        item.txtName.setText(p.getTaskStatusName());
        item.txtNumber.setText(""+(position+ 1));

        final int color = p.getStatusColor();

        if (title != null) {
            if (position == 0) {
                item.top.setVisibility(View.VISIBLE);
                item.txtTitle.setText(title);
            } else {
                item.top.setVisibility(View.GONE);
            }
            switch (p.getStatusColor()) {
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
            item.top.setVisibility(View.GONE);
            switch (p.getStatusColor()) {
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
        Statics.setRobotoFontLight(ctx,item.txtNumber);
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
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
