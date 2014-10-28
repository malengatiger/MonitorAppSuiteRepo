package com.com.boha.monitor.library.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ClientDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;

/**
 * Created by aubreyM on 2014/10/18.
 */
public class ClientDialog extends DialogFragment {
    public interface ClientDialogListener {
        public void onClientAdded(ClientDTO client);
        public void onClientUpdated(ClientDTO client);
        public void onError(String message);
    }
    ClientDialogListener listener;
    Context context;
    TextView txtCompany;
    EditText editClientName, editAddress, editEmail, editCellphone, editPostal;
    ProgressBar progressBar;
    Button btnCancel, btnSave, btnDelete;
    ClientDTO client;
    View view;
    int action;
    static final String LOG = ClientDialog.class.getSimpleName();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.client_edit, container);
        editClientName = (EditText) view.findViewById(R.id.CE_clientName);
        editAddress = (EditText) view.findViewById(R.id.CE_address);
        editEmail = (EditText) view.findViewById(R.id.CE_email);
        editCellphone = (EditText) view.findViewById(R.id.CE_cellphone);
        editPostal = (EditText) view.findViewById(R.id.CE_postCode);
        txtCompany = (TextView)view.findViewById(R.id.CE_groupName);
        txtCompany.setText(SharedUtil.getCompany(context).getCompanyName());

        progressBar = (ProgressBar) view.findViewById(R.id.CE_progress);
        progressBar.setVisibility(View.GONE);


        btnCancel = (Button) view.findViewById(R.id.CE_btnCancel);
        btnSave = (Button) view.findViewById(R.id.CE_btnSave);
        btnDelete= (Button) view.findViewById(R.id.CE_btnDelete);
        getDialog().setTitle(context.getResources().getString(R.string.app_name));


        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                switch (action) {
                    case ClientDTO.ACTION_ADD:
                        registerClient();
                        break;
                    case ClientDTO.ACTION_UPDATE:
                        updateClient();
                        break;
                    case ClientDTO.ACTION_DELETE:
                        break;
                }
            }
        });
        if (action == ClientDTO.ACTION_UPDATE)
            fillForm();
        return view;
    }
    private void fillForm() {
        editClientName.setText(client.getClientName());
        editAddress.setText(client.getAddress());
        editPostal.setText(client.getPostCode());
        editEmail.setText(client.getEmail());
        editCellphone.setText(client.getCellphone());

    }
    private void registerClient() {
        client = new ClientDTO();
        client.setCompanyID(SharedUtil.getCompany(context).getCompanyID());
        if (editClientName.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources()
                    .getString(R.string.enter_name));
            return;
        }

        if (editAddress.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources()
                    .getString(R.string.enter_address));
            return;
        }

        if (editEmail.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources().getString(R.string.enter_email));
            return;
        }

        if (editCellphone.getText().toString().isEmpty()) {
            ToastUtil.toast(context,context.getResources().getString(R.string.enter_cell));
            return;
        }


        client.setClientName(editClientName.getText().toString());
        client.setAddress(editAddress.getText().toString());
        client.setCellphone(editCellphone.getText().toString());
        client.setEmail(editEmail.getText().toString());
        client.setPostCode(editPostal.getText().toString());

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.REGISTER_CLIENT);
        w.setClient(client);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(context, Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(ResponseDTO response) {
                if (!ErrorUtil.checkServerError(context,response)) {
                    return;
                }
                client = response.getClientList().get(0);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        listener.onClientAdded(client);
                        dismiss();
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(final String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        listener.onError(message);
                    }
                });

            }
        });

    }

    private void updateClient() {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setListener(ClientDialogListener listener) {
        this.listener = listener;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }
}
