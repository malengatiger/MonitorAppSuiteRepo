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
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProjectAdapter extends ArrayAdapter<ProjectDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ProjectDTO> mList;
    private Context ctx;

   public ProjectAdapter(Context context, int textViewResourceId,
                         List<ProjectDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtStaffCount;
        TextView txtSiteCount;
        TextView txtNumber, txtDesc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.PROJ_txtName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.PROJ_image);
            item.txtSiteCount = (TextView) convertView
                    .findViewById(R.id.PROJ_txtCount);
            item.txtStaffCount = (TextView) convertView
                   .findViewById(R.id.PROJ_txtStaffCount);
            item.txtDesc = (TextView) convertView
                    .findViewById(R.id.PROJ_txtDesc);
            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        ProjectDTO p = mList.get(position);
        item.txtName.setText(p.getProjectName());
        item.txtNumber.setText(""+(position+ 1));
        item.txtSiteCount.setText("" + p.getProjectSiteList().size());
        if (p.getDescription() == null) {
            item.txtDesc.setText("");
        } else {
            item.txtDesc.setText(p.getDescription());
        }
        int count = 0;
        for (ProjectSiteDTO ps: p.getProjectSiteList()) {
            count += ps.getProjectSiteStaffList().size();
        }
        item.txtStaffCount.setText(""+count);
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
