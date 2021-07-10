package com.samuel.roti.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ornach.nobobutton.NoboButton;
import com.samuel.roti.MainActivity;
import com.samuel.roti.R;
import com.samuel.roti.admin.HomeAdminActivity;
import com.samuel.roti.pembeli.HomePembeli;
import com.samuel.roti.server.BaseURL;
import com.samuel.roti.session.PrefSetting;
import com.samuel.roti.session.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    NoboButton btnLogin;
    Button btnRegister;

    EditText edtUsername, edtpassword;

    private RequestQueue mRequestQueue;
    ProgressDialog pDialog;

    SessionManager session;
    SharedPreferences prefs;
    PrefSetting prefSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        mRequestQueue = Volley.newRequestQueue(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnLogin = (NoboButton) findViewById(R.id.btLogin);
        btnRegister = (Button) findViewById(R.id.btRegister);

        edtUsername = (EditText) findViewById(R.id. UserName);
        edtpassword = (EditText) findViewById(R.id.Password);

        prefSetting = new PrefSetting(this);
        prefs = prefSetting.getSharePreferances();

        session = new SessionManager(this);

        prefSetting.checkLogin(session, prefs);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUserName = edtUsername.getText().toString();
                String strPassword = edtpassword.getText().toString();

                if (strUserName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Username Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                } else if (strPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Password Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                } else {
                    login(strUserName, strPassword);
                }
            }
        });
    }


    public void login(String userName, String password){

        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userName", userName);
        params.put("password", password);

        pDialog.setMessage("Mohon Tunggu.....");
        showDialog();

        JsonObjectRequest req = new JsonObjectRequest(BaseURL.login, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String strMsg = response.getString("msg");
                            boolean status= response.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                String data = response.getString("data");
                                JSONObject jsonObject = new JSONObject(data);
                                String _id  = jsonObject.getString("_id");
                                String userName  = jsonObject.getString("userName");
                                String namaLengkap  = jsonObject.getString("namaLengkap");
                                String noTelp  = jsonObject.getString("noTelp");
                                String password = jsonObject.getString("password");
                                String role = jsonObject.getString("role");
                                session.setLogin(true);
                                prefSetting.storeRegIdSharedPreferences(LoginActivity.this, _id, userName, namaLengkap, noTelp, password, role, prefs);
                                if (role.equals("1")) {
                                    Intent i = new Intent(LoginActivity.this, HomeAdminActivity.class);
                                    startActivity(i);
                                    finish();
                                }else {
                                    Intent i = new Intent(LoginActivity.this, HomePembeli.class);
                                    startActivity(i);
                                    finish();
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hideDialog();
            }
        });
        mRequestQueue.add(req);
    }

    private void showDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }

    private void hideDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
}