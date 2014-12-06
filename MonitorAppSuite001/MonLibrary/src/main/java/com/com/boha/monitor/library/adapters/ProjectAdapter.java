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
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
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
                         List<ProjectDTO> list
                         ) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtStatusCount;
        TextView txtSiteCount, txtImageCount;
        TextView txtNumber, txtDesc, txtClient;
        ImageView imgCamera, imgMap, imgDocs;
    }

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
            item.imgDocs = (ImageView) convertView
                    .findViewById(R.id.PROJ_docs);
            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ProjectDTO p = mList.get(position);
        item.txtName.setText(p.getProjectName());
        item.txtClient.setText(p.getClientName());
        item.txtNumber.setText(""+(position+ 1));
        item.txtSiteCount.setText("" + p.getSiteCount());
        if (p.getDescription() == null) {
            item.txtDesc.setText("");
        } else {
            item.txtDesc.setText(p.getDescription());
        }

        item.txtStatusCount.setText("" + p.getStatusCount());
        if (p.getLastStatus() != null) {
            switch (p.getLastStatus().getTaskStatus().getStatusColor()) {
                case TaskStatusDTO.STATUS_COLOR_RED:
                    item.txtSiteCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xred_oval));
                    item.txtStatusCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xred_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_GREEN:
                    item.txtSiteCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                    item.txtStatusCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xgreen_oval));
                    break;
                case TaskStatusDTO.STATUS_COLOR_YELLOW:
                    item.txtSiteCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xorange_oval));
                    item.txtStatusCount.setBackgroundDrawable(ctx.getResources().getDrawable(R.drawable.xorange_oval));
                    break;
            }
        }
        
        Statics.setRobotoFontLight(ctx,item.txtClient);
        Statics.setRobotoFontLight(ctx,item.txtName);


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
