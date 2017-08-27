package hexagonalpets;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasSymmetry extends ScaleCanvas implements MouseListener,
        MouseMotionListener, KeyListener {

    static PolygonWrapper frame0, frame1, frame2, frame0_A, frame1_A, frame0_B,
            frame1_B, frame2_A, frame2_B;
    DrawMe draw = new DrawMe();
    static int[] ps0, ps1, ps2;
    static List<PolygonWrapper> cells, cells_sym, cells_rot, image_rot, image_cells,
            image_sym, cells_sym2, image_sym2;
    static double s0, r0 = 0;
    static double s1, r1 = 0;
    static double s = 0;
    static boolean draw_cells = false;
    static PolygonWrapper[] cellsA, cellsB, imageA, imageB, cells_ref, image_ref;
    static PolygonWrapper[] cellsA_rot, cellsB_rot, cellsA_sym, cellsB_sym;
    static PolygonWrapper[] cellsA_ref, cellsB_ref, cellsA_sym2, cellsB_sym2;

    Point JX;

    public CanvasSymmetry() {
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        setScales(0, 0, 1);
        ps0 = new int[]{90, 150, 100};
        ps1 = new int[]{90, 150, 240};
        ps2 = new int[]{90, 150, 380};
        frame0 = new PolygonWrapper();
        frame1 = new PolygonWrapper();
        frame2 = new PolygonWrapper();
        frame0_A = new PolygonWrapper();
        frame1_A = new PolygonWrapper();
        frame0_B = new PolygonWrapper();
        frame1_B = new PolygonWrapper();
        frame2_A = new PolygonWrapper();
        frame2_B = new PolygonWrapper();
        cells = new ArrayList<PolygonWrapper>();
        cells_sym = new ArrayList<PolygonWrapper>();
        cells_sym2 = new ArrayList<PolygonWrapper>();
        cells_rot = new ArrayList<PolygonWrapper>();
        image_rot = new ArrayList<PolygonWrapper>();
        image_cells = new ArrayList<PolygonWrapper>();
        image_sym = new ArrayList<PolygonWrapper>();
        image_sym2 = new ArrayList<PolygonWrapper>();
        cells_ref = new PolygonWrapper[13];
        image_ref = new PolygonWrapper[13];
        cellsA = new PolygonWrapper[13];
        cellsB = new PolygonWrapper[13];
        cellsA_rot = new PolygonWrapper[13];
        cellsB_rot = new PolygonWrapper[13];
        cellsA_sym = new PolygonWrapper[13];
        cellsB_sym = new PolygonWrapper[13];
        imageA = new PolygonWrapper[13];
        imageB = new PolygonWrapper[13];
        cellsA_ref = new PolygonWrapper[13];
        cellsB_ref = new PolygonWrapper[13];
        cellsA_sym2 = new PolygonWrapper[13];
        cellsB_sym2 = new PolygonWrapper[13];
    }

    @Override
    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);

        if (draw_cells) {
            PolygonWrapper line = new PolygonWrapper();
            line.count = 2;
            line.z[0] = new Complex(500, 0);
            line.z[1] = new Complex(500, 500);
            paint_frame(g, line, Color.white, 2);

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 2; k++) {
                        paint_frame(g, BasicPolygon.trans(frame0, ps0[0],
                                ps0[1] + k * 500 + i * 200, ps0[2] + j * 140), Color.white, 1);
                        paint_frame(g, BasicPolygon.trans(frame1,
                                ps0[0], ps0[1] + k * 500 + i * 200, ps0[2] + j * 140), Color.cyan, 1);
                        paint_frame(g, BasicPolygon.trans(frame2,
                                ps0[0], ps0[1] + k * 500 + i * 200, ps0[2] + j * 140), Color.pink, 1);
                    }
                }
            }

            PolygonWrapper[] CELLS = BasicPolygon.sort(cells);
            for (int i = 0; i < CELLS.length; i++) {
                for (int k = 0; k < 2; k++) {
                    PolygonWrapper d = new PolygonWrapper(CELLS[i]);
                    Complex ds = new Complex(d.center().x, d.center().y);
                    d = BasicPolygon.trans(d, ps0[0], k * 500 + ps0[1], ps0[2]);
                    fill_frame(g, d, new Color(255, 255, 204, 90));
                    paint_frame(g, d, Color.white, 1);
                    ds = BasicPolygon.trans(ds, ps0[0], k * 500 + ps0[1], ps0[2]);
                    ds = transform(ds.x, ds.y);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 9));
                    g.setColor(Color.orange);
                    g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);
                }
            }

            PolygonWrapper[] IMAGE = BasicPolygon.copy_list(image_cells);
            for (int i = 0; i < IMAGE.length; i++) {
                for (int k = 0; k < 2; k++) {
                    PolygonWrapper d = new PolygonWrapper(IMAGE[i]);
                    Complex ds = new Complex(d.center().x, d.center().y);
                    d = BasicPolygon.trans(d, ps0[0], ps0[1] + k * 500 + 200, ps0[2]);
                    fill_frame(g, d, new Color(255, 255, 204, 90));
                    paint_frame(g, d, Color.white, 1);
                    ds = BasicPolygon.trans(ds, ps0[0], ps0[1] + k * 500 + 200, ps0[2]);
                    ds = transform(ds.x, ds.y);
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 9));
                    g.setColor(Color.orange);
                    g.drawString(Integer.toString(i), (int) ds.x, (int) ds.y);
                }
            }

            PolygonWrapper[] CELLS_SYM = BasicPolygon.copy_list(cells_sym);
            paint_frames_array(g, CELLS_SYM, ps2[0], ps2[1], ps2[2], Color.cyan,
                    Color.green, 1);
            PolygonWrapper[] IMAGE_SYM = BasicPolygon.copy_list(image_sym);
            paint_frames_array(g, IMAGE_SYM, ps2[0], ps2[1] + 200, ps2[2],
                    Color.cyan, Color.green, 1);
            PolygonWrapper[] CELLS_ROT = BasicPolygon.copy_list(cells_rot);
            paint_frames_array(g, CELLS_ROT, ps1[0], ps1[1], ps1[2], Color.white,
                    Color.orange, 0);
            PolygonWrapper[] IMAGE_ROT = BasicPolygon.copy_list(image_rot);
            paint_frames_array(g, IMAGE_ROT, ps1[0], ps1[1] + 200, ps1[2], Color.white,
                    Color.orange, 0);

            paint_frames_array(g, cells_ref, ps1[0], ps1[1] + 500, ps1[2], Color.white,
                    Color.orange, 1);
            paint_frames_array(g, image_ref, ps1[0], ps1[1] + 700, ps1[2], Color.white,
                    Color.orange, 1);

            PolygonWrapper[] CELLS_SYM2 = BasicPolygon.copy_list(cells_sym2);
            paint_frames_array(g, CELLS_SYM2, ps2[0], ps2[1] + 500, ps2[2], Color.pink,
                    Color.yellow, 1);
            PolygonWrapper[] IMAGE_SYM2 = BasicPolygon.copy_list(image_sym2);
            paint_frames_array(g, IMAGE_SYM2, ps2[0], ps2[1] + 700, ps2[2], Color.pink,
                    Color.yellow, 1);
//            paint_frames_array(g, cellsA_sym2, ps2[0], ps2[1] + 500, ps2[2], Color.pink,
//                    Color.yellow, 1);
//            paint_frames_array(g, cellsB_sym2, ps2[0], ps2[1] + 700, ps2[2], Color.pink,
//                    Color.yellow, 1);

        }

    }

    public void Setup() {
        s0 = 1.0 * 13 / 21;
        s1 = 1.0 * 21 / 34;
        s = 1.0 * (Math.sqrt(5) - 1) / 2;

        frame0 = FirstReturn.SymmetrySet(s).get(0);
        frame1 = FirstReturn.SymmetrySet(s).get(1);
        frame2 = FirstReturn.SymmetrySet(s).get(2);

        List<PolygonWrapper> frames0 = new ArrayList<PolygonWrapper>();
        frames0.add(frame0);
        List<PolygonWrapper> frames1 = new ArrayList<PolygonWrapper>();
        frames1.add(frame1);
        List<PolygonWrapper> frames2 = new ArrayList<PolygonWrapper>();
        frames2.add(frame2);

        cells = FirstReturn.returnCell_list(s, frames0);
        PolygonWrapper[] cells_array = BasicPolygon.sort(cells);

        List<PolygonWrapper> adjust_order_cells = BasicPolygon.copy_array(cells_array);
        image_cells = Pet.map_list(adjust_order_cells);

        cells_rot = ConjugationMap.rotation(adjust_order_cells, frame0.z[0], Math.PI / 3);
        image_rot = ConjugationMap.rotation(image_cells, frame0.z[0], Math.PI / 3);
        cells_sym = FirstReturn.inv_returnCell_list(s, frames1);
        for (int i = 0; i < cells_sym.size(); i++) {
            for (int j = 0; j < cells_rot.size(); j++) {
                PolygonWrapper rot = cells_rot.get(j);
                PolygonWrapper sym = cells_sym.get(i);
                if (PolygonWrapper.contains(rot, sym.center())) {
                    cells_sym.get(i).index = j;
                }

            }
        }
        image_sym = Pet.map_list(cells_sym);

        cells_ref = vertical_flip(adjust_order_cells, frame2, frame1.z[2]);
        image_ref = vertical_flip(image_cells, frame2, frame1.z[2]);
        cells_sym2 = FirstReturn.inv_returnCell_list(s, frames2);
        PolygonWrapper[] cells_sym2_array = BasicPolygon.copy_list(cells_sym2);
        cells_sym2_array = assign_index(cells_ref, cells_sym2_array);
        cells_sym2 = BasicPolygon.copy_array(cells_sym2_array);
        image_sym2 = Pet.map_list(cells_sym2);

        frame0_A = FirstReturn.SymmetrySet(s0).get(0);
        List<PolygonWrapper> frames0_A = new ArrayList<PolygonWrapper>();
        frames0_A.add(frame0_A);
        cellsA = BasicPolygon.sort(FirstReturn.returnCell_list(s0, frames0_A));
        frame0_B = FirstReturn.SymmetrySet(s1).get(0);
        List<PolygonWrapper> frames0_B = new ArrayList<PolygonWrapper>();
        frames0_B.add(frame0_B);
        
        cellsB = BasicPolygon.sort(FirstReturn.returnCell_list(s1, frames0_B));
        
        System.out.println("maximal return domains!!!");
           List<double[][][]> parametrize_cells = rationalize_polylist(cellsA,
                cellsB, cells_array, s0, s1, s, 1);
           

        cellsA_rot = ConjugationMap.rotation(cellsA, frame0_A.z[0], Math.PI / 3);
        cellsB_rot = ConjugationMap.rotation(cellsB, frame0_B.z[0], Math.PI / 3);
        PolygonWrapper[] cells_rot_array = BasicPolygon.copy_list(cells_rot);
     

        List<PolygonWrapper> frames1_A = new ArrayList<PolygonWrapper>();
        frame1_A = FirstReturn.SymmetrySet(s0).get(1);
        frames1_A.add(frame1_A);
        cellsA_sym = BasicPolygon.copy_list(FirstReturn.inv_returnCell_list(s0, frames1_A));
        List<PolygonWrapper> frames1_B = new ArrayList<PolygonWrapper>();
        frame1_B = FirstReturn.SymmetrySet(s1).get(1);
        frames1_B.add(frame1_B);
        cellsB_sym = BasicPolygon.copy_list(FirstReturn.inv_returnCell_list(s1, frames1_B));

        cellsA_sym = assign_index(cellsA_rot, cellsA_sym);
        cellsB_sym = assign_index(cellsB_rot, cellsB_sym);

        for (int i = 0; i < cellsA_sym.length + 1; i++) {
            for (int j = 0; j < cellsA_sym.length; j++) {
                if (cellsA_sym[j].index == i && cellsB_sym[j].index == i) {
                    System.out.println();
                    System.out.println("inverse maximal domain index " + i);
                    double[][][] parametrization = rationalize_polygon(
                            cellsA_sym[j], cellsB_sym[j], cells_sym.get(j), s0, s1, s, 1);
                    System.out.println(" translation vectors : ");
                    double[][] vector = CanvasGoldenRatio.rationalize_complex(
                            cellsA_sym[j].vector, cellsB_sym[j].vector, s0, s1);
                }
            }
        }

        List<PolygonWrapper> frames2_A = new ArrayList<PolygonWrapper>();
        frame2_A = FirstReturn.SymmetrySet(s0).get(2);
        frames2_A.add(frame2_A);
        List<PolygonWrapper> frames2_B = new ArrayList<PolygonWrapper>();
        frame2_B = FirstReturn.SymmetrySet(s1).get(2);
        frames2_B.add(frame2_B);
        cellsA_ref = vertical_flip(BasicPolygon.copy_array(cellsA), frame2_A, frame1_A.z[2]);
        cellsB_ref = vertical_flip(BasicPolygon.copy_array(cellsB), frame2_B, frame1_B.z[2]);
        System.out.println();
        System.out.println("Symmetry reflection 2: ");
        List<double[][][]> parametrize_cells_ref = rationalize_polylist(cellsA_ref,
                cellsB_ref, cells_ref, s0, s1, s, 1);

        List<PolygonWrapper> cellsA_sym2_list = FirstReturn.inv_returnCell_list(s0, frames2_A);
        cellsA_sym2 = BasicPolygon.copy_list(cellsA_sym2_list);
        cellsA_sym2 = assign_index(cellsA_ref, cellsA_sym2);
        List<PolygonWrapper> imageA_sym2_list = Pet.map_list(BasicPolygon.copy_array(cellsA_sym2));
        PolygonWrapper[] imageA_sym2 = BasicPolygon.copy_list(imageA_sym2_list);
        List<PolygonWrapper> cellsB_sym2_list = FirstReturn.inv_returnCell_list(s1, frames2_B);
        cellsB_sym2 = BasicPolygon.copy_list(cellsB_sym2_list);
        cellsB_sym2 = assign_index(cellsB_ref, cellsB_sym2);
        List<PolygonWrapper> imageB_sym2_list = Pet.map_list(BasicPolygon.copy_array(cellsB_sym2));
        PolygonWrapper[] imageB_sym2 = BasicPolygon.copy_list(imageB_sym2_list);
        System.out.println("image of inverse maximal domains ");

        for (int i = 1; i < cellsA_sym2.length + 1; i++) {
            for (int j = 0; j < cellsB_sym2.length; j++) {
                if (cellsA_sym2[j].index == i && cellsB_sym2[j].index == i) {
                    System.out.println();
                    System.out.println("inverse maximal domain index " + i);
                    double[][][] parametrization = rationalize_polygon(
                            cellsA_sym2[j], cellsB_sym2[j], cells_sym2.get(j), s0, s1, s, 0);
                    System.out.println(" translation vectors : ");
                    double[][] vector = CanvasGoldenRatio.rationalize_complex(
                            cellsA_sym2[j].vector, cellsB_sym2[j].vector, s0, s1);
                }
            }
        }

        List<PolygonWrapper> imageA_list = Pet.map_list(BasicPolygon.copy_array(cellsA));
        PolygonWrapper[] imageA_ref = vertical_flip(imageA_list, frame2_A, frame1_A.z[2]);
        List<PolygonWrapper> imageB_list = Pet.map_list(BasicPolygon.copy_array(cellsB));
        PolygonWrapper[] imageB_ref = vertical_flip(imageB_list, frame2_B, frame1_B.z[2]);
        System.out.println("Symmetry reflection image 2: ");
        List<double[][][]> parametrize_image_ref = rationalize_polylist(imageA_ref,
                imageB_ref, image_ref, s0, s1, s, 0);

        for (int i = 1; i < imageA_sym2.length + 1; i++) {
            for (int j = 0; j < imageB_sym2.length; j++) {
                if (imageA_sym2[j].index == i && imageB_sym2[j].index == i) {
                    System.out.println();
                    System.out.println("image of inverse maximal domain index " + i);
                    double[][][] parametrization = rationalize_polygon(
                            imageA_sym2[j], imageB_sym2[j], image_sym2.get(j), s0, s1, s, 0);
                }
            }
        }

        draw_cells = true;
        repaint();
    }

    public static List<double[][][]> rationalize_polylist(PolygonWrapper[] PL,
            PolygonWrapper[] QL, PolygonWrapper[] RL, double s0, double s1, double s, int check_mode) {
        List<double[][][]> coeffs = new ArrayList<double[][][]>();
        if (PL.length == QL.length) {
            for (int i = 0; i < PL.length; i++) {
                System.out.println();
                System.out.println("index " + PL[i].index);
                double[][][] coeff = rationalize_polygon(PL[i], QL[i], RL[i], s0, s1, s, 1);
                System.out.println(" translation vectors : ");
                double[][] vector = CanvasGoldenRatio.rationalize_complex(PL[i].vector, QL[i].vector, s0, s1);
                coeffs.add(coeff);
            }
        }
        return (coeffs);
    }
    
    
    public static double[][][] rationalize_polygon(PolygonWrapper P0, PolygonWrapper P1,
            PolygonWrapper P, double x0, double x1, double x, int check_mode) {
        double[][][] coeff = new double[P0.count][2][2];
        if (P0.count == P1.count) {
            Complex[] Z0 = BasicPolygon.order_vertex(P0);
            Complex[] Z1 = BasicPolygon.order_vertex(P1);
            Complex[] Z = BasicPolygon.order_vertex(P);
            for (int i = 0; i < Z0.length; i++) {
                coeff[i] = CanvasGoldenRatio.rationalize_complex(Z0[i], Z1[i], x0, x1);

                if (check_mode == 1) {
                    int[] scalar0 = MathRational.approximate(coeff[i][0][0], Math.pow(10, -10));
                    int[] const0 = MathRational.approximate(coeff[i][0][1], Math.pow(10, -10));
                    int[] scalar1 = MathRational.approximate(coeff[i][1][0], Math.pow(10, -10));
                    int[] const1 = MathRational.approximate(coeff[i][1][1], Math.pow(10, -10));

                    Complex r = new Complex();
                    r.x = 1.0 * scalar0[0] / scalar0[1] * x + 1.0 * const0[0] / const0[1];
                    r.y = Math.sqrt(3) * (1.0 * scalar1[0] / scalar1[1] * x
                            + 1.0 * const1[0] / const1[1]);
                    if (Complex.dist(r, Z[i]) > Math.pow(10, -8)) {
                        System.out.println((char) 27 + "[33mWrong parametrization!" + (char) 27 + "[0m");
                    }
                }
            }

        }
        return (coeff);
    }
    
        public static double[][][] rationalize_polygon(PolygonWrapper P0, PolygonWrapper P1,
             double x0, double x1) {
        double[][][] coeff = new double[P0.count][2][2];
        if (P0.count == P1.count) {
            Complex[] Z0 = BasicPolygon.order_vertex(P0);
            Complex[] Z1 = BasicPolygon.order_vertex(P1);
            for (int i = 0; i < Z0.length; i++) {
                coeff[i] = CanvasGoldenRatio.rationalize_complex(Z0[i], Z1[i], x0, x1);
            }

        }
        return (coeff);
    }

    
    
   
    public static PolygonWrapper[] assign_index(PolygonWrapper[] QL, PolygonWrapper[] PL) {
        for (int i = 0; i < PL.length; i++) {
            for (int j = 0; j < QL.length; j++) {
                PolygonWrapper rot = QL[j];
                PolygonWrapper sym = PL[i];
                if (PolygonWrapper.contains(rot, sym.center())) {
                    PL[i].index = QL[j].index;
                }
            }
        }
        return (PL);
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

    public static PolygonWrapper[] vertical_flip(List<PolygonWrapper> PL,
            PolygonWrapper W, Complex z) {
        List<PolygonWrapper> QL = new ArrayList<PolygonWrapper>();
        for (int i = 0; i < PL.size(); i++) {
            PolygonWrapper Q = new PolygonWrapper(PL.get(i));
            Q = BasicPolygon.flip_vertical(Q, z);
            Complex vector = Q.vector;
            Q = PolygonWrapper.intersect(Q, W);
            if (Q != null && Q.count > 2) {
                Q.index = i;
                Q.vector = vector;
                QL.add(Q);
            }
        }
        PolygonWrapper[] QA = BasicPolygon.copy_list(QL);
        return (QA);
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
