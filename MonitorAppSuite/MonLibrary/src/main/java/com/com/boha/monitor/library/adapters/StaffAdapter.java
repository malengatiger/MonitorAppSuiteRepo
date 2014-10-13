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
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.util.Statics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StaffAdapter extends ArrayAdapter<CompanyStaffDTO> {

    private final LayoutInflater mInflater;
    private final int mLayoutRes;
    private List<CompanyStaffDTO> mList;
    private Context ctx;

   public StaffAdapter(Context context, int textViewResourceId,
                       List<CompanyStaffDTO> list) {
        super(context, textViewResourceId, list);
        this.mLayoutRes = textViewResourceId;
        mList = list;
        ctx = context;
        this.mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    View view;


    static class ViewHolderItem {
        TextView txtName, txtEmail, txtCellphone;
        ImageView imgHistory, imgEdit, imgCamera, imgInvite, photo, imgMessage;
        TextView txtNumber, txtCount, txtStaffType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderItem item;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutRes, null);
            item = new ViewHolderItem();
            item.txtName = (TextView) convertView
                    .findViewById(R.id.PSN_txtName);
            item.txtEmail = (TextView) convertView
                    .findViewById(R.id.PSN_txtEmail);
            item.txtCount = (TextView) convertView
                    .findViewById(R.id.PSN_txtCounter);
            item.txtNumber = (TextView) convertView
                    .findViewById(R.id.PSN_txtNum);
            item.txtCellphone = (TextView) convertView
                    .findViewById(R.id.PSN_txtCell);
            item.txtStaffType = (TextView) convertView
                    .findViewById(R.id.PSN_txtStaffType);
//            item.imgHistory = (ImageView) convertView
//                    .findViewById(R.id.PA_imgStaffHistory);
            item.imgCamera = (ImageView) convertView
                   .findViewById(R.id.PA_imgCamera);
            item.imgEdit = (ImageView) convertView
                    .findViewById(R.id.PA_imgEdit);
            item.imgInvite = (ImageView) convertView
                    .findViewById(R.id.PA_imgInvite);
            item.imgMessage = (ImageView) convertView
                    .findViewById(R.id.PA_imgMessage);
            item.photo = (ImageView) convertView
                    .findViewById(R.id.PSN_imagex);

            convertView.setTag(item);
        } else {
            item = (ViewHolderItem) convertView.getTag();
        }

        CompanyStaffDTO p = mList.get(position);
        item.txtName.setText(p.getFirstName() + " " + p.getLastName());
        item.txtEmail.setText(p.getEmail());
        item.txtStaffType.setText(p.getCompanyStaffType().getCompanyStaffTypeName());
        if (p.getCellphone() == null) {
            item.txtCellphone.setVisibility(View.GONE);
        } else {
            item.txtCellphone.setText(p.getCellphone());
        }
        item.txtNumber.setText(""+(position+ 1));

        Statics.setRobotoFontLight(ctx,item.txtNumber);
        Statics.setRobotoFontLight(ctx,item.txtEmail);
        Statics.setRobotoFontBold(ctx,item.txtName);


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
