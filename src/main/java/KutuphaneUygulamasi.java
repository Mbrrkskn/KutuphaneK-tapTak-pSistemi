import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class KutuphaneUygulamasi {
    private static final String DOSYA_ADI = "Kitaplar.txt";
    private static final String LOG_ADI = "log.txt";
    private static List<Kıtap> kitapListesi = new ArrayList<>();

    public static void main(String[] args) {
        dosyayiOku();
        kitaplariListele();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1- Kitap Ekle\n2- Kitap Sil\n3- Stok Güncelle\n4- Çıkış");
            System.out.print("Seçiminiz: ");
            int secim = scanner.nextInt();
            scanner.nextLine();  // dummy read

            try {
                switch (secim) {
                    case 1 -> {
                        kitapEkle(scanner);
                        kitaplariListele();
                    }
                    case 2 -> {
                        kitapSil(scanner);
                        kitaplariListele();
                    }
                    case 3 -> {
                        stokGuncelle(scanner);
                        kitaplariListele();
                    }

                    case 4->{
                        dosyayaYaz();
                        System.out.println("Çıkılıyor...");
                        return;


                    }
                    case 5 -> kitaplariListele();
                    default -> System.out.println("Geçersiz seçim.");
                }
            } catch (InvalidStockException | IOException e) {
                System.out.println("Hata: " + e.getMessage());
                //logaYaz("HATA: " + e.getMessage());
            }
        }
    }

    private static void dosyayiOku() {
        File file = new File(DOSYA_ADI);
        if (!file.exists()) {
            try {
                file.createNewFile();
                //logaYaz("Yeni kitaplar.txt oluşturuldu.");
            } catch (IOException e) {
                System.out.println("Dosya oluşturulamadı: " + e.getMessage());
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DOSYA_ADI))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                String[] parcalar = satir.split(",");
                if (parcalar.length == 3) {
                    String ad = parcalar[0];
                    String yazar = parcalar[1];
                    int stok = Integer.parseInt(parcalar[2]);
                    kitapListesi.add(new Kıtap(ad, yazar, stok));
                }
            }
            //logaYaz("Kitap verileri okundu.");
        } catch (IOException e) {
            System.out.println("Dosya okunamadı.");
            //logaYaz("HATA: Dosya okunamadı: " + e.getMessage());
        }
    }

    private static void dosyayaYaz() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_ADI))) {
            for (Kıtap k : kitapListesi) {
                writer.write(k.toString());
                writer.newLine();
            }
        }
        logaYaz("Kitap verileri dosyaya yazıldı.");
    }

    private static void kitapEkle(Scanner scanner) throws IOException {
        System.out.print("Kitap adı: ");
        String ad = scanner.nextLine();
        System.out.print("Yazar: ");
        String yazar = scanner.nextLine();
        System.out.print("Stok: ");
        int stok = scanner.nextInt();
        scanner.nextLine();

        kitapListesi.add(new Kıtap(ad, yazar, stok));
        dosyayaYaz();
        //logaYaz("Kitap eklendi: " + ad);
    }

    private static void kitapSil(Scanner scanner) throws IOException {
        System.out.print("Silinecek kitabın adı: ");
        String ad = scanner.nextLine();

        boolean bulundu = kitapListesi.removeIf(k -> k.getAd().equalsIgnoreCase(ad));
        if (bulundu) {
            dosyayaYaz();
            logaYaz("Kitap silindi: " + ad);
        } else {
            System.out.println("Kitap bulunamadı.");
            logaYaz("HATA: Silinmek istenen kitap bulunamadı: " + ad);
        }
    }

    private static void stokGuncelle(Scanner scanner) throws InvalidStockException, IOException {
        System.out.print("Kitap adı: ");
        String ad = scanner.nextLine();
        Kıtap kitap = null;

        for (Kıtap k : kitapListesi) {
            if (k.getAd().equalsIgnoreCase(ad)) {
                kitap = k;
                break;
            }
        }

        if (kitap == null) {
            System.out.println("Kitap bulunamadı.");
            logaYaz("HATA: Stok güncelleme için kitap bulunamadı: " + ad);
            return;
        }

        System.out.print("Yeni stok: ");
        int yeniStok = scanner.nextInt();
        scanner.nextLine();

        if (yeniStok < 0) {
            throw new InvalidStockException("Stok negatif olamaz!");
        }

        kitap.setStok(yeniStok);
        dosyayaYaz();
        logaYaz("Stok güncellendi: " + ad + ", yeni stok: " + yeniStok);
    }

    private static void kitaplariListele() {
        if (kitapListesi.isEmpty()) {
            System.out.println("Kütüphanede kayıtlı kitap yok.");
        } else {
            System.out.println("\nKütüphanedeki Kitaplar:");
            for (Kıtap k : kitapListesi) {
                System.out.println("- " + k.getAd() + " | Yazar: " + k.getYazar() + " | Stok: " + k.getStok());
            }
        }
    }

    private static void logaYaz(String mesaj) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_ADI, true))) {
            String zaman = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write("[" + zaman + "] " + mesaj);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Log yazılamadı: " + e.getMessage());
        }
    }
}


/*import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class KutuphaneUygulamasi {
    private static final String DOSYA_ADI = "kitaplar.txt";
    private static final String LOG_ADI = "log.txt";
    private static List<Kıtap> kitapListesi = new ArrayList<>();

    public static void main(String[] args) {
        dosyayiOku();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n1- Kitap Ekle\n2- Kitap Sil\n3- Stok Güncelle\n4- Çıkış");
            System.out.print("Seçiminiz: ");
            int secim = scanner.nextInt();
            scanner.nextLine();  // dummy read

            try {
                switch (secim) {
                    case 1 -> kitapEkle(scanner);
                    case 2 -> kitapSil(scanner);
                    case 3 -> stokGuncelle(scanner);
                    case 4 -> {
                        dosyayaYaz();
                        return;
                    }
                    default -> System.out.println("Geçersiz seçim.");
                }
            } catch (InvalidStockException | IOException e) {
                System.out.println("Hata: " + e.getMessage());
                logaYaz("HATA: " + e.getMessage());
            }
        }
    }

    private static void dosyayiOku() {
        File file = new File(DOSYA_ADI);
        if (!file.exists()) {
            try {
                file.createNewFile();
                logaYaz("Yeni kitaplar.txt oluşturuldu.");
            } catch (IOException e) {
                System.out.println("Dosya oluşturulamadı: " + e.getMessage());
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DOSYA_ADI))) {
            String satir;
            while ((satir = reader.readLine()) != null) {
                String[] parcalar = satir.split(",");
                if (parcalar.length == 3) {
                    String ad = parcalar[0];
                    String yazar = parcalar[1];
                    int stok = Integer.parseInt(parcalar[2]);
                    kitapListesi.add(new Kıtap(ad, yazar, stok));
                }
            }
            logaYaz("Kitap verileri okundu.");
        } catch (IOException e) {
            System.out.println("Dosya okunamadı.");
            logaYaz("HATA: Dosya okunamadı: " + e.getMessage());
        }
    }

    private static void dosyayaYaz() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOSYA_ADI))) {
            for (Kıtap k : kitapListesi) {
                writer.write(k.toString());
                writer.newLine();
            }
        }
        logaYaz("Kitap verileri dosyaya yazıldı.");
    }

    private static void kitapEkle(Scanner scanner) throws IOException {
        System.out.print("Kitap adı: ");
        String ad = scanner.nextLine();
        System.out.print("Yazar: ");
        String yazar = scanner.nextLine();
        System.out.print("Stok: ");
        int stok = scanner.nextInt();
        scanner.nextLine();

        kitapListesi.add(new Kıtap(ad, yazar, stok));
        dosyayaYaz();
        logaYaz("Kitap eklendi: " + ad);
    }

    private static void kitapSil(Scanner scanner) throws IOException {
        System.out.print("Silinecek kitabın adı: ");
        String ad = scanner.nextLine();

        boolean bulundu = kitapListesi.removeIf(k -> k.getAd().equalsIgnoreCase(ad));
        if (bulundu) {
            dosyayaYaz();
            logaYaz("Kitap silindi: " + ad);
        } else {
            System.out.println("Kitap bulunamadı.");
            logaYaz("HATA: Silinmek istenen kitap bulunamadı: " + ad);
        }
    }

    private static void stokGuncelle(Scanner scanner) throws InvalidStockException, IOException {
        System.out.print("Kitap adı: ");
        String ad = scanner.nextLine();
        Kıtap kitap = null;

        for (Kıtap k : kitapListesi) {
            if (k.getAd().equalsIgnoreCase(ad)) {
                kitap = k;
                break;
            }
        }

        if (kitap == null) {
            System.out.println("Kitap bulunamadı.");
            logaYaz("HATA: Stok güncelleme için kitap bulunamadı: " + ad);
            return;
        }

        System.out.print("Yeni stok: ");
        int yeniStok = scanner.nextInt();
        scanner.nextLine();

        if (yeniStok < 0) {
            throw new InvalidStockException("Stok negatif olamaz!");
        }

        kitap.setStok(yeniStok);
        dosyayaYaz();
        logaYaz("Stok güncellendi: " + ad + ", yeni stok: " + yeniStok);
    }

    private static void logaYaz(String mesaj) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_ADI, true))) {
            String zaman = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write("[" + zaman + "] " + mesaj);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Log yazılamadı: " + e.getMessage());
        }
    }
}
*/