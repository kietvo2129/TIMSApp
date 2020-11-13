package com.example.timsapp.ui.home.Manufacturing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.timsapp.AlerError.AlerError;
import com.example.timsapp.BaseApp;
import com.example.timsapp.R;
import com.example.timsapp.Url;
import com.example.timsapp.ui.home.ActualWO.HomeFragment;
import com.example.timsapp.ui.home.Composite.CompositeActivity;
import com.example.timsapp.ui.home.Composite.WorkerActivity;
import com.example.timsapp.ui.home.Manufacturing.Worker.WorkerAddJobActivity;
import com.example.timsapp.ui.slideshow.shipk.ShipFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class ManufacturingActivity extends AppCompatActivity {
    //String webUrl = BaseApp.isHostting();
    ArrayList<ActualWOMaster> actualWOMasterArrayList;
    ArrayList<ActualWOdetailMaster> actualWOdetailMasterArrayList;
    ActualdetailAdapter actualdetailAdapter;
    ActualWOAdapter adapter;
    ListView theListView;
    RecyclerView recyclerView;
    View viewdetail;
    int page = 1;
    TextView totaldetail, content_request_btn;
    private ProgressDialog dialog;
    private int vitribam = -1;
    int total = -1;
    public static String id_actual = "";
    public static String qc_code = "";
    public static String RollCode = "";
    public static String style_no = "";
    public static String process_nm = "";
    String at_no = HomeFragment.at_no;
    ImageView im_delete;
    private ArrayAdapter<String> adapterProcess , adapterRolls;
    private ArrayList<ItemProcess> listItemProcess;
    private ArrayList<ItemProcess> listItemRolls;
    private String pono;
    private String Product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manufacturing);
        pono = getIntent().getStringExtra("PoNo");
//        intent.putExtra("Product",actualWOMasterArrayList.get(position).getProduct());
        Product = getIntent().getStringExtra("Product");

        setTitle(pono != null ? pono : "Manufactory");

        theListView = findViewById(R.id.mainListView);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_pp_create();
            }
        });

        dialog = new ProgressDialog(this, R.style.AlertDialogCustom);
        getData(page);
    }

    private void open_pp_create() {

        final Dialog dialog = new Dialog(ManufacturingActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(ManufacturingActivity.this).inflate(R.layout.popup_create_secon, null);
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });


        Spinner ProcessName = dialog.findViewById(R.id.ProcessName);
        Spinner Roll = dialog.findViewById(R.id.Roll);


        getTimsProcess(ProcessName);
        getTimsRolls(Roll);
//
//        List<String> Process = new ArrayList<>();
//        Process.add("* Select Process *");
//        Process.add("Ngoại Quan");
//        Process.add("Độ Dày");
////        Process.add("OQC");
//        List<String> Rollq = new ArrayList<>();
//        Rollq.add("* Select Roll *");
//        Rollq.add("Roll Normal");
//        Rollq.add("Roll (Z)");
//        Rollq.add("Sheet");//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item,
//                Process);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        ProcessName.setAdapter(adapter);
//        ArrayAdapter<String> adapterroll = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item,
//                Rollq);
//        adapterroll.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        Roll.setAdapter(adapterroll);

        final String[] pross = {""};
        final String[] roll = {""};

        ProcessName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pross[0] = listItemProcess.get(position).getProcessCode();
//                switch (position) {
//                    case 0:
//                        pross[0] = "";
//                        break;
//                    case 1:
//                        pross[0] = "NQ";
//                        break;
//
//                    case 2:
//                        pross[0] = "DD";
//                        break;
//
//                    case 3:
//                        pross[0] = "OQC";
//                        break;
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Roll.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                roll[0] = listItemRolls.get(position).getProcessCode();
//                switch (position) {
//                    case 0:
//                        roll[0] = "";
//                        break;
//                    case 1:
//                        roll[0] = "100";
//                        break;
//
//                    case 2:
//                        roll[0] = "200";
//                        break;
//
//                    case 3:
//                        roll[0] = "300";
//                        break;
//
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pross[0] == "") {
                    AlerError.Baoloi("Please chose Process Name", ManufacturingActivity.this);
                } else if (roll[0] == "") {
                    AlerError.Baoloi("Please chose Roll", ManufacturingActivity.this);
                } else {
                    dialog.dismiss();
                    new create().execute(BaseApp.isHostting() + "TIMS/Add_w_actual?style_no=" + HomeFragment.product + "&at_no=" + HomeFragment.at_no + "&name=" + pross[0]
                            + "&roll=" + roll[0]);
                    Log.e("create", BaseApp.isHostting() + "TIMS/Add_w_actual?style_no=" + HomeFragment.product + "&at_no=" + HomeFragment.at_no + "&name=" + pross[0]
                            + "&roll=" + roll[0]);
                }
            }
        });
        dialog.show();
    }

    private void getTimsRolls(final Spinner spRoll) {
        String url = BaseApp.isHostting() + "/TIMS/GetTIMSRolls";
        Log.d("getTimsRolls", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getTimsRolls", response);
                listItemRolls = new ArrayList<ItemProcess>();
                listItemRolls.add(new ItemProcess(
                        "",
                        "*Select Rolls*"));
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        Toast.makeText(ManufacturingActivity.this, "No Rolls", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                                listItemRolls.add(new ItemProcess(
                                        object.getString("Code"),
                                        object.getString("Name")
                                ));
                        }

                        adapterRolls = new ArrayAdapter(ManufacturingActivity.this,
                                android.R.layout.simple_spinner_item,
                                listItemRolls);
                        adapterRolls.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spRoll.setAdapter(adapterRolls);
                    }
                } catch (JSONException e) {
                    AlerError.Baoloi("The json error:" + e.toString(), ManufacturingActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AlerError.Baoloi("The server error:" + error.toString(), ManufacturingActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ManufacturingActivity.this);
        requestQueue.add(stringRequest);
    }

    private void getTimsProcess(final Spinner spProcess) {
        String url = BaseApp.isHostting() + "/TIMS/GetTIMSProcesses";
        Log.d("getTimsProcess", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getTimsProcess", response);
                listItemProcess = new ArrayList<ItemProcess>();
                listItemProcess.add(new ItemProcess(
                        "",
                        "*Select Process*"));
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        Toast.makeText(ManufacturingActivity.this, "No process", Toast.LENGTH_SHORT).show();
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            if (object.getString("ProcessCode").trim().equals("ROT") ||
                                    object.getString("ProcessCode").trim().equals("STA")) {
                            } else {
                                listItemProcess.add(new ItemProcess(
                                        object.getString("ProcessCode"),
                                        object.getString("ProcessName")
                                ));
                            }
                        }

                        adapterProcess = new ArrayAdapter(ManufacturingActivity.this,
                                android.R.layout.simple_spinner_item,
                                listItemProcess);
                        adapterProcess.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spProcess.setAdapter(adapterProcess);
                    }

                } catch (JSONException e) {

                    AlerError.Baoloi("The json error:" + e.toString(), ManufacturingActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AlerError.Baoloi("The server error:" + error.toString(), ManufacturingActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ManufacturingActivity.this);
        requestQueue.add(stringRequest);
    }

    private class create extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("result")) {
                    Toast.makeText(ManufacturingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    getData(1);
                } else {
                    AlerError.Baoloi(jsonObject.getString("kq"), ManufacturingActivity.this);
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ManufacturingActivity.this);
                dialog.dismiss();
            }
        }
    }

    private void getData(int page) {
        new getData().execute(BaseApp.isHostting() + "TIMS/GetTIMSActualInfo?rows=50&page=" + page + "&sidx=&sord=asc&at_no=" + at_no);
        Log.e("getData", BaseApp.isHostting() + "TIMS/GetTIMSActualInfo?rows=50&page=" + page + "&sidx=&sord=asc&at_no=" + at_no);
    }

    private void getaddData(int page) {
        new getaddData().execute(BaseApp.isHostting() + "TIMS/GetTIMSActualInfo?rows=50&page=" + page + "&sidx=&sord=asc&at_no=" + at_no);
        Log.e("getaddData", BaseApp.isHostting() + "TIMS/GetTIMSActualInfo?rows=50&page=" + page + "&sidx=&sord=asc&at_no=" + at_no);

    }

    private class getData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            actualWOMasterArrayList = new ArrayList<>();
            String Id, Type, Name, Date, Name_View, QCCode,
                    QCName, RollCode, RollName;
            int Defective, ActualQty;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    //AlerError.Baoloi("No data", ManufacturingActivity.this);
                    theListView.setVisibility(View.GONE);
                    return;
                }

                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    Id = objectRow.getString("Id");
                    Type = objectRow.getString("Type");
                    Defective = objectRow.getInt("Defective");
                    Name = objectRow.getString("Name");
                    ActualQty = objectRow.getInt("ActualQty");
                    Date = objectRow.getString("Date");
                    Name_View = objectRow.getString("Name_View");
                    QCCode = objectRow.getString("QCCode");
                    QCName = objectRow.getString("QCName").replace("null", "");
                    RollCode = objectRow.getString("RollCode");
                    RollName = objectRow.getString("RollName");
                    actualWOMasterArrayList.add(new ActualWOMaster(Id, Type, Name, Date, Name_View, QCCode,
                            QCName, RollCode, RollName, Defective, ActualQty));
                }
                dialog.dismiss();
                theListView.setVisibility(View.VISIBLE);
                setListView();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ManufacturingActivity.this);
                dialog.dismiss();
            }
        }

    }

    private class getaddData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String Id, Type, Name, Date, Name_View, QCCode,
                    QCName, RollCode, RollName;
            int Defective, ActualQty;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", ManufacturingActivity.this);
                    return;
                }

                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    Id = objectRow.getString("Id");
                    Type = objectRow.getString("Type");
                    Defective = objectRow.getInt("Defective");
                    Name = objectRow.getString("Name");
                    ActualQty = objectRow.getInt("ActualQty");
                    Date = objectRow.getString("Date");
                    Name_View = objectRow.getString("Name_View");
                    QCCode = objectRow.getString("QCCode");
                    QCName = objectRow.getString("QCName").replace("null", "");
                    RollCode = objectRow.getString("RollCode");
                    RollName = objectRow.getString("RollName");
                    actualWOMasterArrayList.add(new ActualWOMaster(Id, Type, Name, Date, Name_View, QCCode,
                            QCName, RollCode, RollName, Defective, ActualQty));
                }
                dialog.dismiss();
                //setListView(total);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ManufacturingActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void setListView() {

        adapter = new ActualWOAdapter(ManufacturingActivity.this, actualWOMasterArrayList);

        theListView.setAdapter(adapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                // toggle clicked cell state

                recyclerView = view.findViewById(R.id.recycview);
                totaldetail = view.findViewById(R.id.totaldetail);
                im_delete = view.findViewById(R.id.im_delete);
                viewdetail = view;
                vitribam = pos;
                content_request_btn = view.findViewById(R.id.content_request_btn);
                content_request_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ManufacturingActivity.this, CompositeActivity.class);
                        id_actual = actualWOMasterArrayList.get(pos).getId();
                        RollCode = actualWOMasterArrayList.get(pos).RollCode;
                        qc_code = actualWOMasterArrayList.get(pos).QCCode;
                        process_nm = actualWOMasterArrayList.get(pos).getName();
//                        style_name = actualWOMasterArrayList.get(pos).name;
                        startActivity(intent);
                    }
                });

                //loaddatadetail(pos);

                // Goi activity Worker Add Job
                Intent intent = new Intent(ManufacturingActivity.this, WorkerAddJobActivity.class);
                intent.putExtra("pono",pono);
                intent.putExtra("id_actual",actualWOMasterArrayList.get(pos).getId());
                intent.putExtra("Type",actualWOMasterArrayList.get(pos).getName());
                intent.putExtra("RollName",actualWOMasterArrayList.get(pos).getRollName());
                intent.putExtra("QCCode",actualWOMasterArrayList.get(pos).getQCCode());
                intent.putExtra("Product",Product);

                startActivity(intent);

                im_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(ManufacturingActivity.this, R.style.AlertDialogCustom);
                        alertDialog.setCancelable(false);
                        alertDialog.setTitle("Warning!!!");
                        alertDialog.setMessage("Are you sure Delete?");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new onDelete().execute(BaseApp.isHostting() + "TIMS/xoa_wactual_con?id=" + actualWOMasterArrayList.get(pos).Id);
                                Log.e("onDelete", BaseApp.isHostting() + "TIMS/xoa_wactual_con?id=" + actualWOMasterArrayList.get(pos).Id);
                            }
                        });
                        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        alertDialog.show();

                    }
                });
            }
        });

        theListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (page < total) {
                        total = -1;
                        getaddData(page + 1);

                    }
                }
            }
        });


    }

    private class onDelete extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("result")) {
                    Toast.makeText(ManufacturingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                } else {
                    AlerError.Baoloi(jsonObject.getString("kq"), ManufacturingActivity.this);
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ManufacturingActivity.this);
                dialog.dismiss();
            }
        }

    }

//    private void loaddatadetail(int pos) {
//        new loaddatadetail().execute(BaseApp.isHostting() + "TIMS/GetTIMSActualDetail?id=" +
//                actualWOMasterArrayList.get(pos).Id +
//                "&_search=false&nd=1602733203479&rows=10&page=1&sidx=&sord=asc");
//        Log.e("loaddatadetail", BaseApp.isHostting() + "TIMS/GetTIMSActualDetail?id=" +
//                actualWOMasterArrayList.get(pos).Id +
//                "&_search=false&nd=1602733203479&rows=10&page=1&sidx=&sord=asc");
//    }

    private void loaddatadetail(int pos) {
        String url = BaseApp.isHostting() +
                "TIMS/GetTIMSListStaff?page=1&rows=50&sidx=&sord=asc&_search=false&id_actual=" +
                actualWOMasterArrayList.get(pos).Id;
        new loaddatadetail().execute(url) ;
        Log.e("loaddatadetail", url);
    }

    private class loaddatadetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            actualWOdetailMasterArrayList = new ArrayList<>();
            String no, Id, MaterialCode;
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    setDetail();
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {

//                    JSONObject objectRow = jsonArray.getJSONObject(i);
//                    no = i + 1 + "";
//                    Id = objectRow.getString("Id");
//                    MaterialCode = objectRow.getString("MaterialCode").replace("null", "");
//                    actualWOdetailMasterArrayList.add(new ActualWOdetailMaster(no, Id, MaterialCode));
                }
                setDetail();
            } catch (JSONException e) {
                dialog.dismiss();
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ManufacturingActivity.this);
            }
        }

    }

    private void setDetail() {

        dialog.dismiss();
        RecyclerView.LayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ManufacturingActivity.this);
        actualdetailAdapter = new ActualdetailAdapter(actualWOdetailMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(actualdetailAdapter);
        totaldetail.setText(actualWOdetailMasterArrayList.size() + " ML No");
        ((FoldingCell) viewdetail).toggle(true);
        for (int i = 0; i < actualWOMasterArrayList.size(); i++) {
            if (i != vitribam) {
                adapter.registerFold(i);
            } else {
                adapter.registerToggle(vitribam);
            }
        }

    }

    private class ItemProcess {

        String processCode;
        String processName;

        public ItemProcess(String processCode, String processName) {
            this.processCode = processCode;
            this.processName = processName;
        }

        public String getProcessCode() {
            return processCode;
        }

        public String getProcessName() {
            return processName;
        }

        @Override
        public String toString() {
            return processName;
        }
    }
}