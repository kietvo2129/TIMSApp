package com.example.timsapp.ui.home.MappingOQC;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.example.timsapp.R;
import com.example.timsapp.Url;
import com.example.timsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class MappingBuyerActivity extends AppCompatActivity {
    String webUrl = Url.webUrl;
    String id_actual = ManufacturingActivity.id_actual;
    FloatingActionButton scan;
    FloatingActionButton input;
    private ProgressDialog dialog;
    TextView nodata;
    ArrayList<MappingBuyerMaster> mappingBuyerMasters;
    MappingOQCBuyerAdapter mappingOQCBuyerAdapter;
    RecyclerView recyclerView;
    String mt_cd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_buyer);
        recyclerView = findViewById(R.id.recyclerView);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        setTitle("Mapping Buyer");
        dialog = new ProgressDialog(this, R.style.AlertDialogCustom);
        getData();
        scan = findViewById(R.id.scan);
        input = findViewById(R.id.input);
        input.setVisibility(View.GONE);
        scan.setVisibility(View.GONE);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBobbin();
            }
        });

    }

    private void inputData() {

        final Dialog dialog = new Dialog(MappingBuyerActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(MappingBuyerActivity.this).inflate(R.layout.popup_input, null);
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
                } else {
                    h2.setError(null);
                    creatingMLno(Containercode.getText().toString());
                    dialog.dismiss();
                }
            }
        });


        dialog.show();

    }

    private void scanBobbin() {
        new IntentIntegrator(this).initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MappingBuyerActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                creatingMLno(result.getContents());
            }
        }
    }

    private void creatingMLno(String contents) {

        new creatingMLno().execute(webUrl + "TIMS/mapping_buyer?" +
                "mt_cd=" +
                mt_cd +
                "&buyer_qr=" +
                contents);
        Log.e("creatingMLno", webUrl + "TIMS/mapping_buyer?" +
                "mt_cd=" +
                mt_cd +
                "&buyer_qr=" +
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
                        AlerError.Baoloi(jsonObject.getString("message"), MappingBuyerActivity.this);
                        return;
                    } else {

                        Toast.makeText(MappingBuyerActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());

                    }
                } else {
                    Toast.makeText(MappingBuyerActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", MappingBuyerActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void getData() {
        new getData().execute(webUrl + "TIMS/Getmt_mappingOQC?id_actual=" + id_actual);
        Log.e("mapping", webUrl + "TIMS/Getmt_mappingOQC?id_actual=" + id_actual);
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
            mappingBuyerMasters = new ArrayList<>();
            String wmtid, bb_no, mt_no, mt_cd, buyer_qr;
            int gr_qty;
            try {

                // JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = new JSONArray(s);//jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    nodata.setVisibility(View.VISIBLE);
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    wmtid = objectRow.getString("wmtid");
                    bb_no = objectRow.getString("bb_no");
                    gr_qty = objectRow.getInt("gr_qty");
                    buyer_qr = objectRow.getString("buyer_qr").replace("null", "");
                    mt_no = objectRow.getString("mt_no");
                    mt_cd = objectRow.getString("mt_cd");
                    mappingBuyerMasters.add(new MappingBuyerMaster(wmtid, bb_no, mt_no, mt_cd, buyer_qr, gr_qty));
                }
                dialog.dismiss();
                setRecyc();
            } catch (JSONException e) {
                e.printStackTrace();
                nodata.setVisibility(View.VISIBLE);
                AlerError.Baoloi("Could not connect to server", MappingBuyerActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void setRecyc() {
        nodata.setVisibility(View.GONE);
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MappingBuyerActivity.this);
        mappingOQCBuyerAdapter = new MappingOQCBuyerAdapter(mappingBuyerMasters);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mappingOQCBuyerAdapter);

        mappingOQCBuyerAdapter.setOnItemClickListener(new MappingOQCBuyerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                input.setVisibility(View.GONE);
                scan.setVisibility(View.GONE);
            }

            @Override
            public void onmappbuyer(View view, int position) {
                mt_cd = mappingBuyerMasters.get(position).mt_cd;
                input.setVisibility(View.VISIBLE);
                scan.setVisibility(View.VISIBLE);
            }
        });

    }
}