
package hexagonalpets;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;

/*This is a special class of 3x3 matrices*/

public class LongMatrix {
    double[][] a=new double[3][3];
    double SCALE;


    public LongMatrix(){
	SCALE=1;
    }


    public static LongVector act(LongMatrix M,LongVector V) {
	if(M==null) return(null);
	if(V==null) return(null);
	LongVector W=new LongVector();
	for(int i=0;i<3;++i) {
	    W.x[i]=0;
	    for(int j=0;j<3;++j) {
		W.x[i]=W.x[i]+M.a[i][j]*V.x[j];
	    }
	}
	return(W);
    }

    public static LongMatrix random(int d) {
	LongMatrix m=new LongMatrix();
	for(int i=0;i<3;++i) {
	    for(int j=0;j<3;++j) {
		double t=Math.random();
		m.a[i][j]=(long)(d-2*d*t);
	    }
	}
	m.SCALE=1;
	return(m);
    }

    public double det() {
	double d=0;
	d=d+a[0][0]*a[1][1]*a[2][2];
	d=d+a[0][1]*a[1][2]*a[2][0];
	d=d+a[0][2]*a[1][0]*a[2][1];
	d=d-a[2][0]*a[1][1]*a[0][2];
	d=d-a[2][1]*a[1][2]*a[0][0];
	d=d-a[2][2]*a[1][0]*a[0][1];
	return(d);
    }

    public static LongMatrix times(LongMatrix M1,LongMatrix M2) {
	LongMatrix M=new LongMatrix();
	for(int i=0;i<3;++i) {
	    for(int j=0;j<3;++j) {
		M.a[i][j]=0;
		for(int k=0;k<3;++k) {
		    M.a[i][j]=M.a[i][j]+M1.a[i][k]*M2.a[k][j];
		}
	    }
	}
	M.SCALE=M1.SCALE*M2.SCALE;
	return(M);
    }

    public LongMatrix transpose() {
	LongMatrix m=new LongMatrix();
	for(int i=0;i<3;++i) {
	    for(int j=0;j<3;++j) {
		m.a[j][i]=a[i][j];
	    }
	}
	m.SCALE=SCALE;
	return(m);
    }


    public LongMatrix adjoint() {
	LongMatrix m=new LongMatrix();
	m.a[0][0]=a[1][1]*a[2][2]-a[1][2]*a[2][1];
	m.a[0][2]=a[1][0]*a[2][1]-a[1][1]*a[2][0];
	m.a[2][0]=a[0][1]*a[1][2]-a[1][1]*a[0][2];
	m.a[2][2]=a[0][0]*a[1][1]-a[0][1]*a[1][0];
	m.a[1][1]=a[0][0]*a[2][2]-a[2][0]*a[0][2];
	m.a[0][1]=a[1][2]*a[2][0]-a[1][0]*a[2][2];
	m.a[1][0]=a[0][2]*a[2][1]-a[0][1]*a[2][2];
	m.a[2][1]=a[0][2]*a[1][0]-a[0][0]*a[1][2];
	m.a[1][2]=a[0][1]*a[2][0]-a[0][0]*a[2][1];
	m.SCALE=SCALE;
	return(m.transpose());
        //return(m);
    }

    public void print() {
	for(int i=0;i<3;++i) {
	    for(int j=0;j<3;++j) {
		System.out.print(a[i][j]+" ");
	    }
	    System.out.println("");
	}
	System.out.println("SCALE "+SCALE);
    }

    public LongMatrix inverse() {
	LongMatrix m=this.adjoint();
	m.SCALE=this.det();
	return(m);
    }


}