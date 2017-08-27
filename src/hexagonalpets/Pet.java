package hexagonalpets;

import java.util.*;

public class Pet {

    public static List<List<PolygonWrapper>> move_lattice(double s,
            PolygonWrapper P0, PolygonWrapper P1, Complex[] Lattice, int mode) {
        List<List<PolygonWrapper>> partition = new ArrayList<List<PolygonWrapper>>();
        List<PolygonWrapper> list = new ArrayList<PolygonWrapper>();
        List<PolygonWrapper> list1 = new ArrayList<PolygonWrapper>();
        int times = (int) Math.ceil(1.0 / s) + 5;
        for (int i = -times; i < times; i++) {
            for (int j = -times; j < times; j++) {
                PolygonWrapper W = new PolygonWrapper(P0);
                Complex z = Complex.plus(Lattice[0].scale(i), Lattice[1].scale(j));
                Complex z1 = z.scale(-1);
                W = BasicPolygon.move(P0, z);
                if (mode == 1) {
                    PolygonWrapper Q = W;
                    list.add(Q);
                    list1.add(BasicPolygon.move(Q, z1));
                }
                if (mode == 0) {
                    PolygonWrapper Q = PolygonWrapper.intersect(W, P1);
                    if (Q != null) {
                        list.add(Q);
                        list1.add(BasicPolygon.move(Q, z1));
                    }
                }
            }
        }
        partition.add(list1);
        partition.add(list);
        return (partition);
    }

    public static List<Complex> Get_TV(List<List<PolygonWrapper>> PART) {
        List<Complex> map_vector = new ArrayList<Complex>();
        List<PolygonWrapper> PART0 = PART.get(0);
        List<PolygonWrapper> PART1 = PART.get(1);
        for (int i = 0; i < PART0.size(); i++) {
            Complex c0 = PART0.get(i).center();
            Complex c1 = PART1.get(i).center();
            Complex v = Complex.minus(c1, c0);
            map_vector.add(v);

        }
        return (map_vector);
    }

    public static List<PolygonWrapper> Partition_Image(List<PolygonWrapper> L0,
            List<PolygonWrapper> L1) {
        List<PolygonWrapper> image = new ArrayList<PolygonWrapper>();
        for (PolygonWrapper l0 : L0) {
            for (PolygonWrapper l1 : L1) {
                PolygonWrapper p = PolygonWrapper.intersect(l0, l1);
                if (p != null && p.area() > 0.0000001) {
                    image.add(p);
                }
            }
        }
        return (image);
    }

    public static List<PolygonWrapper> MapAll(List<PolygonWrapper> L0, List<PolygonWrapper> L1,
            List<Complex> V) {
        List<PolygonWrapper> image = new ArrayList<PolygonWrapper>();
        for (PolygonWrapper l0 : L0) {
            for (int i = 0; i < L1.size(); i++) {
                Complex c0 = l0.center();
                if (PolygonWrapper.contains(L1.get(i), c0)) {
                    PolygonWrapper p = new PolygonWrapper(l0);
                    p = BasicPolygon.move(p, V.get(i));
                    image.add(p);
                }
            }

        }
        return (image);
    }

    public static List<PolygonWrapper> GetDomain(List<PolygonWrapper> P,
            List<PolygonWrapper> image, List<Complex> TV) {
        List<PolygonWrapper> domain = new ArrayList<PolygonWrapper>();
        for (int j = 0; j < P.size(); j++) {
            PolygonWrapper m = P.get(j);
            Complex c = m.center();
            for (int i = 0; i < image.size(); i++) {
                if (PolygonWrapper.contains(image.get(i), c)) {
                    Complex v = TV.get(i).scale(-1);
                    PolygonWrapper d = BasicPolygon.move(m, v);

                    domain.add(d);                  
                    break;
                    
                }
            }
        }
        return (domain);
    }

    public static int region(Complex z, List<PolygonWrapper> L) {
        for (int i = 0; i < L.size(); i++) {
            if (PolygonWrapper.contains(L.get(i), z)) {
                return (i);
            }
        }
        return (-1);
    }

    public static List<PolygonWrapper> map_list(List<PolygonWrapper> PL){
        List<PolygonWrapper> QL = new ArrayList<PolygonWrapper>();
        for(int i=0; i<PL.size(); i++){
            PolygonWrapper Q = new PolygonWrapper(PL.get(i));
            Q = BasicPolygon.move(Q, Q.vector);
            QL.add(Q);
        }
        return(QL);
    }
    
    
    
    public static Complex map(Complex z, List<PolygonWrapper> L, List<Complex> TV) {
        int n = region(z, L);
        Complex w = Complex.plus(z, TV.get(n));
        return (w);
    }
    


    public static PolygonWrapper map(PolygonWrapper P, List<PolygonWrapper> L, List<Complex> TV) {
        PolygonWrapper Q = new PolygonWrapper(P);
        Complex c = Q.center();
        int n = region(c, L);
        Q = BasicPolygon.move(Q, TV.get(n));

        return (Q);
    }

    public static Complex map_inverse(Complex z, List<PolygonWrapper> L){
        int n = region(z, L);
        Complex w = Complex.minus(z, L.get(n).vector);
        return(w);
    }
    
        public static PolygonWrapper map_inverse(PolygonWrapper P, List<PolygonWrapper> L, List<Complex> TV) {
        PolygonWrapper Q = new PolygonWrapper(P);
        Complex c = Q.center();
        List<PolygonWrapper> image = Pet.map_list(L);
        int n = region(c, image);
        Complex trans = image.get(n).vector;
        Q = BasicPolygon.move(Q, new Complex(-trans.x, -trans.y));

        return (Q);
    }

    
    public static PolygonWrapper[] map_array(PolygonWrapper[] PL){
        PolygonWrapper[] QL = new PolygonWrapper[PL.length];
        for(int i=0; i<QL.length; i++){
            QL[i] = new PolygonWrapper(PL[i]);
            QL[i] = BasicPolygon.move(QL[i], QL[i].vector);        
            QL[i].index = PL[i].index;
        }
        return(QL);
    } 

    public static List<Complex> translation_vector(List<PolygonWrapper> PL){
        List<Complex> vectors = new ArrayList<Complex>();
        for(int i=0; i<PL.size(); i++){
            vectors.add(PL.get(i).vector);
        }
        return(vectors);
    }
    
    public static List<PolygonWrapper> domain(double s) {
        List<PolygonWrapper> setup = CanvasLattice.Setup(s);
        Complex c = setup.get(0).center();
        List<PolygonWrapper> getdomain = CanvasLattice.GetCell(s, setup);
        for (int i = 0; i < getdomain.size(); i++) {
            getdomain.set(i, BasicPolygon.move(getdomain.get(i), new Complex(-c.x, -c.y)));
        }
        
        List<PolygonWrapper> getimage = map_list(getdomain);
        
        
        List<PolygonWrapper> modify = new ArrayList<PolygonWrapper>();

        int a0 = (int) Math.floor(1.0 / s);
        double side0 = 2 * (1 - a0 * s);
        Complex[] cc = new Complex[3];
        cc[0] = new Complex(0.5 * (s + 2), Math.sqrt(3) * s / 2);
        cc[1] = new Complex(0.5 * (-1 - 2 * s), Math.sqrt(3) / 2);
        cc[2] = new Complex(0.5 * (s - 1), -0.5 * Math.sqrt(3) * (s + 1));

        PolygonWrapper P0 = new PolygonWrapper();
        P0.count = 4;
        P0.z[0] = Complex.minus(new Complex(s, Math.sqrt(3) * s), cc[0]);
        P0.z[1] = new Complex(P0.z[0].x + side0, P0.z[0].y);
        P0.z[2] = new Complex(-0.5 * (s + 2) + side0, -Math.sqrt(3) * s / 2);
        P0.z[3] = new Complex(-0.5 * (s + 2), -Math.sqrt(3) * s / 2);

        for (int i = 0; i < getdomain.size(); i++) {
            PolygonWrapper Q = new PolygonWrapper(getdomain.get(i));
            Complex d = Q.center();
            if (PolygonWrapper.contains(P0, d)) {
                Q = BasicPolygon.move(Q, new Complex(2, 0));
            }
            Q = BasicPolygon.move(Q, new Complex(-side0, 0));
            
            
            PolygonWrapper W = new PolygonWrapper(getimage.get(i));
            Complex w = W.center();
            if(PolygonWrapper.contains(P0, w)){
                W = BasicPolygon.move(W, new Complex(2,0));
            }
            W = BasicPolygon.move(W, new Complex(-side0, 0));
            Q.vector = Complex.minus(W.center(),Q.center());
            modify.add(Q);
        }

        return(modify);
    }
    
    

}
