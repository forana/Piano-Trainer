package crescendo.sheetmusic;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import crescendo.base.song.Note;

/**
 * Represents an eighth note.
 * 
 * @author forana
 */
public class EighthNote extends DrawableNote{
	/** Prototyping constructor. **/
	public EighthNote() {}
	
	/**
	 * @param n The Note object this note "wraps"
	 * @param x The x-position of this note.
	 * @param y The y-position of this note.
	 */
	public EighthNote(Note n,int x,int y)
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
		return new EighthNote(n,x,y);
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
		return 0.5;
	}

	/**
	 * Draws this note to the given graphics context.
	 * 
	 * @param g - the graphics context to draw to.
	 */
	public void draw(Graphics g) {
		super.draw(g);
		g.setColor(color);
		//draw the base circle
		g.fillOval(x,y,8,8);
		
		Graphics2D g2=(Graphics2D)g;
		
		Stroke s=g2.getStroke();
		
		//draw the line attached to it, and the tail
		if(note.getPitch()<79)
		{
			g.drawLine(x+8,y+4,x+8,y-16);
			g2.setStroke(new BasicStroke(2));
			g.drawLine(x+8,y-16,x+12,y-12);
		}
		else
		{
			g.drawLine(x,y+4,x,y+24);
			g2.setStroke(new BasicStroke(2));
			g.drawLine(x,y+24,x-4,y+20);
		}
		g2.setStroke(s);
	}
}
