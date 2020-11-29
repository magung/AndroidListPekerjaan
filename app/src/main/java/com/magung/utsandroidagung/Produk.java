package com.magung.utsandroidagung;

public class Produk {
    String id, nama, deskripsi, pekerjaan, status;



    public Produk(String id, String nama, String deskripsi, String pekerjaan, String status) {
        this.id = id;
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.pekerjaan = pekerjaan;
        this.status = status;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }
}
