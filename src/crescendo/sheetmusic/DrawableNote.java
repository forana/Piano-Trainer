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
 * @author forana
 */
public abstract class DrawableNote extends Drawable{

	
	/** the Note this DrawableNote "wraps" **/
	protected Note note;
	protected Color color;
	
	// Empty constructor - this allows good things for prototyping
	public DrawableNote() {}
	
	/**
	 * @param n The Note object this note "wraps"
	 * @param x The x-position of this note.
	 * @param y The y-position of this note.
	 */
	protected DrawableNote(Note n,int x,int y)
	{
		color = Color.black;
		note = n;
		this.x = x;
		this.y = y;
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
	public abstract DrawableNote spawn(Note n,int x,int y);
	
	/**
	 * The number of beats this note represents.
	 * @return double
	 */
	public abstract double getBeatsCovered();
	
	/**
	 * The wrapped note object.
	 * @return Note
	 */
	public Note getNote()
	{
		return note;
	}
	
	/**
	 * Sets this note's correctness.
	 * 
	 * @param b true if the note is correct, false otherwise.
	 */
	protected void setCorrect(boolean correct){
		if(correct)
			color = Color.green;
		else
			color = Color.red;
	}
	
	protected void setNoteState(NoteState state){
		switch(state){
		case CORRECT:
			color = Color.green;
			break;
		case INCORRECT:
			color = Color.red;
			break;
		case UNMATCHED:
			color = Color.ORANGE;
			break;
		default:
			color = Color.red;
			break;
		}
	}
	
	/**
	 * Set this note's "correctness" back to neither correct nor incorrect.
	 */
	public void reset() {
		color=Color.black;
	}
	
	/**
	 * Draws this note to the given graphics context. Implementing classes should call super.
	 * 
	 * @param g - the graphics context to draw to.
	 */
	public void draw(Graphics g)
	{
		g.setColor(Color.BLACK);
		if (note.isPlayable())
		{
			int offset=0;
			for (int p=note.getPitch(); p>76; p--)
			{
				if (!MusicEngine.isAccidental(p))
				{
					offset++;
				}
			}
			boolean flipper=(offset%2==1);
			for (int i=1; i<offset; i++)
			{
				if (flipper)
				{
					g.drawLine(x-2,y+i*MusicEngine.STAFF_LINE_HEIGHT/2,x+getWidth()+2,y+i*MusicEngine.STAFF_LINE_HEIGHT/2);
				}
				flipper=!flipper;
			}
		}
	}
}
