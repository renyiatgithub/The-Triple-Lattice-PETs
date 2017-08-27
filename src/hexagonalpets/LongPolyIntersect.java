package hexagonalpets;

public class LongPolyIntersect {

    public static LongPolyhedron main(LongPolyhedron P1, LongPolyhedron P2) {
        P1 = isnull(P1);
        P2 = isnull(P2);
        LongPolyhedron Q = new LongPolyhedron();
        Q.count = 0;
        if (P1 != null & P2 != null) {
            for (int i = 0; i < P1.count; i++) {
                LongVector d = new LongVector(P1.V[i]);
                if (Q.onList(d) == false) {
                    if (LongPolyCombinatorics.inside(d, P2)) {
                        Q.V[Q.count] = new LongVector(d);
                        Q.count++;
                    }
                }
            }
            for (int i = 0; i < P2.count; i++) {
                LongVector d = new LongVector(P2.V[i]);
                if (Q.onList(d) == false) {
                    if (LongPolyCombinatorics.inside(d, P1)) {
                        Q.V[Q.count] = new LongVector(d);
                        Q.count++;
                    }
                }
            }
            int t1 = P1.faceNumber();
            int t2 = P2.faceNumber();
            for (int i1 = 0; i1 < t1; i1++) {
                for (int j1 = i1 + 1; j1 < t1; j1++) {
                    for (int i2 = 0; i2 < t2; i2++) {
                        double[] a1 = P1.toFaceFunctional(i1);
                        double[] a2 = P1.toFaceFunctional(j1);
                        double[] a3 = P2.toFaceFunctional(i2);
                        LongVector d = LongPolyhedron.intersect(a1, a2, a3);
                        if (d != null) {
                            if (Q.onList(d) == false) {
                                if (LongPolyCombinatorics.inside(d, P1)) {
                                    if (LongPolyCombinatorics.inside(d, P2)) {
                                        Q.V[Q.count] = new LongVector(d);
                                        Q.count++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            t1 = P2.faceNumber();
            t2 = P1.faceNumber();
            for (int i1 = 0; i1 < t1; i1++) {
                for (int j1 = i1 + 1; j1 < t1; j1++) {
                    for (int i2 = 0; i2 < t2; i2++) {
                        double[] a1 = P2.toFaceFunctional(i1);
                        double[] a2 = P2.toFaceFunctional(j1);
                        double[] a3 = P1.toFaceFunctional(i2);
                        LongVector d = LongPolyhedron.intersect(a1, a2, a3);
                        if (d != null) {
                            if (Q.onList(d) == false) {
                                if (LongPolyCombinatorics.inside(d, P1)) {
                                    if (LongPolyCombinatorics.inside(d, P2)) {
                                        Q.V[Q.count] = new LongVector(d);
                                        Q.count++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (Q.count > 12) {
                System.out.println("number of vertices out of bounds");
                return (null);
            } else {
                Q.FACE = LongPolyFace.faceList(Q);
                Q.VOLUME = Volume.volume12(0, Q);
                return (isnull(Q));
            }
        } else {
            return (null);
        }
    }

    public static LongPolyhedron chop(LongPolyhedron P0, int a, int b, int m) {
        LongPolyhedron P = new LongPolyhedron(P0);
        P.SCALE = 1.0;
        P.FACE = LongPolyFace.faceList(P);
        LongPolyhedron Q = new LongPolyhedron();
        Q.count = 0;

        if (m == 0) {
            for (int i = 0; i < P.count; i++) {
                LongVector d = new LongVector(P.V[i]);
                if (Q.onList(d) == false) {
                    if (d.x[2] >= 1.0 * a / b * P.SCALE) {
                        Q.V[Q.count] = new LongVector(d);
                        Q.count++;
                    }
                }
            }
        }

        if (m == 1) {
            for (int i = 0; i < P.count; i++) {
                LongVector d = new LongVector(P.V[i]);
                if (Q.onList(d) == false) {
                    if (1.0 * b * d.x[2] <= 1.0 * a * P.SCALE) {
                        Q.V[Q.count] = new LongVector(d);
                        Q.count++;
                    }
                }
            }
        }

        int t1 = P.faceNumber();
        for (int i1 = 0; i1 < t1; i1++) {
            for (int j1 = i1 + 1; j1 < t1; j1++) {
                double[] a1 = P.toFaceFunctional(i1);
                double[] a2 = P.toFaceFunctional(j1);
                double[] a3 = {0, 0, 1.0 * b, a};

                LongVector d = LongPolyhedron.intersect(a1, a2, a3);
                if (d != null) {
                    if (Q.onList(d) == false) {
                        if (LongPolyCombinatorics.inside(d, P) == true) {
                            Q.V[Q.count] = new LongVector(d);
                            Q.count++;
                        }
                    }
                }
            }
        }

        Q.FACE = LongPolyFace.faceList(Q);
        Q.VOLUME = Volume.volume12(0, Q);
        Q.trans = P0.trans;
        return (isnull(Q));
    }

    public static LongPolyhedron isnull(LongPolyhedron P) {
        LongPolyhedron W = new LongPolyhedron(P);
        if (W.count < 4) {
            System.out.println("number of vertices < 4");
            return (null);
        }
        W.FACE = LongPolyFace.faceList(W);
        if (LongPolyFace.isCoplanar(W) == true) {
            System.out.println("coplanar");
            return (null);
        }
        if (W.FACE.length < 4) {
            System.out.println("number of faces < 4");
            return (null);
        }
        W.VOLUME = Volume.volume12(0, W);
        if (W.VOLUME < Math.pow(10, -14)) {
            System.out.println("degenerate case");
            return (null);
        }
        return (W);
    }

    public static boolean PolycheckBad(LongPolyhedron P, LongVector d) {
        int v = P.count;
        boolean test = true;
        for (int i = 0; i < v; i++) {
            for (int j = i + 1; j < v; j++) {
                test = checkBad(P, i, j, d);
                if (test == true) {
                    return (true);
                }
            }
        }
        return (false);
    }

    public static boolean checkBad(LongPolyhedron P, int i, int j, LongVector vk) {
        LongVector[] V = {P.V[i], P.V[j], vk};
        LongVector X = LongVector.normal(V);

        double t = LongVector.dot(X, X);
        if (t > 0) {
            return (false);
        }
        Vector V0 = new Vector(V[0]);
        Vector V1 = new Vector(V[1]);
        Vector V2 = new Vector(V[2]);

        double d01 = Vector.dist(V0, V1);
        double d12 = Vector.dist(V1, V2);
        double d20 = Vector.dist(V2, V0);

        if (Math.abs(d20 + d01 - d12) < Math.pow(10, -8)) {
            return (true);
        }
        if (Math.abs(d01 + d12 - d20) < Math.pow(10, -8)) {
            return (true);
        }
        return (false);
    }

}
