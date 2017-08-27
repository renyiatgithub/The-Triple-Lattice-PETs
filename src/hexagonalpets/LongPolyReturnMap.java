package hexagonalpets;

import java.util.ArrayList;
import java.util.List;

public class LongPolyReturnMap {

    public static LongPolyhedron ReturnDomain(List<Integer> index,
            LongVector z, List<LongPolyhedron> PList, LongPolyhedron H) {
        int length = index.size();
        if (length > 1) {
            int r = index.get(0);
            double[] t = {0, 0, 0, 0};
            LongPolyhedron P = LongPolyIntersect.main(PList.get(r), H);

            for (int i = 0; i < length - 1; i++) {
                double[] cd = PList.get(r).trans;
                t = trackplus(t, cd);
                z = BundleMap(z, cd);
                int r1 = index.get(i + 1);
                P = BundleMap(P, cd);
                LongPolyhedron Q = PList.get(r1);
                if (r != r1 & LongPolyCombinatorics.isSubset(P, Q) == false) {
                    LongPolyhedron W = LongPolyIntersect.main(P, Q);
                    if (W != null) {
                        P = W;
                    }
                }
                r = r1;
            }

            P = BundleMap(P, -t[0], -t[1], -t[2], -t[3]);
            P.trans = new double[]{t[0], t[1], t[2], t[3]};
            return (P);
        }
        return (null);
    }

    public static LongPolyhedron ReturnMap(LongPolyhedron P, LongPolyhedron H,
            List<LongPolyhedron> DL) {
        LongPolyhedron W = new LongPolyhedron(P);
        LongVector V = P.center();
        List<Integer> index = trackIndex(V, H, DL);
        double[] t = {0, 0, 0, 0};
        for(int i=0; i<index.size() - 1; i++){
            int r = index.get(i);
            double[] cd = DL.get(r).trans;
            t = trackplus(t,cd);
        }
        W = BundleMap(W, t);
        return(W);
    }

    public static LongPolyhedron preConjugation(LongPolyhedron P,
            LongPolyhedron REF, double theta) {
        LongPolyhedron Q = new LongPolyhedron(P);
        Q = Rotation(Q, REF, theta);
        Q = Flip(Q, REF);
        return (Q);
    }

    public static LongPolyhedron conjugation(LongPolyhedron P,
            LongPolyhedron REF, double theta, double[] cd) {
        LongPolyhedron Q = new LongPolyhedron(P);
        Q = Rotation(Q, REF, theta);
        Q = Flip(Q, REF);
        Q = BundleMap(Q, cd);
        Q = Q.sort();
        return (Q);
    }

    public static LongPolyhedron BundleMap(LongPolyhedron P, double[] code) {
        LongPolyhedron Q = new LongPolyhedron(P);
        for (int i = 0; i < Q.count; i++) {
            double s = Q.V[i].x[2];
            Q.V[i].x[0] = Q.V[i].x[0] + code[0] * s + code[1];
            Q.V[i].x[1] = Q.V[i].x[1] + code[2] * s + code[3];
        }
        Q.FACE = P.FACE;
        return (Q);
    }

    public static LongPolyhedron BundleMap(LongPolyhedron P, double cd0,
            double cd1, double cd2, double cd3) {
        LongPolyhedron Q = new LongPolyhedron(P);
        for (int i = 0; i < Q.count; i++) {
            double s = Q.V[i].x[2];
            Q.V[i].x[0] = Q.V[i].x[0] + cd0 * s + cd1;
            Q.V[i].x[1] = Q.V[i].x[1] + cd2 * s + cd3;
        }
        return (Q);
    }

    public static LongPolyhedron BundleMap0(LongPolyhedron P, double cd0,
            double cd1, double cd2, double cd3) {
        LongPolyhedron Q = new LongPolyhedron(P);
        for (int i = 0; i < Q.count; i++) {
            double s = 0.5 * Q.V[i].x[2];
            Q.V[i].x[0] = Q.V[i].x[0] + cd0 * s + cd1;
            Q.V[i].x[1] = Q.V[i].x[1] + cd2 * s + cd3;
        }
        return (Q);
    }

    public static LongVector BundleMap(LongVector v, double[] code) {
        LongVector w = new LongVector(v);
        double s = w.x[2];
        w.x[0] = w.x[0] + code[0] * s + code[1];
        w.x[1] = w.x[1] + code[2] * s + code[3];
        return (w);
    }

    public static LongVector Rotation(LongVector V, Complex c, double theta) {
        LongVector W = new LongVector();
        W.x[0] = (V.x[0] - c.x) * Math.cos(theta) - (V.x[1] - c.y) * Math.sin(theta) + c.x;
        W.x[1] = (V.x[0] - c.x) * Math.sin(theta) + (V.x[1] - c.y) * Math.cos(theta) + c.y;
        W.x[2] = V.x[2];
        return (W);
    }

    public static LongPolyhedron Rotation(LongPolyhedron P, LongPolyhedron REF0, double theta) {

        LongPolyhedron W = new LongPolyhedron(P);
        W = transferBack(W);
        LongPolyhedron REF = transferBack(REF0);
        for (int i = 0; i < W.count; i++) {
            double s = P.V[i].x[2];
            PolygonWrapper ref = TakeSlice(REF, s);
            W.V[i] = Rotation(W.V[i], ref.center(), theta);
            W.V[i].x[1] = 1.0 * W.V[i].x[1] / Math.sqrt(3);
        }
        return (W);
    }

    public static LongPolyhedron Flip(LongPolyhedron P, LongPolyhedron REF) {
        LongPolyhedron W = new LongPolyhedron(P);
        for (int i = 0; i < W.count; i++) {
            double s = W.V[i].x[2];
            PolygonWrapper ref = TakeSlice(REF, s);
            W.V[i].x[0] = 2 * ref.center().x - W.V[i].x[0];
        }
        return (W);
    }

    public static LongPolyhedron Scale(LongPolyhedron W) {
        LongPolyhedron P = new LongPolyhedron(W);
        for (int i = 0; i < P.count; i++) {
            double s = W.V[i].x[2];
            s = 2.0 / s;
            P.V[i].x[0] = P.V[i].x[0] * s;
            P.V[i].x[1] = P.V[i].x[1] * s;
        }
        return (P);
    }

    public static LongPolyhedron transferBack(LongPolyhedron P) {
        LongPolyhedron W = new LongPolyhedron(P);
        for (int i = 0; i < W.count; i++) {
            W.V[i].x[1] = W.V[i].x[1] * Math.sqrt(3);
        }
        return (W);
    }

    public static List<Integer> trackIndex(LongVector z, LongPolyhedron H,
            List<LongPolyhedron> PList) {
        List<Integer> index_list = new ArrayList<Integer>();

        if (LongPolyCombinatorics.inside(z, H)) {

            int r = region(z, PList);
            index_list.add(r);
            for (int i = 0; i < 500; i++) {
                double[] code = PList.get(r).trans;
                LongVector z1 = BundleMap(z, code);
                int r1 = region(z1, PList);
                index_list.add(r1);
                if (LongPolyCombinatorics.insideStrict(z1, H)) {
                    return (index_list);
                }
                z = z1;
                r = r1;
            }
        }

        return (index_list);
    }

    public static int region(LongVector v, List<LongPolyhedron> PL) {
        int count = -1;
        for (int i = 0; i < PL.size(); i++) {
            if (LongPolyCombinatorics.insideStrict(v, PL.get(i))) {
                count = i;
                return (count);
            }
        }
        if (count == -1) {
            for (int i = 0; i < PL.size(); i++) {
                PolygonWrapper polygon = TakeSlice(PL.get(i), v.x[2]);
                if (polygon != null) {
                    if (PolygonWrapper.contains(polygon, new Complex(v.x[0], v.x[1]))) {
                        count = i;
                        return (count);
                    }
                }
            }
        }
        return (-1);
    }

    public static PolygonWrapper TakeSlice(LongPolyhedron P, double v) {
        P.SCALE = 1;
        Polyhedron PP = P.toPolyhedron();
        PolygonWrapper PW = PolyhedronSlicer.basicSlice(PP, v);
        return (PW);
    }

    public static double[] trackplus(double[] t1, double[] t2) {
        double[] t = new double[4];
        for (int i = 0; i < 4; i++) {
            t[i] = t1[i] + t2[i];
        }
        return (t);
    }

}
