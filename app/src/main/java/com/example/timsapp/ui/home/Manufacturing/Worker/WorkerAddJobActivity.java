package com.example.timsapp.ui.home.Manufacturing.Worker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.timsapp.ui.home.Composite.ItemStaffAdapter;
import com.example.timsapp.ui.home.Composite.VitridungmayMaster;
import com.example.timsapp.ui.home.Manufacturing.Composite.CompositeCheckEAActivity;
import com.example.timsapp.ui.home.Manufacturing.Composite.CompositeOqcActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WorkerAddJobActivity extends AppCompatActivity {
    private RecyclerView recyclerViewAddJob;
    private ProgressDialog progressDialog;
    private ArrayList<ItemWoker> listWorker;
    private AdapterItemWorker adapterworker;
    private TextView nodata;
    private FloatingActionButton fabAddJob, fabAddJob_input, fabAddJob_scan, fabAddJob_select;
    private ItemStaffAdapter itemStaffAdapter;
    //    private ArrayList<ItemStaffMaster> itemStaffMasterArrayList;
//    String mc_no = "", use_unuse = "";
//    private String code_vitridungmay = "";
    private String id_actual;
    int page = 1;
    int total = -1;

    ArrayList<VitridungmayMaster> vitridungmayMasterArrayList;
    private RecyclerView rvView;
    private ListDetailAdapter detailadapter;
    private ArrayList<ListDetail> listDetail;
    private String type;
    private String RollName;
    private String staff_id;
    private String QCCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_add_job);
        setTitle("Worker");

        id_actual = getIntent().getStringExtra("id_actual");
        type = getIntent().getStringExtra("Type");
        RollName = getIntent().getStringExtra("RollName");
        QCCode = getIntent().getStringExtra("QCCode");


        recyclerViewAddJob = findViewById(R.id.recyclerViewAddJob);
        fabAddJob = findViewById(R.id.fabAddJob);
        fabAddJob_input = findViewById(R.id.fabAddJob_input);
        fabAddJob_scan = findViewById(R.id.fabAddJob_scan);
        fabAddJob_select = findViewById(R.id.fabAddJob_select);
        nodata = findViewById(R.id.nodata);
        progressDialog = new ProgressDialog(this, R.style.AlertDialogCustom);
        hidefab();
//        loadWordker(id_actual);

        fabAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabAddJob_input.getVisibility() == View.GONE) {
                    showfab();
                } else {
                    hidefab();
                }
            }
        });
        fabAddJob_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWorker();
                hidefab();
            }
        });
        fabAddJob_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
                hidefab();
            }
        });
        fabAddJob_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calllist();
                hidefab();
            }
        });
    }

    private void calllist() {
        Intent intent = new Intent(WorkerAddJobActivity.this, ListWorkerActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadWordker(id_actual);
    }

//    private void setListView() {
//
//        adapter = new ActualWOAdapter(ManufacturingActivity.this, actualWOMasterArrayList);
//
//        theListView.setAdapter(adapter);
//
//        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
//                // toggle clicked cell state
//
//                recyclerView = view.findViewById(R.id.recycview);
//                totaldetail = view.findViewById(R.id.totaldetail);
//                im_delete = view.findViewById(R.id.im_delete);
//                viewdetail = view;
//                vitribam = pos;
//                content_request_btn = view.findViewById(R.id.content_request_btn);
//                content_request_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(ManufacturingActivity.this, CompositeActivity.class);
//                        id_actual = actualWOMasterArrayList.get(pos).getId();
//                        RollCode = actualWOMasterArrayList.get(pos).RollCode;
//                        qc_code = actualWOMasterArrayList.get(pos).QCCode;
//                        process_nm = actualWOMasterArrayList.get(pos).getName();
////                        style_name = actualWOMasterArrayList.get(pos).name;
//                        startActivity(intent);
//                    }
//                });
//
////                //
////                Intent intent = new Intent(ManufacturingActivity.this, WorkerActivity.class);
//////                id_actual = actualWOMasterArrayList.get(pos).getId();
//////                RollCode = actualWOMasterArrayList.get(pos).RollCode;
//////                qc_code = actualWOMasterArrayList.get(pos).QCCode;
//////                process_nm = actualWOMasterArrayList.get(pos).getName();
//////                        style_name = actualWOMasterArrayList.get(pos).name;
////                startActivity(intent);
//                //loaddatadetail(pos);
//
//                // Goi activity Worker Add Job
//                Intent intent = new Intent(ManufacturingActivity.this, WorkerAddJobActivity.class);
//                intent.putExtra("id_actual",actualWOMasterArrayList.get(pos).getId());
//                startActivity(intent);
//
//                im_delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(ManufacturingActivity.this, R.style.AlertDialogCustom);
//                        alertDialog.setCancelable(false);
//                        alertDialog.setTitle("Warning!!!");
//                        alertDialog.setMessage("Are you sure Delete?");
//                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                new ManufacturingActivity.onDelete().execute(BaseApp.isHostting() + "TIMS/xoa_wactual_con?id=" + actualWOMasterArrayList.get(pos).Id);
//                                Log.e("onDelete", BaseApp.isHostting() + "TIMS/xoa_wactual_con?id=" + actualWOMasterArrayList.get(pos).Id);
//                            }
//                        });
//                        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                            }
//                        });
//                        alertDialog.show();
//
//                    }
//                });
//            }
//        });
//
//        theListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
//                    if (page < total) {
//                        total = -1;
//                        getaddData(page + 1);
//
//                    }
//                }
//            }
//        });
//
//
//    }


    private void inputData() {
        final Dialog dialog = new Dialog(WorkerAddJobActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(WorkerAddJobActivity.this).inflate(R.layout.popup_input, null);
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        final EditText Containercode = dialog.findViewById(R.id.Containercode);
        final Button confirm = dialog.findViewById(R.id.confirm);
        final TextInputLayout h2;

        h2 = dialog.findViewById(R.id.H2);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Containercode.getText().toString() == null || Containercode.getText().toString().length() == 0) {
                    h2.setError("Please, Input here.");
                    return;
                } else {
                    h2.setError(null);
                    findscanWorker(Containercode.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void scanWorker() {
        new IntentIntegrator(this).setOrientationLocked(false).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(WorkerAddJobActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                findscanWorker(result.getContents());
            }

        }
    }

    private void findscanWorker(String contents) {
        ///TIMS/GetWorkers?page=1&rows=50&sidx=&sord=asc&_search=false&userId=&userName=&positionCode=
        //String url = BaseApp.isHostting() + "ActualWO/search_staff_pp?page=" + 1 + "&rows=50&sidx=&sord=asc&userid=" + contents +
        //        "&md_nm=&_search=false";

        // Call activity list worker
        Intent intent = new Intent(WorkerAddJobActivity.this, ListWorkerActivity.class);
        intent.putExtra("userId", contents);
        intent.putExtra("id_actual", id_actual);
        startActivity(intent);
        //finish();

//        String url = BaseApp.isHostting() + "/TIMS/GetWorkers?page=1&rows=50&sidx=&sord=asc&_search=false&userId=&userName=&positionCode=";
//        new findWorker().execute(url);
//        Log.e("findscanWorker", url);
    }

//    private class findWorker extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            return NoiDung_Tu_URL(strings[0]);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(true);
//            progressDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            itemStaffMasterArrayList = new ArrayList<>();
//            String position_cd, userid, RowNum, uname, nick_name, mc_no;
//            try {
//
//                JSONObject jsonObject = new JSONObject(s);
//                JSONArray jsonArray = jsonObject.getJSONArray("rows");
//
//                if (jsonArray.length() == 0) {
//                    progressDialog.dismiss();
//                    AlerError.Baoloi("No data", WorkerAddJobActivity.this);
//                    return;
//                }
//                total = jsonObject.getInt("total");
//                page = jsonObject.getInt("page");
//                for (int i = 0; i < jsonArray.length(); i++) {
//
//                    JSONObject objectRow = jsonArray.getJSONObject(i);
////                    position_cd = objectRow.getString("position_cd").replace("null", "");
////                    RowNum = objectRow.getString("RowNum").replace("null", "");
////                    userid = objectRow.getString("userid").replace("null", "");
////                    uname = objectRow.getString("uname").replace("null", "");
////                    nick_name = objectRow.getString("nick_name").replace("null", "");
////                    mc_no = objectRow.getString("mc_no").replace("null", "");
//
//                    position_cd = objectRow.getString("PositionName").replace("null", "");
//                    RowNum = objectRow.getString("RowNum").replace("null", "");
//                    userid = objectRow.getString("UserId").replace("null", "");
//                    uname = objectRow.getString("UserName").replace("null", "");
//                    nick_name = objectRow.getString("nick_name").replace("null", "");
//                    mc_no = objectRow.getString("mc_no").replace("null", "");
//
//                    itemStaffMasterArrayList.add(new ItemStaffMaster(i + 1 + "", position_cd, userid, RowNum, uname, nick_name, mc_no));
//                }
//                progressDialog.dismiss();
//                buildrecyc();
//            } catch (JSONException e) {
//                e.printStackTrace();
//                AlerError.Baoloi("Could not connect to server", WorkerAddJobActivity.this);
//                progressDialog.dismiss();
//            }
//        }
//
//    }

//    private void getaddData(int page) {
//        new getaddData().execute(BaseApp.isHostting() + "ActualWO/search_staff_pp?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");
//    }

//    private class getaddData extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            return NoiDung_Tu_URL(strings[0]);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(true);
//            progressDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            String position_cd, userid, RowNum, uname, nick_name, mc_no;
//            try {
//
//                JSONObject jsonObject = new JSONObject(s);
//                JSONArray jsonArray = jsonObject.getJSONArray("rows");
//
//                if (jsonArray.length() == 0) {
//                    progressDialog.dismiss();
//                    AlerError.Baoloi("No data", WorkerAddJobActivity.this);
//                    return;
//                }
//                total = jsonObject.getInt("total");
//                page = jsonObject.getInt("page");
//                for (int i = 0; i < jsonArray.length(); i++) {
//
//                    JSONObject objectRow = jsonArray.getJSONObject(i);
//                    position_cd = objectRow.getString("position_cd").replace("null", "");
//                    RowNum = objectRow.getString("RowNum").replace("null", "");
//                    userid = objectRow.getString("userid").replace("null", "");
//                    uname = objectRow.getString("uname").replace("null", "");
//                    nick_name = objectRow.getString("nick_name").replace("null", "");
//                    mc_no = objectRow.getString("mc_no").replace("null", "");
//
//                    itemStaffMasterArrayList.add(new ItemStaffMaster(50 * (page - 1) + i + 1 + "", position_cd, userid, RowNum, uname, nick_name, mc_no));
//                }
//                progressDialog.dismiss();
//                itemStaffAdapter.notifyDataSetChanged();
//            } catch (JSONException e) {
//                e.printStackTrace();
//                AlerError.Baoloi("Could not connect to server", WorkerAddJobActivity.this);
//                progressDialog.dismiss();
//            }
//        }
//
//    }

//    private void buildrecyc() {
//        final LinearLayoutManager mLayoutManager;
//        recyclerViewAddJob.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(WorkerAddJobActivity.this);
//
//        itemStaffAdapter = new ItemStaffAdapter(itemStaffMasterArrayList);
//        recyclerViewAddJob.setLayoutManager(mLayoutManager);
//        recyclerViewAddJob.setAdapter(itemStaffAdapter);
//        recyclerViewAddJob.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                // super.onScrolled(recyclerView, dx, dy);
//                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
//                if (lastVisiblePosition == itemStaffMasterArrayList.size() - 1) {
//                    if (page < total) {
//                        total = -1;
//                        getaddData(page + 1);
//                    }
//                }
//            }
//        });
//        itemStaffAdapter.setOnItemClickListener(new ItemStaffAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                chonvitri(position);
//
//            }
//        });
//
//    }

//    private void chonvitri(final int position) {
//        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkerAddJobActivity.this, android.R.layout.select_dialog_singlechoice);
//        for (int i = 0; i < vitridungmayMasterArrayList.size(); i++) {
//            arrayAdapter.add(vitridungmayMasterArrayList.get(i).getDt_nm());
//        }
//        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(WorkerAddJobActivity.this);
//        builderSingle.setTitle("Select Staff Type:");
//        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//
//        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int i) {
//
//                code_vitridungmay = vitridungmayMasterArrayList.get(i).getDt_cd();
//                xacnhanadd(position, vitridungmayMasterArrayList.get(i).getDt_cd());
//                dialog.dismiss();
//            }
//        });
//        builderSingle.show();
//    }

//    private void xacnhanadd(final int position, final String dt_cd) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkerAddJobActivity.this, R.style.AlertDialogCustom);
//        alertDialog.setTitle("Add Worker");
//        alertDialog.setMessage("You want: "); //"The data you entered does not exist on the server !!!");
//        alertDialog.setPositiveButton("USE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                mc_no = itemStaffMasterArrayList.get(position).getUname();
//                use_unuse = "Y";
//                String url = BaseApp.isHostting() + "TIMS/Createprocess_unitstaff";
//                getVolley(url, dt_cd, itemStaffMasterArrayList.get(position).getUserid(), use_unuse, id_actual);
//            }
//        });
//        alertDialog.setNegativeButton("UNUSE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                mc_no = itemStaffMasterArrayList.get(position).getUserid();
//                use_unuse = "N";
//                String url = BaseApp.isHostting() + "TIMS/Createprocess_unitstaff";
//                getVolley(url, dt_cd, itemStaffMasterArrayList.get(position).getUserid(), use_unuse, id_actual);
//            }
//        });
//        alertDialog.show();
//    }

//    private void getVolley(String url, final String staff_tp, final String staff_id, final String use_yn, final String id_actual) {
//        Log.e("volley", url + "   " + staff_tp + "   " + staff_id + "   " + use_yn + "   " + id_actual);
//        progressDialog.setMessage("Loading...");
//        progressDialog.setCancelable(true);
//        progressDialog.show();
//        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
//        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e("volley", response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getString("result").equals("0")) {
//                        thanhcong();
//                    } else if (jsonObject.getString("result").equals("1")) {
//                        AlerError.Baoloi("The Staff was setting duplicate date", WorkerAddJobActivity.this);
//                    } else if (jsonObject.getString("result").equals("2")) {
//                        AlerError.Baoloi("Start day was bigger End day. That is wrong", WorkerAddJobActivity.this);
//                    } else if (jsonObject.getString("result").equals("3")) {
//                        xacnhan_datontai(jsonObject.getString("update"), jsonObject.getString("start"), jsonObject.getString("end"));
//                    }
//                    progressDialog.dismiss();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    AlerError.Baoloi("Could not connect to server", WorkerAddJobActivity.this);
//                    progressDialog.dismiss();
//                }
//                progressDialog.cancel();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.cancel();
//                AlerError.Baoloi("Could not connect to server", WorkerAddJobActivity.this);
//            }
//        }) {
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("User-Agent", "android");
//                params.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//                return params;
//            }
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("staff_tp", staff_tp);
//                params.put("staff_id", staff_id);
//                params.put("id_actual", id_actual);
//                params.put("use_yn", use_yn);
//                return params;
//            }
//        };
//        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(jsonObjRequest);
//    }


//    private void thanhcong() {
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkerAddJobActivity.this, R.style.AlertDialogCustom);
//        alertDialog.setTitle("Add Worker");
//        alertDialog.setMessage("Add Worker finnish."); //"The data you entered does not exist on the server !!!");
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                finish();
//            }
//        });
//        alertDialog.show();
//    }


//    private void xacnhan_datontai(final String update, final String start, final String end) {
//
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkerAddJobActivity.this, R.style.AlertDialogCustom);
//        alertDialog.setTitle("Add Worker");
//        alertDialog.setMessage("This Worker has already selected. If you confirm it, this Worker will finish the task at the previous stage."); //"The data you entered does not exist on the server !!!");
//        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                new huyketquatruoc().execute(BaseApp.isHostting() + "ActualWO/Createprocessstaff_duplicate?staff_tp=" +
//                        code_vitridungmay + "&staff_id=" + mc_no + "&id_actual=" + id_actual + "&use_yn=" + use_unuse + "&id_update=" + update + "&start=" + start + "&end=" + end + "&remark=");
//            }
//        });
//        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        alertDialog.show();
//    }

//    private class huyketquatruoc extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            return NoiDung_Tu_URL(strings[0]);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setMessage("Loading...");
//            progressDialog.setCancelable(true);
//            progressDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            try {
//
//                JSONObject jsonObject = new JSONObject(s);
//                progressDialog.dismiss();
//                finish();
//            } catch (JSONException e) {
//                e.printStackTrace();
//                AlerError.Baoloi("Could not connect to server", WorkerAddJobActivity.this);
//                progressDialog.dismiss();
//            }
//        }
//
//    }

    private void hidefab() {
        fabAddJob_input.setVisibility(View.GONE);
        fabAddJob_scan.setVisibility(View.GONE);
        fabAddJob_select.setVisibility(View.GONE);
    }

    private void showfab() {
        fabAddJob_input.setVisibility(View.VISIBLE);
        fabAddJob_scan.setVisibility(View.VISIBLE);
        //  fabAddJob_select.setVisibility(View.VISIBLE);
    }

    private void loadWordker(String id_actual) {
        String url = BaseApp.isHostting() +
                "/TIMS/GetTIMSListStaff?page=1&rows=50&sidx=&sord=asc&_search=false&id_actual=" +
                id_actual;
        Log.d("loadWordker", url);
        progressDialog = new ProgressDialog(WorkerAddJobActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("loadWordker", response);
                listWorker = new ArrayList<ItemWoker>();
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");

                    if (jsonArray.length() == 0) {
                        //AlerError.Baoloi("Data don't have!!!", WorkerAddJobActivity.this);
                        recyclerViewAddJob.setVisibility(View.GONE);
                        nodata.setVisibility(View.VISIBLE);
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            listWorker.add(new ItemWoker(
                                    object.getString("psid"),
                                    object.getString("staff_id"),
                                    object.getString("uname"),
                                    object.getString("ActualQty"),
                                    object.getString("use_yn"),
                                    object.getString("staff_tp"),
                                    object.getString("Defective"),
                                    object.getString("start_dt"),
                                    object.getString("end_dt")
                            ));
                        }
                        nodata.setVisibility(View.GONE);
                        recyclerViewAddJob.setVisibility(View.VISIBLE);
                        buildRV();
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), WorkerAddJobActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), WorkerAddJobActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(WorkerAddJobActivity.this);
        requestQueue.add(stringRequest);
    }

    private void buildRV() {
        adapterworker = new AdapterItemWorker(listWorker);
        recyclerViewAddJob.setLayoutManager(new LinearLayoutManager(WorkerAddJobActivity.this));
        recyclerViewAddJob.setHasFixedSize(true);
        recyclerViewAddJob.setAdapter(adapterworker);

        adapterworker.setOnItemClickListener(new AdapterItemWorker.OnItemClickListener() {
            @Override
            public void onItemClick(int position, RecyclerView recyclerView) {
//                if (recyclerView.getVisibility() == View.GONE) {
//                    recyclerView.setVisibility(View.VISIBLE);
//                    makeRecyclerDetail(position, recyclerView);
//                } else {
//                    recyclerView.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onButtonClick(int position) {
                //Toast.makeText(WorkerAddJobActivity.this,"type"+ type +"/" +id_actual+"/"+RollName+"/"+listWorker.get(position).getStaff_id(), Toast.LENGTH_SHORT).show();
                if (type.equals("OQC")) {
                    Intent intent = new Intent(WorkerAddJobActivity.this, CompositeOqcActivity.class);
                    intent.putExtra("Type", type);
                    intent.putExtra("id_actual", id_actual);
                    intent.putExtra("RollName", RollName);
                    intent.putExtra("staff_id", listWorker.get(position).getStaff_id());
                    intent.putExtra("QCCode", QCCode);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(WorkerAddJobActivity.this, CompositeCheckEAActivity.class);
                    intent.putExtra("Type", type);
                    intent.putExtra("id_actual", id_actual);
                    intent.putExtra("RollName", RollName);
                    intent.putExtra("staff_id", listWorker.get(position).getStaff_id());
                    intent.putExtra("QCCode", QCCode);
                    startActivity(intent);
                }
            }
        });
    }

    private void makeRecyclerDetail(int position, RecyclerView recyclerView) {
        listDetail = new ArrayList<>();
        rvView = recyclerView;
        String url = BaseApp.isHostting() + "/TIMS/GetTIMSActualDetail?id_actual=" + id_actual
                + "&staff_id=" + listWorker.get(position).getStaff_id() +
                "&_search=false&rows=100&page=1&sidx=&sord=asc";
        detailJson(url);
        buildRVdetail();
    }

    private void buildRVdetail() {
        detailadapter = new ListDetailAdapter(listDetail);
        rvView.setLayoutManager(new LinearLayoutManager(WorkerAddJobActivity.this));
        rvView.setHasFixedSize(true);
        rvView.setAdapter(detailadapter);
    }

    private void detailJson(String url) {
        Log.d("detailJson", url);
        progressDialog = new ProgressDialog(WorkerAddJobActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("detailJson", response);
                listDetail = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");

                    if (jsonArray.length() == 0) {
                        BaseApp.sendWarning(null, "Data don't have!!!", WorkerAddJobActivity.this);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            listDetail.add(new ListDetail(
                                    object.getString("wmtid"),
                                    object.getString("mt_cd"),
                                    object.getString("bb_no"),
                                    object.getString("real_qty"),
                                    object.getString("gr_qty")
                            ));
                        }
                    }
                    buildRVdetail();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    BaseApp.sendWarning("Error!!!", "The json error:" + e.toString(), WorkerAddJobActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                BaseApp.sendWarning("Error !!!", "The server error:" + error.toString(), WorkerAddJobActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(WorkerAddJobActivity.this);
        requestQueue.add(stringRequest);
    }

    private static class ListDetail {
        String wmtid, mt_cd, bb_no, real_qty, gr_qty;

        public ListDetail(String wmtid, String mt_cd, String bb_no, String real_qty, String gr_qty) {
            this.wmtid = wmtid;
            this.mt_cd = mt_cd;
            this.bb_no = bb_no;
            this.real_qty = real_qty;
            this.gr_qty = gr_qty;
        }

        public String getWmtid() {
            return wmtid;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public String getBb_no() {
            return bb_no;
        }

        public String getReal_qty() {
            return real_qty;
        }

        public String getGr_qty() {
            return gr_qty;
        }
    }

    private static class ListDetailAdapter extends RecyclerView.Adapter<ListDetailAdapter.ListDetailViewHolder> {
        private ArrayList<ListDetail> items;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ListDetailViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_detail,
                    viewGroup, false);
            ListDetailViewHolder evh = new ListDetailViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListDetailAdapter.ListDetailViewHolder vh, int i) {
            ListDetail currentItem = items.get(i);

            vh.ml_d.setText(currentItem.getMt_cd());
            vh.bo_no.setText(currentItem.getBb_no());
            vh.qty.setText(currentItem.getGr_qty());
            vh.qty_r.setText(currentItem.getReal_qty());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class ListDetailViewHolder extends RecyclerView.ViewHolder {
            public TextView ml_d;
            public TextView bo_no;
            public TextView qty;
            public TextView qty_r;

            public ListDetailViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);

                ml_d = itemView.findViewById(R.id.ml_d);
                bo_no = itemView.findViewById(R.id.bo_no);
                qty = itemView.findViewById(R.id.qty);
                qty_r = itemView.findViewById(R.id.qty_r);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }

        public ListDetailAdapter(ArrayList<ListDetail> item) {
            items = item;
        }
    }

    private class ItemWoker {
        String psid, staff_id, uname, actualQty, use_yn, staff_tp, defective, start_dt, end_dt;

        public String getPsid() {
            return psid;
        }

        public String getStaff_id() {
            return staff_id;
        }

        public String getUname() {
            return uname;
        }

        public String getActualQty() {
            return actualQty;
        }

        public String getUse_yn() {
            return use_yn;
        }

        public String getStaff_tp() {
            return staff_tp;
        }

        public String getDefective() {
            return defective;
        }

        public String getStart_dt() {
            return start_dt;
        }

        public String getEnd_dt() {
            return end_dt;
        }

        public ItemWoker(String psid, String staff_id, String uname, String actualQty, String use_yn,
                         String staff_tp, String defective, String start_dt, String end_dt) {
            this.psid = psid;
            this.staff_id = staff_id;
            this.uname = uname;
            this.actualQty = actualQty;
            this.use_yn = use_yn;
            this.staff_tp = staff_tp;
            this.defective = defective;
            this.start_dt = start_dt;
            this.end_dt = end_dt;
        }
    }

    private static class AdapterItemWorker extends RecyclerView.Adapter<AdapterItemWorker.WorkerViewHolder> {
        private ArrayList<ItemWoker> item;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public AdapterItemWorker.WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_job,
                    viewGroup, false);
            AdapterItemWorker.WorkerViewHolder evh = new AdapterItemWorker.WorkerViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull WorkerViewHolder vh, int i) {
            ItemWoker currentItem = item.get(i);

            vh.product.setText(currentItem.getStaff_id());
            vh.item_vcd.setText(currentItem.getUname());
            vh.actual.setText(currentItem.getActualQty());
            vh.defect.setText(currentItem.getDefective());
            vh.no.setText(i + 1 + "");

        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position, RecyclerView recyclerView);

            void onButtonClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener
                                                   listener) {
            mListener = listener;
        }

        public static class WorkerViewHolder extends RecyclerView.ViewHolder {
            //            public TextView tv_item_id, tv_item_name, tv_item_actual, tv_item_defective;
            public TextView btn_item_Composite;

            public TextView item_vcd, product, defect, actual, no;
            RecyclerView re_de_add;

            public WorkerViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                btn_item_Composite = itemView.findViewById(R.id.btn_item_Composite);
                item_vcd = itemView.findViewById(R.id.item_vcd);//name
                product = itemView.findViewById(R.id.product);//id
                defect = itemView.findViewById(R.id.defect); //a
                actual = itemView.findViewById(R.id.actual); //d

                re_de_add = itemView.findViewById(R.id.re_de_add);

                no = itemView.findViewById(R.id.no); //d

//                tv_item_id = itemView.findViewById(R.id.tv_item_id);
//                tv_item_name = itemView.findViewById(R.id.tv_item_name);
//                tv_item_actual = itemView.findViewById(R.id.tv_item_actual);
//                tv_item_defective = itemView.findViewById(R.id.tv_item_defective);
//                btn_item_Composite = itemView.findViewById(R.id.btn_item_Composite);
//
                btn_item_Composite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onButtonClick(position);
                            }
                        }
                    }
                });

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemClick(position, re_de_add);
                            }
                        }
                    }
                });
            }
        }

        public AdapterItemWorker(ArrayList<ItemWoker> listWorker) {
            item = listWorker;
        }
    }
}