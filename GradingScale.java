package crescendo.lesson;

import java.util.List;
import java.util.Collections;

public class GradingScale
{
	private List<Grade> grades;
	
	public GradingScale(List<Grade> grades)
	{
		this.grades=grades;
		Collections.sort(this.grades);
	}
	
	public Grade getGrade(double score)
	{
		Grade grade=null;
		for (Grade p : this.grades)
		{
			if (p.minimum<=score)
			{
				grade=p;
				break;
			}
		}
		return grade;
	}
}
