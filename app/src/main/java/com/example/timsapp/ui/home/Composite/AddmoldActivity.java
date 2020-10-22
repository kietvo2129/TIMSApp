package com.example.timsapp.ui.home.Composite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.timsapp.AlerError.AlerError;
import com.example.timsapp.R;
import com.example.timsapp.Url;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.timsapp.Url.NoiDung_Tu_URL;

public class AddmoldActivity extends AppCompatActivity {
    String webUrl = Url.webUrl;
    String id_actual;
    private ProgressDialog dialog;
    ArrayList<ItemCompositeMaster> itemCompositeMasterArrayList;
    ItemCompositeAdapter itemCompositeAdapter;
    int page = 1;
    int total = -1;
    RecyclerView recyclerView;
    String mc_no ="",use_unuse="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmold);
        setTitle("Mold");
        id_actual = CompositeActivity.id_actual;
        dialog = new ProgressDialog(this);
        recyclerView = findViewById(R.id.recyclerView);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanMold();

            }
        });

        getData(page);
    }

    private void scanMold() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(AddmoldActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                findscanMold(result.getContents());
            }
        }
    }

    private void findscanMold(String contents) {
        new getData().execute(webUrl + "ActualWO/MoldMgtData?page=" + 1 + "&rows=50&sidx=&sord=asc&md_no=" + contents +
                "&md_nm=&_search=false");
    }


    private void getData(int page) {
        new getData().execute(webUrl + "ActualWO/MoldMgtData?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");
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
            itemCompositeMasterArrayList = new ArrayList<>();
            String type, su_dung, RowNum, mdno, md_no, md_nm, purpose, barcode, re_mark, use_yn, del_yn;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", AddmoldActivity.this);
                    return;
                }
                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    type = "";
                    su_dung = objectRow.getString("su_dung").replace("null", "");
                    RowNum = objectRow.getString("RowNum").replace("null", "");
                    mdno = objectRow.getString("mdno").replace("null", "");
                    md_no = objectRow.getString("md_no").replace("null", "");
                    md_nm = objectRow.getString("md_nm").replace("null", "");
                    purpose = objectRow.getString("purpose").replace("null", "");
                    barcode = objectRow.getString("barcode").replace("null", "");
                    re_mark = objectRow.getString("re_mark").replace("null", "");
                    use_yn = objectRow.getString("use_yn").replace("null", "");
                    del_yn = objectRow.getString("del_yn").replace("null", "");

                    itemCompositeMasterArrayList.add(new ItemCompositeMaster(i+1+"",type,su_dung, RowNum, mdno, md_no, md_nm, purpose, barcode, re_mark, use_yn, del_yn));
                }
                dialog.dismiss();
                buildrecyc();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", AddmoldActivity.this);
                dialog.dismiss();
            }
        }

    }

    private void buildrecyc() {

        final LinearLayoutManager mLayoutManager;
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(AddmoldActivity.this);
        itemCompositeAdapter = new ItemCompositeAdapter(itemCompositeMasterArrayList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(itemCompositeAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = mLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == itemCompositeMasterArrayList.size() - 1) {
                    if (page < total) {
                        total = -1;
                        getaddData(page + 1);

                    }
                }
            }
        });


        itemCompositeAdapter.setOnItemClickListener(new ItemCompositeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                xacnhanadd(position);

            }
        });


    }

    private void xacnhanadd(final int position) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddmoldActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add mold");
        alertDialog.setMessage("You want: "); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("USE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                mc_no = itemCompositeMasterArrayList.get(position).md_no;
                use_unuse = "Y";
                new comfirmData().execute(webUrl+ "ActualWO/Createprocessmachine_unit?mc_no=" +itemCompositeMasterArrayList.get(position).md_no +
                        "&use_yn=Y&id_actual="+
                        id_actual +"&remark=");
                Log.e("comfirmData",webUrl+ "ActualWO/Createprocessmachine_unit?mc_no=" +itemCompositeMasterArrayList.get(position).md_no +
                        "&use_yn=Y&id_actual="+
                        id_actual +"&remark=");
            }
        });
        alertDialog.setNegativeButton("UNUSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mc_no = itemCompositeMasterArrayList.get(position).md_no;
                use_unuse = "N";
                new comfirmData().execute(webUrl+ "ActualWO/Createprocessmachine_unit?mc_no=" +itemCompositeMasterArrayList.get(position).md_no +
                        "&use_yn=N&id_actual="+
                        id_actual +"&remark=");
                Log.e("comfirmData",webUrl+ "ActualWO/Createprocessmachine_unit?mc_no=" +itemCompositeMasterArrayList.get(position).md_no +
                        "&use_yn=N&id_actual="+
                        id_actual +"&remark=");
            }
        });
        alertDialog.show();
    }

    private class comfirmData extends AsyncTask<String, Void, String> {
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
                if(jsonObject.getString("result").equals("0")){
                    thanhcong();
                }else if(jsonObject.getString("result").equals("1")){
                    AlerError.Baoloi("The Process Mold Unit was setting duplicate date", AddmoldActivity.this);
                }else if (jsonObject.getString("result").equals("2")){
                    AlerError.Baoloi("Start day was bigger End day. That is wrong", AddmoldActivity.this);
                } else if (jsonObject.getString("result").equals("3")){
                    xacnhan_datontai(jsonObject.getString("update"),jsonObject.getString("start"),jsonObject.getString("end"));
                }
                dialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", AddmoldActivity.this);
                dialog.dismiss();
            }
        }

    }
    private void thanhcong() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddmoldActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add mold");
        alertDialog.setMessage("Add mold finnish."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDialog.show();
    }


    private void xacnhan_datontai(final String update, final String start, final String end) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddmoldActivity.this, R.style.AlertDialogCustom);
        alertDialog.setTitle("Add mold");
        alertDialog.setMessage("This mold has already selected. If you confirm it, this mold will finish the task at the previous stage."); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               new huyketquatruoc().execute(webUrl+"ActualWO/Createprocessmachine_duplicate?mc_no="+
                       mc_no+"&id_actual="+id_actual+"&use_yn="+use_unuse+"&id_update="+update+"&start="+start+"&end="+end+"&remark=");
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }
    private class huyketquatruoc extends AsyncTask<String, Void, String> {
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
                dialog.dismiss();
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", AddmoldActivity.this);
                dialog.dismiss();
            }
        }

    }


    private void getaddData(int page) {
        new getaddData().execute(webUrl + "ActualWO/MoldMgtData?page=" + page + "&rows=50&sidx=&sord=asc&md_no=&md_nm=&_search=false");
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

            String type,su_dung, RowNum, mdno, md_no, md_nm, purpose, barcode, re_mark, use_yn, del_yn;
            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("rows");

                if (jsonArray.length() == 0) {
                    dialog.dismiss();
                    AlerError.Baoloi("No data", AddmoldActivity.this);
                    return;
                }
                total = jsonObject.getInt("total");
                page = jsonObject.getInt("page");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    type = "";
                    su_dung = objectRow.getString("su_dung").replace("null", "");
                    RowNum = objectRow.getString("RowNum").replace("null", "");
                    mdno = objectRow.getString("mdno").replace("null", "");
                    md_no = objectRow.getString("md_no").replace("null", "");
                    md_nm = objectRow.getString("md_nm").replace("null", "");
                    purpose = objectRow.getString("purpose").replace("null", "");
                    barcode = objectRow.getString("barcode").replace("null", "");
                    re_mark = objectRow.getString("re_mark").replace("null", "");
                    use_yn = objectRow.getString("use_yn").replace("null", "");
                    del_yn = objectRow.getString("del_yn").replace("null", "");

                    itemCompositeMasterArrayList.add(new ItemCompositeMaster(50*(page-1)+i+1+"",type,su_dung, RowNum, mdno, md_no, md_nm, purpose, barcode, re_mark, use_yn, del_yn));
                }
                dialog.dismiss();
                itemCompositeAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", AddmoldActivity.this);
                dialog.dismiss();
            }
        }

    }
}