import javafx.util.Pair;

import java.math.BigInteger;
import java.util.*;

public class Main {

    private static int max = 200;

    public static void main(String[] args) {
        new Main().exec2();
    }

    void exec2() {
        BigInteger p = randSimple();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter from 0 to " + p);
        BigInteger m = new BigInteger(String.valueOf(scanner.nextInt()));

        BigInteger g = getPRoot(p);

        BigInteger x = rand(p);

        BigInteger y = pow(g, x).mod(p);
        System.out.println("Public key = " + y);
        System.out.println("Private key = " + x);
        Pair<BigInteger, BigInteger> pair = encrypte(m, g, y, p);
        System.out.println("Pair " + pair);
        System.out.println(decrypte(pair, x, p));
    }

    void exec() {
        /*BigInteger m = new BigInteger("10014");
        BigInteger p = randSimple();
        BigInteger g = rand();

        BigInteger x = rand();//Private key
        BigInteger y = pow(g, x);//Public key

        Pair<BigInteger, BigInteger> pair = encrypte(m, g, y);//encr
        BigInteger decrypte = decrypte(pair, x);//decr

        System.out.println(g + "\t" + x + "\t" + y);
        System.out.println(pair);
        System.out.println("Was = " + m + "; Become = " + decrypte + ".");*/

    }

    private BigInteger randSimple() {
        BigInteger number = rand();
        while (!number.isProbablePrime(100)) {
            number = rand();
        }
        return number;
    }

    private BigInteger decrypte(Pair<BigInteger, BigInteger> pair, BigInteger x, BigInteger p) {
        return (pair.getValue()
                .multiply(getSmth(pow(pair.getKey(), x), p)))
                .mod(p);
        //.divide(pow(pair.getKey(), x));
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

    private Pair<BigInteger, BigInteger> encrypte(BigInteger m, BigInteger g, BigInteger y, BigInteger p) {
        BigInteger r = rand(p);
        return new Pair<>(
                pow(g, r).mod(p),
                m.multiply(pow(y, r).mod(p))
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

    //generate random num
    BigInteger genKey(BigInteger q) {
        BigInteger key = rand();
        while (!Objects.equals(q.gcd(q), BigInteger.ONE)) {
            key = q.add(rand().mod(new BigInteger("3646")));
        }
        return key;
    }

    public static BigInteger getPRoot(BigInteger p) {
        for (BigInteger i = BigInteger.ZERO; i.compareTo(p) < 0; i = i.add(BigInteger.ONE))
            if (IsPRoot(p, i))
                return i;
        return BigInteger.ZERO;
    }

    public static boolean IsPRoot(BigInteger p, BigInteger a) {
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

    public BigInteger getSmth(BigInteger a, BigInteger m) {
        BigInteger b = BigInteger.ONE;
        while (!a.multiply(b).mod(m).equals(BigInteger.ONE)) {
            b = b.add(BigInteger.ONE);
        }
        return b;
    }
}
