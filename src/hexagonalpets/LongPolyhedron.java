package hexagonalpets;

import java.awt.Color;


/*This is the class of polyhedra we use for our
 exact integer calculations.*/
public class LongPolyhedron {

    LongVector[] V = new LongVector[50];
    int[] MOVE = new int[4];   //arithmetic graph fragment - determines F action
    int[] FACE;              //list of faces
    double SCALE;              //scale factor for the polyhedron
    int count;               //number of vertices
    double VOLUME;
    Complex[] VECTOR = new Complex[2];
    double[] trans = new double[4];
    Color color;

    public LongPolyhedron() {
    }

    public LongPolyhedron(LongPolyhedron P) {
        this.count = P.count;
        for (int i = 0; i < count; ++i) {
            V[i] = new LongVector(P.V[i]);
        }
        this.SCALE = P.SCALE;
        this.FACE = P.FACE;
        this.VECTOR = P.VECTOR;
        this.color = P.color;
        this.trans = P.trans;
        this.VOLUME = P.VOLUME;
    }

    public LongPolyhedron dilate(double k) {
        LongPolyhedron P = new LongPolyhedron();
        P.count = this.count;
        P.SCALE = this.SCALE * k;
        P.FACE = this.FACE;
        for (int i = 0; i < this.count; ++i) {
            P.V[i] = LongVector.scale(k, this.V[i]);
        }
        return (P);
    }

    /*These routines have to do with getting the faces*/
    public int faceNumber() {
        return (this.FACE.length);
    }

    /*This creates a new polyhedron which is the qth face
     of the original polyhedron.*/
    public LongPolyhedron toFace(int q) {
        LongPolyhedron P = new LongPolyhedron();
        LongVector[] LIST = face(q);
        P.count = LIST.length;
        for (int i = 0; i < P.count; ++i) {
            P.V[i] = LIST[i];
        }
        P.FACE = null;
        P.MOVE = null;
        P.SCALE = this.SCALE;
        return (P);
    }

    /**
     * This gets the coefficients A,B,C,D so that all the points in the qth face
     * of the polyhedron satisfy Ax+By+Cz=D.
     */
    public double[] toFaceFunctional(int q) {
        LongPolyhedron P = this.toFace(q);
        double[] X = {0, 0, 0, 0};
        int t = P.count;
        for (int i = 0; i < t; ++i) {
            LongVector v1 = LongVector.minus(P.V[(i + 1) % t], P.V[i]);
            LongVector v2 = LongVector.minus(P.V[(i + 2) % t], P.V[i]);
            LongVector v3 = LongVector.cross(v1, v2);

            if (LongVector.dot(v3, v3) != 0) {
                v3 = LongVector.process(v3);
                double x = LongVector.dot(v3, P.V[i]);
                X = new double[]{v3.x[0], v3.x[1], v3.x[2], x};
                return (X);
            }
        }
        return (X);
    }

    /*Gets the list of vectors in the ith face*/
    public LongVector[] face(int i) {
        int a = FACE[i];
        return (LongPolyFace.getList(this, a));
    }

    public boolean inFace(int i, int j) {
        int a = FACE[j];
        int[] b = Combinatorics.binaryList(count, a);
        for (int k = 0; k < b.length; ++k) {
            if (i == b[k]) {
                return (true);
            }
        }
        return (false);
    }


    /*This routine intersects 3 faces*/
    public static LongVector intersect(double[] a, double[] b, double[] c) {
        LongMatrix m = new LongMatrix();
        LongVector d = new LongVector();
        for (int i = 0; i < 3; ++i) {
            m.a[0][i] = a[i];
            m.a[1][i] = b[i];
            m.a[2][i] = c[i];
        }
        d.x[0] = a[3];
        d.x[1] = b[3];
        d.x[2] = c[3];
        if (m.det() == 0) {
            return (null);
        }
        LongMatrix n = m.inverse();
        d = LongMatrix.act(n, d);
        try {
            d = d.guidedDivide(n.SCALE);
        } catch (ProofException pfe) {

        }
        return (d);
    }

    /**
     * done with routines having to do with faces
     */
    /**
     * Converts an integer list into a polyhedron. Our polyhedra are stored as
     * raw lists of integers.
     */
    /**
     * In this routine, the integer list has the form
     *
     * (p1,q1,p2,q2,p3,q3) * and the resulting vector is
     *
     * (scale p1/q1,scale p2/q2,scale p3/q3)
     */
    public static LongPolyhedron fromIntegerList(long scale, int[][] t) {
        LongPolyhedron P = new LongPolyhedron();
        P.SCALE = scale;
        P.count = t.length;
        for (int i = 0; i < P.count; ++i) {
            P.V[i] = new LongVector();
            for (int j = 0; j < 3; ++j) {
                long a = scale * t[i][2 * j];
                checkDivision(a, t[i][2 * j + 1]);
                a = a / t[i][2 * j + 1];
                P.V[i].x[j] = a;
            }
        }
        return (P);
    }

    /*In this routine the integers are just (a,b,c) and the
     vector is also (a,b,c)*/
    public static LongPolyhedron fromIntegerList(int[][] t) {
        LongPolyhedron P = new LongPolyhedron();
        P.SCALE = 1;
        P.count = t.length;
        for (int i = 0; i < P.count; ++i) {
            P.V[i] = new LongVector();
            for (int j = 0; j < 3; ++j) {
                P.V[i].x[j] = (long) (t[i][j]);
            }
        }
        return (P);
    }

    /*This make sure we are only dividing numbers, as integers,
     when the one numbers goes evenly into the other.*/
    public static void checkDivision(long p, long q) {
        if (p % q != 0) {
            System.out.println(p + " " + q);
            throw new ProofException("division fails");
        }
    }

    public boolean onList(LongVector d) {
        for (int i = 0; i < this.count; ++i) {
            LongVector W = new LongVector(d);
            W = LongVector.minus(W, this.V[i]);
            double a = LongVector.dot(W, W);
            if (a < Math.pow(10, -15)) {
                return (true);
            }
        }
        return (false);
    }

    public LongPolyhedron translate(LongVector d) {
        LongPolyhedron W = new LongPolyhedron(this);
        for (int i = 0; i < this.count; i++) {
            W.V[i] = LongVector.plus(W.V[i], d);
        }
        W.SCALE = this.SCALE;
        W.FACE = this.FACE;
        return (W);
    }

    /*Converts a LongPolyhedron to an ordinary Polyhedron*/
    public Polyhedron toPolyhedron() {
        Polyhedron P = new Polyhedron();
        P.count = this.count;
        for (int i = 0; i < count; ++i) {
            P.V[i] = new Vector();
            P.V[i].size = 3;
            for (int j = 0; j < 3; ++j) {
                P.V[i].x[j] = 1.0 * this.V[i].x[j] / SCALE;
            }
        }
        return (P);
    }

    public void print() {
        System.out.println("----------------");
        System.out.println("long polyhedron");
        System.out.println("vectors");

        for (int i = 0; i < count; ++i) {
            System.out.print(i + ": ");
            int[] f0 = MathRational.approximate(V[i].x[0], Math.pow(10, -14));
            f0 = MathRational.cutoff(f0);
            int[] f1 = MathRational.approximate(V[i].x[1], Math.pow(10, -14));
            f1 = MathRational.cutoff(f1);
            int[] f2 = MathRational.approximate(0.5 * V[i].x[2], Math.pow(10, -14));
            f2 = MathRational.cutoff(f2);
            double x = 1.0 * f0[0] / f0[1];
            double y = 1.0 * f1[0] / f1[1];
            double z = 1.0 * f2[0] / f2[1];
            System.out.print("(" + x + "," + y + "," + z + ")  ");
            System.out.println("(" + f0[0] + "/" + f0[1] + "," + f1[0] + "/" + f1[1] + "," + f2[0] + "/" + f2[1] + ")");
        }
        FACE = LongPolyFace.faceList(this);
        System.out.println("faces");
        try {
            for (int i = 0; i < FACE.length; ++i) {
                int[] t = Combinatorics.binaryList(count, FACE[i]);
                printout(t);
            }
        } catch (Exception e) {
            System.out.println("none listed");
        }

        System.out.println("translation vector: (" + trans[0] + ", " + trans[1]
                + ", " + trans[2] + ", " + trans[3] + ")");
        System.out.println("volume: " + VOLUME);
        System.out.println("----------------");
    }

    
        public void print1() {
        System.out.println("----------------");
        System.out.println("long polyhedron");
        System.out.println("vectors");

        for (int i = 0; i < count; ++i) {
            System.out.print(i + ": ");
            int[] f0 = MathRational.approximate(V[i].x[0] * 0.5 / (V[i].x[2]-1), Math.pow(10, -14));
            f0 = MathRational.cutoff(f0);
            int[] f1 = MathRational.approximate(V[i].x[1]  * 0.5 / (V[i].x[2]-1), Math.pow(10, -14));
            f1 = MathRational.cutoff(f1);
            int[] f2 = MathRational.approximate(V[i].x[2] * 0.5, Math.pow(10, -14));
            f2 = MathRational.cutoff(f2);
            double x = 1.0 * f0[0] / f0[1];
            double y = 1.0 * f1[0] / f1[1];
            double z = 1.0 * f2[0] / f2[1];
            System.out.print("(" + x + "," + y + "," + z + ")  ");
            System.out.println("(" + f0[0] + "/" + f0[1] + "," + f1[0] + "/" + f1[1] + "," + f2[0] + "/" + f2[1] + ")");
        }
        FACE = LongPolyFace.faceList(this);
        System.out.println("faces");
        try {
            for (int i = 0; i < FACE.length; ++i) {
                int[] t = Combinatorics.binaryList(count, FACE[i]);
                printout(t);
            }
        } catch (Exception e) {
            System.out.println("none listed");
        }

        System.out.println("translation vector: (" + trans[0] + ", " + trans[1]
                + ", " + trans[2] + ", " + trans[3] + ")");
        System.out.println("volume: " + VOLUME);
        System.out.println("----------------");
    }
    
    
    public void printout(int[] t) {
        for (int i = 0; i < t.length; ++i) {
            System.out.print(t[i] + " ");
        }
        System.out.println("");
    }


    /*For each vertex i, this routine decides which faces
     i belongs to*/
    public void facePrint() {
        for (int i = 0; i < count; ++i) {
            System.out.print(i + " : ");
            for (int j = 0; j < FACE.length; ++j) {
                if (inFace(i, j) == true) {
                    System.out.print(j + " ");
                }
            }
            System.out.println("");
        }
    }

    public LongVector center() {
        LongVector z = new LongVector(0, 0, 0);
        for (int i = 0; i < this.count; i++) {
            z = LongVector.plus(z, this.V[i]);
        }
        z.x[0] = 1.0 * z.x[0] / this.count;
        z.x[1] = 1.0 * z.x[1] / this.count;
        z.x[2] = 1.0 * z.x[2] / this.count;
        return (z);
    }

    public LongPolyhedron sort() {
        LongPolyhedron P = new LongPolyhedron(this);
        for (int i = 0; i < P.count; i++) {
            for (int j = i + 1; j < P.count; j++) {
                if (P.V[j].x[2] > P.V[i].x[2]) {
                    LongVector temp = new LongVector(P.V[i]);
                    P.V[i] = P.V[j];
                    P.V[j] = temp;
                }
                if (P.V[j].x[2] == P.V[i].x[2]) {
                    if (P.V[j].x[1] > P.V[i].x[1]) {
                        LongVector temp = new LongVector(P.V[i]);
                        P.V[i] = P.V[j];
                        P.V[j] = temp;
                    }
                    if (P.V[j].x[1] == P.V[i].x[1]) {
                        if (P.V[j].x[0] > P.V[i].x[0]) {
                            LongVector temp = new LongVector(P.V[i]);
                            P.V[i] = P.V[j];
                            P.V[j] = temp;
                        }
                    }
                }
            }
        }
        return (P);
    }

    /*This routine weeds out points inside a face of polytope */
    public LongPolyhedron trim() {
        LongPolyhedron Q = new LongPolyhedron();
        Q.count = 0;

        for (int i = 0; i < count; ++i) {
            int c = 0;
            for (int j = 0; j < FACE.length; j++) {
                if (inFace(i, j) == true) {
                    if (face(j).length >= 3) {
                        c++;
                    }
                }
            }
            if (c > 1) {
                Q.V[Q.count] = this.V[i];
                Q.count++;
            }
        }
        Q.FACE = LongPolyFace.faceList(Q);
        return (Q);
    }

}
