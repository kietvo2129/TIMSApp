package com.example.timsapp.ui.slideshow.shipk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.timsapp.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.ui.slideshow.shipk.TimsShipScanActivity.KEYEET;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShipFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView rv_ts_d;
    private Button btn_serh;
    private EditText tv_extno, tv_extnm;
    private ProgressDialog progressDialog;
    private ArrayList<ListTS> listTS;
    private ListTSAdapter adapter;
    private RecyclerView rvViewChil;
    private ArrayList<ListDetailChil> listDetailChil;
    private ListDetailChilAdapter listDetailChilAdapter;


    public ShipFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShipFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShipFragment newInstance(String param1, String param2) {
        ShipFragment fragment = new ShipFragment();
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
        View view = inflater.inflate(R.layout.fragment_ship, container, false);

        tv_extno = view.findViewById(R.id.tv_extno);
        tv_extnm = view.findViewById(R.id.tv_extnm);
        btn_serh = view.findViewById(R.id.btn_serh);
        rv_ts_d = view.findViewById(R.id.rv_ts_d);

        //
        Searcj();

        btn_serh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Searcj();
            }
        });

        return view;
    }

    private void Searcj() {
        String url = BaseApp.isHostting() + "/TIMS/GetEXTInfo?ext_no="
                +tv_extno.getText().toString().trim()+"&ext_nm="+tv_extnm.getText().toString().trim();

        searchJson(url);
    }
    private void searchJson(String url) {
        Log.d("serJson", url);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("serJson", response);
                listTS = new ArrayList<ListTS>();
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        AlerError.Baoloi("Data don't have!!!", getActivity());
                    } else {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            listTS.add(new ListTS(
                                    object.getString("extid"),
                                    object.getString("ext_no"),
                                    object.getString("ext_nm"),
                                    object.getString("work_dt"),
                                    object.getString("remark")
                            ));
                        }
                    }
                    buildRV();
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
        adapter = new ListTSAdapter(listTS);
        rv_ts_d.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_ts_d.setHasFixedSize(true);
        rv_ts_d.setAdapter(adapter);

        adapter.setOnItemClickListener(new ListTSAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                 //Toast.makeText(getActivity(), "_ " + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TimsShipScanActivity.class);
                intent.putExtra(KEYEET, listTS.get(position).getExt_no());
                startActivity(intent);
            }

            @Override
            public void onButtonClick(int position, RecyclerView recyclerView) {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    makeRecyclerChil(position, recyclerView);
                }
            }
        });
    }

    private void makeRecyclerChil(int position, RecyclerView recyclerView) {
        listDetailChil = new ArrayList<>();
        rvViewChil = recyclerView;
        String url = BaseApp.isHostting() + "/TIMS/GetTimsShippingScanListPP?ext_no=" + listTS.get(position).getExt_no();
        chilJson(url);
        buildRVdetailChil();
    }

    private void buildRVdetailChil() {
        listDetailChilAdapter = new ListDetailChilAdapter(listDetailChil);
        rvViewChil.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvViewChil.setHasFixedSize(true);
        rvViewChil.setAdapter(listDetailChilAdapter);
    }

    private void chilJson(String url) {
        Log.d("upJson", url);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("upJson", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray array = jsonObject.getJSONArray("data");
                    if (array.length() ==0){
                        AlerError.Baoloi("Don't have data",getActivity());
                        progressDialog.dismiss();
                        return;
                    }
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);

                        listDetailChil.add(new ListDetailChil(
                                object.getString("mt_cd"),
                                object.getString("bb_no"),
                                object.getString("recevice_dt_tims"),
                                object.getString("from_lct_nm").replace("]","").replace("[","").replace("\"",""),
                                object.getString("lct_sts_cd").replace("]","").replace("[","").replace("\"",""),
                                object.getString("gr_qty"),
                                object.getString("mt_type_nm").replace("]","").replace("[","").replace("\"",""),
                                object.getString("sts_nm").replace("]","").replace("[","").replace("\"","")
                        ));
                    }
                    buildRVdetailChil();
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
                AlerError.Baoloi("The server error:" + error.toString(),  getActivity());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue( getActivity());
        requestQueue.add(stringRequest);
    }

    public static class ListDetailChilAdapter extends RecyclerView.Adapter<ListDetailChilAdapter.ListDetailChilViewHolder> {
        private ArrayList<ListDetailChil> items;
        private ListDetailChilAdapter.OnItemClickListener mListener;

        @NonNull
        @Override
        public ListDetailChilViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ts_chi,
                    viewGroup, false);
            ListDetailChilViewHolder evh = new ListDetailChilViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListDetailChilViewHolder vh, int i) {
            ListDetailChil currentItem = items.get(i);

            vh.ml_x.setText(currentItem.getMt_cd());
            vh.bb_x.setText(currentItem.getBb_no());
            vh.mt_x.setText(currentItem.getMt_type_nm());

            vh.st_x.setText(currentItem.getSts_nm());
            vh.qty_x.setText(currentItem.getGr_qty());
            vh.dp_x.setText(currentItem.getFrom_lct_nm());
            vh.ds_i.setText(currentItem.getLct_sts_cd());
            vh.r_dt.setText(currentItem.getRecevice_dt_tims().length()==12?currentItem.getRecevice_dt_tims()
                    :currentItem.getRecevice_dt_tims().substring(0,4)+"-"+currentItem.getRecevice_dt_tims().substring(4,6)
                    +"-"+currentItem.getRecevice_dt_tims().substring(6,8));

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);
        }

        public void setOnItemClickListener(ListDetailChilAdapter.OnItemClickListener listener) {
            mListener = listener;
        }

        public static class ListDetailChilViewHolder extends RecyclerView.ViewHolder {
            public TextView ml_x;
            public TextView bb_x;
            public TextView mt_x;

            public TextView st_x;
            public TextView qty_x;

            public TextView dp_x;
            public TextView ds_i;
            public TextView r_dt;

            public ListDetailChilViewHolder(@NonNull View itemView, final ListDetailChilAdapter.OnItemClickListener listener) {
                super(itemView);
                ml_x = itemView.findViewById(R.id.ml_x);
                bb_x = itemView.findViewById(R.id.bb_x);
                st_x = itemView.findViewById(R.id.st_x);

                mt_x = itemView.findViewById(R.id.mt_x);
                qty_x = itemView.findViewById(R.id.qty_x);

                ds_i = itemView.findViewById(R.id.ds_i);
                dp_x = itemView.findViewById(R.id.dp_x);
                r_dt = itemView.findViewById(R.id.r_dt);

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

        public ListDetailChilAdapter(ArrayList<ListDetailChil> waitItem) {
            items = waitItem;
        }
    }


    public static class ListTSAdapter extends RecyclerView.Adapter<ListTSAdapter.ListTSViewHolder> {
        private ArrayList<ListTS> waitItems;
        private OnItemClickListener mListener;

        @NonNull
        @Override
        public ListTSViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_t_s,
                    viewGroup, false);
            ListTSViewHolder evh = new ListTSViewHolder(v, mListener);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ListTSViewHolder waitViewHolder, int i) {
            ListTS currentItem = waitItems.get(i);

            waitViewHolder.ext_no.setText(currentItem.getExt_no());
            waitViewHolder.ext_nm.setText(currentItem.getExt_nm());
            waitViewHolder.ext_dt.setText(currentItem.getWork_dt().length()==12?currentItem.getWork_dt():
                    currentItem.getWork_dt().substring(0,4)+"-"+currentItem.getWork_dt().substring(4,6)+
                            "-"+currentItem.getWork_dt().substring(6,8));

            waitViewHolder.ext_re.setText(currentItem.getRemark().replace("null",""));
        }

        @Override
        public int getItemCount() {
            return waitItems.size();
        }

        public interface OnItemClickListener {
            void onItemClick(int position);

            void onButtonClick(int position, RecyclerView recyclerView);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        public static class ListTSViewHolder extends RecyclerView.ViewHolder {
            public TextView ext_no;
            public TextView ext_nm;
            public TextView ext_dt;
            public TextView ext_re;
            public RecyclerView rv_ts;

            public ListTSViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);

                ext_no = itemView.findViewById(R.id.ext_no);
                ext_nm = itemView.findViewById(R.id.ext_nm);
                ext_dt = itemView.findViewById(R.id.ext_dt);
                ext_re = itemView.findViewById(R.id.ext_re);
                rv_ts = itemView.findViewById(R.id.rv_ts);

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
                ext_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            int position = getAdapterPosition();
                            if (position != RecyclerView.NO_POSITION) {
                                listener.onButtonClick(position, rv_ts);
                            }
                        }
                    }
                });
            }
        }

        public ListTSAdapter(ArrayList<ListTS> waitItem) {
            waitItems = waitItem;
        }
    }

    private class ListTS {
        String extid,  ext_no,  ext_nm,  work_dt,  remark;

        public ListTS(String extid, String ext_no, String ext_nm, String work_dt, String remark) {
            this.extid = extid;
            this.ext_no = ext_no;
            this.ext_nm = ext_nm;
            this.work_dt = work_dt;
            this.remark = remark;
        }

        public String getExtid() {
            return extid;
        }

        public void setExtid(String extid) {
            this.extid = extid;
        }

        public String getExt_no() {
            return ext_no;
        }

        public void setExt_no(String ext_no) {
            this.ext_no = ext_no;
        }

        public String getExt_nm() {
            return ext_nm;
        }

        public void setExt_nm(String ext_nm) {
            this.ext_nm = ext_nm;
        }

        public String getWork_dt() {
            return work_dt;
        }

        public void setWork_dt(String work_dt) {
            this.work_dt = work_dt;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    private class ListDetailChil {
        String mt_cd, bb_no, recevice_dt_tims, from_lct_nm, lct_sts_cd, gr_qty, mt_type_nm, sts_nm;

        public ListDetailChil(String mt_cd, String bb_no, String recevice_dt_tims, String from_lct_nm, String lct_sts_cd, String gr_qty, String mt_type_nm, String sts_nm) {
            this.mt_cd = mt_cd;
            this.bb_no = bb_no;
            this.recevice_dt_tims = recevice_dt_tims;
            this.from_lct_nm = from_lct_nm;
            this.lct_sts_cd = lct_sts_cd;
            this.gr_qty = gr_qty;
            this.mt_type_nm = mt_type_nm;
            this.sts_nm = sts_nm;
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

        public String getGr_qty() {
            return gr_qty;
        }

        public void setGr_qty(String gr_qty) {
            this.gr_qty = gr_qty;
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