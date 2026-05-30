import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    static Menu           menu    = new Menu();
    static NumberFormat   nf      = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));
    static Scanner        scanner = new Scanner(System.in);
    static final String   FILE_MENU  = "data/menu.txt";
    static final String   DIR_STRUK  = "data/struk/";

    private static void inisialisasiMenu() {
        menu.tambahItem(new Makanan("Nasi Goreng", 15000, "Nasi"));
        menu.tambahItem(new Makanan("Mie Goreng",  14000, "Mie"));
        menu.tambahItem(new Makanan("Ayam Bakar",  25000, "Ayam"));
        menu.tambahItem(new Makanan("Sate Ayam",   22000, "Ayam"));
        menu.tambahItem(new Minuman("Es Teh",      8000,  "Dingin"));
        menu.tambahItem(new Minuman("Jus Jeruk",   12000, "Dingin"));
        menu.tambahItem(new Minuman("Kopi",        10000, "Panas"));
        menu.tambahItem(new Minuman("Air Mineral",  6000, "Dingin"));
        menu.tambahItem(new Diskon("Promo Akhir Pekan", 0.15));
    }

    public static void main(String[] args) {
        new File("data/struk").mkdirs();

        File fileMenu = new File(FILE_MENU);
        if (fileMenu.exists()) {
            try {
                menu.muatDariFile(FILE_MENU);
                System.out.println("Menu dimuat dari " + FILE_MENU + ".");
            } catch (IOException e) {
                System.out.println("Gagal memuat menu: " + e.getMessage() + ". Menggunakan menu default.");
                inisialisasiMenu();
            }
        } else {
            inisialisasiMenu();
            try {
                menu.simpanKeFile(FILE_MENU);
            } catch (IOException e) {
                System.out.println("Peringatan: gagal menyimpan menu awal: " + e.getMessage());
            }
        }

        boolean jalan = true;
        while (jalan) {
            System.out.println("\n=== MENU UTAMA ===");
            System.out.println("1. Pelanggan");
            System.out.println("2. Pemilik");
            System.out.println("0. Keluar");
            System.out.print("Pilih: ");
            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1": menuPelanggan(); break;
                case "2": menuPemilik();   break;
                case "0": jalan = false;   break;
                default:  System.out.println("Pilihan tidak valid.");
            }
        }

        scanner.close();
        System.out.println("Terima kasih!");
    }

    // =========================================================
    //  TAMPIL MENU
    // =========================================================

    private static void tampilkanDaftarMenu() {
        System.out.println("\n=== DAFTAR MENU ===");
        if (menu.jumlahItem() == 0) {
            System.out.println("(Kosong)");
            return;
        }

        boolean[] tampil = { false, false, false };

        System.out.println("-- Makanan --");
        for (int i = 0; i < menu.jumlahItem(); i++) {
            MenuItem item = menu.getItem(i);
            if (item instanceof Makanan) {
                System.out.println((i + 1) + ". " + item.tampilMenu());
                tampil[0] = true;
            }
        }
        if (!tampil[0]) System.out.println("(Tidak ada)");

        System.out.println("-- Minuman --");
        for (int i = 0; i < menu.jumlahItem(); i++) {
            MenuItem item = menu.getItem(i);
            if (item instanceof Minuman) {
                System.out.println((i + 1) + ". " + item.tampilMenu());
                tampil[1] = true;
            }
        }
        if (!tampil[1]) System.out.println("(Tidak ada)");

        System.out.println("-- Diskon/Promo --");
        for (int i = 0; i < menu.jumlahItem(); i++) {
            MenuItem item = menu.getItem(i);
            if (item instanceof Diskon) {
                System.out.println((i + 1) + ". " + item.tampilMenu());
                tampil[2] = true;
            }
        }
        if (!tampil[2]) System.out.println("(Tidak ada)");
    }

    private static int mintaNomorMenu() {
        int nomorValid = -1;
        do {
            System.out.print("Nomor menu (0=batal): ");
            String input = scanner.nextLine().trim();
            if (input.equals("0")) return -1;
            try {
                int n = Integer.parseInt(input);
                if (n >= 1 && n <= menu.jumlahItem()) nomorValid = n;
                else System.out.println("Pilih 1-" + menu.jumlahItem() + ".");
            } catch (NumberFormatException e) {
                System.out.println("Masukkan angka.");
            }
        } while (nomorValid == -1);
        return nomorValid;
    }

    // =========================================================
    //  PELANGGAN
    // =========================================================

    private static void menuPelanggan() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n=== PELANGGAN ===");
            System.out.println("1. Pesan");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");
            String pilihan = scanner.nextLine().trim();

            if (pilihan.equals("1"))      lakukanPemesanan();
            else if (pilihan.equals("0")) kembali = true;
            else                          System.out.println("Pilihan tidak valid.");
        }
    }

    private static void lakukanPemesanan() {
        tampilkanDaftarMenu();
        if (menu.jumlahItem() == 0) return;

        Pesanan pesanan = new Pesanan();

        System.out.println("\nKetik nomor menu untuk memesan, atau 'selesai' untuk selesai.");

        while (true) {
            System.out.print("Nomor menu: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("selesai")) break;

            int nomorMenu;
            try {
                nomorMenu = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid, masukkan angka.");
                continue;
            }

            MenuItem item;
            try {
                item = menu.getItem(nomorMenu - 1);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Item tidak ditemukan: " + e.getMessage());
                continue;
            }

            if (item instanceof Diskon) {
                pesanan.tambahItem(item, 1);
                System.out.println("Promo \"" + item.getNama() + "\" diterapkan ke pesanan.");
                continue;
            }

            System.out.print("Jumlah: ");
            int jumlah;
            try {
                jumlah = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Jumlah tidak valid.");
                continue;
            }
            if (jumlah <= 0) {
                System.out.println("Jumlah harus > 0.");
                continue;
            }

            pesanan.tambahItem(item, jumlah);
            System.out.println(item.getNama() + " x" + jumlah + " ditambahkan.");
        }

        if (pesanan.isEmpty()) {
            System.out.println("Tidak ada pesanan.");
            return;
        }

        String struk = pesanan.cetakStruk();
        System.out.print(struk);

        System.out.print("Simpan struk ke file? (ya/tidak): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("ya")) {
            String waktu    = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String namaFile = DIR_STRUK + "struk_" + waktu + ".txt";
            try {
                pesanan.simpanStrukKeFile(namaFile);
                System.out.println("Struk disimpan ke " + namaFile + ".");
            } catch (IOException e) {
                System.out.println("Gagal menyimpan struk: " + e.getMessage());
            }
        }
    }

    // =========================================================
    //  PEMILIK
    // =========================================================

    private static void menuPemilik() {
        boolean kembali = false;
        while (!kembali) {
            System.out.println("\n=== PEMILIK ===");
            System.out.println("1. Lihat Menu");
            System.out.println("2. Tambah Item");
            System.out.println("3. Ubah Harga / Diskon");
            System.out.println("4. Hapus Item");
            System.out.println("5. Simpan Menu ke File");
            System.out.println("6. Muat Menu dari File");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");
            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1": tampilkanDaftarMenu(); break;
                case "2": tambahItem();          break;
                case "3": ubahHarga();           break;
                case "4": hapusItem();           break;
                case "5": simpanMenu();          break;
                case "6": muatMenu();            break;
                case "0": kembali = true;        break;
                default:  System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private static void tambahItem() {
        System.out.println("\n=== TAMBAH ITEM ===");
        System.out.println("Jenis: 1=Makanan  2=Minuman  3=Diskon/Promo");
        System.out.print("Pilih jenis (0=batal): ");
        String pilJenis = scanner.nextLine().trim();

        switch (pilJenis) {
            case "0": return;
            case "1": tambahMakanan(); break;
            case "2": tambahMinuman(); break;
            case "3": tambahDiskon();  break;
            default:  System.out.println("Pilihan tidak valid.");
        }
    }

    private static void tambahMakanan() {
        System.out.print("Nama: ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) { System.out.println("Nama tidak boleh kosong."); return; }

        double harga = inputHarga();
        if (harga < 0) return;

        System.out.print("Jenis Makanan (mis. Nasi, Mie, Ayam): ");
        String jenis = scanner.nextLine().trim();
        if (jenis.isEmpty()) jenis = "Umum";

        menu.tambahItem(new Makanan(nama, harga, jenis));
        System.out.println(nama + " [Makanan/" + jenis + "] ditambahkan.");
    }

    private static void tambahMinuman() {
        System.out.print("Nama: ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) { System.out.println("Nama tidak boleh kosong."); return; }

        double harga = inputHarga();
        if (harga < 0) return;

        System.out.print("Jenis Minuman (mis. Panas, Dingin, Jus): ");
        String jenis = scanner.nextLine().trim();
        if (jenis.isEmpty()) jenis = "Umum";

        menu.tambahItem(new Minuman(nama, harga, jenis));
        System.out.println(nama + " [Minuman/" + jenis + "] ditambahkan.");
    }

    private static void tambahDiskon() {
        System.out.print("Nama Promo: ");
        String nama = scanner.nextLine().trim();
        if (nama.isEmpty()) { System.out.println("Nama tidak boleh kosong."); return; }

        System.out.print("Persentase Diskon (mis. 10 untuk 10%): ");
        double persen;
        try {
            persen = Double.parseDouble(scanner.nextLine().trim());
            if (persen <= 0 || persen > 100) {
                System.out.println("Persentase harus antara 1-100.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input tidak valid.");
            return;
        }

        menu.tambahItem(new Diskon(nama, persen / 100.0));
        System.out.println("Promo \"" + nama + "\" diskon " + (int) persen + "% ditambahkan.");
    }

    private static double inputHarga() {
        System.out.print("Harga (Rp): ");
        try {
            double harga = Double.parseDouble(scanner.nextLine().trim());
            if (harga <= 0) { System.out.println("Harga harus > 0."); return -1; }
            return harga;
        } catch (NumberFormatException e) {
            System.out.println("Harga tidak valid.");
            return -1;
        }
    }

    private static void ubahHarga() {
        System.out.println("\n=== UBAH HARGA / DISKON ===");
        tampilkanDaftarMenu();
        if (menu.jumlahItem() == 0) return;

        int nomor = mintaNomorMenu();
        if (nomor == -1) return;

        MenuItem item;
        try {
            item = menu.getItem(nomor - 1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Item tidak ditemukan: " + e.getMessage());
            return;
        }

        if (item instanceof Diskon) {
            Diskon d = (Diskon) item;
            System.out.println("Promo: " + d.getNama() + " - " + (int) Math.round(d.getPersentaseDiskon() * 100) + "%");
            System.out.print("Persentase baru (1-100, 0=batal): ");
            try {
                double persen = Double.parseDouble(scanner.nextLine().trim());
                if (persen == 0) return;
                if (persen < 1 || persen > 100) { System.out.println("Tidak valid."); return; }
                d.setPersentaseDiskon(persen / 100.0);
                System.out.println("Diskon diperbarui menjadi " + (int) persen + "%.");
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid.");
            }
        } else {
            System.out.println("Item: " + item.getNama() + " - Rp " + nf.format(Math.round(item.getHarga())));
            System.out.print("Konfirmasi ubah harga? (ya/tidak): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("ya")) {
                System.out.println("Dibatalkan.");
                return;
            }
            double hargaBaru = inputHarga();
            if (hargaBaru < 0) return;
            item.setHarga(hargaBaru);
            System.out.println("Harga diperbarui menjadi Rp " + nf.format(Math.round(hargaBaru)) + ".");
        }
    }

    private static void hapusItem() {
        System.out.println("\n=== HAPUS ITEM ===");
        tampilkanDaftarMenu();
        if (menu.jumlahItem() == 0) return;

        int nomor = mintaNomorMenu();
        if (nomor == -1) return;

        MenuItem item;
        try {
            item = menu.getItem(nomor - 1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Item tidak ditemukan: " + e.getMessage());
            return;
        }

        System.out.println("Item: " + item.tampilMenu());
        System.out.print("Konfirmasi hapus? (ya/tidak): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("ya")) {
            System.out.println("Dibatalkan.");
            return;
        }

        try {
            menu.hapusItem(nomor - 1);
            System.out.println(item.getNama() + " dihapus.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Gagal menghapus: " + e.getMessage());
        }
    }

    private static void simpanMenu() {
        try {
            menu.simpanKeFile(FILE_MENU);
            System.out.println("Menu disimpan ke " + FILE_MENU + ".");
        } catch (IOException e) {
            System.out.println("Gagal menyimpan menu: " + e.getMessage());
        }
    }

    private static void muatMenu() {
        if (!new File(FILE_MENU).exists()) {
            System.out.println("File " + FILE_MENU + " tidak ditemukan.");
            return;
        }
        try {
            menu.muatDariFile(FILE_MENU);
            System.out.println("Menu dimuat dari " + FILE_MENU + ". Total " + menu.jumlahItem() + " item.");
        } catch (IOException e) {
            System.out.println("Gagal memuat menu: " + e.getMessage());
        }
    }
}
