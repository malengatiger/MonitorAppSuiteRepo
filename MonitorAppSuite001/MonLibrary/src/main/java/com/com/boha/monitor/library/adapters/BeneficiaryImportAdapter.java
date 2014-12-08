package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.BeneficiaryDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BeneficiaryImportAdapter extends ArrayAdapter<BeneficiaryDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<BeneficiaryDTO> mList;
    private Context ctx;
    private boolean hideDetail;

    public BeneficiaryImportAdapter(Context context, int textViewResourceId,
                                    List<BeneficiaryDTO> list, boolean hideDetail) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.hideDetail = hideDetail;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        TextView txtName, txtID, txtSubsidy;
        TextView txtNumber, txtStatus, txtSite;
        View detailLayout;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.BEN_txtName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.BEN_txtNum);
            item.txtID = (TextView) convertView
                    .findViewById(R.id.BEN_idnumber);
            item.txtSite = (TextView) convertView
                    .findViewById(R.id.BEN_siteNumber);
            item.txtSubsidy = (TextView) convertView
                    .findViewById(R.id.BEN_subsidy);
            item.txtStatus = (TextView) convertView
                    .findViewById(R.id.BEN_status);
            item.detailLayout = convertView.findViewById(R.id.BEN_bottom);


            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }
        if (hideDetail) {
            item.detailLayout.setVisibility(View.GONE);
        }

        final BeneficiaryDTO p = mList.get(position);
        item.txtName.setText(p.getFullName());
        item.txtNumber.setText("" + (position + 1));

        item.txtStatus.setText(p.getStatus());
        item.txtSite.setText(p.getSiteNumber());
        if (p.getAmountAuthorized() != null)
            item.txtSubsidy.setText(df.format(p.getAmountAuthorized()));
        item.txtID.setText(p.getiDNumber());

        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);

        return (convertView);
    }

    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
}
