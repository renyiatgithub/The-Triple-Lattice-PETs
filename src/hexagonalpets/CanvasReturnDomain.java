package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CanvasReturnDomain extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    DrawMe draw = new DrawMe();
    static boolean draw_domain, draw_points, draw_pointer = false;
    static double v1;
    static int[] ps;
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

    public CanvasReturnDomain() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        ps = new int[]{95, 130, 150};
        v1 = 0;
        Image = new ArrayList<LongPolyhedron>();
        Domain = new ArrayList<LongPolyhedron>();
        coeffs = new ArrayList<double[]>();
        coeffs_sim = new ArrayList<double[]>();
        ReturnDomains = new ArrayList<LongPolyhedron>();
        ReturnImages = new ArrayList<LongPolyhedron>();
        Domain_sim = new ArrayList<LongPolyhedron>();
        Image_sim = new ArrayList<LongPolyhedron>();
        cd = new double[]{0, 0, 0, 0};

        CUT = new LongPolyhedron();
        CUT.count = 6;
        CUT.V[0] = new LongVector(0, 0, 1);
        CUT.V[1] = new LongVector(1, 0, 1);
        CUT.V[2] = new LongVector(0.5, 0.5, 1);
        CUT.V[3] = new LongVector(1.5, 0.5, 1);
        CUT.V[4] = new LongVector(0, 0, 2);
        CUT.V[5] = new LongVector(1, 1, 2);
        CUT = FiberBundleMap.move(CUT, new Complex(-0.5, -0.5), 1);
        CUT = FiberBundleMap.move(CUT, new Complex(-1, 0), 0);
        CUT.FACE = LongPolyFace.faceList(CUT);
        CUT.VOLUME = Volume.volume12(0, CUT);

        CUT_chop = new LongPolyhedron(CUT);
        CUT_chop = LongPolyIntersect.chop(CUT_chop, 4, 3, 1);
        CUT_chop.VOLUME = Volume.volume12(0, CUT_chop);
        vol_ref = CUT_chop.VOLUME;

        CUT_ref = LongPolyReturnMap.preConjugation(CUT_chop, CUT, -1.0 * Math.PI / 3);
        cd = FiberBundleMap.coding(CUT_ref);
        CUT_ref = LongPolyReturnMap.BundleMap(CUT_ref, cd);
     
        INDEX = new ArrayList<List<Integer>>();
        INDEX.add(Arrays.asList(5, 4, 10, 5));
        
        POINTER = new LongPolyhedron(CUT_ref);
    }

    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        
        if(draw_pointer){
            PolygonWrapper pointer = TakeSlice(POINTER, v1);
            pointer = BasicPolygon.trans(pointer, ps[0], ps[1], ps[2]+ 180);
            fill_frame(g, pointer, new Color(0,0,0,80));
        }

        if (draw_domain) {
            PolygonWrapper cut = TakeSlice(CUT, v1);
            PolygonWrapper cut_ref = TakeSlice0(CUT_ref, v1);

            PolygonWrapper[] polys = new PolygonWrapper[2];
            for (int i = 0; i < 2; i++) {
                polys[i] = BasicPolygon.trans(cut, ps[0], ps[1] + i * 220, ps[2]);
                fill_frame(g, polys[i], new Color(255, 0, 0, 80));

                PolygonWrapper cut_ref0 = BasicPolygon.trans(cut_ref, ps[0],
                        ps[1] + i * 220, ps[2] + 180);
                paint_frame(g, cut_ref0, Color.red, 2);
            }

            paint_general(g, Domain, ps[0], ps[1], ps[2], Color.orange, 0);
            paint_general(g, Image, ps[0], ps[1] + 220, ps[2], Color.cyan, 0);
            paint_general(g, ReturnDomains, ps[0], ps[1], ps[2], Color.red, 1);
            paint_general(g, ReturnImages, ps[0], ps[1] + 220, ps[2], Color.green, 1);

            paint_general0(g, Domain_sim, ps[0], ps[1], ps[2] + 180, Color.red, 0);
            paint_general0(g, Image_sim, ps[0], ps[1] + 220, ps[2] + 180, Color.green, 0);
        }

        if (draw_points) {
            Complex p = BasicPolygon.trans(point, ps[0], ps[1], ps[2]);
            p = transform(p.x, p.y);
            Complex q = BasicPolygon.trans(point_image, ps[0], ps[1], ps[2]);
            q = transform(q.x, q.y);
            Complex o = BasicPolygon.trans(point_image, ps[0], ps[1] + 220, ps[2]);
            o = transform(o.x, o.y);
            draw.drawDots(g, p, Color.red, 6);
            draw.drawDots(g, q, Color.blue, 6);
            draw.drawDots(g, o, Color.blue, 6);
        }
    }

    public static void Partition() {
        Domain = CanvasMaxDomain.Domain;
        coeffs = CanvasMaxDomain.coeffs;
        int count = 0;
        for (LongPolyhedron d : Domain) {
            Image.add(LongPolyReturnMap.BundleMap(d, coeffs.get(count)));
            count++;
        }

        for (int i = 0; i < 250; i++) {
            LongVector z = new LongVector(-5.0 / 4 + Math.random(),
                    -1.5 + 2 * Math.random(), 1 + Math.random() / 3);
            if (LongPolyCombinatorics.inside(z, CUT)) {
                List<Integer> index_list = LongPolyReturnMap.trackIndex(z, CUT, Domain);
                if (INDEX.contains(index_list) == false) {
                    System.out.println();
                    INDEX.add(index_list);
                    LongPolyhedron rd = LongPolyReturnMap.ReturnDomain(index_list, z, Domain, CUT);
                    rd = LongPolyIntersect.chop(rd, 4, 3, 1);
                    rd.VOLUME = Volume.volume12(0, rd);
                    ReturnDomains.add(rd);

                    LongPolyhedron rd_image = new LongPolyhedron();
                    rd_image = LongPolyReturnMap.BundleMap(rd, rd.trans);
                    ReturnImages.add(rd_image);

                    LongPolyhedron domain_sim = LongPolyReturnMap.conjugation(rd, CUT,
                            -1.0 * Math.PI / 3, cd).sort();

                    LongPolyhedron image_sim = LongPolyReturnMap.conjugation(rd_image, CUT,
                            -1.0 * Math.PI / 3, cd).sort();
                    double[] cd = FiberBundleMap.coding(domain_sim, image_sim);
                    domain_sim.trans = cd;
                    Domain_sim.add(domain_sim);
                    Image_sim.add(image_sim);

                }
            }
        }

    }

    public static void doSlice() {
        v1 = RotationAxis_Return.s;
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

            if (LongPolyCombinatorics.inside(z, CUT)) {
                try {
                    List<Integer> index_list = LongPolyReturnMap.trackIndex(z, CUT, Domain);
                    for (int j = 0; j < index_list.size(); j++) {
                        System.out.print(" " + index_list.get(j) + " ");
                    }
                    System.out.println();

                    LongPolyhedron rd = LongPolyReturnMap.ReturnDomain(index_list, z, Domain, CUT);
                    rd = LongPolyIntersect.chop(rd, 4, 3, 1);
                    rd.VOLUME = Volume.volume12(0, rd);
                    rd.print();

                    LongPolyhedron rd_image = LongPolyReturnMap.BundleMap(rd, rd.trans);

                    LongPolyhedron domain_sim = LongPolyReturnMap.conjugation(rd, CUT,
                            -1.0 * Math.PI / 3, cd).sort();
                    LongPolyhedron image_sim = LongPolyReturnMap.conjugation(rd_image, CUT,
                            -1.0 * Math.PI / 3, cd).sort();
                    double[] cd = FiberBundleMap.coding(domain_sim, image_sim);
                    domain_sim.trans = cd;
                    POINTER = domain_sim;
                    
                    System.out.println("Maximal return domains: ");

                    if (INDEX.contains(index_list) == false) {
                        INDEX.add(index_list);
                        ReturnDomains.add(rd);
                        ReturnImages.add(rd_image);
                        Domain_sim.add(domain_sim.sort());
                        Image_sim.add(image_sim.sort());
                    }
                } catch (NullPointerException ne) {

                }
            }
            draw_points = true;
        }
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

    public void paint_general0(Graphics2D g, List<LongPolyhedron> PList, int n0,
            int n1, int n2, Color c, int mode) {
        for (int i = 0; i < PList.size(); i++) {
            try {
                PolygonWrapper p = TakeSlice0(PList.get(i), v1);
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

    public static PolygonWrapper TakeSlice0(LongPolyhedron P, double v) {
        P.SCALE = 1;
        Polyhedron PP = P.toPolyhedron();
        PolygonWrapper PW = PolyhedronSlicer.basicSlice(PP, v);
        double s = 2.0 / v;
        for (int i = 0; i < PW.count; i++) {
            PW.z[i].x = PW.z[i].x * s;
            PW.z[i].y = PW.z[i].y * Math.sqrt(3) * s;
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
