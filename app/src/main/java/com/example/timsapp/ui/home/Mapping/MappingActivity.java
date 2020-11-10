package com.example.timsapp.ui.home.Mapping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timsapp.AlerError.AlerError;
import com.example.timsapp.BaseApp;
import com.example.timsapp.R;
import com.example.timsapp.Url;
import com.example.timsapp.ui.home.ActualWO.HomeFragment;
import com.example.timsapp.ui.home.Composite.AddmoldActivity;
import com.example.timsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.example.timsapp.ui.home.Mapping.QCcheck.QCCheckActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class MappingActivity extends AppCompatActivity {

    String webUrl = BaseApp.isHostting();
    String id_actual = ManufacturingActivity.id_actual;
    int page = 1;
    private ProgressDialog dialog;
    TextView nodata;
    ArrayList<MappingMaster> mappingMasterArrayList;
    MappingAdapter mappingAdapter;
    int total = -1;
    RecyclerView recyclerView;
    public static String Ml_no = "";
    FloatingActionButton fab;

    FloatingActionButton scan;
    FloatingActionButton input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);
        setTitle("Mapping");
        recyclerView = findViewById(R.id.recyclerView);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        dialog = new ProgressDialog(MappingActivity.this, R.style.AlertDialogCustom);


        scan = findViewById(R.id.scan);
        input = findViewById(R.id.input);
        input.setVisibility(View.GONE);
        scan.setVisibility(View.GONE);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getVisibility()==View.VISIBLE){
                    input.setVisibility(View.GONE);
                    scan.setVisibility(View.GONE);
                }else {
                    input.setVisibility(View.VISIBLE);
                    scan.setVisibility(View.VISIBLE);
                }

            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBobbin();
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
    }
    private void inputData() {

        final Dialog dialog = new Dialog(MappingActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(MappingActivity.this).inflate(R.layout.popup_input, null);
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        final EditText Containercode = dialog.findViewById(R.id.Containercode);
        final Button confirm = dialog.findViewById(R.id.confirm);
        final TextInputLayout h2;

        h2 = dialog.findViewById(R.id.H2);


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Containercode.getText().toString() == null || Containercode.getText().toString().length() == 0) {
                    h2.setError("Please, Input here.");
                    return;
                }  else {
                    h2.setError(null);
                    creatingMLno(Containercode.getText().toString());
                    dialog.dismiss();
                }
            }
        });


        dialog.show();

    }

    private void getData() {
        new getData().execute(webUrl + "TIMS/getmt_date_web_auto?id_actual=" + id_actual + "&page=" +
                page);// + "&rows=50&sidx=&sord=asc&_search=false&mc_type=&mc_no=&mc_nm=");
        Log.e("mapping", webUrl + "TIMS/getmt_date_web_auto?id_actual=" + id_actual + "&page=" +
                page);// + "&rows=50&sidx=&sord=asc&_search=false&mc_type=&mc_no=&mc_nm=");
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
            mappingMasterArrayList = new ArrayList<>();
            String no, wmtid, date, mt_cd, mt_no, bbmp_sts_cd, mt_qrcode, lct_cd, bb_no, mt_barcode;
            int gr_qty, gr_qty1, cnt,sl_tru_ng;
            try {

               // JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(s);//jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    nodata.setVisibility(View.VISIBLE);
                    return;
                }

                total = 1;//jsonObject.getInt("total");
                page = 1;//jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    no = i + 1 + "";
                    wmtid = objectRow.getString("wmtid");
                    date = objectRow.getString("date");
                    gr_qty = objectRow.getInt("gr_qty");
                    gr_qty1 = objectRow.getInt("gr_qty1");
                    mt_no = objectRow.getString("mt_no");
                    bbmp_sts_cd = objectRow.getString("bbmp_sts_cd");
                    mt_qrcode = objectRow.getString("mt_qrcode");
                    lct_cd = objectRow.getString("lct_cd").replace("null", "");
                    bb_no = objectRow.getString("bb_no");
                    mt_barcode = objectRow.getString("mt_barcode");
                    mt_cd = objectRow.getString("mt_cd");
                    cnt = objectRow.getInt("count_table2");
                    String sl = objectRow.getString("sl_tru_ng").replace("null","0");
                    sl_tru_ng = Integer.parseInt(sl);
                    mappingMasterArrayList.add(new MappingMaster(no, wmtid, date, mt_cd, mt_no, bbmp_sts_cd, mt_qrcode, lct_cd, bb_no, mt_barcode, gr_qty, gr_qty1, cnt,sl_tru_ng));
                }
                dialog.dismiss();
                setRecyc();
            } catch (JSONException e) {
                e.printStackTrace();
                nodata.setVisibility(View.VISIBLE);
                AlerError.Baoloi("Could not connect to server", MappingActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void setRecyc() {
        nodata.setVisibility(View.GONE);
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MappingActivity.this);
        mappingAdapter = new MappingAdapter(mappingMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mappingAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == mappingMasterArrayList.size() - 1) {
                    if (page < total) {
                        total = -1;
                        getaddData(page + 1);

                    }
                }
            }
        });

        mappingAdapter.setOnItemClickListener(new MappingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Ml_no = mappingMasterArrayList.get(position).mt_cd;
                Intent intent = new Intent(MappingActivity.this, MappingDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onQuantityChange(int position, TextView edittext) {
                inputnum(position);
            }

            @Override
            public void onQCCheck(int position, TextView edittext) {
//                Ml_no = mappingMasterArrayList.get(position).mt_cd;
//                numgr_qty = mappingMasterArrayList.get(position).gr_qty;
//                Intent intent = new Intent(MappingActivity.this, QCCheckActivity.class);
//                startActivity(intent);
            }
        });


    }

    private void inputnum(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MappingActivity.this);
        View viewInflated = LayoutInflater.from(MappingActivity.this).inflate(R.layout.number_input_layout, null);
        builder.setTitle("Input Quantity Reality");
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString() == null) {
                    dialog.dismiss();
                } else if (input.getText().toString() == "0") {
                    dialog.dismiss();
                } else if (input.getText().toString().length() == 0) {
                    dialog.dismiss();
                } else {

                    new thaydoisoluong().execute(webUrl + "ActualWO/check_update_grty?mt_cd=" +
                            mappingMasterArrayList.get(pos).mt_cd +
                            "&value=" +
                            input.getText().toString() +
                            "&wmtid=" +
                            mappingMasterArrayList.get(pos).wmtid);
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private class thaydoisoluong extends AsyncTask<String, Void, String> {
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
                if (jsonObject.getBoolean("result")) {
                    dialog.dismiss();
                    Toast.makeText(MappingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                } else {
                    dialog.dismiss();
                    AlerError.Baoloi("Change Quantity reality false. Please check again.", MappingActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", MappingActivity.this);
                dialog.dismiss();
            }
        }

    }


    private void getaddData(int page) {
//        new getaddData().execute(webUrl + "ActualWO/getmt_date_web?id_actual=" + id_actual + "&page=" +
//                page + "&rows=50&sidx=&sord=asc&_search=false&mc_type=&mc_no=&mc_nm=");
        new getData().execute(webUrl + "TIMS/getmt_date_web_auto?id_actual=" + id_actual + "&page=" +
                page);// + "&rows=50&sidx=&sord=asc&_search=false&mc_type=&mc_no=&mc_nm=");
        Log.e("mapping", webUrl + "TIMS/getmt_date_web_auto?id_actual=" + id_actual + "&page=" +
                page);// + "&rows=50&sidx=&sord=asc&_search=false&mc_type=&mc_no=&mc_nm=");
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
            String no, wmtid, date, mt_cd, mt_no, bbmp_sts_cd, mt_qrcode, lct_cd, bb_no, mt_barcode;
            int gr_qty, gr_qty1, cnt,sl_tru_ng;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", MappingActivity.this);
                    return;
                }

                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    no = i + 1 + "";
                    wmtid = objectRow.getString("wmtid");
                    date = objectRow.getString("date");
                    gr_qty = objectRow.getInt("gr_qty");
                    gr_qty1 = objectRow.getInt("gr_qty1");
                    mt_no = objectRow.getString("mt_no");
                    bbmp_sts_cd = objectRow.getString("bbmp_sts_cd");
                    mt_qrcode = objectRow.getString("mt_qrcode");
                    lct_cd = objectRow.getString("lct_cd").replace("null", "");
                    bb_no = objectRow.getString("bb_no");
                    mt_barcode = objectRow.getString("mt_barcode");
                    mt_cd = objectRow.getString("mt_cd");
                    cnt = objectRow.getInt("count_table2");
                    sl_tru_ng = objectRow.getInt("sl_tru_ng");
                    mappingMasterArrayList.add(new MappingMaster(no, wmtid, date, mt_cd, mt_no, bbmp_sts_cd, mt_qrcode, lct_cd, bb_no, mt_barcode, gr_qty, gr_qty1, cnt,sl_tru_ng));
                }
                dialog.dismiss();
                mappingAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", MappingActivity.this);
                dialog.dismiss();
            }
        }

    }

    // open scan qr code
    private void scanBobbin() {
        new IntentIntegrator(this).initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MappingActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                creatingMLno(result.getContents());
            }
        }
    }

    private void creatingMLno(String contents) {

        new creatingMLno().execute(webUrl + "TIMS/InsertMLNoWithSelectedBobin?style_no=" +
                HomeFragment.product +
                "&id_actual=" +
                id_actual +
                "&bb_no=" +
                contents);
        Log.e("creatingMLno", webUrl + "TIMS/InsertMLNoWithSelectedBobin?style_no=" +
                HomeFragment.product +
                "&id_actual=" +
                id_actual +
                "&bb_no=" +
                contents);

    }

    private class creatingMLno extends AsyncTask<String, Void, String> {
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

                if (jsonObject.has("result")) {
                    if (!jsonObject.getBoolean("result")) {
                        dialog.dismiss();
                        AlerError.Baoloi(jsonObject.getString("message"), MappingActivity.this);
                        return;
                    } else {
                        Toast.makeText(MappingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    }
                } else {
                    Toast.makeText(MappingActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", MappingActivity.this);
                dialog.dismiss();
            }
        }

    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }
}