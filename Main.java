import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Main {

    private final int b;
    private final int m;
    private final double alphaMM;
    private final byte[] registers;

    public Main(int b) {
        this.b = b;
        this.m = 1 << b;
        this.registers = new byte[m];

        if (m == 16) alphaMM = 0.673;
        else if (m == 32) alphaMM = 0.697;
        else if (m == 64) alphaMM = 0.709;
        else alphaMM = 0.7213 / (1 + 1.079 / m);
    }

    private long hash(String item) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(item.getBytes(StandardCharsets.UTF_8));
            long result = 0;
            for (int i = 0; i < 8; i++) {
                result = (result << 8) | (hashBytes[i] & 0xff);
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            return (long) item.hashCode() << 32 | (item.hashCode() & 0xFFFFFFFFL);
        }
    }

    public void add(String item) {
        long x = hash(item);
        int j = (int) (x >>> (64 - b));
        long w = (x << b);
        int rho = Long.numberOfLeadingZeros(w | (1L << (b - 1))) + 1;

        if (rho > registers[j]) {
            registers[j] = (byte) rho;
        }
    }

    public void merge(Main other) {
        if (this.b != other.b) {
            throw new IllegalArgumentException("Kova sayıları eşleşmiyor.");
        }
        for (int i = 0; i < m; i++) {
            this.registers[i] = (byte) Math.max(this.registers[i], other.registers[i]);
        }
    }

    public long estimate() {
        double sum = 0;
        for (byte r : registers) {
            sum += 1.0 / Math.pow(2.0, (double) r);
        }

        double estimate = alphaMM * m * m * (1.0 / sum);

        if (estimate <= 2.5 * m) {
            int zeroCount = 0;
            for (byte r : registers) if (r == 0) zeroCount++;
            if (zeroCount != 0) {
                estimate = (double) m * Math.log((double) m / (double) zeroCount);
            }
        }
        return Math.round(estimate);
    }

    public static void main(String[] args) {
        Main hll1 = new Main(10);
        Main hll2 = new Main(10);

        System.out.println("Sistem başlatıldı. Veri setleri işleniyor...");

        for (int i = 0; i < 10000; i++) {
            hll1.add("user_" + i);
        }

        for (int i = 5000; i < 15000; i++) {
            hll2.add("user_" + i);
        }

        System.out.println("HLL 1 Sonucu: " + hll1.estimate());
        System.out.println("HLL 2 Sonucu: " + hll2.estimate());

        hll1.merge(hll2);

        System.out.println("Birleştirilmiş Küme Tahmini: " + hll1.estimate());
        System.out.println("Beklenen Benzersiz Eleman: 15000");
    }
}