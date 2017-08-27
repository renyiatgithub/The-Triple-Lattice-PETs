package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasGoldenRatio extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    static PolygonWrapper[] P;
    DrawMe draw = new DrawMe();
    static boolean draw_tiling, draw_map, draw_pt = false;
    static int[] ps, ps0, ps1;
    static double s0, r0 = 0;
    static double s1, r1 = 0;
    static double s = 0;

    static PolygonWrapper[] domainA;
    static PolygonWrapper[] domainB;
    static PolygonWrapper[] domain_golden;
    static PolygonWrapper[] image_golden;
    static PolygonWrapper[] return_golden0;
    static PolygonWrapper[] image_return_golden0;
    static PolygonWrapper[] return_golden, returnA, returnB;
    static PolygonWrapper[] image_return_golden, image_returnA, image_returnB;

    Point JX;

    public CanvasGoldenRatio() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        ps = new int[]{90, 140, 100};
        ps0 = new int[]{90, 140, 380};
        ps1 = new int[]{90, 155, 240};
        domainA = new PolygonWrapper[12];
        domainB = new PolygonWrapper[12];
        domain_golden = new PolygonWrapper[12];
        image_golden = new PolygonWrapper[12];
        return_golden0 = new PolygonWrapper[12];
        image_return_golden0 = new PolygonWrapper[12];
        return_golden = new PolygonWrapper[12];
        image_return_golden = new PolygonWrapper[12];
        returnA = new PolygonWrapper[14];
        returnB = new PolygonWrapper[14];
        image_returnA = new PolygonWrapper[12];
        image_returnB = new PolygonWrapper[12];

    }

    @Override
    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);

        if (draw_map) {
            for (int i = 0; i < domain_golden.length; i++) {
                PolygonWrapper d = new PolygonWrapper(domain_golden[i]);
                Complex[] ordered_verticies = BasicPolygon.order_vertex(d);
                Complex ds = new Complex(d.center().x, d.center().y);
                d = BasicPolygon.trans(d, ps[0],ps[1], ps[2]);
                fill_frame(g, d, new Color(255, 255, 204, 90));
                paint_frame(g, d, Color.orange, 3);
                ds = BasicPolygon.trans(ds, ps[0], ps[1], ps[2]);
                ds = transform(ds.x, ds.y);
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);
            }

            for (int i = 0; i < image_golden.length; i++) {
                PolygonWrapper d = new PolygonWrapper(image_golden[i]);
                Complex[] ordered_verticies = BasicPolygon.order_vertex(d);
                Complex ds = new Complex(d.center().x, d.center().y);
                d = BasicPolygon.trans(d, ps[0], ps[1] + 230, ps[2]);
                fill_frame(g, d, new Color(255, 255, 204, 90));
                paint_frame(g, d, Color.orange, 3);
                ds = BasicPolygon.trans(ds, ps[0], ps[1] + 230, ps[2]);
                ds = transform(ds.x, ds.y);
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);
            }

            for (int i = 0; i < return_golden0.length; i++) {
                PolygonWrapper d = new PolygonWrapper(return_golden0[i]);
                Complex[] ordered_verticies = BasicPolygon.order_vertex(d);
                Complex ds = new Complex(d.center().x, d.center().y);
                d = BasicPolygon.trans(d, ps1[0],
                        ps1[1], ps1[2]);
                fill_frame(g, d, new Color(255, 255, 204, 90));
                paint_frame(g, d, Color.cyan, 3);
                ds = BasicPolygon.trans(ds, ps1[0], ps1[1], ps1[2]);
                ds = transform(ds.x, ds.y);
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);
            }

            for (int i = 0; i < image_return_golden0.length; i++) {
                PolygonWrapper d = new PolygonWrapper(image_return_golden0[i]);
                Complex[] ordered_verticies = BasicPolygon.order_vertex(d);
                Complex ds = new Complex(d.center().x, d.center().y);
                d = BasicPolygon.trans(d, ps1[0],
                        ps1[1] + 230, ps1[2]);
                fill_frame(g, d, new Color(255, 255, 204, 90));
                paint_frame(g, d, Color.cyan, 3);
                ds = BasicPolygon.trans(ds, ps1[0], ps1[1] + 230, ps1[2]);
                ds = transform(ds.x, ds.y);
                g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);
            }

            for (int i = 0; i < return_golden.length; i++) {
                PolygonWrapper d = new PolygonWrapper(return_golden[i]);
                
                Complex[] ordered_verticies = BasicPolygon.order_vertex(d);
                Complex ds = new Complex(d.center().x, d.center().y);
                d = BasicPolygon.trans(d, ps0[0],
                        ps0[1], ps0[2]);
                fill_frame(g, d, new Color(255, 255, 204, 90));
                paint_frame(g, d, Color.cyan, 3);
                ds = BasicPolygon.trans(ds, ps0[0], ps0[1], ps0[2]);
                ds = transform(ds.x, ds.y);
                g.drawString(Integer.toString(return_golden[i].index), (int) ds.x,
                        (int) ds.y);
            }

            for (int i = 0; i < image_return_golden.length; i++) {
                PolygonWrapper d = new PolygonWrapper(image_return_golden[i]);
                Complex[] ordered_verticies = BasicPolygon.order_vertex(d);
                Complex ds = new Complex(d.center().x, d.center().y);
                d = BasicPolygon.trans(d, ps0[0],
                        ps0[1] + 230, ps0[2]);
                fill_frame(g, d, new Color(255, 255, 204, 90));
                paint_frame(g, d, Color.cyan, 3);
                ds = BasicPolygon.trans(ds, ps0[0], ps0[1] + 230, ps0[2]);
                ds = transform(ds.x, ds.y);
                g.drawString(Integer.toString(image_return_golden[i].index), (int) ds.x,
                        (int) ds.y);
            }

        }
    }

    public void Setup() {
        s0 = 1.0 * 8 / 13;
        s1 = 1.0 * 13 / 21;
        r0 = 1.0 * 5 / 8;
        r1 = s0;
        s = 1.0 * (Math.sqrt(5) - 1) / 2;
        domainA = BasicPolygon.sort(Pet.domain(s0));
        domainB = BasicPolygon.sort(Pet.domain(s1));

        domain_golden = BasicPolygon.sort(Pet.domain(s));
        image_golden = BasicPolygon.sort(Pet.map_list(Pet.domain(s)));

        List<PolygonWrapper> ReturnSet = new ArrayList<PolygonWrapper>();
        PolygonWrapper return_set = FirstReturn.returnSet_SS(s);
        ReturnSet.add(return_set);
        return_golden0 = BasicPolygon.sort(FirstReturn.returnCell_list(s, ReturnSet));
        return_golden = ConjugationMap.return_cell_list(s);
        returnA = ConjugationMap.return_cell_list(s0);
        returnB = ConjugationMap.return_cell_list(s1);

//        return_golden = BasicPolygon.sort(return_golden);
        List<PolygonWrapper> list_return_golden = FirstReturn.returnCell_list(s, ReturnSet);
        List<PolygonWrapper> image = Pet.map_list(list_return_golden);
        image_return_golden0 = BasicPolygon.copy_list(image);

        for (int i = 0; i < return_golden.length; i++) {
            for (int j = 0; j < domain_golden.length; j++) {
                Complex center_return = return_golden[i].center();
                if (BasicPolygon.contain(domain_golden[j], return_golden[i])) {
                    return_golden[i].index = j;
                }
            }
        }

        for (int i = 0; i < returnA.length; i++) {
            for (int j = 0; j < domainA.length; j++) {
                Complex center_return = returnA[i].center();
                if (PolygonWrapper.contains(domainA[j], center_return)) {
                    returnA[i].index = j;
                }
            }
        }

        for (int i = 0; i < returnB.length; i++) {
            for (int j = 0; j < domainB.length; j++) {
                Complex center_return = returnB[i].center();
                if (PolygonWrapper.contains(domainB[j], center_return)) {
                    returnB[i].index = j;
                }
            }
        }
        image_return_golden = Pet.map_array(return_golden);
        image_returnA = Pet.map_array(returnA);
        image_returnB = Pet.map_array(returnB);

        List<double[][][]> coeffs = rationalize_polylist();
        draw_map = true;
        
        double sum_volume = 0;
        for(int i=0; i<domain_golden.length; i++){
            for(int j=i+1; j<domain_golden.length; j++){
                PolygonWrapper overlap = PolygonWrapper.intersect(domain_golden[i], domain_golden[j]);
                if(overlap !=null && overlap.area() > Math.pow(10, -8)){
                    System.out.println((char)27 + "[31mWrong!!!" + (char)27 + "[0m");
                }
            }
            sum_volume = sum_volume + domain_golden[i].area();
        }
        sum_volume = sum_volume / (Math.sqrt(3) * s);
        int[] rat_v_sum = MathRational.approximate(sum_volume, Math.pow(10, -10));
        System.out.println("volume sum of domains: " + sum_volume + " " +
                rat_v_sum[0] + "/" + rat_v_sum[1]);
         System.out.println((char)27 + "[32mdomain separateness checked" + (char)27 + "[0m");
         
         
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

    public static double[][] rationalize_complex(Complex z0, Complex z1,
            double x0, double x1) {
        double[][] coeff = new double[2][2];
        double z0_y = 1.0 * z0.y / Math.sqrt(3);
        double z1_y = 1.0 * z1.y / Math.sqrt(3);
        coeff[0] = rationalize(z0.x, z1.x, x0, x1);
        coeff[1] = rationalize(z0_y, z1_y, x0, x1);

        int[] scalar0 = MathRational.approximate(coeff[0][0], Math.pow(10, -10));
        int[] const0 = MathRational.approximate(coeff[0][1], Math.pow(10, -10));
        int[] scalar1 = MathRational.approximate(coeff[1][0], Math.pow(10, -10));
        int[] const1 = MathRational.approximate(coeff[1][1], Math.pow(10, -10));
        System.out.println( " vectors: " + scalar0[0] + "/" + scalar0[1] + " * s + "
                + const0[0] + "/" + const0[1] + ", (" + scalar1[0] + "/"
                + scalar1[1] + " *s + " + const1[0] + "/" + const1[1] + ") * sqrt 3");
        
        return (coeff);
    }

    public static double[][][] rationalize_polygon(PolygonWrapper P0, PolygonWrapper P1,
            double x0, double x1) {
        double[][][] coeff = new double[P0.count][2][2];
        if (P0.count == P1.count) {
            Complex[] Z0 = BasicPolygon.order_vertex(P0);
            Complex[] Z1 = BasicPolygon.order_vertex(P1);
            for (int i = 0; i < Z0.length; i++) {
                coeff[i] = rationalize_complex(Z0[i], Z1[i], x0, x1);
            }
        }
        return (coeff);
    }

    public static List<double[][][]> rationalize_polylist() {
        List<double[][][]> coeffs = new ArrayList<>();
        System.out.println(s0);
        System.out.println(s1);
        domainA = new PolygonWrapper[12];
        domainB = new PolygonWrapper[12];
        domainA = BasicPolygon.sort(Pet.domain(s0));
        domainB = BasicPolygon.sort(Pet.domain(s1));
        domain_golden = BasicPolygon.sort(Pet.domain(s));

        if (domainA.length == domainB.length) {
            for (int i = 0; i < domainA.length; i++) {
                System.out.println();
                System.out.println("index " + i);
                Complex[] ordered_golden = BasicPolygon.order_vertex(domain_golden[i]);
                double[][][] coeff = rationalize_polygon(domainA[i], domainB[i], s0, s1);
                System.out.println(" translation vectors : ");
                double[][] vector = rationalize_complex(domainA[i].vector, domainB[i].vector, s0, s1);                
                coeffs.add(coeff);
            }
        }

        for (int i = 0; i < returnA.length; i++) {
            for (int j = 0; j < returnB.length; j++) {
                if (returnA[i].index == returnB[j].index
                        && returnA[i].count == returnB[j].count) {
                    System.out.println();
                    System.out.println("index (return domain): " + returnA[i].index);
                    double[][][] coeff = rationalize_polygon(returnA[i], returnB[j], r0, r1);
                    System.out.println(" translation vectors : ");
                    double[][] return_vector = rationalize_complex(returnA[i].vector,
                            returnB[j].vector, r0, r1);
                }
            }
        }

        return (coeffs);
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
