import javafx.util.Pair;

import java.math.BigInteger;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        new Main().exec();
    }

    void exec() {
        BigInteger p = randPrime();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter from 0 to " + p);
        BigInteger m = new BigInteger(String.valueOf(scanner.nextInt()));

        BigInteger g = getPRoot(p);

        BigInteger x = rand(p);

        BigInteger y = pow(g, x).mod(p);

        Pair<BigInteger, BigInteger> pair = encrypt(m, g, y, p);

        System.out.println("private key = " + y);
        System.out.println("Private key = " + x);
        System.out.println("Pair " + pair);
        System.out.println("Message is " + m);
        BigInteger decr = decrypt(pair, x, p);
        System.out.println("Message decrypted as " + decr);
    }

    /**
     * Get random prime BigInteger
     *
     * @return random prime BigInteger
     */
    private BigInteger randPrime() {
        BigInteger number = rand();
        while (!number.isProbablePrime(100)) {
            number = rand();
        }
        return number;
    }

    /**
     * Decrypt message
     *
     * @param pair pair of keys (a,b)
     * @param x    public key
     * @param p    module
     * @return decrypted message
     */
    private BigInteger decrypt(Pair<BigInteger, BigInteger> pair, BigInteger x, BigInteger p) {
        return (pair.getValue()
                .multiply(getMod(pow(pair.getKey(), x), p)))
                .mod(p);
    }

    /**
     * Get random from 1 to max (200)
     *
     * @return rand from 1 to max
     */
    private BigInteger rand() {
        Random r = new Random();
        int low = 1;
        int max = 200;
        int high = max;
        return new BigInteger(String.valueOf(r.nextInt(high - low) + low));
    }

    /**
     * Get BigInteger random number
     *
     * @param n max border
     * @return random from (0,p-1)
     */
    private BigInteger rand(BigInteger n) {
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while (result.compareTo(n) >= 0) {
            result = new BigInteger(n.bitLength(), rand);
        }
        if (result.equals(new BigInteger("0"))
                || result.equals(new BigInteger("1"))
                || result.equals(n.subtract(BigInteger.ONE)))
            result = new BigInteger("2");
        return result;
    }

    /**
     * Encrypt message
     *
     * @param m message to encrypt
     * @param g random number
     * @param y private key
     * @param p module
     * @return encrypted message
     */
    private Pair<BigInteger, BigInteger> encrypt(BigInteger m, BigInteger g, BigInteger y, BigInteger p) {
        BigInteger r = rand(p);
        return new Pair<>(
                pow(g, r).mod(p),
                m.multiply(pow(y, r).mod(p))
        );
    }

    /**
     * finds pow from BigIntegers
     *
     * @param base     power from
     * @param exponent power to
     * @return base^exponent
     */
    private static BigInteger pow(BigInteger base, BigInteger exponent) {
        BigInteger result = BigInteger.ONE;
        while (exponent.signum() > 0) {
            if (exponent.testBit(0)) result = result.multiply(base);
            base = base.multiply(base);
            exponent = exponent.shiftRight(1);
        }
        return result;
    }

    /**
     * Finds P Root for p
     *
     * @param p number for witch P Root should be found
     * @return P Root for p
     */
    private static BigInteger getPRoot(BigInteger p) {
        for (BigInteger i = BigInteger.ZERO; i.compareTo(p) < 0; i = i.add(BigInteger.ONE))
            if (isPRoot(p, i))
                return i;
        return BigInteger.ZERO;
    }

    /**
     * Check if a is P Root ( p mod a == 1)
     *
     * @param a module
     * @return is p mod a == 1
     */
    private static boolean isPRoot(BigInteger p, BigInteger a) {
        if (a.equals(BigInteger.ZERO) || a.equals(BigInteger.ONE))
            return false;
        BigInteger last = BigInteger.ONE;

        Set<BigInteger> set = new HashSet<>();
        for (BigInteger i = BigInteger.ZERO; i.compareTo(p.subtract(BigInteger.ONE)) < 0; i = i.add(BigInteger.ONE)) {
            last = (last.multiply(a)).mod(p);
            if (set.contains(last)) // Если повтор
                return false;
            set.add(last);
        }
        return true;
    }

    /**
     * find b from a * b == m
     * b goes from 1 to INFINITY
     *
     * @param a one of multipliers
     * @param m module
     * @return another multipliers
     */
    private BigInteger getMod(BigInteger a, BigInteger m) {
        BigInteger b = BigInteger.ONE;
        while (!a.multiply(b).mod(m).equals(BigInteger.ONE)) {
            b = b.add(BigInteger.ONE);
        }
        return b;
    }
}
