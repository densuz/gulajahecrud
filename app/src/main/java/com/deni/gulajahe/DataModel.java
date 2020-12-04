package com.deni.gulajahe;

public class DataModel {
    private String id_produk, kd_produk, nama_produk, harga, jumlah_produk;
    public DataModel() {
    }
    public DataModel(String id_produk,String kd_produk, String nama_produk, String harga, String jumlah_produk) {
        this.id_produk = id_produk;
        this.kd_produk = kd_produk;
        this.nama_produk = nama_produk;
        this.harga = harga;
        this.jumlah_produk = jumlah_produk;
    }
    public String getid_produk() {
        return id_produk;
    }
    public void setid_produk(String id_produk) {
        this.id_produk = id_produk;
    }
    public String getkd_produk()
    {return kd_produk;}
    public void setkd_produk(String kd_produk)
    {this.kd_produk = kd_produk;}

    public String getnama_produk() {
        return nama_produk;
    }
    public void setnama_produk(String nama_produk) {
        this.nama_produk = nama_produk;
    }
    public String getharga() {
        return harga;
    }
    public void setharga(String harga) {
        this.harga = harga;
    }
    public String getjumlah_produk() {
        return jumlah_produk;
    }
    public void setjumlah_produk(String jumlah_produk) {
        this.jumlah_produk = jumlah_produk;
    }
}
