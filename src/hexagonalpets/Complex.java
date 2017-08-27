package hexagonalpets;


/*This class does the basic arithmetic
  of complex numbers */
public class Complex {

    double x, y;

    public Complex() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public Complex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Complex(Complex z) {
        this.x = z.x;
        this.y = z.y;
    }

    public Complex set(Complex z) {
        x = z.x;
        y = z.y;
        return this;
    }

    public static double norm(Complex z) {
        return Math.sqrt(z.x * z.x + z.y * z.y);
    }

    public static Complex unit(Complex z) {
        double d = z.norm(z);
        return new Complex(z.x / d, z.y / d);
    }

    public static Complex plus(Complex z1, Complex z2) {
        return new Complex(z1.x + z2.x, z1.y + z2.y);
    }

    public static Complex negative(Complex z) {
        Complex w = new Complex(-z.x, -z.y);
        return (w);
    }

    public static Complex minus(Complex z1, Complex z2) {
        return new Complex(z1.x - z2.x, z1.y - z2.y);
    }

    public static Complex times(Complex z1, Complex z2) {
        return new Complex(z1.x * z2.x - z1.y * z2.y,
                z1.x * z2.y + z1.y * z2.x);
    }

    public static Complex inverse(Complex z) {
        double d = z.x * z.x + z.y * z.y;
        return new Complex(z.x / d, -z.y / d);
    }

    public static Complex divide(Complex z1, Complex z2) {
        return times(z1, inverse(z2));
    }

    public static Complex conjugate(Complex z) {
        return new Complex(z.x, -z.y);
    }

    public static double dot(Complex a, Complex b) {
        return a.x * b.x + a.y * b.y;
    }

    public static double dist(Complex a, Complex b) {
        Complex z = minus(a, b);
        return (norm(z));
    }

    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    public Complex unit() {
        double d = norm();
        return new Complex(x / d, y / d);
    }

    public Complex plus(Complex z) {
        return new Complex(x + z.x, y + z.y);
    }

    public Complex minus(Complex z) {
        return new Complex(x - z.x, y - z.y);
    }

    public Complex times(Complex z) {
        return new Complex(x * z.x - y * z.y,
                x * z.y + y * z.x);
    }

    public Complex inverse() {
        double d = x * x + y * y;
        return new Complex(x / d, -y / d);
    }

    public Complex divide(Complex z2) {
        return times(inverse(z2));
    }

    public Complex conjugate() {
        return new Complex(x, -y);
    }

    public double dot(Complex a) {
        return a.x * x + a.y * y;
    }

    public boolean equals(Complex a) {
        return ((a.x == x) && (a.y == y));
    }

    public double arg() {
        return Math.atan2(y, x);
    }

    public static boolean isPositivelyOriented(Complex z1, Complex z2, Complex z3) {
        Complex[] z = new Complex[5];
        for (int i = 1; i <= 4; ++i) {
            z[i] = new Complex();
        }
        z[1] = Complex.minus(z2, z1);
        z[2] = Complex.minus(z3, z1);
        z[3] = Complex.conjugate(z[2]);
        z[4] = Complex.times(z[1], z[3]);
        if (z[4].y < 0) {
            return (true);
        }
        return (false);
    }

    public Complex reflect(Complex z) {
        Complex w = new Complex(2 * x - z.x, 2 * y - z.y);
        return (w);
    }

    public static Complex random(Complex z1, Complex z2) {
        double s = Math.random();
        double t = Math.random();
        double x = (1 - s) * z1.x + s * z2.x;
        double y = (1 - t) * z1.y + t * z2.y;
        Complex z = new Complex(x, y);
        return (z);
    }

    public static Complex primitiveRoot(int n) {
        double theta = 2.0 * Math.PI / n;
        Complex w = new Complex(Math.cos(theta), Math.sin(theta));
        return (w);
    }

    public static Complex convexAverage(double x, Complex z1, Complex z2) {
        Complex w1 = Complex.times(z1, new Complex(1 - x, 0));
        Complex w2 = Complex.times(z2, new Complex(x, 0));
        Complex w3 = Complex.plus(w1, w2);
        return (w3);
    }

    public Complex scale(Complex z, double r) {
        Complex w = new Complex();
        w.x = r * x + (1.0 - r) * z.x;
        w.y = r * y + (1.0 - r) * z.y;
        return (w);
    }

    public Complex scale(int n) {
        Complex w = new Complex();
        w.x = n * x;
        w.y = n * y;
        return (w);
    }

    public Complex scale(double n) {
        Complex w = new Complex();
        w.x = n * x;
        w.y = n * y;
        return (w);
    }

    public Vector toVector() {

        Vector V = new Vector();
        V.x[0] = this.x;
        V.x[1] = this.y;
        V.x[2] = 0;
        V.size = 3;
        return (V);
    }

    public void print() {
        System.out.println("Complex: " + this.x + " " + this.y);
    }

    public void print_rational() {
        Complex v = new Complex(this);
        v.y = 1.0 * v.y / Math.sqrt(3);
        int[] x = MathRational.approximate(v.x, Math.pow(10, -10));
        int[] y = MathRational.approximate(v.y, Math.pow(10, -10));
        System.out.println("Complex: " + x[0] + "/" + x[1] + "  " + y[0] + "/"
                + y[1] + " * sqrt 3    " + this.x + " " + this.y);
    }
}
