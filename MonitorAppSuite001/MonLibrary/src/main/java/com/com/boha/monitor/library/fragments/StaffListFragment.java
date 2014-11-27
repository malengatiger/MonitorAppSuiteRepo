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
import android.widget.ListPopupWindow;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.SpinnerListAdapter;
import com.com.boha.monitor.library.adapters.StaffAdapter;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a taskStatusList of Items.
 * <project/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <project/>
 * Activities containing this fragment MUST implement the ProjectSiteListListener
 * interface.
 */
public class StaffListFragment extends Fragment
        implements  PageFragment {

    private CompanyStaffListListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    public StaffListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    Context ctx;
    TextView txtCount, txtName;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_staff_list, container, false);
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            ResponseDTO r = (ResponseDTO) b.getSerializable("response");
            companyStaffList = r.getCompany().getCompanyStaffList();
        }

        txtCount = (TextView) view.findViewById(R.id.STAFF_LIST_staffCount);
        txtName = (TextView) view.findViewById(R.id.STAFF_LIST_label);

        Statics.setRobotoFontLight(ctx, txtName);
        txtCount.setText("" + companyStaffList.size());
        setList();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CompanyStaffListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement CompanyStaffListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    ListPopupWindow staffActionsWindow;
    List<String> list;
    private void setList() {
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        staffAdapter = new StaffAdapter(ctx, R.layout.staff_card,
                companyStaffList, new StaffAdapter.StaffAdapterListener() {
            @Override
            public void onPictureRequested(CompanyStaffDTO staff) {
                mListener.onCompanyStaffPictureRequested(staff);
            }

            @Override
            public void onStatusUpdatesRequested(CompanyStaffDTO staff) {

            }
        });
        mListView.setAdapter(staffAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (null != mListener) {
                    companyStaffDTO = companyStaffList.get(position);
                    mListener.onCompanyStaffClicked(companyStaffDTO);
                    list = new ArrayList<>();
                    list.add(ctx.getString(R.string.get_status));
                    list.add(ctx.getString(R.string.take_picture));
                    list.add(ctx.getString(R.string.send_app_link));
                    list.add(ctx.getString(R.string.edit_staff));


                    staffActionsWindow = new ListPopupWindow(getActivity());
                    staffActionsWindow.setAdapter(new SpinnerListAdapter(ctx, R.layout.xxsimple_spinner_item,
                            list, SpinnerListAdapter.STAFF_ACTIONS, false));
                    staffActionsWindow.setAnchorView(txtName);
                    staffActionsWindow.setWidth(600);
                    staffActionsWindow.setModal(true);
                    staffActionsWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            staffActionsWindow.dismiss();
                        }
                    });
                    staffActionsWindow.show();
                }
            }
        });
    }

    CompanyStaffDTO companyStaffDTO;

    public CompanyStaffDTO getCompanyStaffDTO() {
        return companyStaffDTO;
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

    public void addCompanyStaff(CompanyStaffDTO staff) {
        if (companyStaffList == null) {
            companyStaffList = new ArrayList<>();
        }
        companyStaffList.add(staff);
        Collections.sort(companyStaffList);
        staffAdapter.notifyDataSetChanged();
        txtCount.setText("" + companyStaffList.size());
        try {
            Thread.sleep(1000);
            Util.animateRotationY(txtCount, 500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void refreshList(CompanyStaffDTO staff) {
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
        setList();

        int index = 0;
        for (CompanyStaffDTO c: companyStaffList) {
            if (staff.getCompanyStaffID() == c.getCompanyStaffID()) {
                break;
            }
            index++;
        }
        if (index < companyStaffList.size()) {
            mListView.setSelection(index);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow objectAnimator interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <project/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface CompanyStaffListListener {
        public void onCompanyStaffClicked(CompanyStaffDTO companyStaff);

        public void onCompanyStaffPictureRequested(CompanyStaffDTO companyStaff);
    }

    ProjectDTO project;
    List<CompanyStaffDTO> companyStaffList;
    StaffAdapter staffAdapter;
}
