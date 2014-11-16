package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.BeneficiaryAdapter;
import com.com.boha.monitor.library.dialogs.ProjectPopupDialog;
import com.com.boha.monitor.library.dto.BeneficiaryDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.CacheUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BeneficiaryListFragment extends Fragment implements PageFragment {

    private BeneficiaryListListener mListener;
    private AbsListView mListView;

    public BeneficiaryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    View view;
    TextView txtCount, txtTitle, txtProjectName;
    static final String LOG = BeneficiaryListFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "######### onCreateView");
        view = inflater.inflate(R.layout.fragment_beneficiary_list, container, false);
        ctx = getActivity();
        mListView = (AbsListView) view.findViewById(R.id.BC_list);
        txtCount = (TextView) view.findViewById(R.id.BC_count);
        txtTitle = (TextView) view.findViewById(R.id.BC_title);
        txtProjectName= (TextView) view.findViewById(R.id.BC_projectName);
        Statics.setRobotoFontLight(ctx, txtTitle);
        txtProjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showProjectDialog();
            }
        });
        if (savedInstanceState != null) {
            Log.e(LOG,"##### onCreateView, savedInstanceState not null");
            ResponseDTO r = (ResponseDTO)savedInstanceState.getSerializable("projectList");
            projectList = r.getProjectList();
            txtProjectName.setText(projectList.get(0).getProjectName());
            getBeneficiaryList(projectList.get(0).getProjectID());
            return view;
        }
        Bundle args = getArguments();
        if (args != null) {
            ResponseDTO r = (ResponseDTO) args.getSerializable("projectList");
            projectList = r.getProjectList();
            txtProjectName.setText(projectList.get(0).getProjectName());
            getBeneficiaryList(projectList.get(0).getProjectID());
        }

        return view;
    }

    private void showProjectDialog() {
        ProjectPopupDialog diag = new ProjectPopupDialog();
        diag.setContext(ctx);
        diag.setProjectList(projectList);
        diag.setListener(new ProjectPopupDialog.ProjectPopupDialogListener() {
            @Override
            public void onProjectClicked(ProjectDTO p) {
                project = p;
                txtProjectName.setText(p.getProjectName());
                getBeneficiaryList(project.getProjectID());
            }
        });
        diag.show(getActivity().getFragmentManager(), "PROJ_DIAG");
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        Log.e(LOG,"####### onSaveInstanceState");
        ResponseDTO r = new ResponseDTO();
        r.setProjectList(projectList);
        state.putSerializable("projectList", r);
        super.onSaveInstanceState(state);
    }

    private void setList() {
        if (beneficiaryList == null) beneficiaryList = new ArrayList<>();
        txtCount.setText("" + beneficiaryList.size());
        adapter = new BeneficiaryAdapter(ctx, R.layout.beneficiary_card,
                beneficiaryList, new BeneficiaryAdapter.BeneficiaryAdapterListener() {
            @Override
            public void onBeneficiaryEditRequested(BeneficiaryDTO client) {

            }
        });
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                beneficiary = beneficiaryList.get(position);
                mListener.onBeneficiaryClicked(beneficiary);
            }
        });
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(ctx, "Under Construction");
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (BeneficiaryListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement BeneficiaryListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount, 500);

    }

    public void addBeneficiary(BeneficiaryDTO beneficiary) {
        if (beneficiaryList == null) {
            beneficiaryList = new ArrayList<>();
        }
        beneficiaryList.add(beneficiary);
        Collections.sort(beneficiaryList);
        adapter.notifyDataSetChanged();
        txtCount.setText("" + beneficiaryList.size());
        try {
            Thread.sleep(1000);
            Util.animateRotationY(txtCount, 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface BeneficiaryListListener {
        public void onBeneficiaryClicked(BeneficiaryDTO beneficiary);

        public void onBeneficiaryEditRequested(BeneficiaryDTO beneficiary);
    }

    BeneficiaryDTO beneficiary;
    List<BeneficiaryDTO> beneficiaryList;
    BeneficiaryAdapter adapter;
    List<ProjectDTO> projectList;
    ProjectDTO project;


    private void getBeneficiaryList(final Integer projectID) {
        CacheUtil.getCachedProjectData(ctx, CacheUtil.CACHE_PROJECT, projectID, new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                Log.w("BeneficiaryListFragment", "onFileDataDeserialized!");
                if (response != null) {
                    if (!response.getProjectList().isEmpty()) {
                        beneficiaryList = response.getProjectList().get(0).getBeneficiaryList();
                        setList();
                    }
                }
                Util.refreshProjectData(getActivity(), ctx, projectID, new Util.ProjectDataRefreshListener() {
                    @Override
                    public void onDataRefreshed(ProjectDTO project) {
                        beneficiaryList = project.getBeneficiaryList();
                        setList();
                    }

                    @Override
                    public void onError() {
                        Log.e("BeneficiaryListFragment", "Error doing shit!");
                    }
                });

            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {
                Log.e("BeneficiaryListFragment", "Error with cache...refreshing from server");
                Util.refreshProjectData(getActivity(), ctx, projectID, new Util.ProjectDataRefreshListener() {
                    @Override
                    public void onDataRefreshed(ProjectDTO project) {
                        beneficiaryList = project.getBeneficiaryList();
                        setList();
                    }

                    @Override
                    public void onError() {
                        Log.e("BeneficiaryListFragment", "Error refreshing project data!");
                    }
                });

            }
        });
    }


}
