package com.com.boha.monitor.library.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.text.DecimalFormat;


public class GPSScanFragment extends Fragment implements PageFragment {

    @Override
    public void animateCounts() {

    }

    public interface GPSScanFragmentListener {
        public void onStartScanRequested();

        public void onEndScanRequested();
        public void onMapRequested(ProjectSiteDTO projectSite);


    }

    private GPSScanFragmentListener listener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GPSScanFragment.
     */
    public static GPSScanFragment newInstance() {
        GPSScanFragment fragment = new GPSScanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public GPSScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    TextView desiredAccuracy, txtLat, txtLng, txtAccuracy, txtName;
    Button btnScan, btnSave;
    View view;
    SeekBar seekBar;
    boolean isScanning;
    ProjectSiteDTO projectSite;
    ImageView imgLogo, hero;
    Context ctx;
    ObjectAnimator logoAnimator;
    long start, end;
    Chronometer chronometer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(LOG,"###### onCreateView");

        view = inflater.inflate(R.layout.fragment_gps, container, false);
        ctx = getActivity();

        setFields();
        return view;
    }

    public void startScan() {
        listener.onStartScanRequested();
        rotateLogo();
        txtAccuracy.setText("0.00");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        isScanning = true;
        btnScan.setText(ctx.getString(R.string.stop_scan));
    }
    private void setFields() {

        desiredAccuracy = (TextView) view.findViewById(R.id.GPS_desiredAccuracy);
        txtAccuracy = (TextView) view.findViewById(R.id.GPS_accuracy);
        txtLat = (TextView) view.findViewById(R.id.GPS_latitude);
        txtLng = (TextView) view.findViewById(R.id.GPS_longitude);
        btnSave = (Button) view.findViewById(R.id.GPS_btnSave);
        btnScan = (Button) view.findViewById(R.id.GPS_btnStop);
        seekBar = (SeekBar) view.findViewById(R.id.GPS_seekBar);
        imgLogo = (ImageView) view.findViewById(R.id.GPS_imgLogo);
        hero = (ImageView) view.findViewById(R.id.GPS_hero);
        txtName = (TextView) view.findViewById(R.id.GPS_siteName);
        chronometer = (Chronometer)view.findViewById(R.id.GPS_chrono);

        btnSave.setVisibility(View.GONE);
        Statics.setRobotoFontBold(ctx, txtLat);
        Statics.setRobotoFontBold(ctx, txtLng);

        imgLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(imgLogo,100,new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                       listener.onMapRequested(projectSite);
                    }
                });
            }
        });
        txtAccuracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtAccuracy,100,new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        listener.onMapRequested(projectSite);
                    }
                });

            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                desiredAccuracy.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnScan,100,new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        if (isScanning) {
                            listener.onEndScanRequested();
                            isScanning = false;
                            btnScan.setText(ctx.getString(R.string.start_scan));
                            stopRotatingLogo();
                            chronometer.stop();
                        } else {
                            listener.onStartScanRequested();
                            isScanning = true;
                            btnScan.setText(ctx.getString(R.string.stop_scan));
                            rotateLogo();
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();
                            Util.collapse(btnSave,300,null);
                        }
                    }
                });

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(btnSave,100,new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        sendGPSData();
                    }
                });

            }
        });
    }
    public void stopRotatingLogo() {
        pleaseStop = true;
        if (logoAnimator != null) {
            logoAnimator.cancel();
            Log.e(LOG, "###### stopRotatingLogo - logoAnimator.cancel");
        }
    }

    boolean pleaseStop;
    public void resetLogo() {
        logoAnimator = ObjectAnimator.ofFloat(imgLogo, "rotation", 0, 360);
        logoAnimator.setDuration(200);
        logoAnimator.start();
    }
    public void rotateLogo() {
        btnScan.setText(ctx.getString(R.string.stop_scan));
        if (logoAnimator != null) logoAnimator.cancel();
        logoAnimator = ObjectAnimator.ofFloat(imgLogo, "rotation", 0, 360);
        logoAnimator.setDuration(200);
        logoAnimator.setInterpolator(new AccelerateInterpolator());
        logoAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!pleaseStop) {
                    rotateLogo();
                } else {
                    pleaseStop = false;
                    resetLogo();
                    Log.w(LOG, "#### not repeating the logo anim anymore");
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
       // logoAnimator.start();
        //flashAccuracy();
    }
    public void flashAccuracy() {
        Log.w(LOG, "++++++= flashAccuracy ..............");
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(txtAccuracy, "alpha", 0, 1);
        objectAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimator.setDuration(200);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                flashAccuracy();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    private void sendGPSData() {

        RequestDTO w = new RequestDTO(RequestDTO.UPDATE_PROJECT_SITE);
        ProjectSiteDTO site = new ProjectSiteDTO();
        site.setProjectSiteID(projectSite.getProjectSiteID());
        site.setLatitude(location.getLatitude());
        site.setLongitude(location.getLongitude());
        site.setAccuracy(location.getAccuracy());
        w.setProjectSite(site);

        rotateLogo();
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stopRotatingLogo();
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        Log.w(LOG, "++++++++++++ project site location updated");
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(final String message) {
                Log.e(LOG, "---- ERROR websocket - " + message);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showErrorToast(ctx, message);
                    }
                });
            }
        });
    }

    public void setLocation(Location location) {
        if (projectSite == null) return;
        this.location = location;
        txtLat.setText("" + location.getLatitude());
        txtLng.setText("" + location.getLongitude());
        txtAccuracy.setText("" + location.getAccuracy());

        if (location.getAccuracy() == seekBar.getProgress()
                || location.getAccuracy() < seekBar.getProgress()) {
            listener.onEndScanRequested();
            isScanning = false;
            stopRotatingLogo();
            chronometer.stop();
            resetLogo();
            btnScan.setText(ctx.getString(R.string.start_scan));
            btnSave.setVisibility(View.VISIBLE);
            projectSite.setLatitude(location.getLatitude());
            projectSite.setLongitude(location.getLongitude());
            projectSite.setAccuracy(location.getAccuracy());
            Util.expand(btnSave,200,null);
            Log.d(LOG, "----------- onEndScanRequested - stopped scanning");
        }
        Util.flashSeveralTimes(hero,300,1, null);
    }

    static final DecimalFormat df = new DecimalFormat("###,###,###,##0.00");
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (GPSScanFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " - Host activity" + activity.getLocalClassName() + " must implement GPSScanFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    Location location;
    static final String LOG = GPSScanFragment.class.getSimpleName();

    public void setProjectSite(ProjectSiteDTO projectSite) {
        this.projectSite = projectSite;
        if (projectSite != null)
            txtName.setText(projectSite.getProjectSiteName());
    }

    public ProjectSiteDTO getProjectSite() {
        return projectSite;
    }
}
