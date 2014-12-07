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
import com.com.boha.monitor.library.adapters.EngineerAdapter;
import com.com.boha.monitor.library.dto.EngineerDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;

import java.util.ArrayList;
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
public class EngineerListFragment extends Fragment implements AbsListView.OnItemClickListener, PageFragment {

    private EngineerListListener mListener;
    private AbsListView mListView;
    public EngineerListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_engineer_list, container, false);
        ctx = getActivity();
        Bundle b = getArguments();
        mListView = (AbsListView) view.findViewById(R.id.EC_list);
        txtCount = (TextView)view.findViewById(R.id.EC_count);
        txtName = (TextView)view.findViewById(R.id.EC_title);
        Statics.setRobotoFontLight(ctx, txtName);
        if (b != null) {
            ResponseDTO r = (ResponseDTO) b.getSerializable("response");
            engineerList = r.getCompany().getEngineerList();
            if (engineerList == null || engineerList.isEmpty()) {
                setEmptyText(ctx.getString(R.string.no_engineers));
                txtCount.setText("0");
                return view;
            } else {
                txtCount.setText("" + engineerList.size());
                setList();
            }
        }
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtCount,100,new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onNewEngineerRequested();
                    }
                });
            }
        });


        return view;
    }

    private void setList() {
        if (engineerList != null) {
            txtCount.setText("" + engineerList.size());
        } else {
            txtCount.setText("0");
        }
        // Set the adapter
        adapter = new EngineerAdapter(ctx,R.layout.engineer_item, engineerList, new EngineerAdapter.EngineerAdapterListener() {
            @Override
            public void onEngineerEditRequested(EngineerDTO e) {
                mListener.onEngineerEditRequested(e);
            }
        });
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(this);

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (EngineerListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement EngineerListListener");
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
            engineer = engineerList.get(position);
            mListener.onEngineerClicked(engineer);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the taskStatusList is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
//        View emptyView = mListView.getEmptyView();
//
//        if (emptyText instanceof TextView) {
//            ((TextView) emptyView).setText(emptyText);
//        }
    }

    @Override
    public void animateCounts() {
        Util.animateRotationY(txtCount,500);

    }

    public void addEngineer(EngineerDTO engineer) {
        if (engineerList == null) {
            engineerList = new ArrayList<>();
        }
        engineerList.add(engineer);
        //Collections.sort(engineerList);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            txtCount.setText("" + engineerList.size());
        } else {
           setList();
        }

        Util.animateRotationY(txtCount,500);

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow logoAnimator interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface EngineerListListener {
        public void onEngineerClicked(EngineerDTO engineer);
        public void onEngineerEditRequested(EngineerDTO engineer);
        public void onNewEngineerRequested();
    }

    EngineerDTO engineer;
    List<EngineerDTO> engineerList;
    EngineerAdapter adapter;
}
