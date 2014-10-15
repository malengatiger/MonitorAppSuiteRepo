package com.boha.monitor.operations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.com.boha.monitor.library.PictureActivity;
import com.com.boha.monitor.library.adapters.DrawerAdapter;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.fragments.PageFragment;
import com.com.boha.monitor.library.fragments.ProjectListFragment;
import com.com.boha.monitor.library.fragments.StaffListFragment;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.SharedUtil;

import java.util.ArrayList;
import java.util.List;


public class MainPagerActivity extends FragmentActivity
        implements ProjectListFragment.ProjectListListener,
        StaffListFragment.CompanyStaffListListener{

    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mDrawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        ctx = getApplicationContext();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mPager = (ViewPager)findViewById(R.id.pager);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        titles = getResources().getStringArray(R.array.action_items);
        setDrawerList();
        setTitle(SharedUtil.getCompany(ctx).getCompanyName());
        CompanyStaffDTO staff = SharedUtil.getCompanyStaff(ctx);
        getActionBar().setSubtitle(staff.getFullName() + " - " + staff.getCompanyStaffType().getCompanyStaffTypeName());
        //
       // PhotoUploadService.uploadPendingPhotos(ctx);
    }

    private void setDrawerList() {
        CacheUtil.getCachedData(getApplicationContext(),CacheUtil.CACHE_DATA,new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO r) {
                CompanyDTO company = new CompanyDTO();
                if (r != null) {
                    response = r;
                    company = r.getCompany();
                    buildPages();
                }
                for (String s : titles) {
                    sTitles.add(s);
                }
                mDrawerAdapter = new DrawerAdapter(getApplicationContext(), R.layout.drawer_item, sTitles,company);
                drawerListView.setAdapter(mDrawerAdapter);
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        switch (i) {
                            case PROJECTS:
                                mPager.setCurrentItem(0,true);
                                mDrawerLayout.closeDrawers();
                                break;
                            case STAFF:
                                mPager.setCurrentItem(1, true);
                                mDrawerLayout.closeDrawers();
                                break;
                            case MANAGE_DATA:
                                break;
                            case SITE_REPORTS:
                                break;
                            case BENEFICIARIES:
                                break;
                            case PROJECT_MAPS:
                                break;
                            case INVOICES:
                                break;
                            case HAPPY_LETTERS:
                                break;
                            case STATUS_NOTIFICATIONS:
                                break;
                        }
                    }
                });
            }

            @Override
            public void onDataCached() {

            }
        });
    }
    public static final int PROJECTS = 0,
            STAFF = 1, MANAGE_DATA = 2, SITE_REPORTS = 3, BENEFICIARIES = 4, PROJECT_MAPS = 5,
            INVOICES = 6, HAPPY_LETTERS = 7, STATUS_NOTIFICATIONS = 8;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_pager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent i = new Intent(this, PictureActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        projectListFragment = new ProjectListFragment();
        Bundle data1 = new Bundle();
        data1.putSerializable("response", response);
        projectListFragment.setArguments(data1);

        staffListFragment = new StaffListFragment();
        staffListFragment.setArguments(data1);


        pageFragmentList.add(projectListFragment);
        pageFragmentList.add(staffListFragment);

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
    ProjectListFragment projectListFragment;
    StaffListFragment staffListFragment;
    PagerAdapter adapter;
    ViewPager mPager;
    Context ctx;
    ResponseDTO response;
    int currentPageIndex;

    @Override
    public void onProjectClicked(ProjectDTO project) {

    }

    @Override
    public void onCompanyStaffClicked(CompanyStaffDTO companyStaff) {

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
                    title = ctx.getResources().getString(R.string.company_projects);
                    break;
                case 1:
                    title = ctx.getResources().getString(R.string.company_staff);
                    break;


                default:
                    break;
            }
            return title;
        }
    }


    private List<PageFragment> pageFragmentList;
    private ListView drawerListView;
    private String[] titles;
    private List<String> sTitles = new ArrayList<>();
 /*
     <string-array name="action_items">
        <item>Projects</item>
        <item>Staff</item>
        <item>Manage Data</item>
        <item>Site Reports</item>
        <item>Beneficiaries</item>
        <item>Project Maps</item>
        <item>Invoices</item>
        <item>Happy Letters</item>
        <item>Company Clients</item>
        <item>Status Notifications</item>
     */
}
