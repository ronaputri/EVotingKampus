package com.mycompany.evotingkampus;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EVotingApp extends Application {

    // ===== Data aplikasi (memakai class dari tugas sebelumnya) =====
    private Admin admin = new Admin("admin", "Panitia", "admin123");
    private ArrayList<Kandidat> daftarKandidat = new ArrayList<>();
    private ArrayList<Pemilih> daftarPemilih = new ArrayList<>();
    private ArrayList<String> kodeAktif = new ArrayList<>();
    private boolean sesiDibuka = false;

    private Stage stage;
    private Scene scene;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // Data awal agar mudah diuji
        daftarKandidat.add(new Kandidat(1, "Andi", "Kampus aktif", "Perbanyak kegiatan"));
        daftarKandidat.add(new Kandidat(2, "Budi", "Kampus inovatif", "Dukung riset mahasiswa"));
        daftarPemilih.add(new Pemilih("2517052053", "Rona", "123"));
        daftarPemilih.add(new Pemilih("2517052054", "Sinta", "123"));

        stage.setTitle("E-Voting Organisasi Kampus");
        stage.setMinWidth(860);
        stage.setMinHeight(600);
        tampilkanHalamanLogin();
        stage.show();
    }

    // =====================================================
    // METHOD BANTU
    // =====================================================
    // Satu Scene dipakai terus, hanya isinya (root) yang diganti,
    // sehingga ukuran jendela tidak berubah-ubah antar halaman.
    private void setHalaman(VBox root) {
        if (scene == null) {
            scene = new Scene(root, 980, 660);
            stage.setScene(scene);
        } else {
            scene.setRoot(root);
        }
    }

    private void tampilkanAlert(AlertType tipe, String judul, String pesan) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    private Label buatJudul(String teks, int ukuran) {
        Label lbl = new Label(teks);
        lbl.setStyle("-fx-font-size: " + ukuran + "px; -fx-font-weight: bold;");
        return lbl;
    }

    // Bagian berjudul: judul + garis pemisah + isi
    private VBox bagian(String judul, Node... isi) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        box.getChildren().add(buatJudul(judul, 16));
        box.getChildren().add(new Separator());
        box.getChildren().addAll(isi);
        return box;
    }

    // Form dua kolom: label di kiri, isian melebar di kanan
    private GridPane buatGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(12);
        ColumnConstraints kolomLabel = new ColumnConstraints();
        kolomLabel.setMinWidth(110);
        ColumnConstraints kolomIsi = new ColumnConstraints();
        kolomIsi.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().addAll(kolomLabel, kolomIsi);
        return grid;
    }

    private void tambahBaris(GridPane grid, int baris, String label, TextField isian) {
        isian.setMaxWidth(Double.MAX_VALUE);
        grid.add(new Label(label), 0, baris);
        grid.add(isian, 1, baris);
    }

    // Header: judul di kiri, keterangan/tombol di kanan, lalu garis pemisah
    private VBox buatRoot(String judul, Node kanan, Node isi) {
        Region pengisi = new Region();
        HBox.setHgrow(pengisi, Priority.ALWAYS);
        HBox header = new HBox(10, buatJudul(judul, 18), pengisi, kanan);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(14, 20, 14, 20));

        VBox.setVgrow(isi, Priority.ALWAYS);
        return new VBox(header, new Separator(), isi);
    }

    // =====================================================
    // HALAMAN LOGIN
    // =====================================================
    private void tampilkanHalamanLogin() {
        TextField txtId = new TextField();
        txtId.setPromptText("admin atau NPM");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");
        txtPassword.setMaxWidth(Double.MAX_VALUE);

        GridPane form = buatGrid();
        tambahBaris(form, 0, "ID / NPM", txtId);
        form.add(new Label("Password"), 0, 1);
        form.add(txtPassword, 1, 1);

        Button btnAdmin = new Button("Login Admin");
        Button btnPemilih = new Button("Login Pemilih");
        btnAdmin.setMaxWidth(Double.MAX_VALUE);
        btnPemilih.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnAdmin, Priority.ALWAYS);
        HBox.setHgrow(btnPemilih, Priority.ALWAYS);
        HBox tombol = new HBox(12, btnAdmin, btnPemilih);

        // Event: Login Admin
        btnAdmin.setOnAction(e -> {
            String id = txtId.getText().trim();
            String pw = txtPassword.getText();

            if (id.isEmpty() || pw.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "ID dan password wajib diisi!");
                return;
            }
            if (admin.getId().equals(id) && admin.cekPassword(pw)) {
                tampilkanHalamanAdmin();
            } else {
                tampilkanAlert(AlertType.ERROR, "Login Gagal", "ID atau password admin salah!");
            }
        });

        // Event: Login Pemilih
        btnPemilih.setOnAction(e -> {
            String npm = txtId.getText().trim();
            String pw = txtPassword.getText();

            if (npm.isEmpty() || pw.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "NPM dan password wajib diisi!");
                return;
            }
            Pemilih p = cariPemilih(npm);
            if (p == null || !p.cekPassword(pw)) {
                tampilkanAlert(AlertType.ERROR, "Login Gagal",
                        "NPM tidak terdaftar atau password salah. Akses ditolak.");
                return;
            }
            tampilkanHalamanPemilih(p);
        });

        VBox formLogin = new VBox(16, buatJudul("E-VOTING", 26),
                new Label("Pemilihan Ketua Organisasi Kampus"), form, tombol);
        formLogin.setAlignment(Pos.CENTER);
        formLogin.setMaxWidth(420);

        VBox root = new VBox(formLogin);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        setHalaman(root);
    }

    // =====================================================
    // HALAMAN ADMIN
    // =====================================================
    private void tampilkanHalamanAdmin() {
        Label lblSesi = new Label();
        perbaruiLabelSesi(lblSesi);

        Button btnBeranda = new Button("Beranda");
        Button btnTambahKandidat = new Button("Tambah Kandidat");
        Button btnDaftarKandidat = new Button("Lihat Kandidat");
        Button btnTambahPemilih = new Button("Tambah Pemilih");
        Button btnDaftarPemilih = new Button("Lihat / Hapus Pemilih");
        Button btnRekap = new Button("Rekapitulasi");
        Button btnSesi = new Button(sesiDibuka ? "Tutup Sesi Voting" : "Buka Sesi Voting");
        Button btnLogout = new Button("Logout");

        Button[] semua = {btnBeranda, btnTambahKandidat, btnDaftarKandidat, btnTambahPemilih,
            btnDaftarPemilih, btnRekap, btnSesi, btnLogout};
        for (Button b : semua) {
            b.setMaxWidth(Double.MAX_VALUE);
        }

        Region pengisi = new Region();
        VBox.setVgrow(pengisi, Priority.ALWAYS);

        VBox sidebar = new VBox(8, new Label("MENU"), btnBeranda, btnTambahKandidat,
                btnDaftarKandidat, btnTambahPemilih, btnDaftarPemilih, btnRekap,
                new Label("SESI VOTING"), btnSesi, pengisi, btnLogout);
        sidebar.setPadding(new Insets(16));
        sidebar.setPrefWidth(220);
        sidebar.setMinWidth(220);

        // Area konten di sebelah kanan menu
        VBox area = new VBox();
        HBox.setHgrow(area, Priority.ALWAYS);
        tampilkanKonten(area, buatBeranda());

        HBox badan = new HBox(sidebar, new Separator(javafx.geometry.Orientation.VERTICAL), area);
        VBox root = buatRoot("MENU ADMIN  |  " + admin.getNama(), lblSesi, badan);

        btnBeranda.setOnAction(e -> tampilkanKonten(area, buatBeranda()));
        btnTambahKandidat.setOnAction(e -> tampilkanKonten(area, buatFormKandidat()));
        btnDaftarKandidat.setOnAction(e -> tampilkanKonten(area, buatDaftarKandidat()));
        btnTambahPemilih.setOnAction(e -> tampilkanKonten(area, buatFormPemilih()));
        btnDaftarPemilih.setOnAction(e -> tampilkanKonten(area, buatDaftarPemilih()));
        btnRekap.setOnAction(e -> tampilkanKonten(area, buatRekap()));

        btnSesi.setOnAction(e -> {
            sesiDibuka = !sesiDibuka;
            btnSesi.setText(sesiDibuka ? "Tutup Sesi Voting" : "Buka Sesi Voting");
            perbaruiLabelSesi(lblSesi);
            tampilkanAlert(AlertType.INFORMATION, "Sesi Voting",
                    "Sesi voting sekarang: " + (sesiDibuka ? "DIBUKA" : "DITUTUP"));
            tampilkanKonten(area, buatBeranda());
        });

        btnLogout.setOnAction(e -> tampilkanHalamanLogin());

        setHalaman(root);
    }

    private void tampilkanKonten(VBox area, Node konten) {
        VBox.setVgrow(konten, Priority.ALWAYS);
        area.getChildren().setAll(konten);
    }

    private void perbaruiLabelSesi(Label lbl) {
        lbl.setText("Sesi voting: " + (sesiDibuka ? "DIBUKA" : "DITUTUP"));
        lbl.setStyle("-fx-font-weight: bold;");
    }

    // ----- Beranda (ringkasan) -----
    private VBox buatBeranda() {
        int sudahAmbil = 0;
        for (Pemilih p : daftarPemilih) {
            if (p.isSudahMengambilKode()) {
                sudahAmbil++;
            }
        }

        GridPane ringkasan = new GridPane();
        ringkasan.setHgap(30);
        ringkasan.setVgap(10);
        ringkasan.add(new Label("Jumlah kandidat"), 0, 0);
        ringkasan.add(new Label(": " + daftarKandidat.size()), 1, 0);
        ringkasan.add(new Label("Pemilih terdaftar"), 0, 1);
        ringkasan.add(new Label(": " + daftarPemilih.size()), 1, 1);
        ringkasan.add(new Label("Sudah mengambil kode"), 0, 2);
        ringkasan.add(new Label(": " + sudahAmbil), 1, 2);
        ringkasan.add(new Label("Status sesi voting"), 0, 3);
        ringkasan.add(new Label(": " + (sesiDibuka ? "DIBUKA" : "DITUTUP")), 1, 3);

        Label petunjuk = new Label("Gunakan menu di sebelah kiri untuk mengelola kandidat, "
                + "pemilih, dan sesi voting.");
        petunjuk.setWrapText(true);
        return bagian("Selamat datang, " + admin.getNama(), ringkasan, petunjuk);
    }

    // ----- Form tambah kandidat -----
    private VBox buatFormKandidat() {
        TextField txtNo = new TextField();
        TextField txtNama = new TextField();
        TextField txtVisi = new TextField();
        TextField txtMisi = new TextField();
        Button btnSimpan = new Button("Simpan Kandidat");

        GridPane form = buatGrid();
        tambahBaris(form, 0, "Nomor urut", txtNo);
        tambahBaris(form, 1, "Nama", txtNama);
        tambahBaris(form, 2, "Visi", txtVisi);
        tambahBaris(form, 3, "Misi", txtMisi);

        btnSimpan.setOnAction(e -> {
            // Validasi kolom kosong
            if (txtNo.getText().trim().isEmpty() || txtNama.getText().trim().isEmpty()
                    || txtVisi.getText().trim().isEmpty() || txtMisi.getText().trim().isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Semua kolom wajib diisi!");
                return;
            }
            // Validasi format angka dengan try-catch
            int no;
            try {
                no = Integer.parseInt(txtNo.getText().trim());
            } catch (NumberFormatException ex) {
                tampilkanAlert(AlertType.ERROR, "Format Salah", "Nomor urut harus berupa angka!");
                return;
            }
            if (no <= 0) {
                tampilkanAlert(AlertType.ERROR, "Format Salah", "Nomor urut harus lebih dari 0!");
                return;
            }
            if (cariKandidat(no) != null) {
                tampilkanAlert(AlertType.ERROR, "Duplikat", "Nomor urut " + no + " sudah dipakai!");
                return;
            }

            daftarKandidat.add(new Kandidat(no, txtNama.getText().trim(),
                    txtVisi.getText().trim(), txtMisi.getText().trim()));
            tampilkanAlert(AlertType.INFORMATION, "Sukses", "Kandidat berhasil ditambahkan.");
            txtNo.clear();
            txtNama.clear();
            txtVisi.clear();
            txtMisi.clear();
        });

        return bagian("Tambah Kandidat", form, btnSimpan);
    }

    // ----- Form tambah pemilih -----
    private VBox buatFormPemilih() {
        TextField txtNpm = new TextField();
        TextField txtNama = new TextField();
        PasswordField txtPassword = new PasswordField();
        Button btnSimpan = new Button("Daftarkan Pemilih");

        GridPane form = buatGrid();
        tambahBaris(form, 0, "NPM", txtNpm);
        tambahBaris(form, 1, "Nama", txtNama);
        tambahBaris(form, 2, "Password awal", txtPassword);

        btnSimpan.setOnAction(e -> {
            String npm = txtNpm.getText().trim();
            String nama = txtNama.getText().trim();
            String pw = txtPassword.getText();

            if (npm.isEmpty() || nama.isEmpty() || pw.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Semua kolom wajib diisi!");
                return;
            }
            if (pw.length() < 3) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Password minimal 3 karakter!");
                return;
            }
            if (cariPemilih(npm) != null) {
                tampilkanAlert(AlertType.ERROR, "Duplikat", "NPM sudah terdaftar!");
                return;
            }

            daftarPemilih.add(new Pemilih(npm, nama, pw));
            tampilkanAlert(AlertType.INFORMATION, "Sukses", "Pemilih berhasil didaftarkan.");
            txtNpm.clear();
            txtNama.clear();
            txtPassword.clear();
        });

        return bagian("Tambah Pemilih", form, btnSimpan);
    }

    // ----- Daftar kandidat -----
    private VBox buatDaftarKandidat() {
        TextArea area = new TextArea(teksKandidat());
        area.setEditable(false);
        VBox.setVgrow(area, Priority.ALWAYS);
        return bagian("Daftar Kandidat", area);
    }

    private String teksKandidat() {
        if (daftarKandidat.isEmpty()) {
            return "Belum ada kandidat.";
        }
        String teks = "";
        for (Kandidat k : daftarKandidat) {
            teks += k.getNomorUrut() + ". " + k.getNama() + "\n";
            teks += "   Visi: " + k.getVisi() + "\n";
            teks += "   Misi: " + k.getMisi() + "\n\n";
        }
        return teks;
    }

    // ----- Daftar pemilih terdaftar + hapus pemilih -----
    private VBox buatDaftarPemilih() {
        TextArea area = new TextArea(teksPemilih());
        area.setEditable(false);
        VBox.setVgrow(area, Priority.ALWAYS);

        TextField txtHapus = new TextField();
        txtHapus.setPromptText("NPM yang akan dihapus");
        HBox.setHgrow(txtHapus, Priority.ALWAYS);
        Button btnHapus = new Button("Hapus Pemilih");

        btnHapus.setOnAction(e -> {
            String npm = txtHapus.getText().trim();
            if (npm.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "NPM wajib diisi!");
                return;
            }
            Pemilih p = cariPemilih(npm);
            if (p == null) {
                tampilkanAlert(AlertType.ERROR, "Tidak Ditemukan", "NPM tidak terdaftar!");
                return;
            }
            // Pemilih yang sudah mengambil kode tidak boleh dihapus
            // (agar NPM tidak bisa didaftarkan ulang lalu memilih dua kali)
            if (p.isSudahMengambilKode()) {
                tampilkanAlert(AlertType.ERROR, "Tidak Bisa Dihapus",
                        "Pemilih ini sudah mengambil kode, sehingga tidak boleh dihapus.");
                return;
            }

            Alert konfirmasi = new Alert(AlertType.CONFIRMATION);
            konfirmasi.setTitle("Konfirmasi Hapus");
            konfirmasi.setHeaderText(null);
            konfirmasi.setContentText("Hapus pemilih " + p.getNama() + " (" + p.getId() + ")?");
            ButtonType hasil = konfirmasi.showAndWait().orElse(ButtonType.CANCEL);

            if (hasil == ButtonType.OK) {
                daftarPemilih.remove(p);
                area.setText(teksPemilih());   // segarkan daftar
                txtHapus.clear();
                tampilkanAlert(AlertType.INFORMATION, "Sukses", "Pemilih berhasil dihapus.");
            }
        });

        HBox barisHapus = new HBox(10, txtHapus, btnHapus);
        return bagian("Daftar Pemilih Terdaftar", area, new Label("Hapus pemilih (isi NPM):"), barisHapus);
    }

    private String teksPemilih() {
        if (daftarPemilih.isEmpty()) {
            return "Belum ada pemilih terdaftar.";
        }
        String teks = "";
        int no = 1;
        int sudahAmbil = 0;
        for (Pemilih p : daftarPemilih) {
            String status = p.isSudahMengambilKode() ? "Sudah mengambil kode" : "Belum mengambil kode";
            if (p.isSudahMengambilKode()) {
                sudahAmbil++;
            }
            teks += no + ". " + p.getId() + " - " + p.getNama() + " (" + status + ")\n";
            no++;
        }
        teks += "\nTotal pemilih terdaftar : " + daftarPemilih.size();
        teks += "\nSudah mengambil kode    : " + sudahAmbil;
        teks += "\nBelum mengambil kode    : " + (daftarPemilih.size() - sudahAmbil);
        return teks;
    }

    // ----- Rekapitulasi -----
    private VBox buatRekap() {
        int total = 0;
        for (Kandidat k : daftarKandidat) {
            total += k.getJumlahSuara();
        }

        String teks = "";
        Kandidat unggul = null;
        for (Kandidat k : daftarKandidat) {
            double persen = 0;
            if (total > 0) {
                persen = k.getJumlahSuara() * 100.0 / total;
            }
            teks += String.format("No %d - %s : %d suara (%.1f%%)%n",
                    k.getNomorUrut(), k.getNama(), k.getJumlahSuara(), persen);
            if (unggul == null || k.getJumlahSuara() > unggul.getJumlahSuara()) {
                unggul = k;
            }
        }
        teks += "\nTotal suara masuk: " + total + "\n";
        if (total > 0 && unggul != null) {
            teks += "Kandidat unggul  : " + unggul.getNama() + "\n";
        }

        TextArea area = new TextArea(teks);
        area.setEditable(false);
        VBox.setVgrow(area, Priority.ALWAYS);
        return bagian("Rekapitulasi Suara", area);
    }

    // =====================================================
    // HALAMAN PEMILIH
    // =====================================================
    private void tampilkanHalamanPemilih(Pemilih pemilih) {
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> tampilkanHalamanLogin());

        // Langkah 1: ambil kode
        Button btnAmbilKode = new Button("Ambil Kode Pemilihan");
        Label lblKode = new Label("Kode Anda: (belum diambil)");
        lblKode.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");

        // Langkah 2: verifikasi kode
        TextField txtKode = new TextField();
        txtKode.setPromptText("Masukkan kode di sini");
        HBox.setHgrow(txtKode, Priority.ALWAYS);
        Button btnCekKode = new Button("Verifikasi Kode");

        // Langkah 3: pilih kandidat
        TextArea areaKandidat = new TextArea(teksKandidat());
        areaKandidat.setEditable(false);
        VBox.setVgrow(areaKandidat, Priority.ALWAYS);
        TextField txtPilihan = new TextField();
        txtPilihan.setPromptText("Nomor kandidat pilihan Anda");
        HBox.setHgrow(txtPilihan, Priority.ALWAYS);
        Button btnKirim = new Button("Kirim Suara");

        // Awalnya langkah 2 dan 3 dikunci
        txtKode.setDisable(true);
        btnCekKode.setDisable(true);
        txtPilihan.setDisable(true);
        btnKirim.setDisable(true);

        // Event: ambil kode
        btnAmbilKode.setOnAction(e -> {
            if (!sesiDibuka) {
                tampilkanAlert(AlertType.WARNING, "Sesi Ditutup",
                        "Sesi voting belum dibuka. Akses ditolak.");
                return;
            }
            if (pemilih.isSudahMengambilKode()) {
                tampilkanAlert(AlertType.ERROR, "Ditolak",
                        "NPM ini sudah mengambil kode. Akses ditolak.");
                return;
            }

            String kode = buatKode();
            kodeAktif.add(kode);                      // kode TIDAK disimpan bersama NPM
            pemilih.setSudahMengambilKode(true);      // NPM hanya ditandai
            lblKode.setText("Kode Anda: " + kode);
            tampilkanAlert(AlertType.INFORMATION, "Kode Pemilihan",
                    "Kode Anda: " + kode + "\nCatat kode ini lalu masukkan di kolom verifikasi.");

            btnAmbilKode.setDisable(true);
            txtKode.setDisable(false);
            btnCekKode.setDisable(false);
        });

        // Event: verifikasi kode
        btnCekKode.setOnAction(e -> {
            String kodeInput = txtKode.getText().trim();
            if (kodeInput.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Kode wajib diisi!");
                return;
            }
            if (!kodeAktif.contains(kodeInput)) {
                tampilkanAlert(AlertType.ERROR, "Kode Salah", "Kode tidak valid atau sudah dipakai.");
                return;
            }
            tampilkanAlert(AlertType.INFORMATION, "Berhasil", "Kode valid. Silakan pilih kandidat.");
            txtKode.setDisable(true);
            btnCekKode.setDisable(true);
            txtPilihan.setDisable(false);
            btnKirim.setDisable(false);
        });

        // Event: kirim suara
        btnKirim.setOnAction(e -> {
            String teksPilihan = txtPilihan.getText().trim();
            if (teksPilihan.isEmpty()) {
                tampilkanAlert(AlertType.WARNING, "Peringatan", "Nomor kandidat wajib diisi!");
                return;
            }

            int no;
            try {
                no = Integer.parseInt(teksPilihan);
            } catch (NumberFormatException ex) {
                tampilkanAlert(AlertType.ERROR, "Format Salah", "Nomor kandidat harus berupa angka!");
                return;
            }

            Kandidat dipilih = cariKandidat(no);
            if (dipilih == null) {
                tampilkanAlert(AlertType.ERROR, "Tidak Ditemukan", "Nomor kandidat tidak ditemukan.");
                return;
            }

            // Konfirmasi pilihan
            Alert konfirmasi = new Alert(AlertType.CONFIRMATION);
            konfirmasi.setTitle("Konfirmasi");
            konfirmasi.setHeaderText(null);
            konfirmasi.setContentText("Yakin memilih " + dipilih.getNama() + "?");
            ButtonType hasil = konfirmasi.showAndWait().orElse(ButtonType.CANCEL);

            if (hasil == ButtonType.OK) {
                dipilih.tambahSuara();                       // suara anonim
                kodeAktif.remove(txtKode.getText().trim());  // kode hangus
                tampilkanAlert(AlertType.INFORMATION, "Sukses",
                        "Pemilihan berhasil dilakukan. Terima kasih!");
                tampilkanHalamanLogin();
            }
        });

        HBox barisKode = new HBox(10, txtKode, btnCekKode);
        HBox barisPilih = new HBox(10, txtPilihan, btnKirim);

        VBox langkah1 = bagian("Langkah 1: Ambil Kode", btnAmbilKode, lblKode);
        VBox langkah2 = bagian("Langkah 2: Verifikasi Kode", barisKode);
        VBox langkah3 = bagian("Langkah 3: Pilih Kandidat", areaKandidat, barisPilih);
        VBox.setVgrow(langkah3, Priority.ALWAYS);

        // Konten dibatasi lebarnya dan diletakkan di tengah
        VBox konten = new VBox(langkah1, langkah2, langkah3);
        konten.setMaxWidth(760);

        VBox pembungkus = new VBox(konten);
        pembungkus.setAlignment(Pos.TOP_CENTER);

        setHalaman(buatRoot("MENU PEMILIH  |  " + pemilih.getNama(), btnLogout, pembungkus));
    }

    // =====================================================
    // METHOD PENCARIAN
    // =====================================================
    private Pemilih cariPemilih(String npm) {
        for (Pemilih p : daftarPemilih) {
            if (p.getId().equals(npm)) {
                return p;
            }
        }
        return null;
    }

    private Kandidat cariKandidat(int nomor) {
        for (Kandidat k : daftarKandidat) {
            if (k.getNomorUrut() == nomor) {
                return k;
            }
        }
        return null;
    }

    private String buatKode() {
        String kode;
        do {
            kode = "VOTE-" + ((int) (Math.random() * 900000) + 100000);
        } while (kodeAktif.contains(kode));
        return kode;
    }

    public static void main(String[] args) {
        launch(args);
    }
}