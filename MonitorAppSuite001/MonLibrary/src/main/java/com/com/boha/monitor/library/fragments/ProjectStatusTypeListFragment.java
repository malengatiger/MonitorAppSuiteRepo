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
import android.widget.ListAdapter;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.ProjectStatusTypeAdapter;
import com.com.boha.monitor.library.dto.ProjectStatusTypeDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
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
public class ProjectStatusTypeListFragment extends Fragment implements AbsListView.OnItemClickListener, PageFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProjectStatusTypeListListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static ProjectStatusTypeListFragment newInstance(String param1, String param2) {
        ProjectStatusTypeListFragment fragment = new ProjectStatusTypeListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProjectStatusTypeListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

           }

    Context ctx;
    TextView txtCount, txtName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_status_type_list, container, false);
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            ResponseDTO r = (ResponseDTO) b.getSerializable("response");
            projectStatusTypeList = r.getCompany().getProjectStatusTypeList();
        }

        txtCount = (TextView)view.findViewById(R.id.FTST_count);
        txtName = (TextView)view.findViewById(R.id.FTST_title);

        Statics.setRobotoFontBold(ctx, txtName);
        txtCount.setText("" + projectStatusTypeList.size());
        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.FTST_list);
        adapter = new ProjectStatusTypeAdapter(ctx, R.layout.project_status_type_item, projectStatusTypeList);
        mListView.setAdapter(adapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.toast(ctx,"Under Construction");
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProjectStatusTypeListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement TaskStatusListListener");
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
            projectStatusType = projectStatusTypeList.get(position);
            mListener.onProjectStatusTypeClicked(projectStatusType);
        }
    }
    ProjectStatusTypeDTO projectStatusType;

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

    public void addProjectStatusType(ProjectStatusTypeDTO status) {
        if (projectStatusTypeList == null) {
            projectStatusTypeList = new ArrayList<>();
        }
        projectStatusTypeList.add(status);
        Collections.sort(projectStatusTypeList);
        adapter.notifyDataSetChanged();
        txtCount.setText("" + projectStatusTypeList.size());
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
     * <project/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface ProjectStatusTypeListListener {
        public void onProjectStatusTypeClicked(ProjectStatusTypeDTO statusType);
    }

    List<ProjectStatusTypeDTO> projectStatusTypeList;
    ProjectStatusTypeAdapter adapter;
}
