public class Diskon extends MenuItem {
    private double persentaseDiskon;

    public Diskon(String nama, double persentaseDiskon) {
        super(nama, 0, "Diskon");
        this.persentaseDiskon = persentaseDiskon;
    }

    public double getPersentaseDiskon()              { return persentaseDiskon; }
    public void setPersentaseDiskon(double persen)   { this.persentaseDiskon = persen; }

    @Override
    public String tampilMenu() {
        return getNama() + " - Diskon " + (int) Math.round(persentaseDiskon * 100) + "%";
    }

    @Override
    public String toFileString() {
        return "DISKON|" + getNama() + "|0.0|Diskon|" + persentaseDiskon;
    }
}
