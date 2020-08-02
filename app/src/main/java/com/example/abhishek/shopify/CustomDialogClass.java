package com.example.abhishek.shopify;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class CustomDialogClass extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;


    TextView version;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        version = (TextView) findViewById(R.id.version);

        version.setText("Version No: "+Constats.versionCode);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_yes:
                c.finish();
                break;

        }
        dismiss();
    }
}