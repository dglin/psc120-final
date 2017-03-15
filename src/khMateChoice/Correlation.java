package khMateChoice;

import sim.util.Bag;
import sim.util.Double2D;


public class Correlation {
	double sXY = 0;
	double sX = 0;
	double sY =0;
	double sX2 = 0;
	double sY2 = 0;
	double n = 0;
	double x = 0;
	double y = 0;
	boolean headers = false;
	
/**
 * Use this method add two data points (doubles) to an on going collection of data.  Correlation is
 * initialized to 0 data. It is assumed that the use always enters the x and y data points in the same
 * order.  For example, if data are on females and males, then if x = females and y = males, then the
 * data are always added as getData(x(female), y(male)).
 * @param x
 * @param y
 */
	public void getData(double x, double y){
		sXY += x*y;
		sX += x;
		sY += y;
		sX2 += x*x;
		sY2 += y*y;
		n++;
		this.x = x;
		this.y = y;
	}
	
	public void printColumnHeaders(){
		System.out.println("pairs\t"+"mean1\t"+"mean2\t"+"correlation");
	}
	/**
	 * Prints out the number of data pairs, the means for the first and second pair and
	 * the correlation. These are running computations and can be printed out at any
	 * time during the data collection process.
	 */
	public void printData(){
		if(!headers){
			printColumnHeaders();
			headers = true;
		}
		if(n>0)
			System.out.println(n +"\t"+ sX/n +"\t"+ sY/n+"\t"+correlation());
		else
			System.out.println(0 +"\t"+ 0 +"\t"+ 0+"\t"+0);
	}
	
	public Double2D means(){
		return new Double2D(sX/n, sY/n);
	}

	public double correlation(){
		double r = (sXY - (sX*sY)/n)/Math.sqrt((sX2-(sX*sX)/n)*(sY2-(sY*sY)/n));
		return r;
	}

	public static double mean(Bag array){
		double x =0;

		for(int i=0; i< array.numObjs;i++){
			x += (Double)array.get(i);
		}

		return x/(double)array.numObjs;
	}

	public static double ssq(Bag array, double mean){
		double x=0;
		for(int i=0; i< array.numObjs;i++){
			double y = (Double)array.get(i)-mean;
			x += y*y;
		}

		return x;
	}

	public static double sumXY(Bag arrayX, Bag arrayY, double meanX, double meanY){
		double xy = 0;
		for(int i =0; i< arrayX.numObjs;i++){
			double x = (Double)arrayX.get(i)-meanX;
			double y = (Double)arrayY.get(i)-meanY;
			xy += x*y;
		}
		return xy;
	}

	public static double correlation(Bag arrayX, Bag arrayY){
		if(arrayX.numObjs != arrayY.numObjs){
			System.err.println("Arrays are not equal!");
			return -100;
		}
		double r = 0;
		double meanX = mean(arrayX);
		double meanY = mean(arrayY);
		double ssqX = ssq(arrayX, meanX);
		double ssqY = ssq(arrayY, meanY);
		double sXY = sumXY(arrayX,arrayY,meanX,meanY);

		r = sXY/Math.sqrt(ssqX * ssqY);
		return r;
	}

}
