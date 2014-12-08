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
import com.com.boha.monitor.library.dto.InvoiceDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class InvoiceAdapter extends ArrayAdapter<InvoiceDTO> {

    public interface InvoiceAdapterListener {
        public void onContractorClaimRequested(InvoiceDTO invoice);
        public void onInvoicePDFDownloadRequested(InvoiceDTO invoice);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<InvoiceDTO> mList;
    private Context ctx;
    private InvoiceAdapterListener listener;

    public InvoiceAdapter(Context context, int textViewResourceId,
                          List<InvoiceDTO> list,
                          InvoiceAdapterListener listener) {
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
        ViewHolderItem item;
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

        final InvoiceDTO p = mList.get(position);

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
