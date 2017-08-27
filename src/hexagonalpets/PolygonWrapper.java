package hexagonalpets;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;

public class PolygonWrapper {

    Complex[] z = new Complex[7000];
    int count;
    int colormode = 0;
    Complex vector = new Complex(0, 0);
    int index = -1;

    /**
     * Constructors*
     */
    public PolygonWrapper() {
    }

    public PolygonWrapper(PolygonWrapper X) {
        this.count = X.count;
        for (int i = 0; i < count; ++i) {
            this.z[i] = new Complex(X.z[i]);
        }
        this.vector = new Complex(X.vector);
        this.index = X.index;
    }

    public PolygonWrapper(int cc, Complex[] zz) {
        this.count = cc;
        for (int i = 0; i < cc; ++i) {
            z[i] = new Complex(zz[i]);
        }
    }

    /**
     * For drawing the polygon*
     */
    public GeneralPath toGeneralPath() {
        GeneralPath gp = new GeneralPath();
        gp.moveTo((float) (z[0].x), (float) (z[0].y));
        for (int i = 0; i < count; ++i) {
            gp.lineTo((float) (z[i].x), (float) (z[i].y));
        }
        gp.closePath();
        return (gp);
    }

    public GeneralPath Path() {
        GeneralPath gp = new GeneralPath();
        gp.moveTo((float) (z[0].x), (float) (z[0].y));
        for (int i = 0; i < count; ++i) {
            gp.lineTo((float) (z[i].x), (float) (z[i].y));
        }
        return (gp);
    }


    /*converts a polygonal generalpath into a polygon*/
    public static PolygonWrapper fromGeneralPath(GeneralPath X) {
        PolygonWrapper Y = new PolygonWrapper();
        AffineTransform A = AffineTransform.getTranslateInstance(0, 0);
        PathIterator P = X.getPathIterator(A);
        double[] coords = new double[6];
        int count = 0;
        Complex HISTORY = new Complex(-9999, -9999);
        Complex CURRENT = new Complex();
        while (P.isDone() == false) {
            P.currentSegment(coords);
            CURRENT = new Complex(coords[0], coords[1]);
            if (Complex.dist(CURRENT, HISTORY) > .00000001) {
                Y.z[count] = new Complex(CURRENT);
                ++count;
                HISTORY = new Complex(CURRENT);
            }
            P.next();
        }
        Y.count = count;
        return (Y);
    }

    public boolean contains(Complex z) {
        GeneralPath gp = this.toGeneralPath();
        return (gp.contains(z.x, z.y));
    }

    public void print() {
        System.out.println("count " + count);
        for (int i = 0; i < count; ++i) {
            z[i].print();
        }
    }

    public Complex center() {
        Complex w = new Complex();
        for (int i = 0; i < count; ++i) {
            w = Complex.plus(w, this.z[i]);
        }
        w.x = w.x / count;
        w.y = w.y / count;
        return (w);
    }


    /*Computes the area of a convex polytope*/
    public double area() {
        Complex c = this.center();
        double a = 0;
        for (int i = 0; i < count; ++i) {
            int i1 = i;
            int i2 = (i + 1) % count;
            Complex w1 = Complex.minus(z[i1], c);
            Complex w2 = Complex.minus(z[i2], c);
            Complex w3 = Complex.times(w1, w2.conjugate());
            a = a + Math.abs(w3.y) / 2;
        }
        return (a);
    }

    /**
     * Here is the main convex hull routine*
     */
    public static PolygonWrapper convexHull(PolygonWrapper PP) {
        if (PP == null) {
            return (null);
        }
        if (PP.count == 0) {
            return (null);
        }
        PolygonWrapper P = weedOut(PP);
//        if (P.count == 1) {
//            return (null);
//        }
        PolygonWrapper Q = new PolygonWrapper();
        int[] n = new int[100];
        int match;
        int ct;
        for (int i = 0; i < P.count; ++i) {
            Q.z[i] = new Complex(P.z[i]);
        }
        Q.count = P.count;
        n[0] = bottom(Q);
        match = 0;
        ct = 0;
        Complex z1 = P.z[0];

        while (match == 0) {
            n[ct + 1] = nextPoint(Q, n[ct]);
            Complex z0 = P.z[n[ct + 1]];
            if (n[ct + 1] == n[0]) {
                match = 1;
            }
            if (ct >= P.count) {
                match = 1;
            }
            if (n[ct + 1] == -1) {
                match = 1;
            }
            Q = roll(Q, n[ct], n[ct + 1]);
            ++ct;
        }
        for (int i = 0; i < ct; ++i) {
            Q.z[i] = P.z[n[i]];
        }
        Q.count = ct;
        return (Q);
    }

    /*This is the main intersection routine.
      WARNING: polygons must be correctly oriented.*/
    public static PolygonWrapper intersect(PolygonWrapper P1, PolygonWrapper P2) {
        if (P1 == null) {
            return (null);
        }
        if (P2 == null) {
            return (null);
        }
        PolygonWrapper Q = new PolygonWrapper();
        Q.count = 0;
        for (int i = 0; i < P1.count; ++i) {
            for (int j = 0; j < P2.count; ++j) {
                int i1 = i;
                int i2 = (i + 1) % P1.count;
                int j1 = j;
                int j2 = (j + 1) % P2.count;
                Complex q = intersect(P1.z[i1], P1.z[i2], P2.z[j1], P2.z[j2]);
                if (q != null) {
                    if (badNumber(q) == false) {
                        Q.z[Q.count] = new Complex(q);
                        ++Q.count;
                    }
                }
            }
        }

        for (int i = 0; i < P1.count; ++i) {
            if (contains(P2, P1.z[i]) == true) {
                Q.z[Q.count] = new Complex(P1.z[i]);
                ++Q.count;
            }
        }

        for (int i = 0; i < P2.count; ++i) {
            if (contains(P1, P2.z[i]) == true) {
                Q.z[Q.count] = new Complex(P2.z[i]);
                ++Q.count;
            }
        }
        if (Q.count < 3) {
            return null;
        }
        try {
            Q = convexHull(Q);
        } catch (Exception e) {
            return (null);
        }
        return (Q);
    }

    /**
     * Some supporting routines
     */
    /**
     * This weeds out redundant points, up to a small tolerance*
     */
    public static PolygonWrapper weedOut(PolygonWrapper P) {
        PolygonWrapper Q = new PolygonWrapper();
        int count = 0;
        Q.z[1] = new Complex(P.z[0]);
        for (int i = 0; i < P.count; ++i) {
            Complex w = new Complex(P.z[i]);
            boolean redundant = false;
            for (int j = 0; j < count; ++j) {
                if (Complex.dist(w, Q.z[j]) < .00000000001) {
                    redundant = true;
                }
            }
            if (redundant == false) {
                Q.z[count] = new Complex(w);
                ++count;
            }
        }
        Q.count = count;
        return (Q);
    }

    public static PolygonWrapper weedOut1(PolygonWrapper P) {
        PolygonWrapper Q = new PolygonWrapper();
        int count = 0;
        Q.z[1] = new Complex(P.z[0]);
        for (int i = 0; i < P.count; ++i) {
            Complex w = new Complex(P.z[i]);
            boolean redundant = false;
            for (int j = 0; j < count; ++j) {
                if (Complex.dist(w, Q.z[j]) < .0000001) {
                    redundant = true;
                }
            }
            if (redundant == false) {
                Q.z[count] = new Complex(w);
                ++count;
            }
        }
        Q.count = count;
        return (Q);
    }

    /*given a polygon with a horizontal edge,
      this routine finds the point immediately
      counterclockwise from the edge*/
    public static int nextPoint(PolygonWrapper P, int n) {
        double test, min;
        Complex ONE = new Complex(1, 0);
        Complex w = new Complex();
        int index = -1;
        min = 10000000;
        for (int i = 0; i < P.count; ++i) {
            if (i != n) {
                w = Complex.minus(P.z[i], P.z[n]);
                if (w.norm() > .0000000001) {
                    w = Complex.unit(w);
                    test = Complex.dist(w, ONE);
                    if (test < min) {
                        min = test;
                        index = i;
                    }
                }
            }
        }
        return (index);
    }


    /*this routine rotates a polygon so that
      the edge determined by the indices a and b
      is horizontal*/
    public static PolygonWrapper roll(PolygonWrapper P, int a, int b) {
        Complex w = new Complex();
        PolygonWrapper Q = new PolygonWrapper();
        w = Complex.minus(P.z[b], P.z[a]);
        w = Complex.unit(w);
        for (int i = 0; i < P.count; ++i) {
            Q.z[i] = Complex.divide(P.z[i], w);
        }
        Q.count = P.count;
        return (Q);
    }

    /**
     * Finds the bottom most point of the polygon*
     */
    public static int bottom(PolygonWrapper P) {
        double test, min;
        int X = -1;
        min = 10000.0;
        for (int i = 0; i < P.count; ++i) {
            test = P.z[i].y;
            if (test < min) {
                X = i;
                min = test;;
            }
        }
        return (X);
    }

    public static boolean badNumber(Complex q) {
        if (badNumber(q.x) == true) {
            return (true);
        }
        if (badNumber(q.y) == true) {
            return (true);
        }
        return (false);
    }

    public static boolean badNumber(double d) {
        double D = Double.valueOf(d);
        if (Double.isNaN(D) == true) {
            return (true);
        }
        if (Double.isInfinite(D) == true) {
            return (true);
        }
        return (false);
    }

    public static boolean contain_origin(PolygonWrapper P, Complex z) {
        for (int i = 0; i < P.count; ++i) {
            int i1 = i;
            int i2 = (i + 1) % P.count;
            Complex w1 = P.z[i1];
            Complex w2 = P.z[i2];
            if (Complex.dist(z, w1) < .0000000001) {
                return (true);
            }
            boolean test = Complex.isPositivelyOriented(z, w1, w2);
            if (test == false) {
                return (false);
            }
        }
        return (true);
    }

    public static double side(Complex z0, Complex z1, Complex w) {
        Complex Z0 = Complex.minus(z0, w);
        Complex Z1 = Complex.minus(z1, w);
        Complex A = Complex.times(Z0, Z1.conjugate());
        return (A.y);
    }

    public static boolean contains(PolygonWrapper P, Complex z) {
        for (int i = 0; i < P.count; i++) {
            if (Complex.dist(P.z[i], z) < Math.pow(10, -7)) {
                return (true);
            }
        }

        for (int i = 0; i < P.count; i++) {
            Complex zi = P.z[i];
            Complex zj = P.z[(i + 1) % P.count];
            if (isBetween(zi, zj, z) == true) {
                return (true);
            }
        }

        for (int i = 0; i < P.count; i++) {
            int i1 = i;
            int i2 = (i + 1) % P.count;
            int i3 = (i + 2) % P.count;
            Complex w1 = P.z[i1];
            Complex w2 = P.z[i2];
            Complex w3 = P.z[i3];
            double t1 = side(w1, w2, w3) * side(w1, w2, z);

            if (t1 <= -1.0 * Math.pow(10, -7)) {
                return (false);
            }
            if (t1 < 0 & t1 > -1.0 * Math.pow(10, -7)) {
                if (isBetween(w1, w2, z) == false) {
                    return (false);
                }
            }

        }
        return (true);
    }

    public static boolean isBetween(Complex z1, Complex z2, Complex w) {
        double d1 = Complex.dist(w, z1);
        double d2 = Complex.dist(w, z2);
        double d3 = Complex.dist(z1, z2);
        double test = Math.abs(d1 + d2 - d3);
        if (test < Math.pow(10, -10)) {
            return (true);
        }
        return (false);
    }

    public static boolean checkSegment(Complex z1, Complex w1, Complex w2) {
        double d1 = Complex.dist(z1, w1);
        double d2 = Complex.dist(z1, w2);
        double d3 = Complex.dist(w1, w2);
        double test = Math.abs(d1 + d2 - d3);
        if (test < .000000001) {
            return (true);
        }
        return (false);
    }

    public static Complex intersect(Complex z1, Complex z2, Complex w1, Complex w2) {
        if (checkSegment(z1, w1, w2) == true) {
            return (z1);
        }
        if (checkSegment(z2, w1, w2) == true) {
            return (z2);
        }
        if (checkSegment(w1, z1, z2) == true) {
            return (w1);
        }
        if (checkSegment(w2, z1, z2) == true) {
            return (w2);
        }
        Complex q = intersectRaw(z1, z2, w1, w2);
        double d1 = Math.max(Complex.dist(q, z1), Complex.dist(q, z2));
        double d2 = Complex.dist(z1, z2);
        if (d1 > d2) {
            return (null);
        }
        d1 = Math.max(Complex.dist(q, w1), Complex.dist(q, w2));
        d2 = Complex.dist(w1, w2);
        if (d1 > d2) {
            return (null);
        }
        return (q);
    }

    public static Complex intersectRaw(Complex z1, Complex z2, Complex w1, Complex w2) {
        Vector[] V = new Vector[7];
        V[0] = new Vector(z1);
        V[1] = new Vector(z2);
        V[2] = new Vector(w1);
        V[3] = new Vector(w2);
        V[4] = Vector.cross(V[0], V[1]);
        V[5] = Vector.cross(V[2], V[3]);
        V[6] = Vector.cross(V[4], V[5]);
        Complex w = new Complex(V[6].x[0] / V[6].x[2], V[6].x[1] / V[6].x[2]);
        return (w);
    }

    /**
     * These two routines start with a polygon that may have repeated vertices.
     * The output is a polygon with the repeated vertices weeded out.*
     */
    public PolygonWrapper trim() {
        PolygonWrapper Q = new PolygonWrapper();
        Complex[] LIST = new Complex[this.count];
        int c = 0;
        for (int i = 0; i < count; ++i) {
            if (onList(z[i], LIST, c) == false) {
                LIST[c] = new Complex(z[i]);
                ++c;
            }
        }
        Q.count = c;
        for (int i = 0; i < c; ++i) {
            Q.z[i] = new Complex(LIST[i]);
        }
        return (Q);
    }

    public boolean onList(Complex z, Complex[] LIST, int count) {
        for (int i = 0; i < count; ++i) {
            if (Complex.dist(z, LIST[i]) < Math.pow(10, -12)) {
                return (true);
            }
        }
        return (false);
    }

    /**
     * This routine gives a cheap way to take the convex hull. What makes this
     * routine work for us is that we have a bound (8) on the possible side
     * directions of our polygons. This routine is not robust.
     */
    public static PolygonWrapper cheapHull(PolygonWrapper P) {
        PolygonWrapper Q = new PolygonWrapper();
        int[] n = new int[65];
        for (int i = 0; i < 65; ++i) {
            n[i] = -1;
        }
        Complex u = new Complex();
        for (int i = 0; i < 64; ++i) {
            double theta = 1.0 * Math.PI * i / 32.0 + Math.PI / 64.0;
            u = new Complex(Math.cos(theta), Math.sin(theta));
            n[i] = extreme(u, P);
        }
        int count = 0;
        Q.z[0] = P.z[n[0]];
        ++count;
        for (int i = 1; i < 64; ++i) {
            if ((n[i] != -1) && (n[i] != n[i - 1])) {
                Q.z[count] = P.z[n[i]];
                ++count;
            }
        }
        Q.count = count;
        Q = Q.trim();
        return (Q);
    }

    /**
     * Finds the extreme points in a given direction
     */
    public static int extreme(Complex u, PolygonWrapper P) {
        double test, min;
        int X;
        min = 10000.0;
        X = 0;
        Complex v;
        for (int i = 0; i < P.count; ++i) {
            v = Complex.times(u, P.z[i]);
            test = v.y;
            if (test < min) {
                X = i;
                min = test;
            }
        }
        return (X);
    }

    public PolygonWrapper reflection(Complex z) {
        PolygonWrapper W = new PolygonWrapper(this);
        for (int i = 0; i < W.count; i++) {
            W.z[i].x = 2 * z.x - W.z[i].x;
            W.z[i].y = 2 * z.y - W.z[i].y;
        }
        return (W);
    }

    public PolygonWrapper flip_vertical(Complex z) {
        PolygonWrapper W = new PolygonWrapper(this);
        for (int i = 0; i < W.count; i++) {
            W.z[i].x = 2 * z.x - W.z[i].x;
        }
        return (W);
    }
}
