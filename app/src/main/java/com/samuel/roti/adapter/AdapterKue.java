package com.samuel.roti.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.samuel.roti.R;
import com.samuel.roti.model.ModelKue;
import com.samuel.roti.server.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterKue extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelKue> item;

    public AdapterKue(Activity activity, List<ModelKue> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.content_kue, null);


        TextView namaKue      = (TextView) convertView.findViewById(R.id.NamaKue);
        TextView deskripsiKue = (TextView) convertView.findViewById(R.id.DeskripsiKue);
        TextView hargaKue     = (TextView) convertView.findViewById(R.id.HargaKue);
        ImageView gambarKue   = (ImageView) convertView.findViewById(R.id.gambarKue);

        namaKue.setText(item.get(position).getNamaKue());
        deskripsiKue.setText(item.get(position).getDeskripsiKue());
        hargaKue.setText("Rp." + item.get(position).getHargaKue());
        Picasso.get().load(BaseURL.baseUrl + "gambar/" + item.get(position).getGambar())
                .resize(40, 40)
                .centerCrop()
                .into(gambarKue);
        return convertView;
    }
}
