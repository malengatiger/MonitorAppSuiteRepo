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
import com.com.boha.monitor.library.dto.ProjectStatusTypeDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjectStatusTypeAdapter extends ArrayAdapter<ProjectStatusTypeDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ProjectStatusTypeDTO> mList;
    private Context ctx;

   public ProjectStatusTypeAdapter(Context context, int textViewResourceId,
                                   List<ProjectStatusTypeDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        TextView txtName;
        TextView txtNumber;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
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

        ProjectStatusTypeDTO p = mList.get(position);
        item.txtName.setText(p.getProjectStatusName());
        item.txtNumber.setText(""+(position+ 1));
        if (p.getStatusColor() != null) {
            switch (p.getStatusColor()) {
                case TaskStatusDTO.STATUS_COLOR_GREEN:
                    item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_RED:
                    item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xred_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_YELLOW:
                    item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xorange_oval));
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
