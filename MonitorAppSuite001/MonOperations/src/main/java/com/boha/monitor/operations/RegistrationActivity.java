package com.boha.monitor.operations;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.GcmDeviceDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.GCMUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.acra.ACRA;

import java.util.ArrayList;


public class RegistrationActivity extends ActionBarActivity implements

        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
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

            Intent intent = new Intent(ctx, OperationsPagerActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        registerGCMDevice();
    }

    private void registerGCMDevice() {
        boolean ok = checkPlayServices();

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

    public boolean checkPlayServices() {
        Log.w(LOG, "checking GooglePlayServices .................");
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(ctx);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
               // GooglePlayServicesUtil.getErrorDialog(resultCode, this,
               //         PLAY_SERVICES_RESOLUTION_REQUEST).show();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms")));
                return false;
            } else {
                Log.i(LOG, "This device is not supported.");
                throw new UnsupportedOperationException("GooglePlayServicesUtil resultCode: " + resultCode);
            }
        }
        return true;
    }

    private void sendRegistration() {

        if (eGroup.getText().toString().isEmpty()) {
            ToastUtil.errorToast(ctx, ctx.getResources().getString(R.string.enter_comp_name));
            return;
        }
        if (eFirstName.getText().toString().isEmpty()) {
            ToastUtil.errorToast(ctx, ctx.getResources().getString(R.string.enter_firstname));
            return;
        }
        if (eLastName.getText().toString().isEmpty()) {
            ToastUtil.errorToast(ctx, ctx.getResources().getString(R.string.enter_lastname));
            return;
        }

        if (ePin.getText().toString().isEmpty()) {
            ToastUtil.errorToast(ctx, ctx.getResources().getString(R.string.enter_password));
            return;
        }

        if (email == null) {
            ToastUtil.errorToast(ctx, ctx.getResources().getString(R.string.select_email));
            return;
        }
        CompanyStaffDTO a = new CompanyStaffDTO();

        a.setEmail(email);
        a.setFirstName(eFirstName.getText().toString());
        a.setLastName(eLastName.getText().toString());
        a.setPin(ePin.getText().toString());
        a.setGcmDevice(gcmDevice);

        final CompanyDTO g = new CompanyDTO();
        g.setCompanyName(eGroup.getText().toString());


        RequestDTO r = new RequestDTO();
        r.setRequestType(RequestDTO.REGISTER_COMPANY);
        r.setCompany(g);
        r.setCompanyStaff(a);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, r, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (!ErrorUtil.checkServerError(ctx,response)) {
                            return;
                        }

                        SharedUtil.saveCompanyStaff(ctx, response.getCompanyStaff());
                        SharedUtil.saveCompany(ctx, response.getCompany());
                        if (response.getCompany() != null) {
                            ACRA.getErrorReporter().putCustomData("companyID", "" + response.getCompany().getCompanyID());
                            ACRA.getErrorReporter().putCustomData("companyName", response.getCompany().getCompanyName());
                        }

                        ResponseDTO countries = new ResponseDTO();
                        countries.setCountryList(response.getCountryList());

                        CacheUtil.cacheData(ctx, countries, CacheUtil.CACHE_COUNTRIES, new CacheUtil.CacheUtilListener() {
                            @Override
                            public void onFileDataDeserialized(ResponseDTO response) {

                            }

                            @Override
                            public void onDataCached() {
                                response.setCountryList(null);
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

                            @Override
                            public void onError() {

                            }
                        });


                        Intent intent = new Intent(ctx, OperationsPagerActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void onClose() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        ToastUtil.errorToast(ctx, message);

                    }
                });
            }
        });

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

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, r, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (response.getStatusCode() > 0) {
                            ToastUtil.errorToast(ctx, response.getMessage());
                            return;
                        }

                        SharedUtil.saveCompany(ctx, response.getCompany());
                        SharedUtil.saveCompanyStaff(ctx, response.getCompanyStaff());
                        Intent intent = new Intent(ctx, OperationsPagerActivity.class);
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        ToastUtil.errorToast(ctx, message);
                    }
                });
            }
        });


    }

    boolean isRegistration;
    GcmDeviceDTO gcmDevice;
    ProgressBar progressBar;


    private void setFields() {
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        spinnerCountry = (Spinner) findViewById(R.id.EP_countrySpinner);
        eFirstName = (EditText) findViewById(R.id.EP_firstName);
        eLastName = (EditText) findViewById(R.id.EP_lastName);
        ePin = (EditText) findViewById(R.id.EP_password);
        spinnerEmail = (Spinner) findViewById(R.id.EP_emailSpinner);
        eGroup = (EditText) findViewById(R.id.EP_groupName);
        mainEPLayout = findViewById(R.id.REG_ediLayout);
        mainRegLayout = findViewById(R.id.REG_mainLayout);
        mainEPLayout.setVisibility(View.GONE);
        final TextView txtHdr = (TextView) findViewById(R.id.EP_header);
        btnStartSignIn = (Button) findViewById(R.id.REG_btnExisting);
        btnStartNewGroup = (Button) findViewById(R.id.REG_btnNewGroup);
        btnStartNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainRegLayout.setVisibility(View.GONE);
                mainEPLayout.setVisibility(View.VISIBLE);
                isRegistration = true;
                btnSave.setText(ctx.getResources().getString(R.string.register));
                txtHdr.setText(ctx.getResources().getString(R.string.company_reg));
            }
        });
        btnStartSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainRegLayout.setVisibility(View.GONE);
                mainEPLayout.setVisibility(View.VISIBLE);
                eGroup.setVisibility(View.GONE);
                eFirstName.setVisibility(View.GONE);
                eLastName.setVisibility(View.GONE);
                spinnerCountry.setVisibility(View.GONE);
                isRegistration = false;
                btnSave.setText(ctx.getResources().getString(R.string.sign_in));
                txtHdr.setText(ctx.getResources().getString(R.string.company_signin));
            }
        });

        btnSave = (Button) findViewById(R.id.EP_btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRegistration) {
                    sendRegistration();
                } else {
                    sendSignIn();
                }
            }
        });
        Button btnCan = (Button) findViewById(R.id.EP_btnCancel);
        btnCan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainRegLayout.setVisibility(View.VISIBLE);
                mainEPLayout.setVisibility(View.GONE);
                eGroup.setVisibility(View.VISIBLE);
                eFirstName.setVisibility(View.VISIBLE);
                eLastName.setVisibility(View.VISIBLE);
                spinnerCountry.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
        getMenuInflater().inflate(R.menu.registration, menu);
        mMenu = menu;


        return true;
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.menu_help);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_help:
                ToastUtil.toast(ctx, "Under Construction");
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
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

    View mainRegLayout, mainEPLayout;
    Button btnStartSignIn, btnStartNewGroup, btnSave;
    EditText eFirstName, eLastName, ePin, eGroup;
    Spinner spinnerEmail, spinnerCountry;
    static final String LOG = "RegistrationActivity";

    String email;

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG, "### ---> PlayServices onConnected() - gotta go! >>");

    }

}
