package com.example.timsapp.ui.slideshow.shipk;

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
import com.example.timsapp.ui.gallery.receie.RecScanActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.ui.slideshow.shipk.TimsShipManualActivity.KEYID;


public class TimsShipScanActivity extends AppCompatActivity {
    public static final String KEYEET = "KEYEET";
    private TextView tv_ext, tv_extqr;
    private ImageView im_ex_scan, im_ex_m;
    private RecyclerView rv_ex_ts;
    private FloatingActionButton fab_ex;
    private String CodeData;
    private ProgressDialog progressDialog;
    private ArrayList<ListTimsScan> listTimsScan = new ArrayList<>();
    private ListTimsScanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tims_ship_scan);
        setTitle("Shipping Scan (TIMS)");

        Intent intent = getIntent();
        final String ext_n = intent.getStringExtra(KEYEET);

        tv_ext = findViewById(R.id.tv_ext);
        tv_extqr = findViewById(R.id.tv_extqr);

        im_ex_scan = findViewById(R.id.im_ex_scan);
        im_ex_m = findViewById(R.id.im_ex_m);

        rv_ex_ts = findViewById(R.id.rv_ex_ts);
        fab_ex = findViewById(R.id.fab_ex);

        //action
        tv_ext.setText(ext_n);
        im_ex_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call manual
                listTimsScan = new ArrayList<>();
                buildRV();
                Intent intentManual = new Intent(TimsShipScanActivity.this, TimsShipManualActivity.class);
                intentManual.putExtra(KEYID, ext_n);
                startActivity(intentManual);
            }
        });
        fab_ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComlepe();
            }
        });
        im_ex_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScanQR();
            }
        });
        tv_extqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intext(tv_extqr);
            }
        });

    }

    private void intext(final TextView tv) {
        CodeData = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(TimsShipScanActivity.this);
        builder.setTitle("QR Code");
        View viewInflated = LayoutInflater.from(TimsShipScanActivity.this).inflate(R.layout.text_input_layout, null, false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CodeData = input.getText().toString();
                tv.setText(CodeData);
                if (tv.getText().toString().trim().length() > 0) {
                    sendData();
                } else {
                    AlerError.Baoloi("Please insert QR code !!!", TimsShipScanActivity.this);
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

    private void sendData() {
        for (int i = 0; i < listTimsScan.size(); i++) {
            if (CodeData.trim().equals(listTimsScan.get(i).getMt_cd().trim())) {
                AlerError.Baoloi("Data Duplicated !!!", TimsShipScanActivity.this);
                return;
            }
        }
        if (CodeData.length() != 0) {
            String url = Url.webUrl + "/TIMS/GetTimsShippingScanMLQR?buyer_qr=" + CodeData;
            addJsonMaPic(url);
        }
    }

    private void addJsonMaPic(String url) {
        Log.d("addJsonMaPic", url);

        progressDialog = new ProgressDialog(TimsShipScanActivity.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("addJsonMaPic", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getBoolean("result")) {
                        AlerError.Baoloi(jsonObject.has("message") ? jsonObject.getString("message") : "Error!!", TimsShipScanActivity.this);
                    } else {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            listTimsScan.add(new ListTimsScan(
                                    false,
                                    false,
                                    object.getString("wmtid"),
                                    object.getString("mt_cd"),
                                    object.getString("bb_no"),
                                    object.getString("buyer_qr"),
                                    object.getString("gr_qty"),
                                    object.getString("recevice_dt_tims").replace("null", ""),
                                    object.getString("from_lct_nm").replace("]", "").replace("[", "").replace("\"", ""),
                                    object.getString("lct_sts_cd").replace("]", "").replace("[", "").replace("\"", ""),
                                    object.getString("mt_type_nm").replace("]", "").replace("[", "").replace("\"", ""),
                                    object.getString("sts_nm").replace("]", "").replace("[", "").replace("\"", "")

                            ));
                        }
                    }
                    buildRV();

                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), TimsShipScanActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), TimsShipScanActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(TimsShipScanActivity.this);
        requestQueue.add(stringRequest);
    }

    private void buildRV() {
        adapter = new ListTimsScanAdapter(listTimsScan);
        rv_ex_ts.setLayoutManager(new LinearLayoutManager(TimsShipScanActivity.this));
        rv_ex_ts.setHasFixedSize(true);
        rv_ex_ts.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListTimsScanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDeleteClick(int position) {
                listTimsScan.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
    }

    private void startScanQR() {
        CodeData = "";
        new IntentIntegrator(this).setOrientationLocked(false).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(TimsShipScanActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                CodeData = result.getContents();
                tv_extqr.setText(CodeData);

                if (CodeData != null) {
                    sendData();
                } else {
                    AlerError.Baoloi("Please insert QR code !!!", TimsShipScanActivity.this);
                }
            }
        }
    }

    private void sendComlepe() {

        String ai = "";
        for (int i = 0; i < listTimsScan.size(); i++) {
            ai = ai + "," + listTimsScan.get(i).getWmtid();
        }

        if (ai.length() > 0) {
            String url = Url.webUrl + "/TIMS/UpdateMTQR_EXTList?data=" + ai.substring(1) + "&ext_no=" + tv_ext.getText().toString().trim();
            sendJSComlet(url);
        } else {
            AlerError.Baoloi("Don't have data !!!", TimsShipScanActivity.this);
        }

    }

    private void sendJSComlet(String url) {
        Log.d("sendJSComlet", url);

        progressDialog = new ProgressDialog(TimsShipScanActivity.this);
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
                        AlerError.Baoloi(jsonObject.has("message") ? jsonObject.getString("message") : "Error!!", TimsShipScanActivity.this);
                    } else {
                        AlerError.Baoloi(jsonObject.has("message") ? jsonObject.getString("message") : "ok !", TimsShipScanActivity.this);

                        for (int i = 0; i < listTimsScan.size(); i++) {
                            listTimsScan.get(i).setColor(true);
                        }
                        adapter.notifyDataSetChanged();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //do something
                                listTimsScan.removeAll(listTimsScan);
                                adapter.notifyDataSetChanged();
                            }
                        }, 6000);
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), TimsShipScanActivity.this);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), TimsShipScanActivity.this);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(TimsShipScanActivity.this);
        requestQueue.add(stringRequest);
    }

    public static class ListTimsScanAdapter extends RecyclerView.Adapter<ListTimsScanAdapter.ListTimsScanViewHolder> {
        private ArrayList<ListTimsScan> waitItems;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ListTimsScanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ex_t,
                    viewGroup, false);
            ListTimsScanViewHolder evh = new ListTimsScanViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListTimsScanViewHolder waitViewHolder, int i) {
            ListTimsScan currentItem = waitItems.get(i);

            waitViewHolder.ml_x.setText(currentItem.getMt_cd());
            waitViewHolder.bb_x.setText(currentItem.getBb_no());
            waitViewHolder.by_x.setText(currentItem.getBuyer_qr());
            waitViewHolder.st_x.setText(currentItem.getSts_nm());
            waitViewHolder.qty_x.setText(currentItem.getGr_qty());
            waitViewHolder.dp_x.setText(currentItem.getFrom_lct_nm());
            waitViewHolder.ds_i.setText(currentItem.getLct_sts_cd());
            String dt = currentItem.getReplace();

            if(dt.length() >= 8 && dt.length() != 12) {
                dt = currentItem.getReplace().length() == 12
                        ? currentItem.getReplace()
                        : currentItem.getReplace().substring(0, 4) + "-"
                        + currentItem.getReplace().substring(4, 6) + "-"
                        + currentItem.getReplace().substring(6, 8);
            }

            waitViewHolder.r_dt.setText(dt);
            waitViewHolder.mt_x.setText(currentItem.getMt_type_nm());

            if (currentItem.isColor()) {
                waitViewHolder.car.setCardBackgroundColor(Color.GREEN);
            } else {
                waitViewHolder.car.setCardBackgroundColor(Color.YELLOW);
            }
        }

        @Override
        public int getItemCount() {
            return waitItems.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);

            void onDeleteClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class ListTimsScanViewHolder extends RecyclerView.ViewHolder {
            public TextView ml_x, bb_x, by_x, st_x, qty_x, dp_x, ds_i, r_dt, mt_x;
            public ImageView del_l;
            public CardView car;

            public ListTimsScanViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                car = itemView.findViewById(R.id.car);
                ml_x = itemView.findViewById(R.id.ml_x);
                bb_x = itemView.findViewById(R.id.bb_x);
                by_x = itemView.findViewById(R.id.by_x);
                st_x = itemView.findViewById(R.id.st_x);
                qty_x = itemView.findViewById(R.id.qty_x);
                dp_x = itemView.findViewById(R.id.dp_x);
                ds_i = itemView.findViewById(R.id.ds_i);
                del_l = itemView.findViewById(R.id.del_l);
                r_dt = itemView.findViewById(R.id.r_dt);
                mt_x = itemView.findViewById(R.id.mt_x);

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
                del_l.setOnClickListener(new View.OnClickListener() {
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

        public ListTimsScanAdapter(ArrayList<ListTimsScan> waitItem) {
            waitItems = waitItem;
        }
    }

    private class ListTimsScan {
        boolean check, color;
        String wmtid, mt_cd, bb_no, buyer_qr, gr_qty, replace, from_lct_nm,
        lct_sts_cd, mt_type_nm, sts_nm;

        public ListTimsScan(boolean check,boolean color, String wmtid, String mt_cd, String bb_no, String buyer_qr, String gr_qty, String replace, String from_lct_nm, String lct_sts_cd, String mt_type_nm, String sts_nm) {
            this.check = check;
            this.color = color;
            this.wmtid = wmtid;
            this.mt_cd = mt_cd;
            this.bb_no = bb_no;
            this.buyer_qr = buyer_qr;
            this.gr_qty = gr_qty;
            this.replace = replace;
            this.from_lct_nm = from_lct_nm;
            this.lct_sts_cd = lct_sts_cd;
            this.mt_type_nm = mt_type_nm;
            this.sts_nm = sts_nm;
        }

        public boolean isColor() {
            return color;
        }

        public void setColor(boolean color) {
            this.color = color;
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

        public String getBuyer_qr() {
            return buyer_qr;
        }

        public void setBuyer_qr(String buyer_qr) {
            this.buyer_qr = buyer_qr;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public void setGr_qty(String gr_qty) {
            this.gr_qty = gr_qty;
        }

        public String getReplace() {
            return replace;
        }

        public void setReplace(String replace) {
            this.replace = replace;
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

        public String getSts_nm() {
            return sts_nm;
        }

        public void setSts_nm(String sts_nm) {
            this.sts_nm = sts_nm;
        }
    }
}