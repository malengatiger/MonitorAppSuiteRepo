package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.TaskPriceDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskPriceAdapter extends ArrayAdapter<TaskPriceDTO>  {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<TaskPriceDTO> mList;
    private Context ctx;

   public TaskPriceAdapter(Context context, int textViewResourceId,
                           List<TaskPriceDTO> list) {
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
        TextView txtNumber, txtPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.PRICE_ITEM_name);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.PRICE_ITEM_number);
            item.txtPrice = (TextView) convertView
                    .findViewById(R.id.PRICE_ITEM_price);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final TaskPriceDTO p = mList.get(position);
        item.txtName.setText(p.getTaskName());
        item.txtNumber.setText(""+(position+ 1));
        item.txtPrice.setText(df.format(p.getPrice()));

        Statics.setRobotoFontLight(ctx,item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);


        return (convertView);
    }


    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
}
