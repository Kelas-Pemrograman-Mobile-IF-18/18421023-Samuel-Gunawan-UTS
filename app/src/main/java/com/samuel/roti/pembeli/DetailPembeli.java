package com.samuel.roti.pembeli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.samuel.roti.R;
import com.samuel.roti.server.BaseURL;
import com.squareup.picasso.Picasso;

public class DetailPembeli extends AppCompatActivity {

    EditText edtNamaKue, edtDeskripsi, edtHargaKue;
    ImageView imgGambarKue;

    String strNamaKue, strDeskripsi, strHargaKue, strGambar, _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembeli);

        edtNamaKue = (EditText) findViewById(R.id.edtNamaKue1);
        edtDeskripsi = (EditText) findViewById(R.id.edtDeskripsiKue1);
        edtHargaKue = (EditText) findViewById(R.id.edtHargaKueD);

        imgGambarKue = (ImageView) findViewById(R.id.gambar);

        Intent i = getIntent();
        strNamaKue = i.getStringExtra("namaKue");
        strDeskripsi = i.getStringExtra("deskripsiKue");
        strHargaKue = i.getStringExtra("hargaKue");
        strGambar = i.getStringExtra("gambar");
        _id = i.getStringExtra("_id");

        edtNamaKue.setText(strNamaKue);
        edtDeskripsi.setText(strDeskripsi);
        edtHargaKue.setText(strHargaKue);
        Picasso.get().load(BaseURL.baseUrl + "gambar/" + strGambar)
                .into(imgGambarKue);
    }
}