package com.infinitesoftsolution.shoppinglist.shopify;

import android.content.Context;
import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnLoginNav;
    private Button btnRegister;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;

    ProgressBar pb;

    String url="https://infinite-app-version.000webhostapp.com/app_api/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnLoginNav=findViewById(R.id.btnLinkToLoginScreen);
        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        pb = (ProgressBar) findViewById(R.id.pb);
        btnLoginNav.setOnClickListener(this);

        btnRegister.setOnClickListener(this);
        btnLoginNav.setOnClickListener(this);

        pb.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        try{

            switch (v.getId()){


                case R.id.btnRegister:



                    String name = inputFullName.getText().toString().trim();
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    try{

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputPassword.getWindowToken(), 0);
                    }catch (Exception e){
                        e.getMessage();
                    }

                    if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {

                        pb.setVisibility(View.VISIBLE);
                        makeServiceCall(url,name,email,password);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your details!", Toast.LENGTH_LONG)
                                .show();
                    }
                    break;
                case R.id.btnLinkToLoginScreen:
                    Intent i = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivity(i);
                    finish();
                    break;
            }

        }catch (Exception e){
            e.getMessage();
        }
    }


    private void makeServiceCall(String url, final String name, final String email, final String Password) {

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                try {
                    Log.d("Tag", response);

                    pb.setVisibility(View.GONE);

                    if (response.startsWith("99*")) {
                        Toast.makeText(getApplicationContext(),
                                response, Toast.LENGTH_LONG)
                                .show();
                        Constats.LD_UID=name;
                        Intent it = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(it);
                        finish();
                    } else {
                        String resp = response.trim();
                        Toast.makeText(RegisterActivity.this, resp, Toast.LENGTH_LONG)
                                .show();
                    }


                }catch (Exception e){
                    e.getMessage();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Tag",error.toString());

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", name);
                params.put("email", email);
                params.put("password_1",Password );

                return params;
            }};

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(jsonObjReq);
    }
}
