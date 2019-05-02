import javafx.util.Pair;

import java.math.BigInteger;
import java.util.Objects;
import java.util.Random;

public class Main {

    private static int max = 200;

    public static void main(String[] args) {
        double a = (-1) % (7 * 9);
        System.out.println(a);
        new Main().exec();
    }

    void exec() {
        BigInteger m = new BigInteger("10014");
        BigInteger g = rand();

        BigInteger x = rand();//Private key
        BigInteger y = pow(g, x);//Public key

        Pair<BigInteger, BigInteger> pair = encrypte(m, g, y);//encr
        BigInteger decrypte = decrypte(pair, x);//decr

        System.out.println(g + "\t" + x + "\t" + y);
        System.out.println(pair);
        System.out.println("Was = " + m + "; Become = " + decrypte + ".");

    }

    private BigInteger decrypte(Pair<BigInteger, BigInteger> pair, BigInteger x) {
        return pair.getValue().divide(pow(pair.getKey(), x));
    }

    /**
     * @return
     */
    private BigInteger rand() {
        Random r = new Random();
        int low = 1;
        int high = max;
        return new BigInteger(String.valueOf(r.nextInt(high - low) + low));
    }

    private Pair<BigInteger, BigInteger> encrypte(BigInteger m, BigInteger g, BigInteger y) {
        BigInteger r = rand();
        return new Pair<>(
                pow(g, r),
                m.multiply(pow(y, r))
        );
    }

    public static BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0)) result = result.multiply(base);
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }

    BigInteger gcd (BigInteger a, BigInteger b) {
        if (a.compareTo(b) <0) { return b.gcd(a); }
        else if (Objects.equals(a.mod(b), new BigInteger("0"))) { return b; }
        else return gcd(b, a.mod(b));
    }

    //generate random num
    BigInteger gen_key (BigInteger q) {
        BigInteger key;
        key = rand();
        while (!Objects.equals(q.gcd(q), BigInteger.ONE)) {
            key = q.add(rand().mod(new BigInteger("3646")));
        }
        return key;
    }
}
