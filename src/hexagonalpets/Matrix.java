package hexagonalpets;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;

/*This class does the basic arithmetic
  of nxn matrices.  Here n<10 */
public class Matrix {

    double[][] a = new double[20][20];
    int size;
    int[] data = new int[2];

    public Matrix() {
    }

    /*copy routine*/
    public Matrix(Matrix m) {
        this.size = m.size;
        this.data = m.data;
        for (int i = 0; i < m.size; ++i) {
            for (int j = 0; j < m.size; ++j) {
                this.a[i][j] = m.a[i][j];
            }
        }
    }

    public Matrix(double[][] aa) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.a[i][j] = aa[i][j];
            }
        }
    }

    public Matrix(double[][] aa, int m, int n) {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                this.a[i][j] = aa[i][j];
            }
        }
    }


    /*This makes a matrix whose row are the given list of vectors.*/
    public static Matrix makeRow(Vector[] V) {
        int n = V.length;
        Matrix m = new Matrix();
        m.size = n;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                m.a[i][j] = V[i].x[j];
            }
        }
        return (m);
    }

    /*This makes a matrix whose rows are the given list of vectors.*/
    public static Matrix makeColumn(Vector[] V) {
        Matrix m = makeRow(V);
        m = m.transpose();
        return (m);
    }

    public Matrix(Vector V1, Vector V2, Vector V3) {
        this.size = 3;
        Vector[] V = {V1, V2, V3};
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.a[i][j] = V[i].x[j];
            }
        }
    }

    /**
     * special matrices*
     */
    /**
     * THe identity*
     */
    public static Matrix identity(int s) {
        Matrix m = new Matrix();
        m.size = s;
        for (int i = 0; i < s; ++i) {
            for (int j = 0; j < s; ++j) {
                m.a[i][j] = 0;
                if (i == j) {
                    m.a[i][j] = 1;
                }
            }
        }
        return (m);
    }

    /**
     * basic operations*
     */
    public static Matrix times(Matrix M1, Matrix M2) {
        int s = M1.size;
        Matrix M = new Matrix();
        for (int i = 0; i < s; ++i) {
            for (int j = 0; j < s; ++j) {
                M.a[i][j] = 0;
                for (int k = 0; k < s; ++k) {
                    M.a[i][j] = M.a[i][j] + M1.a[i][k] * M2.a[k][j];
                }
            }
        }
        M.size = s;
        return (M);
    }

    public Matrix transpose() {
        Matrix TM = new Matrix();
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                TM.a[j][i] = a[i][j];
            }
        }
        TM.size = size;
        return (TM);
    }

    public double trace() {
        double t = a[0][0] + a[1][1] + a[2][2];
        return (t);
    }

    public static Vector act(Matrix M, Vector V) {
        if (M == null) {
            return (null);
        }
        if (V == null) {
            return (null);
        }
        int s = M.size;
        Vector W = new Vector();
        W.size = M.size;
        for (int i = 0; i < s; ++i) {
            W.x[i] = 0;
            for (int j = 0; j < s; ++j) {
                W.x[i] = W.x[i] + M.a[i][j] * V.x[j];
            }
        }
        return (W);
    }

    /*This gets the jth row*/
    public Vector row(int j) {
        Vector v = new Vector();
        v.size = this.size;
        for (int i = 0; i < size; ++i) {
            v.x[i] = this.a[j][i];
        }
        return (v);
    }

    public double det() {
        return (det(this));
    }

    public static double det(Matrix M) {
        Matrix m = new Matrix(M);
        m.data[0] = 0;
        m.data[1] = 0;
        for (int i = 0; i < m.size; ++i) {
            m = GaussianElimination.step(m);
            if (m == null) {
                return (0);  //singular
            }
        }
        double d = 1;
        for (int i = 0; i < m.size; ++i) {
            d = d * m.a[i][i];
        }
        if (m.data[1] % 2 == 1) {
            d = -d;
        }
        return (d);
    }

    public Matrix inverse() {
        return (Matrix.inverse(this));
    }

    public static Matrix inverse(Matrix m) {
        Matrix[] M = new Matrix[2];
        M[0] = new Matrix(m);
        M[0].data[0] = 0;
        M[0].data[1] = 0;
        M[1] = identity(m.size);
        for (int i = 0; i < m.size; ++i) {
            M = GaussianElimination.step(M);
        }
        if (M == null) {
            return (null); //singular matrix
        }
        for (int i = 0; i < m.size; ++i) {
            for (int j = 0; j < m.size; ++j) {
                M[1].a[i][j] = M[1].a[i][j] / M[0].a[i][i];
            }
        }
        return (M[1]);
    }


    /*computes the absolute volume of the simplex spanned by the vectors*/
    public static double volume(Vector[] LIST) {
        int n = LIST.length - 1;
        Vector[] X = new Vector[n];
        for (int i = 0; i < n; ++i) {
            X[i] = Vector.minus(LIST[i], LIST[n]);
        }
        Matrix m = new Matrix();
        m.size = n;
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                m.a[i][j] = Vector.dot(X[i], X[j]);
            }
        }
        double v = m.det();
        v = Math.abs(v);
        return (Math.sqrt(v));
    }

    /**
     * printing*
     */
    public void print() {

        System.out.println("matrix");
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                double d = nearInt(a[i][j]);
                System.out.print(d + " ");
            }
            System.out.println("");
        }
        System.out.println("---------");
    }

    public static double nearInt(double s) {
        for (int i = -1000; i < 1000; ++i) {
            if (Math.abs(s - i) < .000000000001) {
                return (i);
            }
        }
        return (s);
    }

    /**
     * In the 4x4 case: this starts with a vector A in R^4 and returns the
     * projection T(A) in C, where T is the matrix such that T(V1)=0, T(V2)=0,
     * T(W1)=1, T(W2)=i,
     */
    public static Complex project42(Vector V1, Vector V2, Vector W1, Vector W2, Vector A) {
        Vector[] V = {V1, V2, W1, W2};
        Matrix m = makeColumn(V);
        m = m.inverse();
        Vector B = Matrix.act(m, A);
        Complex z = new Complex(B.x[2], B.x[3]);
        return (z);
    }

    /**
     * This routine creates the affine map which carries 0,1,i) to (z0,z1,z2).
     * here affine maps are considered /**This routine creates the affine map
     * which carries 0,1,i) to (z0,z1,z2). here affine maps are considered as
     * 3x3 matrices*
     */
    public static Matrix makeAffine(Complex z0, Complex z1, Complex z2) {
        double[][] a = {{z1.x - z0.x, z2.x - z0.x, z0.x}, {z1.y - z0.y, z2.y - z0.y, z0.y}, {0, 0, 1}};
        Matrix m = new Matrix(a);
        return (m);
    }

    /**
     * This routine creates the affine map which carries Z={z0,z1,z2} to
     * W={w0,w1,w2}*
     */
    public static Matrix makeAffine(Complex[] Z, Complex[] W) {
        Matrix m1 = makeAffine(Z[0], Z[1], Z[2]);
        Matrix m2 = makeAffine(W[0], W[1], W[2]);
        m1 = m1.inverse();
        m2 = Matrix.times(m2, m1);
        return (m2);
    }

}
