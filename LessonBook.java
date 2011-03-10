package crescendo.lesson;

import java.util.List;

public class LessonBook
{
	private String title;
	private String author;
	private String license;
	private String licenseURL;
	private String website;
	private List<BookItem> items;
	private LessonData data;
	
	public LessonBook(String title,String author,String license,String licenseURL,String website,List<BookItem> items,LessonData data)
	{
		this.title=title;
		this.author=author;
		this.license=license;
		this.licenseURL=licenseURL;
		this.website=website;
		this.items=items;
		this.data=data;
	}
	
	public List<BookItem> getContents()
	{
		return this.items;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getAuthor()
	{
		return this.author;
	}
	
	public String getLicense()
	{
		return this.license;
	}
	
	public String getLicenseURL()
	{
		return this.licenseURL;
	}
	
	public String getWebsite()
	{
		return this.website;
	}
	
	public LessonData getData()
	{
		return this.data;
	}
}
