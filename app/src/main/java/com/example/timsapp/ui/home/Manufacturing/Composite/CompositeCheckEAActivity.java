package com.example.timsapp.ui.home.Manufacturing.Composite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.timsapp.ui.home.Manufacturing.Composite.QC.CheckEaActivity;
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

public class CompositeCheckEAActivity extends AppCompatActivity {
    private static final String ID_ADDNG = "ID_ADDNG";

    private static final String ID_MG = "ID_MG";
    private TextView nodata;
    private RecyclerView recyclerViewOqc;
    private FloatingActionButton fab_add, fab_in, fab_can;
    private String type;
    private String RollName;
    private String id_actual;
    private String staff_id;
    private ProgressDialog progressDialog;
    private AdapterItemOQC adapterOqc;
    private ArrayList<ListCheckQC> listCheckQC;
    private String QCCode;
    private ProgressDialog dialog;
    private RecyclerView recyclerAddNG;
    private EditText tv_mlno, tv_mtno, qty_ng;
    private AdapterNG adpNG;
    private ArrayList<ListNG> listNG;
    private TextView nodatap;
    private static final String K_edit = "K_edit";
    private static final String K_add = "K_add";
    private static final String K_Divi = "K_Divi";
    private String keyscan;
    private int myPosiion;
    private TextView nodatap_m;
    private RecyclerView recycler_view_merge;
    private EditText tv_con_t, tv_mlno_m, tv_mtno_m, qty_m_a;
    private ArrayList<ListMerge> listMerge;
    private AdapterMerge adpMer;
    private RecyclerView recyclerViewDetail;
    private TextView nodatad;
    private ArrayList<ListDetailQ> listDetail;
    private AdapterDetailQ adapterDetailQ;
    private RecyclerView recyclerViewDivide;
    private EditText qty_dev;
    private TextView nodatadivide;
    private TextView tv_qty_s, tv_cont;
    private ArrayList<ListDivide> listDivide;
    private AdapterDivide adapterDivide;
    private ArrayList<ListDivide> listDivideold;
    private String urlsearchJsonDivide;
    private String maLot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composite_check_ea);

        nodata = findViewById(R.id.nodata);
        recyclerViewOqc = findViewById(R.id.recyclerViewOqc);
        fab_add = findViewById(R.id.fab);
        fab_in = findViewById(R.id.input);
        fab_can = findViewById(R.id.scan);

        type = getIntent().getStringExtra("Type");
        setTitle(type + " EA");
        RollName = getIntent().getStringExtra("RollName");
        id_actual = getIntent().getStringExtra("id_actual");
        staff_id = getIntent().getStringExtra("staff_id");
        QCCode = getIntent().getStringExtra("QCCode");

        dialog = new ProgressDialog(CompositeCheckEAActivity.this, R.style.AlertDialogCustom);
        fchide();

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab_can.getVisibility() == View.GONE) {
                    fcshow();
                } else {
                    fchide();
                }
            }
        });
        fab_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fchide();
                inputData(K_add); //add
            }
        });
        fab_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fchide();
                scanBobbin(K_add); //Add
            }
        });

        // not oqc/ roll
        // http://messhinsungcntvina.com:83/TIMS/getmt_date_web_auto?id_actual=61&staff_id=90262
        //[{"wmtid":577,
        // "date":"2020-11-09",
        // "mt_cd":"LJ63-19131ANQ20201109142837000001",
        // "mt_no":"LJ63-19131A-NQ",
        // "real_qty":1000,
        // "gr_qty":900,
        // "gr_qty1":900,
        // "bbmp_sts_cd":["Init"],
        // "mt_qrcode":"LJ63-19131ANQ20201109142837000001",
        // "lct_cd":null,
        // "bb_no":"AUTO-TRAY-20201103114630000001",
        // "mt_barcode":"LJ63-19131ANQ20201109142837000001",
        // "chg_dt":"\/Date(1604907627000)\/",
        // "sl_tru_ng":null
        // },{"wmtid":523,"date":"2020-11-09","mt_cd":"LJ63-19131ANQ20201109130612000001","mt_no":"LJ63-19131A-NQ","real_qty":900,"gr_qty":0,"gr_qty1":0,"bbmp_sts_cd":["Init"],"mt_qrcode":"LJ63-19131ANQ20201109130612000001","lct_cd":null,"bb_no":"AUTO-BOB-20201103092146000008","mt_barcode":"LJ63-19131ANQ20201109130612000001","chg_dt":"\/Date(1604902189000)\/","sl_tru_ng":null},{"wmtid":436,"date":"2020-11-07","mt_cd":"LJ63-19131ANQ20201107140645000001","mt_no":"LJ63-19131A-NQ","real_qty":1900,"gr_qty":0,"gr_qty1":0,"bbmp_sts_cd":["Init"],"mt_qrcode":"LJ63-19131ANQ20201107140645000001","lct_cd":null,"bb_no":"AUTO-BOB-20201102220132000006","mt_barcode":"LJ63-19131ANQ20201107140645000001","chg_dt":"\/Date(1604733072000)\/","sl_tru_ng":null}]

        // EA
        // http://messhinsungcntvina.com:83/TIMS/getmt_date_web_auto?id_actual=95&staff_id=90265
        //[{"wmtid":592,"date":"2020-11-10","mt_cd":"LJ63-16628A-DD20201110090646000001",
        // "mt_no":"LJ63-16628A-DD","real_qty":0,"gr_qty":0,"gr_qty1":0,"bbmp_sts_cd":["Init"],
        // "mt_qrcode":"LJ63-16628A-DD20201110090646000001","lct_cd":null,"bb_no":"BOBBIN-070",
        // "mt_barcode":"LJ63-16628A-DD20201110090646000001","chg_dt":"\/Date(1604974006000)\/","sl_tru_ng":null}]
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (type.equals("OQC")) {
        } else {
            String url = BaseApp.isHostting() + "/TIMS/getmt_date_web_auto?id_actual=" + id_actual + "&staff_id=" + staff_id;
            getJsonQC(url);
        }
    }

    private void inputData(final String key) {
        final Dialog dialog = new Dialog(CompositeCheckEAActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(CompositeCheckEAActivity.this).inflate(R.layout.popup_input, null);
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
                    if (key == K_add) {
                        creatingMLno(Containercode.getText().toString()); // input
                    } else if (key == K_edit) {
                        ChangeBobbin(Containercode.getText().toString());
                    } else if (key == K_Divi) {
                        addDivive(Containercode.getText().toString());
                    } else {

                    }
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void addDivive(String sty) {
        String url = BaseApp.isHostting() + "/TIMS/Changebb_dv?bb_no=" + sty + "&wmtid=" + listDivide.get(myPosiion).getWmtid();
        addJsonDivive(url);
    }

    private void addJsonDivive(String url) {
        Log.d("addJsonDivive", url);
        progressDialog = new ProgressDialog(CompositeCheckEAActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("addJsonDivive", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("result")) {
                        Toast.makeText(CompositeCheckEAActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        searchJsonDivide(urlsearchJsonDivide);
                    } else {
                        AlerError.Baoloi(jsonObject.getString("message"), CompositeCheckEAActivity.this);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    BaseApp.sendWarning("Error!!!", "The json error:" + e.toString(), CompositeCheckEAActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                BaseApp.sendWarning("Error !!!", "The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    private void ChangeBobbin(String str) {
        String url = BaseApp.isHostting() + "/TIMS/Changebb_dvEA?bb_no=" + str + "&wmtid=" + listCheckQC.get(myPosiion).getWmtid();
        chagejsonBobbin(url);
    }

    private void chagejsonBobbin(String url) {
        Log.d("chagejsonBobbin", url);
        progressDialog = new ProgressDialog(CompositeCheckEAActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("chagejsonBobbin", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("result")) {
                        Toast.makeText(CompositeCheckEAActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    } else {
                        AlerError.Baoloi(jsonObject.getString("message"), CompositeCheckEAActivity.this);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    BaseApp.sendWarning("Error!!!", "The json error:" + e.toString(), CompositeCheckEAActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                BaseApp.sendWarning("Error !!!", "The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    // open scan qr code
    private void scanBobbin(String key) {
        keyscan = key;
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(CompositeCheckEAActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if (keyscan == K_add) {
                    creatingMLno(result.getContents()); //scan
                } else if (keyscan == K_edit) {
                    ChangeBobbin(result.getContents());
                } else if (keyscan == K_Divi) {
                    addDivive(result.getContents());
                } else if (keyscan == ID_MG) {
                    tv_con_t.setText(result.getContents());
                    searchmergi();
                } else if (keyscan == ID_ADDNG) {
                    tv_mlno.setText(result.getContents());
                    search();
                } else {

                }
            }
        }
    }

    private void fcshow() {
        fab_in.setVisibility(View.VISIBLE);
        fab_can.setVisibility(View.VISIBLE);
    }

    private void fchide() {
        fab_in.setVisibility(View.GONE);
        fab_can.setVisibility(View.GONE);
    }

    private void creatingMLno(String contents) {
        String url = BaseApp.isHostting() + "/TIMS/insertw_materialEA_mping?" +
                "id_actual=" + id_actual +
                "&staff_id=" + staff_id +
                "&bb_no=" + contents;
        new creatingJMLno().execute(url);
        Log.e("creatingMLno", url);
        // /TIMS/getListQR_oqc?id_actual=96&staff_id=80914&bb_no=AUTO-BOB-20201102160416000005
    }

    private class creatingJMLno extends AsyncTask<String, Void, String> {
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

                if (jsonObject.has("result")) {
                    if (!jsonObject.getBoolean("result")) {
                        dialog.dismiss();
                        AlerError.Baoloi(jsonObject.getString("message"), CompositeCheckEAActivity.this);
                        return;
                    } else {
                        dialog.dismiss();
                        Toast.makeText(CompositeCheckEAActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(CompositeCheckEAActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void popDetai(final int position) {
        Rect displayRectangle = new Rect();
        Window window = CompositeCheckEAActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(CompositeCheckEAActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_detail_qc, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        ImageView btn_close = dialogView.findViewById(R.id.img_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        recyclerViewDetail = dialogView.findViewById(R.id.recyclerViewDetail);
        nodatad = dialogView.findViewById(R.id.nodatad);
        searchDetail(position);
        alertDialog.show();
    }

    private void searchDetail(int position) {
        //http://messhinsungcntvina.com:83/TIMS/ds_mapping_w?mt_cd=LJ63-16628A-NQ20201109100930000001&_search=false&nd=1605007030127&rows=50&page=1&sidx=&sord=asc
        String url = BaseApp.isHostting() + "/TIMS/ds_mapping_w?mt_cd=" + listCheckQC.get(position).getMt_cd() +
                "&_search=false&rows=50&page=1&sidx=&sord=asc";
        loadDetail(url);
    }

    private void loadDetail(String url) {
        Log.d("loadDetail", url);
        dialog.show(); // Display Progress Dialog
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("loadDetail", response);
                listDetail = new ArrayList<ListDetailQ>();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {
                        nodatad.setVisibility(View.VISIBLE);
                        recyclerViewDetail.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String wmtid = object.getString("wmmid");
                            String mt_lot = object.getString("mt_lot");
                            String mt_cd = object.getString("mt_cd");
                            String mt_no = object.getString("mt_no");
                            String gr_qty = object.getString("gr_qty").replace("null", "");
                            String bb_no = object.getString("bb_no");
                            listDetail.add(new ListDetailQ(false, wmtid, mt_lot, mt_cd, mt_no, gr_qty, bb_no, QCCode));
                        }
                        nodatad.setVisibility(View.GONE);
                        recyclerViewDetail.setVisibility(View.VISIBLE);
                        buidDetail();
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    private void buidDetail() {
        adapterDetailQ = new AdapterDetailQ(listDetail);
        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(CompositeCheckEAActivity.this));
        recyclerViewDetail.setHasFixedSize(true);
        recyclerViewDetail.setAdapter(adapterDetailQ);
        adapterDetailQ.setOnItemClickListener(new AdapterDetailQ.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onQCClick(int position) {
                Intent intent = new Intent(CompositeCheckEAActivity.this, CheckEaActivity.class);
                intent.putExtra("ML_LOT", listDetail.get(position).getMt_lot());
                intent.putExtra("MLNO", listDetail.get(position).getMt_cd());

                intent.putExtra("item_vcd", listDetail.get(position).getQcCode());
                intent.putExtra("Qty", listDetail.get(position).getGr_qty());
                startActivity(intent);
            }
        });
    }

    //Search list QC fist
    private void getJsonQC(String url) {
        Log.d("getOQC", url);
        progressDialog = new ProgressDialog(CompositeCheckEAActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getOQC", response);
                listCheckQC = new ArrayList<ListCheckQC>();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        nodata.setVisibility(View.VISIBLE);
                        recyclerViewOqc.setVisibility(View.GONE);
                        //BaseApp.sendWarning(null, "Data don't have!!!", CompositeCheckEAActivity.this);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            listCheckQC.add(new ListCheckQC(false,
                                    object.getString("wmtid"),
                                    object.getString("date"),
                                    object.getString("mt_cd"), /*mt_lot*/
                                    object.getString("mt_no"),
                                    object.getString("real_qty"),
                                    object.getString("gr_qty"),
                                    object.getString("bbmp_sts_cd").replace("null", "").replace("\"", "").replace("[", "").replace("]", ""),
                                    object.getString("lct_cd").replace("null", ""),
                                    object.getString("bb_no"),
                                    object.getString("sl_tru_ng").replace("null", ""),
                                    QCCode));
                        }
                        nodata.setVisibility(View.GONE);
                        recyclerViewOqc.setVisibility(View.VISIBLE);
                        buildRV();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    BaseApp.sendWarning("Error!!!", "The json error:" + e.toString(), CompositeCheckEAActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                BaseApp.sendWarning("Error !!!", "The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    private void buildRV() {
        adapterOqc = new AdapterItemOQC(listCheckQC);
        recyclerViewOqc.setLayoutManager(new LinearLayoutManager(CompositeCheckEAActivity.this));
        recyclerViewOqc.setHasFixedSize(true);
        recyclerViewOqc.setAdapter(adapterOqc);

        adapterOqc.setOnItemClickListener(new AdapterItemOQC.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                maLot = listCheckQC.get(position).getMt_cd();
                myPosiion = position;
                popDetai(position);
            }

            //                Intent intent = new Intent(CompositeCheckEAActivity.this, CheckEaQCActivity.class);
            //                intent.putExtra("item_vcd",listCheckQC.get(position).getQcCode());
            //                intent.putExtra("MLNO",listCheckQC.get(position).getMt_cd());
            //                intent.putExtra("Qty",listCheckQC.get(position).getGr_qty());
            //                startActivity(intent);

            @Override
            public void onAddNGClick(int position) {
                myPosiion = position;
                popAddNG(position);
            }

            @Override
            public void onMergeClick(int position) {
                myPosiion = position;
                popMerge(position);
            }

            @Override
            public void onDeleteClick(final int position) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CompositeCheckEAActivity.this, R.style.AlertDialogCustom);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Warning!!!");
                alertDialog.setMessage("Are you sure Delete: " + listCheckQC.get(position).getMt_cd()); //"The data you entered does not exist on the server !!!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = BaseApp.isHostting() + "/TIMS/CancelEA?id=" + listCheckQC.get(position).getWmtid();
                        sendJsonCancel(url);
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialog.show();
            }

            @Override
            public void onScanClick(int position) {
//                Toast.makeText(CompositeCheckEAActivity.this, "sss" + position, Toast.LENGTH_SHORT).show();

                myPosiion = position;
                scanBobbin(K_edit); // chen Bibi
            }

            @Override
            public void onInputTextClick(int position) {
                //Toast.makeText(CompositeCheckEAActivity.this, "aaa" + position, Toast.LENGTH_SHORT).show();
                myPosiion = position;
                inputData(K_edit); // chen Bibi
            }

            @Override
            public void onDivideClick(int position) {
                popDivide(position);
            }
        });
    }

    // Divide popup
    private void popDivide(final int position) {
        Rect displayRectangle = new Rect();
        Window window = CompositeCheckEAActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(CompositeCheckEAActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_divide_click, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        ImageView btn_close = dialogView.findViewById(R.id.img_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        //init
        recyclerViewDivide = dialogView.findViewById(R.id.recyclerViewDivide);
        tv_cont = dialogView.findViewById(R.id.tv_cont);
        tv_qty_s = dialogView.findViewById(R.id.tv_qty_s);
        Button btn_divi = dialogView.findViewById(R.id.btn_divi);
        nodatadivide = dialogView.findViewById(R.id.nodatadivide);
        qty_dev = dialogView.findViewById(R.id.qty_dev);

        FloatingActionButton fab_save = dialogView.findViewById(R.id.fab_save);

        tv_cont.setText(listCheckQC.get(position).getBb_no());
        tv_qty_s.setText(listCheckQC.get(position).getGr_qty());

        searchDivide(position);

        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDevide();

            }
        });

        btn_divi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_qty_s.getText().toString().length() > 0) {
                    if (Integer.parseInt(tv_qty_s.getText().toString()) > 0) {
                        saveDivide(listCheckQC.get(position).getBb_no(), alertDialog);
                    } else {
                        AlerError.Baoloi("Quantity is '0'.", CompositeCheckEAActivity.this);
                    }
                }
            }
        });

        alertDialog.show();
    }

    // Divide search
    private void editDevide() {
        //http://messhinsungcntvina.com:83/TIMS/change_gr_dv?value_new=1190,1191&value_old=1190,1191&wmtid=654,655

        if (listDivideold == null || listDivide == null) {
            AlerError.Baoloi("no item", CompositeCheckEAActivity.this);
            return;
        }
        String lWidold = "";
        String lQtyold = "";
        String lQty = "";

        for (int i = 0; i < listDivideold.size(); i++) {
            lWidold = lWidold + "," + listDivideold.get(i).getWmtid();
            lQtyold = lQtyold + "," + listDivideold.get(i).getGr_qty();
        }
        for (int i = 0; i < listDivide.size(); i++) {
            lQty = lQty + "," + listDivide.get(i).getGr_qty();
        }

        if (lWidold.length() > 0 && lQtyold.length() > 0 && lQty.length() > 0) {
            String url = BaseApp.isHostting() + "/TIMS/change_gr_dv?value_new=" + lQty.substring(1) +
                    "&value_old=" + lQtyold.substring(1) + "&wmtid=" + lWidold.substring(1);
            editJsonDevide(url, lQty.substring(1), lQtyold.substring(1), lWidold.substring(1));
        } else {
            AlerError.Baoloi("no item", CompositeCheckEAActivity.this);
        }
    }

    private void editJsonDevide(String url, final String value_new, final String value_old, final String wmtid) {
        Log.e("editJsonDevide", url);
        progressDialog = new ProgressDialog(CompositeCheckEAActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("editJsonDevide", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("result")) {
                        Toast.makeText(CompositeCheckEAActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        searchJsonDivide(urlsearchJsonDivide);
                        return;
                    } else {
                        AlerError.Baoloi("error: " + jsonObject.getString("message"), CompositeCheckEAActivity.this);
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
                    progressDialog.dismiss();
                }
                progressDialog.cancel();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
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
                params.put("value_new", value_new);
                params.put("value_old", value_old);
                params.put("wmtid", wmtid);
                return params;
            }
        };
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjRequest);
    }

    // Divide search
    private void searchDivide(int position) {
        String url = BaseApp.isHostting() + "/TIMS/ds_mapping_sta?mt_cd=" +
                listCheckQC.get(position).getMt_cd()
                + "&_search=false&page=1&sidx=&sord=asc";
        searchJsonDivide(url);
    }

    // Divide search json
    private void searchJsonDivide(String url) {
        urlsearchJsonDivide = url;
        Log.d("searchJsonDivide", url);
        dialog.show(); // Display Progress Dialog
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("searchJsonDivide", response);
                listDivide = new ArrayList<ListDivide>();
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {
                        nodatadivide.setVisibility(View.VISIBLE);
                        recyclerViewDivide.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String wmtid = object.getString("wmtid");
                            String mt_cd = object.getString("mt_cd");
                            String mt_no = object.getString("mt_no");
                            String gr_qty = object.getString("gr_qty").replace("null", "");
                            String bb_no = object.getString("bb_no");
                            String sl_tru_ng = object.getString("sl_tru_ng");
                            String real_qty = object.getString("real_qty");

                            listDivide.add(new ListDivide(wmtid, mt_cd, mt_no, gr_qty, bb_no, sl_tru_ng, real_qty));
                        }
                        nodatadivide.setVisibility(View.GONE);
                        recyclerViewDivide.setVisibility(View.VISIBLE);
                        buildDivide();
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    private void buildDivide() {
        adapterDivide = new AdapterDivide(listDivide);
        recyclerViewDivide.setLayoutManager(new LinearLayoutManager(CompositeCheckEAActivity.this));
        recyclerViewDivide.setHasFixedSize(true);
        recyclerViewDivide.setAdapter(adapterDivide);

        listDivideold = listDivide;

        adapterDivide.setOnItemClickListener(new AdapterDivide.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onADDClick(int position) {
                myPosiion = position;
                inputData(K_Divi); // Divi
            }

            @Override
            public void onEditTextClick(int position) {
                inputNumberDialog(position);
            }

            @Override
            public void onScanClick(int position) {
                myPosiion = position;
                scanBobbin(K_Divi); // Divi
            }

        });
    }

    private void inputNumberDialog(final int posi) {
        Rect displayRectangle = new Rect();
        Window window = CompositeCheckEAActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CompositeCheckEAActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Qty Value");
        View dialogView = LayoutInflater.from(this).inflate(R.layout.number_input_layout_ok_cancel, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);
        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        //anh xa
        final EditText input = dialogView.findViewById(R.id.input);
        Button bt_OK = dialogView.findViewById(R.id.in_btn_ok);
        Button bt_Cal = dialogView.findViewById(R.id.in_btn_cancel);

        input.setText(listDivide.get(posi).getGr_qty());

        bt_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = input.getText().toString().trim();
                if (txt.length() > 0) {
                    listDivide.get(posi).setGr_qty(txt);
                    adapterDivide.notifyDataSetChanged();
                    alertDialog.cancel();
                    return;
                } else {
                    input.setError("Please enter value ");
                    input.requestFocus();
                    return;
                }
            }
        });

        bt_Cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    // Divide save
    private void saveDivide(String bb_no, AlertDialog alertDialog) {
        if (qty_dev.getText().toString().length() > 0) {
            String url = BaseApp.isHostting() + "/TIMS/Decevice_sta?bb_no=" +
                    bb_no + "&number_dv=" + qty_dev.getText().toString();
            saveJsonDivide(url, alertDialog);
        } else {
            AlerError.Baoloi("input divide number!", CompositeCheckEAActivity.this);
        }
    }

    private void saveJsonDivide(String url, final AlertDialog alertDialog) {
        Log.d("saveJsonDivide", url);
        dialog.show(); // Display Progress Dialog
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("saveJsonDivide", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("result")) {
                        Toast.makeText(CompositeCheckEAActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    } else {
                        AlerError.Baoloi("error!", CompositeCheckEAActivity.this);
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    private void popAddNG(final int position) {
        Rect displayRectangle = new Rect();
        Window window = CompositeCheckEAActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(CompositeCheckEAActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_add_ng, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        ImageView btn_close = dialogView.findViewById(R.id.img_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        recyclerAddNG = dialogView.findViewById(R.id.recycler_view_add_ng);
        tv_mlno = dialogView.findViewById(R.id.tv_mlno);
        tv_mtno = dialogView.findViewById(R.id.tv_mtno);
        Button btn_searh = dialogView.findViewById(R.id.btn_searh);
        qty_ng = dialogView.findViewById(R.id.qty_ng);
        Button btn_save = dialogView.findViewById(R.id.btn_save);
        nodatap = dialogView.findViewById(R.id.nodatap);
        search();
        ImageView img_scan_ng = dialogView.findViewById(R.id.img_scan_ng);
        img_scan_ng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBobbin(ID_ADDNG);
            }
        });

        btn_searh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFunction(listCheckQC.get(position).getMt_cd(), alertDialog);
            }
        });

        alertDialog.show();
    }

    private void popMerge(final int position) {
        Rect displayRectangle = new Rect();
        Window window = CompositeCheckEAActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(CompositeCheckEAActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_mergi, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);

        final AlertDialog alertDialog = builder.create();
        ImageView btn_close = dialogView.findViewById(R.id.img_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        recycler_view_merge = dialogView.findViewById(R.id.recycler_view_merge);
        tv_con_t = dialogView.findViewById(R.id.tv_con_t);
        tv_mlno_m = dialogView.findViewById(R.id.tv_mlno_m);
        tv_mtno_m = dialogView.findViewById(R.id.tv_mtno_m);
        ImageView img_scan_me = dialogView.findViewById(R.id.img_scan_me);
        img_scan_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBobbin(ID_MG); /// merge
            }
        });
        Button btn_searh = dialogView.findViewById(R.id.btn_searh);
        qty_m_a = dialogView.findViewById(R.id.qty_m_a);
        Button btn_save = dialogView.findViewById(R.id.btn_save);
        nodatap_m = dialogView.findViewById(R.id.nodatap);
        searchmergi();

        btn_searh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchmergi();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMergeFunction(listCheckQC.get(position).getMt_cd(), alertDialog);
            }
        });

        alertDialog.show();
    }

    private void saveFunction(String mt_cd, AlertDialog alertDialog) {
        String wtid = "";
        for (int i = 0; i < listNG.size(); i++) {
            if (listNG.get(i).isCheck()) {
                wtid = wtid + "," + listNG.get(i).getWmtid();
            }
        }
        if (wtid.length() == 0) {
            AlerError.Baoloi("select rows", CompositeCheckEAActivity.this);
        } else {
            if (qty_ng.getText().toString().length() > 0) {
                String url = BaseApp.isHostting() + "/TIMS/Gop_NG?wmtid=" + wtid.substring(1) +
                        "&soluong=" + qty_ng.getText().toString() +
                        "&mt_cd=" + mt_cd;
                saveNgList(url, alertDialog);
            } else {
                AlerError.Baoloi("input Qty", CompositeCheckEAActivity.this);
            }
        }
    }

    private void saveMergeFunction(String mt_cd, AlertDialog alertDialog) {
        String wtid = "";
        for (int i = 0; i < listMerge.size(); i++) {
            if (listMerge.get(i).isCheck()) {
                wtid = wtid + "," + listMerge.get(i).getWmtid();
            }
        }
        if (wtid.length() == 0) {
            AlerError.Baoloi("select rows", CompositeCheckEAActivity.this);
        } else {
            if (qty_m_a.getText().toString().length() > 0) {
                //http://messhinsungcntvina.com:83/TIMS/Gop_OK?wmtid=514,496,495&soluong=1&mt_cd=LJ63-16628A-NQ20201109092616000001
                String url = BaseApp.isHostting() + "/TIMS/Gop_OK?wmtid=" + wtid.substring(1) +
                        "&soluong=" + qty_m_a.getText().toString() +
                        "&mt_cd=" + mt_cd;
                saveJsonmerge(url, alertDialog);
            } else {
                AlerError.Baoloi("input Qty", CompositeCheckEAActivity.this);
            }

        }
    }

    private void searchmergi() {
        String url = BaseApp.isHostting() +
                "/TIMS/Get_OKReason?page=1&rows=100&sidx=&sord=asc&_search=false&mt_cd=" +
                tv_mlno_m.getText().toString() + "&mt_no=" + tv_mtno_m.getText().toString() +
                "&bb_no=" + tv_con_t.getText().toString() + "&id_actual=" + id_actual +
                "&mt_lot=" + listCheckQC.get(myPosiion).getMt_cd();
        loadJsonmerge(url);
    }

    private void saveJsonmerge(String url, final AlertDialog alertDialog) {
        Log.d("savemerge", url);
        dialog.show(); // Display Progress Dialog
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("savemerge", response);
                listNG = new ArrayList<ListNG>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("result")) {
                        Toast.makeText(CompositeCheckEAActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        startActivity(getIntent());
                    } else {
                        AlerError.Baoloi("error! " + jsonObject.getString("message"), CompositeCheckEAActivity.this);
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    private void loadJsonmerge(String url) {
        Log.d("loadJsonmerge", url);
        dialog.show(); // Display Progress Dialog
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("loadJsonmerge", response);
                listMerge = new ArrayList<ListMerge>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    if (jsonArray.length() == 0) {
                        nodatap_m.setVisibility(View.VISIBLE);
                        recycler_view_merge.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String wmtid = object.getString("wmtid");
                            String mt_cd = object.getString("mt_cd");
                            String mt_no = object.getString("mt_no");
                            String gr_qty = object.getString("gr_qty").replace("null", "");
                            String bb_no = object.getString("bb_no");
                            String at_no = object.getString("at_no");

                            listMerge.add(new ListMerge(false, wmtid, mt_cd, mt_no, gr_qty, bb_no, at_no));
                        }
                        nodatap_m.setVisibility(View.GONE);
                        recycler_view_merge.setVisibility(View.VISIBLE);
                        buidMergeList();
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    private void search() {
        String url = BaseApp.isHostting() +
                "/TIMS/Get_NGPO?page=1&rows=100&sidx=mt_cd&sord=asc&_search=true&mt_cd=" +
                tv_mlno.getText().toString() + "&mt_no=" + tv_mtno.getText().toString() +
                "&bb_no=&id_actual=" + id_actual;
        loadJsonNgList(url);
    }

    private void buidMergeList() {
        adpMer = new AdapterMerge(listMerge);
        recycler_view_merge.setLayoutManager(new LinearLayoutManager(CompositeCheckEAActivity.this));
        recycler_view_merge.setHasFixedSize(true);
        recycler_view_merge.setAdapter(adpMer);

        adpMer.setOnItemClickListener(new AdapterMerge.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onCheckClick(int position, CheckBox checkBox) {
                listMerge.get(position).setCheck(checkBox.isChecked());
            }
        });
    }

    private void saveNgList(String url, final AlertDialog alertDialog) {
        Log.d("saveNgList", url);
        dialog.show(); // Display Progress Dialog
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("saveNgList", response);
                listNG = new ArrayList<ListNG>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("result")) {
                        Toast.makeText(CompositeCheckEAActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        startActivity(getIntent());
                    } else {
                        AlerError.Baoloi("error!", CompositeCheckEAActivity.this);
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    private void loadJsonNgList(String url) {
        Log.d("loadJsonNgList", url);
        dialog.show(); // Display Progress Dialog
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("loadJsonNgList", response);
                listNG = new ArrayList<ListNG>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    if (jsonArray.length() == 0) {
                        nodatap.setVisibility(View.VISIBLE);
                        recyclerAddNG.setVisibility(View.GONE);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String wmtid = object.getString("wmtid");
                            String mt_cd = object.getString("mt_cd");
                            String mt_no = object.getString("mt_no");
                            String gr_qty = object.getString("gr_qty").replace("null", "");
                            listNG.add(new ListNG(false, wmtid, mt_cd, mt_no, gr_qty));
                        }
                        nodatap.setVisibility(View.GONE);
                        recyclerAddNG.setVisibility(View.VISIBLE);
                        buidList();
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    private void buidList() {
        adpNG = new AdapterNG(listNG);
        recyclerAddNG.setLayoutManager(new LinearLayoutManager(CompositeCheckEAActivity.this));
        recyclerAddNG.setHasFixedSize(true);
        recyclerAddNG.setAdapter(adpNG);

        adpNG.setOnItemClickListener(new AdapterNG.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onCheckClick(int position, CheckBox checkBox) {
                listNG.get(position).setCheck(checkBox.isChecked());
            }
        });
    }

    private void sendJsonCancel(String url) {
        Log.d("sendJsonCancel", url);
//        progressDialog = new ProgressDialog(CompositeCheckEAActivity.this);
//        progressDialog.setMessage("Loading..."); // Setting Message
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        dialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sendJsonCancel", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("result")) {
                        if (!jsonObject.getBoolean("result")) {
                            dialog.dismiss();
                            AlerError.Baoloi(jsonObject.getString("message"), CompositeCheckEAActivity.this);
                            return;
                        } else {
                            dialog.dismiss();
                            Toast.makeText(CompositeCheckEAActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            startActivity(getIntent());
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(CompositeCheckEAActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    AlerError.Baoloi("Could not connect to server", CompositeCheckEAActivity.this);
                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                dialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), CompositeCheckEAActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeCheckEAActivity.this);
        requestQueue.add(stringRequest);
    }

    private static class AdapterDetailQ extends RecyclerView.Adapter<AdapterDetailQ.DetailQViewHolder> {
        private ArrayList<ListDetailQ> item;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public AdapterDetailQ.DetailQViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_detatl_qc,
                    viewGroup, false);
            AdapterDetailQ.DetailQViewHolder evh = new AdapterDetailQ.DetailQViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterDetailQ.DetailQViewHolder vh, int i) {
            ListDetailQ currentItem = item.get(i);

            vh.tv_con_ten.setText(currentItem.getBb_no());
            vh.tv_mtno.setText(currentItem.getMt_no());
            vh.tv_mlno.setText(currentItem.getMt_cd());
            vh.tv_qty.setText(currentItem.getGr_qty());
            vh.tv_stt_fg.setText(i + 1 + "");
            vh.tv_qc_check.setText(currentItem.getQcCode());

        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);

            void onQCClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class DetailQViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_con_ten, tv_qty, tv_mlno, tv_mtno, tv_stt_fg, tv_qc_check;

            public DetailQViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                tv_con_ten = itemView.findViewById(R.id.tv_con_ten);
                tv_qty = itemView.findViewById(R.id.tv_qty);//qty
                tv_mlno = itemView.findViewById(R.id.tv_mlno);//ml
                tv_mtno = itemView.findViewById(R.id.tv_mtno);//mt
                tv_qc_check = itemView.findViewById(R.id.tv_qc_check); //qc
                tv_stt_fg = itemView.findViewById(R.id.tv_stt_fg); //stt

                tv_qc_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onQCClick(position);
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
                                listener.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }

        public AdapterDetailQ(ArrayList<ListDetailQ> listWorker) {
            item = listWorker;
        }
    }

    private static class AdapterItemOQC extends RecyclerView.Adapter<AdapterItemOQC.ItemOQCViewHolder> {
        private ArrayList<ListCheckQC> item;
        private AdapterItemOQC.OnItemClickListener mListener;

        @NonNull
        @Override
        public AdapterItemOQC.ItemOQCViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_check_tab,
                    viewGroup, false);
            AdapterItemOQC.ItemOQCViewHolder evh = new AdapterItemOQC.ItemOQCViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterItemOQC.ItemOQCViewHolder vh, int i) {
            ListCheckQC currentItem = item.get(i);

            vh.tv_qty_r.setText(currentItem.getGr_qty());
            vh.tv_contei.setText(currentItem.getBb_no());
            vh.tv_mlno.setText(currentItem.getMt_cd());
            vh.tv_qty.setText(currentItem.getReal_qty());
            vh.tv_stt_fg.setText(i + 1 + "");

            // vh.check.setChecked(currentItem.isCheck());
        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);

            void onAddNGClick(int position);

            void onMergeClick(int position);

            void onDeleteClick(int position);

            void onScanClick(int position);

            void onInputTextClick(int position);

            void onDivideClick(int position);
        }

        public void setOnItemClickListener(AdapterItemOQC.OnItemClickListener listener) {
            mListener = listener;
        }

        public static class ItemOQCViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_contei, tv_mlno, tv_qty_r, tv_qty, tv_stt_fg;
            //CheckBox check;
            public TextView btn_merge, btn_addNG, btn_divide;
            public ImageView img_del_fg, img_chabobi;
            public LinearLayout linear_bbno;

            public ItemOQCViewHolder(@NonNull View itemView, final AdapterItemOQC.OnItemClickListener listener) {
                super(itemView);
                img_chabobi = itemView.findViewById(R.id.img_chabobi);
                tv_contei = itemView.findViewById(R.id.tv_con_ten);
                tv_mlno = itemView.findViewById(R.id.tv_mlno);//name
                tv_qty_r = itemView.findViewById(R.id.tv_qty_r);//id
                tv_qty = itemView.findViewById(R.id.tv_qty); //a
                tv_stt_fg = itemView.findViewById(R.id.tv_stt_fg); //d
                btn_merge = itemView.findViewById(R.id.btn_merge);
                img_del_fg = itemView.findViewById(R.id.img_del_fg);
                btn_addNG = itemView.findViewById(R.id.btn_addNG);
                btn_divide = itemView.findViewById(R.id.btn_divide);

                linear_bbno = itemView.findViewById(R.id.linear_bbno);
                btn_divide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onDivideClick(position);
                            }
                        }
                    }
                });
                tv_contei.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onInputTextClick(position);
                            }
                        }
                    }
                });

                img_chabobi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onScanClick(position);
                            }
                        }
                    }
                });

                img_del_fg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onDeleteClick(position);
                            }
                        }
                    }
                });
                btn_addNG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onAddNGClick(position);
                            }
                        }
                    }
                });
                btn_merge.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onMergeClick(position);
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
                                listener.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }

        public AdapterItemOQC(ArrayList<ListCheckQC> listWorker) {
            item = listWorker;
        }
    }

    private class ListCheckQC {
        boolean check;
        String wmtid, date, mt_cd, mt_no, real_qty, gr_qty, bbmp_sts_cd, lct_cd, bb_no, sl_tru_ng, qcCode;

        public ListCheckQC(boolean check, String wmtid, String date, String mt_cd, String mt_no,
                           String real_qty, String gr_qty, String bbmp_sts_cd, String lct_cd, String bb_no,
                           String sl_tru_ng, String qcCode) {
            this.check = check;
            this.wmtid = wmtid;
            this.date = date;
            this.mt_cd = mt_cd;
            this.mt_no = mt_no;
            this.real_qty = real_qty;
            this.gr_qty = gr_qty;
            this.bbmp_sts_cd = bbmp_sts_cd;
            this.lct_cd = lct_cd;
            this.bb_no = bb_no;
            this.sl_tru_ng = sl_tru_ng;
            this.qcCode = qcCode;
        }

        public boolean isCheck() {
            return check;
        }

        public String getWmtid() {
            return wmtid;
        }

        public String getDate() {
            return date;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public String getMt_no() {
            return mt_no;
        }

        public String getReal_qty() {
            return real_qty;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public String getBbmp_sts_cd() {
            return bbmp_sts_cd;
        }

        public String getLct_cd() {
            return lct_cd;
        }

        public String getBb_no() {
            return bb_no;
        }

        public String getSl_tru_ng() {
            return sl_tru_ng;
        }

        public String getQcCode() {
            return qcCode;
        }
    }

    private static class AdapterNG extends RecyclerView.Adapter<AdapterNG.NGViewHolder> {
        private ArrayList<ListNG> item;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public AdapterNG.NGViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add_ng,
                    viewGroup, false);
            AdapterNG.NGViewHolder evh = new AdapterNG.NGViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterNG.NGViewHolder vh, int i) {
            ListNG currentItem = item.get(i);

            vh.ichek.setChecked(currentItem.isCheck());
            vh.tv_mtno.setText(currentItem.getMt_no());
            vh.tv_mlno.setText(currentItem.getMt_cd());
            vh.tv_qty.setText(currentItem.getGr_qty());
            vh.tv_stt_fg.setText(i + 1 + "");

        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);

            void onCheckClick(int position, CheckBox checkBox);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class NGViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_mlno, tv_mtno, tv_qty, tv_stt_fg;
            CheckBox ichek;

            public NGViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                //check = itemView.findViewById(R.id.check);
                ichek = itemView.findViewById(R.id.ichek);
                tv_mlno = itemView.findViewById(R.id.tv_mlno);//ml
                tv_mtno = itemView.findViewById(R.id.tv_mtno);//mt
                tv_qty = itemView.findViewById(R.id.tv_qty); //sl
                tv_stt_fg = itemView.findViewById(R.id.tv_stt_fg); //stt

                ichek.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onCheckClick(position, ichek);
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
                                listener.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }

        public AdapterNG(ArrayList<ListNG> listWorker) {
            item = listWorker;
        }
    }

    private class ListNG {
        String wmtid, mt_cd, mt_no, gr_qty;
        boolean check;

        public ListNG(boolean check, String wmtid, String mt_cd, String mt_no, String gr_qty) {
            this.check = check;
            this.wmtid = wmtid;
            this.mt_cd = mt_cd;
            this.mt_no = mt_no;
            this.gr_qty = gr_qty;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public String getWmtid() {
            return wmtid;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public String getMt_no() {
            return mt_no;
        }

        public String getGr_qty() {
            return gr_qty;
        }
    }

    private static class AdapterDivide extends RecyclerView.Adapter<AdapterDivide.DivideViewHolder> {
        private ArrayList<ListDivide> item;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public AdapterDivide.DivideViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_divi_d,
                    viewGroup, false);
            AdapterDivide.DivideViewHolder evh = new AdapterDivide.DivideViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterDivide.DivideViewHolder vh, int i) {
            ListDivide currentItem = item.get(i);

            vh.tv_mtno.setText(currentItem.getMt_no());
            vh.tv_ml.setText(currentItem.getMt_cd());
            vh.tv_qty.setText(currentItem.getReal_qty());
            vh.tv_qty_r.setText(currentItem.getGr_qty());

            vh.tv_stt_fg.setText(i + 1 + "");
            if (currentItem.getBb_no().length() == 0) {
                vh.tv_add_con.setText("Add container");
            } else {
                vh.tv_add_con.setText(currentItem.getBb_no());
            }
        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);

            void onADDClick(int position);

            void onEditTextClick(int position);

            void onScanClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class DivideViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_ml, tv_mtno, tv_qty, tv_stt_fg, tv_add_con;
            TextView tv_qty_r;
            ImageView scan_div;

            public DivideViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                tv_ml = itemView.findViewById(R.id.tv_ml);
                tv_mtno = itemView.findViewById(R.id.tv_mtno);//mt
                tv_qty = itemView.findViewById(R.id.tv_qty); //sl
                tv_stt_fg = itemView.findViewById(R.id.tv_stt_fg); //stt
                tv_qty_r = itemView.findViewById(R.id.tv_qty_r); //edt
                tv_add_con = itemView.findViewById(R.id.tv_add_con); //bb
                scan_div = itemView.findViewById(R.id.scan_div);
                scan_div.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onScanClick(position);
                            }
                        }
                    }
                });
                tv_qty_r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onEditTextClick(position);
                            }
                        }
                    }
                });

                tv_add_con.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onADDClick(position);
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
                                listener.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }

        public AdapterDivide(ArrayList<ListDivide> listWorker) {
            item = listWorker;
        }
    }

    private static class AdapterMerge extends RecyclerView.Adapter<AdapterMerge.MergeViewHolder> {
        private ArrayList<ListMerge> item;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public AdapterMerge.MergeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_merge_a,
                    viewGroup, false);
            AdapterMerge.MergeViewHolder evh = new AdapterMerge.MergeViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterMerge.MergeViewHolder vh, int i) {
            ListMerge currentItem = item.get(i);

            vh.ichek.setChecked(currentItem.isCheck());
            vh.tv_mtno.setText(currentItem.getMt_no());
            vh.tv_mlno.setText(currentItem.getMt_cd());
            vh.tv_qty.setText(currentItem.getGr_qty());
            vh.tv_stt_fg.setText(i + 1 + "");
            vh.tv_con_ten.setText(currentItem.getBb_no());
            vh.tv_pono.setText(currentItem.getAt_no());
        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);

            void onCheckClick(int position, CheckBox checkBox);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class MergeViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_con_ten, tv_mlno, tv_mtno, tv_qty, tv_stt_fg, tv_pono;
            CheckBox ichek;

            public MergeViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                tv_con_ten = itemView.findViewById(R.id.tv_con_ten);
                ichek = itemView.findViewById(R.id.ichek);
                tv_mlno = itemView.findViewById(R.id.tv_mlno);//ml
                tv_mtno = itemView.findViewById(R.id.tv_mtno);//mt
                tv_qty = itemView.findViewById(R.id.tv_qty); //sl
                tv_stt_fg = itemView.findViewById(R.id.tv_stt_fg); //stt
                tv_pono = itemView.findViewById(R.id.tv_pono); //stt

                ichek.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onCheckClick(position, ichek);
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
                                listener.onItemClick(position);
                            }
                        }
                    }
                });
            }
        }

        public AdapterMerge(ArrayList<ListMerge> listWorker) {
            item = listWorker;
        }
    }

    private class ListMerge {
        boolean check;
        String wmtid, mt_cd, mt_no, gr_qty, bb_no, at_no;

        public ListMerge(boolean check, String wmtid, String mt_cd, String mt_no, String gr_qty, String bb_no, String at_no) {
            this.check = check;
            this.wmtid = wmtid;
            this.mt_cd = mt_cd;
            this.mt_no = mt_no;
            this.gr_qty = gr_qty;
            this.bb_no = bb_no;
            this.at_no = at_no;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public boolean isCheck() {
            return check;
        }

        public String getWmtid() {
            return wmtid;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public String getMt_no() {
            return mt_no;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public String getBb_no() {
            return bb_no;
        }

        public String getAt_no() {
            return at_no;
        }
    }

    private class ListDetailQ {
        boolean check;
        String wmtid, mt_lot, mt_cd, mt_no, gr_qty, bb_no, qcCode;

        public ListDetailQ(boolean check, String wmtid, String mt_lot, String mt_cd, String mt_no, String gr_qty, String bb_no, String qcCode) {
            this.check = check;
            this.wmtid = wmtid;
            this.mt_lot = mt_lot;
            this.mt_cd = mt_cd;
            this.mt_no = mt_no;
            this.gr_qty = gr_qty;
            this.bb_no = bb_no;
            this.qcCode = qcCode;
        }

        public boolean isCheck() {
            return check;
        }

        public String getWmtid() {
            return wmtid;
        }

        public String getMt_lot() {
            return mt_lot;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public String getMt_no() {
            return mt_no;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public String getBb_no() {
            return bb_no;
        }

        public String getQcCode() {
            return qcCode;
        }
    }

    private class ListDivide {
        String wmtid, mt_cd, mt_no, gr_qty, bb_no, sl_tru_ng, real_qty;

        public ListDivide(String wmtid, String mt_cd, String mt_no, String gr_qty, String bb_no, String sl_tru_ng, String real_qty) {
            this.wmtid = wmtid;
            this.mt_cd = mt_cd;
            this.mt_no = mt_no;
            this.gr_qty = gr_qty;
            this.bb_no = bb_no;
            this.sl_tru_ng = sl_tru_ng;
            this.real_qty = real_qty;
        }

        public String getWmtid() {
            return wmtid;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public String getMt_no() {
            return mt_no;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public String getBb_no() {
            return bb_no;
        }

        public String getSl_tru_ng() {
            return sl_tru_ng;
        }

        public String getReal_qty() {
            return real_qty;
        }

        public void setGr_qty(String gr_qty) {
            this.gr_qty = gr_qty;
        }

        public void setBb_no(String bb_no) {
            this.bb_no = bb_no;
        }

        public void setSl_tru_ng(String sl_tru_ng) {
            this.sl_tru_ng = sl_tru_ng;
        }

        public void setReal_qty(String real_qty) {
            this.real_qty = real_qty;
        }
    }
}