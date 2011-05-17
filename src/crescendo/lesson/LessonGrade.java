package crescendo.lesson;

import java.io.Serializable;

/**
 * Data storage for a particular score and the grade that was assigned to it.
 * @author forana
 */
public class LessonGrade implements Serializable
{
	private static final long serialVersionUID=1L;
	
	private boolean complete;
	private double grade;
	
	public LessonGrade()
	{
		this.complete=false;
		this.grade=0;
	}
	
	public boolean isComplete()
	{
		return this.complete;
	}
	
	public void setGrade(double grade)
	{
		this.complete=true;
		this.grade=grade;
	}
	
	public double getGrade()
	{
		return this.grade;
	}
}
