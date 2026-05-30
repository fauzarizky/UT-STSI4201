import java.util.ArrayList;
import java.io.*;

public class Menu {
    private ArrayList<MenuItem> items;

    public Menu() {
        this.items = new ArrayList<>();
    }

    public void tambahItem(MenuItem item) {
        items.add(item);
    }

    public void hapusItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Item pada nomor " + (index + 1) + " tidak ditemukan.");
        }
        items.remove(index);
    }

    public MenuItem getItem(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Item pada nomor " + (index + 1) + " tidak ditemukan.");
        }
        return items.get(index);
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    public int jumlahItem() {
        return items.size();
    }

    public void simpanKeFile(String namaFile) throws IOException {
        new File(namaFile).getParentFile().mkdirs();
        BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile));
        for (MenuItem item : items) {
            writer.write(item.toFileString());
            writer.newLine();
        }
        writer.close();
    }

    public void muatDariFile(String namaFile) throws IOException {
        items.clear();
        BufferedReader reader = new BufferedReader(new FileReader(namaFile));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\|");
            if (parts.length < 5) continue;
            String tipe  = parts[0];
            String nama  = parts[1];
            double harga = Double.parseDouble(parts[2]);
            String extra = parts[4];
            switch (tipe) {
                case "MAKANAN":
                    items.add(new Makanan(nama, harga, extra));
                    break;
                case "MINUMAN":
                    items.add(new Minuman(nama, harga, extra));
                    break;
                case "DISKON":
                    items.add(new Diskon(nama, Double.parseDouble(extra)));
                    break;
            }
        }
        reader.close();
    }
}
