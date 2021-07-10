package com.samuel.roti.pembeli;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.samuel.roti.R;
import com.samuel.roti.adapter.AdapterKue;
import com.samuel.roti.model.ModelKue;
import com.samuel.roti.server.BaseURL;
import com.samuel.roti.session.PrefSetting;
import com.samuel.roti.session.SessionManager;
import com.samuel.roti.user.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomePembeli extends AppCompatActivity {

    ProgressDialog pDialog;

    AdapterKue adapter;
    ListView list;

    ArrayList<ModelKue> newsList = new ArrayList<ModelKue>();
    private RequestQueue mRequestQueue;

    FloatingActionButton floatingExit;

    SessionManager session;
    SharedPreferences prefs;
    PrefSetting prefSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_pembeli);
        prefSetting = new PrefSetting(this);
        prefs = prefSetting.getSharePreferances();

        session = new SessionManager(HomePembeli.this);

        prefSetting.isLogin(session, prefs);

        getSupportActionBar().setTitle("Data Kue");
        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        list = (ListView) findViewById(R.id.array_list);

        floatingExit = (FloatingActionButton) findViewById(R.id.exit);

        newsList.clear();
        adapter = new AdapterKue(HomePembeli.this, newsList);
        list.setAdapter(adapter);
        getAllKue();

//        txtNama = (TextView) findViewById(R.id.txtNama);
//        txtNama.setText(PrefSetting.namaLengkap);

        floatingExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);
                session.setSessid(0);
                Intent i = new Intent(HomePembeli.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void getAllKue() {
        // Pass second argument as "null" for GET requests
        pDialog.setMessage("Loading");
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, BaseURL.dataKue, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            boolean status = response.getBoolean("error");
                            if (status == false) {
                                Log.d("data buku = ", response.toString());
                                String data = response.getString("data");
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    final ModelKue kue = new ModelKue();
                                    final String _id = jsonObject.getString("_id");
                                    final String namaKue = jsonObject.getString("namaKue");
                                    final String deskripsiKue = jsonObject.getString("deskripsiKue");
                                    final String hargaKue = jsonObject.getString("hargaKue");
                                    final String gambar = jsonObject.getString("gambar");
                                    kue.setNamaKue(namaKue);
                                    kue.setDeskripsiKue(deskripsiKue);
                                    kue.setHargaKue(hargaKue);
                                    kue.setGambar(gambar);
                                    kue.set_id(_id);

                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            // TODO Auto-generated method stub
                                            Intent a = new Intent(HomePembeli.this, DetailPembeli.class);
                                            a.putExtra("_id", newsList.get(position).get_id());
                                            a.putExtra("namaKue", newsList.get(position).getNamaKue());
                                            a.putExtra("deskripsiKue", newsList.get(position).getDeskripsiKue());
                                            a.putExtra("hargaKue", newsList.get(position).getHargaKue());
                                            a.putExtra("gambar", newsList.get(position).getGambar());
                                            startActivity(a);
                                        }
                                    });
                                    newsList.add(kue);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hideDialog();
            }
        });

        /* Add your Requests to the RequestQueue to execute */
        mRequestQueue.add(req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}