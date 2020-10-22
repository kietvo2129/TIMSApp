package com.example.timsapp.ui.home.Composite.Divide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timsapp.AlerError.AlerError;
import com.example.timsapp.R;
import com.example.timsapp.Url;
import com.example.timsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.example.timsapp.ui.home.Mapping.MappingActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class DivideActivity extends AppCompatActivity {

    String webUrl = Url.webUrl;
    String id_actual = ManufacturingActivity.id_actual;
    int page = 1;
    private ProgressDialog dialog;
    TextView nodata;

    ArrayList<DivideMaster> mappingMasterArrayList;
    DivideAdapter divideAdapter;
    int total = -1;
    RecyclerView recyclerView;
    public static String Ml_no = "";
    FloatingActionButton fab;
    public static int numgr_qty;
    EditText Containercode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divide);
        setTitle("Divide");

        recyclerView = findViewById(R.id.recyclerView);
        nodata = findViewById(R.id.nodata);
        nodata.setVisibility(View.GONE);
        dialog = new ProgressDialog(this, R.style.AlertDialogCustom);

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
            int gr_qty, gr_qty1, cnt, sl_tru_ng;
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
                    String sl = objectRow.getString("sl_tru_ng").replace("null", "0");
                    sl_tru_ng = Integer.parseInt(sl);
                    mappingMasterArrayList.add(new DivideMaster(no, wmtid, date, mt_cd, mt_no, bbmp_sts_cd, mt_qrcode, lct_cd, bb_no, mt_barcode, gr_qty, gr_qty1, cnt, sl_tru_ng));
                }
                dialog.dismiss();
                setRecyc();
            } catch (JSONException e) {
                e.printStackTrace();
                nodata.setVisibility(View.VISIBLE);
                AlerError.Baoloi("Could not connect to server", DivideActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void setRecyc() {
        nodata.setVisibility(View.GONE);
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        divideAdapter = new DivideAdapter(mappingMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(divideAdapter);
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

        divideAdapter.setOnItemClickListener(new DivideAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Ml_no = mappingMasterArrayList.get(position).mt_cd;
//                Intent intent = new Intent(DivideActivity.this, MappingDetailActivity.class);
//                startActivity(intent);
                opendetailDiv();
            }

            @Override
            public void onDiv(int position) {

                popupDiv(position);

            }
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    private void popupDiv(final int position) {

        final Dialog dialog = new Dialog(DivideActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(DivideActivity.this).inflate(R.layout.popup_input_div, null);
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
        final TextInputLayout h2;
        final TextInputLayout h1;

        h1 = dialog.findViewById(R.id.H1);
        h2 = dialog.findViewById(R.id.H2);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num_div.getText().toString() == null || num_div.getText().toString().length() == 0) {
                    h1.setErrorEnabled(true);
                    h2.setError(null);
                    h1.setError("Please, Input here.");
                    return;
                } else if (Containercode.getText().toString() == null || Containercode.getText().toString().length() == 0) {

                    h1.setError(null);
                    h2.setErrorEnabled(true);
                    h2.setError("Please, Input here.");
                    return;
                } else {
                    h1.setError(null);
                    h2.setError(null);
                    new confirmDiv().execute(webUrl+"TIMS/Decevice_sta?mt_cd=" +
                            mappingMasterArrayList.get(position).mt_cd +
                            "&number_dv=" +
                            num_div.getText().toString() +
                            "&bobin=" +
                            Containercode.getText().toString());
                    Log.e("confirmDiv",webUrl+"TIMS/Decevice_sta?mt_cd=" +
                            mappingMasterArrayList.get(position).mt_cd +
                            "&number_dv=" +
                            num_div.getText().toString() +
                            "&bobin=" +
                            Containercode.getText().toString());
                    dialog.dismiss();
                }


            }
        });


        dialog.show();

    }

    private class confirmDiv extends AsyncTask<String, Void, String> {
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
                if (!jsonObject.getBoolean("result")){
                    dialog.dismiss();
                    AlerError.Baoloi(jsonObject.getString("message"), DivideActivity.this);
                    return;
                }
                opendetailDiv();

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", DivideActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void opendetailDiv() {
        Intent intent =new Intent(DivideActivity.this, DivDetailActivity.class);
        startActivity(intent);
    }


    private void openscan() {
        new IntentIntegrator(this).initiateScan();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(DivideActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Containercode.setText(result.getContents());
            }
        }
    }

    private void getaddData(int page) {
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
            int gr_qty, gr_qty1, cnt, sl_tru_ng;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", DivideActivity.this);
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
                    mappingMasterArrayList.add(new DivideMaster(no, wmtid, date, mt_cd, mt_no, bbmp_sts_cd, mt_qrcode, lct_cd, bb_no, mt_barcode, gr_qty, gr_qty1, cnt, sl_tru_ng));
                }
                dialog.dismiss();
                divideAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", DivideActivity.this);
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