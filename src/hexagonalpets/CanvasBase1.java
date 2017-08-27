package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasBase1 extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    DrawMe draw = new DrawMe();
    static boolean draw_domain, draw_points, draw_pointer = false;
    static double v1;
    static int[] ps, ps0;
    static double[] cd;
    static List<LongPolyhedron> Image, Domain, ReturnDomains, ReturnImages;
    static List<LongPolyhedron> Image_sim, Domain_sim;
    static LongPolyhedron POINTER;
    static List<List<Integer>> INDEX;
    static List<double[]> coeffs, coeffs_sim;
    static LongPolyhedron CUT, D, CUT_ref, CUT_chop;
    static Complex point, point_image = new Complex(0, 0);
    static double vol_ref, vol = 0;
    Point JX;

    public CanvasBase1() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        ps = new int[]{95, 130, 150};
        ps0 = new int[]{150, 130, 150};
        v1 = 0;
        Image = new ArrayList<LongPolyhedron>();
        Domain = new ArrayList<LongPolyhedron>();
        coeffs = new ArrayList<double[]>();
        coeffs_sim = new ArrayList<double[]>();
        ReturnDomains = new ArrayList<LongPolyhedron>();
        ReturnImages = new ArrayList<LongPolyhedron>();
        Domain_sim = new ArrayList<LongPolyhedron>();
        Image_sim = new ArrayList<LongPolyhedron>();
        cd = new double[]{-2, 1, -1, 1};

        D = new LongPolyhedron();
        D.count = 8;
        D.V[0] = new LongVector(0, 0, 1);
        D.V[1] = new LongVector(1.0 / 2, 1.0 / 2, 1);
        D.V[2] = new LongVector(1.0 / 2 + 2, 1.0 / 2, 1);
        D.V[3] = new LongVector(2, 0, 1);
        D.V[4] = new LongVector(0, 0, 2);
        D.V[5] = new LongVector(1, 1, 2);
        D.V[6] = new LongVector(1 + 2, 1, 2);
        D.V[7] = new LongVector(2, 0, 2);
        D = FiberBundleMap.move(D, new Complex(-0.5, -0.5), 1);
        D = FiberBundleMap.move(D, new Complex(-1, 0), 0);
        D.FACE = LongPolyFace.faceList(D);
        D.VOLUME = Volume.volume12(0, D);
        D.print();

        CUT = new LongPolyhedron();
        CUT.count = 8;
        CUT.V[0] = new LongVector(4.0 / 5, 4.0 / 5, 8.0 / 5);
        CUT.V[1] = new LongVector(6.0 / 5, 4.0 / 5, 8.0 / 5);
        CUT.V[2] = new LongVector(4.0 / 5, 2.0 / 5, 8.0 / 5);
        CUT.V[3] = new LongVector(2.0 / 5, 2.0 / 5, 8.0 / 5);
        CUT.V[4] = new LongVector(3.0 / 4, 3.0 / 4, 3.0 / 2);
        CUT.V[5] = new LongVector(5.0 / 4, 3.0 / 4, 3.0 / 2);
        CUT.V[6] = new LongVector(1.0, 1.0 / 2, 3.0 / 2);
        CUT.V[7] = new LongVector(1.0 / 2, 1.0 / 2, 3.0 / 2);
        CUT = FiberBundleMap.move(CUT, new Complex(-0.5, -0.5), 1);
        CUT = FiberBundleMap.move(CUT, new Complex(-1, 0), 0);
        CUT = CUT.sort();
        CUT.FACE = LongPolyFace.faceList(CUT);
        CUT.VOLUME = Volume.volume12(0, CUT);
        CUT.print();

        CUT_ref = inverse_Similarity(CUT, new Complex(0, 0), -Math.PI / 3, cd);

        INDEX = new ArrayList<List<Integer>>();
//        POINTER = new LongPolyhedron(CUT_ref);
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);

        if (draw_pointer) {
            PolygonWrapper pointer = TakeSlice(POINTER, v1);
            pointer = BasicPolygon.trans(pointer, ps[0], ps[1], ps[2] + 180);
            fill_frame(g, pointer, new Color(0, 0, 0, 80));
        }

        if (draw_domain) {
            PolygonWrapper d = TakeSlice(D, v1);
            PolygonWrapper cut = TakeSlice(CUT, v1);
            PolygonWrapper cut_ref = TakeSlice(CUT_ref, v1);

            PolygonWrapper[] polys = new PolygonWrapper[2];
            for (int i = 0; i < 2; i++) {
                polys[i] = BasicPolygon.trans(d, ps[0], ps[1] + i * 250, ps[2]);
                fill_frame(g, polys[i], new Color(255, 255, 255, 80));

                PolygonWrapper cut0 = BasicPolygon.trans(cut, ps[0],
                        ps[1] + i * 250, ps[2]);
                fill_frame(g, cut0, new Color(255, 0, 0, 80));

//                PolygonWrapper cut0_ref = BasicPolygon.trans(cut_ref, ps[0],
//                        ps[1] + i * 220, ps[2] + 180);
//                paint_frame(g, cut0_ref, Color.red, 2);
            }

            paint_general(g, Domain, ps[0], ps[1], ps[2], Color.orange, 1);
            paint_general(g, Image, ps[0], ps[1] + 250, ps[2], Color.cyan, 0);
            paint_general(g, ReturnDomains, ps[0], ps[1], ps[2], Color.red, 1);
            paint_general(g, ReturnImages, ps[0], ps[1] + 250, ps[2], Color.green, 1);
            paint_general(g, Domain_sim, ps[0] * 3, ps[1], ps[2] + 180, Color.red, 1);
            paint_general(g, Image_sim, ps[0] * 3, ps[1] + 250, ps[2] + 180, Color.green, 1);
        }

        if (draw_points) {
            Complex p = BasicPolygon.trans(point, ps[0], ps[1], ps[2]);
            p = transform(p.x, p.y);
            Complex q = BasicPolygon.trans(point_image, ps[0], ps[1], ps[2]);
            q = transform(q.x, q.y);
            Complex o = BasicPolygon.trans(point_image, ps[0], ps[1] + 250, ps[2]);
            o = transform(o.x, o.y);
            draw.drawDots(g, p, Color.red, 6);
            draw.drawDots(g, q, Color.blue, 6);
            draw.drawDots(g, o, Color.blue, 6);
        }
    }

    public static void Partition() {
        Domain = CanvasMaxDomain.Domain;
        coeffs = CanvasMaxDomain.coeffs;
        Image = CanvasMaxDomain.Image;

        for (LongPolyhedron domain : Domain) {
            LongPolyhedron P = new LongPolyhedron(domain);
            P = Similarity(P, new Complex(0, 0), -Math.PI / 3);
            ReturnDomains.add(P);
            LongPolyhedron Q = new LongPolyhedron(P);
            Q = inverse_Similarity(Q, new Complex(0, 0), -Math.PI / 3, cd);
            Domain_sim.add(Q);           
        }
        
        for (LongPolyhedron rd : ReturnDomains) {
            LongPolyhedron rm = LongPolyReturnMap.ReturnMap(rd, CUT, Domain);
            ReturnImages.add(rm);
            LongPolyhedron Q = new LongPolyhedron(rm);
            Q = inverse_Similarity(Q, new Complex(0, 0), -Math.PI / 3, cd);
            Image_sim.add(Q);
        }
        
        for(int i=0; i<Image.size(); i++){
            LongPolyhedron image = Image.get(i);
            image = Similarity(image, new Complex(0,0), -1.0 * Math.PI / 3);
            System.out.println("index: " + i + " Check conjugation");
            
            image.print();
            
            ReturnImages.get(i).print();
        }
    }

    public static void doSlice() {
        v1 = RotationAxis_Base1.s;
        draw_domain = true;
    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = J.X;
        point = unTransform(J.X);
        if (e.getButton() == MouseEvent.BUTTON1 & J.X.y < 250) {
            point = BasicPolygon.untrans(point, ps[0], ps[1], ps[2]);
            LongVector z = new LongVector(point.x, 1.0 * point.y / Math.sqrt(3), v1);
            int region = LongPolyReturnMap.region(z, Domain);
            LongVector w = LongPolyReturnMap.BundleMap(z, coeffs.get(region));
            point_image = new Complex(w.x[0], w.x[1] * Math.sqrt(3));
            draw_points = true;

            if (LongPolyCombinatorics.inside(z, CUT)) {
            }
        }
        repaint();
    }

    public static LongPolyhedron Similarity(LongPolyhedron P, Complex c,
            double theta) {
        LongPolyhedron W = new LongPolyhedron(P);
        W = BasicLongPolyhedron.Rotation(W, c, -Math.PI / 3);
        W = BasicLongPolyhedron.Flip(W, c);
        try {
            for (int i = 0; i < W.count; i++) {
                double t = W.V[i].x[2] / 2;
                W.V[i].x[0] = 1.0 / (3 * t + 1) * W.V[i].x[0];
                W.V[i].x[1] = 1.0 / (3 * t + 1) * W.V[i].x[1];
                W.V[i].x[2] = inverse_renorm(W.V[i].x[2]);
            }
            W = W.sort();
            W = LongPolyReturnMap.BundleMap0(W, cd[0], cd[1], cd[2], cd[3]);
            W.FACE = LongPolyFace.faceList(W);
            W.VOLUME = Volume.volume12(0, W);
        } catch (NullPointerException e) {

        }
        return (W);
    }

    public static LongPolyhedron inverse_Similarity(LongPolyhedron P, Complex c,
            double theta, double[] t) {
        LongPolyhedron W = new LongPolyhedron(P);
        W = LongPolyReturnMap.BundleMap0(W, -t[0], -t[1], -t[2], -t[3]);
//        for (int i = 0; i < W.count; i++) {
//            double s = W.V[i].x[2];
//            s = renorm(s);
//            s = 0.5 * s;
//            W.V[i].x[0] = W.V[i].x[0] * (3 * s + 1);
//            W.V[i].x[1] = W.V[i].x[1] * (3 * s + 1);
//        }
        W = BasicLongPolyhedron.Flip(W, c);
        W = BasicLongPolyhedron.Rotation(W, c, -theta);
        W = W.sort();
        return (W);
    }

    public static double inverse_renorm(double s0) {
        double s = 0.5 * s0;
        double t = 1.0 * (2 * s + 1) / (3 * s + 1);
        return (2 * t);
    }

    public static double renorm(double t0) {
        double t = 0.5 * t0;
        double s = 1.0 * (1 - t) / (3 * t - 2);
        return (2 * s);
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
        repaint();
    }

    public void paint_general(Graphics2D g, List<LongPolyhedron> PList, int n0,
            int n1, int n2, Color c, int mode) {
        for (int i = 0; i < PList.size(); i++) {
            try {
                PolygonWrapper p = TakeSlice(PList.get(i), v1);
                p = BasicPolygon.trans(p, n0, n1, n2);
                paint_frame(g, p, c, 2);
                fill_frame(g, p, new Color(255, 255, 255, 80));
                if (mode == 1) {
                    for (int j = 0; j < p.count; j++) {
                        g.setFont(new Font("TimesRoman", Font.BOLD, 10));
                        g.setColor(Color.CYAN);
                        Complex p0 = transform(p.center().x, p.center().y);
                        g.drawString(Integer.toString(i), (int) p0.x, (int) p0.y);
                    }
                }
            } catch (NullPointerException e) {
            }
        }
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

    public static PolygonWrapper TakeSlice(LongPolyhedron P, double v) {
        P.SCALE = 1;
        Polyhedron PP = P.toPolyhedron();
        PolygonWrapper PW = PolyhedronSlicer.basicSlice(PP, v);
        for (int i = 0; i < PW.count; i++) {
            PW.z[i].y = PW.z[i].y * Math.sqrt(3);
        }
        return (PW);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char ch = e.getKeyChar();
        int test = 0;
        if (ch == 'd') {
            test = 2;
        }
        if (ch == 'f') {
            test = 1;
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

}
