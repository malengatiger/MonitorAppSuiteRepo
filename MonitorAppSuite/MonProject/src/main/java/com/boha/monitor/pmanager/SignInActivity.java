package com.boha.monitor.pmanager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.GcmDeviceDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.services.PhotoUploadService;
import com.com.boha.monitor.library.toolbox.BaseVolley;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.GCMUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;

public class SignInActivity extends Activity {

    Spinner spinnerEmail;
    TextView txtApp;
    EditText ePin;
    Button btnSave;
    Context ctx;
    String email;

    boolean isRegistration;
    GcmDeviceDTO gcmDevice;
    static final String LOG = SignInActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        ctx = getApplicationContext();
        checkVirgin();
        setFields();
        getEmail();
    }
    private void checkVirgin() {

        CompanyStaffDTO dto = SharedUtil.getCompanyStaff(ctx);
        if (dto != null) {
            Log.i(LOG, "++++++++ Not a virgin anymore ...checking GCM registration....");
            String id = SharedUtil.getRegistrationId(getApplicationContext());
            if (id == null) {
                registerGCMDevice();
            }

            Intent intent = new Intent(ctx, ProjectPagerActivity.class);
            startActivity(intent);
            PhotoUploadService.uploadPendingPhotos(ctx);
            finish();
            return;
        }
        registerGCMDevice();
    }

    private void registerGCMDevice() {
        boolean ok = GCMUtil.checkPlayServices(getApplicationContext(), this);
        if (ok) {
            Log.e(LOG, "############# Starting Google Cloud Messaging registration");
            GCMUtil.startGCMRegistration(getApplicationContext(), new GCMUtil.GCMUtilListener() {
                @Override
                public void onDeviceRegistered(String id) {
                    Log.e(LOG, "############# GCM - we cool, cool.....: " + id);
                    gcmDevice = new GcmDeviceDTO();
                    gcmDevice.setManufacturer(Build.MANUFACTURER);
                    gcmDevice.setModel(Build.MODEL);
                    gcmDevice.setSerialNumber(Build.SERIAL);
                    gcmDevice.setProduct(Build.PRODUCT);
                    gcmDevice.setAndroidVersion(Build.VERSION.RELEASE);
                    gcmDevice.setRegistrationID(id);

                }

                @Override
                public void onGCMError() {
                    Log.e(LOG, "############# onGCMError --- we got GCM problems");

                }
            });
        }
    }
    private void sendSignIn() {

        if (ePin.getText().toString().isEmpty()) {
            ToastUtil.errorToast(ctx, ctx.getResources().getString(R.string.enter_password));
            return;
        }
        if (email == null) {
            ToastUtil.errorToast(ctx, ctx.getResources().getString(R.string.select_email));
            return;
        }
        RequestDTO r = new RequestDTO();
        r.setRequestType(RequestDTO.LOGIN);
        r.setEmail(email);
        r.setPin(ePin.getText().toString());
        r.setGcmDevice(gcmDevice);

        setRefreshActionButtonState(true);
        BaseVolley.checkNetworkOnDevice(ctx);

        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, r, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        if (response.getStatusCode() > 0) {
                            ToastUtil.errorToast(ctx, response.getMessage());
                            return;
                        }

                        SharedUtil.saveCompany(ctx, response.getCompany());
                        SharedUtil.saveCompanyStaff(ctx, response.getCompanyStaff());
                        Intent intent = new Intent(ctx, ProjectPagerActivity.class);
                        startActivity(intent);

                        CacheUtil.cacheData(ctx, response, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
                            @Override
                            public void onFileDataDeserialized(ResponseDTO response) {

                            }

                            @Override
                            public void onDataCached() {

                            }

                            @Override
                            public void onError() {

                            }
                        });
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        ToastUtil.errorToast(ctx, message);
                    }
                });
            }
        });


    }
    private void setFields() {
        ePin = (EditText) findViewById(R.id.SI_pin);
        spinnerEmail = (Spinner) findViewById(R.id.SI_spinner);
        txtApp = (TextView)findViewById(R.id.SI_app);
        btnSave = (Button)findViewById(R.id.SI_btnSave);
        txtApp.setText(ctx.getString(R.string.pm_app));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSignIn();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sign_in, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_help) {
            ToastUtil.toast(ctx,ctx.getString(R.string.under_cons));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void getEmail() {
        AccountManager am = AccountManager.get(getApplicationContext());
        Account[] accts = am.getAccounts();
        if (accts.length == 0) {
            //TODO - send user to create acct
            ToastUtil.errorToast(ctx, "No Accounts found. Please create one and try again");
            finish();
            return;
        }
        if (accts.length == 1) {
            email = accts[0].name;
            spinnerEmail.setVisibility(View.GONE);
            return;
        }
        final ArrayList<String> tarList = new ArrayList<String>();
        if (accts != null) {
            tarList.add(ctx.getResources().getString(R.string.select_acct));
            for (int i = 0; i < accts.length; i++) {
                tarList.add(accts[i].name);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    R.layout.xsimple_spinner_item, tarList);
            dataAdapter
                    .setDropDownViewResource(R.layout.xsimple_spinner_dropdown_item);
            spinnerEmail.setAdapter(dataAdapter);
            spinnerEmail
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int index, long arg3) {
                            Log.w("RegistrationActivity", "###### Email account index is " + index);
                            if (index == 0) {
                                email = null;
                                return;
                            }
                            email = tarList.get(index);
                            Log.e("RegistrationActivity", "###### Email account selected is "
                                    + email);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
        }
    }
    Menu mMenu;
    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_help);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

}
