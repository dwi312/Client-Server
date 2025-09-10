package com.app.server.model;

public class Mahasiswa {
    private String nim;
    private String nama;
    private String jurusan;
    private int angkatan;

    public Mahasiswa() {}

    public Mahasiswa(String nim, String nama, String jurusan, int angkatan) {
        this.nim = nim;
        this.nama = nama;
        this.jurusan = jurusan;
        this.angkatan = angkatan;
    }

    public String getNim() {
        return nim;
    }
    
    public String getNama() {
        return nama;
    }

    public String getJurusan() {
        return jurusan;
    }

    public int getAngkatan() {
        return angkatan;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public void setAngkatan(int angkatan) {
        this.angkatan = angkatan;
    }

}
