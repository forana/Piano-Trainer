package crescendo.lesson;

import java.io.Serializable;

public class Grade implements Comparable<Grade>,Serializable
{
	public final double minimum;
	public final String label;
	
	public Grade(double minimum,String label)
	{
		this.minimum=minimum;
		this.label=label;
	}
	
	public int compareTo(Grade other)
	{
		double res=other.minimum-this.minimum;
		if (res>0)
		{
			return 1;
		}
		else if (res<0)
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
}
