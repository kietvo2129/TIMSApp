package com.example.timsapp.ui.home.Composite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.timsapp.AlerError.AlerError;
import com.example.timsapp.ui.home.Composite.Divide.DivDetailActivity;
import com.example.timsapp.ui.home.Composite.Divide.DivideActivity;

import com.example.timsapp.ui.home.Manufacturing.ManufacturingActivity;
import com.example.timsapp.ui.home.Mapping.MappingActivity;
import com.example.timsapp.R;
import com.example.timsapp.Url;
import com.example.timsapp.ui.home.MappingOQC.MappingBuyerActivity;
import com.example.timsapp.ui.home.MappingOQC.MappingOQCActivity;
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

import java.util.ArrayList;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class CompositeActivity extends AppCompatActivity {
    String webUrl = Url.webUrl;
    private ProgressDialog dialog;
    ArrayList<CompositeMaster> compositeMasterArrayList;
    TextView nodata;
    RecyclerView recyclerView;
    CompositeAdapter compositeAdapter;
    public static String id_actual;

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
        getData();
        //build bombutton
        BoomMenuButton bmb4 = (BoomMenuButton) findViewById(R.id.bmb4);


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
            }
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
        }
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
                e.printStackTrace();
                nodata.setVisibility(View.VISIBLE);
                AlerError.Baoloi("Could not connect to server", CompositeActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void buildrecyc() {
        nodata.setVisibility(View.GONE);

        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(CompositeActivity.this);
        compositeAdapter = new CompositeAdapter(compositeMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(compositeAdapter);
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }
}