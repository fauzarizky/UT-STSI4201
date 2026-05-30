import java.text.NumberFormat;
import java.util.Locale;

public abstract class MenuItem {
    protected static final NumberFormat nf = NumberFormat.getInstance(Locale.forLanguageTag("id-ID"));

    private String nama;
    private double harga;
    private String kategori;

    public MenuItem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama()      { return nama; }
    public double getHarga()     { return harga; }
    public String getKategori()  { return kategori; }

    public void setNama(String nama)         { this.nama = nama; }
    public void setHarga(double harga)       { this.harga = harga; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    public abstract String tampilMenu();

    public abstract String toFileString();
}
