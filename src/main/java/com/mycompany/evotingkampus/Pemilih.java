package com.mycompany.evotingkampus;

public class Pemilih extends Pengguna {
    private boolean sudahMengambilKode;

    public Pemilih(String npm, String nama, String password) {
        super(npm, nama, password);
        this.sudahMengambilKode = false;
    }

    public boolean isSudahMengambilKode() {
        return this.sudahMengambilKode;
    }

    public void setSudahMengambilKode(boolean status) {
        this.sudahMengambilKode = status;
    }

    @Override
    public void tampilkanMenu() {
        System.out.println("\n=== MENU PEMILIH (" + getNama() + ") ===");
    }

    @Override
    public void tampilanData() {
        super.tampilanData();
        System.out.println("Peran: Pemilih");
        System.out.println("Sudah mengambil kode: " + (this.sudahMengambilKode ? "Ya" : "Belum"));
    }
}