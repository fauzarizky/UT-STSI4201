import java.util.ArrayList;
import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;

public class Pesanan {
    private ArrayList<MenuItem> itemPesanan;
    private ArrayList<Integer>  jumlahPesanan;
    private static final NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));

    public Pesanan() {
        this.itemPesanan  = new ArrayList<>();
        this.jumlahPesanan = new ArrayList<>();
    }

    public void tambahItem(MenuItem item, int jumlah) {
        for (int i = 0; i < itemPesanan.size(); i++) {
            if (itemPesanan.get(i) == item) {
                jumlahPesanan.set(i, jumlahPesanan.get(i) + jumlah);
                return;
            }
        }
        itemPesanan.add(item);
        jumlahPesanan.add(jumlah);
    }

    public boolean isEmpty() {
        return itemPesanan.isEmpty();
    }

    private double hitungSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < itemPesanan.size(); i++) {
            MenuItem item = itemPesanan.get(i);
            if (!(item instanceof Diskon)) {
                subtotal += item.getHarga() * jumlahPesanan.get(i);
            }
        }
        return subtotal;
    }

    private double hitungTotalDiskon(double subtotal) {
        double totalDiskon = 0;
        for (MenuItem item : itemPesanan) {
            if (item instanceof Diskon) {
                totalDiskon += subtotal * ((Diskon) item).getPersentaseDiskon();
            }
        }
        return totalDiskon;
    }

    public String cetakStruk() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== STRUK PEMBAYARAN ===\n");

        for (int i = 0; i < itemPesanan.size(); i++) {
            MenuItem item = itemPesanan.get(i);
            if (item instanceof Diskon) {
                sb.append("[Promo] ").append(item.tampilMenu()).append("\n");
            } else {
                long totalItem = Math.round(item.getHarga() * jumlahPesanan.get(i));
                sb.append(item.getNama())
                  .append(" x").append(jumlahPesanan.get(i))
                  .append(" = Rp ").append(nf.format(totalItem))
                  .append("\n");
            }
        }

        sb.append("---\n");

        double subtotal     = hitungSubtotal();
        double totalDiskon  = hitungTotalDiskon(subtotal);
        double setelahDiskon = subtotal - totalDiskon;
        double pajak        = setelahDiskon * 0.10;
        double biayaLayanan = 20000;
        double grandTotal   = setelahDiskon + pajak + biayaLayanan;

        sb.append("Subtotal      : Rp ").append(nf.format(Math.round(subtotal))).append("\n");
        if (totalDiskon > 0) {
            sb.append("Diskon        : -Rp ").append(nf.format(Math.round(totalDiskon))).append("\n");
        }
        sb.append("Pajak 10%     : Rp ").append(nf.format(Math.round(pajak))).append("\n");
        sb.append("Biaya Layanan : Rp ").append(nf.format(Math.round(biayaLayanan))).append("\n");
        sb.append("---\n");
        sb.append("TOTAL         : Rp ").append(nf.format(Math.round(grandTotal))).append("\n");

        return sb.toString();
    }

    public void simpanStrukKeFile(String namaFile) throws IOException {
        new File(namaFile).getParentFile().mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile));
        writer.write(cetakStruk());
        writer.close();
    }
}
