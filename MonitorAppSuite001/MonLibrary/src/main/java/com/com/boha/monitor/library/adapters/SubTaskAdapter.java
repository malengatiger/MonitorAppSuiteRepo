package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.SubTaskDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SubTaskAdapter extends ArrayAdapter<SubTaskDTO>  {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<SubTaskDTO> mList;
    private Context ctx;
    private boolean hidePrice;

   public SubTaskAdapter(Context context, int textViewResourceId,
                         List<SubTaskDTO> list, boolean hidePrice) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        this.hidePrice = hidePrice;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        TextView txtName;
        TextView txtNumber, txtSequence;
        View priceLayout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.TTS_name);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.TTS_number);
            item.txtSequence = (TextView) convertView
                    .findViewById(R.id.TTS_sequenceNumber);
            item.priceLayout = convertView.findViewById(R.id.TTS_priceLayout);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        if (hidePrice) {
            item.priceLayout.setVisibility(View.GONE);
        }

        final SubTaskDTO p = mList.get(position);
        item.txtName.setText(p.getSubTaskName());
        item.txtNumber.setText(""+(position+ 1));
        if (p.getSubTaskNumber() == null) {
            item.txtSequence.setText("0");
        } else {
            item.txtSequence.setText("" + p.getSubTaskNumber());
        }

        Statics.setRobotoFontLight(ctx,item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);


        return (convertView);
    }


    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,##0.0");
}
