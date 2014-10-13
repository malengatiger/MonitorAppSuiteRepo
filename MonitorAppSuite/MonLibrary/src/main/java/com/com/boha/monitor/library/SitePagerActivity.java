package com.com.boha.monitor.library;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.fragments.PageFragment;
import com.com.boha.monitor.library.fragments.ProjectSiteListFragment;
import com.com.boha.monitor.library.toolbox.BaseVolley;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import java.util.ArrayList;
import java.util.List;

public class SitePagerActivity extends FragmentActivity implements com.google.android.gms.location.LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, ProjectSiteListFragment.ProjectSiteListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pager);
        ctx = getApplicationContext();
        mPager = (ViewPager)findViewById(R.id.pager);

        project = (ProjectDTO)getIntent().getSerializableExtra("project");
        buildPages();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_pager, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    boolean isRefresh;
    private void getData() {
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_COMPANY_DATA);
        w.setCompanyID(1);

        if (!BaseVolley.checkNetworkOnDevice(ctx)) {
            return;
        }
        setRefreshActionButtonState(true);
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO r) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        if (!ErrorUtil.checkServerError(ctx,r)) {
                            return;
                        }
                       //TODO - use for refresh
                    }
                });

            }

            @Override
            public void onClose() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        if (mLocationClient != null) {
            mLocationClient.connect();
            Log.i(LOG,
                    "#################### onStart - locationClient connecting ... ");
        }

    }

    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
    }

    @Override
    public void onStop() {

        if (mLocationClient != null) {
            if (mLocationClient.isConnected()) {
                stopPeriodicUpdates();
            }

            // After disconnect() is called, the client is considered "dead".
            mLocationClient.disconnect();
            Log.e("map", "### onStop - locationClient disconnected: "
                    + mLocationClient.isConnected());
        }
        super.onStop();
    }

    @Override
    public void onLocationChanged(Location loc) {
        Log.e(LOG, "### .........Location changed, lat: "
                + loc.getLatitude() + " lng: "
                + loc.getLongitude()
                + " -- getting nearby clubs");
        latitude = loc.getLatitude();
        longitude = loc.getLongitude();
        mCurrentLocation = loc;


    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(LOG,
                "### ---> PlayServices onConnected() - gotta start something! >>");
        mCurrentLocation = mLocationClient.getLastLocation();
        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();
            Log.e(LOG, "######### onConnected() - starting ServerTask");
            onLocationChanged(mCurrentLocation);
        } else {
            Log.e("map", "$$$$ mCurrentLocation is NULL");
        }
    }

    @Override
    public void onDisconnected() {
        Log.e(LOG,
                "### ---> PlayServices onDisconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG,
                "### ---> PlayServices onConnectionFailed: " + connectionResult.toString());
    }

    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        projectSiteListFragment = new ProjectSiteListFragment();
        Bundle data1 = new Bundle();
        data1.putSerializable("project", project);
        projectSiteListFragment.setArguments(data1);


        pageFragmentList.add(projectSiteListFragment);

        initializeAdapter();

    }

    private void initializeAdapter() {
        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                currentPageIndex = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public void onProjectSiteClicked(ProjectSiteDTO projectSite) {

    }

    private class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            return (Fragment) pageFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return pageFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "Title";

            switch (position) {
                case 0:
                    title = project.getProjectName();
                    break;


                default:
                    break;
            }
            return title;
        }
    }

    @Override
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (mMenu != null) {
            final MenuItem refreshItem = mMenu.findItem(R.id.action_refresh);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.action_bar_progess);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    ProjectSiteListFragment projectSiteListFragment;
    List<PageFragment> pageFragmentList;
    double latitude, longitude;
    Location mCurrentLocation;
    Context ctx;
    ViewPager mPager;
    Menu mMenu;
    int currentPageIndex;
    PagerAdapter adapter;
    ProjectDTO project;
    LocationClient mLocationClient;
    static final String LOG = SitePagerActivity.class.getSimpleName();
}
