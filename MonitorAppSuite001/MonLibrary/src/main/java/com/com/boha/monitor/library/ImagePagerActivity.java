package com.com.boha.monitor.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.fragments.ImageFragment;
import com.com.boha.monitor.library.fragments.ImageGridFragment;
import com.com.boha.monitor.library.fragments.PageFragment;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.PhotoCache;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImagePagerActivity extends ActionBarActivity implements ImageGridFragment.ImageListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_pager);
        ctx = getApplicationContext();
        mPager = (ViewPager) findViewById(R.id.pager);
        PagerTitleStrip strip = (PagerTitleStrip)findViewById(R.id.pager_title_strip);
        strip.setVisibility(View.GONE);
        setTitle(ctx.getResources().getString(R.string.app_name));
        if (savedInstanceState != null) {
            Log.w(LOG,"##### savedInstanceState not null");
            type = savedInstanceState.getInt("type");
            projectSite = (ProjectSiteDTO)savedInstanceState.getSerializable("projectSite");
            project = (ProjectDTO)savedInstanceState.getSerializable("project");
            isRefresh = true;
            getData();
        } else {
            type = getIntent().getIntExtra("type", 0);
            projectSite = (ProjectSiteDTO) getIntent().getSerializableExtra("projectSite");
            project = (ProjectDTO) getIntent().getSerializableExtra("project");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle b) {
        b.putInt("type", type);
        if (project != null)
            b.putSerializable("project", project);
        if (projectSite != null)
            b.putSerializable("projectSite", projectSite);
        super.onSaveInstanceState(b);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_pager, menu);
        mMenu = menu;
        getData();
        return true;
    }

    static final int PICTURE_REQ = 231;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_camera) {
            Intent i = new Intent(this, PictureActivity.class);
            if (projectSite != null) {
                i.putExtra("type", PhotoUploadDTO.SITE_IMAGE);
                i.putExtra("projectSite", projectSite);
            }
            if (project != null) {
                i.putExtra("type", PhotoUploadDTO.PROJECT_IMAGE);
                i.putExtra("project", project);
            }
            startActivityForResult(i, PICTURE_REQ);
            return true;
        }
        if (id == R.id.action_refresh) {
            isRefresh = true;
            getData();
            return true;
        }
        if (id == R.id.action_help) {
            Util.showToast(ctx, ctx.getString(R.string.under_cons));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int req, int res, Intent data) {
        if (req == PICTURE_REQ) {
            if (res == RESULT_OK) {
                isRefresh = true;
                getData();
            }
        }
    }

    public static final int PROJECT = 1, SITE = 2, TASK = 3, STAFF = 4, CACHE = 5;
    int type;
    CompanyDTO company;


    private void getData() {
        company = SharedUtil.getCompany(ctx);
        RequestDTO w = new RequestDTO();
        if (type == 0) type = CACHE;
        switch (type) {
            case CACHE:
                CacheUtil.getCachedData(ctx, CacheUtil.CACHE_PHOTOS, new CacheUtil.CacheUtilListener() {
                    @Override
                    public void onFileDataDeserialized(ResponseDTO response) {
                        if (response != null) {
                            if (response.getPhotoCache() != null) {
                                photoCache = response.getPhotoCache();
                                buildPages();
                                return;
                            }
                        }
                    }

                    @Override
                    public void onDataCached() {

                    }

                    @Override
                    public void onError() {

                    }
                });
                break;
            case PROJECT:
                w.setRequestType(RequestDTO.GET_PROJECT_IMAGES);
                w.setCompanyID(company.getCompanyID());
                w.setProjectID(project.getProjectID());
                getServerData(w);
                break;
            case SITE:
                if (isRefresh) {
                    isRefresh = false;
                    w.setRequestType(RequestDTO.GET_SITE_IMAGES);
                    w.setCompanyID(company.getCompanyID());
                    w.setProjectSiteID(projectSite.getProjectSiteID());
                    getServerData(w);

                } else {
                    buildPages();
                }
                break;
        }


    }

    private void getServerData(RequestDTO w) {

        setRefreshActionButtonState(true);
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO r) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        if (!ErrorUtil.checkServerError(ctx, r)) {
                            return;
                        }
                        response = r;
                        buildPages();
                        if (isRefresh) {
                            isRefresh = false;
                            mPager.setCurrentItem(currentPageIndex);
                        }

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
                        Util.showErrorToast(ctx, message);
                    }
                });
            }
        });

    }

    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        imageGridFragment = new ImageGridFragment();

        switch (type) {
            case CACHE:
                Bundle data1 = new Bundle();
                data1.putSerializable("photoCache", photoCache);
                imageGridFragment.setArguments(data1);
                break;
            case SITE:
                setTitle(getString(R.string.site_gallery));
                getSupportActionBar().setSubtitle(projectSite.getProjectName());
                Bundle data2 = new Bundle();
                data2.putSerializable("projectSite", projectSite);
                imageGridFragment.setArguments(data2);
                break;
        }
        pageFragmentList.add(imageGridFragment);
        initializeAdapter();

    }


    private void initializeAdapter() {
        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                currentPageIndex = arg0;
                pageFragmentList.get(arg0).animateCounts();
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
    public void onPause() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        super.onPause();
    }

    @Override
    public void onImageClicked(PhotoUploadDTO photo, int index) {

        switch (type) {
            case CACHE:
                Intent i = new Intent(this, ImageActivity.class);
                i.putExtra("photoCache", photoCache);
                i.putExtra("index", index);
                startActivity(i);
                break;
            case SITE:
                Intent i2 = new Intent(this, ImageActivity.class);
                i2.putExtra("projectSite", projectSite);
                i2.putExtra("index", index);
                startActivity(i2);
                break;
        }

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
            String title = "";
            switch (type) {
                case SITE:
                    title = projectSite.getProjectSiteName();
                    break;
                case PROJECT:
                    title = project.getProjectName();
                    break;
                case CACHE:
                    if (pageFragmentList.get(position) instanceof ImageFragment) {
                        ImageFragment f = (ImageFragment) pageFragmentList.get(position);
                        String path = f.getPath();
                        if (path != null) {
                            File file = new File(path);
                            path = file.getName();
                            int index = path.indexOf(".");
                            String sTime = path.substring(1, index);
                            long time = Long.parseLong(sTime);
                            title = sdf.format(new Date(time));
                        }

                    } else {
                        title = "Gallery";
                    }
                    break;
            }


            return title;
        }
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

    static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
    ImageGridFragment imageGridFragment;
    List<PageFragment> pageFragmentList;
    PhotoCache photoCache;
    ProjectDTO project;
    ProjectSiteDTO projectSite;
    CompanyStaffDTO companyStaff;
    Context ctx;
    ViewPager mPager;
    Menu mMenu;
    int currentPageIndex;
    PagerAdapter adapter;
    ResponseDTO response;
    boolean isRefresh;
    static final String LOG = ImagePagerActivity.class.getSimpleName();
}
