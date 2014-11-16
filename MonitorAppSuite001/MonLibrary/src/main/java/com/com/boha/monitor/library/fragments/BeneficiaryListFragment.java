package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.BeneficiaryAdapter;
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

/**
 * A fragment representing a list of Items.
 * <project/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project/>
 * Activities containing this fragment MUST implement the ProjectSiteListListener
 * interface.
 */
public class BeneficiaryListFragment extends Fragment implements  PageFragment {

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
    TextView txtCount, txtName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_beneficiary_list, container, false);
        ctx = getActivity();
        mListView = (AbsListView) view.findViewById(R.id.BC_list);
        txtCount = (TextView) view.findViewById(R.id.BC_count);
        txtName = (TextView) view.findViewById(R.id.BC_title);
        Statics.setRobotoFontLight(ctx, txtName);

        Bundle b = getArguments();
        if (b != null) {
            ResponseDTO r = (ResponseDTO) b.getSerializable("response");
            beneficiaryList = r.getCompany().getBeneficiaryList();
            if (beneficiaryList == null) {
                beneficiaryList = new ArrayList<>();
            }
        }
        setList();
        setSpinner();
        return view;
    }

    private void setList() {
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
    Spinner spinner;
    private void setSpinner() {

        CacheUtil.getCachedData(ctx,CacheUtil.CACHE_DATA,new CacheUtil.CacheUtilListener() {
            @Override
            public void onFileDataDeserialized(ResponseDTO response) {
                if (response != null) {
                    projectList = response.getCompany().getProjectList();
                    List<String> list = new ArrayList<>();
                    list.add(ctx.getString(R.string.select_project));
                    for (ProjectDTO p: projectList) {
                        list.add(p.getProjectName());
                    }
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ctx, R.layout.xxsimple_spinner_item,list);
                    adapter1.setDropDownViewResource(R.layout.xxsimple_spinner_dropdown_item);
                    if (spinner == null) {
                        spinner = (Spinner)view.findViewById(R.id.BC_projectSpinner);
                    }
                    spinner.setAdapter(adapter1);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                project = null;
                            } else {
                                project = projectList.get(position - 1);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onDataCached() {

            }

            @Override
            public void onError() {

            }
        });
    }
}
