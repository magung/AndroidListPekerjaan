package com.magung.utsandroidagung;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder dialog;
    EditText etID, etNama, etDeskripsi, etPekerjaan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("List Pekerjaan"); // for set actionbar title

        //load fragment pertama kali
        Cons.ACTIVE_FRAGMENT="FirstFragment";
        loadFragment(new FirstFragment());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.form_input_data,
                        null);

                dialog.setView(view);
                dialog.setCancelable(true);

                //definisi objek
                etID = (EditText) view.findViewById(R.id.et_id);
                etNama = (EditText) view.findViewById(R.id.et_nama);
                etDeskripsi = (EditText) view.findViewById(R.id.et_deskripsi);
                etPekerjaan = (EditText) view.findViewById(R.id.et_pekerjaan);

                dialog.setPositiveButton("SIMPAN",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String id, nama, deskripsi, pekerjaan;

                                id = etID.getText().toString();
                                nama = etNama.getText().toString();
                                deskripsi = etDeskripsi.getText().toString();
                                pekerjaan = etPekerjaan.getText().toString();

                                //simpan customer
                                simpanData(id, nama, deskripsi, pekerjaan);
                            }
                        });

                dialog.setNegativeButton("BATAL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });


                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.d("Pekerjaan ", "onResume: "+ Cons.ACTIVE_FRAGMENT);

            //cek fragment mana yang sebelumnya aktif
            if( Cons.ACTIVE_FRAGMENT.equals("FirstFragment")) {
                loadFragment(new FirstFragment());
            }else if( Cons.ACTIVE_FRAGMENT.equals("SecondFragment")){
                loadFragment(new SecondFragment());
            }else if( Cons.ACTIVE_FRAGMENT.equals("ThirdFragment")){
                loadFragment(new ThirdFragment());
            }

        }catch (Exception e){
            Log.d("Pekerjaan ", "onResume: "+ e.getMessage() );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.m_semua) {
            Cons.ACTIVE_FRAGMENT = "FirstFragment";
            loadFragment(new FirstFragment());
            return true;
        }else if (id == R.id.m_belum) {
            Cons.ACTIVE_FRAGMENT = "SecondFragment";
            loadFragment(new SecondFragment());
            return true;
        }else if (id == R.id.m_selesai) {
            Cons.ACTIVE_FRAGMENT = "ThirdFragment";
            loadFragment(new ThirdFragment());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void simpanData(String id, String nama, String deskripsi, String pekerjaan) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://rest-api-agung-411.000webhostapp.com/api/produk.php?action=simpan&id="+id+"&nama="+nama+"&deskripsi="+deskripsi+"&pekerjaan="+pekerjaan;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        if (response.optString("result").equals("true")){
                            Toast.makeText(getApplicationContext(), "Sukses menambah data!", Toast.LENGTH_SHORT).show();

                            //panggil fungsi load pada fragment
                            loadFragment(new FirstFragment());

                        }else{
                            Toast.makeText(getApplicationContext(), "Gagal menambah data", Toast.LENGTH_SHORT).show();
                        }                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.d("Events: ", error.toString());

                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }

    public void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        };
    }
}