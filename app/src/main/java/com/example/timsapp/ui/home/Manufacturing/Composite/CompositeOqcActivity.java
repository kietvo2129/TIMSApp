package com.example.timsapp.ui.home.Manufacturing.Composite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.timsapp.ui.home.Manufacturing.Composite.QC.CheckQCActivity;
import com.example.timsapp.ui.home.Manufacturing.Worker.WorkerAddJobActivity;
import com.example.timsapp.ui.home.MappingOQC.MappingOQCActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class CompositeOqcActivity extends AppCompatActivity {

    private TextView nodata;
    private RecyclerView recyclerViewOqc;
    private FloatingActionButton fab_add, fab_in, fab_can;
    private String type;
    private String RollName;
    private String id_actual;
    private String staff_id;
    private ProgressDialog progressDialog;
    private AdapterItemOQC adapterOqc;
    private ArrayList<ListOQC> listOQC;
    private String QCCode;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composite_oqc);
        setTitle("OQC");
        nodata = findViewById(R.id.nodata);
        recyclerViewOqc = findViewById(R.id.recyclerViewOqc);
        fab_add = findViewById(R.id.fab);
        fab_in = findViewById(R.id.input);
        fab_can = findViewById(R.id.scan);

        type = getIntent().getStringExtra("Type");
        RollName = getIntent().getStringExtra("RollName");
        id_actual = getIntent().getStringExtra("id_actual");
        staff_id = getIntent().getStringExtra("staff_id");
        QCCode = getIntent().getStringExtra("QCCode");

        dialog = new ProgressDialog(CompositeOqcActivity.this, R.style.AlertDialogCustom);
        fchide();
//        if (type.equals("OQC")) {
//            String url = BaseApp.isHostting() + "/TIMS/Getmt_lotOQC?id_actual=" + id_actual + "&staff_id=" + staff_id;
//            getOQC(url);
//        } else {
//
//        }
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
                inputData();
            }
        });
        fab_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fchide();
                scanBobbin();
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
        //http://messhinsungcntvina.com:83/TIMS/getmt_date_web_auto?id_actual=58&staff_id=90255
        //[[
        //{
        //"wmtid": 412,
        //"date": "2020-11-07",
        //"mt_cd": "LJ63-19131A-DD20201107105142000001",
        //"mt_no": "LJ63-19131A-DD",
        //"real_qty": 1500,
        //"gr_qty": 0,
        //"gr_qty1": 0,
        //"bbmp_sts_cd": ["Init"],
        //"mt_qrcode": "LJ63-19131A-DD20201107105142000001",
        //"lct_cd": null,
        //"bb_no": "BOBBIN-AUTO-20201102143711000006",
        //"mt_barcode": "LJ63-19131A-DD20201107105142000001",
        //"chg_dt": "/Date(1604721154000)/",
        //"sl_tru_ng": 1500
        //}
        //]

        //oqc
        //http://messhinsungcntvina.com:83/TIMS/Getmt_lotOQC?id_actual=59&staff_id=90242
        //[{"wmtid":423,
        // "bb_no":"BOBBIN-AUTO-20201102143711000006",
        // "mt_no":"LJ63-19131A-NQ",
        // "mt_cd":"LJ63-19131A-NQ20201107111623000001-DV2",
        // "gr_qty":300,
        // "count_ng":0}]
    }

    @Override
    protected void onResume() {
        super.onResume();
//        String url = BaseApp.isHostting() + "/TIMS/Getmt_lotOQC?id_actual=" + id_actual + "&staff_id=" + staff_id;
//        getOQC(url);
        if (type.equals("OQC")) {
            String url = BaseApp.isHostting() + "/TIMS/Getmt_lotOQC?id_actual=" + id_actual + "&staff_id=" + staff_id;
            getOQC(url);
        } else {

        }
    }

    private void inputData() {
        final Dialog dialog = new Dialog(CompositeOqcActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(CompositeOqcActivity.this).inflate(R.layout.popup_input, null);
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
                    creatingMLno(Containercode.getText().toString()); // input
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    // open scan qr code
    private void scanBobbin() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(CompositeOqcActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                creatingMLno(result.getContents()); //scan
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
        String url = BaseApp.isHostting() + "TIMS/getListQR_oqc?" +
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
                        AlerError.Baoloi(jsonObject.getString("message"), CompositeOqcActivity.this);
                        return;
                    } else {
                        if (jsonObject.getJSONArray("kq").length() == 0) {
                            AlerError.Baoloi("Create false, Please check Container code", CompositeOqcActivity.this);
                        } else {
                            Toast.makeText(CompositeOqcActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            startActivity(getIntent());
                        }
                    }
                } else {
                    Toast.makeText(CompositeOqcActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", CompositeOqcActivity.this);
                dialog.dismiss();
            }
        }

    }

    private class passAndReturn extends AsyncTask<String, Void, String> {
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
                    dialog.dismiss();
                    Toast.makeText(CompositeOqcActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                } else {
                    dialog.dismiss();
                    AlerError.Baoloi(jsonObject.getString("message"), CompositeOqcActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Change Quantity reality false. Please check again.", CompositeOqcActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void getOQC(String url) {
        Log.d("getOQC", url);
        progressDialog = new ProgressDialog(CompositeOqcActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("getOQC", response);
                listOQC = new ArrayList<ListOQC>();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        nodata.setVisibility(View.VISIBLE);
                        recyclerViewOqc.setVisibility(View.GONE);
                        //BaseApp.sendWarning(null, "Data don't have!!!", CompositeOqcActivity.this);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            listOQC.add(new ListOQC(false,
                                    object.getString("wmtid"),
                                    object.getString("bb_no"),
                                    object.getString("mt_no"),
                                    object.getString("mt_cd"),
                                    object.getString("gr_qty"),
                                    object.getString("count_ng"),
                                    QCCode));
                        }
                        nodata.setVisibility(View.GONE);
                        recyclerViewOqc.setVisibility(View.VISIBLE);
                        buildRV();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    BaseApp.sendWarning("Error!!!", "The json error:" + e.toString(), CompositeOqcActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                BaseApp.sendWarning("Error !!!", "The server error:" + error.toString(), CompositeOqcActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(CompositeOqcActivity.this);
        requestQueue.add(stringRequest);
    }

    private void buildRV() {
        adapterOqc = new AdapterItemOQC(listOQC);
        recyclerViewOqc.setLayoutManager(new LinearLayoutManager(CompositeOqcActivity.this));
        recyclerViewOqc.setHasFixedSize(true);
        recyclerViewOqc.setAdapter(adapterOqc);

        adapterOqc.setOnItemClickListener(new AdapterItemOQC.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onButtonQCClick(int position) {
                Intent intent = new Intent(CompositeOqcActivity.this, CheckQCActivity.class);
                intent.putExtra("item_vcd",listOQC.get(position).getQcCode());
                intent.putExtra("MLNO",listOQC.get(position).getMt_cd());
                intent.putExtra("Qty",listOQC.get(position).getGr_qty());

                startActivity(intent);
            }

            @Override
            public void onPassClick(int position) {
                new passAndReturn().execute(BaseApp.isHostting()+"/TIMS/Changests_packing?wmtid=" +
                        listOQC.get(position).wmtid); //pass
            }

            @Override
            public void onReturnClick(int position) {
                new passAndReturn().execute(BaseApp.isHostting()+"/TIMS/Returnsts_packing?wmtid=" +
                        listOQC.get(position).wmtid); //Return
            }

        });
    }

    private static class AdapterItemOQC extends RecyclerView.Adapter<AdapterItemOQC.ItemOQCViewHolder> {
        private ArrayList<ListOQC> item;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public AdapterItemOQC.ItemOQCViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_oqc_tab1,
                    viewGroup, false);
            AdapterItemOQC.ItemOQCViewHolder evh = new AdapterItemOQC.ItemOQCViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterItemOQC.ItemOQCViewHolder vh, int i) {
            ListOQC currentItem = item.get(i);

            vh.tv_qc.setText(currentItem.getQcCode());
            vh.tv_contei.setText(currentItem.getBb_no());
            vh.tv_mlno.setText(currentItem.getMt_cd());
            vh.tv_mtno.setText(currentItem.getMt_no());
            vh.tv_qty.setText(currentItem.getGr_qty());
            vh.tv_stt_fg.setText(i + 1 + "");

            // vh.check.setChecked(currentItem.isCheck());
        }

        @Override
        public int getItemCount() {
            return item.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);

            void onButtonQCClick(int position);

            void onPassClick(int position);

            void onReturnClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class ItemOQCViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_contei, tv_mlno, tv_qc, tv_mtno, tv_qty, tv_stt_fg;
            //CheckBox check;
            public TextView btn_return, btn_pass;

            public ItemOQCViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                //check = itemView.findViewById(R.id.check);
                tv_contei = itemView.findViewById(R.id.tv_contei);
                tv_mlno = itemView.findViewById(R.id.tv_mlno);//name
                tv_qc = itemView.findViewById(R.id.tv_qc);//id
                tv_qty = itemView.findViewById(R.id.tv_qty); //a
                tv_stt_fg = itemView.findViewById(R.id.tv_stt_fg); //d
                tv_mtno = itemView.findViewById(R.id.tv_mtno); //d
                btn_return = itemView.findViewById(R.id.btn_return);
                btn_pass = itemView.findViewById(R.id.btn_pass);

                btn_return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onReturnClick(position);
                            }
                        }
                    }
                });
                btn_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onPassClick(position);
                            }
                        }
                    }
                });
                tv_qc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onButtonQCClick(position);
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

        public AdapterItemOQC(ArrayList<ListOQC> listWorker) {
            item = listWorker;
        }
    }

    private class ListOQC {
        String wmtid, bb_no, mt_no, mt_cd, gr_qty, count_ng;
        boolean check;
        String QcCode;

        public ListOQC(boolean check, String wmtid, String bb_no, String mt_no, String mt_cd,
                       String gr_qty, String count_ng, String QcCode) {
            this.wmtid = wmtid;
            this.check = check;
            this.bb_no = bb_no;
            this.mt_no = mt_no;
            this.mt_cd = mt_cd;
            this.gr_qty = gr_qty;
            this.count_ng = count_ng;
            this.QcCode = QcCode;
        }

        public String getQcCode() {
            return QcCode;
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

        public String getBb_no() {
            return bb_no;
        }

        public String getMt_no() {
            return mt_no;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public String getCount_ng() {
            return count_ng;
        }
    }
}