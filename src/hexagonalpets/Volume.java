package hexagonalpets;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author renyi
 */
public class Volume {

    public static double volume12(int q, LongPolyhedron P) {
        LongVector V0 = P.V[q];
        LongVector V = new LongVector(V0);
        double total = 0;
        P.FACE = LongPolyFace.faceList(P);
        int num_face = P.FACE.length;
        
        for (int i = 0; i < num_face; ++i) {
            LongVector[] W0 = P.face(i);
            LongVector[] W = new LongVector[W0.length];
            for (int j = 0; j < W0.length; ++j) {
                W[j] = new LongVector(W0[j]);
            }

            if (W.length == 3) {
                total = total + volumePrism3(V, W);
            }
            if (W.length == 4) {
                total = total + volumePrism4(V, W);
            }
            if (W.length == 5) {
                total = total + volumePrism5(V, W);
            }
            if (W.length == 6) {
                total = total + volumePrism6(V, W);
            }
        }
        return (total);
    }

    public static double volumePrism3(LongVector V, LongVector[] W) {
        LongVector[] X = new LongVector[3];
        for (int i = 0; i < 3; ++i) {
            X[i] = LongVector.minus(W[i], V);
        }
        double t = LongVector.dot(X[0], LongVector.cross(X[1], X[2]));
        if (t < 0) {
            t = -t;
        }
        return (t * 2);
    }

    public static double volumePrism4(LongVector V, LongVector[] W) {
        LongVector[] X = new LongVector[4];
        for (int i = 0; i < 4; ++i) {
            X[i] = LongVector.minus(W[i], V);
        }
        double t1 = LongVector.dot(X[0], LongVector.cross(X[1], X[2]));
        double t2 = LongVector.dot(X[0], LongVector.cross(X[1], X[3]));
        double t3 = LongVector.dot(X[0], LongVector.cross(X[2], X[3]));
        double t4 = LongVector.dot(X[1], LongVector.cross(X[2], X[3]));
        double[] t = {t1, t2, t3, t4};
        for (int i = 0; i < 4; ++i) {
            if (t[i] < 0) {
                t[i] = -t[i];
            }
        }
        double u = t[0] + t[1] + t[2] + t[3];
        return (u);
    }

    public static double volumePrism5(LongVector V, LongVector[] W) {
        double vol = 0;

            LongPolyhedron Q0 = new LongPolyhedron();
            Q0.count = 5;
            for (int i = 0; i < 4; i++) {
                Q0.V[i] = new LongVector(W[i]);
            }
            Q0.V[4] = new LongVector(V);
            Q0.FACE = LongPolyFace.faceList(Q0);
            LongVector[] W0 = new LongVector[4];
            for (int i = 0; i < 4; i++) {
                W0[i] = new LongVector(W[i]);
            }

            LongPolyhedron Q1 = new LongPolyhedron();
            Q1.count = 4;
            Q1.V[0] = new LongVector(W[4]);
            Q1.V[1] = new LongVector(V);
            for (int j = 0; j < 4; j++) {
                for (int k = j + 1; k < 4; k++) {
                    Q1.V[2] = new LongVector(W[j]);
                    Q1.V[3] = new LongVector(W[k]);
                    
                    Q1.FACE = LongPolyFace.faceList(Q1);
                    LongPolyhedron IntQ = LongPolyIntersect.main(Q0, Q1);
                    if (IntQ == null) {
                        LongVector[] W1 = new LongVector[3];
                        W1[0] = new LongVector(W[4]);
                        W1[1] = new LongVector(W[j]);
                        W1[2] = new LongVector(W[k]);
                        vol = volumePrism4(V, W0) + volumePrism3(V, W1);
                        return (vol);
                    }
                }
            
        }
        return (vol);
    }

    public static double volumePrism6(LongVector V, LongVector[] W) {
        double vol = 0;
        
        LongPolyhedron Q0 = new LongPolyhedron();
        Q0.count = 5;
        for (int i = 0; i < 4; i++) {
            Q0.V[i] = new LongVector(W[i]);
        }
        Q0.V[4] = new LongVector(V);
        Q0.FACE = LongPolyFace.faceList(Q0);
        LongVector[] W0 = new LongVector[4];
        for (int i = 0; i < 4; i++) {
            W0[i] = new LongVector(W[i]);
        }

        LongPolyhedron Q1 = new LongPolyhedron();
        Q1.count = 5;
        Q1.V[0] = new LongVector(V);
        Q1.V[1] = new LongVector(W[5]);
        Q1.V[2] = new LongVector(W[4]);
        for (int j = 0; j < 4; ++j) {
            for (int k = j + 1; k < 4; ++k) {
                Q1.V[3] = new LongVector(W[j]);
                Q1.V[4] = new LongVector(W[k]);
                Q1.FACE = LongPolyFace.faceList(Q1);
                LongPolyhedron IntQ = LongPolyIntersect.main(Q0, Q1);
                if (IntQ == null){
                    LongVector[] W1 = new LongVector[4];
                    W1[0] = new LongVector(W[5]);
                    W1[1] = new LongVector(W[j]);
                    W1[2] = new LongVector(W[k]);
                    W1[3] = new LongVector(W[4]);
                    vol = volumePrism4(V, W0) + volumePrism4(V, W1);
                    return (vol);
                }
            }        
        }
        return (vol);
    }

}
