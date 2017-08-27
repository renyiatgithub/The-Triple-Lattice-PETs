package hexagonalpets;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class Main extends Applet {

    static CanvasLattice TL;
    static PopupLatticeCanvas Pop_TL;
    static PopupCellCanvas Pop_CC;
    static CanvasCell CC;
    static CanvasFiberBundle RC;
    static PopupTilingCanvas Pop_TIG;
    static CanvasTiling TIG;
    static PopupScissor Pop_CS;
    static CanvasScissor CS;
    static PopupAction Pop_A;
    static CanvasAction A;
    static CanvasGraph G;
    static PopupGraph Pop_G;
    static PopupFiberBundle Pop_FB;
    static CanvasFiberBundle FB;
    static PopupGoldenRatio Pop_GR;
    static CanvasGoldenRatio GR;
    static PopupReflection Pop_R;
    static CanvasReflection R;
    static PopupSymmetry Pop_SYM;
    static CanvasSymmetry SYM;
    static CanvasControl CONTROL;
    static CanvasMaxDomain MD;
    static PopupMaximalDomain Pop_MD;
    static RotationAxis_Return AR;
    static CanvasReturnDomain RD;
    static PopupReturnDomain Pop_RD;
    static RotationAxis_Base AB;
    static CanvasBaseDomain GD;
    static PopupGeneralDomain Pop_GD;
    static PopupBaseCase1 Pop_Base1;
    static RotationAxis_Base1 AB1;
    static CanvasBase1 Base1;
    static Panel controlpanel;
    static TextField text;
    static Button button_parameter;
    static String parameter_string;
    static double value;

    public static void main(String[] args) {
        Main a = new Main();
        Frame2 F_control = new Frame2("Control Canvas", a);
        F_control.setSize(500, 500);
        CONTROL = new CanvasControl();
        a.init();

        controlpanel = new Panel();
        controlpanel.setLayout(new FlowLayout());
        Label label_parameter = new Label("parameter s:", Label.RIGHT);
        text = new TextField(10);
        button_parameter = new Button("GO");
        button_parameter.setBounds(0, 0, 3, 3);
        controlpanel.setSize(500, 35);
        controlpanel.setLocation(0, 20);
        controlpanel.add(label_parameter);
        controlpanel.add(text);
        controlpanel.add(button_parameter);
        F_control.add(controlpanel);
        F_control.add(CONTROL);
        F_control.addWindowListener(new WinList(F_control));
        CONTROL.setVisible(true);
        F_control.setVisible(true);

        button_parameter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                parameter_string = text.getText();
                value = EntrySystem.value(parameter_string);
                int[] frac = MathRational.approximate(value, Math.pow(10, -10));
                CONTROL.parameter();
                CONTROL.repaint();
                Pop_TL.TL.Evaluate(value);
                Pop_TL.TL.repaint();
                Pop_CC.C.SetDomain(value);
                Pop_CC.C.repaint();
                Pop_TIG.TIG.Tiling(value);
                Pop_TIG.TIG.repaint();
                Pop_CS.CS.Tiling(value);
                Pop_CS.CS.repaint();
                Pop_FB.A.Slice(value);
                Pop_FB.A.repaint();

            }
        });

    }

    public void init() {
        TL = new CanvasLattice();
        Pop_TL = new PopupLatticeCanvas(false, TL, new Color(70, 130, 180));
        CC = new CanvasCell();
        Pop_CC = new PopupCellCanvas(false, CC, new Color(70, 130, 180));
        TIG = new CanvasTiling();
        Pop_TIG = new PopupTilingCanvas(false, TIG, new Color(70, 130, 180));
        RC = new CanvasFiberBundle();
        A = new CanvasAction();
        Pop_A = new PopupAction(false, A, new Color(70, 130, 180));
        G = new CanvasGraph();
        Pop_G = new PopupGraph(false, G, new Color(70, 130, 180));
        CS = new CanvasScissor();
        Pop_CS = new PopupScissor(false, CS, G, new Color(70, 130, 180));
        GR = new CanvasGoldenRatio();
        Pop_GR = new PopupGoldenRatio(false, GR, new Color(70, 130, 180));
        SYM = new CanvasSymmetry();
        Pop_SYM = new PopupSymmetry(false, SYM, new Color(70, 130, 180));
        FB = new CanvasFiberBundle();
        Pop_FB = new PopupFiberBundle(false, FB, new Color(70, 130, 180));
        MD = new CanvasMaxDomain();
        AR = new RotationAxis_Return();
        AB = new RotationAxis_Base();
        AB1 = new RotationAxis_Base1();
        Pop_MD = new PopupMaximalDomain(false, MD, AR, AB, AB1, new Color(70, 130, 180));
        RD = new CanvasReturnDomain();
        Pop_RD = new PopupReturnDomain(false, RD, new Color(70, 130, 180));
        GD = new CanvasBaseDomain();
        Pop_GD = new PopupGeneralDomain(false, GD, new Color(70, 130, 180));
        R = new CanvasReflection();
        Pop_R = new PopupReflection(false, R, new Color(70, 130, 180));
        Base1 = new CanvasBase1();
        Pop_Base1 = new PopupBaseCase1(false,Base1, Color.black);

    }

    public static class Frame2 extends Frame implements ComponentListener {

        Main a;

        public Frame2(String W, Main a) {
            super(W);
            this.a = a;
            addComponentListener(this);
        }

        public void componentHidden(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentResized(ComponentEvent e) {
        }

        public void componentShown(ComponentEvent e) {
        }

    }
}
