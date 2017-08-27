package hexagonalpets;

import java.util.*;

public class GetTiling {

    static List<PolygonWrapper> domain;
    static List<Complex> TV;

    public static List<PolygonWrapper> orbit(Complex z) {
        List<PolygonWrapper> list = new ArrayList<PolygonWrapper>();
        PolygonWrapper P = tile(z);
        int time = period(z, domain, TV);
        if (time > 0) {
            P.colormode = time;
            list.add(P);
            for (int i = 0; i < time - 1; i++) {
                try{
                P = Pet.map(P, domain, TV);
                P.colormode = time;
                list.add(P);
                }catch(OutOfMemoryError e){
                    
                }
            }
        }
        return (list);
    }
    
    public static List<PolygonWrapper> orbit(Complex z, List<PolygonWrapper> Ds,
            List<Complex> TVs){
        List<PolygonWrapper> list = new ArrayList<PolygonWrapper>();
        PolygonWrapper P = tile(z, Ds, TVs);
        int time = period(z, Ds, TVs);
        if (time > 0) {
            P.colormode = time;
            list.add(P);
            for (int i = 0; i < time - 1; i++) {
                P = Pet.map(P, Ds, TVs);
                P.colormode = time;
                list.add(P);
            }
        }
        return (list);                
    }

    public static PolygonWrapper tile(Complex z) {
        domain = CanvasCell.domain;
        TV = CanvasCell.TV;
        List<Integer> index = index_seq(z);
        int n = index.size();
        PolygonWrapper P = domain.get(Pet.region(z, domain));
        if (n < 3) {
            return P;
        }
        PolygonWrapper Q = new PolygonWrapper(P);
        for (int i = 1; i < n; i++) {
            Q = Pet.map(Q, domain, TV);
            PolygonWrapper W = domain.get(index.get(i));
            Q = PolygonWrapper.intersect(Q, W);
        }
        return (Q);
    }

    
        public static PolygonWrapper tile(Complex z, List<PolygonWrapper> Ds, 
                List<Complex> TVs) {
        domain = Ds;
        TV = TVs;
        List<Integer> index = index_seq(z, Ds, TVs);
        int n = index.size();
        PolygonWrapper P = domain.get(Pet.region(z, domain));
        if (n < 3) {
            return P;
        }
        PolygonWrapper Q = new PolygonWrapper(P);
        for (int i = 1; i < n; i++) {
            Q = Pet.map(Q, domain, TV);
            PolygonWrapper W = domain.get(index.get(i));
            Q = PolygonWrapper.intersect(Q, W);
        }
        return (Q);
    }
    
    public static List<Integer> index_seq(Complex z) {
        domain = CanvasCell.domain;
        TV = CanvasCell.TV;
        List<Integer> index = new ArrayList<Integer>();
        int time = period(z, domain, TV);
        if (time > 0) {
            index.add(Pet.region(z, domain));
            for (int i = 0; i < time; i++) {
                z = Pet.map(z, domain, TV);
                int n = Pet.region(z, domain);
                if (n != -1) {
                    index.add(n);
                } else {
                    return (null);
                }
            }
        }
        return (index);
    }

    public static List<Integer> index_seq(Complex z, List<PolygonWrapper> Ds,
            List<Complex> TVs) {
        domain = Ds;
        TV = TVs;
        List<Integer> index = new ArrayList<Integer>();
        int time = period(z, domain, TV);
        if (time > 0) {
            index.add(Pet.region(z, domain));
            for (int i = 0; i < time; i++) {
                z = Pet.map(z, domain, TV);
                int n = Pet.region(z, domain);
                if (n != -1) {
                    index.add(n);
                } else {
                    return (null);
                }
            }
        }
        return (index);
    }

    public static int period(Complex z, List<PolygonWrapper> Ds,
            List<Complex> TVs) {
        Complex z0 = new Complex(z);
        z0 = Pet.map(z, Ds, TVs);
        int count = 1;
        for (int i = 0; i < 10000; i++) {
            if (Complex.dist(z, z0) > Math.pow(10, -10)) {
                z0 = Pet.map(z0, Ds, TVs);
                count++;
            } else {
                return (count);
            }
        }
        return (0);
    }

    public static void printlist(List<Integer> index) {
        int n = index.size();
        System.out.println("steps: " + Integer.toString(n-1));
        for (int i = 0; i < n; i++) {
            System.out.print(index.get(i) + ", ");
        }
        System.out.println();
    }

    public static List<List<Integer>> CR(List<Integer> circle) {
        List<List<Integer>> circlelt = new ArrayList<List<Integer>>();
        circlelt.add(circle);
        int sc = circle.size();
        for (int j = 0; j < sc; j++) {
            List<Integer> ref = new ArrayList<Integer>();
            for (int i = 0; i < sc; i++) {
                ref.add(i, circle.get((i + 1) % sc));
            }
            circlelt.add(ref);
            circle = ref;
        }

        return (circlelt);
    }

    public static boolean list_contain_pt(List<PolygonWrapper> PL, Complex z) {
        for (PolygonWrapper P : PL) {
            if (PolygonWrapper.contains(P, z)) {
                return (true);
            }
        }
        return (false);
    }
}
