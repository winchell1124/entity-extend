package similarity;

public class NumricSim {
	private  double THRESHNUM;
	public NumricSim(double THRESHNUM)
	{
		this.THRESHNUM=THRESHNUM;
	}
	public static double getNumricSim(double d1,double d2)
	{
		double nSim=0.0;
		double n1=Math.abs(d1);
		double n2=Math.abs(d2);
		if(n1>n2)
		{
			nSim=n2/n1;
		}
		else
			nSim=n1/n2;
		return nSim;
	}
	public  boolean equal(double d1,double d2)
	{
		if(getNumricSim(d1,d2)>THRESHNUM)
		{
			return true;
		}
		return false;
	}
	public static void main(String[] args)
	{
		System.out.println(new NumricSim(0.99).equal(0.180950,0.1820759));
	}

}
