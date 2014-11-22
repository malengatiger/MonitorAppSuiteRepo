package com.com.boha.monitor.library;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ContractorClaimDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.fragments.ContractorClaimFragment;
import com.com.boha.monitor.library.fragments.ContractorClaimListFragment;
import com.com.boha.monitor.library.fragments.PageFragment;

import java.util.ArrayList;
import java.util.List;


public class ClaimAndInvoicePagerActivity extends FragmentActivity
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
        setTitle(ctx.getString(R.string.project_sites));
        getActionBar().setSubtitle(project.getProjectName());
    }


    private void buildPages() {

        pageFragmentList = new ArrayList<>();
        contractorClaimFragment = new ContractorClaimFragment();
        Bundle b1 = new Bundle();
        b1.putSerializable("project",project);
        contractorClaimFragment.setArguments(b1);
        //
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
        getMenuInflater().inflate(R.menu.claim_and_invoice_pager, menu);
        mMenu = menu;
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
    public void onContractorClaimAdded(ContractorClaimDTO contractorClaimDTO) {

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

    Menu mMenu;
    int currentPageIndex;
    List<PageFragment> pageFragmentList;
    ContractorClaimFragment contractorClaimFragment;
    ContractorClaimListFragment contractorClaimListFragment;
}
