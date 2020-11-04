package com.example.timsapp.ui.gallery.receie;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.timsapp.AlerError.AlerError;
import com.example.timsapp.LoginActivity;
import com.example.timsapp.R;
import com.example.timsapp.SplashActivity;
import com.example.timsapp.Url;
import com.example.timsapp.ui.slideshow.shipk.TimsShipScanActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.R.*;


public class Receive_Fragment extends Fragment {

    // private EditText edt_sd, edt_sm;
    //private Button btn_serh;
    private RecyclerView recyclerViewListRecTims;
    private ProgressDialog progressDialog;
    private ArrayList<ListRecTims> listRecTims;
    private ListRecTimsAdaptor listRecTimsAdaptor;
    private ImageView im_can_bobbin;
    private TextView tv_bobbin_no;
//    private ArrayList<ListRecS> listRecS;
//    private ListRecSAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(layout.fragment_receive, container, false);
        getActivity().setTitle("Receiving Scan(TIMS)");

        recyclerViewListRecTims = view.findViewById(id.recyclerViewListRecTims);
        tv_bobbin_no = view.findViewById(id.tv_bobbin_no);
        im_can_bobbin = view.findViewById(id.im_can_bobbin);

        //innit
        //edt_sd = view.findViewById(R.id.edt_sd);
        //edt_sm = view.findViewById(R.id.edt_sm);
        // btn_serh = view.findViewById(R.id.btn_serh);
        // rv_rc_d = view.findViewById(R.id.rv_rc_d);
        //action
//        Search();
//        btn_serh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Search();
//            }
//        });

        tv_bobbin_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intext(tv_bobbin_no);
            }
        });

        im_can_bobbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQRScanner();
            }
        });
        return view;
    }

    private void intext(final TextView tv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        builder.setTitle("QR Code");

        View viewInflated = this.getLayoutInflater().inflate(layout.text_input_layout, null,false);
//        View viewInflated = LayoutInflater.from(TimsShipScanActivity.this).inflate(R.layout.text_input_layout, null, false);


//        LayoutInflater inflater = this.getLayoutInflater();
//        View viewInflated = inflater.inflate(R.layout.text_input_layout, null);

        final EditText input = (EditText) viewInflated.findViewById(id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String txt = input.getText().toString();
                tv.setText(txt);

                if (txt.length() > 0) {
                    sendData(txt);// intext
                } else {
                    AlerError.Baoloi("Please insert QR code !!!", getActivity());
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
        IntentIntegrator.forSupportFragment(this).setOrientationLocked(false).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_LONG).show();
            } else {
                String txt = result.getContents();
                tv_bobbin_no.setText(txt);
                if (txt != null) {
                    sendData(txt);
                } else {
                    AlerError.Baoloi("Please insert QR code !!!", getActivity());
                }
            }
        }
    }

    private void sendData(String code) {
        String url = Url.webUrl + "/TIMS/UpdateMTQR_RDList?bb_no=" + code;
        updateJsonList(url);
    }
//    private void Search() {
//
//        String url = Url.webUrl + "/TIMS/GetRDInfo?rd_no="
//                + edt_sd.getText().toString().trim() + "&rd_nm=" + edt_sm.getText().toString().trim();
//        serJson(url);
//    }

//    private void serJson(String url) {
//        Log.d("serJson", url);
//
//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("Loading..."); // Setting Message
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//        progressDialog.show(); // Display Progress Dialog
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("serJson", response);
//                listRecS = new ArrayList<ListRecS>();
//
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//
//                    if (jsonArray.length() == 0) {
//
//                        AlerError.Baoloi("Data don't have!!!", getActivity());
//                    } else {
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject object = jsonArray.getJSONObject(i);
//
//                            listRecS.add(new ListRecS(
//                                    object.getString("rid"),
//                                    object.getString("rd_no"),
//                                    object.getString("rd_nm"),
//                                    object.getString("rd_sts_cd"),
//                                    object.getString("lct_cd"),
//                                    object.getString("receiving_dt"),
//                                    object.getString("remark")
//                            ));
//                        }
//                    }
//                    buildRV();
//                    progressDialog.dismiss();
//                } catch (JSONException e) {
//                    progressDialog.dismiss();
//                    AlerError.Baoloi("The json error:" + e.toString(), getActivity());
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                progressDialog.dismiss();
//                AlerError.Baoloi("The server error:" + error.toString(), getActivity());
//            }
//        });
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
//    }

    private void buildRV() {
        listRecTimsAdaptor = new ListRecTimsAdaptor(listRecTims);
        recyclerViewListRecTims.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewListRecTims.setHasFixedSize(true);
        recyclerViewListRecTims.setAdapter(listRecTimsAdaptor);

//        adapter.setOnItemClickListener(new ListRecSAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                // Toast.makeText(getActivity(), "d" + position, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), RecScanActivity.class);
//                intent.putExtra(KEYSDNO, listRecS.get(position).getRd_no());
//                intent.putExtra(KEYSID, listRecS.get(position).getRid());
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadListRecTims();
//        Search();
    }

    private void loadListRecTims() {
        String url = Url.webUrl + "/TIMS/Get_List_Material_TimsReceiving_PO?_search=false&rows=5000&page=1&sidx=&sord=asc";
        loadJsonList(url);
    }

    private void updateJsonList(String url) {
        Log.d("updateJsonList", url);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("updateJsonList", response);
                progressDialog.dismiss();
                //listRecTims = new ArrayList<ListRecTims>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                    if (jsonArray.length() == 0) {
                        AlerError.Baoloi("Data don't have!!!", getActivity());
                    } else {
                        JSONObject object = jsonArray.getJSONObject(0);
                        boolean changColor = true;
                        final ListRecTims list = new ListRecTims(
                                changColor,
                                object.getString("wmtid"),
                                object.getString("id_actual"),
                                object.getString("mt_cd"),
                                object.getString("mt_type"),
                                object.getString("bb_no"),
                                object.getString("gr_qty"),
                                object.getString("recevice_dt_tims"),
                                object.getString("from_lct_cd"),
                                object.getString("lct_sts_cd"),
                                object.getString("mt_sts_cd"),
                                object.getString("input_dt"),
                                object.getString("sts_nm"),
                                object.getString("mt_type_nm"),
                                object.getString("from_lct_nm")
                        );

                        for (int i = 0; i < listRecTims.size(); i++) {
                            if (list.getWmtid().equals(listRecTims.get(i).getWmtid())) {
                                listRecTims.get(i).setColor(true);
                            }
                        }
                        listRecTimsAdaptor.notifyDataSetChanged();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < listRecTims.size(); i++) {
                                    if (list.getWmtid().equals(listRecTims.get(i).getWmtid())) {
                                        listRecTims.remove(i);
                                        i--;
                                    }
                                }
                                listRecTimsAdaptor.notifyDataSetChanged();
                            }
                        }, 3000);
                    }

                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), getActivity());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), getActivity());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void loadJsonList(String url) {
        Log.d("loadJsonList", url);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("loadJsonList", response);
                listRecTims = new ArrayList<ListRecTims>();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        AlerError.Baoloi("Data don't have!!!", getActivity());
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            boolean color = false;
                            listRecTims.add(new ListRecTims(
                                    color,
                                    object.getString("wmtid"),
                                    object.getString("id_actual"),
                                    object.getString("mt_cd"),
                                    object.getString("mt_type"),
                                    object.getString("bb_no"),
                                    object.getString("gr_qty"),
                                    object.getString("recevice_dt_tims"),
                                    object.getString("from_lct_cd"),
                                    object.getString("lct_sts_cd"),
                                    object.getString("mt_sts_cd"),
                                    object.getString("input_dt"),
                                    object.getString("sts_nm"),
                                    object.getString("mt_type_nm"),
                                    object.getString("from_lct_nm")
                            ));
                        }
                    }
                    buildRV();
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    AlerError.Baoloi("The json error:" + e.toString(), getActivity());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                AlerError.Baoloi("The server error:" + error.toString(), getActivity());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private class ListRecTims {
        boolean color;
        String wmtid, id_actual, mt_cd, mt_type, bb_no, gr_qty, recevice_dt_tims, from_lct_cd,
                lct_sts_cd, mt_sts_cd, input_dt, sts_nm, mt_type_nm, from_lct_nm;

        public ListRecTims(boolean color, String wmtid, String id_actual, String mt_cd, String mt_type, String bb_no,
                           String gr_qty, String recevice_dt_tims, String from_lct_cd, String lct_sts_cd,
                           String mt_sts_cd, String input_dt, String sts_nm, String mt_type_nm, String from_lct_nm) {
            this.color = color;
            this.wmtid = wmtid;
            this.id_actual = id_actual;
            this.mt_cd = mt_cd;
            this.mt_type = mt_type;
            this.bb_no = bb_no;
            this.gr_qty = gr_qty;
            this.recevice_dt_tims = recevice_dt_tims;
            this.from_lct_cd = from_lct_cd;
            this.lct_sts_cd = lct_sts_cd;
            this.mt_sts_cd = mt_sts_cd;
            this.input_dt = input_dt;
            this.sts_nm = sts_nm;
            this.mt_type_nm = mt_type_nm;
            this.from_lct_nm = from_lct_nm;
        }

        public boolean isColor() {
            return color;
        }

        public void setColor(boolean color) {
            this.color = color;
        }

        public String getWmtid() {
            return wmtid;
        }

        public String getId_actual() {
            return id_actual;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public String getMt_type() {
            return mt_type;
        }

        public String getBb_no() {
            return bb_no;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public String getRecevice_dt_tims() {
            return recevice_dt_tims;
        }

        public String getFrom_lct_cd() {
            return from_lct_cd;
        }

        public String getLct_sts_cd() {
            return lct_sts_cd;
        }

        public String getMt_sts_cd() {
            return mt_sts_cd;
        }

        public String getInput_dt() {
            return input_dt;
        }

        public String getSts_nm() {
            return sts_nm;
        }

        public String getMt_type_nm() {
            return mt_type_nm;
        }

        public String getFrom_lct_nm() {
            return from_lct_nm;
        }
    }

    public static class ListRecTimsAdaptor extends RecyclerView.Adapter<ListRecTimsAdaptor.ListRecTimsViewHolder> {
        private ArrayList<ListRecTims> items;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ListRecTimsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout.item_rec_tims,
                    viewGroup, false);
            ListRecTimsViewHolder evh = new ListRecTimsViewHolder(v, mListener);
            return evh;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(@NonNull ListRecTimsViewHolder vh, int i) {
            ListRecTims currentItem = items.get(i);

            vh.tv_qty.setText(currentItem.getGr_qty());
            vh.tv_ml_no.setText(currentItem.getMt_cd());
            vh.tv_bio_bi.setText(currentItem.getBb_no());
            vh.tv_st.setText(currentItem.getSts_nm());
            vh.tv_ty_ty.setText(currentItem.getMt_type_nm());
            vh.tv_depar.setText(currentItem.getFrom_lct_nm());
            vh.tv_rec_dt.setText(
                    currentItem.getInput_dt().length() > 8
                            ? currentItem.getInput_dt().substring(0, 4) + "-"
                            + currentItem.getInput_dt().substring(4, 6) + "-"
                            + currentItem.getInput_dt().substring(6, 8)
                            : currentItem.getInput_dt());

            if(currentItem.isColor()){
                vh.card_item.setCardBackgroundColor(Color.GREEN);
            }else{
                vh.card_item.setCardBackgroundColor(color.colorbackgound);
            }
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

        public class ListRecTimsViewHolder extends RecyclerView.ViewHolder {

            public TextView tv_ml_no;
            public TextView tv_qty;
            public TextView tv_bio_bi;
            public TextView tv_st;
            public TextView tv_ty_ty;
            public TextView tv_depar;
            public TextView tv_rec_dt;
            public CardView card_item;

            public ListRecTimsViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                card_item = itemView.findViewById(id.card_item);
                tv_ml_no = itemView.findViewById(id.tv_ml_no);
                tv_qty = itemView.findViewById(id.tv_qty);
                tv_bio_bi = itemView.findViewById(id.tv_bio_bi);
                tv_st = itemView.findViewById(id.tv_st);
                tv_ty_ty = itemView.findViewById(id.tv_ty_ty);
                tv_depar = itemView.findViewById(id.tv_depar);
                tv_rec_dt = itemView.findViewById(id.tv_rec_dt);

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

        public ListRecTimsAdaptor(ArrayList<ListRecTims> list) {
            items = list;
        }

    }
//
//    private class ListRecS {
//        String rid, rd_no, rd_nm, rd_sts_cd, lct_cd, receiving_dt, remark;
//
//        public ListRecS(String rid, String rd_no, String rd_nm, String rd_sts_cd, String lct_cd, String receiving_dt, String remark) {
//            this.rid = rid;
//            this.rd_no = rd_no;
//            this.rd_nm = rd_nm;
//            this.rd_sts_cd = rd_sts_cd;
//            this.lct_cd = lct_cd;
//            this.receiving_dt = receiving_dt;
//            this.remark = remark;
//        }
//
//        public String getRid() {
//            return rid;
//        }
//
//        public void setRid(String rid) {
//            this.rid = rid;
//        }
//
//        public String getRd_no() {
//            return rd_no;
//        }
//
//        public void setRd_no(String rd_no) {
//            this.rd_no = rd_no;
//        }
//
//        public String getRd_nm() {
//            return rd_nm;
//        }
//
//        public void setRd_nm(String rd_nm) {
//            this.rd_nm = rd_nm;
//        }
//
//        public String getRd_sts_cd() {
//            return rd_sts_cd;
//        }
//
//        public void setRd_sts_cd(String rd_sts_cd) {
//            this.rd_sts_cd = rd_sts_cd;
//        }
//
//        public String getLct_cd() {
//            return lct_cd;
//        }
//
//        public void setLct_cd(String lct_cd) {
//            this.lct_cd = lct_cd;
//        }
//
//        public String getReceiving_dt() {
//            return receiving_dt;
//        }
//
//        public void setReceiving_dt(String receiving_dt) {
//            this.receiving_dt = receiving_dt;
//        }
//
//        public String getRemark() {
//            return remark;
//        }
//
//        public void setRemark(String remark) {
//            this.remark = remark;
//        }
//    }
//
//    public static class ListRecSAdapter extends RecyclerView.Adapter<ListRecSAdapter.ListRecSViewHolder> {
//        private ArrayList<ListRecS> waitItems;
//        private OnItemClickListener mListener;
//
//        @NonNull
//        @Override
//        public ListRecSViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_w_r,
//                    viewGroup, false);
//            ListRecSViewHolder evh = new ListRecSViewHolder(v, mListener);
//            return evh;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ListRecSViewHolder waitViewHolder, int i) {
//            ListRecS currentItem = waitItems.get(i);
//
//            waitViewHolder.dt_v.setText(currentItem.getReceiving_dt().length()>8?currentItem.getReceiving_dt():currentItem.getReceiving_dt().substring(0,4)+"-"+currentItem.getReceiving_dt().substring(4,6)+"-"+currentItem.getReceiving_dt().substring(6,8));
//            waitViewHolder.sd_v.setText(currentItem.getRd_no());
//            waitViewHolder.sm_v.setText(currentItem.getRd_nm());
//            waitViewHolder.re_v.setText(currentItem.getRemark().replace("null",""));
//        }
//
//        @Override
//        public int getItemCount() {
//            return waitItems.size();
//        }
//
//        public interface OnItemClickListener {
//            void onItemClick(int position);
//        }
//
//        public void setOnItemClickListener(OnItemClickListener listener) {
//            mListener = listener;
//        }
//
//        public static class ListRecSViewHolder extends RecyclerView.ViewHolder {
//
//            public TextView dt_v;
//            public TextView sd_v;
//            public TextView sm_v;
//            public TextView re_v;
//
//            public ListRecSViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
//                super(itemView);
//
//                dt_v = itemView.findViewById(R.id.dt_v);
//                sd_v = itemView.findViewById(R.id.sd_v);
//                sm_v = itemView.findViewById(R.id.sm_v);
//                re_v = itemView.findViewById(R.id.re_v);
//
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (listener != null) {
//                            int position = getAdapterPosition();
//                            if (position != RecyclerView.NO_POSITION) {
//                                listener.onItemClick(position);
//                            }
//                        }
//                    }
//                });
//            }
//        }
//
//        public ListRecSAdapter(ArrayList<ListRecS> waitItem) {
//            waitItems = waitItem;
//        }
//    }
}