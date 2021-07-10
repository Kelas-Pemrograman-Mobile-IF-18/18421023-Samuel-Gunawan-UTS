package com.samuel.roti.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.samuel.roti.R;
import com.samuel.roti.server.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Button btnBackLogin;
    NoboButton btnRegistrasi;
    EditText  edtNamaLengkap, edtUsername, edtPassword, edtNoTelp;
    ProgressDialog pDialog;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRequestQueue = Volley.newRequestQueue(this);

        edtUsername = (EditText) findViewById(R.id.UserName2);
        edtNamaLengkap = (EditText) findViewById(R.id.NamaLengkap2);
        edtNoTelp = (EditText) findViewById(R.id.NoTelp2);
        edtPassword = (EditText) findViewById(R.id.Password2);

        btnBackLogin = (Button) findViewById(R.id.btBackLogin2);
        btnRegistrasi = (NoboButton) findViewById(R.id.btRegister2);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnRegistrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strNamaLengkap   = edtNamaLengkap.getText().toString();
                String strUserName      = edtUsername.getText().toString();
                String strNoTelp        = edtNoTelp.getText().toString();
                String strPassword      = edtPassword.getText().toString();

                if(strUserName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }else if(strNamaLengkap.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Nama Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }else if(strNoTelp.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Nomor Telphone Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }else if(strPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Password Tidak Boleh Kosong", Toast.LENGTH_LONG).show();
                }else {
                    registrasi(strUserName, strNamaLengkap, strNoTelp, strPassword);
                }
            }
        });
    }

    public void registrasi(String userName, String namaLengkap, String noTelp, String password){

        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userName", userName);
        params.put("namaLengkap", namaLengkap);
        params.put("noTelp", noTelp);
        params.put("password", password);
        params.put("role", "2");

        pDialog.setMessage("Mohon Tunggu.....");
        showDialog();

        JsonObjectRequest req = new JsonObjectRequest(BaseURL.register, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String strMsg = response.getString("msg");
                            boolean status= response.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
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