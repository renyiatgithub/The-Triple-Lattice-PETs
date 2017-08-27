package hexagonalpets;

import static hexagonalpets.CanvasSymmetry.cellsA_rot;
import static hexagonalpets.CanvasSymmetry.rationalize_polygon;
import static hexagonalpets.CanvasSymmetry.rationalize_polylist;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasReflection extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    static PolygonWrapper[] P;
    DrawMe draw = new DrawMe();
    static boolean draw_tiling, draw_cells, draw_pt = false;
    static int[] ps, ps0, ps1;
    static double s0, r0 = 0;
    static double s1, r1 = 0;
    static double s = 0;

    static PolygonWrapper frame0, frame1;
    static PolygonWrapper[] domainA, domainB;
    static PolygonWrapper[] domain_golden, domain_sym, domain_map;
    static PolygonWrapper[] image_golden, image_sym, image_map;
    static PolygonWrapper[] symA, symB;

    Point JX;

    public CanvasReflection() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        ps = new int[]{140, 140, 100};
        ps1 = new int[]{140, 155, 240};

        frame0 = new PolygonWrapper();
        frame1 = new PolygonWrapper();

        domainA = new PolygonWrapper[10];
        domainB = new PolygonWrapper[10];
        domain_golden = new PolygonWrapper[10];
        image_golden = new PolygonWrapper[10];
        domain_sym = new PolygonWrapper[10];
        image_sym = new PolygonWrapper[10];
        domain_map = new PolygonWrapper[10];
        image_map = new PolygonWrapper[10];

    }

    @Override
    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);

        if (draw_cells) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    paint_frame(g, BasicPolygon.trans(frame0, ps[0],
                            ps[1] + i * 200, ps[2] + j * 140), Color.white, 1);
                    paint_frame(g, BasicPolygon.trans(frame1,
                            ps[0], ps[1] + i * 200, ps[2] + j * 140), Color.cyan, 1);

                }
            }

            for (int i = 0; i < domain_golden.length; i++) {
                PolygonWrapper d0 = new PolygonWrapper(domain_golden[i]);
                Complex[] ordered_verticies = BasicPolygon.order_vertex(d0);
                Complex ds = new Complex(d0.center().x, d0.center().y);
                PolygonWrapper d = BasicPolygon.trans(d0, ps[0], ps[1], ps[2]);
                fill_frame(g, d, new Color(255, 255, 204, 90));
                paint_frame(g, d, Color.white, 1);
                ds = BasicPolygon.trans(ds, ps[0], ps[1], ps[2]);
                ds = transform(ds.x, ds.y);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 9));
                g.setColor(Color.orange);
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);

            }

            for (int i = 0; i < image_golden.length; i++) {
                PolygonWrapper d = new PolygonWrapper(image_golden[i]);
                Complex[] ordered_verticies = BasicPolygon.order_vertex(d);
                Complex ds = new Complex(d.center().x, d.center().y);
                d = BasicPolygon.trans(d, ps[0], ps[1] + 200, ps[2]);
                fill_frame(g, d, new Color(255, 255, 204, 90));
                paint_frame(g, d, Color.white, 1);
                ds = BasicPolygon.trans(ds, ps[0], ps[1] + 200, ps[2]);
                ds = transform(ds.x, ds.y);
                g.setFont(new Font("TimesRoman", Font.PLAIN, 9));
                g.setColor(Color.orange);
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);
            }

            paint_frames_array(g, domain_map, ps[0], ps[1], ps[2] + 140, Color.white,
                    Color.orange, 0);
            paint_frames_array(g, image_map, ps[0], ps[1] + 200, ps[2] + 140, Color.white,
                    Color.orange, 0);
            paint_frames_array(g, symA, ps[0], ps[1], ps[2] + 280, Color.cyan,
                    Color.green, 1);
            paint_frames_array(g, symB, ps[0], ps[1] + 200, ps[2] + 280, Color.cyan,
                    Color.green, 1);

        }
    }

    public void Setup() {
        s0 = 1.0 * 8 / 13;
        s1 = 1.0 * 13 / 21;
        s = 1.0 * (Math.sqrt(5) - 1) / 2;
        frame0 = FirstReturn.ReflectionSet(s).get(0);
        List<PolygonWrapper> frames0 = new ArrayList<PolygonWrapper>();
        frames0.add(frame0);

        frame1 = FirstReturn.ReflectionSet(s).get(1);
        List<PolygonWrapper> frames1 = new ArrayList<PolygonWrapper>();
        frames1.add(frame1);

        List<PolygonWrapper> domain = FirstReturn.inv_returnCell_list(s, frames0);
        domain_golden = BasicPolygon.sort(domain);
        List<PolygonWrapper> image_golden_list = Pet.map_list(BasicPolygon.copy_array(domain_golden));
        image_golden = BasicPolygon.copy_list(image_golden_list);

        List<PolygonWrapper> domain_s = FirstReturn.returnCell_list(s, frames1);
        domain_sym = BasicPolygon.sort(domain_s);
        List<PolygonWrapper> image_sym_list = Pet.map_list(BasicPolygon.copy_array(domain_sym));
        image_sym = BasicPolygon.copy_list(image_sym_list);

        for (int i = 0; i < domain_map.length; i++) {
            domain_map[i] = BasicPolygon.flip_vertical(domain_golden[i], frame0.z[2]);
            domain_map[i].index = i;
        }
        for (int i = 0; i < image_map.length; i++) {
            image_map[i] = BasicPolygon.flip_vertical(image_golden[i], frame0.z[2]);
            image_map[i].index = i;
        }
        domain_sym = CanvasSymmetry.assign_index(domain_map, domain_sym);
        image_sym = CanvasSymmetry.assign_index(image_map, image_sym);

        PolygonWrapper frame0_domainA = FirstReturn.ReflectionSet(s0).get(0);
        List<PolygonWrapper> frames0_domainA = new ArrayList<PolygonWrapper>();
        frames0_domainA.add(frame0_domainA);
        List<PolygonWrapper> domain_listA = FirstReturn.inv_returnCell_list(s0, frames0_domainA);
        domain_listA = BasicPolygon.copy_array(BasicPolygon.sort(domain_listA));
        domain_listA.remove(7);
        domainA = BasicPolygon.sort(domain_listA);
        PolygonWrapper temp = domainA[8];
        domainA[8] = domainA[9];
        domainA[9] = temp;
        for (int i = 0; i < domainA.length; i++) {
            domainA[i] = BasicPolygon.flip_vertical(domainA[i], frame0_domainA.z[2]);
            domainA[i].vector = new Complex(2 * frame0_domainA.z[2].x - domainA[i].vector.x, domainA[i].vector.y);
            domainA[i].index = i;
        }

        PolygonWrapper frame0_domainB = FirstReturn.ReflectionSet(s1).get(0);
        List<PolygonWrapper> frames0_domainB = new ArrayList<PolygonWrapper>();
        frames0_domainB.add(frame0_domainB);
        List<PolygonWrapper> domain_listB = FirstReturn.inv_returnCell_list(s1, frames0_domainB);
        domainB = BasicPolygon.sort(domain_listB);
        for (int i = 0; i < domainB.length; i++) {
            domainB[i] = BasicPolygon.flip_vertical(domainB[i], frame0_domainB.z[2]);
            domainB[i].vector = new Complex(2 * frame0_domainB.z[2].x - domainB[i].vector.x, domainB[i].vector.y);
            domainB[i].index = i;
        }
        List<double[][][]> parametrize_cells = CanvasSymmetry.rationalize_polylist(domainA,
                domainB, domain_map, s0, s1, s, 1);

        PolygonWrapper frame1_domainA = FirstReturn.ReflectionSet(s0).get(1);
        List<PolygonWrapper> frames1_domainA = new ArrayList<PolygonWrapper>();
        frames1_domainA.add(frame1_domainA);
        List<PolygonWrapper> sym_listA = FirstReturn.returnCell_list(s0, frames1_domainA);
        symA = BasicPolygon.sort(sym_listA);

        System.out.println("Symmetry domains: ");
        PolygonWrapper frame1_domainB = FirstReturn.ReflectionSet(s1).get(1);
        List<PolygonWrapper> frames1_domainB = new ArrayList<PolygonWrapper>();
        frames1_domainB.add(frame1_domainB);
        List<PolygonWrapper> sym_listB = FirstReturn.returnCell_list(s1, frames1_domainB);
        symB = BasicPolygon.sort(sym_listB);
        symA = CanvasSymmetry.assign_index(domain_sym, symA);
        symB = CanvasSymmetry.assign_index(domain_sym, symB);
        List<double[][][]> parametrize_sym = CanvasSymmetry.rationalize_polylist(symA,
                symB, domain_sym, s0, s1, s, 1);
        draw_cells = true;

        repaint();
    }

    public void mouseClicked(MouseEvent e) {
        MouseData J = MouseData.process(e);
        JX = J.X;
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

    public static double[] rationalize(double m0, double m1, double x0,
            double x1) {
        double[] coeff = new double[2];
        coeff[0] = 1.0 * (m1 - m0) / (x1 - x0);
        coeff[1] = m1 - coeff[0] * x1;
        return (coeff);
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

    public void paint_frames_array(Graphics2D g, PolygonWrapper[] PL,
            int x, int y, int z, Color c, Color c0, int n) {
        for (int i = 0; i < PL.length; i++) {
            PolygonWrapper d = new PolygonWrapper(PL[i]);
            Complex ds = new Complex(d.center().x, d.center().y);
            d = BasicPolygon.trans(d, x, y, z);
            fill_frame(g, d, new Color(255, 255, 204, 90));
            paint_frame(g, d, c, 1);
            ds = BasicPolygon.trans(ds, x, y, z);
            ds = transform(ds.x, ds.y);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 8));
            g.setColor(c0);
            if (n == 0) {
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);
            }
            if (n == 1) {
                g.drawString(Integer.toString(PL[i].index), (int) ds.x, (int) ds.y);
            }

        }
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
