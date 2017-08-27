package hexagonalpets;

public class BasicLongPolyhedron {

    public static LongPolyhedron move(LongPolyhedron P, LongVector v) {
        LongPolyhedron Q = new LongPolyhedron(P);
        for (int i = 0; i < Q.count; i++) {
            Q.V[i] = LongVector.plus(Q.V[i], v);
        }
        return Q;
    }

    public static LongPolyhedron reflt_line(LongPolyhedron P, Complex line) {
        LongPolyhedron Q = new LongPolyhedron(P);
        for (int i = 0; i < Q.count; i++) {
            Q.V[i] = BasicGeometry.pt_reflt_line(Q.V[i], line);
        }

        return Q;
    }

    public static LongVector Rotation(LongVector V, Complex c, double theta) {
        LongVector W = new LongVector(V);
        W.x[0] = (V.x[0] - c.x) * Math.cos(theta)
                - (V.x[1] - c.y) * Math.sin(theta) + c.x;
        W.x[1] = (V.x[0] - c.x) * Math.sin(theta)
                + (V.x[1] - c.y) * Math.cos(theta) + c.y;
        return (W);
    }

    public static LongPolyhedron Rotation(LongPolyhedron P, Complex c, double theta) {
        LongPolyhedron W = new LongPolyhedron(P);
        for (int i = 0; i < W.count; i++) {
            W.V[i].x[1] = W.V[i].x[1] * Math.sqrt(3);
            W.V[i] = Rotation(W.V[i], c, theta);
            W.V[i].x[1] = W.V[i].x[1] / Math.sqrt(3);
        }
        return (W);
    }
   
    public static LongPolyhedron Flip(LongPolyhedron P, Complex c){
        LongPolyhedron W = new LongPolyhedron(P);
        for(int i=0; i<W.count; i++){
            W.V[i].x[0] = 2 * c.x - W.V[i].x[0];
        }
        return(W);
    }
}
