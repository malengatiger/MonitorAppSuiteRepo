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

import com.boha.malengagolf.library.R;
import com.com.boha.monitor.library.adapters.StaffAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteStaffDTO;
import com.com.boha.monitor.library.dto.ResponseDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A fragment representing a list of ProjectSiteStaffDTO.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the ProjectSiteStaffListListener
 * interface.
 */
public class ProjectSiteStaffListFragment extends Fragment implements AbsListView.OnItemClickListener, PageFragment{



    private ProjectSiteStaffListListener mListener;

    private AbsListView mListView;
    private StaffAdapter mAdapter;
    Context ctx;
    ProjectDTO project;
    ResponseDTO response;
    TextView txtCount, txtLabel;

    public static ProjectSiteStaffListFragment newInstance(ProjectDTO project) {
        ProjectSiteStaffListFragment fragment = new ProjectSiteStaffListFragment();
        Bundle args = new Bundle();
        args.putSerializable("project", project);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProjectSiteStaffListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HashMap<Integer, ProjectSiteStaffDTO> map = new HashMap<>();
        if (getArguments() != null) {
            response = (ResponseDTO)getArguments().getSerializable("response");
            projectSiteStaffList = new ArrayList<>();
            for (ProjectDTO p: response.getCompany().getProjectList()) {
                for (ProjectSiteDTO ps : p.getProjectSiteList()) {
                    for (ProjectSiteStaffDTO pss : ps.getProjectSiteStaffList()) {
                        if (!map.containsKey(pss.getCompanyStaff().getCompanyStaffID())) {
                            map.put(pss.getCompanyStaff().getCompanyStaffID(), pss);
                        }
                    }
                }
            }
            Set<Map.Entry<Integer,ProjectSiteStaffDTO>> set;
            set = map.entrySet();
            Iterator iter = set.iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer,ProjectSiteStaffDTO> e = (Map.Entry<Integer, ProjectSiteStaffDTO>) iter.next();
                projectSiteStaffList.add(e.getValue());
            }
            //TODO sort the list ...
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projectsitestaff, container, false);
        ctx = getActivity();
        txtCount = (TextView)view.findViewById(R.id.STAFF_LIST_staffCount);
        txtLabel = (TextView)view.findViewById(R.id.STAFF_LIST_label);
        Statics.setRobotoFontLight(ctx,txtLabel);
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mAdapter = new StaffAdapter(ctx,R.layout.person_item,projectSiteStaffList);
        mListView.setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        txtCount.setText("" + projectSiteStaffList.size());
        animateCounts();


        return view;
    }

    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount, 500);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProjectSiteStaffListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement ProjectSiteStaffListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            projectSiteStaff = projectSiteStaffList.get(position);
            mListener.onSiteStaffClicked(projectSiteStaff);
        }
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

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface ProjectSiteStaffListListener {
        public void onSiteStaffClicked(ProjectSiteStaffDTO projectSiteStaff);
    }

    List<ProjectSiteStaffDTO> projectSiteStaffList;
    ProjectSiteStaffDTO projectSiteStaff;
}
