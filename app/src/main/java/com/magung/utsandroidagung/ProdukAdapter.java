package com.magung.utsandroidagung;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.GridViewHolder> {

    private List<Produk> produks;
    private Context context;

    public ProdukAdapter(Context context, List<Produk> produks) {
        this.produks = produks;
        this.context = context;
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.produk_item_layout, parent, false);
        GridViewHolder viewHolder = new GridViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {
        final int pos = position;
        final String id = produks.get(position).getId();
        final String nama = produks.get(position).getNama();
        final String deskripsi = produks.get(position).getDeskripsi();
        final String pekerjaan = produks.get(position).getPekerjaan();
        final String status = produks.get(position).getStatus();

        holder.tvStatus.setText(status);
        holder.tvNama.setText(nama);
        holder.tvPekerjaan.setText(pekerjaan);

        if(status.equals("SELESAI")) {
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Operasi data");
                alertDialog.setMessage(id +" - "+ nama);
                alertDialog.setPositiveButton("BATAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.setNegativeButton("LIHAT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Bundle b = new Bundle();
                        b.putString("b_id", id);
                        b.putString("b_nama", nama);
                        b.putString("b_deskripsi", deskripsi);
                        b.putString("b_pekerjaan", pekerjaan);
                        b.putString("b_status", status);

                        Intent intent = new Intent(context, DataDetail.class);
                        intent.putExtras(b);

                        //context.startActivity(intent);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            ((Activity) context).startActivityForResult(intent, 1, b);
                        }
                    }
                });

                alertDialog.setNeutralButton("HAPUS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestQueue queue = Volley.newRequestQueue(context);
                        String url = "https://rest-api-agung-411.000webhostapp.com/api/produk.php?action=hapus&id="+ id;

                        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                null,
                                new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        String id, nama, telp;

                                        if (response.optString("result").equals("true")){
                                            Toast.makeText(context, "Data berhasil dihapus!", Toast.LENGTH_SHORT).show();

                                            produks.remove(pos); //hapus baris customers
                                            notifyItemRemoved(pos); //refresh customer list ( ada animasinya )
                                            notifyDataSetChanged();

                                        }else{
                                            Toast.makeText(context, "Gagal hapus data", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO Auto-generated method stub
                                Log.d("Events: ", error.toString());

                                Toast.makeText(context, "Please check your connection", Toast.LENGTH_SHORT).show();
                            }
                        });

                        queue.add(jsObjRequest);
                    }
                });


                AlertDialog dialog = alertDialog.create();
                dialog.show();

                //untuk ubah warna tombol dialog
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.red));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.teal_200));
                dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(context.getResources().getColor(R.color.design_default_color_primary));
            }
        });
    }

    @Override
    public int getItemCount() {
        return produks.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatus, tvNama, tvPekerjaan;
        public GridViewHolder(@NonNull View itemView) {
            super(itemView);

            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvNama = (TextView) itemView.findViewById(R.id.tv_nama);
            tvPekerjaan = (TextView) itemView.findViewById(R.id.tv_pekerjaan);
        }
    }
}
