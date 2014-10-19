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
import com.com.boha.monitor.library.dto.ClientDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ClientAdapter extends ArrayAdapter<ClientDTO> {

    public interface ClientAdapterListener {
        public void onClientEditRequested(ClientDTO client);
    }

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<ClientDTO> mList;
    private Context ctx;
    private ClientAdapterListener listener;

    public ClientAdapter(Context context, int textViewResourceId,
                         List<ClientDTO> list,
                         ClientAdapterListener listener) {
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
        TextView txtName, txtAddress, txtEmail;
        TextView txtNumber;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.CI_name);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.CI_number);
            item.txtAddress = (TextView) convertView
                    .findViewById(R.id.CI_address);
            item.txtEmail = (TextView) convertView
                    .findViewById(R.id.CI_email);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        final ClientDTO p = mList.get(position);
        item.txtName.setText(p.getClientName());
        item.txtNumber.setText("" + (position + 1));
        item.txtAddress.setText(p.getAddress());
        item.txtEmail.setText(p.getEmail());

        item.txtNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClientEditRequested(p);
            }
        });

        Statics.setRobotoFontLight(ctx, item.txtNumber);
        Statics.setRobotoFontLight(ctx, item.txtName);

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
