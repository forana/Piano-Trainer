package crescendo.lesson;

import java.io.Serializable;

/**
 * A single grade.
 * @author forana
 */
public class Grade implements Comparable<Grade>,Serializable
{
	private static final long serialVersionUID=1L;
	
	// The minimum score needed to attain this grade.
	public final double minimum;
	// Textual representation of this grade.
	public final String label;
	
	/**
	 * @param minimum The minimum score needed to attain this grade.
	 * @param label Textual representation of this grade.
	 */
	public Grade(double minimum,String label)
	{
		this.minimum=minimum;
		this.label=label;
	}
	
	@Override
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
