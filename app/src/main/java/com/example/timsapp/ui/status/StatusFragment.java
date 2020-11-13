package com.example.timsapp.ui.status;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.timsapp.BaseApp;
import com.example.timsapp.R;
import com.example.timsapp.Url;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatusFragment extends Fragment {
    public static final String IDBUYED = "IDBUYED";
    public static final String IDBOBBIN = "IDBOBBIN";

    private TextView tv_qe, tv_qe_buy;
    private ImageView imex, imex_buy;
    ProgressDialog progressDialog;
    private ArrayList<ListStatus> listStatus;
    private ListStatusAdaptor adaptor;
    private RecyclerView rv_status;
    private String idKey;
    private ArrayList<ListStatus> listBStatus;
    private ListBStatusAdaptor adaptorB;

    public StatusFragment() {
        // Required empty public constructor
    }
    ///Tims/Get_Status_Bobin?bb_no=BOBBIN-006

    ///Tims/Get_Status_Buyer?buyerCode=hunghehe
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        getActivity().setTitle("Status");

        // http://192.168.100.53:81/wipwms/CheckStatusMaterial?ml_no=

        tv_qe = view.findViewById(R.id.tv_qe);
        imex = view.findViewById(R.id.imex);
        rv_status = view.findViewById(R.id.rv_status);
        tv_qe_buy = view.findViewById(R.id.tv_qe_buy);
        imex_buy = view.findViewById(R.id.imex_buy);

        tv_qe_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText(IDBUYED);
            }
        });
        imex_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idKey = IDBUYED;
                startQRScanner();
            }
        });

        tv_qe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText(IDBOBBIN);
            }
        });
        imex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idKey = IDBOBBIN;
                startQRScanner();
            }
        });

        return view;
    }

    private void loadJson(String url, final String bibon) {
        Log.d("LoadMaterialInformation", url);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        //progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("LoadMaterialInformation", response);
                //LJ63-17969BSTA20201019105417000001
                progressDialog.dismiss();
                listStatus = new ArrayList<>();
                try {

                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("result")) {
                        JSONArray jsonArray = object.getJSONArray("Data");
                        if (jsonArray.length() == 0) {
                            AlerError.Baoloi(object.getString("message"), getActivity());
                        } else {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                listStatus.add(new ListStatus(
                                        bibon,
                                        jsonObject.getString("gr_qty"),
                                        jsonObject.getString("gr_qty_bf"),
                                        jsonObject.getString("process"),
                                        jsonObject.getString("mt_type"),
                                        jsonObject.getString("recevice_dt_tims"),
                                        jsonObject.getString("mt_sts_nm"),
                                        jsonObject.getString("lct_nm"),
                                        jsonObject.getString("mt_cd"),
                                        jsonObject.getString("po"),
                                        jsonObject.getString("product"),
                                        jsonObject.getString("staff_id"),
                                        jsonObject.getString("staff_nm")
                                ));
                            }
                            Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            builRecyclerView();
                        }
                    } else {
                        AlerError.Baoloi(object.getString("message"), getActivity());
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

    private void builRecyclerView() {
        adaptor = new ListStatusAdaptor(listStatus);
        rv_status.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_status.setHasFixedSize(true);
        rv_status.setAdapter(adaptor);
    }

    public static class ListStatusAdaptor extends RecyclerView.Adapter<ListStatusAdaptor.ListStatusViewHolder> {
        private ArrayList<ListStatus> items;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ListStatusAdaptor.ListStatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fm_st,
                    viewGroup, false);
            ListStatusAdaptor.ListStatusViewHolder evh = new ListStatusAdaptor.ListStatusViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListStatusAdaptor.ListStatusViewHolder vh, int i) {
            ListStatus currentItem = items.get(i);
            vh.v_rec_dt.setText(currentItem.getRecevice_dt_tims().length() == 8
                    ? currentItem.getRecevice_dt_tims().substring(0, 4) + "-"
                    + currentItem.getRecevice_dt_tims().substring(4, 6) + "-"
                    + currentItem.getRecevice_dt_tims().substring(6, 8)
                    : currentItem.getRecevice_dt_tims());
            vh.v_stt.setText(String.valueOf(i + 1));
            vh.v_bobbin_no.setText(currentItem.getBibon());
            vh.v_qty.setText(currentItem.getGr_qty());
            vh.v_qty_bf.setText(currentItem.getGr_qty_bf());
            vh.v_mt_cd.setText(currentItem.getMt_cd());
            vh.v_product.setText(currentItem.getProduct());
            vh.v_po_no.setText(currentItem.getPo());
            vh.v_process.setText(currentItem.getProcess());
            vh.v_type.setText(currentItem.getMt_type());
            vh.v_lct_nm.setText(currentItem.getLct_nm());
            vh.v_rec_dt.setText(currentItem.getLct_nm());
            vh.v_staff_id.setText(currentItem.getStaff_id());
            vh.v_staff_nm.setText(currentItem.getStaff_nm());
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

        public static class ListStatusViewHolder extends RecyclerView.ViewHolder {
            public TextView v_stt, v_bobbin_no;
            public TextView v_qty, v_qty_bf;
            public TextView v_mt_cd, v_product, v_po_no, v_process, v_type, v_lct_nm;
            public TextView v_rec_dt, v_staff_id, v_staff_nm;


            public ListStatusViewHolder(@NonNull View itemView, final ListStatusAdaptor.OnItemClickListener listener) {
                super(itemView);

                v_stt = itemView.findViewById(R.id.v_stt);
                v_bobbin_no = itemView.findViewById(R.id.v_bobbin_no);
                v_qty = itemView.findViewById(R.id.v_qty);
                v_qty_bf = itemView.findViewById(R.id.v_qty_bf);
                v_mt_cd = itemView.findViewById(R.id.v_mt_cd);
                v_product = itemView.findViewById(R.id.v_product);
                v_po_no = itemView.findViewById(R.id.v_po_no);
                v_process = itemView.findViewById(R.id.v_process);
                v_type = itemView.findViewById(R.id.v_type);
                v_lct_nm = itemView.findViewById(R.id.v_lct_nm);
                v_rec_dt = itemView.findViewById(R.id.v_rec_dt);
                v_staff_id = itemView.findViewById(R.id.v_staff_id);
                v_staff_nm = itemView.findViewById(R.id.v_staff_nm);

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

        public ListStatusAdaptor(ArrayList<ListStatus> waitItem) {
            items = waitItem;
        }
    }

    private void sendData(String conText) {
        String url = BaseApp.isHostting() + "/Tims/Get_Status_Bobin?bb_no=" + conText;
        loadJson(url, conText);
    }

    //
    private void loadJsonBuy(String url, final String bibon) {
        Log.d("loadJsonBuy", url);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        //progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("loadJsonBuy", response);
                //LJ63-17969BSTA20201019105417000001
                progressDialog.dismiss();
                listBStatus = new ArrayList<>();
                try {

                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("result")) {
                        JSONArray jsonArray = object.getJSONArray("Data");
                        if (jsonArray.length() == 0) {
                            AlerError.Baoloi(object.getString("message"), getActivity());
                        } else {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                listBStatus.add(new ListStatus(
                                        bibon,
                                        jsonObject.getString("gr_qty"),
                                        jsonObject.getString("gr_qty_bf"),
                                        ("process"),
                                        ("mt_type"),
                                        ("recevice_dt_tims"),
                                        jsonObject.getString("mt_sts_nm"),
                                        ("lct_nm"),
                                        ("mt_cd"),
                                        jsonObject.getString("po"),
                                        jsonObject.getString("product"),
                                        ("staff_id"),
                                        ("staff_nm")
                                ));
                            }
                            Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                            builBRecyclerView();
                        }
                    } else {
                        AlerError.Baoloi(object.getString("message"), getActivity());
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

    private void builBRecyclerView() {
        adaptorB = new ListBStatusAdaptor(listBStatus);
        rv_status.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_status.setHasFixedSize(true);
        rv_status.setAdapter(adaptorB);
    }

    public static class ListBStatusAdaptor extends RecyclerView.Adapter<ListBStatusAdaptor.ListBStatusViewHolder> {
        private ArrayList<ListStatus> items;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ListBStatusAdaptor.ListBStatusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_buy_sd,
                    viewGroup, false);
            ListBStatusAdaptor.ListBStatusViewHolder evh = new ListBStatusAdaptor.ListBStatusViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListBStatusAdaptor.ListBStatusViewHolder vh, int i) {
            ListStatus currentItem = items.get(i);

            vh.v_stt.setText(String.valueOf(i + 1));
            vh.v_bobbin_no.setText(currentItem.getBibon());

            vh.v_qty.setText(currentItem.getGr_qty());
            vh.v_qty_bf.setText(currentItem.getGr_qty_bf());

            vh.v_product.setText(currentItem.getProduct());
            vh.v_po_no.setText(currentItem.getPo());
            vh.v_sts_nm.setText(currentItem.getProcess());

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

        public static class ListBStatusViewHolder extends RecyclerView.ViewHolder {
            public TextView v_stt, v_bobbin_no;
            public TextView v_qty, v_qty_bf;
            public TextView  v_product, v_po_no,  v_sts_nm;

            public ListBStatusViewHolder(@NonNull View itemView, final ListBStatusAdaptor.OnItemClickListener listener) {
                super(itemView);

                v_stt = itemView.findViewById(R.id.v_stt);
                v_bobbin_no = itemView.findViewById(R.id.v_bobbin_no);
                v_qty = itemView.findViewById(R.id.v_qty);
                v_qty_bf = itemView.findViewById(R.id.v_qty_bf);
                v_product = itemView.findViewById(R.id.v_product);
                v_po_no = itemView.findViewById(R.id.v_po_no);

                v_sts_nm = itemView.findViewById(R.id.v_sts_nm);

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

        public ListBStatusAdaptor(ArrayList<ListStatus> waitItem) {
            items = waitItem;
        }
    }

    private void sendDatabuy(String conText) {
        String url = BaseApp.isHostting() + "/Tims/Get_Status_Buyer?buyerCode=" + conText;
        loadJsonBuy(url, conText);
    }

    //
    private void inputText(final String sid) {
        //AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AlertDialogCustom));
        builder.setTitle("Qr Code");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_layout, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setCancelable(false);
        builder.setView(viewInflated);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String QrScan = input.getText().toString().trim();
                if (QrScan.length() > 0) {
                    if (sid.equals(IDBUYED)) {
                        tv_qe_buy.setText(QrScan);
                        sendDatabuy(QrScan);
                    } else {
                        tv_qe.setText(QrScan);
                        sendData(QrScan);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please insert QR code", Toast.LENGTH_SHORT).show();
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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_LONG).show();
            } else {
                String QrScan = result.getContents().trim();

                if (QrScan.length() > 0) {
                    if(idKey.equals(IDBUYED)) {
                        tv_qe_buy.setText(QrScan);
                        sendDatabuy(QrScan);// scan
                    } else {
                        tv_qe.setText(QrScan);
                        sendData(QrScan);// scan
                    }
                } else {
                    Toast.makeText(getActivity(), "Please insert QR code", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class ListStatus {
        String bibon, gr_qty, gr_qty_bf, process, mt_type, recevice_dt_tims, mt_sts_nm,
                lct_nm, mt_cd, po, product, staff_id, staff_nm;

        public ListStatus(String bibon, String gr_qty, String gr_qty_bf, String process, String mt_type,
                          String recevice_dt_tims, String mt_sts_nm, String lct_nm, String mt_cd,
                          String po, String product, String staff_id, String staff_nm) {
            this.bibon = bibon;
            this.gr_qty = gr_qty;
            this.gr_qty_bf = gr_qty_bf;
            this.process = process;
            this.mt_type = mt_type;
            this.recevice_dt_tims = recevice_dt_tims;
            this.mt_sts_nm = mt_sts_nm;
            this.lct_nm = lct_nm;
            this.mt_cd = mt_cd;
            this.po = po;
            this.product = product;
            this.staff_id = staff_id;
            this.staff_nm = staff_nm;
        }

        public String getBibon() {
            return bibon;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public String getGr_qty_bf() {
            return gr_qty_bf;
        }

        public String getProcess() {
            return process;
        }

        public String getMt_type() {
            return mt_type;
        }

        public String getRecevice_dt_tims() {
            return recevice_dt_tims;
        }

        public String getMt_sts_nm() {
            return mt_sts_nm;
        }

        public String getLct_nm() {
            return lct_nm;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public String getPo() {
            return po;
        }

        public String getProduct() {
            return product;
        }

        public String getStaff_id() {
            return staff_id;
        }

        public String getStaff_nm() {
            return staff_nm;
        }
    }
}
