package hexagonalpets;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;

/*This class does the basic arithmetic
  of vectors up to length 20*/
public class Vector {

    double[] x = new double[20];
    int size;

    public Vector() {
        size = 3;
    }

    /*constructors*/
    public Vector(Complex z) {
        size = 3;
        x[0] = z.x;
        x[1] = z.y;
        x[2] = 1.0;
    }

    public Vector(double a0, double a1, double a2) {
        size = 3;
        x[0] = a0;
        x[1] = a1;
        x[2] = a2;
    }

    public Vector(double xx, double yy) {
        x[0] = xx;
        x[1] = yy;
        x[2] = 1.0;
        size = 3;
    }

    public Vector(double[] X) {
        size = X.length;
        for (int i = 0; i < size; ++i) {
            x[i] = X[i];
        }
    }

    public Vector(int[] X) {
        size = X.length;
        for (int i = 0; i < size; ++i) {
            x[i] = X[i];
        }
    }

    public Vector(Vector V) {
        size = V.size;
        for (int i = 0; i < size; ++i) {
            x[i] = V.x[i];
        }
    }

    public Vector(int s) {
        this.size = s;
        for (int i = 0; i < size; ++i) {
            this.x[i] = 0;
        }
    }

    public Vector(LongVector V) {
        for (int i = 0; i < size; ++i) {
            x[i] = V.x[i];
        }
    }

    //checks is V2 is between V1 and V3
    public int between(Vector V1, Vector V2, Vector V3) {
        double d1 = dist(V1, V2);
        double d2 = dist(V2, V3);
        double d3 = dist(V1, V3);
        if (d1 + d2 - d3 < .0000001) {
            return (1);
        }
        return (0);
    }

    public Vector normalize() {
        Vector W = new Vector();
        W.x[0] = this.x[0] / this.x[2];
        W.x[1] = this.x[1] / this.x[2];
        W.x[2] = 1;
        W.size = 3;
        return (W);
    }

    /**
     * a random vector
     */
    public static Vector random(int s) {
        Vector m = new Vector();
        m.size = s;
        for (int i = 0; i < s; ++i) {
            m.x[i] = Math.random();
        }
        return (m);
    }

    public static Vector random2(int s) {
        Vector m = new Vector();
        m.size = s;
        for (int i = 0; i < s; ++i) {
            m.x[i] = Math.random() - .5;
        }
        return (m);
    }

    /**
     * A random vector with integer entries between -N and N
     */
    public static Vector randomInt(int s, int N) {
        Vector m = random(s);
        for (int i = 0; i < s; ++i) {
            m.x[i] = Math.floor(2 * N * m.x[i] - N);
        }
        return (m);
    }

    /**
     * We should have q0<q1
     */
    public Vector coordinateSwap(int q0, int q1) {

        if (q1 <= q0) {
            return (null);
        }

        Vector V = new Vector(this);
        if ((q0 == 0) && (q1 == 1)) {
            return (V);
        }

        if (q0 == 0) {
            V.x[1] = x[q1];
            V.x[q1] = x[1];
            return (V);
        }
        if (q0 == 1) {
            V.x[0] = x[q1];
            V.x[q1] = x[0];
            return (V);
        }
        V.x[0] = this.x[q0];
        V.x[1] = this.x[q1];
        V.x[q0] = this.x[0];
        V.x[q1] = this.x[1];
        V.size = 3;
        return (V);
    }

    /**
     * Here are some routines specially for 3D vectors
     */
    public static Vector cross(Vector v, Vector w) {
        if (v.size != 3) {
            return (null);
        }
        if (w.size != 3) {
            return (null);
        }
        Vector X = new Vector();
        X.x[0] = v.x[1] * w.x[2] - w.x[1] * v.x[2];
        X.x[1] = v.x[2] * w.x[0] - w.x[2] * v.x[0];
        X.x[2] = v.x[0] * w.x[1] - w.x[0] * v.x[1];
        X.size = 3;
        return (X);
    }

    public static Vector findCross(Vector v1, Vector v2, Vector v3, Vector v4) {
        if (v1.size != 3) {
            return (null);
        }
        if (v2.size != 3) {
            return (null);
        }
        if (v3.size != 3) {
            return (null);
        }
        if (v4.size != 3) {
            return (null);
        }
        Vector v5 = cross(v1, v2);
        Vector v6 = cross(v3, v4);
        Vector v7 = cross(v5, v6);
        v7.size = 3;
        return (v7);
    }

    /**
     * Here are some general routines*
     */
    public static Vector plus(Vector v1, Vector v2) {
        if (v1.size != v2.size) {
            return (null);
        }
        Vector w = new Vector();
        for (int i = 0; i < v1.size; ++i) {
            w.x[i] = v1.x[i] + v2.x[i];
        }
        w.size = v1.size;
        return (w);
    }

    public static Vector minus(Vector v1, Vector v2) {
        if (v1.size != v2.size) {
            return (null);
        }
        Vector w = new Vector();
        for (int i = 0; i < v1.size; ++i) {
            w.x[i] = v1.x[i] - v2.x[i];
        }
        w.size = v1.size;
        return (w);
    }

    public static Vector scale(double r, Vector v) {
        Vector w = new Vector();
        for (int i = 0; i < v.size; ++i) {
            w.x[i] = r * v.x[i];
        }
        w.size = v.size;
        return (w);
    }

    public Vector scale(double r) {
        return (scale(r, this));
    }

    public static Vector interpolate(double s, Vector V1, Vector V2) {
        Vector W1 = scale((1 - s), V1);
        Vector W2 = scale(s, V2);
        Vector W3 = plus(W1, W2);
        W3.size = 3;
        return (W3);
    }

    public static double dot(Vector v, Vector w) {
        int s = v.size;
        double d = 0;
        for (int i = 0; i < s; ++i) {
            d = d + v.x[i] * w.x[i];
        }
        return (d);
    }

    public double norm() {
        return (Math.sqrt(dot(this, this)));
    }

    public double norm2() {
        double s = 0;
        for (int i = 0; i < size; ++i) {
            double t = Math.abs(x[i]);
            if (s < t) {
                s = t;
            }
        }
        return (s);
    }

    public Vector unit() {
        double t = this.norm();
        t = Math.sqrt(t);
        Vector V = Vector.scale(1 / t, this);
        V.size = 3;
        return (V);
    }

    public static double dist(Vector v, Vector w) {
        Vector x = minus(v, w);
        return (x.norm());
    }

    /**
     * This does Gram schmidt to a pair of vectors*
     */
    public static Vector[] ortho(Vector[] V) {
        Vector[] W = new Vector[2];
        W[0] = new Vector(V[0]);
        double x = W[0].norm();
        W[0] = scale(1 / x, W[0]);
        W[1] = new Vector(V[1]);
        W[1] = minus(W[1], scale(dot(W[0], W[1]), W[0]));
        return (W);
    }

    /**
     * This takes the minimum of two vectors
     */
    public static Vector min(Vector V1, Vector V2) {
        if (V1.size != V2.size) {
            return (null);
        }
        Vector W = new Vector(V1.size);
        for (int i = 0; i < V1.size; ++i) {
            W.x[i] = Math.min(V1.x[i], V2.x[i]);
        }
        return (W);
    }

    /**
     * This takes the maximum of two vectors
     */
    public static Vector max(Vector V1, Vector V2) {
        if (V1.size != V2.size) {
            return (null);
        }
        Vector W = new Vector(V1.size);
        for (int i = 0; i < V1.size; ++i) {
            W.x[i] = Math.max(V1.x[i], V2.x[i]);
        }
        return (W);
    }

    public void print() {
        for (int i = 0; i < size; ++i) {
            double d = nearInt(x[i]);
            System.out.print(d + " ");
        }
        System.out.println("");
    }

    public static double nearInt(double s) {
        for (int i = -1000; i < 1000; ++i) {
            if (Math.abs(s - i) < .000000000001) {
                return (i);
            }
        }
        return (s);
    }

    public static double latticeDist(Vector V) {
        double max = 0;
        for (int i = 0; i < 4; ++i) {
            double test = latticeDist(V.x[i]);
            if (max < test) {
                max = test;
            }
        }
        return (max);
    }

    public static double latticeDist(double x) {
        double min = 1;
        for (int i = -10; i < 10; ++i) {
            double test = Math.abs(x - i);
            if (test < min) {
                min = test;
            }
        }
        return (min);
    }

    public Complex toComplex() {
        Complex z = new Complex(x[0], x[1]);
        return (z);
    }

    public static double tripleProduct(Vector v1, Vector v2, Vector v3) {
        return (dot(cross(v1, v2), v3));
    }

    public static double tripleProduct2(Complex z1, Complex z2, Complex z3) {
        Vector v1 = new Vector(z1);
        Vector v2 = new Vector(z2);
        Vector v3 = new Vector(z3);
        return (tripleProduct(v1, v2, v3));
    }

    public static Complex findCross2(Complex z1, Complex z2, Complex z3, Complex z4) {
        Vector v1 = new Vector(z1);
        Vector v2 = new Vector(z2);
        Vector v3 = new Vector(z3);
        Vector v4 = new Vector(z4);
        Vector v5 = findCross(v1, v2, v3, v4);
        return (v5.toComplex());
    }

}
