package crescendo.lesson;

import java.util.List;

public class LessonBook implements BookItemReceiver
{
	private String author;
	private String license;
	private String licenseURL;
	private String website;
	private List<BookItem> items;
	
	public LessonBook(String author,String license,String licenseURL,String website,List<BookItem> items)
	{
		this.author=author;
		this.license=license;
		this.licenseURL=licenseURL;
		this.website=website;
		this.items=items;
	}
	
	public List<BookItem> getContents()
	{
		return this.items;
	}
}
