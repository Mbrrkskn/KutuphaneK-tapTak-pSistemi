public class Kıtap {
    private String ad;
    private String yazar;
    private int stok;

    public Kıtap(String ad, String yazar, int stok) {
        this.ad = ad;
        this.yazar = yazar;
        this.stok = stok;

    }

    public String getAd() {
        return ad;
    }

    public String getYazar() {
        return yazar;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    @Override
    public String toString() {
        return ad + "," + yazar + "," + stok;
    }
}
