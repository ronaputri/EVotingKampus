package com.mycompany.evotingkampus;

public class Admin extends Pengguna {

    public Admin(String id, String nama, String password) {
        super(id, nama, password);
    }

    @Override
    public void tampilkanMenu() {
        System.out.println("\n=== MENU ADMIN (" + getNama() + ") ===");
        System.out.println("1. Tambah kandidat");
        System.out.println("2. Tambah pemilih");
        System.out.println("3. Lihat daftar kandidat");
        System.out.println("4. Buka/tutup sesi voting");
        System.out.println("5. Lihat rekapitulasi");
        System.out.println("0. Logout");
    }

    @Override
    public void tampilanData() {
        super.tampilanData();
        System.out.println("Peran: Admin (Panitia)");
    }
}