package crescendo.lesson;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a grading scale, which can be used to calculate a grade.
 * @author forana
 */
public class GradingScale implements Serializable
{
	private static final long serialVersionUID=1L;
	
	// list of grades
	private List<Grade> grades;
	
	/**
	 * Creates a new grading scale. Grades should be provided in order from highest to lowest.
	 * @param grades The grades possible
	 */
	public GradingScale(List<Grade> grades)
	{
		this.grades=grades;
	}
	
	/**
	 * Returns a grade for a score.
	 * @param score
	 * @return The highest matching grade.
	 */
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
