package crescendo.base.profile;

import java.io.Serializable;

import crescendo.base.song.Creator;
import crescendo.base.song.SongModel;

public class SongScore implements Serializable,Comparable<SongScore> {
	private static final long serialVersionUID=1L;
	
	private String filepath;
	private String title;
	private String author;
	private int highScore;
	
	public SongScore(String filepath,SongModel model)
	{
		this.filepath=filepath;
		this.title=model.getTitle();
		this.author=null;
		for (Creator creator : model.getCreators())
		{
			if (this.author==null)
			{
				this.author="";
			}
			else
			{
				this.author+=", ";
			}
			this.author+=creator.getType()+": "+creator.getName();
		}
		
		this.highScore=0;
	}
	
	public boolean setHighScore(int score)
	{
		boolean set=false;
		if (score>highScore)
		{
			set=true;
			highScore=score;
		}
		return set;
	}
	
	public int getHighScore()
	{
		return this.highScore;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getAuthor()
	{
		return this.author;
	}
	
	public String getFilePath()
	{
		return this.filepath;
	}
	
	public int compareTo(SongScore other)
	{
		return this.title.compareTo(other.title);
	}
}
