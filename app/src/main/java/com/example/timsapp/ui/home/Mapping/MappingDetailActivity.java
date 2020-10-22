package com.example.timsapp.ui.home.Mapping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.timsapp.ui.home.Composite.WorkerActivity;
import com.example.timsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.example.timsapp.ui.home.Mapping.QCcheck.QCCheckActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class MappingDetailActivity extends AppCompatActivity {
    String webUrl = Url.webUrl;
    String Mt_cd = MappingActivity.Ml_no;
    private ProgressDialog dialog;
    ArrayList<MappingDetailMaster> mappingDetailMasterArrayList;
    MappingDetailAdapter mappingDetailAdapter;
    TextView nodata;
    RecyclerView recyclerView;
    BoomMenuButton bmb;
    String vt_scan = "";
    public static int numgr_qty = 0;
    FloatingActionButton scan;
    FloatingActionButton input;
    public static String Ml_no = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping_detail);
        setTitle("Mapping Detail");
        nodata = findViewById(R.id.nodata);
        recyclerView = findViewById(R.id.recyclerView);
        nodata.setVisibility(View.GONE);
        dialog = new ProgressDialog(MappingDetailActivity.this, R.style.AlertDialogCustom);

        scan = findViewById(R.id.scan);
        input = findViewById(R.id.input);
        input.setVisibility(View.GONE);
        scan.setVisibility(View.GONE);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getVisibility() == View.VISIBLE) {
                    input.setVisibility(View.GONE);
                    scan.setVisibility(View.GONE);
                } else {
                    input.setVisibility(View.VISIBLE);
                    scan.setVisibility(View.VISIBLE);
                }

            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialMapping();
            }
        });

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });
//        bmb = findViewById(R.id.bmb);
//        bmb.setButtonEnum(ButtonEnum.TextOutsideCircle);
//        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_1);
//        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_2_1);
//        bmb.addBuilder(new TextOutsideCircleButton.Builder()
//                .normalImageRes(R.drawable.ic_mater)
//                .normalTextRes(R.string.MaterialMapping));
//        bmb.addBuilder(new TextOutsideCircleButton.Builder()
//                .normalImageRes(R.drawable.ic_container)
//                .normalTextRes(R.string.ContainerMapping));
//
//        bmb.setOnBoomListener(new OnBoomListener() {
//            @Override
//            public void onClicked(int index, BoomButton boomButton) {
//                switch (index) {
//                    case 0:
//                        vt_scan = "MT";
//                        MaterialMapping();
//                        http://messhinsungcntvina.com:83/TIMS/insertw_material_mping?mt_cd=LJ63-86354ADD20201020185535000001&bb_no=BOBBIN-AUTO-20201020142849000001&id_actual=62
//
//                        break;
//                    case 1:
//                        vt_scan = "CT";
//                        MaterialMapping();
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onBackgroundClick() {
//
//            }
//
//            @Override
//            public void onBoomWillHide() {
//
//            }
//
//            @Override
//            public void onBoomDidHide() {
//
//            }
//
//            @Override
//            public void onBoomWillShow() {
//
//            }
//
//            @Override
//            public void onBoomDidShow() {
//
//            }
//        });


    }

    private void inputData() {

        final Dialog dialog = new Dialog(MappingDetailActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(MappingDetailActivity.this).inflate(R.layout.popup_input, null);
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
                    new Mappingmaterial().execute(webUrl + "TIMS/insertw_material_mping?mt_cd=" +
                            Mt_cd +
                            "&bb_no=" +
                            Containercode.getText().toString() +
                            "&id_actual=" +
                            ManufacturingActivity.id_actual);
                    Log.e("Mappingmaterial", webUrl + "TIMS/insertw_material_mping?mt_cd=" +
                            Mt_cd +
                            "&bb_no=" +
                            Containercode.getText().toString() +
                            "&id_actual=" +
                            ManufacturingActivity.id_actual);
                    dialog.dismiss();
                }


            }
        });


        dialog.show();

    }

    //scan
    private void MaterialMapping() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(MappingDetailActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                new Mappingmaterial().execute(webUrl + "TIMS/insertw_material_mping?mt_cd=" +
                        Mt_cd +
                        "&bb_no=" +
                        result.getContents() +
                        "&id_actual=" +
                        ManufacturingActivity.id_actual);
                Log.e("Mappingmaterial", webUrl + "TIMS/insertw_material_mping?mt_cd=" +
                        Mt_cd +
                        "&bb_no=" +
                        result.getContents() +
                        "&id_actual=" +
                        ManufacturingActivity.id_actual);
            }
        }
    }

    private class Mappingmaterial extends AsyncTask<String, Void, String> {
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
                        AlerError.Baoloi(jsonObject.getString("message"), MappingDetailActivity.this);
                        return;
                    } else {
                        dialog.dismiss();
                        Toast.makeText(MappingDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    }
                } else {
                    dialog.dismiss();
                    Toast.makeText(MappingDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                }

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", MappingDetailActivity.this);
                dialog.dismiss();
            }
        }

    }
    ////////

    private void getData() {

        new getData().execute(webUrl + "TIMS/ds_mapping_w?mt_cd=" + Mt_cd);
        Log.e("mapping Detail", webUrl + "TIMS/ds_mapping_w?mt_cd=" + Mt_cd);
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
            mappingDetailMasterArrayList = new ArrayList<>();
            String no, wmmid, mt_lot, mt_cd, use_yn, reg_dt, mt_no, bb_no, Remain;
            int gr_qty, Used;
            try {

                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    nodata.setVisibility(View.VISIBLE);
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    no = i + 1 + "";
                    wmmid = objectRow.getString("wmmid");
                    mt_lot = objectRow.getString("mt_lot");
                    gr_qty = objectRow.getInt("gr_qty");
                    Used = objectRow.getInt("Used");
                    mt_no = objectRow.getString("mt_no");
                    use_yn = objectRow.getString("use_yn");
                    reg_dt = objectRow.getString("reg_dt");
                    Remain = objectRow.getString("Remain").replace("null", "0");
                    bb_no = objectRow.getString("bb_no").replace("null", "");
                    mt_cd = objectRow.getString("mt_cd");
                    mappingDetailMasterArrayList.add(new MappingDetailMaster(no, wmmid, mt_lot, mt_cd, use_yn, reg_dt, mt_no, bb_no, Remain, gr_qty, Used));
                }
                dialog.dismiss();
                setRecyc();
            } catch (JSONException e) {
                e.printStackTrace();
                nodata.setVisibility(View.VISIBLE);
                AlerError.Baoloi("Could not connect to server", MappingDetailActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void setRecyc() {
        nodata.setVisibility(View.GONE);
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(MappingDetailActivity.this);
        mappingDetailAdapter = new MappingDetailAdapter(mappingDetailMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mappingDetailAdapter);

        mappingDetailAdapter.setOnItemClickListener(new MappingDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onR(int position, TextView edittext) {
                inputnum(position);
            }

            @Override
            public void onF(final int position, TextView edittext) {

                if (mappingDetailMasterArrayList.get(position).getUse_yn().equals("N")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MappingDetailActivity.this, R.style.AlertDialogCustom);
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle("Warning!!!");
                    alertDialog.setMessage("Is A sure you want to return to the used state"); //"The data you entered does not exist on the server !!!");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            new Cancel_mapping().execute(webUrl + "TIMS/Finish_back?wmmid=" + mappingDetailMasterArrayList.get(position).wmmid);
                        }
                    });
                    alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    alertDialog.show();
                } else {

                    new Cancel_mapping().execute(webUrl + "TIMS/Finish_back?wmmid=" + mappingDetailMasterArrayList.get(position).wmmid);
                }

            }

            @Override
            public void qcCheck(int position, TextView edittext) {
                numgr_qty = mappingDetailMasterArrayList.get(position).gr_qty;
                Ml_no = mappingDetailMasterArrayList.get(position).mt_cd;
                Intent intent = new Intent(MappingDetailActivity.this, QCCheckActivity.class);
                startActivity(intent);
            }

            @Override
            public void ondelete(final int position, TextView edittext) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MappingDetailActivity.this, R.style.AlertDialogCustom);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Warning!!!");
                alertDialog.setMessage("Are you sure Delete: " + mappingDetailMasterArrayList.get(position).mt_cd); //"The data you entered does not exist on the server !!!");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Cancel_mapping().execute(webUrl + "TIMS/Cancel_mapping?wmmid=" + mappingDetailMasterArrayList.get(position).wmmid);
                    }
                });
                alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialog.show();

            }
        });
    }

    private void inputnum(final int pos) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MappingDetailActivity.this);
        View viewInflated = LayoutInflater.from(MappingDetailActivity.this).inflate(R.layout.number_input_layout, null);


        builder.setTitle("Input Number Return");
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (input.getText().toString() == null) {
                    input.setText("0");
                } else if (input.getText().toString().length() == 0) {
                    input.setText("0");
                } else if (Integer.parseInt(input.getText().toString()) > mappingDetailMasterArrayList.get(pos).getGr_qty()) {
                    input.setText(mappingDetailMasterArrayList.get(pos).getGr_qty() + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString() == null) {
                    dialog.dismiss();
                } else if (input.getText().toString() == "0") {
                    dialog.dismiss();
                } else if (input.getText().toString().length() == 0) {
                    dialog.dismiss();
                }
                if (Integer.parseInt(input.getText().toString()) > 0) {
                    new Cancel_mapping().execute(webUrl + "TIMS/savereturn_lot?soluong=" +
                            input.getText().toString() +
                            "&mt_cd=" +
                            mappingDetailMasterArrayList.get(pos).mt_cd +
                            "&mt_lot=" +
                            Mt_cd);
                    Log.e("return", webUrl + "TIMS/savereturn_lot?soluong=" +
                            input.getText().toString() +
                            "&mt_cd=" +
                            mappingDetailMasterArrayList.get(pos).mt_cd +
                            "&mt_lot=" +
                            Mt_cd);
                } else {
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

    private class Cancel_mapping extends AsyncTask<String, Void, String> {
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

                if (!jsonObject.getBoolean("result")) {
                    dialog.dismiss();
                    AlerError.Baoloi(jsonObject.getString("message"), MappingDetailActivity.this);
                    return;
                } else {
                    startActivity(getIntent());
                    Toast.makeText(MappingDetailActivity.this, "Done", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", MappingDetailActivity.this);
                dialog.dismiss();
            }
        }

    }

    @Override
    protected void onPostResume() {
        getData();
        super.onPostResume();
    }
}