package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;
import java.util.ArrayList;

public class DrawMe extends Canvas {

  
    public static PolygonWrapper hexagon(Complex C, double s) {
        PolygonWrapper H = new PolygonWrapper();
        H.count = 6;
        H.z[0] = new Complex(C.x, C.y - s);
        H.z[1] = new Complex(C.x + Math.sqrt(3) / 2 * s, C.y - s / 2);
        H.z[2] = new Complex(C.x + Math.sqrt(3) / 2 * s, C.y + s / 2);
        for (int i = 3; i < 6; i++) {
            H.z[i] = new Complex(2 * C.x - H.z[i - 3].x, 2 * C.y - H.z[i - 3].y);
        }
        return (H);
    }


    public void drawPoly(Graphics2D g, PolygonWrapper P, Color c1, Color c2) {
        GeneralPath gp = P.toGeneralPath();
        g.setColor(c1);
        g.setStroke(new BasicStroke(2));
        g.fill(gp);
        g.setColor(c2);
        g.draw(gp);
    }

    public void PolygonFrame(Graphics2D g, PolygonWrapper P, Color c) {
        g.setStroke(new BasicStroke(2));
        GeneralPath gp = P.toGeneralPath();
        g.setColor(c);
        g.draw(gp);
    }

    public static void drawDots(Graphics2D g, Complex z, Color color) {
        double x1 = z.x;
        double y1 = z.y;
        Ellipse2D.Double circle = new Ellipse2D.Double(x1, y1, 5, 5);
        g.setColor(color);
        g.fill(circle);
    }
    
        public static void drawDots(Graphics2D g, Complex z, Color color, Color color_fill) {
        double x1 = z.x;
        double y1 = z.y;
        Ellipse2D.Double circle = new Ellipse2D.Double(x1, y1, 5, 5);
        g.setColor(color);
        g.draw(circle);
        g.setColor(color_fill);
        g.fill(circle);
    }
    
        public static void drawDots(Graphics2D g, Complex z, Color color,  int n) {
        double x1 = z.x;
        double y1 = z.y;
        Ellipse2D.Double circle = new Ellipse2D.Double(x1-n/2, y1-n/2, n, n);
        g.setColor(color);
        g.fill(circle);
    }
        
        public static void drawDots(Graphics2D g, Complex z, Color color, 
                Color color_fill,  int n) {
        double x1 = z.x;
        double y1 = z.y;
        Ellipse2D.Double circle = new Ellipse2D.Double(x1-n/2, y1-n/2, n, n);
        g.setColor(color);
        g.draw(circle);
        g.setColor(color_fill);
        g.fill(circle);
    }

    public static void drawLine(Graphics2D g, Complex p, Complex q, Color c) {
        g.setColor(c);
        g.draw(new Line2D.Double(p.x, p.y, q.x, q.y));
    }


}
