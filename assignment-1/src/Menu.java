public class Menu {
    private String nama;
    private Integer harga;
    private String kategori;

    public Menu(String nama, Integer harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public String getNama() {
        return nama;
    }

    public Integer getHarga(){
        return harga;
    }

    public String getKategori(){
        return kategori;
    }
}
