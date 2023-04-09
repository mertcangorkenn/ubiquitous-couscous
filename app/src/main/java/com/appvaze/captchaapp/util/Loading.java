package com.appvaze.captchaapp.util;

import android.app.ProgressDialog;
import android.content.Context;

public class Loading {
    ProgressDialog progressDialog;
    public Loading(Context context){
        progressDialog=new ProgressDialog( context );
        progressDialog.setMessage( "Loading..." );
        progressDialog.setCancelable( false );
    }

    public void show(){
        progressDialog.show();
    }
    public void hide(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }
}
