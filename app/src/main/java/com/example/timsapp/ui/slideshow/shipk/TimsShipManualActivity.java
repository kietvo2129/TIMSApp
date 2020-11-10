package com.example.timsapp.ui.slideshow.shipk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimsShipManualActivity extends AppCompatActivity {
    public static final String KEYID = "KEYID";
    private EditText edt_by, edt_ml, edt_mt;
    private Button btn_ser;
    private TextView tv_ext;
    private Button btn_pik;
    private RecyclerView rv_sh_s;
    private ProgressDialog progressDialog;
    private ArrayList<ListManualTims> listManualTims;
    private ListManualTimsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tims_ship_manual);
        setTitle("Shipping Manual(TIMS)");

        Intent intent = getIntent();
        String ss = intent.getStringExtra(KEYID);
        edt_by = findViewById(R.id.edt_by);
        edt_ml = findViewById(R.id.edt_ml);
        edt_mt = findViewById(R.id.edt_mt);
        btn_ser = findViewById(R.id.btn_ser);
        tv_ext = findViewById(R.id.tv_ext);
        btn_pik = findViewById(R.id.btn_pik);
        rv_sh_s = findViewById(R.id.rv_sh_s);

        serch();
        tv_ext.setText(ss);

        btn_ser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serch();
            }
        });

        btn_pik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pick();
            }
        });
    }

    private void serch() {
        String url = BaseApp.isHostting() + "/TIMS/Get_List_Material_TimsShipping?page=1&rows=5000&sidx=&sord=asc&_search=false&buyer_qr=" +
                edt_by.getText().toString().trim() + "&mt_no=" +
                edt_mt.getText().toString().trim() + "&mt_cd=" +
                edt_ml.getText().toString().trim();
        serchJson(url);
    }

    private void serchJson(String url) {
        Log.d("serchJson", url);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("serchJson", response);
                listManualTims = new ArrayList<ListManualTims>();
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    if (jsonArray.length() == 0) {
                        AlerError.Baoloi("Data don't have!!!", TimsShipManualActivity.this);
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            boolean check = false;
                            listManualTims.add(new ListManualTims(
                                    check,
                                    false,
                                    object.getString("wmtid"),
                                    object.getString("mt_cd"),
                                    object.getString("mt_no"),
                                    object.getString("bb_no"),
                                    object.getString("lot_no"),
                                    object.getString("gr_qty"),
                                    object.getString("buyer_qr"),
                                    object.getString("recevice_dt_tims"),
                                    object.getString("from_lct_nm"),
                                    object.getString("lct_sts_cd"),
                                    object.getString("sts_nm"),
                                    object.getString("mt_type_nm")
                            ));
                        }
                    }
                    buildRV();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), TimsShipManualActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), TimsShipManualActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void buildRV() {
        adapter = new ListManualTimsAdapter(listManualTims);
        rv_sh_s.setLayoutManager(new LinearLayoutManager(this));
        rv_sh_s.setHasFixedSize(true);
        rv_sh_s.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListManualTimsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Toast.makeText(PickManualActivity.this, "ss " + listPicMn.get(position).isCheck(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemCheck(int position, boolean chek) {
                listManualTims.get(position).setCheck(chek);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public static class ListManualTimsAdapter extends RecyclerView.Adapter<ListManualTimsAdapter.ListPicMnViewHolder> {
        private ArrayList<ListManualTims> waitItems;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ListPicMnViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sh_sl,
                    viewGroup, false);
            ListPicMnViewHolder evh = new ListPicMnViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListPicMnViewHolder waitViewHolder, int i) {
            ListManualTims currentItem = waitItems.get(i);

            waitViewHolder.ml_x.setText(currentItem.mt_cd);
            waitViewHolder.bb_x.setText(currentItem.getBb_no());
            waitViewHolder.by_x.setText(currentItem.getBuyer_qr());
            waitViewHolder.mt_x.setText(currentItem.getMt_type_nm());

            waitViewHolder.st_x.setText(currentItem.getSts_nm());
            waitViewHolder.qty_x.setText(currentItem.getGr_qty());

            waitViewHolder.dp_x.setText(currentItem.getFrom_lct_nm());
            waitViewHolder.ds_i.setText(currentItem.getLct_sts_cd());

            if (currentItem.getRecevice_dt_tims().length() > 5) {
                waitViewHolder.r_dt.setText(currentItem.getRecevice_dt_tims().length() > 8
                        ? currentItem.getRecevice_dt_tims()
                        : currentItem.getRecevice_dt_tims().substring(0, 4) + "-"
                        + currentItem.getRecevice_dt_tims().substring(4, 6) + "-"
                        + currentItem.getRecevice_dt_tims().substring(6, 8));
            } else {
                waitViewHolder.r_dt.setText(currentItem.getRecevice_dt_tims().replace("null", ""));
            }

            if(currentItem.isClo()){
                waitViewHolder.clo.setCardBackgroundColor(Color.GREEN);
            }else {
                waitViewHolder.clo.setCardBackgroundColor(Color.YELLOW);
            }

            waitViewHolder.checkas.setChecked(currentItem.isCheck());
        }

        @Override
        public int getItemCount() {
            return waitItems.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);

            void onItemCheck(int position, boolean chek);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class ListPicMnViewHolder extends RecyclerView.ViewHolder {
            public TextView ml_x, bb_x, by_x, st_x, qty_x, dp_x, ds_i, r_dt, mt_x;
            public CheckBox checkas;
            CardView clo;

            public ListPicMnViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                clo = itemView.findViewById(R.id.clo);
                checkas = itemView.findViewById(R.id.checkas);
                ml_x = itemView.findViewById(R.id.ml_x);
                bb_x = itemView.findViewById(R.id.bb_x);
                by_x = itemView.findViewById(R.id.by_x);
                mt_x = itemView.findViewById(R.id.mt_x);

                st_x = itemView.findViewById(R.id.st_x);
                qty_x = itemView.findViewById(R.id.qty_x);

                dp_x = itemView.findViewById(R.id.dp_x);
                ds_i = itemView.findViewById(R.id.ds_i);
                r_dt = itemView.findViewById(R.id.r_dt);

                checkas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemCheck(position, checkas.isChecked());
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

        public ListManualTimsAdapter(ArrayList<ListManualTims> waitItem) {
            waitItems = waitItem;
        }
    }

    private void pick() {
        //Toast.makeText(this, "Triệu chưa sửa chổ này, bị lỗi check box, và xóa đi. bên dưới // lấy.", Toast.LENGTH_SHORT).show();
        String sa = "";
        for (int i = 0; i < listManualTims.size(); i++) {
            if (listManualTims.get(i).isCheck()) {
                sa += "," + listManualTims.get(i).getWmtid();
            }
        }
        if (sa.length() > 0) {
            //AlerError.Baoloi(sa, this);
            // run
            String url = BaseApp.isHostting() + "/TIMS/TimsShipping_Scan_M?data=" +
                    sa.substring(1, sa.length()) + "&ext_no=" + tv_ext.getText().toString().trim();
            pickJson(url);
            //Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
        } else {
            AlerError.Baoloi("Check row picking !!!", this);
        }
    }

    private void pickJson(String url) {
        Log.d("pickJson", url);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("pickJson", response);

                progressDialog.dismiss();
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("result")) {
                        AlerError.Baoloi(jsonObject.has("message") ? jsonObject.getString("message") : "Error!!", TimsShipManualActivity.this);
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("datalist");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String wmtid = object.getString("wmtid");
                            for (int j = 0; j < listManualTims.size(); j++) {
                                if (wmtid.equals(listManualTims.get(j).getWmtid())) {
                                    listManualTims.get(j).setClo(true);
                                    listManualTims.get(j).setSts_nm(object.getString("sts_nm")
                                            .replace("]","")
                                            .replace("[","")
                                            .replace("\"",""));
                                    listManualTims.get(j).setRecevice_dt_tims(object.getString("recevice_dt_tims").replace("]",""));
                                }
                            }
                        }
                    }
                    buildRV();
                    AlerError.Baoloi(jsonObject.getString("message"), TimsShipManualActivity.this);

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                           for(int i=0; i<listManualTims.size();i++){
                               if(listManualTims.get(i).isClo()){
                                   listManualTims.remove(i);
                                   i--;
                               }
                           }
                           buildRV();
                        }
                    }, 5000);
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), TimsShipManualActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), TimsShipManualActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private class ListManualTims {
        boolean check, clo;
        String wmtid, mt_cd, mt_no, bb_no, lot_no, gr_qty, buyer_qr, recevice_dt_tims,
                from_lct_nm, lct_sts_cd, sts_nm, mt_type_nm;

        public ListManualTims(boolean check, boolean clo, String wmtid, String mt_cd, String mt_no, String bb_no, String lot_no, String gr_qty, String buyer_qr, String recevice_dt_tims, String from_lct_nm, String lct_sts_cd, String sts_nm, String mt_type_nm) {
            this.check = check;
            this.clo = clo;
            this.wmtid = wmtid;
            this.mt_cd = mt_cd;
            this.mt_no = mt_no;
            this.bb_no = bb_no;
            this.lot_no = lot_no;
            this.gr_qty = gr_qty;
            this.buyer_qr = buyer_qr;
            this.recevice_dt_tims = recevice_dt_tims;
            this.from_lct_nm = from_lct_nm;
            this.lct_sts_cd = lct_sts_cd;
            this.sts_nm = sts_nm;
            this.mt_type_nm = mt_type_nm;
        }

        public boolean isClo() {
            return clo;
        }

        public void setClo(boolean clo) {
            this.clo = clo;
        }

        public String getMt_no() {
            return mt_no;
        }

        public void setMt_no(String mt_no) {
            this.mt_no = mt_no;
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

        public void setWmtid(String wmtid) {
            this.wmtid = wmtid;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public void setMt_cd(String mt_cd) {
            this.mt_cd = mt_cd;
        }

        public String getBb_no() {
            return bb_no;
        }

        public void setBb_no(String bb_no) {
            this.bb_no = bb_no;
        }

        public String getLot_no() {
            return lot_no;
        }

        public void setLot_no(String lot_no) {
            this.lot_no = lot_no;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public void setGr_qty(String gr_qty) {
            this.gr_qty = gr_qty;
        }

        public String getBuyer_qr() {
            return buyer_qr;
        }

        public void setBuyer_qr(String buyer_qr) {
            this.buyer_qr = buyer_qr;
        }

        public String getRecevice_dt_tims() {
            return recevice_dt_tims;
        }

        public void setRecevice_dt_tims(String recevice_dt_tims) {
            this.recevice_dt_tims = recevice_dt_tims;
        }

        public String getFrom_lct_nm() {
            return from_lct_nm;
        }

        public void setFrom_lct_nm(String from_lct_nm) {
            this.from_lct_nm = from_lct_nm;
        }

        public String getLct_sts_cd() {
            return lct_sts_cd;
        }

        public void setLct_sts_cd(String lct_sts_cd) {
            this.lct_sts_cd = lct_sts_cd;
        }

        public String getSts_nm() {
            return sts_nm;
        }

        public void setSts_nm(String sts_nm) {
            this.sts_nm = sts_nm;
        }

        public String getMt_type_nm() {
            return mt_type_nm;
        }

        public void setMt_type_nm(String mt_type_nm) {
            this.mt_type_nm = mt_type_nm;
        }
    }
}