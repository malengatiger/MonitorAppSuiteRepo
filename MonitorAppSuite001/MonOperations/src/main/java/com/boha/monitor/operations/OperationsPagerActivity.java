package com.boha.monitor.operations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.com.boha.monitor.library.BeneficiaryImportActivity;
import com.com.boha.monitor.library.ClaimAndInvoicePagerActivity;
import com.com.boha.monitor.library.ImagePagerActivity;
import com.com.boha.monitor.library.MonitorMapActivity;
import com.com.boha.monitor.library.PictureActivity;
import com.com.boha.monitor.library.SitePagerActivity;
import com.com.boha.monitor.library.SubTaskActivity;
import com.com.boha.monitor.library.adapters.DrawerAdapter;
import com.com.boha.monitor.library.dialogs.BeneficiaryDialog;
import com.com.boha.monitor.library.dialogs.ClientDialog;
import com.com.boha.monitor.library.dialogs.EngineerDialog;
import com.com.boha.monitor.library.dialogs.ProjectDialog;
import com.com.boha.monitor.library.dialogs.StaffDialog;
import com.com.boha.monitor.library.dialogs.TaskAndProjectStatusDialog;
import com.com.boha.monitor.library.dialogs.TaskDialog;
import com.com.boha.monitor.library.dto.BeneficiaryDTO;
import com.com.boha.monitor.library.dto.ClientDTO;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.EngineerDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectStatusTypeDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.fragments.BeneficiaryListFragment;
import com.com.boha.monitor.library.fragments.ClientListFragment;
import com.com.boha.monitor.library.fragments.EngineerListFragment;
import com.com.boha.monitor.library.fragments.PageFragment;
import com.com.boha.monitor.library.fragments.ProjectListFragment;
import com.com.boha.monitor.library.fragments.ProjectStatusTypeListFragment;
import com.com.boha.monitor.library.fragments.SiteTaskAndStatusAssignmentFragment;
import com.com.boha.monitor.library.fragments.StaffListFragment;
import com.com.boha.monitor.library.fragments.TaskListFragment;
import com.com.boha.monitor.library.fragments.TaskStatusListFragment;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.ErrorUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.WebSocketUtil;

import java.util.ArrayList;
import java.util.List;


public class OperationsPagerActivity extends ActionBarActivity
        implements ProjectListFragment.ProjectListListener,
        StaffListFragment.CompanyStaffListListener,
        TaskStatusListFragment.TaskStatusListListener,
        ProjectStatusTypeListFragment.ProjectStatusTypeListListener,
        ClientListFragment.ClientListListener, TaskListFragment.TaskListListener,
        EngineerListFragment.EngineerListListener, BeneficiaryListFragment.BeneficiaryListListener{

    private DrawerLayout mDrawerLayout;
    private DrawerAdapter mDrawerAdapter;
    private List<ProjectDTO> projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        ctx = getApplicationContext();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mPager = (ViewPager) findViewById(R.id.pager);
        Toolbar tb = (Toolbar)findViewById(R.id.toolbar);
        if (tb != null) {
            //setSupportActionBar(tb);
        }
        PagerTitleStrip s = (PagerTitleStrip)findViewById(R.id.pager_title_strip);
        s.setVisibility(View.GONE);
        drawerListView = (ListView) findViewById(R.id.left_drawer);
        titles = getResources().getStringArray(R.array.action_items);
        setDrawerList();
        setTitle(SharedUtil.getCompany(ctx).getCompanyName());
        CompanyStaffDTO staff = SharedUtil.getCompanyStaff(ctx);
        getSupportActionBar().setSubtitle(staff.getFullName());

    }

    private void setDrawerList() {
        CacheUtil.getCachedData(getApplicationContext(), CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO r) {
                CompanyDTO company = new CompanyDTO();
                if (r != null) {
                    response = r;
                    if (response.getCompany() == null) {
                        getCompanyData();
                        return;
                    }
                    company = r.getCompany();
                    projectList = company.getProjectList();
                    buildPages();
                }
                for (String s : titles) {
                    sTitles.add(s);
                }
                mDrawerAdapter = new DrawerAdapter(getApplicationContext(), R.layout.drawer_item, sTitles, company);
                drawerListView.setAdapter(mDrawerAdapter);
                LayoutInflater in = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = in.inflate(R.layout.hero_image, null);
                drawerListView.addHeaderView(v);
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView tv = (TextView)view.findViewById(R.id.DI_txtTitle);
                        Log.w(LOG,"##### onItemClick, index: " + i + " title: " + tv.getText().toString());
                        mPager.setCurrentItem(i - 1, true);

                        mDrawerLayout.closeDrawers();
                    }
                });
                getCompanyData();
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {
                getCompanyData();
            }
        });
    }


    private void getCompanyData() {

        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.GET_COMPANY_DATA);
        w.setCompanyID(SharedUtil.getCompany(ctx).getCompanyID());

        WebSocketUtil.sendRequest(ctx, Statics.COMPANY_ENDPOINT, w, new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO r) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!ErrorUtil.checkServerError(ctx, r)) {
                            return;
                        }
                        response = r;
                        projectList = r.getCompany().getProjectList();
                        buildPages();
                    }
                });

                CacheUtil.cacheData(ctx, r, CacheUtil.CACHE_DATA, new CacheUtil.CacheUtilListener() {
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

            @Override
            public void onClose() {

            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.errorToast(ctx, message);
                    }
                });
            }
        });
    }

    public static final int PROJECTS = 0,
            STAFF = 1, CLIENTS = 2, TASK_STATUS = 3, BENEFICIARIES = 4, PROJECT_STATUS= 5,
            TASKS = 6, ENGINEERS = 7;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.operations_pager, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            PageFragment pf = pageFragmentList.get(currentPageIndex);
            if (pf instanceof ProjectListFragment) {
                ProjectDialog pd = new ProjectDialog();
                pd.setContext(ctx);
                pd.setAction(ProjectDTO.ACTION_ADD);
                pd.setProject(new ProjectDTO());
                pd.setListener(new ProjectDialog.ProjectDialogListener() {
                    @Override
                    public void onProjectAdded(final ProjectDTO project) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                projectListFragment.addProject(project);
                            }
                        });
                    }

                    @Override
                    public void onProjectUpdated(ProjectDTO project) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                pd.show(getFragmentManager(), "PROJ_DIAG");
            }
            if (pf instanceof StaffListFragment) {
                StaffDialog diag = new StaffDialog();
                diag.setAction(CompanyStaffDTO.ACTION_ADD);
                diag.setContext(ctx);
                diag.setListener(new StaffDialog.StaffDialogListener() {
                    @Override
                    public void onStaffAdded(final CompanyStaffDTO companyStaff) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //ToastUtil.toast(ctx, ctx.getResources().getString(R.string.company_staff_saved));
                                staffListFragment.addCompanyStaff(companyStaff);
                            }
                        });

                    }

                    @Override
                    public void onStaffUpdated(CompanyStaffDTO companyStaff) {

                    }

                    @Override
                    public void onError(final String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.errorToast(ctx, message);
                            }
                        });

                    }
                });
                diag.show(getFragmentManager(), "STAFF_DIALOG");
            }
            if (pf instanceof ClientListFragment) {
                ClientDialog cd = new ClientDialog();
                cd.setContext(ctx);
                cd.setClient(new ClientDTO());
                cd.setAction(ClientDTO.ACTION_ADD);
                cd.setListener(new ClientDialog.ClientDialogListener() {
                    @Override
                    public void onClientAdded(final ClientDTO client) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                clientListFragment.addClient(client);
                            }
                        });
                    }

                    @Override
                    public void onClientUpdated(ClientDTO client) {

                    }

                    @Override
                    public void onError(final String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.errorToast(ctx,message);
                            }
                        });
                    }
                });
                cd.show(getFragmentManager(),"CLIENT_DIAG");
            }
            if (pf instanceof TaskListFragment) {
                TaskDialog td = new TaskDialog();
                td.setContext(ctx);
                td.setAction(TaskDTO.ACTION_ADD);
                td.setListener(new TaskDialog.TaskDialogListener() {
                    @Override
                    public void onTaskAdded(TaskDTO task) {
                        taskListFragment.addTask(task);
                    }

                    @Override
                    public void onTaskUpdated(TaskDTO task) {

                    }

                    @Override
                    public void onTaskDeleted(TaskDTO task) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                td.show(getFragmentManager(),"TD_DIAG");
            }
            if (pf instanceof ProjectStatusTypeListFragment) {
                TaskAndProjectStatusDialog d2 = new TaskAndProjectStatusDialog();
                d2.setContext(ctx);
                d2.setAction(TaskAndProjectStatusDialog.ACTION_ADD);
                d2.setType(TaskAndProjectStatusDialog.PROJECT_STATUS);
                d2.setProjectStatusType(new ProjectStatusTypeDTO());
                d2.setListener(new TaskAndProjectStatusDialog.EditDialogListener() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(final String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.errorToast(ctx,message);
                            }
                        });
                    }
                });
                d2.show(getFragmentManager(), "EDIT_DIALOG");
            }
            if (pf instanceof EngineerListFragment) {
                EngineerDialog ed = new EngineerDialog();
                ed.setContext(ctx);
                ed.setAction(EngineerDTO.ACTION_ADD);
                ed.setListener(new EngineerDialog.EngineerDialogListener() {
                    @Override
                    public void onEngineerAdded(EngineerDTO engineer) {
                        engineerListFragment.addEngineer(engineer);
                    }

                    @Override
                    public void onEngineerUpdated(EngineerDTO engineer) {

                    }

                    @Override
                    public void onEngineerDeleted(EngineerDTO engineer) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                ed.show(getFragmentManager(),"ENG_DIAG");
            }
            if (pf instanceof BeneficiaryListFragment) {
                BeneficiaryDialog bd = new BeneficiaryDialog();
                bd.setContext(ctx);
                bd.setAction(BeneficiaryDTO.ACTION_ADD);
                bd.setListener(new BeneficiaryDialog.BeneficiaryDialogListener() {
                    @Override
                    public void onBeneficiaryAdded(BeneficiaryDTO beneficiary) {
                        beneficiaryListFragment.addBeneficiary(beneficiary);
                    }

                    @Override
                    public void onBeneficiaryUpdated(BeneficiaryDTO beneficiary) {

                    }

                    @Override
                    public void onBeneficiaryDeleted(BeneficiaryDTO beneficiary) {

                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                bd.show(getFragmentManager(),"BEN_DIAG");
            }
            if (pf instanceof TaskStatusListFragment) {
                TaskAndProjectStatusDialog d = new TaskAndProjectStatusDialog();
                d.setContext(ctx);
                d.setAction(TaskAndProjectStatusDialog.ACTION_ADD);
                d.setType(TaskAndProjectStatusDialog.TASK_STATUS);
                d.setListener(new TaskAndProjectStatusDialog.EditDialogListener() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(final String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.errorToast(ctx,message);
                            }
                        });
                    }
                });
                d.show(getFragmentManager(),"TS_DIALOG");
            }
            return true;
        }
        if (id == R.id.action_refresh) {
            getCompanyData();
            return true;
        }
        if (id == R.id.action_gallery) {
            Intent i = new Intent(this, ImagePagerActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void buildPages() {

        if (pageFragmentList == null) {
            pageFragmentList = new ArrayList<>();

            projectListFragment = new ProjectListFragment();
            Bundle data1 = new Bundle();
            data1.putSerializable("response", response);
            projectListFragment.setArguments(data1);

            staffListFragment = new StaffListFragment();
            staffListFragment.setArguments(data1);

            taskStatusListFragment = new TaskStatusListFragment();
            taskStatusListFragment.setArguments(data1);

            projectStatusTypeListFragment = new ProjectStatusTypeListFragment();
            projectStatusTypeListFragment.setArguments(data1);

            clientListFragment = new ClientListFragment();
            clientListFragment.setArguments(data1);

            taskListFragment = new TaskListFragment();
            engineerListFragment = new EngineerListFragment();
            engineerListFragment.setArguments(data1);

            beneficiaryListFragment = new BeneficiaryListFragment();
            ResponseDTO xx = new ResponseDTO();
            data1 = new Bundle();
            xx.setProjectList(projectList);
            data1.putSerializable("projectList", xx);
            beneficiaryListFragment.setArguments(data1);

            pageFragmentList.add(projectListFragment);
            pageFragmentList.add(staffListFragment);
            pageFragmentList.add(clientListFragment);
            pageFragmentList.add(taskStatusListFragment);
            pageFragmentList.add(projectStatusTypeListFragment);
            pageFragmentList.add(taskListFragment);
            pageFragmentList.add(engineerListFragment);
            pageFragmentList.add(beneficiaryListFragment);

            initializeAdapter();
        } else {
                if (isRefreshedDueToImport) {
                    Log.i(LOG, "*** asking fragment to refresh itself");
                    isRefreshedDueToImport = false;
                    beneficiaryListFragment.refreshBeneficiaryList(project);
                }
            //TODO - refresh the rest of the fragments with new company data
        }


    }

    private void initializeAdapter() {
        adapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                currentPageIndex = arg0;
                PageFragment pf = pageFragmentList.get(currentPageIndex);
                if (pf instanceof BeneficiaryListFragment) {
                    beneficiaryListFragment.expandTopView();
                }
                if (pf instanceof TaskListFragment) {
                    taskListFragment.expandHeroImage();
                }
                if (pf instanceof StaffListFragment) {
                }
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
    TaskStatusListFragment taskStatusListFragment;
    ProjectStatusTypeListFragment projectStatusTypeListFragment;
    ClientListFragment clientListFragment;
    TaskListFragment taskListFragment;
    EngineerListFragment engineerListFragment;
    BeneficiaryListFragment beneficiaryListFragment;
    PagerAdapter adapter;
    ViewPager mPager;
    Context ctx;
    ResponseDTO response;
    int currentPageIndex;

    @Override
    public void onProjectClicked(ProjectDTO project) {

    }

    @Override
    public void onProjectEditDialogRequested(ProjectDTO project) {
        ProjectDialog pd = new ProjectDialog();
        pd.setAction(ProjectDTO.ACTION_UPDATE);
        pd.setContext(ctx);
        pd.setProject(project);
        pd.setListener(new ProjectDialog.ProjectDialogListener() {
            @Override
            public void onProjectAdded(final ProjectDTO project) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        projectListFragment.addProject(project);
                    }
                });
            }

            @Override
            public void onProjectUpdated(ProjectDTO project) {

            }

            @Override
            public void onError(String message) {

            }
        });
        pd.show(getFragmentManager(), "PDIAG");
    }

    @Override
    public void onProjectSitesRequested(ProjectDTO project) {

        Intent i = new Intent(this, SitePagerActivity.class);
        i.putExtra("project",project);
        i.putExtra("type", SiteTaskAndStatusAssignmentFragment.OPERATIONS);
        startActivity(i);

    }

    @Override
    public void onProjectPictureRequested(ProjectDTO project) {
        Intent i = new Intent(this,PictureActivity.class);
        i.putExtra("type", PhotoUploadDTO.PROJECT_IMAGE);
        i.putExtra("project",project);
        startActivity(i);
    }

    @Override
    public void onGalleryRequested(ProjectDTO project) {
        Intent i = new Intent(this,ImagePagerActivity.class);
        i.putExtra("project",project);
        i.putExtra("type",ImagePagerActivity.PROJECT);
        startActivity(i);
    }

    @Override
    public void onMapRequested(ProjectDTO project) {
        Intent i = new Intent(this, MonitorMapActivity.class);
        i.putExtra("project",project);
        startActivity(i);
    }

    @Override
    public void onClaimsAndInvoicesRequested(ProjectDTO project) {
        Intent i = new Intent(this,ClaimAndInvoicePagerActivity.class);
        i.putExtra("project",project);
        startActivity(i);
    }

    @Override
    public void onStatusReportRequested() {

    }

    @Override
    public void onCompanyStaffClicked(CompanyStaffDTO companyStaff) {

    }

    static final int PICTURE_REQUESTED = 9133;
    CompanyStaffDTO companyStaff;
    @Override
    public void onCompanyStaffPictureRequested(CompanyStaffDTO companyStaff) {
        this.companyStaff = companyStaff;
        Intent i = new Intent(this, PictureActivity.class);
        i.putExtra("companyStaff",companyStaff);
        i.putExtra("type", PhotoUploadDTO.STAFF_IMAGE);
        startActivityForResult(i,PICTURE_REQUESTED);
    }

    boolean isRefreshedDueToImport;
    ProjectDTO project;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w(LOG,"## onActivityResult");
        if (requestCode == PICTURE_REQUESTED) {
            if (resultCode == RESULT_OK) {
                Log.e(LOG,"############# refresh picture,  stafflist ");
                staffListFragment.refreshList(companyStaff);
            }
        }
        if (requestCode == IMPORT_REQUESTED) {
            Log.w(LOG,"## checking for data... from intent: " + data);
            if (resultCode == RESULT_OK) {
                project = (ProjectDTO)data.getSerializableExtra("project");
                Log.e(LOG,"## result is kool, retrive project loaded with beneficiaries: " + project.getBeneficiaryList().size());
                beneficiaryListFragment.refreshBeneficiaryList(project);
                //isRefreshedDueToImport = true;
                //getCompanyData();
            } else {
                Log.e(LOG,"## no project data found onActivityResult - ?????");
            }
        }
    }
    @Override
    public void onTaskStatusClicked(TaskStatusDTO taskStatus) {
        TaskAndProjectStatusDialog d = new TaskAndProjectStatusDialog();
        d.setContext(ctx);
        d.setAction(TaskAndProjectStatusDialog.ACTION_UPDATE);
        d.setType(TaskAndProjectStatusDialog.TASK_STATUS);
        d.setTaskStatus(taskStatus);
        d.setListener(new TaskAndProjectStatusDialog.EditDialogListener() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
        d.show(getFragmentManager(),"EDIT_DIALOG");
    }

    @Override
    public void onNewTaskStatusRequested() {
        TaskAndProjectStatusDialog d = new TaskAndProjectStatusDialog();
        d.setContext(ctx);
        d.setAction(TaskAndProjectStatusDialog.ACTION_ADD);
        d.setType(TaskAndProjectStatusDialog.TASK_STATUS);
        d.setListener(new TaskAndProjectStatusDialog.EditDialogListener() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
        d.show(getFragmentManager(),"EDIT_DIALOG");
    }

    @Override
    public void onProjectStatusTypeClicked(ProjectStatusTypeDTO statusType) {

        TaskAndProjectStatusDialog d = new TaskAndProjectStatusDialog();
        d.setContext(ctx);
        d.setProjectStatusType(statusType);
        d.setAction(TaskAndProjectStatusDialog.ACTION_UPDATE);
        d.setType(TaskAndProjectStatusDialog.PROJECT_STATUS);
        d.setListener(new TaskAndProjectStatusDialog.EditDialogListener() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
        d.show(getFragmentManager(), "EDIT_DIALOG");

    }

    @Override
    public void onNewProjectStatusTypeRequested() {
        TaskAndProjectStatusDialog d = new TaskAndProjectStatusDialog();
        d.setContext(ctx);
        d.setAction(TaskAndProjectStatusDialog.ACTION_ADD);
        d.setType(TaskAndProjectStatusDialog.PROJECT_STATUS);
        d.setListener(new TaskAndProjectStatusDialog.EditDialogListener() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onError(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
        d.show(getFragmentManager(), "EDIT_DIALOG");
    }

    @Override
    public void onClientClicked(ClientDTO client) {

    }

    @Override
    public void onClientEditRequested(ClientDTO client) {
        ClientDialog cd = new ClientDialog();
        cd.setContext(ctx);
        cd.setClient(client);
        cd.setAction(ClientDTO.ACTION_UPDATE);
        cd.setListener(new ClientDialog.ClientDialogListener() {
            @Override
            public void onClientAdded(ClientDTO client) {

            }

            @Override
            public void onClientUpdated(ClientDTO client) {

            }

            @Override
            public void onError(final String message) {
                Log.e(LOG, "---- ERROR websocket - " + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
        cd.show(getFragmentManager(),"PICD");
    }

    @Override
    public void onNewClientRequested() {
        ClientDialog cd = new ClientDialog();
        cd.setContext(ctx);
        cd.setAction(ClientDTO.ACTION_ADD);
        cd.setListener(new ClientDialog.ClientDialogListener() {
            @Override
            public void onClientAdded(ClientDTO client) {
                clientListFragment.addClient(client);
            }

            @Override
            public void onClientUpdated(ClientDTO client) {

            }

            @Override
            public void onError(final String message) {
                Log.e(LOG, "---- ERROR websocket - " + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
        cd.show(getFragmentManager(),"PICD");
    }

    @Override
    public void onTaskClicked(TaskDTO task) {

        TaskDialog td = new TaskDialog();
        td.setAction(TaskDTO.ACTION_UPDATE);
        td.setContext(ctx);
        td.setTask(task);
        td.setListener(new TaskDialog.TaskDialogListener() {
            @Override
            public void onTaskAdded(TaskDTO task) {
                taskListFragment.addTask(task);
            }

            @Override
            public void onTaskUpdated(TaskDTO task) {
                Log.i(LOG,"####### task updated");
            }

            @Override
            public void onTaskDeleted(TaskDTO task) {

            }

            @Override
            public void onError(String message) {

            }
        });
        td.show(getFragmentManager(),"TD_DIAG");
    }

    @Override
    public void onSubTasksRequested(TaskDTO task) {
        Intent i = new Intent(ctx, SubTaskActivity.class);
        i.putExtra("task", task);
        startActivity(i);
    }

    @Override
    public void onNewTaskRequested() {
        TaskDialog td = new TaskDialog();
        td.setAction(TaskDTO.ACTION_ADD);
        td.setContext(ctx);
        td.setListener(new TaskDialog.TaskDialogListener() {
            @Override
            public void onTaskAdded(TaskDTO task) {
                taskListFragment.addTask(task);
            }

            @Override
            public void onTaskUpdated(TaskDTO task) {
                Log.i(LOG,"####### task updated");
            }

            @Override
            public void onTaskDeleted(TaskDTO task) {

            }

            @Override
            public void onError(String message) {

            }
        });
        td.show(getFragmentManager(),"TD_DIAG");
    }

    static final String LOG = OperationsPagerActivity.class.getSimpleName();


    @Override
    public void onEngineerClicked(EngineerDTO engineer) {

    }

    @Override
    public void onEngineerEditRequested(EngineerDTO engineer) {
        EngineerDialog ed = new EngineerDialog();
        ed.setContext(ctx);
        ed.setAction(EngineerDTO.ACTION_UPDATE);
        ed.setListener(new EngineerDialog.EngineerDialogListener() {
            @Override
            public void onEngineerAdded(EngineerDTO engineer) {
                engineerListFragment.addEngineer(engineer);
            }

            @Override
            public void onEngineerUpdated(EngineerDTO engineer) {

            }

            @Override
            public void onEngineerDeleted(EngineerDTO engineer) {

            }

            @Override
            public void onError(String message) {

            }
        });
        ed.show(getFragmentManager(),"ENG_DIAG");
    }

    @Override
    public void onNewEngineerRequested() {
        EngineerDialog ed = new EngineerDialog();
        ed.setContext(ctx);
        ed.setAction(EngineerDTO.ACTION_ADD);
        ed.setListener(new EngineerDialog.EngineerDialogListener() {
            @Override
            public void onEngineerAdded(EngineerDTO engineer) {
                engineerListFragment.addEngineer(engineer);
            }

            @Override
            public void onEngineerUpdated(EngineerDTO engineer) {

            }

            @Override
            public void onEngineerDeleted(EngineerDTO engineer) {

            }

            @Override
            public void onError(String message) {

            }
        });
        ed.show(getFragmentManager(),"ENG_DIAG");
    }

    @Override
    public void onBeneficiaryClicked(BeneficiaryDTO beneficiary) {

    }

    @Override
    public void onBeneficiaryImportRequested(ProjectDTO project) {
        Intent i = new Intent(ctx, BeneficiaryImportActivity.class);
        if (project == null) {
            i.putExtra("project", projectList.get(0));
        } else {
            i.putExtra("project", project);
        }
        Log.e(LOG,"## starting activity for result");
        startActivityForResult(i, IMPORT_REQUESTED);
    }


    static final int IMPORT_REQUESTED = 6131;
    @Override
    public void onBeneficiaryEditRequested(BeneficiaryDTO beneficiary) {
        BeneficiaryDialog bd = new BeneficiaryDialog();
        bd.setContext(ctx);
        bd.setAction(BeneficiaryDTO.ACTION_UPDATE);
        bd.setBeneficiary(beneficiary);
        bd.setListener(new BeneficiaryDialog.BeneficiaryDialogListener() {
            @Override
            public void onBeneficiaryAdded(BeneficiaryDTO beneficiary) {
                beneficiaryListFragment.addBeneficiary(beneficiary);
            }

            @Override
            public void onBeneficiaryUpdated(BeneficiaryDTO beneficiary) {

            }

            @Override
            public void onBeneficiaryDeleted(BeneficiaryDTO beneficiary) {

            }

            @Override
            public void onError(final String message) {
                Log.e(LOG, "---- ERROR websocket - " + message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.errorToast(ctx,message);
                    }
                });
            }
        });
        bd.show(getFragmentManager(),"BEN_DIAG");
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

            PageFragment pf = pageFragmentList.get(position);
            if (pf instanceof ProjectListFragment) {
                title = ctx.getString(R.string.company_projects);
            }
            if (pf instanceof StaffListFragment) {
                title = ctx.getString(R.string.company_staff);
            }
            if (pf instanceof ClientListFragment) {
                title = ctx.getString(R.string.client_list);
            }
            if (pf instanceof TaskStatusListFragment) {
                title = ctx.getString(R.string.task_status);
            }
            if (pf instanceof ProjectStatusTypeListFragment) {
                title = ctx.getString(R.string.project_status);
            }
            if (pf instanceof TaskListFragment) {
                title = ctx.getString(R.string.tasks);
            }
            if (pf instanceof BeneficiaryListFragment) {
                title = ctx.getString(R.string.bennie_list);
            }
            if (pf instanceof EngineerListFragment) {
                title = ctx.getString(R.string.engineer_list);
            }


            return title;
        }
    }

    @Override
    public void onPause() {
        overridePendingTransition(com.boha.monitor.library.R.anim.slide_in_left, com.boha.monitor.library.R.anim.slide_out_right);
        super.onPause();
    }

    private List<PageFragment> pageFragmentList;
    private ListView drawerListView;
    private String[] titles;
    private List<String> sTitles = new ArrayList<>();

}
