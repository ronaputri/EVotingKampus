package com.mycompany.evotingkampus;

public class Kandidat {
    private int nomorUrut;
    private String nama;
    private String visi;
    private String misi;
    private int jumlahSuara;

    public static int totalKandidat = 0;

    public Kandidat(int nomorUrut, String nama, String visi, String misi) {
        setNomorUrut(nomorUrut);
        this.nama = nama;
        this.visi = visi;
        this.misi = misi;
        this.jumlahSuara = 0;
        totalKandidat++;
    }

    public int getNomorUrut() {
        return this.nomorUrut;
    }

    public void setNomorUrut(int nomorUrut) {
        if (nomorUrut > 0) {
            this.nomorUrut = nomorUrut;
        } else {
            System.out.println("Nomor urut tidak valid! Dipakai default: 1");
            this.nomorUrut = 1;
        }
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getVisi() {
        return this.visi;
    }

    public String getMisi() {
        return this.misi;
    }

    public int getJumlahSuara() {
        return this.jumlahSuara;
    }

    public void tambahSuara() {
        this.jumlahSuara++;
    }

    public void tampilkanInfo() {
        System.out.println(this.nomorUrut + ". " + this.nama);
    }

    public void tampilkanInfo(boolean lengkap) {
        tampilkanInfo();
        if (lengkap) {
            System.out.println("   Visi: " + this.visi);
            System.out.println("   Misi: " + this.misi);
        }
    }
}