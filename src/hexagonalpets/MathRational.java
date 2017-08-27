package hexagonalpets;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;

public class MathRational {

    public int p;  //numerator
    public int q;  //denominator
    public int code;
    public int level;

    public MathRational(int p, int q) {
        this.p = p;
        this.q = q;
        this.code = 0;
        this.level = 0;
    }

    public MathRational() {
    }


    /*The first part of this file deals with the approximation
     of real numbers by rationals.*/
    public static MathRational reduce(MathRational F) {
        int k = GCD(F.p, F.q);
        MathRational G = new MathRational(F.p / k, F.q / k);
        return (G);
    }

    public static int[] reduce(int a, int b) {
        int c = GCD(a, b);
        int[] d = {a / c, b / c};
        return (d);
    }

    public static MathRational average(MathRational F1, MathRational F2) {
        MathRational G1 = reduce(F1);
        MathRational G2 = reduce(F2);
        MathRational H = new MathRational(G1.p + G2.p, G1.q + G2.q);
        H = reduce(H);
        return (H);
    }

    /*
     1 means (nearly) equals left endpoint
     2 means inside
     3 means (nearly) equals right endpoint
     0 means outside
     */
    public static int isInside(double x, MathRational[] F) {
        double f0 = 1.0 * F[0].p / F[0].q;
        double f1 = 1.0 * F[1].p / F[1].q;
        double cutoff = 0.000000000000001;
        if (x < f0 - cutoff) {
            return (0);
        }
        if (x > f1 + cutoff) {
            return (0);
        }
        if (Math.abs(x - f0) <= cutoff) {
            return (1);
        }
        if (Math.abs(x - f1) <= cutoff) {
            return (3);
        }
        return (2);
    }

    public static MathRational[] subdivide0(int k, MathRational[] F) {
        MathRational[] G = new MathRational[2];
        G[0] = new MathRational(0, 1);
        G[1] = new MathRational(1, 1);
        if (k == 0) {
            G[0] = new MathRational(F[0].p, F[0].q);
            G[1] = average(F[0], F[1]);
        }

        if (k == 1) {
            G[0] = average(F[0], F[1]);
            G[1] = new MathRational(F[1].p, F[1].q);
        }
        G[0].code = k;
        return (G);
    }

    public static MathRational[] subdivide(double x, MathRational[] F) {
        MathRational[] G1 = subdivide0(0, F);
        MathRational[] G2 = subdivide0(1, F);
        int test = isInside(x, G2);
        if (test != 0) {
            return (G2);
        }
        return (G1);
    }

    /**
     * the double should be between 0 and 1
     */
    public static MathRational approximate0(double x, double tol) {
        MathRational[] F = new MathRational[2];
        int code = 0;
        F[0] = new MathRational(0, 1);
        F[1] = new MathRational(1, 1);
        int count = 0;
        double test = 1.0;
        while ((count < 1200) && (test > tol)) {
            ++count;
            F = subdivide(x, F);
            double f0 = 1.0 * F[0].p / F[0].q;
            double f1 = 1.0 * F[1].p / F[1].q;
            test = Math.abs(f0 - f1);
        }

        MathRational G = average(F[0], F[1]);
        double d1 = 1.0 * G.p / G.q - x;
        if (d1 > 0) {
            return (F[0]);
        }
        return (F[1]);
    }

    /**
     * the double can be a+x. a is an integer, x in [0,1]
     */
    public static int[] approximate(double x, double tol) {
        if (x >= 0) {
            double x1 = Math.floor(x);
            double x2 = x - x1;
            int[] I = new int[2];

            if (Math.abs(x2) < .000000001) {
                I[0] = (int) (x1);
                I[1] = 1;
                return (I);
            }
            if (Math.abs(x2) > 1 - .000000001) {
                I[0] = (int) (x1 + 1);
                I[1] = 1;
                return (I);
            }

            MathRational F = approximate0(x2, tol);
            I[0] = (int) (x1 * F.q + F.p);
            I[1] = F.q;
            return (I);
        } else {
            double x1 = Math.floor(x);
            double x2 = x - x1;
            int[] I = new int[2];

            if (Math.abs(x2) < .000000001) {
                I[0] = (int) (x1);
                I[1] = 1;
                return (I);
            }
            if (Math.abs(x2) > 1 - .000000001) {
                I[0] = (int) (x1 + 1);
                I[1] = 1;
                return (I);
            }

            MathRational F = approximate0(x2, tol);
            I[0] = (int) (x1 * F.q + F.p);
            I[1] = F.q;
            return (I);
        }
    }


    /*This first routine implements the extended Euclidean algorithm.
     This is to say that program takes integers a,b and finds not
     only g=gcd(a,b) but also integers c,d such that ac+bd=g.  I 
     originally had programmed my own version of the Euclidean algorithm,
     but I lifted this algorithm from the internet.  The algorithm is
     taken from Donald Knuth's book of algorithms.*/
    public static int[] GCDe(int a, int b) {
        int u, v, g;
        int u1, v1, g1;
        int t1, t2, t3;
        int q;
        u = 1;
        v = 0;
        g = a;
        u1 = 0;
        v1 = 1;
        g1 = b;
        while (g1 != 0) {
            q = g / g1;
            t1 = u - q * u1;
            t2 = v - q * v1;
            t3 = g - q * g1;
            u = u1;
            v = v1;
            g = g1;
            u1 = t1;
            v1 = t2;
            g1 = t3;
        }
        int[] x = {u, v, g};
        return (x);
    }

    public static int[] cfe(int[] P) {
        if (P[0] < P[1]) {
            return (cfe1(P));
        }
        int k = P[0] / P[1];
        int[] Q = {P[0] - k * P[1], P[1]};
        int[] list = cfe1(Q);
        list[0] = k;
        return (list);
    }

    /*continued fraction, for number in (0,1)*/
    public static int[] cfe1(int[] P) {
        int[] X = {P[0], P[1]};
        int[] list = new int[100];
        int count = 0;
        while (X[0] > 0) {
            list[count] = cfe0(X);
            ++count;
            X = gauss(X);
        }
        int[] list2 = new int[count + 1];
        list2[0] = 0;
        for (int i = 0; i < count; ++i) {
            list2[i + 1] = list[i];
        }
        return (list2);
    }

    /**
     * This computes the predecessors of a fraction. These are the two
     * farey-related fractions which have smaller denominator
     */
    public static int[] predecessor(int choice, int[] P) {
        int[] Q = rawPredecessor(P);
        double p = 1.0 * P[0] / P[1];
        double q = 1.0 * Q[0] / Q[1];
        if ((choice == 0) && (q < p)) {
            return (Q);
        }
        if ((choice == 1) && (q > p)) {
            return (Q);
        }
        Q[0] = P[0] - Q[0];
        Q[1] = P[1] - Q[1];
        return (Q);
    }

    public static int[] rawPredecessor(int[] P) {
        int p0 = P[0];
        int q0 = P[1];
        int[] x = GCDe(p0, q0);
        if (x[2] > 1) {
            return (null);
        }
        if (x[0] < 0) {
            x[0] = -x[0];
        }
        if (x[1] < 0) {
            x[1] = -x[1];
        }
        int[] y = {x[0], x[1]};
        if (x[0] > x[1]) {
            y[0] = x[1];
            y[1] = x[0];
        }
        return (y);
    }

    /**
     * the gauss map
     */
    public static double gauss(double x) {
        double y = 1 / x - Math.floor(1 / x);
        return (y);
    }

    public static int[] gauss(int[] P) {
        int p = P[0];
        int q = P[1];
        double a = 1.0 * q / p;
        double b = Math.floor(a);
        int q1 = p;
        int p1 = (int) (q - b * p + .00000001);
        int[] X = {p1, q1};
        return (X);
    }

    public static int cfe0(int[] P) {
        int p = P[0];
        int q = P[1];
        double a = 1.0 * q / p;
        double b = Math.floor(a);
        int B = (int) (b);
        return (B);
    }

    /*This checks if all cfe coeffs are <=k.*/
    public static boolean cfeSmall(int[] P, int k) {
        int[] list = cfe(P);
        for (int i = 0; i < list.length; ++i) {
            if (list[i] > k) {
                return (false);
            }
        }
        return (true);
    }

    /**
     * continued fraction to fraction
     */
    public static int[] toFraction(int[] a) {
        int n = a.length;
        int[] h = new int[n + 2];
        int[] k = new int[n + 2];
        h[0] = 0;
        h[1] = 1;
        k[0] = 1;
        k[1] = 0;
        for (int i = 0; i < n; ++i) {
            h[i + 2] = a[i] * h[i + 1] + h[i];
            k[i + 2] = a[i] * k[i + 1] + k[i];
        }
        int[] b = {h[n + 1], k[n + 1]};
        return (b);
    }


    /*This computes the greatest common divisor, without the coefficients.*/
    public static int GCD(int a, int b) {
        int[] x = GCDe(a, b);
        return (x[2]);
    }

    /**
     * this moves the point to the center of the nearest square in the 1/D grid/
     */
    public static Complex gridpointZero(Complex z, int D) {
        Complex w = new Complex(D * z.x + .5, D * z.y + .5);
        double x = Math.floor(w.x);
        double y = Math.floor(w.y);
        x = x / D;
        y = y / D;
        w = new Complex(x, y);
        return (w);
    }

    public static Complex gridpointMid(Complex z, int D) {
        Complex w = new Complex(D * z.x + .5, D * z.y + .5);
        double x = Math.floor(w.x) + .5;
        double y = Math.floor(w.y) + .5;
        x = x / D;
        y = y / D;
        w = new Complex(x, y);
        return (w);
    }

    /*this converts a double to an integer.  It is used when the
     double is already supposed to be an integer.*/
    public static int convert(double d) {
        int sign = 1;
        if (d < 0) {
            sign = -1;
        }
        int k = convertPositive(d);
        return (sign * k);
    }

    public static int convertPositive(double d) {
        double e = Math.abs(d);
        return ((int) (e + .1));
    }

    public static long ModifyRational(double x, int z) {
        long index = 0;
        int mode = 0;
        long first = 0;
        if (x < 0) {
            x = -x;
            mode = 1;
        }
        double x0 = x;
        if (x > 1) {
            first = Math.round(Math.floor(x));
            x = x - first;
        }
        for (int i = 0; i < z + 1; i++) {
            double x1 = 1.0 * i / z;
            if (Math.abs(x1 - x) < x0) {
                index = i;
                x0 = Math.abs(x1 - x);
            }
        }
        index = first * z + index;

        if (mode == 1) {
            index = -index;
        }
        return (index);
    }

    public static int[] cutoff(int[] r) {
        int[] b = cfe(r);
        int mode = 0;
        if (r[0] < 0 && r[1] > 0) {
            r[0] = -r[0];
            mode = 1;
        }
        if (r[1] < 0 && r[0] > 0) {
            r[1] = -r[1];
            mode = 1;
        }

        if ((r[0] >= 0 && r[1] > 0) || (r[0] <= 0 && r[1] < 0)) {
            b = cfe(r);

            int count = b.length;
            if (b.length > 2) {
                for (int k = 2; k < b.length; k++) {
                    if (b[k] > 300) {
                        count = k;
                        break;
                    }
                }
            }
            int[] c = new int[count];
            for (int j = 0; j < count; j++) {
                c[j] = b[j];
            }

            int[] d = EntrySystem.interpretCFE(c);
            if (mode == 1) {
                d[0] = (-1) * d[0];
            }
            return (d);
        }
        return (b);
    }

    public static void print(double s) {
        long v_large = Math.round(s);
        String number = String.valueOf(v_large);
        for (int i = 0; i < number.length(); i++) {
            int j = Character.digit(number.charAt(i), 10);
            System.out.print(j + ",");
        }
    }

}
