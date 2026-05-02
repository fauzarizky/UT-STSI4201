import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static Menu[] buatDaftarMenu() {
        Menu[] daftarMenu = new Menu[8];

        daftarMenu[0] = new Menu("Nasi Goreng", 15000, "Makanan");
        daftarMenu[1] = new Menu("Mie Goreng", 14000, "Makanan");
        daftarMenu[2] = new Menu("Ayam Bakar", 25000, "Makanan");
        daftarMenu[3] = new Menu("Sate Ayam", 22000, "Makanan");

        daftarMenu[4] = new Menu("Es Teh", 8000, "Minuman");
        daftarMenu[5] = new Menu("Jus Jeruk", 12000, "Minuman");
        daftarMenu[6] = new Menu("Kopi", 10000, "Minuman");
        daftarMenu[7] = new Menu("Air Mineral", 6000, "Minuman");

        return daftarMenu;
    }

    private static void tampilkanDaftarMenu(Menu[] daftarMenu, NumberFormat nf) {
        System.out.println("== MENU MAKANAN ==");
        System.out.println("1. " + daftarMenu[0].getNama() + " - Rp " + nf.format(daftarMenu[0].getHarga()));
        System.out.println("2. " + daftarMenu[1].getNama() + " - Rp " + nf.format(daftarMenu[1].getHarga()));
        System.out.println("3. " + daftarMenu[2].getNama() + " - Rp " + nf.format(daftarMenu[2].getHarga()));
        System.out.println("4. " + daftarMenu[3].getNama() + " - Rp " + nf.format(daftarMenu[3].getHarga()));

        System.out.println("\n== MENU MINUMAN ==");
        System.out.println("5. " + daftarMenu[4].getNama() + " - Rp " + nf.format(daftarMenu[4].getHarga()));
        System.out.println("6. " + daftarMenu[5].getNama() + " - Rp " + nf.format(daftarMenu[5].getHarga()));
        System.out.println("7. " + daftarMenu[6].getNama() + " - Rp " + nf.format(daftarMenu[6].getHarga()));
        System.out.println("8. " + daftarMenu[7].getNama() + " - Rp " + nf.format(daftarMenu[7].getHarga()));
    }

    private static Menu pilihMenu(Menu[] daftarMenu, int nomorMenu) {
        if (nomorMenu < 1 || nomorMenu > daftarMenu.length) {
            return null;
        }
        return daftarMenu[nomorMenu - 1];
    }

    private static int hitungTotalItem(Menu menu, int jumlah) {
        if (menu == null || jumlah <= 0) {
            return 0;
        }
        return menu.getHarga() * jumlah;
    }

    private static boolean isMinuman(Menu menu) {
        if (menu == null) {
            return false;
        }
        return "Minuman".equalsIgnoreCase(menu.getKategori());
    }

    private static void cetakBarisStruk(int nomor, Menu menu, int jumlah, NumberFormat nf) {
        if (menu == null || jumlah <= 0) {
            return;
        }

        int hargaSatuan = menu.getHarga();
        int totalHarga = hargaSatuan * jumlah;
        System.out.println(
                nomor + ". " + menu.getNama() + " (" + menu.getKategori() + ") x " + jumlah + " @ Rp "
                        + nf.format(hargaSatuan) + " = Rp " + nf.format(totalHarga));
    }

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));

        Menu[] daftarMenu = buatDaftarMenu();
        tampilkanDaftarMenu(daftarMenu, nf);

        System.out.println("\n=== PEMESANAN (maks 4 menu) ===");
        System.out.println("Masukkan nomor menu (1-8). Ketik 0 untuk selesai.");

        System.out.print("Pesanan 1 - nomor menu: ");
        int nomor1 = scanner.nextInt();
        if (nomor1 == 0) {
            System.out.println("Tidak ada pesanan.");
            scanner.close();
            return;
        }
        Menu menu1 = pilihMenu(daftarMenu, nomor1);
        System.out.print("Pesanan 1 - jumlah: ");
        int jumlah1 = scanner.nextInt();
        if (menu1 == null || jumlah1 < 0) {
            jumlah1 = 0;
        }

        System.out.print("Pesanan 2 - nomor menu (0 jika selesai): ");
        int nomor2 = scanner.nextInt();
        Menu menu2 = null;
        int jumlah2 = 0;
        if (nomor2 != 0) {
            menu2 = pilihMenu(daftarMenu, nomor2);
            System.out.print("Pesanan 2 - jumlah: ");
            jumlah2 = scanner.nextInt();
            if (menu2 == null || jumlah2 < 0) {
                jumlah2 = 0;
            }
        }

        int nomor3 = 0;
        Menu menu3 = null;
        int jumlah3 = 0;
        if (nomor2 != 0) {
            System.out.print("Pesanan 3 - nomor menu (0 jika selesai): ");
            nomor3 = scanner.nextInt();
            if (nomor3 != 0) {
                menu3 = pilihMenu(daftarMenu, nomor3);
                System.out.print("Pesanan 3 - jumlah: ");
                jumlah3 = scanner.nextInt();
                if (menu3 == null || jumlah3 < 0) {
                    jumlah3 = 0;
                }
            }
        }

        Menu menu4 = null;
        int jumlah4 = 0;
        if (nomor3 != 0) {
            System.out.print("Pesanan 4 - nomor menu (0 jika selesai): ");
            int nomor4 = scanner.nextInt();
            if (nomor4 != 0) {
                menu4 = pilihMenu(daftarMenu, nomor4);
                System.out.print("Pesanan 4 - jumlah: ");
                jumlah4 = scanner.nextInt();
                if (menu4 == null || jumlah4 < 0) {
                    jumlah4 = 0;
                }
            }
        }

        int totalItem1 = hitungTotalItem(menu1, jumlah1);
        int totalItem2 = hitungTotalItem(menu2, jumlah2);
        int totalItem3 = hitungTotalItem(menu3, jumlah3);
        int totalItem4 = hitungTotalItem(menu4, jumlah4);
        int subTotal = totalItem1 + totalItem2 + totalItem3 + totalItem4; 

        int diskon = 0;
        int potonganBogo = 0;
        Menu menuBogo = null;
        int jumlahMinumanBogo = 0;
        int jumlahGratisBogo = 0;

        if (subTotal > 100000) {
            diskon = (int) Math.round(subTotal * 0.10);
        } else if (subTotal > 50000) {
            if (isMinuman(menu1)) {
                menuBogo = menu1;
                jumlahMinumanBogo = jumlah1;
            } else if (isMinuman(menu2)) {
                menuBogo = menu2;
                jumlahMinumanBogo = jumlah2;
            } else if (isMinuman(menu3)) {
                menuBogo = menu3;
                jumlahMinumanBogo = jumlah3;
            } else if (isMinuman(menu4)) {
                menuBogo = menu4;
                jumlahMinumanBogo = jumlah4;
            }

            if (menuBogo != null) {
                jumlahGratisBogo = jumlahMinumanBogo / 2;
                potonganBogo = menuBogo.getHarga() * jumlahGratisBogo;
            }
        }

        int subTotalSetelahPromo = subTotal - diskon - potonganBogo;
        int pajak = (int) Math.round(subTotalSetelahPromo * 0.10);
        int biayaLayanan = 20000;
        int totalAkhir = subTotalSetelahPromo + pajak + biayaLayanan;

        System.out.println("\n=== STRUK PEMBAYARAN ===");
        cetakBarisStruk(1, menu1, jumlah1, nf);
        cetakBarisStruk(2, menu2, jumlah2, nf);
        cetakBarisStruk(3, menu3, jumlah3, nf);
        cetakBarisStruk(4, menu4, jumlah4, nf);

        System.out.println("--------------------------------");
        System.out.println("Subtotal (sebelum promo): Rp " + nf.format(subTotal));

        if (diskon > 0) {
            System.out.println("Diskon 10%: -Rp " + nf.format(diskon));
        }

        if (potonganBogo > 0) {
            System.out.println(
                    "Promo BOGO minuman: " + menuBogo.getNama() + " (gratis " + jumlahGratisBogo + ") -Rp "
                            + nf.format(potonganBogo));
        }

        System.out.println("Subtotal (setelah promo): Rp " + nf.format(subTotalSetelahPromo));
        System.out.println("Pajak (10%): Rp " + nf.format(pajak));
        System.out.println("Biaya layanan: Rp " + nf.format(biayaLayanan));
        System.out.println("================================");
        System.out.println("TOTAL BAYAR: Rp " + nf.format(totalAkhir));

        scanner.close();
    }
}
