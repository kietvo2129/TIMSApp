package com.example.timsapp.ui.DownReason;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.timsapp.ui.home.Manufacturing.Composite.CompositeCheckRollActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DownReasonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownReasonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView tv_qe;
    private ImageView imex;
    private EditText tv_qe_buy, edt_reason;
    private Button btn_map;
    private ProgressDialog progressDialog;

    public DownReasonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DownReasonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DownReasonFragment newInstance(String param1, String param2) {
        DownReasonFragment fragment = new DownReasonFragment();
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
        View view = inflater.inflate(R.layout.fragment_down_reason, container, false);

        tv_qe = view.findViewById(R.id.tv_qe);
        imex = view.findViewById(R.id.imex);
        tv_qe_buy = view.findViewById(R.id.tv_qe_buy);
        edt_reason = view.findViewById(R.id.edt_reason);

        btn_map = view.findViewById(R.id.btn_map);
        tv_qe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
        imex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBobbin();
            }
        });
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_qe.getText().toString().length()==0){
                    AlerError.Baoloi("input bobbin",getActivity());
                    return;
                }
                if(tv_qe_buy.getText().toString().length()==0){
                    AlerError.Baoloi("input qty",getActivity());
                    return;
                }

                String url =BaseApp.isHostting() +"/TIMS/check_update_grty_api?container="+tv_qe.getText().toString()
                        +"&value="+tv_qe_buy.getText().toString()+"&reason="+edt_reason.getText().toString();
                chagejsonBobbin(url);
            }
        });


        return view;
    }

    private void chagejsonBobbin(String url) {
        Log.d("chagejsonBobbin", url);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("chagejsonBobbin", response);
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("result")) {
                        Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
                    } else {
                        AlerError.Baoloi( jsonObject.has("kq")?jsonObject.getString("kq"):"error", getActivity());
                    }
                    progressDialog.dismiss();
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    BaseApp.sendWarning("Error!!!", "The json error:" + e.toString(), getActivity());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                BaseApp.sendWarning("Error !!!", "The server error:" + error.toString(), getActivity());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void scanBobbin() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    private void inputData() {
        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_input, null);
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
                    tv_qe.setText(Containercode.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                tv_qe.setText(result.getContents());
            }
        }
    }
}