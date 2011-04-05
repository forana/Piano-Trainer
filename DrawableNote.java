package crescendo.sheetmusic;

import java.awt.Color;
import java.awt.Graphics;

import crescendo.base.song.Note;

/**
 * DrawableNote
 * 
 * This abstract class represents a note from the view of the sheet music 
 * 	module. It contains a draw() method to implement how the note is 
 * 	rendered to the screen.
 * 
 * @author groszc
 *
 */
public abstract class DrawableNote extends Drawable{

	
	/** the Note this DrawableNote "wraps" **/
	protected Note note;
	protected NoteType noteType;
	protected Color color;
	
	// Empty constructor - this allows good things for prototyping
	public DrawableNote() {}
	
	/**
	 * DrawableNote
	 * 
	 * Basic constructor, take a Note as a parameter.
	 * 
	 * @param n - the Note this DrawableNote "wraps"
	 */
	protected DrawableNote(Note n,int x,int y)
	{
		color = Color.black;
		note = n;
		this.x = x;
		this.y = y;//+yPositionOfNote(n.getPitch());
	}
	
	public abstract DrawableNote spawn(Note n,int x,int y);
	public abstract double getBeatsCovered();
	
	public Note getNote()
	{
		return note;
	}
	
	protected void setCorrect(boolean correct){
		if(correct)
			color = Color.green;
		else
			color = Color.red;
	}
	
	/**
	 * draw
	 * 
	 * Draws this note to the given graphics context.
	 * 
	 * @param g - the graphics context to draw to.
	 */
	public void draw(Graphics g)
	{
		g.setColor(Color.BLACK);
	}

}
