package hexagonalpets;

/**
 * This class extracts the face list from a long polyhedron that is give in
 * terms of vertices*
 */
public class LongPolyCombinatorics {

    /**
     * This routine returns true if and only if the point lies in the (closed)
     * polyhedron
     */
    public static boolean inside(LongVector W, LongPolyhedron P) {
        for (int i = 0; i < P.FACE.length; i++) {
            if (correctSide(W, P, i) == false) {
                return (false);
            }
        }

        return (true);
    }
    

    /**
     * This routine returns true if and only if the point lies in the open
     * polyhedron
     */
    public static boolean insideStrict(LongVector W, LongPolyhedron P) {
        for (int i = 0; i < P.FACE.length; ++i) {
            if (correctSideStrict(W, P, i) == false) {
                return (false);
            }
        }
        return (true);
    }

    /*This routine returns true if a point lines on the same 
     side of a given face as a polyhedron.*/
    public static boolean correctSide(LongVector W, LongPolyhedron P, int f) {
        LongVector[] LIST = P.face(f);
        LongVector X = LongVector.normal(LIST);
        double target = LongVector.dot(X, LIST[0]);
        double test = LongVector.dot(X, W);
        double[] m = range(X, P);
        if ((m[0] >= target) && (test > (target - Math.pow(10, -8)))) {
            return (true);
        }
        if ((m[1] <= target) && (test < (target + Math.pow(10, -8)))) {
            return (true);
        }

        return (false);
    }

    /*This routine returns true if a point lines strictly on the same 
     side of a given face as a polyhedron.*/
    public static boolean correctSideStrict(LongVector W, LongPolyhedron P, int f) {
        LongVector[] LIST = P.face(f);
        LongVector X = LongVector.normal(LIST);

        double target = LongVector.dot(X, LIST[0]);
        double test = LongVector.dot(X, W);
        double[] m = range(X, P);
        if ((m[0] >= target) && (test > target)) {
            return (true);
        }
        if ((m[1] <= target) && (test < target)) {
            return (true);
        }
        return (false);
    }


    /*This routine returns true if the P1 is a subset of P2.*/
    public static boolean isSubset(LongPolyhedron P1, LongPolyhedron P2) {
        for (int i = 0; i < P1.count; ++i) {
            if (inside(P1.V[i], P2) == false) {
                return (false);
            }
        }
        return (true);
    }

    /**
     * This returns true if a simple search algorithm shows that two long
     * polyhedra P and Q have disjoint interiors. The idea is that we search for
     * a vector W such that min P.V[i].W >= max Q.V[i].W
     */
    public static boolean separate(int q, LongPolyhedron P, LongPolyhedron Q) {
        for (long i = -q; i <= q; ++i) {
            for (long j = -q; j <= q; ++j) {
                for (long k = -q; k <= q; ++k) {
                    LongVector W = new LongVector(i, j, k);
                    if (separates(W, P, Q) == true) {
                        return (true);
                    }
                }
            }
        }
        return (false);
    }

    /**
     * This returns true if the vector W is such that min P.V[i].W >= max
     * Q.V[i].W
     */
    public static boolean separates(LongVector W, LongPolyhedron P, LongPolyhedron Q) {
        double z = LongVector.dot(W, W);
        if (z == 0) {
            return (false);
        }
        double max = -10000000000L;
        double min = +10000000000L;

        for (int i = 0; i < P.count; ++i) {
            double test = LongVector.dot(W, P.V[i]);
            if (min > test) {
                min = test;
            }
        }

        for (int i = 0; i < Q.count; ++i) {
            double test = LongVector.dot(W, Q.V[i]);
            if (max < test) {
                max = test;
            }
        }
        if (max <= min) {
            return (true);
        }
        return (false);
    }

    public static double[] range(LongVector W, LongPolyhedron P) {
        double max = -10000000;
        double min = +10000000;

        for (int i = 0; i < P.count; ++i) {
            double test = LongVector.dot(W, P.V[i]);
            if (min > test) {
                min = test;
            }
        }

        for (int i = 0; i < P.count; ++i) {
            double test = LongVector.dot(W, P.V[i]);
            if (max < test) {
                max = test;
            }
        }
        double[] m = {min + Math.pow(10, -8), max - Math.pow(10, -8)};
        return (m);
    }

    public static boolean checkSegment(LongVector w, LongVector v1, LongVector v2) {
        double d1 = LongVector.dist(w, v1);
        double d2 = LongVector.dist(w, v2);
        double d12 = LongVector.dist(v1, v2);
        if (Math.abs(d1 + d2 - d12) < Math.pow(10, -10)) {
            return true;
        }
        System.out.println("check segment false");
        return (false);
    }
    

 
}
