package hexagonalpets;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;
import java.io.*;

public class PolyhedronSlicer {

    public PolyhedronSlicer() {
    }

    /**
     * This slices the polyhedron by the X-Y plane*
     */
    public static PolygonWrapper basicSlice(Polyhedron X, double t) {
        Vector U1 = new Vector(1, 0, 0);
        Vector U2 = new Vector(0, 1, 0);
        Vector POS = new Vector(0, 0, t);
        return (slice(U1, U2, POS, X));
    }

    /**
     * This is the main routine.
     */
    public static PolygonWrapper slice(Vector U1, Vector U2, Vector POS, Polyhedron X) {
        int count = 0;
        PolygonWrapper P = new PolygonWrapper();
        for (int i = 0; i < X.count; ++i) {
            for (int j = 0; j < X.count; ++j) {
                Complex w = slice(U1, U2, POS, X, i, j);
                if (w != null) {
                    P.z[count] = new Complex(w);
                    ++count;
                }
            }
        }
        P.count = count;
        if (count == 0) {
            return (null);
        }
        P = PolygonWrapper.cheapHull(P);
        try {
            P = P.trim();
        } catch (Exception e) {
        }
        return (P);
    }

    /**
     * This considers a pair of vectors in a polyhedron
     */
    public static Complex slice(Vector U1, Vector U2, Vector POS, Polyhedron X, int i, int j) {
        Vector V1 = X.V[i];
        Vector V2 = X.V[j];
        Vector DIR = Vector.cross(U1, U2);
        double t = getWeight(DIR, POS, V1, V2);
        if ((t >= 0) && (t <= 1)) {
            Complex w = project(U1, U2, POS, V1, V2, t);
            return (w);
        }
        return (null);
    }

    /**
     * We consider an affine embedding of the plane into R^3. When trans==true,
     * the map is
     *
     * (0,0) to POS (1,0) to (U1-POS) (0,1) to (U2-POS)
     *
     * When trans==false, the map is
     *
     * (0,0) to (0,0,0) (1,0) to U1 (0,1) to U2
     *
     * The vector W is assumed to lie in the image. We pull back W under the
     * map, and this is our point.*
     */
    public static Complex project(Vector U1, Vector U2, Vector POS, Vector W) {
        Vector U3 = Vector.cross(U1, U2);
        Matrix M = new Matrix(U1, U2, U3);
        M = M.inverse();
        M = M.transpose();
        Vector X = Matrix.act(M, W);
        return (new Complex(X.x[0], X.x[1]));
    }

    /**
     * This routine is like the previous one, except that the two vectors V1 and
     * V2 lie on either side of the plane in question, and the number t tells us
     * which linear combination makes the vectors lie in the plane.
     */
    public static Complex project(Vector U1, Vector U2, Vector POS, Vector V1, Vector V2, double t) {
        Vector W = Vector.plus(V1.scale(t), V2.scale(1 - t));
        return (project(U1, U2, POS, W));
    }

    /**
     * This routine finds where an edge crosses the plane given by the equation
     *
     * (p - POS) . DIR = 0
     *
     *
     */
    public static double getWeight(Vector DIR, Vector POS, Vector V1, Vector V2) {
        double z = Vector.dot(POS, DIR);
        double z1 = Vector.dot(V1, DIR);
        double z2 = Vector.dot(V2, DIR);
        if ((z1 < z) && (z2 < z)) {
            return (-1);
        }
        if ((z1 > z) && (z2 > z)) {
            return (-1);
        }
        if (Math.abs(z1 - z2)<Math.pow(10, -10)) {
            return (1);
        }
        double t = Math.abs((z - z1) / (z2 - z1));
        t = 1 - t;
        if (t < 0) {
            t = 0;
        }
        return (t);
    }

}
