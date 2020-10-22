package com.example.timsapp.ui.gallery.receie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.example.timsapp.R;
import com.example.timsapp.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecMaActivity extends AppCompatActivity {
    public static final String KYE_RD = "KYE_RD";
    private Button btn_ser;
    private Button btn_pik;
    private RecyclerView rv_picm;
    private EditText edt_ml, edt_mt;
    private ProgressDialog progressDialog;
    private ArrayList<ListPicMn> listPicMn;
    private ListPicMnAdapter adapter;
    private Spinner sp_cho_pick;
    private ArrayList<ListPik> listPik;
    private int sp_pos;
    private EditText edt_bb;
    private TextView tex_sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_ma);
        setTitle("Receiving Manual(TIMS)");
        Intent intent = getIntent();
        String rdn = intent.getStringExtra(KYE_RD);

        // init
        btn_ser = findViewById(R.id.btn_ser);
        btn_pik = findViewById(R.id.btn_pik);
        rv_picm = findViewById(R.id.rv_picm);
        edt_mt = findViewById(R.id.edt_mt);
        edt_ml = findViewById(R.id.edt_ml);
        edt_bb = findViewById(R.id.edt_bb);

        sp_cho_pick = findViewById(R.id.sp_cho_pick);
        tex_sd = findViewById(R.id.tex_sd);

        //
        tex_sd.setText(rdn);

        // action
//        load_pick_sp();

        serch();

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

//    private void load_pick_sp() {
//
//        String url = BaseApp.isHostting() + "/ShippingMgt/GetPickingScan";
//        load_pick_sp_Json(url);
//
//        sp_cho_pick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                sp_pos = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }

    private void load_pick_sp_Json(String url) {
        Log.d("load_pick_sp_Json", url);

//        progressDialog = new ProgressDialog(PickManualActivity.this);
//        progressDialog.setMessage("Loading..."); // Setting Message
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                Log.d("load_pick_sp_Json", response);
                listPik = new ArrayList<ListPik>();
                listPik.add(new ListPik("", "  -- SD NO --", "", "", "", "", "", "", "", "", ""));
//                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {
                        AlerError.Baoloi("Picking Scan No Data !!!", RecMaActivity.this);
                    } else {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            listPik.add(new ListPik(
                                    object.getString("sid"),
                                    object.getString("sd_no"),
                                    object.getString("sd_nm"),
                                    object.getString("sd_sts_cd"),
                                    object.getString("lct_cd"),
                                    object.getString("alert"),
                                    object.getString("remark"),
                                    object.getString("use_yn"),
                                    object.getString("del_yn"),
                                    object.getString("sid"),
                                    object.getString("lct_nm")
                            ));
                        }
                    }

                    ArrayAdapter adapter = new ArrayAdapter(RecMaActivity.this, R.layout.spinner_item, listPik);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp_cho_pick.setAdapter(adapter);

//                    progressDialog.dismiss();
                } catch (JSONException e) {
//                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), RecMaActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), RecMaActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(RecMaActivity.this);
        requestQueue.add(stringRequest);
    }

    private void pick() {
        //http://192.168.100.53:81/ShippingMgt/ShippingPicking_M?data=1,2&sd_no=
//        if (sp_pos <= 0) {
//            AlerError.Baoloi("Haven't selected SDNO !!!", RecMaActivity.this);
//            return;
//        }
        String sa = "";
        for (int i = 0; i < listPicMn.size(); i++) {
            if (listPicMn.get(i).isCheck()) {
                sa += "," + listPicMn.get(i).getWmtid();
            }
        }
        if (sa.length() > 0) {
            //AlerError.Baoloi(sa, this);
            // run
            String url = Url.webUrl + "/TIMS/Receving_Scan_M?data="+
                    sa.substring(1,sa.length()) + "&rd_no="+ tex_sd.getText() ;
            pickJson(url);
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
                  /*
                  {"result":true,"message":"Successfully!!!",
                  "datalist":[{"wmtid":11,"mt_cd":"DYT1503-SNR205-CP-201012111307000001","lot_no":"7","gr_qty":100,"expiry_dt":"20201005","dt_of_receipt":"20201006","expore_dt":"20201006"},{"wmtid":12,"mt_cd":"4296L-1012A-NQ-CP-201014090943000001","lot_no":"samsung","gr_qty":100,"expiry_dt":"20201014","dt_of_receipt":"20201014","expore_dt":"20201014"}]}
                  */
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("result")) {
                        AlerError.Baoloi("Picking Error !!!", RecMaActivity.this);
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("datalist");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String wmtid =  object.getString("wmtid");
                            for(int j =0; j<listPicMn.size(); j++){
                                if(wmtid.equals(listPicMn.get(j).getWmtid())){
                                    listPicMn.remove(j);
                                    break;
                                }
                            }
                        }
                    }

                    Toast.makeText(RecMaActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    buildRV();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), RecMaActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), RecMaActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void serch() {

        String url = Url.webUrl + "/TIMS/Get_List_Material_TimsReceiving?mt_no=" +
                edt_mt.getText().toString().trim() + "&mt_cd=" +
                edt_ml.getText().toString().trim() + "&bb_no=" +
                edt_bb.getText().toString().trim();
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
                listPicMn = new ArrayList<ListPicMn>();
                progressDialog.dismiss();
                if (response.indexOf("result") != -1) {
                    AlerError.Baoloi("Data don't have!!!", RecMaActivity.this);
                    buildRV();
                    progressDialog.dismiss();
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        AlerError.Baoloi("Data don't have!!!", RecMaActivity.this);
                    } else {
                        //"wmtid": 55,
                        //"mt_cd": "LJ63-123ROT20201016110058000001",
                        //"mt_no": "LJ63-123-ROT",
                        //"bb_no": "B191107028",
                        //"gr_qty": 1660,
                        //"recevice_dt_tims": "20201016110058",
                        //"from_lct_cd": "002002000000000000",
                        //"from_lct_nm": ["Sản xuất 1"],
                        //"lct_sts_cd": ["Receive"],
                        //"mt_type_nm": ["Composite Material"],
                        //"mt_type": "CMT",
                        //"mt_sts_cd": "002",
                        //"sts_nm": ["Using"]
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            boolean check = false;
                            listPicMn.add(new ListPicMn(
                                    false,
                                    check,
                                    object.getString("wmtid"),
                                    object.getString("mt_cd"),
                                    object.getString("mt_no"),
                                    object.getString("bb_no"),
                                    object.getString("gr_qty"),
                                    object.getString("recevice_dt_tims"),
                                    object.getString("from_lct_nm").replace("[", "").replace("]", "").replace("\"", ""),
                                    object.getString("lct_sts_cd").replace("[", "").replace("]", "").replace("\"", ""),
                                    object.getString("mt_type_nm").replace("[", "").replace("]", "").replace("\"", ""),
                                    object.getString("mt_type"),
                                    object.getString("sts_nm").replace("[", "").replace("]", "").replace("\"", "")
                            ));
                        }

                    }
                    buildRV();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), RecMaActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), RecMaActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void buildRV() {
        adapter = new ListPicMnAdapter(listPicMn);
        rv_picm.setLayoutManager(new LinearLayoutManager(this));
        rv_picm.setHasFixedSize(true);
        rv_picm.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListPicMnAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Toast.makeText(PickManualActivity.this, "ss " + listPicMn.get(position).isCheck(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemCheck(int position, boolean chek) {
                listPicMn.get(position).setCheck(chek);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private class ListPicMn {
      boolean check;boolean colo;
      String wmtid, mt_cd, mt_no, bb_no, gr_qty, recevice_dt_tims, from_lct_nm,
        lct_sts_cd, mt_type_nm, mt_type, sts_nm;

        public ListPicMn(boolean colo, boolean check, String wmtid, String mt_cd, String mt_no, String bb_no, String gr_qty, String recevice_dt_tims, String from_lct_nm, String lct_sts_cd, String mt_type_nm, String mt_type, String sts_nm) {
            this.colo = colo;
            this.check = check;
            this.wmtid = wmtid;
            this.mt_cd = mt_cd;
            this.mt_no = mt_no;
            this.bb_no = bb_no;
            this.gr_qty = gr_qty;
            this.recevice_dt_tims = recevice_dt_tims;
            this.from_lct_nm = from_lct_nm;
            this.lct_sts_cd = lct_sts_cd;
            this.mt_type_nm = mt_type_nm;
            this.mt_type = mt_type;
            this.sts_nm = sts_nm;
        }

        public boolean isColo() {
            return colo;
        }

        public void setColo(boolean colo) {
            this.colo = colo;
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

        public String getMt_no() {
            return mt_no;
        }

        public void setMt_no(String mt_no) {
            this.mt_no = mt_no;
        }

        public String getBb_no() {
            return bb_no;
        }

        public void setBb_no(String bb_no) {
            this.bb_no = bb_no;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public void setGr_qty(String gr_qty) {
            this.gr_qty = gr_qty;
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

        public String getMt_type_nm() {
            return mt_type_nm;
        }

        public void setMt_type_nm(String mt_type_nm) {
            this.mt_type_nm = mt_type_nm;
        }

        public String getMt_type() {
            return mt_type;
        }

        public void setMt_type(String mt_type) {
            this.mt_type = mt_type;
        }

        public String getSts_nm() {
            return sts_nm;
        }

        public void setSts_nm(String sts_nm) {
            this.sts_nm = sts_nm;
        }
    }

    public static class ListPicMnAdapter extends RecyclerView.Adapter<ListPicMnAdapter.ListPicMnViewHolder> {
        private ArrayList<ListPicMn> waitItems;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ListPicMnViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pp_rd,
                    viewGroup, false);
            ListPicMnViewHolder evh = new ListPicMnViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListPicMnViewHolder waitViewHolder, int i) {
            ListPicMn currentItem = waitItems.get(i);

            waitViewHolder.mt_i.setText(currentItem.getMt_no());
            waitViewHolder.ml_i.setText(currentItem.getMt_cd());
            waitViewHolder.bb_i.setText(currentItem.getBb_no());
            waitViewHolder.qty_i.setText(currentItem.getGr_qty());

            waitViewHolder.st_i.setText(currentItem.getSts_nm());
            waitViewHolder.yt_i.setText(currentItem.getMt_type_nm());
            waitViewHolder.dp_i.setText(currentItem.getFrom_lct_nm());
            waitViewHolder.ds_i.setText(currentItem.getLct_sts_cd());

            waitViewHolder.dt_i.setText(currentItem.getRecevice_dt_tims().length() == 12 ? currentItem.getRecevice_dt_tims() :
                    currentItem.getRecevice_dt_tims().substring(0, 4) + "-" + currentItem.getRecevice_dt_tims().substring(4, 6)
                            + "-" + currentItem.getRecevice_dt_tims().substring(6, 8));
            waitViewHolder.check_i.setChecked(currentItem.isCheck());
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
            public TextView mt_i;
            public TextView ml_i;
            public TextView bb_i;
            public TextView qty_i;

            public TextView yt_i;
            public TextView st_i;
            public TextView dp_i;
            public TextView ds_i;

            public TextView dt_i;
            public CheckBox check_i;
            public CardView card_mad;

            public ListPicMnViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                check_i = itemView.findViewById(R.id.is_check);
                mt_i = itemView.findViewById(R.id.mt_i);
                ml_i = itemView.findViewById(R.id.ml_i);
                bb_i = itemView.findViewById(R.id.bb_i);
                qty_i = itemView.findViewById(R.id.qty_i);
                yt_i = itemView.findViewById(R.id.yt_i);
                st_i = itemView.findViewById(R.id.st_i);
                dp_i = itemView.findViewById(R.id.dp_i);
                ds_i = itemView.findViewById(R.id.ds_i);
                dt_i = itemView.findViewById(R.id.dt_i);
                card_mad = itemView.findViewById(R.id.card_mad);

                check_i.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onItemCheck(position, check_i.isChecked());
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

        public ListPicMnAdapter(ArrayList<ListPicMn> waitItem) {
            waitItems = waitItem;
        }
    }


    public static class ListPik {
        String sid;
        String sd_no;
        String sd_nm;
        String sd_sts_cd;
        String lct_cd;
        String alert;
        String remark;
        String use_yn;
        String del_yn;
        String sts_nm;
        String lct_nm;

        public ListPik(String sid, String sd_no, String sd_nm, String sd_sts_cd, String lct_cd, String alert, String remark, String use_yn, String del_yn, String sts_nm, String lct_nm) {
            this.sid = sid;
            this.sd_no = sd_no;
            this.sd_nm = sd_nm;
            this.sd_sts_cd = sd_sts_cd;
            this.lct_cd = lct_cd;
            this.alert = alert;
            this.remark = remark;
            this.use_yn = use_yn;
            this.del_yn = del_yn;
            this.sts_nm = sts_nm;
            this.lct_nm = lct_nm;
        }

        public String getSd_sts_cd() {
            return sd_sts_cd;
        }

        public void setSd_sts_cd(String sd_sts_cd) {
            this.sd_sts_cd = sd_sts_cd;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getSd_no() {
            return sd_no;
        }

        public void setSd_no(String sd_no) {
            this.sd_no = sd_no;
        }

        public String getSd_nm() {
            return sd_nm;
        }

        public void setSd_nm(String sd_nm) {
            this.sd_nm = sd_nm;
        }

        public String getLct_cd() {
            return lct_cd;
        }

        public void setLct_cd(String lct_cd) {
            this.lct_cd = lct_cd;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getUse_yn() {
            return use_yn;
        }

        public void setUse_yn(String use_yn) {
            this.use_yn = use_yn;
        }

        public String getDel_yn() {
            return del_yn;
        }

        public void setDel_yn(String del_yn) {
            this.del_yn = del_yn;
        }

        public String getSts_nm() {
            return sts_nm;
        }

        public void setSts_nm(String sts_nm) {
            this.sts_nm = sts_nm;
        }

        public String getLct_nm() {
            return lct_nm;
        }

        public void setLct_nm(String lct_nm) {
            this.lct_nm = lct_nm;
        }

        @Override
        public String toString() {
            return sd_no;
        }
    }

}