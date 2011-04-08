package crescendo.sheetmusic;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import crescendo.base.song.Note;

public class QuarterRest extends DrawableNote{
	
	private static final String IMAGE_PATH="resources/images/rest.quarter.8x32.png";
	
	private static Image IMAGE;
	
	public QuarterRest() {}
	
	public QuarterRest(Note n,int x,int y)
	{
		super(n,x,y);
		
		noteType = NoteType.QUARTERNOTE;
	}
	
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new QuarterRest(n,x,y);
	}
	
	public int getWidth() {
		// needs to be hardcoded unfortunately
		return 8;
	}
	
	public double getBeatsCovered() {
		return 1;
	}

	@Override
	public void draw(Graphics g) {
		if (IMAGE==null)
		{
			IMAGE=Toolkit.getDefaultToolkit().createImage(IMAGE_PATH);
		}
		// not passing the ImageObserver means this won't show up right away, but that shouldn't be a problem
		// due to triple-resizing
		g.drawImage(IMAGE,x,y-2*MusicEngine.STAFF_LINE_HEIGHT,this.getWidth(),MusicEngine.STAFF_HEIGHT,null);
	}
}
