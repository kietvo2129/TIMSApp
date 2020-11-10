package com.example.timsapp.ui.gallery.receie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.ui.gallery.receie.RecMaActivity.KYE_RD;

public class RecScanActivity extends AppCompatActivity {
    public static final String KEYSDNO = "KEYSDNO";
    public static final String KEYSID = "KEYSID";
    private RecyclerView rv_res;
    private ImageView im_can, im_me;
    private TextView sd_num, edt_ea;
    private String codDae = "";
    private ProgressDialog progressDialog;
    private ListRecWAdapter adapter;
    private ArrayList<ListRecW> listRecW = new ArrayList<>();
    private TextView re_nub;
    //    private LinearLayout abtn_rem;
    private RecyclerView rv_pw;
    //    private ListRWPAdapter adapterP;
    private FloatingActionButton fab_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_scan);
        setTitle("Receiving Scan(TIMS)");
        final Intent intent = getIntent();
        final String SDNO = intent.getStringExtra(KEYSDNO);
        final String SID = intent.getStringExtra(KEYSID);

        // init
        sd_num = findViewById(R.id.sd_num);
        edt_ea = findViewById(R.id.edt_ea);
        im_can = findViewById(R.id.im_can);
        im_me = findViewById(R.id.im_me);
        rv_res = findViewById(R.id.rv_res);
        //re_nub = findViewById(R.id.re_nub);
        fab_set = findViewById(R.id.fab_set);

        //action
        sd_num.setText(SDNO);

        //scan
        im_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQRScanner();
            }
        });
        edt_ea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intext(edt_ea);
            }
        });

        im_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RecScanActivity.this, "Manual", Toast.LENGTH_SHORT).show();
                listRecW = new ArrayList<>();
                buildRV();
                Intent intent1 = new Intent(RecScanActivity.this, RecMaActivity.class);
                intent1.putExtra(KYE_RD, SDNO);
                startActivity(intent1);
            }
        });

        fab_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComlit();
            }
        });

        // ati
        String url = BaseApp.isHostting() + "/wipwms/Count_Remain_Qty?sd_no=" + SDNO;
//        remaiJson(url);
        //http://192.168.100.53:81/wipwms/Count_Remain_Qty?sd_no=sd6
    }

    private void sendComlit() {
        String ai = "";
        for (int i = 0; i < listRecW.size(); i++) {
            ai = ai + "," + listRecW.get(i).getWmtid();
        }

        if (ai.length() > 0) {
            String url = BaseApp.isHostting() + "/TIMS/UpdateMTQR_RDList?data=" + ai.substring(1) + "&rd_no=" + sd_num.getText().toString().trim();
            sendJSComlet(url);
        } else {
            AlerError.Baoloi("Don't have data !!!", RecScanActivity.this);
        }
    }

    private void sendJSComlet(String url) {
        Log.d("sendJSComlet", url);

        progressDialog = new ProgressDialog(RecScanActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("sendJSComlet", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("result")) {
                        AlerError.Baoloi(jsonObject.has("message") ? jsonObject.getString("message") : "Error!!", RecScanActivity.this);
                    } else {
                        AlerError.Baoloi(jsonObject.has("message") ? jsonObject.getString("message") : "ok !", RecScanActivity.this);

                        for (int i = 0; i < listRecW.size(); i++) {
                            listRecW.get(i).setColo(true);
                        }
                        adapter.notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //do something
                                aluXoa();
                            }
                        }, 6000);
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), RecScanActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), RecScanActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(RecScanActivity.this);
        requestQueue.add(stringRequest);
    }

    private void aluXoa() {
        listRecW.removeAll(listRecW);
        adapter.notifyDataSetChanged();
    }

    private void intext(final TextView tv) {
        codDae = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Code");

        View viewInflated = this.getLayoutInflater().inflate(R.layout.text_input_layout, null);

//        LayoutInflater inflater = this.getLayoutInflater();
//        View viewInflated = inflater.inflate(R.layout.text_input_layout, null);

        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                codDae = input.getText().toString();
                tv.setText(codDae);

                if (codDae.length() > 0) {
                    sendData(codDae);// intext
                } else {
                    AlerError.Baoloi("Please insert QR code !!!", RecScanActivity.this);
                }

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void startQRScanner() {
        codDae = "";
        new IntentIntegrator(this).setOrientationLocked(false).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(getActivity(), "+" + result.getContents(), Toast.LENGTH_LONG).show();
                //updateText(result.getContents());
                codDae = result.getContents();
                edt_ea.setText(codDae);
//                if (sp_pos <= 0) {
//                    AlerError.Baoloi("Haven't selected SDNO !!!", getActivity());
//                    return;
//                }
                if (codDae != null) {
                    sendData(codDae);
                } else {
                    AlerError.Baoloi("Please insert QR code !!!", this);
                }
            }
        }
    }

    private void sendData(String cod) {
        // Toast.makeText(this, sd_num.getText().toString() +"......"+ cod, Toast.LENGTH_SHORT).show();
        String url = BaseApp.isHostting() + "/TIMS/GetTimsReceiScanMLQR?bb_no=" + cod;
        //"http://192.168.100.53:81/wipwms/ScanML_no_ReceiWIP?ml_no=&sd_no="
        upJson(url);
    }

//    private void remaiJson(String url) {
//        Log.d("remaiJson", url);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("remaiJson", response);
//                re_nub.setText(response.toString().trim());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                progressDialog.dismiss();
//                AlerError.Baoloi("The server error:" + error.toString(), RecScanActivity.this);
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(RecScanActivity.this);
//        requestQueue.add(stringRequest);
//    }

    private void upJson(String url) {
        Log.d("upJson", url);

        progressDialog = new ProgressDialog(RecScanActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("upJson", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("result")) {
                        AlerError.Baoloi(jsonObject.has("message") ? jsonObject.getString("message") : "Error!!", RecScanActivity.this);
                    } else {
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            listRecW.add(new ListRecW(
                                    false,
                                    object.getString("wmtid"),
                                    object.getString("mt_cd"),
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
                        //re_nub.setText(jsonObject.getString("remain_qty"));
                        Toast.makeText(RecScanActivity.this, "Success !!!", Toast.LENGTH_SHORT).show();
                        buildRV();
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), RecScanActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), RecScanActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(RecScanActivity.this);
        requestQueue.add(stringRequest);
    }

    private void buildRV() {
        adapter = new ListRecWAdapter(listRecW);
        rv_res.setLayoutManager(new LinearLayoutManager(RecScanActivity.this));
        rv_res.setHasFixedSize(true);
        rv_res.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListRecWAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                String Bb_no = listRecW.get(position).getBb_no().trim();
                for (int i = 0; i < listRecW.size(); i++) {
                    if (Bb_no.equals(listRecW.get(i).getBb_no())) {
                        listRecW.remove(i);
                        i--;
                    }
                }

                adapter.notifyDataSetChanged();
            }
        });
    }

    public static class ListRecWAdapter extends RecyclerView.Adapter<ListRecWAdapter.ListRecWViewHolder> {
        private ArrayList<ListRecW> items;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ListRecWViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_rc_es,
                    viewGroup, false);
            ListRecWViewHolder evh = new ListRecWViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListRecWViewHolder viewHolder, int i) {
            ListRecW currentItem = items.get(i);

            viewHolder.ml_d.setText(currentItem.getMt_cd());
            viewHolder.bb_d.setText(currentItem.getBb_no());
            viewHolder.mt_d.setText(currentItem.getMt_type_nm());

            viewHolder.qty_d.setText(currentItem.getGr_qty());
            viewHolder.st_d.setText(currentItem.getSts_nm());

            viewHolder.dp_d.setText(currentItem.getFrom_lct_nm());
            viewHolder.dp_st.setText(currentItem.getLct_sts_cd());
            viewHolder.re_dt.setText(currentItem.getRecevice_dt_tims().length() == 12 ? currentItem.getRecevice_dt_tims() :
                    currentItem.getRecevice_dt_tims().substring(0, 4) + "-" + currentItem.getRecevice_dt_tims().substring(4, 6)
                            + "-" + currentItem.getRecevice_dt_tims().substring(6, 8));

            if (currentItem.isColo()) {
                viewHolder.car_s.setCardBackgroundColor(Color.GREEN);
            } else {
                viewHolder.car_s.setCardBackgroundColor(Color.YELLOW);
            }

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);

            void onDeleteClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class ListRecWViewHolder extends RecyclerView.ViewHolder {
            public TextView ml_d;
            public TextView bb_d;
            public TextView mt_d;
            public TextView qty_d;
            public TextView st_d;
            public TextView dp_d;
            public TextView dp_st;
            public TextView re_dt;
            ImageView dele_i;
            CardView car_s;

            public ListRecWViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                car_s = itemView.findViewById(R.id.car_s);
                ml_d = itemView.findViewById(R.id.ml_d);
                bb_d = itemView.findViewById(R.id.bb_d);
                mt_d = itemView.findViewById(R.id.mt_d);
                qty_d = itemView.findViewById(R.id.qty_d);
                st_d = itemView.findViewById(R.id.st_d);
                dp_d = itemView.findViewById(R.id.dp_d);
                dp_st = itemView.findViewById(R.id.dp_st);
                re_dt = itemView.findViewById(R.id.re_dt);
                dele_i = itemView.findViewById(R.id.dele_i);

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
                dele_i.setOnClickListener(new View.OnClickListener() {
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
            }
        }

        public ListRecWAdapter(ArrayList<ListRecW> waitItem) {
            items = waitItem;
        }
    }

    private class ListRecW {
        String wmtid, mt_cd, bb_no, gr_qty, recevice_dt_tims, from_lct_nm, lct_sts_cd, mt_type_nm,
                mt_type, sts_nm;
        boolean colo;

        public ListRecW(boolean colo, String wmtid, String mt_cd, String bb_no, String gr_qty, String recevice_dt_tims, String from_lct_nm, String lct_sts_cd, String mt_type_nm, String mt_type, String sts_nm) {
            this.colo = colo;
            this.wmtid = wmtid;
            this.mt_cd = mt_cd;
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
}