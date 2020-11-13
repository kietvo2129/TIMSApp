package com.example.timsapp.ui.home.Manufacturing.Worker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.example.timsapp.ui.home.Composite.ItemStaffAdapter;
import com.example.timsapp.ui.home.Composite.ItemStaffMaster;
import com.example.timsapp.ui.home.Composite.VitridungmayMaster;
import com.example.timsapp.ui.home.Composite.WorkerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class ListWorkerActivity extends AppCompatActivity {
    ArrayList<VitridungmayMaster> vitridungmayMasterArrayList;
    private String code_vitridungmay = "";
    String mc_no = "", use_unuse = "";
    private String id_actual ="";
    private ProgressDialog progressDialog;
    private ArrayList<ItemStaffMaster> itemStaffMasterArrayList;
    private RecyclerView recyclerViewListWorker;
    private ItemStaffAdapter itemStaffAdapter;
    int page = 1;
    int total = -1;
    private String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_worker);
        id_actual = getIntent().getStringExtra("id_actual");
        //intent.putExtra("id_actual", id_actual);

        progressDialog = new ProgressDialog(this, R.style.AlertDialogCustom);
        recyclerViewListWorker = findViewById(R.id.recyclerViewListWorker);
        id = getIntent().getStringExtra("userId");

//        String url = BaseApp.isHostting() + "/TIMS/GetWorkers?page=1&rows=50&sidx=&sord=asc&_search=false&userId=" + id + "&userName=&positionCode=";
//        new findWorker().execute(url);
//        Log.e("findscanWorker", url);
//
//        new getvitridungmay().execute(BaseApp.isHostting() + "ActualWO/get_staff");
    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = BaseApp.isHostting() + "/TIMS/GetWorkers?page=1&rows=50&sidx=&sord=asc&_search=false&userId=" + id + "&userName=&positionCode=";
        new findWorker().execute(url);
        Log.e("findscanWorker", url);

        new getvitridungmay().execute(BaseApp.isHostting() + "ActualWO/get_staff");
    }

    private class findWorker extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            itemStaffMasterArrayList = new ArrayList<>();
            String position_cd, userid, RowNum, uname, nick_name, mc_no;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("No data", ListWorkerActivity.this);
                    return;
                }
                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
//                    position_cd = objectRow.getString("position_cd").replace("null", "");
//                    RowNum = objectRow.getString("RowNum").replace("null", "");
//                    userid = objectRow.getString("userid").replace("null", "");
//                    uname = objectRow.getString("uname").replace("null", "");
//                    nick_name = objectRow.getString("nick_name").replace("null", "");
//                    mc_no = objectRow.getString("mc_no").replace("null", "");

                    position_cd = objectRow.getString("PositionName").replace("null", "");
                    RowNum = objectRow.getString("RowNum").replace("null", "");
                    userid = objectRow.getString("UserId").replace("null", "");
                    uname = objectRow.getString("UserName").replace("null", "");
                    nick_name = objectRow.getString("nick_name").replace("null", "");
                    mc_no = objectRow.getString("mc_no").replace("null", "");

                    itemStaffMasterArrayList.add(new ItemStaffMaster(i + 1 + "", position_cd, userid, RowNum, uname, nick_name, mc_no));
                }
                progressDialog.dismiss();
                buildrecyc();

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ListWorkerActivity.this);
                progressDialog.dismiss();
            }
        }
    }

    private void getaddData(int page) {
        new getaddData().execute(BaseApp.isHostting() + "ActualWO/search_staff_pp?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");
    }

    private class getaddData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String position_cd, userid, RowNum, uname, nick_name, mc_no;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("No data", ListWorkerActivity.this);
                    return;
                }
                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    position_cd = objectRow.getString("position_cd").replace("null", "");
                    RowNum = objectRow.getString("RowNum").replace("null", "");
                    userid = objectRow.getString("userid").replace("null", "");
                    uname = objectRow.getString("uname").replace("null", "");
                    nick_name = objectRow.getString("nick_name").replace("null", "");
                    mc_no = objectRow.getString("mc_no").replace("null", "");

                    itemStaffMasterArrayList.add(new ItemStaffMaster(50 * (page - 1) + i + 1 + "", position_cd, userid, RowNum, uname, nick_name, mc_no));
                }
                progressDialog.dismiss();
                itemStaffAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ListWorkerActivity.this);
                progressDialog.dismiss();
            }
        }
    }

    private void buildrecyc() {
        final LinearLayoutManager mLayoutManager;
        recyclerViewListWorker.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(ListWorkerActivity.this);

        itemStaffAdapter = new ItemStaffAdapter(itemStaffMasterArrayList);
        recyclerViewListWorker.setLayoutManager(new LinearLayoutManager(ListWorkerActivity.this));
        recyclerViewListWorker.setAdapter(itemStaffAdapter);
        recyclerViewListWorker.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == itemStaffMasterArrayList.size() - 1) {
                    if (page < total) {
                        total = -1;
                        getaddData(page + 1);
                    }
                }
            }
        });
        itemStaffAdapter.setOnItemClickListener(new ItemStaffAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //chonvitri(position);
                xacnhanadd(position,"","");
            }
        });
    }

    private void chonvitri(final int position) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ListWorkerActivity.this, android.R.layout.select_dialog_singlechoice);

        for (int i = 0; i < vitridungmayMasterArrayList.size(); i++) {
            arrayAdapter.add(vitridungmayMasterArrayList.get(i).getDt_nm());
        }

        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(ListWorkerActivity.this);
        builderSingle.setTitle("Select Staff Type:");
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                code_vitridungmay = vitridungmayMasterArrayList.get(i).getDt_cd();
                xacnhanadd(position, vitridungmayMasterArrayList.get(i).getDt_cd(), vitridungmayMasterArrayList.get(i).getDt_nm());
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private class getvitridungmay extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            vitridungmayMasterArrayList = new ArrayList<>();
            try {
                String dt_cd, dt_nm;
                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray.length() == 0) {
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    dt_cd = object.getString("dt_cd");
                    dt_nm = object.getString("dt_nm");
                    vitridungmayMasterArrayList.add(new VitridungmayMaster(dt_cd, dt_nm));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ListWorkerActivity.this);
            }
        }
    }

    private void xacnhanadd(final int position /* lít úee*/, final String dt_cd, String name) {
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(ListWorkerActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add Worker");
        alertDialog.setMessage("You want: " + itemStaffMasterArrayList.get(position).getUname() + " / " + name); //"The data you entered does not exist on the server !!!");

        alertDialog.setPositiveButton("USE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mc_no = itemStaffMasterArrayList.get(position).getUname();
                use_unuse = "Y";
                String url = BaseApp.isHostting() + "TIMS/Createprocess_unitstaff";
                String link = BaseApp.isHostting()
                        + "/TIMS/Createprocess_unitstaff?staff_id="
                        + itemStaffMasterArrayList.get(position).getUserid()
                        + "&id_actual=" + id_actual + "&use_yn=Y";
                addWorker(link);
                //getVolley(url, dt_cd, itemStaffMasterArrayList.get(position).getUserid(), use_unuse, id_actual);
            }
        });
        alertDialog.setNegativeButton("UNUSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mc_no = itemStaffMasterArrayList.get(position).getUserid();
                use_unuse = "N";
                String url = BaseApp.isHostting() + "TIMS/Createprocess_unitstaff";
                //getVolley(url, dt_cd, itemStaffMasterArrayList.get(position).getUserid(), use_unuse, id_actual);
                String link = BaseApp.isHostting()
                        + "/TIMS/Createprocess_unitstaff?staff_id="
                        + itemStaffMasterArrayList.get(position).getUserid()
                        + "&id_actual=" + id_actual + "&use_yn=N";
                addWorker(link);
            }
        });
        alertDialog.show();
    }

    private void addWorker(String stringData) {
        //http://messhinsungcntvina.com:83/TIMS/Createprocess_unitstaff?staff_id=90254&id_actual=85&use_yn=Y

        String url = stringData;
        Log.d("addWorker", url);

        progressDialog = new ProgressDialog(ListWorkerActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("addWorker", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("result").equals("0")) {
                        thanhcong();
                    } else if (jsonObject.getString("result").equals("1")) {
                        AlerError.Baoloi("The Staff was setting duplicate date", ListWorkerActivity.this);
                    } else if (jsonObject.getString("result").equals("2")) {
                        AlerError.Baoloi("Start day was bigger End day. That is wrong", ListWorkerActivity.this);
                    } else if (jsonObject.getString("result").equals("3")) {
                        //This Worker has already selected. If you confirm it, this worker will finish the task at the previous stage
                        xacnhan_datontai(jsonObject.getString("update"), jsonObject.getString("start"), jsonObject.getString("end"));
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", ListWorkerActivity.this);
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), ListWorkerActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(ListWorkerActivity.this);
        requestQueue.add(stringRequest);
    }

//    private void agetVolley(String url, final String staff_tp, final String staff_id, final String use_yn, final String id_actual) {
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
//                        AlerError.Baoloi("The Staff was setting duplicate date", ListWorkerActivity.this);
//                    } else if (jsonObject.getString("result").equals("2")) {
//                        AlerError.Baoloi("Start day was bigger End day. That is wrong", ListWorkerActivity.this);
//                    } else if (jsonObject.getString("result").equals("3")) {
//                        xacnhan_datontai(jsonObject.getString("update"), jsonObject.getString("start"), jsonObject.getString("end"));
//                    }
//                    progressDialog.dismiss();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    AlerError.Baoloi("Could not connect to server", ListWorkerActivity.this);
//                    progressDialog.dismiss();
//                }
//                progressDialog.cancel();
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.cancel();
//                AlerError.Baoloi("Could not connect to server", ListWorkerActivity.this);
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

    private void thanhcong() {

        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(ListWorkerActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add Worker");
        alertDialog.setMessage("Add Worker finnish."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialog.show();
    }

    private void xacnhan_datontai(final String update, final String start, final String end) {

        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(ListWorkerActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add Worker");
        alertDialog.setMessage("This Worker has already selected. If you confirm it, this Worker will finish the task at the previous stage."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new huyketquatruoc().execute(BaseApp.isHostting() + "ActualWO/Createprocessstaff_duplicate?staff_tp=" +
                        code_vitridungmay + "&staff_id=" + mc_no + "&id_actual=" + id_actual + "&use_yn=" + use_unuse + "&id_update=" + update + "&start=" + start + "&end=" + end + "&remark=");
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    private class huyketquatruoc extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                progressDialog.dismiss();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", ListWorkerActivity.this);
                progressDialog.dismiss();
            }
        }

    }
}