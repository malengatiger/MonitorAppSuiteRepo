package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.util.Statics;

import java.util.List;
import java.util.Random;

public class ProjectSiteSelectionAdapter extends ArrayAdapter<ProjectSiteDTO> {

    public interface ProjectSiteSelectionAdapterListener {
        public void onCheckBoxChange(ProjectSiteDTO site, int index);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ProjectSiteDTO> mList;
    private Context ctx;
    private ProjectSiteSelectionAdapterListener listener;
    private Random random;

    public ProjectSiteSelectionAdapter(Context context, int layoutResourceId,
                                       List<ProjectSiteDTO> list,
                                       ProjectSiteSelectionAdapterListener listener) {
        super(context, layoutResourceId, list);
        this.mLayoutRes = layoutResourceId;
        this.listener = listener;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtNumber;
        CheckBox chkSelect;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        random = new Random(System.currentTimeMillis());
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.SS_siteName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.SS_number);
            item.chkSelect = (CheckBox) convertView
                    .findViewById(R.id.SS_chkSelected);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ProjectSiteDTO p = mList.get(position);
        item.txtName.setText(p.getProjectSiteName());
        item.txtNumber.setText("" + (position + 1));
        item.chkSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                p.setSelected(isChecked);
                listener.onCheckBoxChange(p,position);
            }
        });
        if (p.isSelected()) {
            item.chkSelect.setChecked(true);
        } else {
            item.chkSelect.setChecked(false);
        }

        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);

        return (convertView);
    }

}
