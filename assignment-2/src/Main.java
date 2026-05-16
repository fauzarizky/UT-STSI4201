import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class Main {
  static Menu[] daftarMenu;
  static NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));
  static Scanner scanner = new Scanner(System.in);

  private static Menu[] buatDaftarMenu() {
    Menu[] menu = new Menu[8];
    menu[0] = new Menu("Nasi Goreng", 15000, "Makanan");
    menu[1] = new Menu("Mie Goreng", 14000, "Makanan");
    menu[2] = new Menu("Ayam Bakar", 25000, "Makanan");
    menu[3] = new Menu("Sate Ayam", 22000, "Makanan");
    menu[4] = new Menu("Es Teh", 8000, "Minuman");
    menu[5] = new Menu("Jus Jeruk", 12000, "Minuman");
    menu[6] = new Menu("Kopi", 10000, "Minuman");
    menu[7] = new Menu("Air Mineral", 6000, "Minuman");
    return menu;
  }

  private static void tampilkanDaftarMenu() {
    System.out.println("\n=== DAFTAR MENU ===");
    if (daftarMenu.length == 0) {
      System.out.println("(Kosong)");
      return;
    }

    System.out.println("-- Makanan --");
    int nomor = 1;
    for (Menu m : daftarMenu) {
      if (m.getKategori().equals("Makanan")) System.out.println(nomor + ". " + m.getNama() + " - Rp " + nf.format(m.getHarga()));
      nomor++;
    }

    System.out.println("-- Minuman --");
    nomor = 1;
    for (Menu m : daftarMenu) {
      if (m.getKategori().equals("Minuman")) System.out.println(nomor + ". " + m.getNama() + " - Rp " + nf.format(m.getHarga()));
      nomor++;
    }
  }

  private static Menu pilihMenu(int nomor) {
    if (nomor < 1 || nomor > daftarMenu.length) return null;
    return daftarMenu[nomor - 1];
  }

  private static boolean isMinuman(Menu menu) {
    return menu != null && "Minuman".equalsIgnoreCase(menu.getKategori());
  }

  // Minta nomor menu yang valid dari user. Return -1 jika batal. (do-while)
  private static int mintaNomorMenu() {
    int nomorValid = -1;
    do {
      System.out.print("Nomor menu (0=batal): ");
      String input = scanner.nextLine().trim();
      if (input.equals("0")) return -1;
      try {
        int n = Integer.parseInt(input);
        if (n >= 1 && n <= daftarMenu.length) nomorValid = n;
        else System.out.println("Pilih 1-" + daftarMenu.length + ".");
      } catch (NumberFormatException e) {
        System.out.println("Masukkan angka.");
      }
    } while (nomorValid == -1);
    return nomorValid;
  }

  public static void main(String[] args) {
    daftarMenu = buatDaftarMenu();

    boolean jalan = true;
    while (jalan) {
      System.out.println("\n=== MENU UTAMA ===");
      System.out.println("1. Pelanggan");
      System.out.println("2. Pemilik");
      System.out.println("0. Keluar");
      System.out.print("Pilih: ");
      String pilihan = scanner.nextLine().trim();

      switch (pilihan) {
        case "1":
          menuPelanggan();
          break;
        case "2":
          menuPemilik();
          break;
        case "0":
          jalan = false;
          break;
        default:
          System.out.println("Pilihan tidak valid.");
      }
    }

    scanner.close();
  }

  private static void menuPelanggan() {
    boolean kembali = false;
    while (!kembali) {
      System.out.println("\n=== PELANGGAN ===");
      System.out.println("1. Pesan");
      System.out.println("0. Kembali");
      System.out.print("Pilih: ");
      String pilihan = scanner.nextLine().trim();

      if (pilihan.equals("1")) lakukanPemesanan();
      else if (pilihan.equals("0")) kembali = true;
      else System.out.println("Pilihan tidak valid.");
    }
  }

  private static void lakukanPemesanan() {
    tampilkanDaftarMenu();
    if (daftarMenu.length == 0) return;

    Menu[] menuDipesan = new Menu[0];
    int[] jumlahDipesan = new int[0];

    System.out.println("Ketik nomor menu untuk memesan, atau 'selesai' untuk selesai.");

    while (true) {
      System.out.print("Nomor menu: ");
      String input = scanner.nextLine().trim();

      if (input.equalsIgnoreCase("selesai")) break;

      int nomorMenu;
      try {
        nomorMenu = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("Input tidak valid.");
        continue;
      }

      Menu menu = pilihMenu(nomorMenu);
      if (menu == null) {
        System.out.println("Nomor tidak valid. Pilih 1-" + daftarMenu.length + ".");
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

      boolean sudahAda = false;
      for (int i = 0; i < menuDipesan.length; i++) {
        if (menuDipesan[i] == menu) {
          jumlahDipesan[i] += jumlah;
          sudahAda = true;
          break;
        }
      }

      if (!sudahAda) {
        Menu[] menuBaru   = new Menu[menuDipesan.length + 1];
        int[]  jumlahBaru = new int[menuDipesan.length + 1];
        for (int i = 0; i < menuDipesan.length; i++) {
          menuBaru[i]   = menuDipesan[i];
          jumlahBaru[i] = jumlahDipesan[i];
        }
        menuBaru[menuDipesan.length]   = menu;
        jumlahBaru[menuDipesan.length] = jumlah;
        menuDipesan   = menuBaru;
        jumlahDipesan = jumlahBaru;
      }
      System.out.println(menu.getNama() + " x" + jumlah + " ditambahkan.");
    }

    if (menuDipesan.length == 0) {
      System.out.println("Tidak ada pesanan.");
      return;
    }

    cetakStruk(menuDipesan, jumlahDipesan, menuDipesan.length);
  }

  private static void cetakStruk( Menu[] menuDipesan, int[] jumlahDipesan, int totalPesanan) {
    int subTotal = 0;
    for (int i = 0; i < totalPesanan; i++) {
      subTotal += menuDipesan[i].getHarga() * jumlahDipesan[i];
    }

    int diskon = 0;
    int potonganBogo = 0;
    Menu menuBogoTerpilih = null;
    int jumlahGratisBogo = 0;

    if (subTotal > 100000) {
      // Promo 1: diskon 10%
      diskon = (int) Math.round(subTotal * 0.10);
    } else if (subTotal > 50000) {
      // Promo 2: BOGO minuman pertama
      for (int i = 0; i < totalPesanan; i++) {
        if (isMinuman(menuDipesan[i])) {
          menuBogoTerpilih = menuDipesan[i];
          jumlahGratisBogo = jumlahDipesan[i] / 2;
          if (jumlahGratisBogo > 0) potonganBogo =
            menuBogoTerpilih.getHarga() * jumlahGratisBogo;
          break;
        }
      }
    }

    int subTotalSetelahPromo = subTotal - diskon - potonganBogo;
    int pajak = (int) Math.round(subTotalSetelahPromo * 0.10);
    int layanan = 20000;
    int grandTotal = subTotalSetelahPromo + pajak + layanan;

    System.out.println("\n=== STRUK PEMBAYARAN ===");
    for (int i = 0; i < totalPesanan; i++) {
      int totalItem = menuDipesan[i].getHarga() * jumlahDipesan[i];
      System.out.println(
        menuDipesan[i].getNama() +
          " x" +
          jumlahDipesan[i] +
          " = Rp " +
          nf.format(totalItem)
      );
    }

    System.out.println("---");
    System.out.println("Subtotal      : Rp " + nf.format(subTotal));

    if (diskon > 0) System.out.println(
      "Diskon 10%    : -Rp " + nf.format(diskon)
    );

    if (potonganBogo > 0) System.out.println(
      "BOGO " +
        menuBogoTerpilih.getNama() +
        " (" +
        jumlahGratisBogo +
        " gratis): -Rp " +
        nf.format(potonganBogo)
    );

    System.out.println("Pajak 10%     : Rp " + nf.format(pajak));
    System.out.println("Biaya Layanan : Rp " + nf.format(layanan));
    System.out.println("---");
    System.out.println("TOTAL         : Rp " + nf.format(grandTotal));
  }

  private static void menuPemilik() {
    boolean kembali = false;
    while (!kembali) {
      System.out.println("\n=== PEMILIK ===");
      System.out.println("1. Lihat Menu");
      System.out.println("2. Tambah Menu");
      System.out.println("3. Ubah Harga");
      System.out.println("4. Hapus Menu");
      System.out.println("0. Kembali");
      System.out.print("Pilih: ");
      String pilihan = scanner.nextLine().trim();

      switch (pilihan) {
        case "1":
          tampilkanDaftarMenu();
          break;
        case "2":
          tambahMenu();
          break;
        case "3":
          ubahHarga();
          break;
        case "4":
          hapusMenu();
          break;
        case "0":
          kembali = true;
          break;
        default:
          System.out.println("Pilihan tidak valid.");
      }
    }
  }

  private static void tambahMenu() {
    System.out.println("\n=== TAMBAH MENU ===");
    System.out.println("(Kosongkan nama untuk berhenti)");

    while (true) {
      System.out.print("Nama: ");
      String nama = scanner.nextLine().trim();
      if (nama.isEmpty()) break;

      System.out.print("Harga (Rp): ");
      int harga;
      try {
        harga = Integer.parseInt(scanner.nextLine().trim());
      } catch (NumberFormatException e) {
        System.out.println("Harga tidak valid, ulangi.");
        continue;
      }
      if (harga <= 0) {
        System.out.println("Harga harus > 0, ulangi.");
        continue;
      }

      System.out.print("Kategori (1=Makanan, 2=Minuman): ");
      String pilKat = scanner.nextLine().trim();
      String kategori;
      if (pilKat.equals("1")) kategori = "Makanan";
      else if (pilKat.equals("2")) kategori = "Minuman";
      else {
        System.out.println("Kategori tidak valid, ulangi.");
        continue;
      }

      // Tambah ke array: buat array baru ukuran +1 (for)
      Menu[] menuBaru = new Menu[daftarMenu.length + 1];
      for (int i = 0; i < daftarMenu.length; i++) {
        menuBaru[i] = daftarMenu[i];
      }
      menuBaru[daftarMenu.length] = new Menu(nama, harga, kategori);
      daftarMenu = menuBaru;

      System.out.println(nama + " [" + kategori + "] ditambahkan.");
    }
  }

  private static void ubahHarga() {
    System.out.println("\n=== UBAH HARGA ===");
    tampilkanDaftarMenu();
    if (daftarMenu.length == 0) return;

    int nomor = mintaNomorMenu();
    if (nomor == -1) return;

    Menu menu = daftarMenu[nomor - 1];
    System.out.println(
      "Menu: " + menu.getNama() + " - Rp " + nf.format(menu.getHarga())
    );

    System.out.print("Konfirmasi ubah? (Ya/tidak): ");
    if (!scanner.nextLine().trim().equalsIgnoreCase("Ya")) {
      System.out.println("Dibatalkan.");
      return;
    }

    System.out.print("Harga baru (Rp): ");
    try {
      int hargaBaru = Integer.parseInt(scanner.nextLine().trim());
      if (hargaBaru > 0) {
        menu.setHarga(hargaBaru);
        System.out.println(
          "Harga diperbarui menjadi Rp " + nf.format(hargaBaru) + "."
        );
      } else {
        System.out.println("Harga tidak valid.");
      }
    } catch (NumberFormatException e) {
      System.out.println("Input tidak valid.");
    }
  }

  private static void hapusMenu() {
    System.out.println("\n=== HAPUS MENU ===");
    tampilkanDaftarMenu();
    if (daftarMenu.length == 0) return;

    int nomor = mintaNomorMenu();
    if (nomor == -1) return;

    Menu menu = daftarMenu[nomor - 1];
    System.out.println("Menu: " + menu.getNama());

    System.out.print("Konfirmasi hapus? (Ya/tidak): ");
    if (!scanner.nextLine().trim().equalsIgnoreCase("Ya")) {
      System.out.println("Dibatalkan.");
      return;
    }

    // Hapus dari array: buat array baru ukuran -1, skip item yang dihapus (for)
    Menu[] menuBaru = new Menu[daftarMenu.length - 1];
    int j = 0;
    for (int i = 0; i < daftarMenu.length; i++) {
      if (i != nomor - 1) {
        menuBaru[j] = daftarMenu[i];
        j++;
      }
    }
    daftarMenu = menuBaru;
    System.out.println(menu.getNama() + " dihapus.");
  }
}
