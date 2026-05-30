public class Minuman extends MenuItem {
    private String jenisMinuman;

    public Minuman(String nama, double harga, String jenisMinuman) {
        super(nama, harga, "Minuman");
        this.jenisMinuman = jenisMinuman;
    }

    public String getJenisMinuman()             { return jenisMinuman; }
    public void setJenisMinuman(String jenis)   { this.jenisMinuman = jenis; }

    @Override
    public String tampilMenu() {
        return getNama() + " [" + jenisMinuman + "] - Rp " + nf.format(Math.round(getHarga()));
    }

    @Override
    public String toFileString() {
        return "MINUMAN|" + getNama() + "|" + getHarga() + "|Minuman|" + jenisMinuman;
    }
}
