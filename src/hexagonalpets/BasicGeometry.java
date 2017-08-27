package hexagonalpets;

public class BasicGeometry {
    //Algorithm for reflecting a point across a line
    
    public static Complex pt_reflt_line(Complex pt, Complex line){
    //line formula: y=ax+c so that complex line = (a,c).    
        Complex q = new Complex(pt);
        double d = (pt.x+(pt.y-line.y)*line.x)/(1+line.x*line.x);
        q.x = 2*d - q.x;
        q.y = 2*line.x*d - q.y + 2*line.y;       
        return(q);
    }
    
    public static LongVector pt_reflt_line(LongVector v, Complex line){
        LongVector w = new LongVector(v);
        double d = (v.x[0]+(v.x[1]-line.y)*line.x)/(1+line.x*line.x);
        w.x[0] = 2*d - w.x[0];
        w.x[1] = 2*line.x*d - w.x[1] + 2*line.y;
                
        return(w);
    }
}