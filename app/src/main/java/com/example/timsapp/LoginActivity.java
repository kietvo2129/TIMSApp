package com.example.timsapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timsapp.AlerError.AlerError;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static com.example.timsapp.Url.webUrl;


public class LoginActivity extends AppCompatActivity {
    private String TAG = LoginActivity.class.getSimpleName();

    private static final int MIN_LENGTH = 3;
    String Url = webUrl;

    private EditText userLoginEditText;
    private EditText userFullNameEditText;
    Button btnsignup;
    private SharedPreferences sharedPreferences;
    private ProgressDialog dialog;
    TextView version;
    TextInputLayout h2;
    TextInputLayout h1;
    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW","android.permission.CAMERA"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnsignup = findViewById(R.id.btnsignup);
        userLoginEditText = findViewById(R.id.user_login);
        userFullNameEditText = findViewById(R.id.user_full_name);
        version= findViewById(R.id.version);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        h1 = findViewById(R.id.H1);
        h2 = findViewById(R.id.H2);
        dialog = new ProgressDialog(this);
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);


        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        version.setText("Version: " + versionName);


        userLoginEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                h2.setError(null);
            }
        });

        userFullNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                h1.setError(null);
            }
        });




        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userFullNameEditText.getText().toString() == null || userFullNameEditText.getText().toString().length() == 0) {
                    h1.setErrorEnabled(true);
                    h2.setError(null);
                    h1.setError("Please, Input ID");
                    return;
                }else if (userLoginEditText.getText().toString() == null || userLoginEditText.getText().toString().length() == 0) {

                    h1.setError(null);
                    h2.setErrorEnabled(true);
                    h2.setError("Please, Input Password");
                   // userLoginEditText.setError("Input PW");
                    return;
                } else {
                    h1.setError(null);
                    h2.setError(null);
                    Log.d("Login",Url +"home/API_Login?" + "user=" + userFullNameEditText.getText().toString() + "&password=" + userLoginEditText.getText().toString()+"&type=MMS");
                    new docJSON().execute(Url + "home/API_Login?" + "user=" + userFullNameEditText.getText().toString() + "&password=" + userLoginEditText.getText().toString()+"&type=MMS");
                }
            }
        });

    }

    class docJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
           dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            try {
                //JSONArray mangJSON = new JSONArray(s);
                JSONObject trave = new JSONObject(s);
                String chuoitrave = trave.getString("result");
                if(chuoitrave == "true"){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("TK",true);
                    editor.commit();

                    finish();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("TK",false);
                    editor.commit();
                    Canhbaoloi("The User name or Password you entered were invalid.");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", LoginActivity.this);
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private void Canhbaoloi(String text) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Warning!!!");
        alertDialog.setMessage(text); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }


    private String docNoiDung_Tu_URL(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }

}