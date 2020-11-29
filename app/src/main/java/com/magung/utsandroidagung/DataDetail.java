package com.magung.utsandroidagung;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class DataDetail extends AppCompatActivity {
    ProgressBar pb;
    EditText etID,etNama, etDeskripsi, etPekerjaan, etStatus;
    Button btHapus, btUbah, btSelesai, btBelum;
    String id, nama, deskripsi, pekerjaan, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);

        pb = (ProgressBar) findViewById(R.id.pb);
        etID = (EditText) findViewById(R.id.et_id);
        etNama = (EditText) findViewById(R.id.et_nama);
        etDeskripsi = (EditText) findViewById(R.id.et_deskripsi);
        etPekerjaan = (EditText) findViewById(R.id.et_pekerjaan);
        etStatus = (EditText) findViewById(R.id.et_status);
        btUbah = (Button) findViewById(R.id.bt_ubah);
        btHapus = (Button) findViewById(R.id.bt_hapus);
        btBelum = (Button) findViewById(R.id.bt_belum);
        btSelesai = (Button) findViewById(R.id.bt_selesai);

        //tombol back
        getSupportActionBar().setTitle("Detil Pekerjaan"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action back

        //tangkap bundle
        Bundle bundle = null;
        bundle = this.getIntent().getExtras();

        //letakkan isi bundle
        id = bundle.getString("b_id");
        nama = bundle.getString("b_nama");
        deskripsi = bundle.getString("b_deskripsi");
        pekerjaan = bundle.getString("b_pekerjaan");
        status = bundle.getString("b_status");

        //letakkan pada textview
        etID.setText(id);
        etNama.setText(nama);
        etDeskripsi.setText(deskripsi);
        etPekerjaan.setText(pekerjaan);
        etStatus.setText(status);


        if(status.equals("SELESAI")) {
            btBelum.setVisibility(Button.VISIBLE);
            btSelesai.setVisibility(Button.GONE);
        }else{
            btBelum.setVisibility(Button.GONE);
            btSelesai.setVisibility(Button.VISIBLE);
        }
        //operasi ubah data

        btUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nama =  etNama.getText().toString();
                deskripsi =  etDeskripsi.getText().toString();
                pekerjaan =  etPekerjaan.getText().toString();

                pb.setVisibility(ProgressBar.VISIBLE); //munculkan progressbar

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://rest-api-agung-411.000webhostapp.com/api/produk.php?action=ubah&id="+id+"&nama="+nama+"&deskripsi="+deskripsi+"&pekerjaan="+pekerjaan;

                Log.d("Hendro ", "onClick: " + url);
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                String id, nama, telp;

                                if (response.optString("result").equals("true")){
                                    Toast.makeText(getApplicationContext(), "Data terubah!", Toast.LENGTH_SHORT).show();

                                    finish(); //tutup activity
                                }else{
                                    Toast.makeText(getApplicationContext(), "O ow, sepertinya harus dicoba lagi", Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(ProgressBar.GONE); //sembunyikan progress bar
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("Events: ", error.toString());

                        Toast.makeText(getApplicationContext(), "Hmm, masalah internet atau data yang kamu masukkan", Toast.LENGTH_SHORT).show();

                        pb.setVisibility(ProgressBar.GONE); //sembunyikan progress bar
                    }
                });

                queue.add(jsObjRequest);
            }
        });

        btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(ProgressBar.VISIBLE); //tampilkan progress bar

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://rest-api-agung-411.000webhostapp.com/api/produk.php?action=hapus&id="+ id;

                JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                if (response.optString("result").equals("true")){
                                    Toast.makeText(getApplicationContext(), "Data terhapus!", Toast.LENGTH_SHORT).show();

                                    finish(); //tutup activity
                                }else{
                                    Toast.makeText(getApplicationContext(), "O ow, sepertinya harus dicoba lagi", Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(ProgressBar.GONE); //sembunyikan progress bar
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("Events: ", error.toString());

                        pb.setVisibility(ProgressBar.GONE); //sembunyikan progress bar

                        Toast.makeText(getApplicationContext(), "Hmm, masalah internet atau data yang kamu masukkan", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(jsObjRequest);
            }
        });
        btSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(ProgressBar.VISIBLE); //tampilkan progress bar

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://rest-api-agung-411.000webhostapp.com/api/produk.php?action=selesai&id="+ id;

                JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                if (response.optString("result").equals("true")){
                                    Toast.makeText(getApplicationContext(), "Pekerjaan terselesaikan", Toast.LENGTH_SHORT).show();

                                    finish(); //tutup activity
                                }else{
                                    Toast.makeText(getApplicationContext(), "O ow, sepertinya harus dicoba lagi", Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(ProgressBar.GONE); //sembunyikan progress bar
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("Events: ", error.toString());

                        pb.setVisibility(ProgressBar.GONE); //sembunyikan progress bar

                        Toast.makeText(getApplicationContext(), "Hmm, masalah internet atau data yang kamu masukkan", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(jsObjRequest);
            }
        });

        btBelum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pb.setVisibility(ProgressBar.VISIBLE); //tampilkan progress bar

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "https://rest-api-agung-411.000webhostapp.com/api/produk.php?action=belum&id="+ id;

                JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                if (response.optString("result").equals("true")){
                                    Toast.makeText(getApplicationContext(), "Pekerjaan Belum terselesaikan", Toast.LENGTH_SHORT).show();

                                    finish(); //tutup activity
                                }else{
                                    Toast.makeText(getApplicationContext(), "O ow, sepertinya harus dicoba lagi", Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(ProgressBar.GONE); //sembunyikan progress bar
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("Events: ", error.toString());

                        pb.setVisibility(ProgressBar.GONE); //sembunyikan progress bar

                        Toast.makeText(getApplicationContext(), "Hmm, masalah internet atau data yang kamu masukkan", Toast.LENGTH_SHORT).show();
                    }
                });

                queue.add(jsObjRequest);
            }
        });
    }

}