package com.com.boha.monitor.library.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.ContractorClaimAdapter;
import com.com.boha.monitor.library.adapters.SpinnerListAdapter;
import com.com.boha.monitor.library.dto.ContractorClaimDTO;
import com.com.boha.monitor.library.dto.ContractorClaimSiteDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.util.FileDownloader;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContractorClaimListFragment extends Fragment implements PageFragment {

    private ContractorClaimListListener mListener;

    public ContractorClaimListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Context ctx;
    View view, topView;
    TextView txtCount, txtName;
    ProjectDTO project;
    ListView mListView;
    LayoutInflater inflater;
    ImageView hero;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contractor_claim_list, container, false);
        ctx = getActivity();
        this.inflater = inflater;

        txtCount = (TextView) view.findViewById(R.id.FCC_count);
        txtName = (TextView) view.findViewById(R.id.FCC_title);
        topView = view.findViewById(R.id.FCC_top);
        hero = (ImageView) view.findViewById(R.id.FCC_hero);
        mListView = (ListView) view.findViewById(R.id.FCC_list);

        Statics.setRobotoFontLight(ctx, txtName);
        hero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Util.collapse(topView, 1500, null);

            }
        });
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    public void collapseHeroImage() {
        Util.collapse(topView,1000,null);
    }
    public void expandHeroImage() {
        Util.expand(topView, 1200, null);
    }
    public void setProject(ProjectDTO p) {
        project = p;
        contractorClaimList = project.getContractorClaimList();
        setList();

    }

    ListPopupWindow invoicePopupWindow;
    List<String> list;
    static final String LOG = ContractorClaimListFragment.class.getSimpleName();
    private void setList() {
        Log.e(LOG,"###### setList .........");
        if (contractorClaimList == null) return;
        txtCount.setText("" + contractorClaimList.size());
        adapter = new ContractorClaimAdapter(ctx, R.layout.contractor_claim_item,
                contractorClaimList, new ContractorClaimAdapter.ContractorClaimAdapterListener() {
            @Override
            public void onProjectSiteListRequested(ContractorClaimDTO cc) {
                //TODO - start activity or dialog to add or remove project sites from claim
                Log.w(LOG, "#### onInvoiceActionsRequested");
                list = new ArrayList<>();
                for (ContractorClaimSiteDTO ccs: cc.getContractorClaimSiteList()) {
                    list.add(ctx.getString(R.string.site_no) + " : " + ccs.getProjectSite().getProjectSiteName());
                }

                invoicePopupWindow = new ListPopupWindow(getActivity());
                invoicePopupWindow.setAdapter(new SpinnerListAdapter(ctx,R.layout.xxsimple_spinner_item,list, SpinnerListAdapter.SITE_LIST,true));
                invoicePopupWindow.setAnchorView(txtName);
                invoicePopupWindow.setWidth(600);
                invoicePopupWindow.setModal(true);
                invoicePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        invoicePopupWindow.dismiss();
                    }
                });
                invoicePopupWindow.show();
            }

            @Override
            public void onPDFDownloadRequested(ContractorClaimDTO cc) {
                contractorClaim = cc;
                viewPDF();
            }

            @Override
            public void onInvoiceActionsRequested(ContractorClaimDTO claim) {
                Log.w(LOG, "#### onInvoiceActionsRequested");
                list = new ArrayList<>();
                list.add(ctx.getString(R.string.create_invoice));
                list.add(ctx.getString(R.string.download_invoice));
                list.add(ctx.getString(R.string.edit_prices));

                invoicePopupWindow = new ListPopupWindow(getActivity());
                invoicePopupWindow.setAdapter(new SpinnerListAdapter(ctx,R.layout.xxsimple_spinner_item,list, SpinnerListAdapter.INVOICE_ACTIONS, true));
                invoicePopupWindow.setAnchorView(txtName);
                invoicePopupWindow.setWidth(600);
                invoicePopupWindow.setModal(true);
                invoicePopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                //TODO - start invoice fragment
                                Log.i(LOG,"#### start invoice fragment to create new");
                                break;
                            case 1:
                                //TODO - downlaod invoice pdf
                                Log.i(LOG,"#### download pdf");
                                break;
                            case 2:
                                //TODO - edit task price
                                Log.i(LOG,"#### edit task price ");

                                break;
                        }
                        invoicePopupWindow.dismiss();
                    }
                });
                invoicePopupWindow.show();
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
                if (topView.getVisibility() == View.GONE) {
                    Util.flashOnce(txtCount,100,new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {
                            Util.expand(topView, 1200, null);
                        }
                    });
                } else {
                    Util.flashOnce(txtCount,100,new Util.UtilAnimationListener() {
                        @Override
                        public void onAnimationEnded() {
                            Util.collapse(topView, 1200, null);
                        }
                    });
                }
            }
        });
    }

    private void viewPDF() {
        FileDownloader.downloadContractorClaimPDF(ctx,
                contractorClaim.getContractorClaimID(),
                SharedUtil.getCompany(ctx).getCompanyID(),
                contractorClaim.getProjectID(),
                new FileDownloader.FileDownloaderListener() {
                    @Override
                    public void onFileDownloaded(final File file) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Uri uri = Uri.fromFile(file);
                                try {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setDataAndType(uri, "application/pdf");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                } catch (ActivityNotFoundException e) {
                                    Util.showErrorToast(ctx,
                                            "PDF Reader application is not installed in your device");
                                }
                            }
                        });
                    }

                    @Override
                    public void onError() {

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
     * the taskStatusList is empty. If you would like to change the text, call this method
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

    public void addContractorClaim(ContractorClaimDTO contractorClaim) {
        if (contractorClaimList == null) {
            contractorClaimList = new ArrayList<>();
        }
        contractorClaimList.add(0, contractorClaim);
        adapter.notifyDataSetChanged();
        mListView.setSelection(0);
        txtCount.setText("" + contractorClaimList.size());

        Util.animateRotationY(txtCount, 500);
    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow logoAnimator interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface ContractorClaimListListener {
        public void onContractorClaimClicked(ContractorClaimDTO contractorClaimDTO);
        public void onInvoiceActionsRequested(ContractorClaimDTO contractorClaimDTO);
    }

    ContractorClaimDTO contractorClaim;
    List<ContractorClaimDTO> contractorClaimList;
    ContractorClaimAdapter adapter;
}
