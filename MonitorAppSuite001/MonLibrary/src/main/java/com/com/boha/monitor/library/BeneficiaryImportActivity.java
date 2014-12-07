package com.com.boha.monitor.library;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.fragments.BeneficiaryImportFragment;
import com.com.boha.monitor.library.util.SharedUtil;

public class BeneficiaryImportActivity extends ActionBarActivity implements BeneficiaryImportFragment.ImportListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ben_import);

        beneficiaryImportFragment = (BeneficiaryImportFragment)getFragmentManager().findFragmentById(R.id.fragment);
        project = (ProjectDTO)getIntent().getSerializableExtra("project");
        beneficiaryImportFragment.setProject(project);

        setTitle(SharedUtil.getCompany(getApplicationContext()).getCompanyName());
        getSupportActionBar().setSubtitle(project.getProjectName());
    }


    ProjectDTO project;
    BeneficiaryImportFragment beneficiaryImportFragment;
    boolean beneficiariesImported;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beneficiary_import, menu);
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
    public void onBeneficiariesImported(ProjectDTO project) {
        Log.d(LOG,"## onBeneficiariesImported. list: " + project.getBeneficiaryList().size());
        beneficiariesImported = true;
        this.project = project;
        onBackPressed();
    }
    static final String LOG = BeneficiaryImportActivity.class.getSimpleName();
    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        if (beneficiariesImported) {
            Log.i(LOG,"## passing project on to pager caller, list = " + project.getBeneficiaryList().size());
            i.putExtra("project",project);
            setResult(RESULT_OK, i);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
        //super.onBackPressed();
        Log.e(LOG,"##### onBackPressed");

    }
}
