package hexagonalpets;

import java.util.ArrayList;
import java.util.List;

public class FiberBundleMap {

    //There are two types of moves: move a LongPolyhdron by a fixed vector, 
    //or move a LongPolyhedron with a vector of flexible length
    public static LongPolyhedron move(LongPolyhedron P, Complex v, int mode) {
        LongPolyhedron Q = new LongPolyhedron(P);
        if (mode == 0) {
            for (int i = 0; i < Q.count; i++) {
                Q.V[i].x[0] = Q.V[i].x[0] + v.x;
                Q.V[i].x[1] = Q.V[i].x[1] + v.y;
            }
        }
        if (mode == 1) {
            for (int i = 0; i < Q.count; i++) {
                double s = Q.V[i].x[2];
                Q.V[i].x[0] = Q.V[i].x[0] + 0.5 * s * v.x;
                Q.V[i].x[1] = Q.V[i].x[1] + 0.5 * s * v.y;
            }
        }
        Q.FACE = P.FACE;
        return (Q);
    }

    public static LongPolyhedron move(LongPolyhedron P, Complex v, int n,
            int mode) {
        LongPolyhedron Q = new LongPolyhedron(P);
        if (mode == 0) {
            for (int i = 0; i < Q.count; i++) {
                Q.V[i].x[0] = Q.V[i].x[0] + n * v.x;
                Q.V[i].x[1] = Q.V[i].x[1] + n * v.y;
            }
        }
        if (mode == 1) {
            for (int i = 0; i < Q.count; i++) {
                double s = Q.V[i].x[2];
                Q.V[i].x[0] = Q.V[i].x[0] + n * 0.5 * s * v.x;
                Q.V[i].x[1] = Q.V[i].x[1] + n * 0.5 * s * v.y;
            }
        }
        return (Q);
    }

    public static List<LongPolyhedron> move_lattice(LongPolyhedron P,
            Complex[] lattice) {
        List<LongPolyhedron> tiling = new ArrayList<LongPolyhedron>();
        for (int i = -10; i < 10; i++) {
            LongPolyhedron P0 = move(P, lattice[0], i, 0);
            for (int j = -10; j < 10; j++) {
                LongPolyhedron P1 = move(P0, lattice[1], j, 1);
                tiling.add(P1);
            }
            tiling.add(P0);
        }
        return tiling;
    }

    public static List<LongPolyhedron> move_intersect(LongPolyhedron P,
            LongPolyhedron Q, Complex[] lattice, int mode) {
        List<LongPolyhedron> tiling = new ArrayList<LongPolyhedron>();

        for (int i = 1; i < 3; i++) {
            LongPolyhedron P0 = move(P, lattice[0], i, 0);
            for (int j = -5; j < 5; j++) {
                LongPolyhedron P1 = move(P0, lattice[1], j, 1);
                if (mode == 0) {
                    tiling.add(P1);
                }
                if (mode == 1) {
                    LongPolyhedron Q1 = new LongPolyhedron();
                    try {
                        Q1 = LongPolyIntersect.main(Q, P1);
                        if (Q1 != null) {
                            Q1.VECTOR = new Complex[2];
                            Q1.VECTOR[0] = new Complex(lattice[0].x * i, lattice[0].y * i);
                            Q1.VECTOR[1] = new Complex(lattice[1].x * j, lattice[1].y * j);
                            Q1.FACE = LongPolyFace.faceList(Q1);
                            tiling.add(Q1);

                        }
                    } catch (NullPointerException ne) {
                    }
                }
                if (mode == 2) {
                    LongPolyhedron Q1 = new LongPolyhedron(Q);
                    try {
                        Q1 = LongPolyIntersect.main(Q1, P1);
                        if (Q1 != null) {
                            tiling.add(P1);
                        }
                    } catch (NullPointerException ne) {
                    }
                }
            }
        }
        return tiling;
    }

    public static List<LongPolyhedron> preimage(List<LongPolyhedron> PL,
            Complex[] lattice) {
        List<LongPolyhedron> QL = new ArrayList<LongPolyhedron>();
        for (int i = 0; i < PL.size(); i++) {
            LongPolyhedron Q = new LongPolyhedron(PL.get(i));
            Q = move(Q, Q.VECTOR[0], -1, 0);
            Q = move(Q, Q.VECTOR[1], -1, 1);
            Q.VECTOR = PL.get(i).VECTOR;
            Q.VOLUME = Volume.volume12(0, Q);
            QL.add(Q);

        }
        return (QL);
    }

    public static List<LongPolyhedron> preimage_lattice(List<LongPolyhedron> PL,
            List<LongPolyhedron> WL) {
        List<LongPolyhedron> QL = new ArrayList<LongPolyhedron>();
        for (int i = 0; i < PL.size(); i++) {
            try {
                LongPolyhedron Q = new LongPolyhedron(PL.get(i));
                LongVector q = Q.center();
                for (int j = 0; j < WL.size(); j++) {
                    LongPolyhedron W = new LongPolyhedron(WL.get(j));
                    if (LongPolyCombinatorics.inside(q, W)) {
                        Q = move(Q, W.VECTOR[0], -1, 0);
                        Q = move(Q, W.VECTOR[1], -1, 1);
                        Q.VECTOR = PL.get(i).VECTOR;
                        Q.VOLUME = Volume.volume12(0, Q);
                        QL.add(Q);

                    }
                }
            } catch (NullPointerException e) {

            }
        }
        return (QL);
    }

    public static List<LongPolyhedron> intersect_list(List<LongPolyhedron> PL,
            List<LongPolyhedron> QL) {
        List<LongPolyhedron> WL = new ArrayList<LongPolyhedron>();
        for (int i = 0; i < PL.size(); i++) {
            LongPolyhedron P = new LongPolyhedron(PL.get(i));
            for (int j = 0; j < QL.size(); j++) {
                LongPolyhedron Q = new LongPolyhedron(QL.get(j));
                LongPolyhedron W = new LongPolyhedron();
                try {
                    if (LongPolyCombinatorics.isSubset(P, Q) == false
                            && LongPolyCombinatorics.isSubset(Q, P) == false) {
                        W = LongPolyIntersect.main(P, Q);
                    }
                    if (LongPolyCombinatorics.isSubset(P, Q)) {
                        W = new LongPolyhedron(P);

                    }
                    if (LongPolyCombinatorics.isSubset(Q, P)) {
                        W = new LongPolyhedron(Q);
                    }
                    W = LongPolyIntersect.isnull(W);
                    if (W != null) {
                        W.VECTOR = P.VECTOR;
                        WL.add(W);
                    }
                } catch (NullPointerException e) {
                }
            }
        }
        return (WL);
    }

    public static List<LongPolyhedron> mapall(List<LongPolyhedron> PL,
            List<LongPolyhedron> QL) {
        List<LongPolyhedron> WL = new ArrayList<LongPolyhedron>();
        for (int i = 0; i < PL.size(); i++) {
            LongPolyhedron P = new LongPolyhedron(PL.get(i));
            LongVector v = P.center();
            for (int j = 0; j < QL.size(); j++) {
                LongPolyhedron Q = new LongPolyhedron(QL.get(j));
                Q.FACE = LongPolyFace.faceList((Q));
                if (LongPolyCombinatorics.inside(v, Q)) {
                    LongPolyhedron W = new LongPolyhedron(P);
                    W = move(W, Q.VECTOR[0], 1, 0);
                    W = move(W, Q.VECTOR[1], 1, 1);
                    WL.add(W);
                    break;
                }
            }
        }
        return (WL);
    }

    public static double[] coding(LongPolyhedron P) {
        double s0 = P.V[0].x[2];
        double s1 = P.V[P.count - 1].x[2];
        PolygonWrapper X0 = LongPolyReturnMap.TakeSlice(P, s0);
        LongVector x0 = new LongVector(X0.center().x, X0.center().y, s0);
        LongVector y0 = new LongVector(0, 0, s0);

        PolygonWrapper X1 = LongPolyReturnMap.TakeSlice(P, s1);
        LongVector x1 = new LongVector(X1.center().x, X1.center().y, s1);
        LongVector y1 = new LongVector(0, 0, s1);

        double[] code = new double[4];
        code[0] = 1.0 * (y1.x[0] - y0.x[0] - x1.x[0] + x0.x[0]) / (x1.x[2] - x0.x[2]);
        code[1] = y0.x[0] - x0.x[0] - code[0] * x0.x[2];
        code[2] = 1.0 * (y1.x[1] - y0.x[1] - x1.x[1] + x0.x[1]) / (x1.x[2] - x0.x[2]);
        code[3] = y0.x[1] - x0.x[1] - code[2] * x0.x[2];

        return (code);
    }

    public static double[] coding(LongPolyhedron dd, LongPolyhedron gg) {
        double[] code = new double[4];
        LongVector x0 = dd.V[0];
        LongVector y0 = gg.V[0];
        LongVector x1 = dd.V[dd.count - 1];
        LongVector y1 = gg.V[gg.count - 1];

        code[0] = 1.0 * (y1.x[0] - y0.x[0] - x1.x[0] + x0.x[0]) / (x1.x[2] - x0.x[2]);
        code[1] = y0.x[0] - x0.x[0] - code[0] * x0.x[2];
        code[2] = 1.0 * (y1.x[1] - y0.x[1] - x1.x[1] + x0.x[1]) / (x1.x[2] - x0.x[2]);
        code[3] = y0.x[1] - x0.x[1] - code[2] * x0.x[2];

        dd.trans = code;
        return(code);
    }

    public static List<double[]> coding(List<LongPolyhedron> domain, List<LongPolyhedron> image) {
        List<double[]> codes = new ArrayList<double[]>();
        for (int i = 0; i < domain.size(); i++) {
            LongPolyhedron dd = domain.get(i);
            LongPolyhedron gg = image.get(i);
            double[] code = new double[4];
            LongVector x0 = dd.V[0];
            LongVector y0 = gg.V[0];
            LongVector x1 = dd.V[dd.count - 1];
            LongVector y1 = gg.V[gg.count - 1];

            code[0] = 1.0 * (y1.x[0] - y0.x[0] - x1.x[0] + x0.x[0]) / (x1.x[2] - x0.x[2]);
            code[1] = y0.x[0] - x0.x[0] - code[0] * x0.x[2];
            code[2] = 1.0 * (y1.x[1] - y0.x[1] - x1.x[1] + x0.x[1]) / (x1.x[2] - x0.x[2]);
            code[3] = y0.x[1] - x0.x[1] - code[2] * x0.x[2];

            domain.get(i).trans = code;
            codes.add(code);
        }
        return (codes);
    }

}
