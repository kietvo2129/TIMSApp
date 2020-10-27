package com.example.timsapp.ui.home.ActualWO;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timsapp.AlerError.AlerError;
import com.example.timsapp.R;
import com.example.timsapp.Url;

import com.example.timsapp.ui.home.Composite.ItemCompositeAdapter;
import com.example.timsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class HomeFragment extends Fragment {
    String webUrl = Url.webUrl;
    RecyclerView recyclerView;
    int total=-1;
    int page =1;
    private ProgressDialog dialog;
    ArrayList<ActualWOHomeMaster> actualWOMasterArrayList;
    ActualWOHomeAdapter actualWOHomeAdapter;
    EditText Containercode;
    public static String at_no ="";
    public static String product ="";

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        dialog = new ProgressDialog(getContext(),R.style.AlertDialogCustom);

        root.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_pp_create();
            }
        });
        getData(page);
        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void open_pp_create() {

        final Dialog dialog = new Dialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.popup_create_first, null);
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
/// click drawable in TextInputEditText

        Containercode = dialog.findViewById(R.id.Containercode);
        Containercode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (Containercode.getRight() - Containercode.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        openscan();
                        return true;
                    }
                }
                return false;
            }
        });
/////
        final EditText num_div = dialog.findViewById(R.id.num_div);
        final Button confirm = dialog.findViewById(R.id.confirm);
        final EditText remark = dialog.findViewById(R.id.remark);
        final TextInputLayout h2;
        final TextInputLayout h1;
        final TextInputLayout h3;
        h1 = dialog.findViewById(R.id.H1);
        h2 = dialog.findViewById(R.id.H2);
        h3 = dialog.findViewById(R.id.H3);




        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num_div.getText().toString() == null || num_div.getText().toString().length() == 0) {
                    h2.setErrorEnabled(true);
                    h1.setError(null);
                    h3.setError(null);
                    h2.setError("Please, Input here.");
                    return;
                } else if (Containercode.getText().toString() == null || Containercode.getText().toString().length() == 0) {

                    h2.setError(null);
                    h1.setErrorEnabled(true);
                    h3.setError(null);
                    h1.setError("Please, Input here.");
                    return;
                } else {
                    h1.setError(null);
                    h2.setError(null);
                    h3.setError(null);
                    new create().execute(webUrl+"TIMS/CreateTIMSActual?productCode=" + Containercode.getText().toString() +"&target="+num_div.getText().toString()
                            +"&remark="+remark.getText().toString());
                    Log.e("create",webUrl+"TIMS/CreateTIMSActual?productCode=" + Containercode.getText().toString() +"&target="+num_div.getText().toString()
                            +"&remark="+remark.getText().toString());
                    dialog.dismiss();
                }


            }
        });


        dialog.show();
    }
    private class create extends AsyncTask<String, Void, String> {
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

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("result")){
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    getData(1);
                }else {
                    AlerError.Baoloi(jsonObject.getString("kq"), getContext());
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", getContext());
                dialog.dismiss();
            }
        }

    }

    private void openscan() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Containercode.setText(result.getContents());
            }
        }
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
            String id_actualpr,at_no,type,product,remark,style_nm,reg_dt;
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
                    reg_dt = objectRow.getString("reg_dt").replace("/Date(","").replace("000)/","");
                    String dateconver = getDate(Long.parseLong(reg_dt));
                    actualWOMasterArrayList.add(new ActualWOHomeMaster(id_actualpr,at_no,type,product,remark,style_nm,target,dateconver));
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
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("yyyy-MM-dd", cal).toString();
        return date;
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
            String id_actualpr,at_no,type,product,remark,style_nm,reg_dt;
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
                    reg_dt = objectRow.getString("reg_dt").replace("/Date(","").replace("000)/","");

                    String dateconver = getDate(Long.parseLong(reg_dt));

                    actualWOMasterArrayList.add(new ActualWOHomeMaster(id_actualpr,at_no,type,product,remark,style_nm,target,dateconver));
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