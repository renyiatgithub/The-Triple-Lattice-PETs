package hexagonalpets;

import java.applet.Applet;
import java.awt.event.*;
import java.awt.*;

/*This class does the basic arithmetic
  of nxn matrices.  Here n<20 */


public class GaussianElimination {


    /**This does a single step of Gaussian elimination
       for a single matrix*/

    public static Matrix step(Matrix M) {
	int s=M.data[0];
	int n=M.size;
	double max=0;
	int index=-1;

	for(int i=s;i<n;++i) {
	    double test=Math.abs(M.a[i][s]);
	    if(max<test) {
		index=i;
		max=test;
	    }
	}

	Matrix N=new Matrix(M);
	N.data[0]=M.data[0]+1;
	N.data[1]=M.data[1];
	if(index!=-1) {              //this was 0 but I think it should be -1
	    N.a[s]=M.a[index];
	    N.a[index]=M.a[s];
	    ++N.data[1];
	}
	for(int i=0;i<n;++i) {
	    if(i!=s) {
		if(Math.abs(N.a[s][s])<.00000001) return(null);
		double d=N.a[i][s]/N.a[s][s];
	        for(int j=s;j<n;++j) {
		    N.a[i][j]=N.a[i][j]-d*N.a[s][j];
		}
	    }
	}
	return(N);
    }

    /**This does a single step of Gaussian elimination
       for a pair of matrices.  The first matrix controls
       the pivot data*/


    public static Matrix[] step(Matrix[] M) {
	if(M==null) return(null);
	int s=M[0].data[0];
	int n=M[0].size;
	double max=0;
	int index=-1;

	for(int i=s;i<n;++i) {
	    double test=Math.abs(M[0].a[i][s]);
	    if(max<test) {
		index=i;
		max=test;
	    }
	}

	if(index==-1) return(null);  //singular matrix

	Matrix[] N=new Matrix[2];
	N[0]=new Matrix(M[0]);
	N[1]=new Matrix(M[1]);
	N[0].data[0]=M[0].data[0]+1;
	N[0].data[1]=M[0].data[1];
	if(index!=0) {
	    N[0].a[s]=M[0].a[index];
	    N[0].a[index]=M[0].a[s];
	    N[1].a[s]=M[1].a[index];
	    N[1].a[index]=M[1].a[s];
	    ++N[0].data[1];
	}
	for(int i=0;i<n;++i) {
	    if(i!=s) {
		double d=N[0].a[i][s]/N[0].a[s][s];
	        for(int j=0;j<n;++j) {
		    N[0].a[i][j]=N[0].a[i][j]-d*N[0].a[s][j];
		    N[1].a[i][j]=N[1].a[i][j]-d*N[1].a[s][j];
		}
	    }
	}
	return(N);
    }



}




