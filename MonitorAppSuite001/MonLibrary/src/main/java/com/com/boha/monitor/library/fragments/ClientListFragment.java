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
import com.com.boha.monitor.library.adapters.ClientAdapter;
import com.com.boha.monitor.library.dto.ClientDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.Util;

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
public class ClientListFragment extends Fragment implements AbsListView.OnItemClickListener, PageFragment {

    private ClientListListener mListener;
    private AbsListView mListView;
    public ClientListFragment() {
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
         view = inflater.inflate(R.layout.fragment_client_list, container, false);
        ctx = getActivity();
        Bundle b = getArguments();
        if (b != null) {
            ResponseDTO r = (ResponseDTO) b.getSerializable("response");
            clientList = r.getCompany().getClientList();
        }

        txtCount = (TextView)view.findViewById(R.id.FC_count);
        txtName = (TextView)view.findViewById(R.id.FC_title);

        Statics.setRobotoFontLight(ctx, txtName);
        setList();
        return view;
    }

    private void setList() {
        txtCount.setText("" + clientList.size());
        // Set the adapter
        mListView = (AbsListView) view.findViewById(R.id.FC_list);
        adapter = new ClientAdapter(ctx, R.layout.client_item,
                clientList, new ClientAdapter.ClientAdapterListener() {
            @Override
            public void onClientEditRequested(ClientDTO client) {
                mListener.onClientEditRequested(client);
            }
        });
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(this);
        txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(txtCount,100,new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {
                        mListener.onNewClientRequested();
                    }
                });
            }
        });
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ClientListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Host " + activity.getLocalClassName()
                    + " must implement ClientListListener");
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
            client = clientList.get(position);
            mListener.onClientClicked(client);
        }
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
        Util.animateRotationY(txtCount,500);

    }

    public void addClient(ClientDTO client) {
        if (clientList == null) {
            clientList = new ArrayList<>();
        }
        clientList.add(client);
        Collections.sort(clientList);
        adapter.notifyDataSetChanged();
        txtCount.setText("" + clientList.size());
        try {
            Thread.sleep(1000);
            Util.animateRotationY(txtCount,500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow logoAnimator interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface ClientListListener {
        public void onClientClicked(ClientDTO client);
        public void onClientEditRequested(ClientDTO client);
        public void onNewClientRequested();
    }

    ClientDTO client;
    List<ClientDTO> clientList;
    ClientAdapter adapter;
}
