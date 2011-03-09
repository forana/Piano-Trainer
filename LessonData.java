package crescendo.lesson;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class LessonData implements Serializable
{
	private static final long serialVersionUID=100000L;
	
	private String filePath;
	private String title;
	private Map<Integer,LessonGrade> gradeMap;
	private GradingScale scale;
	
	public LessonData(String filePath,String title,GradingScale scale,List<MusicItem> music)
	{
		this.filePath=filePath;
		this.title=title;
		this.scale=scale;
		this.gradeMap=new HashMap<Integer,LessonGrade>();
		for (MusicItem item : music)
		{
			this.gradeMap.put(item.getCode(),new LessonGrade());
		}
	}
	
	public String getPath()
	{
		return this.filePath;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public void setTitle(String title)
	{
		this.title=title;
	}
	
	public GradingScale getScale()
	{
		return this.scale;
	}
	
	public LessonGrade getGrade(int code)
	{
		return this.gradeMap.get(code);
	}
}
