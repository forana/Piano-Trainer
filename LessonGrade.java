package crescendo.lesson;

public class LessonGrade
{
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
