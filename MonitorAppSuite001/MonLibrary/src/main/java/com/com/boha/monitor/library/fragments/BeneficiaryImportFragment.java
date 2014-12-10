package com.com.boha.monitor.library.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.boha.monitor.library.R;
import com.com.boha.monitor.library.adapters.BeneficiaryImportAdapter;
import com.com.boha.monitor.library.dto.BeneficiaryDTO;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.transfer.RequestDTO;
import com.com.boha.monitor.library.dto.transfer.ResponseDTO;
import com.com.boha.monitor.library.util.ImportUtil;
import com.com.boha.monitor.library.util.SharedUtil;
import com.com.boha.monitor.library.util.Statics;
import com.com.boha.monitor.library.util.ToastUtil;
import com.com.boha.monitor.library.util.Util;
import com.com.boha.monitor.library.util.WebSocketUtil;
import com.com.boha.monitor.library.util.bean.ImportException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


public class BeneficiaryImportFragment extends Fragment implements PageFragment {
    @Override
    public void animateCounts() {

    }

    public interface ImportListener {
        public void onBeneficiariesImported(ProjectDTO project);
    }

    ImportListener listener;
    View view;
    Context ctx;
    TextView txtTitle, txtCount;
    Spinner fileSpinner;
    Button btnImport;
    ListView list;
    CheckBox chkAll;
    List<BeneficiaryDTO> beneficiaryList;
    List<File> files = new ArrayList<File>();
    ImageView image;
    ProgressBar progressBar;

    int index = 0, pageCnt = 0, totalPages = 0;
    static final int PAGE_SIZE = 1024;
    @Override
    public void onAttach(Activity a) {
        if (a instanceof ImportListener) {
            listener = (ImportListener)a;
        } else {
            throw new UnsupportedOperationException("Host activity " +
            a.getLocalClassName() + " must implement ImportListener");
        }
        super.onAttach(a);
    }

    public BeneficiaryImportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_import_beneficiary, container, false);
        ctx = getActivity();
        setFields();
        return view;
    }

    ProjectDTO project;

    public void setProject(ProjectDTO project) {
        this.project = project;
    }
    boolean hideDetail;
    View topLayout;
    ImageView imgMore;

    private void setFields() {
        progressBar = (ProgressBar)view.findViewById(R.id.IMP_progress);
        progressBar.setVisibility(View.GONE);
        fileSpinner = (Spinner) view.findViewById(R.id.IMP_fileSpinner);
        btnImport = (Button) view.findViewById(R.id.IMP_btnImport);
        txtCount = (TextView) view.findViewById(R.id.IMP_count);
        txtTotal = (TextView) view.findViewById(R.id.IMP_money);
        txtTitle = (TextView) view.findViewById(R.id.IMP_countLabel);
        image = (ImageView) view.findViewById(R.id.IMP_image);
        imgMore = (ImageView) view.findViewById(R.id.IMP_more);
        list = (ListView) view.findViewById(R.id.IMP_list);
        topLayout = view.findViewById(R.id.IMP_topLayout);

        Statics.setRobotoFontLight(ctx, txtTotal);
        Statics.setRobotoFontLight(ctx, txtTitle);

        imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topLayout.getVisibility() == View.GONE) {
                    Util.expand(topLayout, 1000, null);
                } else {
                    Util.collapse(topLayout, 1000, null);
                }
            }
        });
        txtTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDetail = !hideDetail;
                setList(hideDetail);

            }
        });
        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideDetail = !hideDetail;
                setList(hideDetail);
            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (beneficiaryList == null) {
                    Util.showErrorToast(ctx,
                            ctx.getString(R.string.import_not_found));
                    return;
                }
                totalPages = beneficiaryList.size() / PAGE_SIZE;
                int rem = beneficiaryList.size() % PAGE_SIZE;
                if (rem > 0) {
                    totalPages++;
                }
                controlImport();
            }
        });


        files = ImportUtil.getImportFilesOnSD();
        files.addAll(ImportUtil.getImportFiles());

        setSpinner();
        Log.w(LOG, "++++++++ Import files found: " + files.size());
    }

    private void parseFile(File file) throws IOException {
        beneficiaryList = new ArrayList<BeneficiaryDTO>();

        BufferedReader brReadMe = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), "UTF-8"));
        String strLine = brReadMe.readLine();
        while (strLine != null) {
            BeneficiaryDTO dto = null;
            try {
                dto = parseLine(strLine);
                if (dto != null) {
                    beneficiaryList.add(dto);
                    //Log.e(LOG, "####### Beneficiary added to list from import_pic file");
                }
                strLine = brReadMe.readLine();
            } catch (ImportException e) {
                e.printStackTrace();
            }

        }

        brReadMe.close();
        setList(false);
        Log.e(LOG, "####### Beneficiary list has been imported into app, found: " + beneficiaryList.size());
    }

    public BeneficiaryDTO parseLine(String line) throws ImportException {
        Pattern patt = Pattern.compile(";");
        if (line.indexOf(",") > -1) {
            //patt = Pattern.compile(",");
        }
        String[] result = patt.split(line);
        BeneficiaryDTO dto = new BeneficiaryDTO();
        try {
            if (result[1] != null) {
                if (result[1].equalsIgnoreCase("Surname")) {
                    return null;
                }
            }

            if (result[0] != null) {
                dto.setIDNumber(result[0]);
            }

            if (result[1] != null) {
                dto.setLastName(result[1]);
            }
            if (result[2] != null) {
                dto.setFirstName(result[2]);
            }
            if (result[3] != null) {
                dto.setStatus(result[3]);
            }
            if (result[4] != null) {
                int i = result[4].indexOf("R");
                if (i > -1) {
                    String amt = result[4].substring(1);
                    dto.setAmountAuthorized(Double.parseDouble(amt));
                } else {
                    String amt = result[4];
                    int x = amt.indexOf(",");
                    if (x > -1) {
                        String pre = amt.substring(0, x);
                        String pos = amt.substring(x + 1);
                        amt = pre + "." + pos;
                    }
                    dto.setAmountAuthorized(Double.parseDouble(amt));
                }
            }
            if (result[5] != null) {
                dto.setPhbDate(getDate(result[5]));
            }
            if (result[6] != null) {
                dto.setSiteNumber(result[6]);
            }
            if (result[7] != null) {
                dto.setTownshipName(result[7]);
            }
        } catch (Exception e) {
            Log.e(LOG, "---- ERROR parse failed", e);
            return null;
        }

//        Log.e(LOG,
//                "Found beneficiary: " + dto.getFirstName() + " "
//                        + dto.getLastName() + " to import into Monitor");
        return dto;
    }

    private Date getDate(String dt) {
        String y = dt.substring(0, 2);
        String m = dt.substring(3, 5);
        String d = dt.substring(6);
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(y) + 2000);
        cal.set(Calendar.MONTH, Integer.parseInt(m) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        return cal.getTime();
    }

    boolean isDone;

    private void controlImport() {
        btnImport.setEnabled(false);

        if (pageCnt < totalPages || pageCnt == totalPages) {
            List<BeneficiaryDTO> list = new ArrayList<>();
            for (int i = 0; i < PAGE_SIZE; i++) {
                try {
                    list.add(beneficiaryList.get(index));
                    index++;
                } catch (Exception e) {
                }
            }
            if (!list.isEmpty()) {
                sendData(list);
            }else {
                weIsDone();
            }

        } else {
            weIsDone();
        }


    }

    private void weIsDone() {
        Util.showToast(ctx, ctx.getResources().getString(R.string.import_ok));
        listener.onBeneficiariesImported(project);
    }
    private void sendData(List<BeneficiaryDTO> list) {
        Log.e(LOG, "### totalPages: " + totalPages + " pageCnt: " + pageCnt + " list: " + list.size());
        RequestDTO w = new RequestDTO();
        w.setRequestType(RequestDTO.IMPORT_BENEFICIARIES);
        w.setBeneficiaryList(list);

        progressBar.setVisibility(View.VISIBLE);
        WebSocketUtil.sendRequest(ctx,Statics.COMPANY_ENDPOINT,w,new WebSocketUtil.WebSocketListener() {
            @Override
            public void onMessage(final ResponseDTO response) {

                Log.e(LOG,"+++++ 1 page of beneficiaries sent OK.., " +
                        "status: " + response.getStatusCode() + " " + response.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        if (response.getStatusCode() == 0) {
                            pageCnt++;
                            beneficiaryList = response.getBeneficiaryList();
                            project.setBeneficiaryList(beneficiaryList);
                            controlImport();
                        }
                    }
                });

            }

            @Override
            public void onClose() {
                Log.w(LOG, "------ websocket onClose");
            }

            @Override
            public void onError(final String message) {
                Log.e(LOG, "---- ERROR websocket - " + message);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showErrorToast(ctx,message);
                    }
                });
            }
        });

    }

    private void setList(boolean hideDetail) {
        adapter = new BeneficiaryImportAdapter(ctx, R.layout.beneficiary_item, beneficiaryList, hideDetail);
        list.setAdapter(adapter);
        txtCount.setText("" + beneficiaryList.size());
        double tot = 0;
        for (BeneficiaryDTO b : beneficiaryList) {
            b.setProjectID(project.getProjectID());
            CompanyDTO c = SharedUtil.getCompany(ctx);
            CompanyDTO cc = new CompanyDTO();
            cc.setCompanyID(c.getCompanyID());
            b.setCompany(cc);
            if (b.getAmountAuthorized() != null) {
                tot += b.getAmountAuthorized().doubleValue();
            }
        }
        txtTotal.setText(df.format(tot));
    }

    TextView txtTotal;
    BeneficiaryImportAdapter adapter;
    static final String LOG = BeneficiaryImportFragment.class.getSimpleName();
    static final Locale loc = Locale.getDefault();
    static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm", loc);
    static final DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,###,###,###.00");

    File selectedFile;

    private void setSpinner() {

        List<String> list = new ArrayList<String>();
        list.add(ctx.getString(R.string.select_file));
        for (File p : files) {
            list.add(p.getName() + " - " + sdf.format(new Date(p.lastModified())));
        }
        ArrayAdapter a = new ArrayAdapter(ctx, android.R.layout.simple_spinner_item, list);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fileSpinner.setAdapter(a);
        fileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    selectedFile = null;
                    return;
                }
                try {
                    parseFile(files.get(i - 1));
                } catch (IOException e) {
                    Util.showErrorToast(ctx, ctx.getResources().getString(R.string.failed_import));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}
