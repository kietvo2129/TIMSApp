package com.example.timsapp.ui.home.Composite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.timsapp.AlerError.AlerError;
import com.example.timsapp.ui.home.Composite.Divide.DivDetailActivity;
import com.example.timsapp.ui.home.Composite.Divide.DivideActivity;

import com.example.timsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.example.timsapp.ui.home.Mapping.MappingActivity;
import com.example.timsapp.R;
import com.example.timsapp.Url;
import com.example.timsapp.ui.home.MappingOQC.MappingBuyerActivity;
import com.example.timsapp.ui.home.MappingOQC.MappingOQCActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class CompositeActivity extends AppCompatActivity {
    String webUrl = Url.webUrl;
    private ProgressDialog dialog;
    ArrayList<CompositeMaster> compositeMasterArrayList;
    TextView nodata;
    RecyclerView recyclerView;
    CompositeAdapter compositeAdapter;
    public static String id_actual;
    ArrayList<VitridungmayMaster> vitridungmayMasterArrayList;
    TextView StaffType, StaffType_code;
    EditText tvid;
    BoomMenuButton bmb4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composite);
        setTitle("Composite");
        dialog = new ProgressDialog(this);
        nodata = findViewById(R.id.nodata);
        recyclerView = findViewById(R.id.recyclerView);
        nodata.setVisibility(View.GONE);
        Bundle bundle = getIntent().getExtras();
        id_actual = ManufacturingActivity.id_actual;
        new getvitridungmay().execute(webUrl + "ActualWO/get_staff");
        //build bombutton
        bmb4 = (BoomMenuButton) findViewById(R.id.bmb4);

    }

    //        BoomMenuButton mapping = (BoomMenuButton) findViewById(R.id.mapping);
//
//        if (ManufacturingActivity.style_name.equals("STA")){
//            mapping.setButtonEnum(ButtonEnum.Ham);
//            mapping.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
//            mapping.setPiecePlaceEnum(PiecePlaceEnum.DOT_2_1);
//            mapping.addBuilder(new HamButton.Builder()
//                    .normalImageRes(R.drawable.ic_mapping)
//                    .normalTextRes(R.string.Mappiing)
//                    .subNormalTextRes(R.string.CompositeMaterialMapping));
//            mapping.addBuilder(new HamButton.Builder()
//                    .normalImageRes(R.drawable.ic_div)
//                    .normalTextRes(R.string.Divide)
//                    .subNormalTextRes(R.string.DivideMTNo));
//        }else {
//            mapping.setButtonEnum(ButtonEnum.Ham);
//            mapping.setButtonPlaceEnum(ButtonPlaceEnum.HAM_1);
//            mapping.setPiecePlaceEnum(PiecePlaceEnum.DOT_1);
//            mapping.addBuilder(new HamButton.Builder()
//                    .normalImageRes(R.drawable.ic_mapping)
//                    .normalTextRes(R.string.Mappiing)
//                    .subNormalTextRes(R.string.CompositeMaterialMapping));
//        }
//
//        mapping.setOnBoomListener(new OnBoomListener() {
//            @Override
//            public void onClicked(int index, BoomButton boomButton) {
//                switch (index) {
//                    case 0:
//                        Intent intent = new Intent(CompositeActivity.this, MappingActivity.class);
//                        startActivity(intent);
//                        break;
//                    case 1:
//                        Intent intent1 = new Intent(CompositeActivity.this, DivideActivity.class);
//                        startActivity(intent1);
//                        break;
////                    case 2:
////                        Intent intent2 = new Intent(CompositeActivity.this,MachineActivity.class);
////                        startActivity(intent2);
////                        break;
//                }
//
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
    private class getvitridungmay extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            vitridungmayMasterArrayList = new ArrayList<>();
            try {
                String dt_cd, dt_nm;
                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray.length() == 0) {
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);

                    dt_cd = object.getString("dt_cd");
                    dt_nm = object.getString("dt_nm");
                    vitridungmayMasterArrayList.add(new VitridungmayMaster(dt_cd, dt_nm));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", CompositeActivity.this);
            }
        }

    }

    private void getData() {

        new getData().execute(webUrl + "TIMS/Getprocess_staff?id_actual=" + id_actual);
        Log.e("getData", webUrl + "TIMS/Getprocess_staff?id_actual=" + id_actual);
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
            compositeMasterArrayList = new ArrayList<>();
            String no, psid, staff_id, staff_tp, staff_tp_nm, uname, start_dt, end_dt, use_yn;


            try {

                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    nodata.setVisibility(View.VISIBLE);
                    setmenuadd();
                    return;
                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    no = i + 1 + "";
                    psid = objectRow.getString("psid").replace("null", "");
                    start_dt = objectRow.getString("start_dt").replace("null", "");
                    end_dt = objectRow.getString("end_dt").replace("null", "");
                    staff_id = objectRow.getString("staff_id").replace("null", "");

                    staff_tp = objectRow.getString("staff_tp").replace("null", "");
                    staff_tp_nm = objectRow.getString("staff_tp_nm").replace("null", "").replace("[\"", "").replace("\"]", "");
                    uname = objectRow.getString("uname").replace("null", "").replace("[\"", "").replace("\"]", "");
                    use_yn = objectRow.getString("use_yn").replace("null", "");

                    compositeMasterArrayList.add(new CompositeMaster(no, psid, staff_id, staff_tp, staff_tp_nm, uname, start_dt, end_dt, use_yn));
                }
                dialog.dismiss();
                buildrecyc();
            } catch (JSONException e) {
                setmenuadd();
                e.printStackTrace();
                nodata.setVisibility(View.VISIBLE);
                AlerError.Baoloi("Could not connect to server", CompositeActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void setmenuadd() {
        bmb4.clearBuilders();
        bmb4.setPiecePlaceEnum(PiecePlaceEnum.HAM_1);
        bmb4.setButtonPlaceEnum(ButtonPlaceEnum.HAM_1);
        bmb4.addBuilder(new HamButton.Builder()
                .normalImageRes(R.drawable.ic_staff)
                .normalTextRes(R.string.Worker)
                .subNormalTextRes(R.string.movingpositionofmachinery));
        bmb4.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                switch (index) {
                    case 0:
                        Intent intent = new Intent(CompositeActivity.this, WorkerActivity.class);
                        startActivity(intent);
                        break;
                }

            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });
    }

    private void buildrecyc() {
        nodata.setVisibility(View.GONE);
        setmenu();
        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(CompositeActivity.this);
        compositeAdapter = new CompositeAdapter(compositeMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(compositeAdapter);
        compositeAdapter.setOnItemClickListener(new CompositeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                openpopupmold(position, compositeMasterArrayList.get(position).start_dt, compositeMasterArrayList.get(position).end_dt);

            }

            @Override
            public void ondelete(final int position) {

                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(CompositeActivity.this, R.style.AlertDialogCustom);
                alertDialog.setCancelable(false);
                alertDialog.setTitle("Warning!!!");
                alertDialog.setMessage("Are you sure Delete?");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new onDelete().execute(webUrl + "ActualWO/DeleteMold_mc_wk_actual?id=" + compositeMasterArrayList.get(position).psid + "&sts=wk");
                        Log.e("onDelete", webUrl + "ActualWO/DeleteMold_mc_wk_actual?id=" + compositeMasterArrayList.get(position).psid + "&sts=wk");
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

    private class onDelete extends AsyncTask<String, Void, String> {
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
                dialog.dismiss();
                if (s.trim().indexOf("false")!=-1){
                    AlerError.Baoloi("Delete false. Please check again.", CompositeActivity.this);
                    return;
                }
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("result")) {
                    dialog.dismiss();
                    Toast.makeText(CompositeActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                } else {
                    dialog.dismiss();
                    AlerError.Baoloi("Delete false. Please check again.", CompositeActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", CompositeActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void setmenu() {
        bmb4.clearBuilders();
        if (ManufacturingActivity.process_nm.equals("OQC")) {
            bmb4.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
            bmb4.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);

            bmb4.addBuilder(new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_staff)
                    .normalTextRes(R.string.Worker)
                    .subNormalTextRes(R.string.movingpositionofmachinery));

            bmb4.addBuilder(new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_mapping)
                    .normalTextRes(R.string.Mappiing)
                    .subNormalTextRes(R.string.CompositeMaterialMapping));
            bmb4.addBuilder(new HamButton.Builder()
                    .normalImageRes(R.drawable.ic_div)
                    .normalTextRes(R.string.MappingBuyer)
                    .subNormalTextRes(R.string.MappingBuyerQRCode));
            bmb4.setOnBoomListener(new OnBoomListener() {
                @Override
                public void onClicked(int index, BoomButton boomButton) {
                    switch (index) {
                        case 0:
                            Intent intent = new Intent(CompositeActivity.this, WorkerActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Intent intent1 = new Intent(CompositeActivity.this, MappingOQCActivity.class);
                            startActivity(intent1);
                            break;
                        case 2:
                            Intent intent2 = new Intent(CompositeActivity.this, MappingBuyerActivity.class);
                            startActivity(intent2);
                            break;
                    }

                }

                @Override
                public void onBackgroundClick() {

                }

                @Override
                public void onBoomWillHide() {

                }

                @Override
                public void onBoomDidHide() {

                }

                @Override
                public void onBoomWillShow() {

                }

                @Override
                public void onBoomDidShow() {

                }
            });

        } else {
            if (ManufacturingActivity.RollCode.equals("300")) {
                bmb4.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
                bmb4.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);

                bmb4.addBuilder(new HamButton.Builder()
                        .normalImageRes(R.drawable.ic_staff)
                        .normalTextRes(R.string.Worker)
                        .subNormalTextRes(R.string.movingpositionofmachinery));

                bmb4.addBuilder(new HamButton.Builder()
                        .normalImageRes(R.drawable.ic_mapping)
                        .normalTextRes(R.string.Mappiing)
                        .subNormalTextRes(R.string.CompositeMaterialMapping));
                bmb4.addBuilder(new HamButton.Builder()
                        .normalImageRes(R.drawable.ic_div)
                        .normalTextRes(R.string.Divide)
                        .subNormalTextRes(R.string.DivideMTNo));
                bmb4.setOnBoomListener(new OnBoomListener() {
                    @Override
                    public void onClicked(int index, BoomButton boomButton) {
                        switch (index) {
                            case 0:
                                Intent intent = new Intent(CompositeActivity.this, WorkerActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(CompositeActivity.this, MappingActivity.class);
                                startActivity(intent1);
                                break;
                            case 2:
                                Intent intent2 = new Intent(CompositeActivity.this, DivideActivity.class);
                                startActivity(intent2);
                                break;
                        }

                    }

                    @Override
                    public void onBackgroundClick() {

                    }

                    @Override
                    public void onBoomWillHide() {

                    }

                    @Override
                    public void onBoomDidHide() {

                    }

                    @Override
                    public void onBoomWillShow() {

                    }

                    @Override
                    public void onBoomDidShow() {

                    }
                });
            } else {
                bmb4.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
                bmb4.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);

                bmb4.addBuilder(new HamButton.Builder()
                        .normalImageRes(R.drawable.ic_staff)
                        .normalTextRes(R.string.Worker)
                        .subNormalTextRes(R.string.movingpositionofmachinery));

                bmb4.addBuilder(new HamButton.Builder()
                        .normalImageRes(R.drawable.ic_mapping)
                        .normalTextRes(R.string.Mappiing)
                        .subNormalTextRes(R.string.CompositeMaterialMapping));
                bmb4.setOnBoomListener(new OnBoomListener() {
                    @Override
                    public void onClicked(int index, BoomButton boomButton) {
                        switch (index) {
                            case 0:
                                Intent intent = new Intent(CompositeActivity.this, WorkerActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(CompositeActivity.this, MappingActivity.class);
                                startActivity(intent1);
                                break;
                        }

                    }

                    @Override
                    public void onBackgroundClick() {

                    }

                    @Override
                    public void onBoomWillHide() {

                    }

                    @Override
                    public void onBoomDidHide() {

                    }

                    @Override
                    public void onBoomWillShow() {

                    }

                    @Override
                    public void onBoomDidShow() {

                    }
                });
            }

        }

    }

    //modify mc mold worker
    private void openpopupmold(final int position, String start_dt, String end_dt) {
        final Dialog dialog = new Dialog(CompositeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView;

        dialogView = LayoutInflater.from(CompositeActivity.this).inflate(R.layout.change_worker, null);
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        StaffType_code = dialog.findViewById(R.id.StaffType_code);
        StaffType = dialog.findViewById(R.id.StaffType);
        StaffType.setText(compositeMasterArrayList.get(position).staff_tp_nm);
        StaffType_code.setText(compositeMasterArrayList.get(position).staff_tp);
        dialog.findViewById(R.id.rll2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_Worker(StaffType);
            }
        });


        final TextView ngaystart, giostart, Used;
        ngaystart = dialog.findViewById(R.id.ngaystart);
        giostart = dialog.findViewById(R.id.giostart);

        final TextView ngayend, gioend;
        ngayend = dialog.findViewById(R.id.ngayend);
        gioend = dialog.findViewById(R.id.gioend);
        Used = dialog.findViewById(R.id.Used);
        tvid = dialog.findViewById(R.id.tvid);
        tvid.setText(compositeMasterArrayList.get(position).staff_id);

        if (compositeMasterArrayList.get(position).use_yn.equals("Y")) {
            Used.setText("USE");
        } else {
            Used.setText("UNUSE");
        }
        dialog.findViewById(R.id.im1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_camera_scan();
            }
        });

        dialog.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvid.getText().toString().length() != 0) {
                    //so sanh 2 ngay duoc chon
                    Date dsend = new Date();
                    Date dstart = new Date();

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        dstart = sdf.parse(ngaystart.getText().toString() + " " + giostart.getText().toString());
                        dsend = sdf.parse(ngayend.getText().toString() + " " + gioend.getText().toString());

                    } catch (ParseException ex) {
                        Log.e("rrr", ex.getMessage());
                    }


                    if (dsend.after(dstart)) {

                        String us = Used.getText().toString();
                        String keyus = "";
                        if (us.equals("USE")) {
                            keyus = "Y";
                        } else {
                            keyus = "N";
                        }

                        String url = webUrl + "ActualWO/Modifyprocess_unitstaff?staff_id=" +
                                tvid.getText().toString() +
                                "&staff_tp=" +
                                StaffType_code.getText().toString() +
                                "&psid=" +
                                compositeMasterArrayList.get(position).psid +
                                "&use_yn=" +
                                keyus +
                                "&start=" +
                                ngaystart.getText().toString() + " " + giostart.getText().toString() +
                                "&id_actual=" +
                                id_actual +
                                "&end=" +
                                ngayend.getText().toString() + " " + gioend.getText().toString() +
                                "&remark=";
                        new Modifyprocessmachine_unit().execute(url);
                        Log.e("Modify", url);

                    } else {
                        AlerError.Baoloi("Start day was bigger End day. That is wrong", CompositeActivity.this);
                    }

                } else {
                    tvid.setError("Please input here!");
                }

            }
        });

        dialog.findViewById(R.id.rl2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_pp_use(Used);
            }
        });

        final String yy, MM, dd, hh, mm, ss, yye, MMe, dde, hhe, mme, sse;
        if (start_dt.length() == 19) {
            yy = start_dt.substring(0, 4);
            MM = start_dt.substring(5, 7);
            dd = start_dt.substring(8, 10);
            hh = start_dt.substring(11, 13);
            mm = start_dt.substring(14, 16);
            ss = start_dt.substring(17, 19);
        } else {
            AlerError.Baoloi("Format date incorrect.", CompositeActivity.this);
            return;
        }
        if (end_dt.length() == 19) {
            yye = end_dt.substring(0, 4);
            MMe = end_dt.substring(5, 7);
            dde = end_dt.substring(8, 10);
            hhe = end_dt.substring(11, 13);
            mme = end_dt.substring(14, 16);
            sse = end_dt.substring(17, 19);
        } else {
            AlerError.Baoloi("Format date incorrect.", CompositeActivity.this);
            return;
        }
        ngaystart.setText(yy + "-" + MM + "-" + dd);
        ngayend.setText(yye + "-" + MMe + "-" + dde);
        giostart.setText(hh + ":" + mm + ":" + ss);
        gioend.setText(hhe + ":" + mme + ":" + sse);

        ngaystart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNgay(ngaystart);
            }
        });
        giostart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoggio(giostart, hh, mm);
            }
        });
        ngayend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogNgay(ngayend);
            }
        });
        gioend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialoggio(gioend, hhe, mme);
            }
        });

        dialog.findViewById(R.id.btclose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    private class Modifyprocessmachine_unit extends AsyncTask<String, Void, String> {

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
                if (jsonObject.getString("result").equals("0")) {
                    Toast.makeText(CompositeActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    startActivity(getIntent());
                } else if (jsonObject.getString("result").equals("1")) {

                    AlerError.Baoloi("The Worker was setting duplicate date", CompositeActivity.this);

                } else if (jsonObject.getString("result").equals("2")) {
                    AlerError.Baoloi("Start day was bigger End day. That is wrong", CompositeActivity.this);
                } else if (jsonObject.getString("result").equals("3")) {
                    //   xacnhan_datontai(jsonObject.getString("update"),jsonObject.getString("start"),jsonObject.getString("end"));
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", CompositeActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void open_pp_use(final TextView used) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CompositeActivity.this, android.R.layout.select_dialog_singlechoice);
        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(CompositeActivity.this);
        builderSingle.setTitle("Select One Line:");
        arrayAdapter.add("USE");
        arrayAdapter.add("UNUSE");
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                used.setText(arrayAdapter.getItem(i));
                dialog.dismiss();
            }
        });
        builderSingle.show();

    }

    private void open_camera_scan() {
        new IntentIntegrator(CompositeActivity.this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(CompositeActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                tvid.setText(result.getContents());
            }
        }
    }

    private void popup_Worker(final TextView staffType) {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CompositeActivity.this, android.R.layout.select_dialog_singlechoice);
        android.app.AlertDialog.Builder builderSingle = new android.app.AlertDialog.Builder(CompositeActivity.this);
        builderSingle.setTitle("Select One Line:");
        for (int i = 0; i < vitridungmayMasterArrayList.size(); i++) {
            arrayAdapter.add(vitridungmayMasterArrayList.get(i).dt_nm);
        }

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                staffType.setText(vitridungmayMasterArrayList.get(i).dt_nm);
                StaffType_code.setText(vitridungmayMasterArrayList.get(i).dt_cd);
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void dialogNgay(final TextView ngaystart) {
        Calendar c = Calendar.getInstance();
        int selectedYear = c.get(Calendar.YEAR);
        int selectedMonth = c.get(Calendar.MONTH);
        int selectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {

                ngaystart.setText((year + "-" + String.format("%02d", monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void dialoggio(final TextView ngaystart, final String hh, final String mm) {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                ngaystart.setText(String.format("%02d", hourOfDay) + ":" + (String.format("%02d", minute)) + ":00");
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener,
                Integer.parseInt(hh), Integer.parseInt(mm), true);
        timePickerDialog.show();
    }

    //modify mc mold worker

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }
}