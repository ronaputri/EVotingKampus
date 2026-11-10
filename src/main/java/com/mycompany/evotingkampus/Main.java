package com.mycompany.evotingkampus;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    static Admin admin = new Admin("admin", "Panitia", "admin123");

    static Kandidat[] daftarKandidat = new Kandidat[10];
    static int jumlahKandidat = 0;

    static Pemilih[] daftarPemilih = new Pemilih[50];
    static int jumlahPemilih = 0;

    static String[] kodeAktif = new String[50];
    static int jumlahKode = 0;

    static boolean sesiDibuka = false;

    public static void main(String[] args) {
        // Instansiasi objek (data awal agar mudah diuji)
        daftarKandidat[jumlahKandidat++] = new Kandidat(1, "Andi", "Kampus aktif", "Perbanyak kegiatan");
        daftarKandidat[jumlahKandidat++] = new Kandidat(2, "Budi", "Kampus inovatif", "Dukung riset mahasiswa");
        daftarPemilih[jumlahPemilih++] = new Pemilih("2517052053", "Rona", "123");
        daftarPemilih[jumlahPemilih++] = new Pemilih("2517052054", "Sinta", "123");

        System.out.println("=== DATA AWAL SISTEM ===");
        admin.tampilanData();
        System.out.println("------------------------");
        daftarPemilih[0].tampilanData();
        System.out.println("------------------------");
        System.out.println("Total pengguna : " + Pengguna.totalPengguna);
        System.out.println("Total kandidat : " + Kandidat.totalKandidat);

        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n========================================");
            System.out.println("   E-VOTING ORGANISASI KAMPUS");
            System.out.println("========================================");
            System.out.println("1. Login Admin");
            System.out.println("2. Login Pemilih");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            int pilihan = bacaAngka();

            switch (pilihan) {
                case 1:
                    loginAdmin();
                    break;
                case 2:
                    loginPemilih();
                    break;
                case 0:
                    System.out.println("Terima kasih telah menggunakan E-Voting!");
                    isRunning = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan masukkan angka 0-2.");
                    break;
            }
        }
    }

    static int bacaAngka() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static void loginAdmin() {
        System.out.print("ID Admin : ");
        String id = scanner.nextLine();
        System.out.print("Password : ");
        String pw = scanner.nextLine();

        if (!(admin.getId().equals(id) && admin.cekPassword(pw))) {
            System.out.println("Login admin gagal!");
            return;
        }

        int pilihan = -1;
        while (pilihan != 0) {
            admin.tampilkanMenu();
            System.out.print("Pilih: ");
            pilihan = bacaAngka();

            switch (pilihan) {
                case 1:
                    tambahKandidat();
                    break;
                case 2:
                    tambahPemilih();
                    break;
                case 3:
                    lihatKandidat();
                    break;
                case 4:
                    sesiDibuka = !sesiDibuka;
                    System.out.println("Sesi voting sekarang: " + (sesiDibuka ? "DIBUKA" : "DITUTUP"));
                    break;
                case 5:
                    tampilkanRekap();
                    break;
                case 0:
                    System.out.println("Logout admin.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan masukkan angka 0-5.");
                    break;
            }
        }
    }

    static void tambahKandidat() {
        if (jumlahKandidat >= daftarKandidat.length) {
            System.out.println("Maaf, kapasitas kandidat sudah penuh!");
            return;
        }
        System.out.println("\n-- Form Tambah Kandidat --");
        System.out.print("Nomor urut : ");
        int no = bacaAngka();
        System.out.print("Nama       : ");
        String nama = scanner.nextLine();
        System.out.print("Visi       : ");
        String visi = scanner.nextLine();
        System.out.print("Misi       : ");
        String misi = scanner.nextLine();

        daftarKandidat[jumlahKandidat] = new Kandidat(no, nama, visi, misi);
        jumlahKandidat++;
        System.out.println("Sukses! Kandidat berhasil ditambahkan.");
    }

    static void tambahPemilih() {
        if (jumlahPemilih >= daftarPemilih.length) {
            System.out.println("Maaf, kapasitas pemilih sudah penuh!");
            return;
        }
        System.out.println("\n-- Form Daftar Pemilih --");
        System.out.print("NPM      : ");
        String npm = scanner.nextLine();

        if (cariPemilih(npm) != null) {
            System.out.println("NPM sudah terdaftar!");
            return;
        }

        System.out.print("Nama     : ");
        String nama = scanner.nextLine();
        System.out.print("Password : ");
        String pw = scanner.nextLine();

        daftarPemilih[jumlahPemilih] = new Pemilih(npm, nama, pw);
        jumlahPemilih++;
        System.out.println("Sukses! Pemilih berhasil didaftarkan.");
    }

    static void lihatKandidat() {
        System.out.println("\n--- Daftar Kandidat ---");
        if (jumlahKandidat == 0) {
            System.out.println("Belum ada kandidat.");
        } else {
            for (int i = 0; i < jumlahKandidat; i++) {
                daftarKandidat[i].tampilkanInfo(true);
            }
        }
    }

    static void tampilkanRekap() {
        int total = 0;
        for (int i = 0; i < jumlahKandidat; i++) {
            total += daftarKandidat[i].getJumlahSuara();
        }

        System.out.println("\n=== REKAPITULASI SUARA ===");
        Kandidat unggul = null;
        for (int i = 0; i < jumlahKandidat; i++) {
            Kandidat k = daftarKandidat[i];
            double persen = 0;
            if (total > 0) {
                persen = k.getJumlahSuara() * 100.0 / total;
            }
            System.out.printf("No %d - %s : %d suara (%.1f%%)%n",
                    k.getNomorUrut(), k.getNama(), k.getJumlahSuara(), persen);

            if (unggul == null || k.getJumlahSuara() > unggul.getJumlahSuara()) {
                unggul = k;
            }
        }
        System.out.println("Total suara masuk: " + total);
        if (total > 0 && unggul != null) {
            System.out.println("Kandidat unggul  : " + unggul.getNama());
        }
    }

    static Pemilih cariPemilih(String npm) {
        for (int i = 0; i < jumlahPemilih; i++) {
            if (daftarPemilih[i].getId().equals(npm)) {
                return daftarPemilih[i];
            }
        }
        return null;
    }

    static int cariKode(String kode) {
        for (int i = 0; i < jumlahKode; i++) {
            if (kodeAktif[i] != null && kodeAktif[i].equals(kode)) {
                return i;
            }
        }
        return -1;
    }

    static String buatKode() {
        String kode;
        do {
            kode = "VOTE-" + ((int) (Math.random() * 900000) + 100000);
        } while (cariKode(kode) != -1);
        return kode;
    }

    static void loginPemilih() {
        System.out.print("NPM      : ");
        String npm = scanner.nextLine();
        System.out.print("Password : ");
        String pw = scanner.nextLine();

        Pemilih p = cariPemilih(npm);
        if (p == null || !p.cekPassword(pw)) {
            System.out.println("NPM tidak terdaftar atau password salah. Akses ditolak.");
            return;
        }
        if (!sesiDibuka) {
            System.out.println("Sesi voting belum dibuka. Akses ditolak.");
            return;
        }
        if (p.isSudahMengambilKode()) {
            System.out.println("NPM ini sudah mengambil kode. Akses ditolak.");
            return;
        }

        p.tampilkanMenu();

        String kode = buatKode();
        kodeAktif[jumlahKode] = kode;
        jumlahKode++;
        p.setSudahMengambilKode(true);

        System.out.println("Kode pemilihan Anda: " + kode);
        System.out.println("Catat kode ini lalu masukkan di bawah.");
        System.out.print("Masukkan kode: ");
        String kodeInput = scanner.nextLine();

        int posisiKode = cariKode(kodeInput);
        if (posisiKode == -1) {
            System.out.println("Kode tidak valid atau sudah dipakai.");
            return;
        }

        System.out.println("\n--- Daftar Kandidat ---");
        for (int i = 0; i < jumlahKandidat; i++) {
            daftarKandidat[i].tampilkanInfo(true);
        }

        System.out.print("Pilih nomor kandidat: ");
        int no = bacaAngka();

        Kandidat dipilih = null;
        for (int i = 0; i < jumlahKandidat; i++) {
            if (daftarKandidat[i].getNomorUrut() == no) {
                dipilih = daftarKandidat[i];
            }
        }
        if (dipilih == null) {
            System.out.println("Nomor kandidat tidak ditemukan.");
            return;
        }

        System.out.print("Yakin memilih " + dipilih.getNama() + "? (y/n): ");
        String yakin = scanner.nextLine();

        if (yakin.equalsIgnoreCase("y")) {
            dipilih.tambahSuara();          // suara anonim (tanpa identitas)
            kodeAktif[posisiKode] = null;   // kode hangus
            System.out.println("Pemilihan berhasil dilakukan. Terima kasih!");
        } else {
            System.out.println("Pemilihan dibatalkan.");
        }
    }
}