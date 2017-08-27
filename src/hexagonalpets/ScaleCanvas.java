
package hexagonalpets;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;

public class ScaleCanvas extends Canvas implements MouseListener, 
        MouseMotionListener{
    Complex SOURCE=new Complex(0,0);
    AffineTransform[] A=new AffineTransform[2];
    double SCALE;

     public ScaleCanvas() {
	 setScales(0,0,1);  
         SCALE=Math.pow(2,0.25);
     }

     public void setScales(double m,double n) {
	A[0]=AffineTransform.getTranslateInstance(m,m);
	A[1]=AffineTransform.getScaleInstance(n,n);
    }


    public void setScales(double m1,double m2,double n) {
	A[0]=AffineTransform.getTranslateInstance(m1,m2);
	A[1]=AffineTransform.getScaleInstance(n,n);
    }

    /**double buffering - seems to run automatically without 
       explicitly being invoked in the paint routine.*/
    public void update(Graphics g) {
        Graphics g2;
        Image offscreen = null;
        offscreen = createImage(this.getWidth(),this.getHeight());
        g2 = offscreen.getGraphics();
        paint(g2);
        g.drawImage(offscreen, 0, 0, this);
	g2.dispose();
	offscreen.flush();
    }
    /**end double buffering*/
    
    public void fillPoint(Graphics2D g,Complex z,double r,Color C,int q) {
	GeneralPath gp=new GeneralPath();
	float x=(float)(z.x);
	float y=(float)(z.y);
	for(int i=0;i<=q;++i) {
	    double c=Math.cos(2*Math.PI*i/q);
	    double s=Math.sin(2*Math.PI*i/q);
	    c=r*c+z.x;
	    s=r*s+z.y;
	    if(i==0) gp.moveTo((float)(c),(float)(s));
	    if(i!=0) gp.lineTo((float)(c),(float)(s));
	}
	gp=transform(gp);
	g.setColor(C);
	g.fill(gp);
	g.draw(gp);
    }


    public void paint(Graphics g2) {}

    public GeneralPath transform(GeneralPath H) {
	GeneralPath HH=new GeneralPath(H);
	HH.transform(A[1]);   //scale
	HH.transform(A[0]);   //translate
	return(HH);
    }
    
    public Complex transform(double tx, double ty){
        double ux = A[0].getTranslateX();
        double uy = A[0].getTranslateY();
        double sx = A[1].getScaleX();
        double sy = A[1].getScaleY();        
        tx = tx * sx;
        tx = tx + ux;
        ty = ty * sy;
        ty = ty + uy;
        Complex z = new Complex(tx,ty);
        return(z);
    }
    

    


    

    /**ZOOM: This routine is the companion to the scaling routine.  
       After I have zoomed into the picture in some way, my further
       mouse clicks have different meanings than they did before the
       zoom.  In other words, suppose that I dilate the picture by
       100000.  When I click on the point with pixel value (50,50)
       I really mean to select the number (.00005,.00005). This routine
       changes the pixel value of the point to the intended value.*/

    public Complex unTransform(Point X) {
       double tx=X.x;
       double ty=X.y;
       return(unTransform(tx,ty));
    }


    /**This is a real valued version of the same thing**/
    public Complex unTransform(double tx,double ty) {
       double ux=A[0].getTranslateX();
       double uy=A[0].getTranslateY();
       double sx=A[1].getScaleX();
       double sy=A[1].getScaleY();
       ux=(ux-tx)+tx;
       uy=(uy-ty)+ty;
       tx=tx-ux;
       ty=ty-uy;
       tx=tx/sx;
       ty=ty/sy;
       Complex z=new Complex(tx,ty);
       return(z);
    }
    

    /**ZOOM: this routine scales up or down with the mouse click.
       The first argument is the point about which you scale, and
       the second argument just tells you whether to go up or down.
       The basic idea is that I have globally defined some
       AffineTransform objects.  These will rescale a GeneralPath
       whenever I draw it.  So, I just modify the components of
       these AffineTransforms whenever I do this routine.*/

    public void scaleUp(Point X,int k) {

	double scale=SCALE;
	double ss=scale;
	if(k==1) ss=1/scale;

	double sx=A[1].getScaleX();
	double sy=A[1].getScaleY();
	double tx=X.x;
	double ty=X.y;
	double ux0=A[0].getTranslateX();
	double uy=A[0].getTranslateY();

	double ux1=ss*(ux0-tx)+tx;
	uy=ss*(uy-ty)+ty;
	sx=sx*ss;
        sy=sy*ss;
	A[1]=AffineTransform.getScaleInstance(sx,sy);
	A[0]=AffineTransform.getTranslateInstance(ux1,uy);
    }


    public void drawSource(Graphics2D g) {
	GeneralPath gp=new GeneralPath();
	float x=(float)(SOURCE.x);
	float y=(float)(SOURCE.y);
	gp.moveTo((float)(x-.001),(float)(y-.001));
	gp.lineTo((float)(x-.001),(float)(y+.001));
	gp.lineTo((float)(x+.001),(float)(y+.001));
	gp.lineTo((float)(x+.001),(float)(y-.001));
	gp.closePath();
	gp=transform(gp);
	g.setColor(Color.white);
	g.fill(gp);
	g.draw(gp);
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}

    public void doScale(MouseEvent e) {
        MouseData J=MouseData.process(e);
	int mode=J.mode;
        if(mode==1) scaleUp(J.X,-1);
        if(mode==3) scaleUp(J.X,+1);
	if(mode==2) {
            Complex temp=unTransform(J.X);
	    SOURCE=temp;
	}
    }

   public void doScale2(MouseEvent e) {
        MouseData J=MouseData.process(e);
	int mode=J.mode;	
	if(mode==2) {
            Complex temp=unTransform(J.X);
	    SOURCE=temp;
	}
    }

    public void doScale(int mode,Point X) {
        if(mode==1) scaleUp(X,-1);
        if(mode==3) scaleUp(X,+1);
	if(mode==2) {
            Complex temp=unTransform(X);
	    SOURCE=temp;
	}
    }

    public void doScale2(int mode, Point X) {
	if(mode==2) {
            Complex temp=unTransform(X);
	    SOURCE=temp;
	}
    }

}

