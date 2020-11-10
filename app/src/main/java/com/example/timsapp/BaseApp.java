package com.example.timsapp;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by Administrator on 2017/4/27.
 */

public class BaseApp extends Application {

    public static String hostting;

    public static String isHostting() {
        return hostting;
    }

    public static void setHostting(String url) {
        hostting = url;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static void sendWarning(String title, String text, Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setCancelable(false);
        if(title != null) {
            alertDialog.setTitle("Warning !!!");
        }
        alertDialog.setMessage(text); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }
}
