package com.infinitesoftsolution.shoppinglist.shopify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnReg;
    String apilink="https://infinite-app-version.000webhostapp.com/app_api/login.php";

    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    ProgressBar pb;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnReg=findViewById(R.id.btnLinkToRegisterScreen);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnReg.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        pb=(ProgressBar) findViewById(R.id.pb);
        pb.setVisibility(View.GONE);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();

        String name = pref.getString("name", "nll");
        String pass= pref.getString("pass", "nll");

        if(name.trim().equals("nll") || pass.equals("nll")){

        }else{
            Constats.LD_UID=name;
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(it);
            finish();
        }



    }

    @Override
    public void onClick(View v) {
        try{

            switch (v.getId()){

                case R.id.btnLogin:

                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    try{

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputPassword.getWindowToken(), 0);
                    }catch (Exception e){
                        e.getMessage();
                    }

                    // Check for empty data in the form
                    if (!email.isEmpty() && !password.isEmpty()) {
                        // login user
                        makeServiceCall(apilink,email, password);
                        pb.setVisibility(View.VISIBLE);
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                    }


                    break;
                case R.id.btnLinkToRegisterScreen:

                    Intent it=new Intent(LoginActivity.this,RegisterActivity.class);
                     startActivity(it);


                    break;
            }

        }catch (Exception e){
            e.getMessage();
        }
    }


    private void makeServiceCall(String url, final String name, final String Password) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Tag",response);
                pb.setVisibility(View.GONE);

                if (response.startsWith("99*")) {
                    Toast.makeText(getApplicationContext(),
                            response.substring(3), Toast.LENGTH_LONG)
                            .show();

                    editor.clear();
                    editor.putString("name", name);
                    editor.putString("pass",Password);
                    editor.commit();
                    Constats.LD_UID=name;
                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(it);
                    finish();

                } else {
                    String resp = response.trim();
                    Toast.makeText(LoginActivity.this, resp, Toast.LENGTH_LONG)
                            .show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag",error.toString());
                pb.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG)
                        .show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", name);
                params.put("password",Password );

                return params;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(jsonObjReq);
    }
}
