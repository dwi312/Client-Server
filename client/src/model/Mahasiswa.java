package model;


/**
 * Model Mahasiswa di sisi client (mirip dengan server)
 */
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

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getJurusan() { return jurusan; }
    public void setJurusan(String jurusan) { this.jurusan = jurusan; }

    public int getAngkatan() { return angkatan; }
    public void setAngkatan(int angkatan) { this.angkatan = angkatan; }

    @Override
    public String toString() {
        return nim + " | " + nama + " | " + jurusan + " | " + angkatan;
    }
}
