package com.com.boha.monitor.library;

import android.content.Context;
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
import android.widget.ListPopupWindow;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ContractorClaimDTO;
import com.com.boha.monitor.library.dto.EngineerDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.fragments.ContractorClaimFragment;
import com.com.boha.monitor.library.fragments.ContractorClaimListFragment;
import com.com.boha.monitor.library.fragments.PageFragment;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;
import java.util.List;


public class ClaimAndInvoicePagerActivity extends ActionBarActivity
implements ContractorClaimFragment.ContractorClaimFragmentListener, ContractorClaimListFragment.ContractorClaimListListener{

    ViewPager mPager;
    Context ctx;
    ProjectDTO project;
    static final int NUM_ITEMS = 2;
    PagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_and_invoice_pager);
        ctx = getApplicationContext();
        mPager = (ViewPager) findViewById(R.id.SITE_pager);
        mPager.setOffscreenPageLimit(NUM_ITEMS-1);
        PagerTitleStrip strip = (PagerTitleStrip)findViewById(R.id.pager_title_strip);
        strip.setVisibility(View.GONE);

        project = (ProjectDTO) getIntent().getSerializableExtra("project");

        buildPages();
        setTitle(getString(R.string.claims_invoices));
        getSupportActionBar().setSubtitle(project.getProjectName());
    }

    private void getCachedProjectData() {
        setRefreshActionButtonState(true);
        CacheUtil.getCachedProjectData(ctx,CacheUtil.CACHE_PROJECT,project.getProjectID(),new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    if (response.getProjectList() != null && !response.getProjectList().isEmpty()) {
                        project = response.getProjectList().get(0);
                        contractorClaimFragment.setProject(project);
                        contractorClaimListFragment.setProject(project);
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

        setRefreshActionButtonState(true);
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        if (!ErrorUtil.checkServerError(ctx,response)) {
                            return;
                        }
                        project = response.getProjectList().get(0);
                        contractorClaimFragment.setProject(project);
                        contractorClaimListFragment.setProject(project);
                        CacheUtil.cacheProjectData(ctx,response,CacheUtil.CACHE_PROJECT,
                                project.getProjectID(),new CacheUtil.CacheUtilListener() {
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
                setRefreshActionButtonState(false);
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

    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        contractorClaimFragment = new ContractorClaimFragment();
        contractorClaimListFragment = new ContractorClaimListFragment();

        pageFragmentList.add(contractorClaimFragment);
        pageFragmentList.add(contractorClaimListFragment);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(LOG,"##### onCreateOptionsMenu getCachedCompanyData ...");
        getMenuInflater().inflate(R.menu.claim_and_invoice_pager, menu);
        mMenu = menu;
        getCachedCompanyData();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            refreshCompanyData();
            return true;
        }
        if (id == R.id.action_invoices) {
            return true;
        }
        if (id == R.id.action_claims) {
            return true;
        }
        if (id == R.id.action_help) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onContractorClaimAdded(ContractorClaimDTO contractorClaimDTO) {
        contractorClaimListFragment.addContractorClaim(contractorClaimDTO);
        mPager.setCurrentItem(1);
    }

    @Override
    public void onContractorClaimUpdated(ContractorClaimDTO contractorClaimDTO) {

    }

    @Override
    public void onContractorClaimDeleted(ContractorClaimDTO contractorClaimDTO) {

    }

    @Override
    public void onContractorClaimClicked(ContractorClaimDTO contractorClaimDTO) {

    }

    ListPopupWindow invoicePopupWindow;
    List<String> list;
    @Override
    public void onInvoiceActionsRequested(ContractorClaimDTO contractorClaimDTO) {
        //TODO popup actions for invoices - generate new, download existing

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

    List<EngineerDTO> engineerList;
    List<TaskDTO> taskList;
    static final String LOG = ClaimAndInvoicePagerActivity.class.getSimpleName();
    private void getCachedCompanyData() {

        CacheUtil.getCachedData(ctx, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    Log.d(LOG, "CacheUtil.CACHE_DATA cached response ok...............");
                    if (response.getCompany() == null || response.getCompany().getTaskList() == null) {
                        Log.e(LOG,"---- cache company or taskList is NULL, refreshing company data");
                        refreshCompanyData();
                        return;
                    }
                    taskList = response.getCompany().getTaskList();
                    engineerList = response.getCompany().getEngineerList();
                    contractorClaimFragment.setData(engineerList, taskList);
                } else {
                    Log.e(LOG, "-- ERROR - company cache is null");
                }
                refreshCompanyData();
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {
                Log.e(LOG,"------> Cache Error");
                refreshCompanyData();
            }
        });
    }
    private void refreshCompanyData() {
        RequestDTO w = new RequestDTO(RequestDTO.GET_COMPANY_DATA);
        w.setCompanyID(SharedUtil.getCompany(ctx).getCompanyID());
        setRefreshActionButtonState(true);
        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshActionButtonState(false);
                        if (!ErrorUtil.checkServerError(ctx, response)) {
                            return;
                        }
                        engineerList = response.getCompany().getEngineerList();
                        taskList = response.getCompany().getTaskList();
                        contractorClaimFragment.setData(engineerList, taskList);

                        CacheUtil.cacheData(ctx, response, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
                            @Override
                            public void onFileDataDeserialized(ResponseDTO response) {

                            }

                            @Override
                            public void onDataCached() {
                                getCachedProjectData();
                            }

                            @Override
                            public void onError() {
                                getCachedProjectData();
                            }
                        });
                    }
                });
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(String message) {

            }
        });
    }

    Menu mMenu;
    int currentPageIndex;
    List<PageFragment> pageFragmentList;
    ContractorClaimFragment contractorClaimFragment;
    ContractorClaimListFragment contractorClaimListFragment;
}
