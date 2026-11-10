package com.mycompany.evotingkampus;

public class Pengguna {
    private String id;
    private String nama;
    private String password;

    public static int totalPengguna = 0;

    public Pengguna(String id, String nama, String password) {
        this.id = id;
        this.nama = nama;
        setPassword(password);
        totalPengguna++;
    }

    public String getId() {
        return this.id;
    }

    public String getNama() {
        return this.nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setPassword(String password) {
        if (password != null && password.length() >= 3) {
            this.password = password;
        } else {
            System.out.println("Password minimal 3 karakter! Dipakai default: 123");
            this.password = "123";
        }
    }

    public boolean cekPassword(String input) {
        return this.password.equals(input);
    }

    public void tampilkanMenu() {
        System.out.println("\n=== MENU PENGGUNA ===");
    }

    public void tampilanData() {
        System.out.println("ID   : " + this.id);
        System.out.println("Nama : " + this.nama);
    }
}