public class Makanan extends MenuItem {
    private String jenisMakanan;

    public Makanan(String nama, double harga, String jenisMakanan) {
        super(nama, harga, "Makanan");
        this.jenisMakanan = jenisMakanan;
    }

    public String getJenisMakanan()              { return jenisMakanan; }
    public void setJenisMakanan(String jenis)    { this.jenisMakanan = jenis; }

    @Override
    public String tampilMenu() {
        return getNama() + " [" + jenisMakanan + "] - Rp " + nf.format(Math.round(getHarga()));
    }

    @Override
    public String toFileString() {
        return "MAKANAN|" + getNama() + "|" + getHarga() + "|Makanan|" + jenisMakanan;
    }
}
