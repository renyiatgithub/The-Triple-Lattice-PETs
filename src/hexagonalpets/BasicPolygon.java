package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class BasicPolygon {

    ScaleCanvas scale = new ScaleCanvas();

    public static PolygonWrapper trans(PolygonWrapper P, int c, double x, double y) {
        PolygonWrapper Q = new PolygonWrapper(P);
        for (int i = 0; i < Q.count; i++) {
            Q.z[i].x = (Q.z[i].x) * c + x;
            Q.z[i].y = -(Q.z[i].y) * c + y;
        }
        return (Q);
    }

    public static LongPolygonWrapper trans(LongPolygonWrapper P, int c, double x, double y) {
        LongPolygonWrapper Q = new LongPolygonWrapper(P);
        for (int i = 0; i < Q.count; i++) {
            Q.z[i].x = (Q.z[i].x) * c + x;
            Q.z[i].y = -(Q.z[i].y) * c + y;
        }
        return (Q);
    }

    public static Complex trans(Complex z, int c, double x, double y) {
        Complex w = new Complex(z);
        w.x = w.x * c + x;
        w.y = -w.y * c + y;
        return (w);
    }

    public static Complex untrans(Complex z, int c, double x, double y) {
        Complex z0 = new Complex(z);
        z0.x = 1.0 * (z0.x - x) / c;
        z0.y = -1.0 * (z0.y - y) / c;
        return (z0);
    }

    public static PolygonWrapper scalar(PolygonWrapper P, double c) {
        PolygonWrapper Q = new PolygonWrapper(P);
        for (int i = 0; i < Q.count; i++) {
            Q.z[i].x *= c;
            Q.z[i].y *= c;
        }
        return (Q);
    }

    //This routine is to translate a polygon by a vector z
    public static PolygonWrapper move(PolygonWrapper P, Complex z) {
        PolygonWrapper W = new PolygonWrapper(P);
        for (int i = 0; i < W.count; i++) {
            W.z[i].x = W.z[i].x + z.x;
            W.z[i].y = W.z[i].y + z.y;
        }
        W.colormode = P.colormode;
        W.vector = P.vector;
        return (W);
    }

    public static Complex rotation(Complex z, double theta, Complex c) {
        Complex w = new Complex();
        w.x = (z.x - c.x) * Math.cos(theta) - (z.y - c.y) * Math.sin(theta) + c.x;
        w.y = (z.x - c.x) * Math.sin(theta) + (z.y - c.y) * Math.cos(theta) + c.y;
        return (w);
    }

    public static PolygonWrapper rotation(PolygonWrapper P, double theta) {
        PolygonWrapper Q = new PolygonWrapper(P);
        for (int i = 0; i < Q.count; i++) {
            Q.z[i] = rotation(Q.z[i], theta, P.center());
        }
        Q.vector = rotation(P.vector, theta, P.center());
        return (Q);
    }
    
    public static PolygonWrapper rotation(PolygonWrapper P, Complex c, double theta){
        PolygonWrapper Q = new PolygonWrapper(P);
        for(int i=0; i<Q.count; i++){
            Q.z[i] = rotation(Q.z[i], theta, c);
        }
        Q.vector = rotation(P.vector, theta, c);
        return(Q);
    }

    public static LongPolygonWrapper rotation_center(LongPolygonWrapper P, double theta) {
        LongPolygonWrapper Q = new LongPolygonWrapper(P);
        for (int i = 0; i < Q.count; i++) {
            Q.z[i] = rotation(Q.z[i], theta, P.center());
        }
        return (Q);
    }

    public void paint_polygon(Graphics2D g, PolygonWrapper P, Color c, int n) {
        GeneralPath gp = P.toGeneralPath();
        gp = scale.transform(gp);
        g.setColor(c);
        g.setStroke(new BasicStroke(n));
        g.draw(gp);
    }

    public static Complex[] order_vertex(PolygonWrapper P) {
        Complex[] verticies = new Complex[P.count];
        for (int i = 0; i < P.count; i++) {
            verticies[i] = new Complex(P.z[i]);
        }
        for (int i = 0; i < verticies.length; i++) {
            for (int j = i + 1; j < verticies.length; j++) {
                if (verticies[i].y > verticies[j].y) {
                    Complex v = new Complex(verticies[i]);
                    verticies[i] = verticies[j];
                    verticies[j] = v;
                }
                if (Math.abs(verticies[i].y - verticies[j].y) < Math.pow(10, -10)
                        && verticies[i].x > verticies[j].x) {
                    Complex w = new Complex(verticies[i]);
                    verticies[i] = verticies[j];
                    verticies[j] = w;
                }
            }
        }

        return (verticies);
    }

    public static PolygonWrapper[] sort(List<PolygonWrapper> PL) {
        PolygonWrapper[] RR = new PolygonWrapper[PL.size()];
        for (int i = 0; i < PL.size(); i++) {
            RR[i] = new PolygonWrapper(PL.get(i));
        }
        for (int j = 0; j < PL.size(); j++) {
            for (int k = j + 1; k < PL.size(); k++) {
                Complex cj = RR[j].center();
                Complex ck = RR[k].center();
                if (cj.y > ck.y && Math.abs(cj.y - ck.y) >= Math.pow(10, -6)) {
                    PolygonWrapper Q = new PolygonWrapper(RR[k]);
                    RR[k] = new PolygonWrapper(RR[j]);
                    RR[j] = Q;
                }
                if (Math.abs(cj.y - ck.y) < Math.pow(10, -6)) {
                    if (cj.x > ck.x) {
                        PolygonWrapper Q = new PolygonWrapper(RR[k]);
                        RR[k] = new PolygonWrapper(RR[j]);
                        RR[j] = Q;
                    }
                }
            }
        }
        return (RR);
    }
    
   
    
       public static PolygonWrapper[] sort(PolygonWrapper[] PL) {
        PolygonWrapper[] RR = new PolygonWrapper[PL.length];
        for (int i = 0; i < PL.length; i++) {
            RR[i] = new PolygonWrapper(PL[i]);
        }
        for (int j = 0; j < PL.length; j++) {
            for (int k = j + 1; k < PL.length; k++) {
                Complex cj = RR[j].center();
                Complex ck = RR[k].center();
                if (Math.abs(cj.y - ck.y) >= Math.pow(10, -6)) {
                    PolygonWrapper Q = new PolygonWrapper(RR[k]);
                    RR[k] = new PolygonWrapper(RR[j]);
                    RR[j] = Q;
                }
                if (Math.abs(cj.y - ck.y) < Math.pow(10, -6)) {
                    if (cj.x > ck.x) {
                        PolygonWrapper Q = new PolygonWrapper(RR[k]);
                        RR[k] = new PolygonWrapper(RR[j]);
                        RR[j] = Q;
                    }
                }
            }
        }
        return (RR);
    }

    public static void rational_print(PolygonWrapper P) {
        System.out.println("count " + P.count);
        for (int i = 0; i < P.count; i++) {
            Complex v = new Complex(P.z[i]);
            v.y = 1.0 * v.y / Math.sqrt(3);
            int[] x = MathRational.approximate(v.x, Math.pow(10, -12));
            int[] y = MathRational.approximate(v.y, Math.pow(10, -12));
            System.out.println(i + " " + x[0] + "/" + x[1] + "  " + y[0]
                    + "/" + y[1] + " * sqrt 3");

        }
    }

    public static PolygonWrapper copy(PolygonWrapper P) {
        PolygonWrapper W = new PolygonWrapper(P);
        return (W);
    }

    public static LongPolygonWrapper flip_vertical(LongPolygonWrapper P, Complex z) {
        LongPolygonWrapper W = new LongPolygonWrapper(P);
        for (int i = 0; i < W.count; i++) {
            W.z[i].x = 2 * z.x - W.z[i].x;
        }
        return (W);
    }
    
    public static PolygonWrapper flip_vertical(PolygonWrapper P, Complex z){
        PolygonWrapper W = new PolygonWrapper(P);
        for(int i=0; i<W.count; i++){
            W.z[i].x = 2 * z.x - W.z[i].x;
        }
        W.vector = new Complex(2 * z.x - P.vector.x, P.vector.y);
        W.index = P.index;
        return(W);
    }
    
    
    
    public static PolygonWrapper flip_horizontal(PolygonWrapper P, double y){
        PolygonWrapper Q = new PolygonWrapper(P);
        for(int i=0; i<Q.count; i++){
            Q.z[i].y = 2 * y - Q.z[i].y;
        }
        Q.vector = new Complex(Q.vector.x, 2 * y - Q.vector.y);
        return(Q);
    }
    
    public static PolygonWrapper[] copy_list(List<PolygonWrapper> PL){
        int l = PL.size();
        PolygonWrapper[] WL = new PolygonWrapper[l];
        for(int i=0; i<l; i++){
            WL[i] = new PolygonWrapper(PL.get(i));
        }
        return(WL);
    }
    
    public static List<PolygonWrapper> copy_array(PolygonWrapper[] PL){
        List<PolygonWrapper> WL = new ArrayList<PolygonWrapper>();
        for(int i=0; i<PL.length; i++){
            PolygonWrapper W = new PolygonWrapper(PL[i]);
            W.vector = PL[i].vector;
            W.index = PL[i].index;
            WL.add(W);
        }
        return(WL);
    }
    
    public static boolean contain(PolygonWrapper P, PolygonWrapper Q){
        for(int i=0; i<Q.count; i++){
            if(PolygonWrapper.contains(P, Q.z[i])==false){
                return(false);
            }            
        }
        return(true);
    }
    
    public static double area_sum(List<PolygonWrapper> PL){
        double sum = 0;
        for(PolygonWrapper P : PL){
            sum = sum + P.area();
        }
        return(sum);
    }
    
    public static List<PolygonWrapper> clean_list(List<PolygonWrapper> PL){
        List<PolygonWrapper> CL = new ArrayList<PolygonWrapper>();
        for(int i=0; i<PL.size(); i++){
            int mark = 1;
            for(int j=i+1; j<PL.size(); j++){
                PolygonWrapper I = PolygonWrapper.intersect(PL.get(i), PL.get(j));
                if(I == null || I.count< 3){
                    mark = 1 * mark;
                }else{
                    mark = 0;
                }               
            }
            if(mark == 1){
                CL.add(PL.get(i));
            }
        }
        return(CL);
    }

}
