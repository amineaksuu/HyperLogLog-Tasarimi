import java.util.Scanner;

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
        long h = 1125899906842597L;
        int len = item.length();
        for (int i = 0; i < len; i++) {
            h = 31 * h + item.charAt(i);
        }
        return h;
    }

    public void add(String item) {
        long x = hash(item);
        int j = (int) (x >>> (64 - b));
        long w = (x << b) | (1L << (b - 1));
        int rho = Long.numberOfLeadingZeros(w) + 1;

        if (rho > registers[j]) {
            registers[j] = (byte) rho;
        }
    }

    public void merge(Main other) {
        if (this.b != other.b) {
            throw new IllegalArgumentException("Kova sayıları aynı olmalıdır!");
        }
        for (int i = 0; i < m; i++) {
            this.registers[i] = (byte) Math.max(this.registers[i], other.registers[i]);
        }
    }

    public long estimate() {
        double sum = 0;
        for (byte r : registers) {
            sum += Math.pow(2, -r);
        }

        double estimate = alphaMM * m * m * (1.0 / sum);

        if (estimate <= 2.5 * m) {
            int zeroCount = 0;
            for (byte r : registers) if (r == 0) zeroCount++;
            if (zeroCount != 0) {
                estimate = m * Math.log((double) m / zeroCount);
            }
        }
        return Math.round(estimate);
    }

    public static void main(String[] args) {
        Main hll1 = new Main(10);
        Main hll2 = new Main(10);

        System.out.println("Veriler işleniyor...");

        // İlk set: 0-10000
        for (int i = 0; i < 10000; i++) hll1.add("user_" + i);
        // İkinci set: 5000-15000 (kesişim var)
        for (int i = 5000; i < 15000; i++) hll2.add("user_" + i);

        System.out.println("HLL 1 Tahmin: " + hll1.estimate());
        System.out.println("HLL 2 Tahmin: " + hll2.estimate());

        hll1.merge(hll2);
        System.out.println("Birleştirilmiş HLL Tahmini (Beklenen ~15000): " + hll1.estimate());
    }
}