package com.example.researchradar.UtilsService;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.snackbar.Snackbar;

public class UtilService {
    public void hideKeyboard(View view, Activity activity){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeKeyboardFromFragment(Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        View view = (View) getView().getRootView().getWindowToken();
//        if (view == null) {
//            view = (View) getView().getRootView().getWindowToken();
//        }
        // hide the keyboard
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void showSnackBar(View view, String msg){

        Snackbar.make(view,msg,Snackbar.LENGTH_LONG)
//                .setBackgroundTint(Color.rgb(220, 53, 69))
                .setBackgroundTint(Color.rgb(237,67,55))
                .setTextColor(Color.WHITE)
                .show();
    }
    public void showSnackBarSuccess(View view, String msg){

        Snackbar.make(view,msg,Snackbar.LENGTH_LONG)
                .setBackgroundTint(Color.rgb(34, 139, 34))
                .setTextColor(Color.WHITE)
                .show();
    }
    public String IPAddr(){
//        String BaseURL = "https://graduation.psgitech.ac.in/";

        String BaseURL = "http://192.168.29.206:4444/";
//        String BaseURL = "http://192.0.0.2:4444/";
        return BaseURL;
    }

}
