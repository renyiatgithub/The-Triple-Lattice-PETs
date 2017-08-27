package hexagonalpets;

import java.util.ArrayList;
import java.util.List;

public class FirstReturn {

    static List<PolygonWrapper> domain;
    static List<Complex> TV;
    static PolygonWrapper D_SS;

    public static List<PolygonWrapper> largeSet(double s) {
        List<PolygonWrapper> set = new ArrayList<PolygonWrapper>();
        PolygonWrapper D = new PolygonWrapper(CanvasLattice.F[0]);
        int a = (int) Math.floor(1.0 / s);
        double side = 2 * (1 - a * s);

        PolygonWrapper S0 = new PolygonWrapper();
        S0.count = 3;
        S0.z[0] = new Complex(D.z[3]);
        S0.z[1] = new Complex(S0.z[0].x + 2 * s, S0.z[0].y);
        S0.z[2] = new Complex(S0.z[0].x + s, S0.z[0].y + Math.sqrt(3) * s);

        double base = 2 * s + side;
        PolygonWrapper S1 = new PolygonWrapper();
        S1.count = 4;
        S1.z[0] = new Complex(D.z[1]);
        S1.z[1] = new Complex(S1.z[0].x - base, S1.z[0].y);
        S1.z[2] = new Complex(S1.z[0].x - s - side, S1.z[0].y - Math.sqrt(3) * s);
        S1.z[3] = new Complex(S1.z[0].x - s, S1.z[2].y);

        set.add(S0);
        set.add(S1);
        return (set);
    }

    public static List<PolygonWrapper> returnSet(double s) {
        List<PolygonWrapper> set = new ArrayList<PolygonWrapper>();
        int a = (int) Math.floor(1.0 / s);
        double side = 2 * (1 - a * s);
        double t = 1 - a * s;
        int b = (int) Math.floor(s / t);
        double shside = 2 * (s - b * t);

        PolygonWrapper D = new PolygonWrapper(CanvasLattice.F[0]);
        PolygonWrapper S0 = new PolygonWrapper();
        S0.count = 3;
        S0.z[0] = new Complex(D.z[1]);
        S0.z[1] = new Complex(S0.z[0].x - side, S0.z[0].y);
        S0.z[2] = new Complex(S0.z[0].x - 0.5 * side, S0.z[0].y - 0.5 * Math.sqrt(3) * side);

        PolygonWrapper S1 = new PolygonWrapper();
        S1.count = 4;
        S1.z[0] = new Complex(D.z[3].x + 0.5 * shside, D.z[3].y + 0.5 * Math.sqrt(3) * shside);
        S1.z[1] = new Complex(D.z[3].x + shside, D.z[3].y);
        S1.z[2] = new Complex(D.z[3].x + side + shside, D.z[3].y);
        S1.z[3] = new Complex(D.z[3].x + 0.5 * (side + shside),
                D.z[3].y + 0.5 * Math.sqrt(3) * (side + shside));
        Complex v = Complex.minus(D.z[0], S1.z[3]);
        S1 = BasicPolygon.move(S1, v);
        set.add(S0);
        set.add(S1);
        return (set);
    }

    public static PolygonWrapper returnSet_SS(double s) {
        int a = (int) Math.floor(1.0 / s);
        double side = 2 * (1 - a * s);
        double t = 1 - a * s;
        int b = (int) Math.floor(s / t);
        double shside = 2 * (s - b * t);

        Complex c = new Complex(0.5 * (s + 2), Math.sqrt(3) * s / 2);
        PolygonWrapper R = new PolygonWrapper();
        R.count = 4;
        R.z[0] = Complex.minus(new Complex(s, Math.sqrt(3) * s), c);
        R.z[1] = Complex.minus(new Complex(2 + s, Math.sqrt(3) * s), c);
        R.z[2] = Complex.minus(new Complex(2, 0), c);
        R.z[3] = Complex.minus(new Complex(0, 0), c);

        R = BasicPolygon.move(R, new Complex(side, 0));
        double aside = side + shside;

        PolygonWrapper P = new PolygonWrapper();
        P.count = 4;
        P.z[0] = new Complex(R.z[1].x - 2 * s - side, R.z[1].y);
        P.z[1] = new Complex(P.z[0].x + side, P.z[0].y);
        P.z[2] = new Complex(P.z[1].x - 0.5 * aside, P.z[1].y - Math.sqrt(3) / 2 * aside);
        P.z[3] = new Complex(P.z[2].x - side, P.z[2].y);
        return (BasicPolygon.move(P, new Complex(-side, 0)));
    }

    public static PolygonWrapper returnSet_square(double s) {
        int a = (int) Math.floor(1.0 / s);
        double side = 2 * (1 - a * s);
        double t = 1 - a * s;
        int b = (int) Math.floor(s / t);
        double shside = 2 * (s - b * t);

        Complex c = new Complex(0.5 * (s + 2), Math.sqrt(3) * s / 2);
        PolygonWrapper R = new PolygonWrapper();
        R.count = 4;
        R.z[0] = Complex.minus(new Complex(s, Math.sqrt(3) * s), c);
        R.z[1] = Complex.minus(new Complex(2 + s, Math.sqrt(3) * s), c);
        R.z[2] = Complex.minus(new Complex(2, 0), c);
        R.z[3] = Complex.minus(new Complex(0, 0), c);

        R = BasicPolygon.move(R, new Complex(side, 0));
        double aside = side + shside;

        PolygonWrapper P = new PolygonWrapper();
        P.count = 4;
        P.z[0] = new Complex(R.z[1].x - 2 * s - side, R.z[1].y);
        P.z[1] = new Complex(P.z[0].x + side, P.z[0].y);
        P.z[2] = new Complex(P.z[1].x - 0.5 * aside, P.z[1].y - Math.sqrt(3) / 2 * aside);
        P.z[3] = new Complex(P.z[2].x - side, P.z[2].y);
        P = BasicPolygon.move(P, new Complex(-side, 0));

        PolygonWrapper Q = new PolygonWrapper();
        Q.count = 4;
        Q.z[0] = new Complex(P.z[0]);
        Q.z[1] = new Complex(P.z[1]);
        Q.z[2] = new Complex(Q.z[0].x - 0.5 * shside + side,
                Q.z[0].y - 0.5 * Math.sqrt(3) * shside);
        Q.z[3] = new Complex(Q.z[2].x - side, Q.z[2].y);
        return (Q);
    }

    public static PolygonWrapper largeSet_SS(double s) {
        int a = (int) Math.floor(1.0 / s);
        double side = 2 * (1 - a * s);
        PolygonWrapper R = CanvasLattice.F[0];
        R = BasicPolygon.move(R, new Complex(side, 0));

        PolygonWrapper P = new PolygonWrapper();
        P.count = 4;
        P.z[0] = new Complex(R.z[1].x - 2 * s - side, R.z[1].y);
        P.z[1] = new Complex(R.z[1]);
        P.z[2] = new Complex(R.z[2]);
        P.z[3] = new Complex(R.z[2].x - 2 * s - side, R.z[2].y);
        return (BasicPolygon.move(P, new Complex(-side, 0)));
    }

    public static Complex firstReturn(Complex z, PolygonWrapper P,
            List<PolygonWrapper> Ds, List<Complex> TVs) {
        Complex w = new Complex(z);
        int time = GetTiling.period(z, Ds, TVs);
        for (int i = 0; i < time + 1; i++) {
            w = Pet.map(w, Ds, TVs);
            if (PolygonWrapper.contains(P, w)) {
                return (w);
            }
        }

        return (null);
    }

    public static Complex inverse_first_return(Complex z, PolygonWrapper P,
            List<PolygonWrapper> Ds, List<Complex> TVs) {
        Complex w = new Complex(z);
        int time = GetTiling.period(z, Ds, TVs);
        List<PolygonWrapper> image = Pet.map_list(Ds);
        for (int i = 0; i < time + 1; i++) {
            w = Pet.map_inverse(w, image);

            if (PolygonWrapper.contains(P, w)) {
                return (w);
            }
        }
        return (null);
    }

    public static PolygonWrapper returnCell(Complex z, PolygonWrapper P,
            List<PolygonWrapper> Ds, List<Complex> TVs) {
        domain = Ds;
        TV = TVs;
        Complex w = new Complex(z);
        PolygonWrapper Q = domain.get(Pet.region(z, domain));
        if (PolygonWrapper.contains(P, z)) {
            Q = PolygonWrapper.intersect(P, Q);
            List<Integer> index = GetTiling.index_seq(z, Ds, TVs);
            int n = index.size();
            PolygonWrapper W = new PolygonWrapper(Q);
            for (int i = 1; i < n; i++) {
                W = Pet.map(W, domain, TV);
                w = Pet.map(w, domain, TV);
                int k = Pet.region(w, domain);
                PolygonWrapper D = domain.get(k);
                W = PolygonWrapper.intersect(W, D);
                if (PolygonWrapper.contains(P, w)) {
                    Complex position = Complex.minus(z, w);
                    W = BasicPolygon.move(W, position);
                    W.vector = Complex.minus(w, z);
                    return (W);
                }
            }
        }
        return (null);
    }

    public static PolygonWrapper returnCell(double s, Complex z, PolygonWrapper P) {
        List<PolygonWrapper> domain_cell = Pet.domain(s);
        List<Complex> vectors = Pet.translation_vector(domain_cell);
        Complex w = new Complex(z);
        PolygonWrapper Q = domain_cell.get(Pet.region(z, domain_cell));
        if (PolygonWrapper.contains(P, z)) {
            Q = PolygonWrapper.intersect(P, Q);
            int n = GetTiling.period(z, domain_cell, vectors);
            PolygonWrapper W = new PolygonWrapper(Q);
            for (int i = 1; i < n + 1; i++) {
                W = BasicPolygon.move(W, W.vector);
                w = Complex.plus(w, W.vector);
                int k = Pet.region(w, domain_cell);
                PolygonWrapper D = domain_cell.get(k);
                W = PolygonWrapper.intersect(W, D);
                if (PolygonWrapper.contains(P, w)) {
                    Complex position = Complex.minus(z, w);
                    W = BasicPolygon.move(W, position);
                    W.vector = Complex.minus(w, z);
                    return (W);
                }

            }
        }
        return (null);
    }

    public static List<PolygonWrapper> returnCell_list(double s, List<PolygonWrapper> Plist) {
        List<PolygonWrapper> domain_cell = Pet.domain(s);
        List<Complex> vectors = Pet.translation_vector(domain_cell);
        List<PolygonWrapper> returncell = new ArrayList<PolygonWrapper>();
        List<List<Integer>> return_index = new ArrayList<List<Integer>>();

        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 23; k++) {
                Complex center = new Complex(-1 - 0.5 * s + 0.5 / 22 * i * (s + 2),
                        s * (-0.5 + 1.0 / 22 * k) * Math.sqrt(3));

                for (int j = 0; j < Plist.size(); j++) {
                    if (PolygonWrapper.contains(Plist.get(j), center)) {
                        List<Integer> return_seq = FirstReturn.return_seq(center,
                                Plist.get(j), domain_cell, vectors);
                        if (return_index.contains(return_seq) == false) {
                            PolygonWrapper R = FirstReturn.returnCell(center,
                                    Plist.get(j), domain_cell, vectors);
                            return_index.add(return_seq);
                            if (R != null && R.count > 2) {
                                returncell.add(new PolygonWrapper(R));
                            }
                        }
                    }
                }
            }
        }
        System.out.println("return maximal domains size: " + returncell.size());
        return (returncell);
    }

    public static PolygonWrapper inv_returnCell(Complex z, PolygonWrapper P,
            List<PolygonWrapper> Ds, List<Complex> TVs) {
        List<PolygonWrapper> image = Pet.map_list(Ds);
        Complex w = new Complex(z);
        PolygonWrapper Q = image.get(Pet.region(z, image));

        if (PolygonWrapper.contains(P, z)) {
            Q = PolygonWrapper.intersect(P, Q);
            int n = GetTiling.period(z, Ds, TVs);
            if (n == 1) {
                return (Q);
            }
            PolygonWrapper W = new PolygonWrapper(Q);
            for (int i = 1; i < n + 1; i++) {
                w = Pet.map_inverse(w, image);
                int k = Pet.region(w, image);
                W = Pet.map_inverse(W, Ds, TVs);
                PolygonWrapper D = image.get(k);
                W = PolygonWrapper.intersect(W, D);
                if (PolygonWrapper.contains(P, w)) {
                    Complex position = Complex.minus(z, w);
                    W = BasicPolygon.move(W, position);
                    W.vector = Complex.minus(w, z);
                    return (W);
                }
            }
        }
        return (null);
    }

    public static List<PolygonWrapper> inv_returnCell_list(double s,
            List<PolygonWrapper> Plist) {
        List<PolygonWrapper> domain_cell = Pet.domain(s);
        List<PolygonWrapper> image = Pet.map_list(domain_cell);

        List<Complex> vectors = Pet.translation_vector(domain_cell);
        List<PolygonWrapper> returncell = new ArrayList<PolygonWrapper>();
        List<List<Integer>> return_index = new ArrayList<List<Integer>>();

        for (int i = 0; i < 40; i++) {
            for (int k = 0; k < 40; k++) {
                Complex center = new Complex(-1 - 0.5 * s + 0.5 / 25 * i * (s + 2),
                        s * (-0.5 + 1.0 / 25 * k) * Math.sqrt(3));

                for (int j = 0; j < Plist.size(); j++) {
                    if (PolygonWrapper.contains(Plist.get(j), center)) {
                        List<Integer> return_seq = FirstReturn.return_inv_seq(center,
                                Plist.get(j), domain_cell, vectors);
                        if (return_index.contains(return_seq) == false) {
                            PolygonWrapper R = inv_returnCell(center,
                                    Plist.get(j), domain_cell, vectors);
                            return_index.add(return_seq);
                            if (R != null && R.count > 2) {
                                returncell.add(new PolygonWrapper(R));
                            }
                        }
                    }
                }
            }
        }
        System.out.println("inverse return maximal domains size: " + returncell.size());
        return (returncell);
    }

    public static List<Integer> return_seq(Complex z, PolygonWrapper P,
            List<PolygonWrapper> Ds, List<Complex> TVs) {
        domain = Ds;
        TV = TVs;
        List<Integer> index = new ArrayList<Integer>();
        int time = GetTiling.period(z, domain, TV);
        if (time > 0) {
            index.add(Pet.region(z, domain));
            for (int i = 0; i < time; i++) {
                z = Pet.map(z, domain, TV);
                int n = Pet.region(z, domain);
                if (n != -1) {
                    index.add(n);
                    if (PolygonWrapper.contains(P, z)) {
                        return (index);
                    }
                } else {
                    return (null);
                }
            }
        }
        return (index);
    }

    public static List<Integer> return_inv_seq(Complex z, PolygonWrapper P,
            List<PolygonWrapper> Ds, List<Complex> TVs) {
        domain = Ds;
        List<PolygonWrapper> image = Pet.map_list(Ds);
        TV = TVs;
        List<Integer> index = new ArrayList<Integer>();
        int time = GetTiling.period(z, domain, TV);
        if (time > 0) {
            index.add(Pet.region(z, image));
            for (int i = 0; i < time; i++) {
                z = Pet.map_inverse(z, image);
                int n = Pet.region(z, image);
                if (n != -1) {
                    index.add(n);
                    if (PolygonWrapper.contains(P, z)) {
                        return (index);
                    }
                } else {
                    return (null);
                }
            }
        }
        return (index);
    }

    public static List<PolygonWrapper> SymmetrySet(double s) {
        List<PolygonWrapper> sym = new ArrayList<PolygonWrapper>();
        int a = (int) Math.floor(1.0 / s);
        double side = 2 * (1 - a * s);
        double t = 1 - a * s;
        int b = (int) Math.floor(s / t);
        double shside = 2 * (s - b * t);

        Complex c = new Complex(0.5 * (s + 2), Math.sqrt(3) * s / 2);
        PolygonWrapper R = new PolygonWrapper();
        R.count = 4;
        R.z[0] = Complex.minus(new Complex(s, Math.sqrt(3) * s), c);
        R.z[1] = Complex.minus(new Complex(2 + s, Math.sqrt(3) * s), c);
        R.z[2] = Complex.minus(new Complex(2, 0), c);
        R.z[3] = Complex.minus(new Complex(0, 0), c);

        double aside = side + shside;

        PolygonWrapper S0 = new PolygonWrapper();
        S0.count = 3;
        S0.z[0] = new Complex(R.z[0]);
        S0.z[1] = new Complex(R.z[0].x - s, R.z[0].y - Math.sqrt(3) * s);
        S0.z[2] = new Complex(S0.z[1].x + 2 * s, S0.z[1].y);

        PolygonWrapper S1 = new PolygonWrapper();
        S1.count = 3;
        S1.z[0] = new Complex(R.z[0]);
        S1.z[1] = new Complex(R.z[0].x + 2 * s, R.z[0].y);
        S1.z[2] = new Complex(S0.z[2]);

        PolygonWrapper S2 = new PolygonWrapper();
        S2.count = 4;
        S2.z[0] = new Complex(S1.z[1]);
        S2.z[1] = new Complex(S1.z[1].x + 0.5 * side, S1.z[1].y - 0.5 * side * Math.sqrt(3));
        S2.z[2] = new Complex(R.z[2]);
        S2.z[3] = new Complex(R.z[2].x - side, R.z[2].y);

        sym.add(S0);
        sym.add(S1);
        sym.add(S2);
        return (sym);

    }

    public static List<PolygonWrapper> ReflectionSet(double s) {
        List<PolygonWrapper> list_reflection = new ArrayList<PolygonWrapper>();
        int a = (int) Math.floor(1.0 / s);
        double side = 2 * (1 - a * s);
        double t = 1 - a * s;
        int b = (int) Math.floor(s / t);
        double shside = 2 * (s - b * t);

        Complex c = new Complex(0.5 * (s + 2), Math.sqrt(3) * s / 2);
        PolygonWrapper R = new PolygonWrapper();
        R.count = 4;
        R.z[0] = Complex.minus(new Complex(s, Math.sqrt(3) * s), c);
        R.z[1] = Complex.minus(new Complex(2 + s, Math.sqrt(3) * s), c);
        R.z[2] = Complex.minus(new Complex(2, 0), c);
        R.z[3] = Complex.minus(new Complex(0, 0), c);

        double aside = side + shside;

        PolygonWrapper R0 = new PolygonWrapper();
        R0.count = 4;
        R0.z[0] = new Complex(R.z[0]);
        R0.z[1] = new Complex(R.z[0].x + (2 * s - side), R.z[0].y);
        R0.z[2] = new Complex(R0.z[1].x + side - s, R.z[1].y - (side - s) * Math.sqrt(3));
        R0.z[3] = new Complex(R0.z[0].x + 0.5 * side, R.z[0].y - 0.5 * Math.sqrt(3) * side);;

        PolygonWrapper R1 = new PolygonWrapper();
        R1 = BasicPolygon.flip_vertical(R0, new Complex(R0.z[2]));

        list_reflection.add(R0);
        list_reflection.add(R1);

        return (list_reflection);
    }

    
    
}
