package com.com.boha.monitor.library;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.GcmDeviceDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.toolbox.BaseVolley;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.GCMUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;

import java.util.ArrayList;

public class SignInActivity extends FragmentActivity implements

        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_staff);
        ctx = getApplicationContext();
        checkVirgin();
        setFields();
        getEmail();
    }

    private void checkVirgin() {
        CompanyStaffDTO dto = SharedUtil.getCompanyStaff(ctx);
        if (dto != null) {
            Log.e(LOG, "Not a virgin anymore ...checking GCM registration....");
            //check that this device is registered for GCM
            String id = SharedUtil.getRegistrationId(getApplicationContext());
            if (id == null) {
                updateGCMDevice = true;
                registerGCMDevice();
            }

            Intent intent = new Intent(ctx, MainPagerActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        registerGCMDevice();

    }

    boolean updateGCMDevice;

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
                    if (updateGCMDevice) {
                        CompanyStaffDTO s = SharedUtil.getCompanyStaff(ctx);
                        email = s.getEmail();
                        ePin.setText(s.getEmail());
                        sendSignIn();
                    }

                }

                @Override
                public void onGCMError() {
                    Log.e(LOG, "############# onGCMError --- we got GCM problems");
                }
            });
        }
    }

    private void sendSignIn() {

        if (email == null) {
            ToastUtil.errorToast(ctx, ctx.getResources().getString(com.boha.monitor.library.R.string.select_email));
            return;
        }
        if (ePin.getText().toString().isEmpty()) {
            ToastUtil.errorToast(ctx, ctx.getResources().getString(com.boha.monitor.library.R.string.enter_pin));
            return;
        }


        RequestDTO r = new RequestDTO();
        r.setRequestType(RequestDTO.LOGIN);
        r.setEmail(email);
        r.setPin(ePin.getText().toString());
        r.setGcmDevice(gcmDevice);

        setRefreshActionButtonState(true);
        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }
        WebSocketUtil.sendRequest(getApplicationContext(), Statics.COMPANY_ENDPOINT, r, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(LOG, "########### SIGN-IN RESPONSE coming in");
                        setRefreshActionButtonState(false);
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        if (response.getCompanyStaff() == null) {
                            Log.e(LOG, "CompanyStaff is null, ignoring .... " + response.getMessage());
                            return;
                        }

                        SharedUtil.saveCompanyStaff(ctx, response.getCompanyStaff());

                        Intent intent = new Intent(ctx, MainPagerActivity.class);
                        intent.putExtra("response", response);
                        startActivity(intent);

                        CacheUtil.cacheData(ctx, response, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
                            @Override
                            public void onFileDataDeserialized(ResponseDTO response) {

                            }

                            @Override
                            public void onDataCached() {

                            }
                        });


                        finish();
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        ToastUtil.errorToast(ctx, ctx.getResources().getString(com.boha.monitor.library.R.string.error_server_comms));
                    }
                });
            }
        });


    }

    GcmDeviceDTO gcmDevice;

    private void setFields() {

        ePin = (EditText) findViewById(com.boha.monitor.library.R.id.SI_pin);
        spinnerEmail = (Spinner) findViewById(com.boha.monitor.library.R.id.SI_spinner);
        txtApp = (TextView) findViewById(R.id.SI_app);
        txtApp.setText(ctx.getResources().getString(R.string.scorer_app));
        btnSave = (Button) findViewById(com.boha.monitor.library.R.id.SI_btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSignIn();

            }
        });

    }

    @Override
    public void onPause() {
        overridePendingTransition(com.boha.monitor.library.R.anim.slide_in_left, com.boha.monitor.library.R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(LOG, "onResume ...nothing to be done");
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        Log.d(LOG, "--- onSaveInstanceState ...");
        super.onSaveInstanceState(b);
    }

    Menu mMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signin, menu);
        mMenu = menu;

        return true;
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.menu_help);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(com.boha.monitor.library.R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_help) {
            ToastUtil.toast(ctx, ctx.getResources().getString(R.string.under_cons));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDisconnected() {
        Log.w(LOG, "### ---> PlayServices onDisconnected() ");
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG, "onConnection failed: " + connectionResult.toString());
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    public void getEmail() {
        AccountManager am = AccountManager.get(getApplicationContext());
        Account[] accts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        if (accts.length == 0) {
            //TODO - send user to create acct
            ToastUtil.errorToast(ctx, "No Google account found. Please create one and try again");
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
                    com.boha.monitor.library.R.layout.xsimple_spinner_item, tarList);
            dataAdapter
                    .setDropDownViewResource(com.boha.monitor.library.R.layout.xsimple_spinner_dropdown_item);
            spinnerEmail.setAdapter(dataAdapter);
            spinnerEmail
                    .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> arg0,
                                                   View arg1, int index, long arg3) {
                            if (index ==  0) {
                                email = null;
                                return;
                            }
                            email = tarList.get(index);
                            Log.d("Reg", "###### Email account selected is "
                                    + email);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {

                        }
                    });
        }
    }

    Button btnSave;
    EditText ePin;
    TextView txtApp;
    Spinner spinnerEmail;
    static final String LOG = "SignInActivity";
    String email;

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG, "### ---> PlayServices onConnected() - gotta go! >>");

    }

}
