package com.com.boha.monitor.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.fragments.ImageFragment;
import com.com.boha.monitor.library.fragments.ImageGridFragment;
import com.com.boha.monitor.library.fragments.PageFragment;
import com.com.boha.monitor.library.toolbox.BaseVolley;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.PhotoCache;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImagePagerActivity extends FragmentActivity implements ImageGridFragment.ImageListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_pager);
        ctx = getApplicationContext();
        mPager = (ViewPager) findViewById(R.id.pager);
        setTitle(ctx.getResources().getString(R.string.app_name));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_pager, menu);
        mMenu = menu;
        getData();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            return true;
        }
        if (id == R.id.action_refresh) {
            isRefresh = true;
            getData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static final int PROJECT = 1, SITE = 2, TASK = 3, STAFF = 4, CACHE = 5;
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
                });
                break;
            case PROJECT:
                w.setRequestType(RequestDTO.GET_PROJECT_IMAGE_FILENAMES);
                w.setCompanyID(company.getCompanyID());
                w.setProjectID(project.getProjectID());
                getServerData(w);
                break;
        }


    }

    private void getServerData(RequestDTO w) {
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
                        if (!ErrorUtil.checkServerError(ctx, r)) {
                            return;
                        }
                        response = r;
                        buildPages();
                        setTitle(response.getCompany().getCompanyName());
                        if (isRefresh) {
                            isRefresh = false;
                            mPager.setCurrentItem(currentPageIndex);
                        }
                        CacheUtil.cacheData(ctx, response, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
                            @Override
                            public void onFileDataDeserialized(ResponseDTO response) {

                            }

                            @Override
                            public void onDataCached() {

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
                        ToastUtil.errorToast(ctx, message);
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
                ResponseDTO r1 = new ResponseDTO();
                Bundle data1 = new Bundle();
                data1.putSerializable("photoCache", photoCache);
                imageGridFragment.setArguments(data1);
                pageFragmentList.add(imageGridFragment);
//                int count = 0;
//                for (PhotoUploadDTO s : photoCache.getPhotoUploadList()) {
//                    ImageFragment f = ImageFragment.newInstance(s.getImageFilePath());
//                    pageFragmentList.add(f);
//                    count++;
//                    if (count > 20) {
//                        break;
//                    }
//                }
                break;
        }
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
    public void onImageClicked(int index) {

        switch (type) {
            case CACHE:
                Intent i = new Intent(this,ImageActivity.class);
                i.putExtra("photoCache", photoCache);
                i.putExtra("index",index);
                startActivity(i);
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
