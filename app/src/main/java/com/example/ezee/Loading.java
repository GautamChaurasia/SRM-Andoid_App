package com.example.ezee;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import androidx.fragment.app.Fragment;

public class Loading {

    Activity activity;
    AlertDialog dialog;

    public Loading(Activity myActivity) {
        activity = myActivity;
    }

    void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_spinner, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }
}
