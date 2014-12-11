package com.com.boha.monitor.library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MonitorMapActivity extends ActionBarActivity
        implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener {

    LocationClient mLocationClient;
    GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    Location location;
    Context ctx;
    List<Marker> markers = new ArrayList<Marker>();
    static final String LOG = MonitorMapActivity.class.getSimpleName();
    boolean mResolvingError;
    static final long ONE_MINUTE = 1000 * 60;
    static final long FIVE_MINUTES = 1000 * 60 * 5;
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    ProjectSiteDTO projectSite;
    ProjectDTO project;
    int index;
    TextView text, txtCount;
    View topLayout;
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(LOG, "#### onCreate");
        super.onCreate(savedInstanceState);
        ctx = getApplicationContext();
        try {
            setContentView(R.layout.activity_monitor_map);
        } catch (Exception e) {
            Log.e(LOG,"######## cannot setContentView", e);
        }
        projectSite = (ProjectSiteDTO) getIntent().getSerializableExtra("projectSite");
        project = (ProjectDTO) getIntent().getSerializableExtra("project");
        index = getIntent().getIntExtra("index", 0);

        mLocationClient = new LocationClient(getApplicationContext(), this,
                this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        text = (TextView) findViewById(R.id.text);
        txtCount = (TextView) findViewById(R.id.count);
        txtCount.setText("0");
        Statics.setRobotoFontBold(ctx,text);

        topLayout = findViewById(R.id.top);

        if (projectSite != null) {
            txtCount.setText("1");
            text.setText(getString(R.string.site_map));
        }
        if (project != null) {
            text.setText(getString(R.string.project_map));
        }
        googleMap = mapFragment.getMap();
        if (googleMap == null) {
            Util.showToast(ctx, getString(R.string.map_not_available));
            finish();
            return;
        }
        setGoogleMap();
    }


    private void setGoogleMap() {
        googleMap.setMyLocationEnabled(true);
        googleMap.setBuildingsEnabled(true);
        //TODO - remove after test
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);
                Log.w(LOG, "********* onMapClick");
            }
        });
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng latLng = marker.getPosition();
                Location loc = new Location(location);
                loc.setLatitude(latLng.latitude);
                loc.setLongitude(latLng.longitude);

                float mf = location.distanceTo(loc);
                Log.w(LOG, "######### distance, again: " + mf);

                showPopup(latLng.latitude,latLng.longitude, marker.getTitle() + "\n" + marker.getSnippet());

//                if (mf > 100) {
//                    showDirectionsDialog(latLng.latitude, latLng.longitude);
//                } else {
//                    Util.showToast(ctx, "You are currently " + df.format(mf) + " metres from where the picture was taken");
//                }
                return true;
            }
        });
        if (projectSite != null) {
            if (projectSite.getLatitude() == null) {
                Util.showToast(ctx,"Site has no location coordinates. Unable to put site on map.");
                finish();
            } else {
                setOneMarker();
            }
        }
        if (project != null) {
            getCachedData();
        }

    }

    static final DecimalFormat df = new DecimalFormat("###,##0.00");

    private void getCachedData() {
        CacheUtil.getCachedProjectData(ctx,CacheUtil.CACHE_PROJECT,project.getProjectID(),new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    if (response.getProjectList() != null && !response.getProjectList().isEmpty()) {
                        project = response.getProjectList().get(0);
                        setProjectMarkers();
                        text.setText(project.getProjectName()  + " - Sites (" +  project.getProjectSiteList().size() + ")");
                    }
                }
                refreshProjectData();
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {
                refreshProjectData();
            }
        });
    }

    private void refreshProjectData() {
        RequestDTO w = new RequestDTO(RequestDTO.GET_PROJECT_DATA);
        w.setProjectID(project.getProjectID());
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx,response)) {
                            return;
                        }
                        project = response.getProjectList().get(0);
                        setProjectMarkers();
                        text.setText(project.getProjectName() + " - Sites (" +   project.getProjectSiteList().size() + ")");
                    }
                });
            }

            @Override
            public void onClose() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshProjectData();
                    }
                });
            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showErrorToast(ctx,message);
                    }
                });
            }
        });
    }
    private void setProjectMarkers() {

        googleMap.clear();
        LatLng point = null;
        int index = 0, count = 0;
        for (ProjectSiteDTO site : project.getProjectSiteList()) {
            if (site.getLatitude() == null) continue;;
            LatLng pnt = new LatLng(site.getLatitude(), site.getLongitude());
            if (index == 0) {
                point = pnt;
            }
            BitmapDescriptor desc = null;
            Short color = getLastStatus(site);
            if (color == null) {
                desc = BitmapDescriptorFactory.fromResource(R.drawable.bullet_pink_32);
            } else {
                switch (color) {
                    case TaskStatusDTO.STATUS_COLOR_RED:
                        desc = BitmapDescriptorFactory.fromResource(R.drawable.bullet_red);
                        break;
                    case TaskStatusDTO.STATUS_COLOR_GREEN:
                        desc = BitmapDescriptorFactory.fromResource(R.drawable.bullet_green_32);
                        break;
                    case TaskStatusDTO.STATUS_COLOR_YELLOW:
                        desc = BitmapDescriptorFactory.fromResource(R.drawable.bullet_yellow_32);
                        break;
                }
            }
            Marker m =
                    googleMap.addMarker(new MarkerOptions()
                            .title(site.getProjectSiteName())
                            .icon(desc)
                            .snippet(site.getProjectName())
                            .position(pnt));
            markers.add(m);
            index++;
            count++;
        }
        txtCount.setText(""+count);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 1.0f));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
        setTitle(project.getProjectName());
    }

    private Short getLastStatus(ProjectSiteDTO site) {
        Short s = null;
        List<ProjectSiteTaskStatusDTO> stList = new ArrayList<>();
        for (ProjectSiteTaskDTO task : site.getProjectSiteTaskList()) {
            stList.addAll(task.getProjectSiteTaskStatusList());
        }
        Collections.sort(stList);
        if (!stList.isEmpty()) {
            s = stList.get(0).getTaskStatus().getStatusColor();
        }
        return s;
    }

    private void setOneMarker() {
        if (projectSite.getLatitude() == null) {
            return;
        }
        LatLng pnt = new LatLng(projectSite.getLatitude(), projectSite.getLongitude());
        BitmapDescriptor desc = BitmapDescriptorFactory.fromResource(R.drawable.number_1);
        Marker m =
                googleMap.addMarker(new MarkerOptions()
                        .title(projectSite.getProjectName())
                        .icon(desc)
                        .snippet(projectSite.getProjectSiteName())
                        .position(pnt));
        markers.add(m);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pnt, 1.0f));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12.0f));
        setTitle(projectSite.getProjectName());
        getSupportActionBar().setSubtitle(projectSite.getProjectSiteName());
    }

    private void showDirectionsDialog(final double lat, final double lng) {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setTitle(ctx.getString(R.string.directions))
                .setMessage(ctx.getString(R.string.directions_text))
                .setPositiveButton(ctx.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDirectionsMap(lat, lng);
                    }
                })
                .setNegativeButton(ctx.getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    ListPopupWindow actionsWindow;
    List<String> list;
    private void showPopup(final double lat, final double lng, String title) {
        list = new ArrayList<>();
        list.add("Directions");
        list.add("Status Report");
        if (projectSite != null) {
            if (projectSite.getLocationConfirmed() == null) {
                list.add(getString(R.string.confirm_gps));
            }
        }
        Util.showPopupBasicWithHeroImage(ctx,this,list,topLayout,ctx.getString(R.string.select_action),new Util.UtilPopupListener() {
            @Override
            public void onItemSelected(int index) {
                switch (index) {
                    case 0:
                        startDirectionsMap(lat, lng);
                        break;
                    case 1:
                        Util.showToast(ctx, ctx.getString(R.string.under_cons));
                        break;
                    case 2:
                        confirmLocation();
                        break;

                }
            }
        });


    }
    private void confirmLocation() {
        RequestDTO w = new RequestDTO(RequestDTO.CONFIRM_LOCATION);
        w.setProjectSiteID(projectSite.getProjectSiteID());
        w.setLatitude(projectSite.getLatitude());
        w.setLongitude(projectSite.getLongitude());
        w.setAccuracy(projectSite.getAccuracy());

        WebSocketUtil.sendRequest(ctx,Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx,response)) {
                            return;
                        };
                        Log.d(LOG, "########## location confirmed, status code: " + response.getStatusCode());
                        projectSite.setLocationConfirmed(1);
                        coordsConfirmed = true;
                        Util.showToast(ctx, "Site location has been confirmed");
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(final String message) {
                Log.e(LOG, "---- ERROR websocket - " + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showErrorToast(ctx,message);
                    }
                });
            }
        });
    }
    private void startDirectionsMap(double lat, double lng) {
        Log.i(LOG, "startDirectionsMap ..........");
        String url = "http://maps.google.com/maps?saddr="
                + location.getLatitude() + "," + location.getLongitude()
                + "&daddr=" + lat + "," + lng + "&mode=driving";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.monitor_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        Log.e(LOG, "####### onLocationChanged");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(LOG, "################ onStart .... connect API and location clients ");
        if (!mResolvingError) {  // more about this later
            //mGoogleApiClient.connect();
            mLocationClient.connect();
        }

    }

    @Override
    protected void onStop() {
        Log.w(LOG, "############## onStop stopping google service clients");
        try {
            //mGoogleApiClient.disconnect();
            mLocationClient.disconnect();
        } catch (Exception e) {
            Log.e(LOG, "Failed to Stop something", e);
        }
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e(LOG, "########### onConnected .... what is in the bundle...?");
        location = mLocationClient.getLastLocation();

        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(FIVE_MINUTES);
        locationRequest.setInterval(ONE_MINUTE);

        mLocationClient.requestLocationUpdates(locationRequest, this);

    }

    @Override
    public void onDisconnected() {
        Log.e(LOG, "onDisconnected.....");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    List<BitmapDescriptor> bmdList = new ArrayList<BitmapDescriptor>();

    private void loadIcons() {
        try {
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_1));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_2));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_3));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_4));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_5));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_6));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_7));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_8));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_9));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_10));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_11));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_12));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_13));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_14));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_15));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_16));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_17));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_18));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_19));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_20));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_21));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_22));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_23));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_24));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_25));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_26));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_27));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_28));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_29));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_30));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_31));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_32));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_33));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_34));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_35));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_36));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_37));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_38));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_39));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_40));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_41));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_42));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_43));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_44));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_45));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_46));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_47));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_48));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_49));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_50));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_51));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_52));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_53));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_54));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_55));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_56));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_57));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_58));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_59));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_60));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_61));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_62));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_63));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_64));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_65));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_66));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_67));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_68));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_69));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_70));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_71));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_72));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_73));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_74));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_75));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_76));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_77));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_78));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_79));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_80));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_81));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_82));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_83));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_84));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_85));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_86));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_87));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_88));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_89));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_90));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_91));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_92));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_93));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_94));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_95));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_96));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_97));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_98));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_99));
            bmdList.add(BitmapDescriptorFactory.fromResource(R.drawable.number_100));
        } catch (Exception e) {
            Log.e(LOG, "Load icons failed", e);
        }


    }

    boolean coordsConfirmed;
    @Override
    public void onBackPressed() {
        Log.e(LOG,"######## onBackPressed, coordsConfirmed: " + coordsConfirmed);
        if (coordsConfirmed) {
            Intent i = new Intent();
            i.putExtra("projectSite",projectSite);
            setResult(RESULT_OK, i);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

}
