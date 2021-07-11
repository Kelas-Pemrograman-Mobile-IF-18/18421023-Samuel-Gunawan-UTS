package com.samuel.roti.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.samuel.roti.R;
import com.samuel.roti.adapter.AdapterKue;
import com.samuel.roti.model.ModelKue;
import com.samuel.roti.server.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataKue extends AppCompatActivity {
    ProgressDialog pDialog;

    AdapterKue adapter;
    ListView list;

    ArrayList<ModelKue> newsList = new ArrayList<ModelKue>();
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kue);
        getSupportActionBar().setTitle("Data Kue");
        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        list = (ListView) findViewById(R.id.array_list);
        newsList.clear();
        adapter = new AdapterKue(DataKue.this, newsList);
        list.setAdapter(adapter);
        getAllKue();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(DataKue.this, HomeAdminActivity.class);
        startActivity(i);
        finish();
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
                                Log.d("data kue = ", response.toString());
                                String data = response.getString("data");
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    final ModelKue kue = new ModelKue();
                                    final String _id = jsonObject.getString("_id");
                                    final String NamaKue = jsonObject.getString("namaKue");
                                    final String DeskripsiKue = jsonObject.getString("deskripsiKue");
                                    final String HargaKue = jsonObject.getString("hargaKue");
                                    final String gambar = jsonObject.getString("gambar");
                                    kue.setNamaKue(NamaKue);
                                    kue.setDeskripsiKue(DeskripsiKue);
                                    kue.setHargaKue(HargaKue);
                                    kue.setGambar(gambar);
                                    kue.set_id(_id);

                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            // TODO Auto-generated method stub
                                            Intent a = new Intent(DataKue.this, EditKueDanHapus.class);
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