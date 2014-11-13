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
        if (b != null) {
            ResponseDTO r = (ResponseDTO) b.getSerializable("response");
            engineerList = r.getEngineerList();
            if (engineerList == null || engineerList.isEmpty()) {
                setEmptyText(ctx.getString(R.string.no_engineers));
            }
        }

        txtCount = (TextView)view.findViewById(R.id.EC_count);
        txtName = (TextView)view.findViewById(R.id.EC_title);

        Statics.setRobotoFontBold(ctx, txtName);
        txtCount.setText("" + engineerList.size());
        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.EC_list);
        adapter = new EngineerAdapter(ctx,R.layout.engineer_item, engineerList, new EngineerAdapter.EngineerAdapterListener() {
            @Override
            public void onEngineerEditRequested(EngineerDTO client) {

            }
        });
        mListView.setAdapter(adapter);

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

    public void addEngineer(EngineerDTO engineer) {
        if (engineerList == null) {
            engineerList = new ArrayList<>();
        }
        engineerList.add(engineer);
        //Collections.sort(engineerList);
        adapter.notifyDataSetChanged();
        txtCount.setText("" + engineerList.size());
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
    public interface EngineerListListener {
        public void onEngineerClicked(EngineerDTO engineer);
        public void onEngineerEditRequested(EngineerDTO engineer);
    }

    EngineerDTO engineer;
    List<EngineerDTO> engineerList;
    EngineerAdapter adapter;
}
