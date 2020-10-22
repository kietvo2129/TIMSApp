package com.example.timsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {
    String webUrl = Url.webUrl;
    private static final int SPLASH_DELAY = 1000;
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;
    String latestVersion = "", latestVersionCode = "", url = "", releaseNotes = "";
    //ProgressBar progressBar;
    TextView version;
    Handler mHandler = new Handler();
    int intprogress = -1;
    //TextView eff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       // progressBar = findViewById(R.id.progressBar);
       // eff = findViewById(R.id.eff);
        version = findViewById(R.id.version);
        //startService(new Intent(this, MainActivity.class));
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        version.setText("Version: "+ versionName);
        //new read_infor_app().execute(webUrl + "APIProduct/GetApp_info");
        mHandler.post(runnable);
        checkversionapp();

    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            intprogress+=1;
            mHandler.postDelayed(this, SPLASH_DELAY/100);
            if (intprogress>99){
                intprogress=99;
            }
          //  progressBar.setProgress(intprogress);
           // eff.setText(intprogress+"%");
        }
    };


//    private class read_infor_app extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            return Url.NoiDung_Tu_URL(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//                JSONArray jsonArray = new JSONArray(s);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);
//                    if (jsonObject2.getInt("id") == 2) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        latestVersion = jsonObject.getString("latestVersion").replace("null", "");
//                        latestVersionCode = jsonObject.getString("latestVersionCode").replace("null", "");
//                        url = jsonObject.getString("url").replace("null", "");
//                        releaseNotes = jsonObject.getString("releaseNotes").replace("null", "");
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//                AlerError.Baoloi("Could not connect to server", SplashActivity.this);
//            }
//            checkversionapp();
//        }
//
//    }

    private void checkversionapp() {
        //Toast.makeText(this, latestVersionCode+"    "+latestVersion, Toast.LENGTH_SHORT).show();
       // if (latestVersionCode.equals(versionCode + "") && latestVersion.equals(versionName)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_DELAY);

//        } else {
//            Baoloi("Please, update new version app.");
//        }
    }

//    public void Baoloi(String text) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
//        alertDialog.setCancelable(false);
//        alertDialog.setTitle("Warning!!!");
//        alertDialog.setMessage(text + "\n" + releaseNotes);
//        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                startActivity(intent);
//            }
//        });
//        alertDialog.show();
//    }

    @Override
    protected void onResume() {
        //checkversionapp();
      //  new read_infor_app().execute(webUrl + "APIProduct/GetApp_info");
        super.onResume();
    }


}