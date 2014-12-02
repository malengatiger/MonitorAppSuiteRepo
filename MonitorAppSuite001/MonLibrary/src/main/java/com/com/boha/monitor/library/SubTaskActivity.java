package com.com.boha.monitor.library;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.fragments.SubTaskFragment;
import com.com.boha.monitor.library.util.SharedUtil;

public class SubTaskActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subrask);
        ctx = getApplicationContext();
        subTaskFragment = (SubTaskFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        TaskDTO task = (TaskDTO)getIntent().getSerializableExtra("task");
        subTaskFragment.setTask(task);
        setTitle(SharedUtil.getCompany(ctx).getCompanyName());
        getSupportActionBar().setSubtitle(SharedUtil.getCompanyStaff(ctx).getFullName());
    }


    SubTaskFragment subTaskFragment;
    Context ctx;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sub_task, menu);
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
}
