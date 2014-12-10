package com.com.boha.monitor.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.fragments.StaffFragment;

public class StaffActivity extends ActionBarActivity implements StaffFragment.StaffFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);
        staffFragment = (StaffFragment)getFragmentManager().findFragmentById(R.id.fragment);
        CompanyStaffDTO staff = (CompanyStaffDTO) getIntent().getSerializableExtra("companyStaff");
        staffFragment.setCompanyStaff(staff);
    }


    StaffFragment staffFragment;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_staff, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        overridePendingTransition(com.boha.monitor.library.R.anim.slide_in_left, com.boha.monitor.library.R.anim.slide_out_right);
        super.onPause();
    }

    CompanyStaffDTO companyStaff;
    @Override
    public void onStaffAdded(CompanyStaffDTO companyStaff) {
        this.companyStaff = companyStaff;
        onBackPressed();
    }

    @Override
    public void onStaffUpdated(CompanyStaffDTO companyStaff) {
        onBackPressed();
    }
    @Override
    public void onBackPressed() {
        if (companyStaff != null) {
            Intent i = new Intent();
            i.putExtra("companyStaff",companyStaff);
            setResult(RESULT_OK, i);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
