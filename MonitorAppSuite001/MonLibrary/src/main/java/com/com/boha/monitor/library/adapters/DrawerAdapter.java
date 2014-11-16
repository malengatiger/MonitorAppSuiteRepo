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
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DrawerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<String> mList;
    private Context ctx;
    private CompanyDTO company;

    public DrawerAdapter(Context context, int textViewResourceId,
                         List<String> list, CompanyDTO company) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.company = company;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtNumber, txtCount;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.DI_txtTitle);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.DI_txtNumber);
            item.txtCount = (TextView) convertView
                    .findViewById(R.id.DI_txtCount);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        String p = mList.get(position);
        item.txtName.setText(p);
        item.txtNumber.setText("" + (position + 1));

        Statics.setRobotoFontLight(ctx, item.txtName);
        switch (position) {
            case 0:
                if (company.getProjectList().isEmpty()) {
                    item.txtCount.setVisibility(View.GONE);
                } else {
                    item.txtCount.setVisibility(View.VISIBLE);
                    item.txtCount.setText("" + company.getProjectList().size());
                }
                break;
            case 1:
                //item.txtNumber.setBackground(ctx.getResources().getDrawable(R.drawable.xblue_oval_small));
                if (company.getCompanyStaffList().isEmpty()) {
                    item.txtCount.setVisibility(View.GONE);
                } else {
                    item.txtCount.setVisibility(View.VISIBLE);
                    item.txtCount.setText("" + company.getCompanyStaffList().size());
                }
                break;
            case 2:
                if (company.getClientList().isEmpty()) {
                    item.txtCount.setVisibility(View.GONE);
                } else {
                    item.txtCount.setVisibility(View.VISIBLE);
                    item.txtCount.setText("" + company.getClientList().size());
                }
                break;
            case 3:
                if (company.getTaskStatusList().isEmpty()) {
                    item.txtCount.setVisibility(View.GONE);
                } else {
                    item.txtCount.setVisibility(View.VISIBLE);
                    item.txtCount.setText("" + company.getTaskStatusList().size());
                }
                break;
            case 4:
                if (company.getProjectStatusTypeList().isEmpty()) {
                    item.txtCount.setVisibility(View.GONE);
                } else {
                    item.txtCount.setVisibility(View.VISIBLE);
                    item.txtCount.setText("" + company.getProjectStatusTypeList().size());
                }
                break;
            case 5:
                if (company.getTaskList().isEmpty()) {
                    item.txtCount.setVisibility(View.GONE);
                } else {
                    item.txtCount.setVisibility(View.VISIBLE);
                    item.txtCount.setText("" + company.getTaskList().size());
                }
                break;
            case 6:
                if (company.getEngineerList().isEmpty()) {
                    item.txtCount.setVisibility(View.GONE);
                } else {
                    item.txtCount.setVisibility(View.VISIBLE);
                    item.txtCount.setText("" + company.getEngineerList().size());
                }
                break;
            case 7:
                if (company.getProjectList().isEmpty()) {
                    item.txtCount.setVisibility(View.GONE);
                } else {
                    item.txtCount.setVisibility(View.VISIBLE);
                    int count = 0;
                    for (ProjectDTO px: company.getProjectList()) {
                        if (px.getBeneficiaryCount() != null) {
                            count += px.getBeneficiaryCount().intValue();
                        }
                    }
                    item.txtCount.setText("" + count);
                }
                break;

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
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
