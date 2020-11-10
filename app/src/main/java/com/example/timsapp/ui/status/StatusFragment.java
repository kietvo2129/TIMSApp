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
    private String ID_MLNO = "";
    private TextView tv_qe;
    private ImageView imex;

    ProgressDialog progressDialog;
    private ArrayList<ListStatus> listStatus = new ArrayList<>();
    private ListStatusAdaptor adaptor;
    private RecyclerView rv_status;

    public StatusFragment() {
        // Required empty public constructor
    }

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

        tv_qe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputText(tv_qe);
            }
        });
        imex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                try {
                    //{"result": false,"message": "Data has not exist, Please Scan Again!!!"}
                    //{"result": true,"message": "Success","Data": [
                    // {"gr_qty": 1500,"mt_type": "CMT","recevice_dt_tims": "20201020","mt_sts_nm": "Using","lct_nm": "TIMS"},
                    // {"gr_qty": 0,"mt_type": "CMT","recevice_dt_tims": null,"mt_sts_nm": "Defect","lct_nm": "TIMS"},
                    // {"gr_qty": 1000,"mt_type": "CMT","recevice_dt_tims": null,"mt_sts_nm": "Used/Sent","lct_nm": "Sản xuất 1"}]}

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
                                        jsonObject.getString("mt_type"),
                                        jsonObject.getString("recevice_dt_tims").replace("null", ""),
                                        jsonObject.getString("mt_sts_nm"),
                                        jsonObject.getString("lct_nm")
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
        public void onBindViewHolder(@NonNull ListStatusAdaptor.ListStatusViewHolder viewHolder, int i) {
            ListStatus currentItem = items.get(i);

            viewHolder.tv_bb_no_fs.setText(currentItem.getBibon());
            viewHolder.tv_qty_dt.setText(currentItem.getGr_qty());
            viewHolder.tv_st_st.setText(currentItem.getMt_sts_nm());
            viewHolder.tv_dep_st.setText(currentItem.getLct_nm());
            viewHolder.tv_mt_st.setText(currentItem.getMt_type());

            viewHolder.tv_dt_st.setText(currentItem.getRecdate().length() == 8
                    ? currentItem.getRecdate().substring(0, 4) + "-"
                    + currentItem.getRecdate().substring(4, 6) + "-"
                    + currentItem.getRecdate().substring(6, 8)
                    : currentItem.getRecdate());
            viewHolder.tv_stt_st.setText(String.valueOf(i+1));
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
            public TextView tv_bb_no_fs, tv_qty_dt, tv_st_st, tv_dep_st, tv_mt_st, tv_dt_st, tv_stt_st;

            public ListStatusViewHolder(@NonNull View itemView, final ListStatusAdaptor.OnItemClickListener listener) {
                super(itemView);
                tv_bb_no_fs = itemView.findViewById(R.id.tv_bb_no_fs);
                tv_qty_dt = itemView.findViewById(R.id.tv_qty_dt);
                tv_st_st = itemView.findViewById(R.id.tv_st_st);
                tv_dep_st = itemView.findViewById(R.id.tv_dep_st);
                tv_mt_st = itemView.findViewById(R.id.tv_mt_st);
                tv_dt_st = itemView.findViewById(R.id.tv_dt_st);
                tv_stt_st = itemView.findViewById(R.id.tv_stt_st);

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
        ID_MLNO = conText;
        String url = BaseApp.isHostting() + "/Tims/Get_Status_Bobin?bb_no=" + conText;
        loadJson(url, conText);
    }

    private void inputText(final TextView TextID) {
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
                TextID.setText(QrScan);
                if (QrScan.length() > 0) {
                    sendData(QrScan);
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
                tv_qe.setText(QrScan);
                if (QrScan.length() > 0) {
                    sendData(QrScan);// scan
                } else {
                    Toast.makeText(getActivity(), "Please insert QR code", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    private class ListStatus {
        String bibon, gr_qty, mt_type, recdate, mt_sts_nm, lct_nm;

        public ListStatus(String bibon, String gr_qty, String mt_type, String recdate, String mt_sts_nm, String lct_nm) {
            this.bibon = bibon;
            this.gr_qty = gr_qty;
            this.mt_type = mt_type;
            this.recdate = recdate;
            this.mt_sts_nm = mt_sts_nm;
            this.lct_nm = lct_nm;
        }

        public String getBibon() {
            return bibon;
        }

        public void setBibon(String bibon) {
            this.bibon = bibon;
        }

        public String getGr_qty() {
            return gr_qty;
        }

        public void setGr_qty(String gr_qty) {
            this.gr_qty = gr_qty;
        }

        public String getMt_type() {
            return mt_type;
        }

        public void setMt_type(String mt_type) {
            this.mt_type = mt_type;
        }

        public String getRecdate() {
            return recdate;
        }

        public void setRecdate(String recdate) {
            this.recdate = recdate;
        }

        public String getMt_sts_nm() {
            return mt_sts_nm;
        }

        public void setMt_sts_nm(String mt_sts_nm) {
            this.mt_sts_nm = mt_sts_nm;
        }

        public String getLct_nm() {
            return lct_nm;
        }

        public void setLct_nm(String lct_nm) {
            this.lct_nm = lct_nm;
        }
    }
}
