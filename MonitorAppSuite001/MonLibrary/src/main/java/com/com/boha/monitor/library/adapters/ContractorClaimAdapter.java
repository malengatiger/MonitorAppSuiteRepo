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
import com.com.boha.monitor.library.dto.ContractorClaimDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ContractorClaimAdapter extends ArrayAdapter<ContractorClaimDTO> {

    public interface ContractorClaimAdapterListener {
        public void onProjectSiteListRequested(ContractorClaimDTO claim);
        public void onPDFDownloadRequested(ContractorClaimDTO claim);
        public void onInvoiceActionsRequested(ContractorClaimDTO claim);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ContractorClaimDTO> mList;
    private Context ctx;
    private ContractorClaimAdapterListener listener;

    public ContractorClaimAdapter(Context context, int textViewResourceId,
                                  List<ContractorClaimDTO> list,
                                  ContractorClaimAdapterListener listener) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        this.listener = listener;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;
    static class ViewHolderItem {
        TextView txtEngineerName, txtSiteCount, txtDate,
                txtDoc, txtNumber, txtClaimNumber, txtInvoice;
        ImageView imgYes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtEngineerName = (TextView) convertView
                    .findViewById(R.id.CCI_engineer);
            item.imgYes = (ImageView) convertView
                    .findViewById(R.id.CCI_imgYes);
            item.txtSiteCount = (TextView) convertView
                    .findViewById(R.id.CCI_siteCount);
            item.txtDate = (TextView) convertView
                    .findViewById(R.id.CCI_date);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.CCI_number);
            item.txtDoc = (TextView) convertView
                    .findViewById(R.id.CCI_doc);
            item.txtInvoice = (TextView) convertView
                    .findViewById(R.id.CCI_invoice);
            item.txtClaimNumber = (TextView) convertView
                    .findViewById(R.id.CCI_claimNumber);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ContractorClaimDTO p = mList.get(position);
        item.txtEngineerName.setText(p.getEngineerName());
        item.txtClaimNumber.setText(p.getClaimNumber());
        item.txtNumber.setText("" + (position + 1));
        if (p.getContractorClaimSiteList() != null)
            item.txtSiteCount.setText("" + p.getSiteCount());
        else
            item.txtSiteCount.setText("0");
        item.txtDate.setText(sdf.format(p.getClaimDate()));

        item.txtSiteCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onProjectSiteListRequested(p);
            }
        });
        item.txtInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(item.txtInvoice, 100, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onInvoiceActionsRequested(p);
                    }
                });
            }
        });
        item.txtDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(item.txtDoc, 100, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onPDFDownloadRequested(p);
                    }
                });
            }
        });

        Statics.setRobotoFontLight(ctx, item.txtDate);
        Statics.setRobotoFontLight(ctx, item.txtEngineerName);

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
    static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
