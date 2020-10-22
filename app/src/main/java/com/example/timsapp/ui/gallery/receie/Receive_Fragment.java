package com.example.timsapp.ui.gallery.receie;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.example.timsapp.R;
import com.example.timsapp.Url;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.ui.gallery.receie.RecScanActivity.KEYSDNO;
import static com.example.timsapp.ui.gallery.receie.RecScanActivity.KEYSID;


public class Receive_Fragment extends Fragment {

    private EditText edt_sd, edt_sm;
    private Button btn_serh;
    private RecyclerView rv_rc_d;
    private ProgressDialog progressDialog;
    private ArrayList<ListRecS> listRecS;
    private ListRecSAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_receive, container, false);
        getActivity().setTitle("Receiving Scan(TIMS)");

        //innit
        edt_sd = view.findViewById(R.id.edt_sd);
        edt_sm = view.findViewById(R.id.edt_sm);
        btn_serh = view.findViewById(R.id.btn_serh);
        rv_rc_d = view.findViewById(R.id.rv_rc_d);

        //action
//        Search();

        btn_serh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search();
            }
        });

        return view;
    }

    private void Search() {

        String url = Url.webUrl + "/TIMS/GetRDInfo?rd_no="
                + edt_sd.getText().toString().trim() + "&rd_nm=" + edt_sm.getText().toString().trim();
        serJson(url);
    }

    private void serJson(String url) {
        Log.d("serJson", url);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("serJson", response);
                listRecS = new ArrayList<ListRecS>();

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {

                        AlerError.Baoloi("Data don't have!!!", getActivity());
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);

                            listRecS.add(new ListRecS(
                                    object.getString("rid"),
                                    object.getString("rd_no"),
                                    object.getString("rd_nm"),
                                    object.getString("rd_sts_cd"),
                                    object.getString("lct_cd"),
                                    object.getString("receiving_dt"),
                                    object.getString("remark")
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

    private void buildRV() {
        adapter = new ListRecSAdapter(listRecS);
        rv_rc_d.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_rc_d.setHasFixedSize(true);
        rv_rc_d.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListRecSAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Toast.makeText(getActivity(), "d" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), RecScanActivity.class);
                intent.putExtra(KEYSDNO, listRecS.get(position).getRd_no());
                intent.putExtra(KEYSID, listRecS.get(position).getRid());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Search();
    }

    private class ListRecS {
        String rid, rd_no, rd_nm, rd_sts_cd, lct_cd, receiving_dt, remark;

        public ListRecS(String rid, String rd_no, String rd_nm, String rd_sts_cd, String lct_cd, String receiving_dt, String remark) {
            this.rid = rid;
            this.rd_no = rd_no;
            this.rd_nm = rd_nm;
            this.rd_sts_cd = rd_sts_cd;
            this.lct_cd = lct_cd;
            this.receiving_dt = receiving_dt;
            this.remark = remark;
        }

        public String getRid() {
            return rid;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public String getRd_no() {
            return rd_no;
        }

        public void setRd_no(String rd_no) {
            this.rd_no = rd_no;
        }

        public String getRd_nm() {
            return rd_nm;
        }

        public void setRd_nm(String rd_nm) {
            this.rd_nm = rd_nm;
        }

        public String getRd_sts_cd() {
            return rd_sts_cd;
        }

        public void setRd_sts_cd(String rd_sts_cd) {
            this.rd_sts_cd = rd_sts_cd;
        }

        public String getLct_cd() {
            return lct_cd;
        }

        public void setLct_cd(String lct_cd) {
            this.lct_cd = lct_cd;
        }

        public String getReceiving_dt() {
            return receiving_dt;
        }

        public void setReceiving_dt(String receiving_dt) {
            this.receiving_dt = receiving_dt;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    public static class ListRecSAdapter extends RecyclerView.Adapter<ListRecSAdapter.ListRecSViewHolder> {
        private ArrayList<ListRecS> waitItems;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ListRecSViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_w_r,
                    viewGroup, false);
            ListRecSViewHolder evh = new ListRecSViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListRecSViewHolder waitViewHolder, int i) {
            ListRecS currentItem = waitItems.get(i);

            waitViewHolder.dt_v.setText(currentItem.getReceiving_dt().length()>8?currentItem.getReceiving_dt():currentItem.getReceiving_dt().substring(0,4)+"-"+currentItem.getReceiving_dt().substring(4,6)+"-"+currentItem.getReceiving_dt().substring(6,8));
            waitViewHolder.sd_v.setText(currentItem.getRd_no());
            waitViewHolder.sm_v.setText(currentItem.getRd_nm());
            waitViewHolder.re_v.setText(currentItem.getRemark().replace("null",""));
        }

        @Override
        public int getItemCount() {
            return waitItems.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class ListRecSViewHolder extends RecyclerView.ViewHolder {

            public TextView dt_v;
            public TextView sd_v;
            public TextView sm_v;
            public TextView re_v;

            public ListRecSViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);

                dt_v = itemView.findViewById(R.id.dt_v);
                sd_v = itemView.findViewById(R.id.sd_v);
                sm_v = itemView.findViewById(R.id.sm_v);
                re_v = itemView.findViewById(R.id.re_v);

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

        public ListRecSAdapter(ArrayList<ListRecS> waitItem) {
            waitItems = waitItem;
        }
    }
}