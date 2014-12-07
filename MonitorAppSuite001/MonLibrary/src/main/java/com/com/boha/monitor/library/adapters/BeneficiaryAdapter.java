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
import com.com.boha.monitor.library.dto.BeneficiaryDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class BeneficiaryAdapter extends ArrayAdapter<BeneficiaryDTO> {

    public interface BeneficiaryAdapterListener {
        public void onBeneficiaryEditRequested(BeneficiaryDTO client);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<BeneficiaryDTO> mList;
    private Context ctx;
    private BeneficiaryAdapterListener listener;

    public BeneficiaryAdapter(Context context, int textViewResourceId,
                              List<BeneficiaryDTO> list,
                              BeneficiaryAdapterListener listener) {
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
        TextView txtNumber, txtHappy;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.BEN_CARD_txtName);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.BEN_CARD_txtNum);

            item.txtHappy = (TextView) convertView
                    .findViewById(R.id.BEN_CARD_txtHappy);


            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final BeneficiaryDTO p = mList.get(position);
        item.txtName.setText(p.getFullName());
        item.txtNumber.setText("" + (position + 1));


        item.txtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBeneficiaryEditRequested(p);
            }
        });

        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);

        //animateView(convertView);
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
