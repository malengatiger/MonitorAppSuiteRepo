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
import com.com.boha.monitor.library.dto.EngineerDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EngineerAdapter extends ArrayAdapter<EngineerDTO> {

    public interface EngineerAdapterListener {
        public void onEngineerEditRequested(EngineerDTO client);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<EngineerDTO> mList;
    private Context ctx;
    private EngineerAdapterListener listener;

    public EngineerAdapter(Context context, int textViewResourceId,
                           List<EngineerDTO> list,
                           EngineerAdapterListener listener) {
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
        TextView txtName, txtCell, txtEmail;
        TextView txtNumber;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.EI_name);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.EI_number);
            item.txtCell = (TextView) convertView
                    .findViewById(R.id.EI_cellphone);
            item.txtEmail = (TextView) convertView
                    .findViewById(R.id.EI_email);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final EngineerDTO p = mList.get(position);
        item.txtName.setText(p.getEngineerName());
        item.txtNumber.setText("" + (position + 1));
        item.txtCell.setText(p.getCellphone());
        item.txtEmail.setText(p.getEmail());

        item.txtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEngineerEditRequested(p);
            }
        });

        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);
        Statics.setRobotoFontLight(ctx, item.txtEmail);
        Statics.setRobotoFontLight(ctx, item.txtCell);

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
