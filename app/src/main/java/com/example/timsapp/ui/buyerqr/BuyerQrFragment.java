package com.example.timsapp.ui.buyerqr;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.timsapp.ui.status.StatusFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuyerQrFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuyerQrFragment extends Fragment {

    public static final String IDBUYED = "IDBUYED";
    public static final String IDBOBBIN = "IDBOBBIN";

    private TextView tv_qe, tv_qe_buy;
    private ImageView imex, imex_buy;
    ProgressDialog progressDialog;
    private RecyclerView rv_status;
    private String idKey;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btn_map;
    private ArrayList<ListStatus> listStatus;
    private ListSAdaptor adaptor;


    public BuyerQrFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BuyerQrFragment newInstance(String param1, String param2) {
        BuyerQrFragment fragment = new BuyerQrFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buyer_qr, container, false);

        tv_qe = view.findViewById(R.id.tv_qe);
        imex = view.findViewById(R.id.imex);
        rv_status = view.findViewById(R.id.rv_buyer_qr);
        tv_qe_buy = view.findViewById(R.id.tv_qe_buy);
        imex_buy = view.findViewById(R.id.imex_buy);
        btn_map = view.findViewById(R.id.btn_map);

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

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
        return view;
    }

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
                    } else if (sid.equals(IDBOBBIN)) {
                        tv_qe.setText(QrScan);
                    } else {
                    }
                    //sendData(); // intext
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
                    if (idKey.equals(IDBUYED)) {
                        tv_qe_buy.setText(QrScan);
                    } else if (idKey.equals(IDBOBBIN)) {
                        tv_qe.setText(QrScan);
                    } else {
                    }
                    //sendData();
                } else {
                    Toast.makeText(getActivity(), "Please insert QR code", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void sendData() {
        if (tv_qe.getText().toString().length() == 0) {
            AlerError.Baoloi("input container qr!", getActivity());
            return;
        }
        if (tv_qe_buy.getText().toString().length() == 0) {
            AlerError.Baoloi("input buyer qr!", getActivity());
            return;
        }

        //$.get("/TIMS/mapping_buyer?bb_no=" + $("#bb_no").val() + "&buyer_qr=" + $("#buyer_qr").val(), function (data) {
        //            if (data.result == true) {
        //                SuccessAlert("Success");
        //                $.each(data.kq, function (key, item) {
        //                    var myList = $("#table_mapping_oqc").jqGrid('getDataIDs');
        //                    for (var i = 0; i < myList.length; i++) {
        //                        var rowData = $("#table_mapping_oqc").getRowData(myList[i]);
        //                        if (rowData.bb_no == bobin_buyer) {
        //                            rowData.buyer_qr = $("#buyer_qr").val();
        //                            $("#table_mapping_oqc").jqGrid('setRowData', myList[i], rowData);
        //                            $("#table_mapping_oqc").setRowData(myList[i], false, { background: "#d0e9c6" });
        //
        //                        }
        //                    }
        //                });
        //            } else {
        //                ErrorAlert(data.message);
        //            }
        //        });

        String url = BaseApp.isHostting() + "/TIMS/mapping_buyer?bb_no=" + tv_qe.getText().toString()
                + "&buyer_qr=" + tv_qe_buy.getText().toString();
        loadJson(url);

    }

    private void loadJson(String url) {
        Log.d("loadJson", url);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        //progressDialog.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("loadJson", response);
                //LJ63-17969BSTA20201019105417000001
                progressDialog.dismiss();
                listStatus = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("result")) {
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();

                        JSONArray jsonArray = object.getJSONArray("kq");
                        if (jsonArray.length() == 0) {
                            AlerError.Baoloi(object.getString("message"), getActivity());
                        } else {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                listStatus.add(new ListStatus(
                                        tv_qe_buy.getText().toString(),
                                        jsonObject.getString("blno"),
                                        jsonObject.getString("mc_type"),
                                        jsonObject.getString("bb_no"),
                                        jsonObject.getString("mt_cd"),
                                        jsonObject.getString("bb_nm"),
                                        jsonObject.getString("use_yn")
                                ));
                            }
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

    private class ListStatus {
        String buyerbo,blno, mc_type, bb_no, mt_cd, bb_nm, use_yn;

        public ListStatus(String buyerbo, String blno, String mc_type, String bb_no, String mt_cd, String bb_nm, String use_yn) {
            this.buyerbo = buyerbo;
            this.blno = blno;
            this.mc_type = mc_type;
            this.bb_no = bb_no;
            this.mt_cd = mt_cd;
            this.bb_nm = bb_nm;
            this.use_yn = use_yn;
        }

        public String getBuyerno() {
            return buyerbo;
        }

        public void setBuyerno(String buyerbo) {
            this.buyerbo = buyerbo;
        }

        public String getBlno() {
            return blno;
        }

        public void setBlno(String blno) {
            this.blno = blno;
        }

        public String getMc_type() {
            return mc_type;
        }

        public void setMc_type(String mc_type) {
            this.mc_type = mc_type;
        }

        public String getBb_no() {
            return bb_no;
        }

        public void setBb_no(String bb_no) {
            this.bb_no = bb_no;
        }

        public String getMt_cd() {
            return mt_cd;
        }

        public void setMt_cd(String mt_cd) {
            this.mt_cd = mt_cd;
        }

        public String getBb_nm() {
            return bb_nm;
        }

        public void setBb_nm(String bb_nm) {
            this.bb_nm = bb_nm;
        }

        public String getUse_yn() {
            return use_yn;
        }

        public void setUse_yn(String use_yn) {
            this.use_yn = use_yn;
        }
    }

    private void builRecyclerView() {
        adaptor = new ListSAdaptor(listStatus);
        rv_status.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_status.setHasFixedSize(true);
        rv_status.setAdapter(adaptor);
    }

    public static class ListSAdaptor extends RecyclerView.Adapter<ListSAdaptor.ListSViewHolder> {
        private ArrayList<ListStatus> items;
        private ListSAdaptor.OnItemClickListener mListener;

        @NonNull
        @Override
        public ListSAdaptor.ListSViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_buyer_qr,
                    viewGroup, false);
            ListSAdaptor.ListSViewHolder evh = new ListSAdaptor.ListSViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListSAdaptor.ListSViewHolder vh, int i) {
            ListStatus currentItem = items.get(i);

            vh.v_stt.setText(String.valueOf(i + 1));
            vh.v_buyer.setText(currentItem.getBuyerno());

            vh.v_mt_cd.setText(currentItem.getMt_cd());
            vh.v_bb.setText(currentItem.getBb_no());

            vh.v_bb_nm.setText(currentItem.getBb_nm());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public void setOnItemClickListener(ListSAdaptor.OnItemClickListener listener) {
            mListener = listener;
        }

        public static class ListSViewHolder extends RecyclerView.ViewHolder {
            public TextView v_stt, v_buyer, v_mt_cd, v_bb, v_bb_nm;

            public ListSViewHolder(@NonNull View itemView, final ListSAdaptor.OnItemClickListener listener) {
                super(itemView);

                v_stt = itemView.findViewById(R.id.v_stt);
                v_buyer = itemView.findViewById(R.id.v_buyer);
                v_mt_cd = itemView.findViewById(R.id.v_mt_cd);
                v_bb = itemView.findViewById(R.id.v_bb);
                v_bb_nm = itemView.findViewById(R.id.v_bb_nm);

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

        public ListSAdaptor(ArrayList<ListStatus> waitItem) {
            items = waitItem;
        }
    }

}