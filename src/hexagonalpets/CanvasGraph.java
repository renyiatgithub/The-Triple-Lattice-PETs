package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CanvasGraph extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    static PolygonWrapper D, Q, G0_return, return_rotation;
    DrawMe draw = new DrawMe();
    static boolean draw_pt, draw_graph, draw_return, draw_drag, draw_return_drag = false;
    static List<PolygonWrapper> image, domain, CELL;
    static List<Complex> TV;
    static Complex pt, pt_image;
    static Complex[] lattice0, lattice1, lattice2;
    static List<Complex> lt_image, ag_part;
    static Complex[] ag, ag_return;
    static int[] word;
    static int count_ag, count_return, mode_flip, mode_rot = 0;
    static int[] ps, ps0;
    static int mode = 2;
    static String string_cfe;
    static PolygonWrapper ReturnSet;
    static LongPolygonWrapper G0, G0_rotation;
    Point JX;

    public CanvasGraph() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        Q = new PolygonWrapper();
        D = new PolygonWrapper();
        G0 = new LongPolygonWrapper();
        G0_return = new PolygonWrapper();
        image = new ArrayList<PolygonWrapper>();
        domain = new ArrayList<PolygonWrapper>();
        CELL = new ArrayList<PolygonWrapper>();
        TV = new ArrayList<Complex>();
        pt = new Complex(0, 0);
        pt_image = new Complex(0, 0);
        ps = new int[]{100, 200, 150};
        ps0 = new int[]{20, 200, 280};
        lt_image = new ArrayList<Complex>();
        ReturnSet = new PolygonWrapper();
        string_cfe = new String();
        G0_rotation = new LongPolygonWrapper();
        return_rotation = new PolygonWrapper();
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.drawString(string_cfe, 180, 20);
        g.drawString("'z' - vertical flip", 250, 380);
        g.drawString("'a' - turn off return graph", 250, 360);
        g.drawString("'s' - turn off graph", 250, 340);

        if (draw_graph) {
            try {
                for (int i = 0; i < G0.count; i++) {
                    Complex point = BasicPolygon.trans(ag[i], ps[0], ps[1], ps[2]);
                    point = transform(point.x, point.y);
                    draw.drawDots(g, point, Color.green, 5);
                }
                LongPolygonWrapper G = BasicPolygon.trans(G0, ps[0], ps[1], ps[2]);
                paint_path0(g, G, Color.yellow, 3);
            } catch (NullPointerException ne) {

            }
        }

        if (draw_return && mode_rot == 0) {
            try {
                pt = ag_return[0];
                pt_image = ag_return[1];
                for (int i = 0; i < G0_return.count; i++) {
                    Complex p = BasicPolygon.trans(ag_return[i], ps[0], ps[1], ps[2]);
                    p = transform(p.x, p.y);
                    draw.drawDots(g, p, Color.red, 8);
                }
                PolygonWrapper G = BasicPolygon.trans(G0_return, ps[0], ps[1], ps[2]);
                paint_path(g, G, Color.cyan, 2);
            } catch (NullPointerException e) {
                System.out.println("Warning: Please choose return set");
            }
        }

        if (draw_drag) {

            for (int i = 0; i < G0_rotation.count; i++) {
                Complex point = BasicPolygon.trans(G0_rotation.z[i], ps[0], ps[1], ps[2]);
                point = transform(point.x, point.y);
                draw.drawDots(g, point, Color.green, 5);
            }
            pt = G0_rotation.z[0];
            pt_image = G0_rotation.z[1];

            LongPolygonWrapper G = new LongPolygonWrapper(G0_rotation);
            G = BasicPolygon.trans(G, ps[0], ps[1], ps[2]);
            paint_path0(g, G, Color.yellow, 3);

            if (draw_return) {
                pt = return_rotation.z[0];
                pt_image = return_rotation.z[1];
                for (int i = 0; i < return_rotation.count; i++) {
                    Complex p = BasicPolygon.trans(return_rotation.z[i], ps[0], ps[1], ps[2]);
                    p = transform(p.x, p.y);
                    draw.drawDots(g, p, Color.red, 8);
                }
                PolygonWrapper M = new PolygonWrapper(return_rotation);
                M = BasicPolygon.trans(M, ps[0], ps[1], ps[2]);
                paint_path(g, M, Color.cyan, 2);
            }
        }

        if (draw_pt) {
            try{
            Complex p = BasicPolygon.trans(pt, ps[0], ps[1], ps[2]);
            p = transform(p.x, p.y);
            draw.drawDots(g, p, Color.green, 9);
            Complex q = BasicPolygon.trans(pt_image, ps[0], ps[1], ps[2]);
            q = transform(q.x, q.y);
            draw.drawDots(g, q, Color.blue, 9);
            }catch(NullPointerException e){
                
            }
        }
    }

    public static void graph_units(Complex z, double s) {
        string_cfe = CanvasControl.string_cfe;
        G0 = new LongPolygonWrapper();
        int n = GetTiling.period(z, CanvasScissor.domain, CanvasScissor.TV);
        Complex q = new Complex(z);
        ag = new Complex[20000];
        word = new int[20000];
        count_ag = 0;
        ag[count_ag] = new Complex(q);
        count_ag++;

        for (int i = 1; i < 3 * n + 1; i++) {
            Complex image = Pet.map(q, CanvasScissor.domain, CanvasScissor.TV);
            List<Complex> pt_images = pts_lift(image, s);
//            Complex q_nearest = closest_pt(q, pt_images);      
            Complex q_nearest = proj(q, pt_images, s);

            Complex v = Complex.minus(q_nearest, q);
            ag[count_ag] = new Complex(Complex.plus(ag[i - 1], v));
            int symbol = SymbolicCoding.coding(v);
            word[count_ag - 1] = symbol;
            count_ag++;
            q = image;
            if (Complex.dist(ag[i], z) < Math.pow(10, -10)) {
                break;
            }
        }
//        System.out.println("periodicity: " + n);

        G0 = new LongPolygonWrapper();
        G0.count = count_ag;
        for (int i = 0; i < G0.count; i++) {
            G0.z[i] = ag[i];
        }
        G0_rotation = new LongPolygonWrapper(G0);
        pt = ag[0];
        pt_image = ag[1];
        draw_graph = true;
        if (CanvasScissor.draw_returnSet == false) {
            draw_return = false;
        }
        draw_drag = false;
        draw_pt = true;
//        System.out.println("symbolic coding:");
        word = Arrays.copyOfRange(word, 0, count_ag - 1);
//        System.out.println(Arrays.toString(word));
//        System.out.println("length: " + word.length);
    }

    public static void graph_return(Complex z, double s) {
        ReturnSet = FirstReturn.returnSet_SS(s);
        int n = GetTiling.period(z, CanvasScissor.domain, CanvasScissor.TV);
        Complex q = new Complex(z);
        ag = new Complex[20000];
        count_ag = 0;
        ag[count_ag] = new Complex(q);
        count_ag++;

        count_return = 0;
        ag_return = new Complex[20000];
        ag_return[count_return] = new Complex(q);
        count_return++;

        for (int i = 1; i < 3 * n + 1; i++) {
            Complex image = Pet.map(q, CanvasScissor.domain, CanvasScissor.TV);
            List<Complex> pt_images = pts_lift(image, s);
//            Complex q_nearest = closest_pt(q, pt_images);      
            Complex q_nearest = proj(q, pt_images, s);
            Complex v = Complex.minus(q_nearest, q);
            ag[count_ag] = new Complex(Complex.plus(ag[i - 1], v));

            if (PolygonWrapper.contains(ReturnSet, image)) {
                ag_return[count_return] = new Complex(ag[count_ag]);
                count_return++;
            }
            count_ag++;
            q = image;
            if (Complex.dist(ag[i], z) < Math.pow(10, -10)) {
                break;
            }
        }
        System.out.println("periodicity: " + n);

        G0_return = new PolygonWrapper();
        G0_return.count = count_return;
        for (int i = 0; i < G0_return.count; i++) {
            G0_return.z[i] = ag_return[i];
        }
        return_rotation = new PolygonWrapper(G0_return);
        mode_rot = 0;
        pt = ag_return[0];
        pt_image = ag_return[1];
        draw_graph = true;
        draw_return = true;
        draw_pt = true;
    }

    public static void graph_orbit(Complex z) {
        int n = GetTiling.period(z, CanvasScissor.domain, CanvasScissor.TV);
        Complex q = new Complex(z);
        pt = q;
        ag = new Complex[20000];
        count_ag = 0;
        ag[count_ag] = new Complex(q);
        count_ag++;

        for (int i = 0; i < n; i++) {
            q = Pet.map(q, CanvasScissor.domain, CanvasScissor.TV);
            ag[count_ag] = q;
            count_ag++;
        }
        draw_graph = true;
    }

    public void GraphRotation() {
        G0_rotation = BasicPolygon.rotation_center(G0, RotationAxis.theta);
        if (draw_return) {
            return_rotation = BasicPolygon.rotation(G0_return,G0.center(),
                    RotationAxis.theta);
        }
        draw_drag = true;
        draw_graph = false;
        mode_rot = 1;
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = new Point(J.X);
    }

    public void mouseEntered(MouseEvent e) {
        requestFocus();
    }

    public void doMouseClick(int mode) {
        if (mode == 1) {
            scaleUp(JX, 0);
        }
        if (mode == 2) {
            scaleUp(JX, 1);
        }
        if (mode == 3) {
            graph_units(CanvasScissor.pt, CanvasScissor.value);
            ps = new int[]{10, 200, 150};
        }
        if (mode == 4) {
            G0_rotation = BasicPolygon.flip_vertical(G0_rotation, G0_rotation.center());
            mode_flip = (mode_flip + 1) % 2;
            if (draw_return) {
                return_rotation = return_rotation.flip_vertical(G0_rotation.center());
                G0_return = return_rotation;
            }
            G0 = G0_rotation;
        }
        if (mode == 5) {
            graph_orbit(CanvasScissor.pt);
            ps = new int[]{100, 200, 150};
        }

        repaint();
    }

    public void paint_frame(Graphics2D g, PolygonWrapper P, Color c, int n) {
        GeneralPath gp = P.toGeneralPath();
        gp = transform(gp);
        g.setColor(c);
        g.setStroke(new BasicStroke(n));
        g.draw(gp);
    }

    public void fill_frame(Graphics2D g, PolygonWrapper P, Color c) {
        GeneralPath gp = P.toGeneralPath();
        gp = transform(gp);
        g.setColor(c);
        g.fill(gp);
    }

    public void paint_path(Graphics2D g, PolygonWrapper P, Color c, int n) {
        GeneralPath gp = P.Path();
        gp = transform(gp);
        g.setColor(c);
        g.setStroke(new BasicStroke(n));
        g.draw(gp);
    }

    public void paint_path0(Graphics2D g, LongPolygonWrapper P, Color c, int n) {
        GeneralPath gp = P.Path();
        gp = transform(gp);
        g.setColor(c);
        g.setStroke(new BasicStroke(n));
        g.draw(gp);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        int test = 0;
        if (ch == 'f') {
            test = 1;
        }
        if (ch == 'd') {
            test = 2;
        }
        if (ch == 'e') {
            test = 3;
            mode = 2;
        }
        if (ch == 'o') {
            test = 5;
            mode = 1;
        }
        if (ch == 's') {
            draw_return = !draw_return;
        }
        if (ch == 'a') {
            draw_graph = !draw_graph;
        }
        if (ch == 'z') {
            test = 4;
        }
        if (test > 0) {
            doMouseClick(test);
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static List<Complex> pts_lift(Complex p, double s) {
        int a = (int) Math.floor(1.0 / s);
        double side = 2 * (1 - a * s);
        Complex w = new Complex(p);
        List<Complex> lt_image = new ArrayList<Complex>();
        for (int i = -2; i < 3; i++) {
            Complex wm = Complex.plus(w, new Complex(i
                    * (2 * s + side), 0));
            lt_image.add(wm);

            for (int j = -2; j < 3; j++) {
                Complex wm0 = Complex.plus(wm, new Complex(j * s,
                        j * s * Math.sqrt(3)));
                lt_image.add(wm0);
            }
        }
        return (lt_image);
    }

    public static Complex closest_pt(Complex z, List<Complex> lt_pts) {
        Complex min = new Complex(lt_pts.get(0));
        for (int i = 0; i < lt_pts.size(); i++) {
            Complex zi = new Complex(lt_pts.get(i));
            double dist_i = Complex.dist(z, zi);
            if (dist_i < Complex.dist(z, min)) {
                min = zi;
            }
        }
        return (min);
    }

    public static Complex proj(Complex z, List<Complex> lt_pts, double s) {
        Complex pt = new Complex(lt_pts.get(0));
        for (int i = 0; i < lt_pts.size(); i++) {
            Complex zi = new Complex(lt_pts.get(i));
            double dist_i = Complex.dist(z, zi);
            if (dist_i < Complex.dist(z, pt)) {
                pt = zi;
            }
        }
        Complex v = Complex.minus(pt, z);
        double cost = Complex.dot(v, new Complex(1, 0)) / (Complex.norm(v));
        if (Math.abs(cost - 0.5) < Math.pow(10, -6)) {
            for (int i = 0; i < lt_pts.size(); i++) {
                Complex zi = new Complex(lt_pts.get(i));
                if (PolygonWrapper.contains(FirstReturn.largeSet_SS(s), zi)) {
                    Complex w = Complex.minus(zi, z);
                    double cosw = Complex.dot(w, new Complex(1, 0)) / Complex.norm(w);
                    if (Math.abs(cosw + 0.5) < Math.pow(10, -6)) {
                        return (zi);
                    }
                }
            }
        }
        return (pt);
    }
}
