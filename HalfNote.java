package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

/**
 * Represents a half note.
 * 
 * @author forana
 */
public class HalfNote extends DrawableNote{
	/** Prototyping constructor. **/
	public HalfNote() {}
	
	/**
	 * @param n The Note object this note "wraps"
	 * @param x The x-position of this note.
	 * @param y The y-position of this note.
	 */
	public HalfNote(Note n,int x,int y)
	{
		super(n,x,y);
	}
	
	/**
	 * Creates a new note of this object's type.
	 * 
	 * @param n The note to be wrapped.
	 * @param x The x-position of the new note.
	 * @param y The y-position of the new note.
	 * 
	 * @return DrawableNote
	 */
	public DrawableNote spawn(Note n,int x,int y)
	{
		return new HalfNote(n,x,y);
	}
	
	/**
	 * This note's width, in pixels.
	 * 
	 * @return int
	 */
	public int getWidth() {
		return 8;
	}
	/**
	 * The number of beats this note represents.
	 * @return double
	 */
	public double getBeatsCovered() {
		return 2;
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(color);
		//draw the base circle
		g.drawOval(x,y,8,8);
		
		//draw the line attached to it
		if(note.getPitch()<79)
			g.drawLine(x+8,y+4,x+8,y-16);
		else
			g.drawLine(x,y+4,x,y+24);
	}
}
