package crescendo.lesson;

import java.io.Serializable;
import java.util.List;

public class GradingScale implements Serializable
{
	private static final long serialVersionUID=1L;
	
	private List<Grade> grades;
	
	public GradingScale(List<Grade> grades)
	{
		this.grades=grades;
	}
	
	public Grade getGrade(double score)
	{
		Grade grade=null;
		for (Grade p : this.grades)
		{
			grade=p;
			if (p.minimum<=score)
			{
				break;
			}
		}
		return grade;
	}
}
