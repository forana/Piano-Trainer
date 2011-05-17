package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

/**
 * Represents an eighth rest.
 * 
 * @author forana
 */
public class EighthRest extends DrawableNote{
	/** Prototyping constructor. **/
	public EighthRest() {}
	
	/**
	 * @param n The Note object this note "wraps"
	 * @param x The x-position of this note.
	 * @param y The y-position of this note.
	 */
	public EighthRest(Note n,int x,int y)
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
		return new EighthRest(n,x,y);
	}
	
	/**
	 * This note's width, in pixels.
	 * 
	 * @return int
	 */
	public int getWidth() {
		return 17;
	}
	
	/**
	 * The number of beats this note represents.
	 * @return double
	 */
	public double getBeatsCovered() {
		return 0.5;
	}

	/**
	 * Draws this note to the given graphics context.
	 * 
	 * @param g - the graphics context to draw to.
	 */
	public void draw(Graphics g) {
		g.setColor(color);
		g.drawLine(x-5,y+10,x+5,y-10);
		g.drawLine(x+5,y-10,x-5,y-6);
		g.fillOval(x-7,y-10,5,5);
	}
}
