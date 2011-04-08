package crescendo.lesson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.StringReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import crescendo.base.HeuristicsModel;

public class LessonFactory
{
	private static final List<Grade> DEFAULT_GRADES = new LinkedList<Grade>();
	private static final GradingScale DEFAULT_SCALE;
	
	private static List<File> tempFiles=new LinkedList<File>();
	
	// add grades to list
	static
	{
		DEFAULT_GRADES.add(new Grade(85,"A"));
		DEFAULT_GRADES.add(new Grade(70,"B"));
		DEFAULT_GRADES.add(new Grade(55,"C"));
		DEFAULT_GRADES.add(new Grade(40,"D"));
		DEFAULT_GRADES.add(new Grade(0,"F"));
		
		DEFAULT_SCALE=new GradingScale(DEFAULT_GRADES);
	}
	
	public static LessonBook createLessonBook(String filepath) throws IOException
	{
		return createLessonBook(filepath,null);
	}
	
	public static LessonBook createLessonBook(LessonData data) throws IOException
	{
		return createLessonBook(data.getPath(),data);
	}
	
	private static LessonBook createLessonBook(String path,LessonData data) throws IOException
	{
		// step 1 - create zip input stream (will fail if the file doesn't exist)
		ZipInputStream zip=new ZipInputStream(new FileInputStream(path));
		
		// step 2 - create temporary directory (will fail if program does not have privileges)
		// use the API to get a path to temp
		File tempDir=File.createTempFile("trainer","");
		// delete the file
		tempDir.delete();
		// create a directory at a very similar path
		// (cannot be the same because the JVM will not delete the file immediately if any
		// other process is looking at it, for example antivirus software - see
		// http://stackoverflow.com/questions/617414/create-a-temporary-directory-in-java)
		tempDir=new File(tempDir.getAbsolutePath()+".lesson");
		tempDir.mkdir();
		// add the file to our list so it gets cleared out later
		tempFiles.add(tempDir);
		
		// step 3 - begin extracting zip to temp
		ZipEntry entry;
		byte[] buffer=new byte[512];
		while ((entry=zip.getNextEntry())!=null)
		{
			File file=new File(tempDir.getAbsolutePath()+"/"+entry.getName());
			File parentDir=new File(file.getParent());
			// make sure the containing folders exist
			parentDir.mkdirs();
			
			if (entry.isDirectory())
			{
				file.mkdir();
			}
			else
			{
				FileOutputStream out=new FileOutputStream(file);
				int n; // zip does not support EOF
				while ((n=zip.read(buffer,0,buffer.length-1))>-1)
				{
					out.write(buffer,0,n);
				}
				out.close();
			}
		}
		zip.close();
		
		// step 4 - parse XML
		DocumentBuilder builder;
		try
		{
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			builder=factory.newDocumentBuilder();
			// don't connect to the internet
			builder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId,String systemId) throws SAXException,IOException {
					return new InputSource(new StringReader(""));
				}
			});
		}
		catch (ParserConfigurationException e)
		{
			throw new IOException("Internal parser error");
		}
		
		Document doc;
		try
		{
			doc=builder.parse(tempDir.getAbsolutePath()+"/book.xml");
		}
		catch (SAXException e)
		{
			throw new IOException("Improperly formatted listing file.");
		}
		
		Element root=doc.getDocumentElement();
		String title="Untitled Book";
		String author=null;
		String license=null;
		String licenseURL=null;
		String website=null;
		GradingScale defaultScale=DEFAULT_SCALE;
		String temp=tempDir.getAbsolutePath()+"/";
		List<BookItem> items=new LinkedList<BookItem>();
		if (!root.getNodeName().toLowerCase().equals("book"))
		{
			throw new IOException("Root book node not found");
		}
		for (int i=0; i<root.getChildNodes().getLength(); i++)
		{
			Node sectionNode=root.getChildNodes().item(i);
			if (sectionNode.getNodeName().toLowerCase().equals("meta"))
			{
				for (int j=0; j<sectionNode.getChildNodes().getLength(); j++)
				{
					Node node=sectionNode.getChildNodes().item(j);
					if (node.getNodeName().toLowerCase().equals("title"))
					{
						title=node.getTextContent();
					}
					else if (node.getNodeName().toLowerCase().equals("author"))
					{
						author=node.getTextContent();
					}
					else if (node.getNodeName().toLowerCase().equals("license"))
					{
						license=node.getTextContent();
						Node urlnode=node.getAttributes().getNamedItem("url");
						if (urlnode!=null)
						{
							licenseURL=urlnode.getTextContent();
						}
					}
					else if (node.getNodeName().toLowerCase().equals("website"))
					{
						website=node.getTextContent();
					}
					else if (node.getNodeName().toLowerCase().equals("scale"))
					{
						defaultScale=parseScale(node);
					}
				}
			}
			else if (sectionNode.getNodeName().toLowerCase().equals("content"))
			{
				for (int j=0; j<sectionNode.getChildNodes().getLength(); j++)
				{
					Node node=sectionNode.getChildNodes().item(j);
					if (node.getNodeName().toLowerCase().equals("chapter"))
					{
						items.add(parseChapter(node,defaultScale,temp));
					}
					else if (node.getNodeName().toLowerCase().equals("lesson"))
					{
						items.add(parseLesson(node,defaultScale,temp));
					}
				}
			}
		}
		
		// step 5 - instantiate full book
		if (data==null)
		{
			// need to round up the music items so that the data knows how many to count
			List<MusicItem> music=new LinkedList<MusicItem>();
			for (BookItem item : items)
			{
				if (item instanceof Chapter)
				{
					for (BookItem lesson : ((Chapter)item).getContents())
					{
						for (PageItem page : lesson.getItems())
						{
							if (page instanceof MusicItem)
							{
								music.add((MusicItem)page);
							}
						}
					}
				}
				else
				{
					for (PageItem page : item.getItems())
					{
						if (page instanceof MusicItem)
						{
							music.add((MusicItem)page);
						}
					}
				}
			}
			data=new LessonData(path,title,defaultScale,music);
		}
		
		//step 6 link music items to the data so they can be scored
		for (BookItem item : items)
		{
			if (item instanceof Chapter)
			{
				for (BookItem lesson : ((Chapter)item).getContents())
				{
					for (PageItem page : lesson.getItems())
					{
						if (page instanceof MusicItem)
						{
							((MusicItem)page).linkLessonData(data);
						}
					}
				}
			}
			else
			{
				for (PageItem page : item.getItems())
				{
					if (page instanceof MusicItem)
					{
						((MusicItem)page).linkLessonData(data);
					}
				}
			}
		}
		
		//step 7 return
		return new LessonBook(title,author,license,licenseURL,website,items,data);
	}
	
	private static GradingScale parseScale(Node n)
	{
		List<Grade> grades=new LinkedList<Grade>();
		for (int i=0; i<n.getChildNodes().getLength(); i++)
		{
			Node child=n.getChildNodes().item(i);
			if (child.getNodeName().toLowerCase().equals("grade"))
			{
				int min=0;
				String label=child.getTextContent();
				Node minNode=child.getAttributes().getNamedItem("min");
				if (minNode!=null)
				{
					try
					{
						min=Integer.parseInt(minNode.getTextContent());
					}
					catch (NumberFormatException e)
					{
					}
				}
				grades.add(new Grade(min,label));
			}
		}
		Collections.sort(grades);
		return new GradingScale(grades);
	}
	
	private static Chapter parseChapter(Node n,GradingScale scale,String tempDir)
	{
		List<BookItem> contents=new LinkedList<BookItem>();
		List<PageItem> items=new LinkedList<PageItem>();
		String title="Untitled Chapter";
		Node titleNode=n.getAttributes().getNamedItem("title");
		if (titleNode!=null)
		{
			title=titleNode.getTextContent();
		}
		for (int i=0; i<n.getChildNodes().getLength(); i++)
		{
			Node child=n.getChildNodes().item(i);
			if (child.getNodeName().toLowerCase().equals("lesson"))
			{
				contents.add(parseLesson(child,scale,tempDir));
			}
			else if (child.getNodeName().toLowerCase().equals("#text"))
			{
				items.add(parseTextItem(child));
			}
			else if (child.getNodeName().toLowerCase().equals("image"))
			{
				items.add(parseImageItem(child,tempDir));
			}
			else if (child.getNodeName().toLowerCase().equals("link"))
			{
				items.add(parseLinkItem(child));
			}
		}
		return new Chapter(title,contents,items);
	}
	
	private static Lesson parseLesson(Node n,GradingScale scale,String tempDir)
	{
		List<PageItem> items=new LinkedList<PageItem>();
		String title="Untitled Lesson";
		Node titleNode=n.getAttributes().getNamedItem("title");
		if (titleNode!=null)
		{
			title=titleNode.getTextContent();
		}
		for (int i=0; i<n.getChildNodes().getLength(); i++)
		{
			Node child=n.getChildNodes().item(i);
			if (child.getNodeName().toLowerCase().equals("#text"))
			{
				items.add(parseTextItem(child));
			}
			else if (child.getNodeName().toLowerCase().equals("image"))
			{
				items.add(parseImageItem(child,tempDir));
			}
			else if (child.getNodeName().toLowerCase().equals("link"))
			{
				items.add(parseLinkItem(child));
			}
			else if (child.getNodeName().toLowerCase().equals("music"))
			{
				items.add(parseMusicItem(child,scale,tempDir));
			}
		}
		return new Lesson(title,items);
	}
	
	private static TextItem parseTextItem(Node n)
	{
		return new TextItem("<html><p>"+n.getTextContent()+"</p></html>");
	}
	
	private static LinkItem parseLinkItem(Node n)
	{
		String text=n.getTextContent();
		String url="";
		Node urlnode=n.getAttributes().getNamedItem("url");
		if (urlnode!=null)
		{
			url=urlnode.getTextContent();
		}
		return new LinkItem(text,url);
	}
	
	private static ImageItem parseImageItem(Node n,String tempDir)
	{
		String figure=n.getTextContent();
		String alt="Image";
		String source=null;
		Node altnode=n.getAttributes().getNamedItem("alt");
		if (altnode!=null)
		{
			alt=altnode.getTextContent();
		}
		Node sourcenode=n.getAttributes().getNamedItem("source");
		if (sourcenode!=null)
		{
			source=tempDir+sourcenode.getTextContent();
		}
		return new ImageItem(source,alt,figure);
	}
	
	private static MusicItem parseMusicItem(Node n,GradingScale scale,String tempDir)
	{
		boolean pitch=true;
		boolean dynamic=true;
		double interval=0.5;
		int velocityTolerance=30;
		int track=0;
		GradingScale usedScale=scale;
		String source=null;
		for (int i=0; i<n.getChildNodes().getLength(); i++)
		{
			Node child=n.getChildNodes().item(i);
			if (child.getNodeName().toLowerCase().equals("scale"))
			{
				usedScale=parseScale(child);
			}
		}
		Node pitchnode=n.getAttributes().getNamedItem("pitch");
		if (pitchnode!=null)
		{
			pitch=(!pitchnode.getTextContent().equals("off"));
		}
		Node dynamicnode=n.getAttributes().getNamedItem("dynamic");
		if (dynamicnode!=null)
		{
			dynamic=(!dynamicnode.getTextContent().equals("off"));
		}
		Node vtolnode=n.getAttributes().getNamedItem("velocitytol");
		if (vtolnode!=null)
		{
			try
			{
				velocityTolerance=Integer.parseInt(vtolnode.getTextContent());
			}
			catch (NumberFormatException e)
			{
			}
		}
		Node intervalnode=n.getAttributes().getNamedItem("interval");
		if (intervalnode!=null)
		{
			try
			{
				interval=Integer.parseInt(intervalnode.getTextContent());
			}
			catch (NumberFormatException e)
			{
			}
		}
		Node tracknode=n.getAttributes().getNamedItem("track");
		if (tracknode!=null)
		{
			try
			{
				track=Integer.parseInt(intervalnode.getTextContent());
			}
			catch (NumberFormatException e)
			{
			}
		}
		Node sourcenode=n.getAttributes().getNamedItem("source");
		if (sourcenode!=null)
		{
			source=tempDir+sourcenode.getTextContent();
		}
		
		HeuristicsModel heuristics=new HeuristicsModel(interval,velocityTolerance,pitch,dynamic);
		return new MusicItem(source,heuristics,usedScale,track,null);
	}
	
	public static void clean()
	{
		for (File file : tempFiles)
		{
			if (file.isDirectory())
			{
				cleanDir(file);
			}
			file.delete();
		}
		tempFiles.clear();
	}
	
	private static void cleanDir(File dir)
	{
		for (File file : dir.listFiles())
		{
			if (file.isDirectory())
			{
				cleanDir(file);
			}
			file.delete();
		}
	}
}
