package hexagonalpets;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.geom.*;
import java.math.*;
import java.io.*;


/**This class extracts the face list from a
   long polyhedron that is give in terms of vertices**/


public class Combinatorics {

     /**Produces a subset of {0,...,N-1} from the pair (N,n)**/

    public static int[] binaryList(int N,int n) {
	int[] x=new int[N];
	int m=n;
	for(int count=0;count<N;++count) {
	    x[N-1-count]=m%2;
	    m=(m-x[N-1-count])/2;
	}
	int[] y=new int[N];
	int total=0;
	for(int i=0;i<N;++i) {
	    if(x[i]==1) {
                y[total]=i;
		++total;
	    }
	}
	int[] z=new int[total];
	for(int i=0;i<total;++i) z[i]=y[i];
	return(z);
    }




    /**The pairs (N,a) and (N,b) both determine subsets of the set {0,...,N-1}.
       This routine tests if the former is a subset of the latter.  The purpose
       of this routine is to help in picking out the maximal faces.**/


    public static boolean isSubset(int N,int a,int b) {
	int[] u=binaryList(N,a);
	int[] v=binaryList(N,b);
	return(isSubset(u,v));
    }

     /**straight up routine to tell if one integer list is a subset of another.*/

    public static  boolean isSubset(int[] u,int[] v) {
	for(int i=0;i<u.length;++i) {
	    boolean test=false;
	    for(int j=0;j<v.length;++j) {
		if(u[i]==v[j]) test = true;       
	    }
	    if(test==false) return(false);
	}
	return(true);
    }

    public static boolean isSubset(int N,int a,int[] LIST) {
	for(int i=0;i<LIST.length;++i) {
	    if((a!=LIST[i])&&(isSubset(N,a,LIST[i])==true)) return(true);
	}
	return(false);
    }

     /**removes all but the maximal subsets.
        Everything is based on N-element subsets.*/

    public static int[] trim(int N,int[] LIST) {
	int total=0;
	int[] LIST1=new int[LIST.length];
	for(int i=0;i<LIST.length;++i) {
	    if(isSubset(N,LIST[i],LIST)==false) {
		LIST1[total]=LIST[i];
		++total;
	    }
	}
	int[] LIST2=new int[total];
	for(int i=0;i<total;++i) LIST2[i]=LIST1[i];
	return(LIST2);
    }


    /**This routine copys a list of integers but
       correctly sets the length of the list so
       we don't need to keep track of it with an
       auxilliary variable.*/

    public static int[] copy(int[] A,int count) {
	int[] B=new int[count];
	for(int i=0;i<count;++i) B[i]=A[i];
	return(B);
    }

}


