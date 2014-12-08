package com.com.boha.monitor.library.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends ArrayAdapter<TaskDTO>  {


    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<TaskDTO> mList;
    private Context ctx;

   public TaskAdapter(Context context, int textViewResourceId,
                      List<TaskDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    View view;


    static class ViewHolderItem {
        TextView txtName, txtPrice;
        TextView txtNumber, txtSequence;
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
            item.txtPrice = (TextView) convertView
                    .findViewById(R.id.TTS_price);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final TaskDTO p = mList.get(position);
        item.txtName.setText(p.getTaskName());
        item.txtNumber.setText(""+(position+ 1));
        if (p.getTaskNumber() == null) {
            item.txtSequence.setText("0");
        } else {
            item.txtSequence.setText("" + p.getTaskNumber());
        }
        if (p.getTaskPriceList() != null && !p.getTaskPriceList().isEmpty()) {
            item.txtPrice.setText(df.format(p.getTaskPriceList().get(0).getPrice()));
        } else {
            item.txtPrice.setText("0.00");
        }

        Statics.setRobotoFontLight(ctx,item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);


        return (convertView);
    }
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,##0.00");

    static final Locale x = Locale.getDefault();
    static final SimpleDateFormat y = new SimpleDateFormat("dd MMMM yyyy", x);
}
