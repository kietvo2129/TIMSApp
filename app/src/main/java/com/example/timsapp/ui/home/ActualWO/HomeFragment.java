package com.example.timsapp.ui.home.ActualWO;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timsapp.AlerError.AlerError;
import com.example.timsapp.R;
import com.example.timsapp.Url;

import com.example.timsapp.ui.home.Composite.ItemCompositeAdapter;
import com.example.timsapp.ui.home.Manufacturing.ManufacturingActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class HomeFragment extends Fragment {
    String webUrl = Url.webUrl;
    RecyclerView recyclerView;
    int total=-1;
    int page =1;
    private ProgressDialog dialog;
    ArrayList<ActualWOHomeMaster> actualWOMasterArrayList;
    ActualWOHomeAdapter actualWOHomeAdapter;

    public static String at_no ="";
    public static String product ="";

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        dialog = new ProgressDialog(getContext(),R.style.AlertDialogCustom);

        getData(page);
        return root;
    }
    private void getData(int page) {
        new getData().execute(webUrl+ "TIMS/Getdataw_actual_primary?rows=50&page="+ page+"&sidx=&sord=asc");
        Log.e("getData",webUrl+ "TIMS/Getdataw_actual_primary?rows=50&page="+ page+"&sidx=&sord=asc");
    }
    private class getData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            actualWOMasterArrayList = new ArrayList<>();
            String id_actualpr,at_no,type,product,remark,style_nm;
            int target;

            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", getContext());
                    return;
                }

                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    id_actualpr = objectRow.getString("id_actualpr");
                    at_no = objectRow.getString("at_no");
                    type = objectRow.getString("type");
                    product = objectRow.getString("product");
                    remark = objectRow.getString("remark").replace("null","");
                    target = objectRow.getInt("target");
                    style_nm = objectRow.getString("style_nm");
                    actualWOMasterArrayList.add(new ActualWOHomeMaster(id_actualpr,at_no,type,product,remark,style_nm,target));
                }
                dialog.dismiss();
                setListView();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", getContext());
                dialog.dismiss();
            }
        }

    }

    private void setListView() {
        dialog.dismiss();
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        actualWOHomeAdapter = new ActualWOHomeAdapter(actualWOMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(actualWOHomeAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == actualWOMasterArrayList.size() - 1) {
                    if (page < total) {
                        total = -1;
                        getaddData(page + 1);

                    }
                }
            }
        });
        actualWOHomeAdapter.setOnItemClickListener(new ItemCompositeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                at_no = actualWOMasterArrayList.get(position).at_no;
                product = actualWOMasterArrayList.get(position).product;
                Intent intent = new Intent(getContext(), ManufacturingActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getaddData(int page) {
        new getaddData().execute(webUrl+ "TIMS/Getdataw_actual_primary?rows=50&page="+ page+"&sidx=&sord=asc");
        Log.e("getaddData",webUrl+ "TIMS/Getdataw_actual_primary?rows=50&page="+ page+"&sidx=&sord=asc");
    }

    private class getaddData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(true);
            dialog.show();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String id_actualpr,at_no,type,product,remark,style_nm;
            int target;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", getContext());
                    return;
                }

                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    id_actualpr = objectRow.getString("id_actualpr");
                    at_no = objectRow.getString("at_no");
                    type = objectRow.getString("type");
                    product = objectRow.getString("product");
                    remark = objectRow.getString("remark").replace("null","");
                    target = objectRow.getInt("target");
                    style_nm = objectRow.getString("style_nm");
                    actualWOMasterArrayList.add(new ActualWOHomeMaster(id_actualpr,at_no,type,product,remark,style_nm,target));
                }
                dialog.dismiss();
                actualWOHomeAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", getContext());
                dialog.dismiss();
            }
        }

    }
}