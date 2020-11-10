package com.example.timsapp.ui.home.Composite;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class WorkerActivity extends AppCompatActivity {
    String webUrl = BaseApp.isHostting();
    String id_actual;
    private ProgressDialog dialog;
    ArrayList<ItemStaffMaster> itemStaffMasterArrayList;
    ItemStaffAdapter itemStaffAdapter;
    int page = 1;
    int total = -1;
    RecyclerView recyclerView;

    ArrayList<VitridungmayMaster> vitridungmayMasterArrayList;

    String mc_no ="",use_unuse="";
    private String code_vitridungmay="";

    FloatingActionButton scan;
    FloatingActionButton input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);
        setTitle("Worker");
        id_actual = CompositeActivity.id_actual;
        dialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recyclerView);
        getData(page);
        new getvitridungmay().execute(webUrl + "ActualWO/get_staff");

        scan = findViewById(R.id.scan);
        input = findViewById(R.id.input);
        input.setVisibility(View.GONE);
        scan.setVisibility(View.GONE);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getVisibility()==View.VISIBLE){
                    input.setVisibility(View.GONE);
                    scan.setVisibility(View.GONE);
                }else {
                    input.setVisibility(View.VISIBLE);
                    scan.setVisibility(View.VISIBLE);
                }

            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanWorker();
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }
    private void inputData() {

        final Dialog dialog = new Dialog(WorkerActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(WorkerActivity.this).inflate(R.layout.popup_input, null);
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
                }  else {
                    h2.setError(null);
                    findscanWorker(Containercode.getText().toString());
                    dialog.dismiss();
                }


            }
        });


        dialog.show();

    }

    private void scanWorker() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(WorkerActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                findscanWorker(result.getContents());
            }
        }
    }
    private void findscanWorker(String contents) {
        new getData().execute(webUrl + "ActualWO/search_staff_pp?page=" + 1 + "&rows=50&sidx=&sord=asc&userid=" + contents +
                "&md_nm=&_search=false");
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
                String dt_cd,dt_nm;
                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray.length()==0){
                    return;
                }
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);

                    dt_cd = object.getString("dt_cd");
                    dt_nm = object.getString("dt_nm");
                    vitridungmayMasterArrayList.add(new VitridungmayMaster(dt_cd,dt_nm));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", WorkerActivity.this);
            }
        }

    }

    private void getData(int page) {
        new getData().execute(webUrl + "ActualWO/search_staff_pp?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");
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
            itemStaffMasterArrayList = new ArrayList<>();
            String position_cd,userid,RowNum,uname,nick_name,mc_no;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", WorkerActivity.this);
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

                    itemStaffMasterArrayList.add(new ItemStaffMaster(i+1+"",position_cd,userid,RowNum,uname,nick_name,mc_no));
                }
                dialog.dismiss();
                buildrecyc();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", WorkerActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void buildrecyc() {

        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(WorkerActivity.this);
        itemStaffAdapter = new ItemStaffAdapter(itemStaffMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemStaffAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                chonvitri(position);

            }
        });


    }

    private void chonvitri(final int position) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(WorkerActivity.this, android.R.layout.select_dialog_singlechoice);
        for (int i = 0; i < vitridungmayMasterArrayList.size(); i++) {
            arrayAdapter.add(vitridungmayMasterArrayList.get(i).getDt_nm());
        }
        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(WorkerActivity.this);
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

                code_vitridungmay = vitridungmayMasterArrayList.get(i).dt_cd;
                xacnhanadd(position,vitridungmayMasterArrayList.get(i).dt_cd);
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }


    private void xacnhanadd(final int position, final String dt_cd) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkerActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add Worker");
        alertDialog.setMessage("You want: "); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("USE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mc_no = itemStaffMasterArrayList.get(position).userid;
                use_unuse = "Y";
                String url =webUrl+ "TIMS/Createprocess_unitstaff";
                getVolley(url,dt_cd,itemStaffMasterArrayList.get(position).userid,use_unuse,id_actual);
            }
        });
        alertDialog.setNegativeButton("UNUSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mc_no = itemStaffMasterArrayList.get(position).userid;
                use_unuse = "N";
                String url =webUrl+ "TIMS/Createprocess_unitstaff";
                getVolley(url,dt_cd,itemStaffMasterArrayList.get(position).userid,use_unuse,id_actual);
            }
        });
        alertDialog.show();
        alertDialog.show();
    }

    private void getVolley(String url, final String staff_tp, final String staff_id, final String use_yn, final String id_actual) {
        Log.e("volley",url+ "   "+staff_tp+"   "+staff_id+"   "+use_yn+"   "+id_actual);
        dialog.setMessage("Loading...");
        dialog.setCancelable(true);
        dialog.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("volley", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getString("result").equals("0")){
                        thanhcong();
                    }else if(jsonObject.getString("result").equals("1")){
                        AlerError.Baoloi("The Staff was setting duplicate date", WorkerActivity.this);
                    }else if (jsonObject.getString("result").equals("2")){
                        AlerError.Baoloi("Start day was bigger End day. That is wrong", WorkerActivity.this);
                    } else if (jsonObject.getString("result").equals("3")){
                        xacnhan_datontai(jsonObject.getString("update"),jsonObject.getString("start"),jsonObject.getString("end"));
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", WorkerActivity.this);
                    dialog.dismiss();
                }
                dialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                AlerError.Baoloi("Could not connect to server", WorkerActivity.this);
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "android");
                params.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("staff_tp", staff_tp);
                params.put("staff_id", staff_id);
                params.put("id_actual", id_actual);
                params.put("use_yn", use_yn);
                return params;
            }
        };
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjRequest);
    }


    private void thanhcong() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkerActivity.this, R.style.AlertDialogCustom);
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

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(WorkerActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add Worker");
        alertDialog.setMessage("This Worker has already selected. If you confirm it, this Worker will finish the task at the previous stage."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new huyketquatruoc().execute(webUrl+"ActualWO/Createprocessstaff_duplicate?staff_tp="+
                        code_vitridungmay+"&staff_id="+ mc_no +"&id_actual="+id_actual+"&use_yn="+use_unuse+"&id_update="+update+"&start="+start+"&end="+end+"&remark=");
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
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                dialog.dismiss();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", WorkerActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void getaddData(int page) {
        new getaddData().execute(webUrl + "ActualWO/search_staff_pp?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");
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

            String position_cd,userid,RowNum,uname,nick_name,mc_no;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", WorkerActivity.this);
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

                    itemStaffMasterArrayList.add(new ItemStaffMaster(50*(page-1)+i+1+"",position_cd,userid,RowNum,uname,nick_name,mc_no));
                }
                dialog.dismiss();
                itemStaffAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", WorkerActivity.this);
                dialog.dismiss();
            }
        }

    }
}