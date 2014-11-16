package com.com.boha.monitor.library.dialogs;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.ProjectPopupAdapter;
import com.com.boha.monitor.library.dto.ProjectDTO;

import java.util.List;

/**
 * Add, update and delete company tasks
 * Created by aubreyM on 2014/10/18.
 */
public class ProjectPopupDialog extends DialogFragment {
    public interface ProjectPopupDialogListener {
        public void onProjectClicked(ProjectDTO project);
    }
    ProjectPopupDialogListener listener;
    Context context;
    ListView listView;
    String title;
    View view;
    List<ProjectDTO> projectList;
    static final String LOG = ProjectPopupDialog.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.project_popup_dialog, container);
        listView = (ListView) view.findViewById(R.id.PPD_list);
        ProjectPopupAdapter adapter = new ProjectPopupAdapter(context,R.layout.project_popup_item,projectList);
        listView.setAdapter(adapter);
        View v = inflater.inflate(R.layout.hero_image,null);
        listView.addHeaderView(v);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LOG,"#### onItemClick, position: " + position);
                if (position == 0) return;
                listener.onProjectClicked(projectList.get(position - 1));
                dismiss();
            }
        });

        getDialog().setTitle(context.getResources().getString(R.string.company_projects));
        return view;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public void setListener(ProjectPopupDialogListener listener) {
        this.listener = listener;
    }

    public void setProjectList(List<ProjectDTO> projectList) {
        this.projectList = projectList;
    }

}
