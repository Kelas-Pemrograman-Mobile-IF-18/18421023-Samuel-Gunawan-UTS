package com.samuel.roti.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.samuel.roti.R;
import com.samuel.roti.session.PrefSetting;

public class Profile extends AppCompatActivity {

    TextView txtUserName, txtNamaLengkap, txtNotelp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile User");

        txtUserName = (TextView) findViewById(R.id.userNameP);
        txtNamaLengkap = (TextView) findViewById(R.id.namaLengkapP);
        txtNotelp = (TextView) findViewById(R.id.noTelpP);

        txtUserName.setText(PrefSetting.userName);
        txtNamaLengkap.setText(PrefSetting.namalengkap);
        txtNotelp.setText(PrefSetting.noTelp);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Profile.this, HomeAdminActivity.class);
        startActivity(i);
        finish();
    }
}