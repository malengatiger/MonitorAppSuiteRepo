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
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.ContractorClaimAdapter;
import com.com.boha.monitor.library.dto.ContractorClaimDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;

import java.util.ArrayList;
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
public class ContractorClaimListFragment extends Fragment implements PageFragment {

    private ContractorClaimListListener mListener;
    private AbsListView mListView;
    public ContractorClaimListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

           }

    Context ctx;
    View view;
    TextView txtCount, txtName;
    ProjectDTO project;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_contractor_claim_list, container, false);
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            project = (ProjectDTO) b.getSerializable("project");
            contractorClaimList = project.getContractorClaimList();
        }

        txtCount = (TextView)view.findViewById(R.id.FCC_count);
        txtName = (TextView)view.findViewById(R.id.FCC_title);

        Statics.setRobotoFontLight(ctx, txtName);
        setList();
        return view;
    }

    private void setList() {
        if (contractorClaimList == null) return;
        txtCount.setText("" + contractorClaimList.size());
        mListView = (AbsListView) view.findViewById(R.id.FCC_list);
        adapter = new ContractorClaimAdapter(ctx, R.layout.contractor_claim_item,
                contractorClaimList, new ContractorClaimAdapter.ContractorClaimAdapterListener() {
            @Override
            public void onProjectSiteListRequested(ContractorClaimDTO client) {
                    //TODO - start activity or dialog to add or remove project sites from claim
            }
        });
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                contractorClaim = contractorClaimList.get(position);
                mListener.onContractorClaimClicked(contractorClaim);
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
            mListener = (ContractorClaimListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement ContractorClaimListListener");
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
        Util.animateRotationY(txtCount,500);

    }

    public void addContractorClaim(ContractorClaimDTO contractorClaim) {
        if (contractorClaimList == null) {
            contractorClaimList = new ArrayList<>();
        }
        contractorClaimList.add(0,contractorClaim);
        adapter.notifyDataSetChanged();
        txtCount.setText("" + contractorClaimList.size());
        try {
            Thread.sleep(1000);
            Util.animateRotationY(txtCount,500);
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
    public interface ContractorClaimListListener {
        public void onContractorClaimClicked(ContractorClaimDTO contractorClaimDTO);
    }

    ContractorClaimDTO contractorClaim;
    List<ContractorClaimDTO> contractorClaimList;
    ContractorClaimAdapter adapter;
}
