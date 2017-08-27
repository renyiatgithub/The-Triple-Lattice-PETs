package hexagonalpets;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class EntrySystem {

    /**
     * Delete ':,/' in the string *
     */
    public static int[] interpretSEQ(char u, String S) {
        int count = 1;
        int[] x = new int[50];
        x[0] = -1;
        int s = S.length();
        if (S.charAt(s - 1) == u) {
            S = S.substring(0, s - 1);
        }
        for (int i = 0; i < s; ++i) {
            if (S.charAt(i) == u) {
                x[count] = i;
                ++count;
            }
        }
        x[count] = s;
        int[] y = new int[count];
        for (int i = 0; i < count; ++i) {
            String T = S.substring(x[i] + 1, x[i + 1]);
            Integer t = new Integer(T);
            y[i] = t.intValue();
        }
        return (y);
    }

    public static int countChar(String T, char x) {
        int count = 0;
        int n = T.length();
        for (int i = 0; i < n; ++i) {
            char ch = T.charAt(i);
            if (ch == x) {
                ++count;
            }
        }
        return (count);
    }

    public static int classify(String S) {
        int c1 = countChar(S, ',');
        int c2 = countChar(S, '/');
        int c3 = countChar(S, '(');
        int c4 = countChar(S, ')');
        int c5 = countChar(S, '.');
        if ((c1 > 0) && (c2 + c3 + c4 > 0)) {
            return (-1);
        }
        if (c3 != c4) {
            return (-1);
        }
        if (c1 > 0) {
            return (1);
        }
        if (c3 > 0) {
            return (3);
        }
        if (c5 > 0) {
            return (5);
        }
        return (0);
    }

    public static int[] interpretQI(String S) {
        int k1 = S.indexOf("+");
        int k2 = S.indexOf("(");
        int k3 = S.indexOf(")");
        String S1 = S.substring(0, k1);
        String S2 = S.substring(k1 + 1, k2);
        String S3 = S.substring(k2 + 1, k3);

        S1 = S1.replace(" ", "");
        S2 = S2.replace(" ", "");
        S3 = S3.replace(" ", "");

        int[] a1 = interpretFraction(S1);
        int[] a2 = interpretFraction(S2);
        Integer b = new Integer(S3);
        int a3 = b.intValue();
        int[] c = {a1[0], a1[1], a2[0], a2[1], a3};
        return (c);
    }

    public static double interpretDouble(String S) {
        return (Double.parseDouble(S));
    }

    public static int[] interpretCFE(int[] a) {
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

    public static int[] interpretFraction(String U) {
        int k = U.indexOf("/");
        if (k == -1) {
            U = U.replace(" ", "");
            Integer I = new Integer(U);
            int[] a = {I.intValue(), 1};
            return (a);
        }
        String T1 = U.substring(0, k);
        String T2 = U.substring(k + 1, U.length());
        Integer I1 = new Integer(T1);
        Integer I2 = new Integer(T2);
        int[] I = {I1.intValue(), I2.intValue()};
        return (I);
    }

    static public int[] fractionToCFE(String S) {
        int[] b = {0};
        int[] a = interpretFraction(S);
        if (a[0] > 0 && a[1] > 0) {
            b = MathRational.cfe(a);
        }
        return (b);
    }

    static public int[] doubletoCFE(String S) {
        int[] b = {0};
        double vv = interpretDouble(S);
        int[] frac = MathRational.approximate(vv, Math.pow(10, -10));
        if (frac[0] > 0 && frac[1] > 0) {
            b = MathRational.cfe(frac);
        }
        return (b);
    }

    public static double Fraction(int[] b) {
        double y = 1.0 * b[0] / b[1];
        System.out.println(b[0] + "/" + b[1] + ", " + y);
        return (y);
    }

    public static int negative(String S) {
        int negative = 1;
        if (S.charAt(0) == '-') {
            negative = -1;
            return (negative);
        }
        return (negative);
    }

    public static double Frac_Decimal(int m, int n) {
        double x = 1.0 * m / n;
        return (x);
    }

    public static int[] argrenorm(double s) {
        double t = s / (1 - 2 * s);
        int m = (int) Math.floor(t);
        int n = m;
        int r = 1;
        int[] bb = new int[2];

        if (t - m > 0.5) {
            n = m + 1;
        }
        if (t - m < -0.5) {
            n = m - 1;
        }
        if (t - n < 0) {
            r = -1;
        }
        bb[0] = n;
        bb[1] = r;

        return (bb);
    }

    public static double value(String s_text) {
        double value1 = 0;
        int classify = EntrySystem.classify(s_text);

        if (classify == 1) {
            int[] seq1 = EntrySystem.interpretSEQ(',', s_text);
            int[] l1 = EntrySystem.interpretCFE(seq1);
            value1 = EntrySystem.Fraction(l1);
        }

        if (classify == 0) {
            int[] frac = EntrySystem.interpretFraction(s_text);
            value1 = EntrySystem.Fraction(frac);
        }
        if (classify == 3) {
            int[] a = EntrySystem.interpretQI(s_text);
            value1 = 1.0 * a[0] / a[1] + 1.0 * a[2] / a[3] * Math.sqrt(a[4]);
        }
        if(classify == 5){
            value1 = Double.parseDouble(s_text);
        }
        return (value1);
    }

}
