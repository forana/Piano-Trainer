package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

/**
 * Note modifier that denotes that a note should be 1.5 as long as the enclosed note would have been.
 * @author forana
 */
public class DottedNote extends DrawableNote {
	
	// The enclosed note
	private DrawableNote wrappedNote;
	
	/**
	 * Wrap a note with a dot.
	 * @param note The wrapped note.
	 */
	public DottedNote(DrawableNote note)
	{
		super(note.getNote(),note.getX(),note.getY());
		this.wrappedNote=note;
	}
	
	/**
	 * The width of this note, in pixels.
	 * 
	 * @return int
	 */
	public int getWidth()
	{
		return this.wrappedNote.getWidth()+6;
	}
	
	/**
	 * The number of beats this note represents.
	 * @return double
	 */
	public double getBeatsCovered() {
		return this.wrappedNote.getBeatsCovered()*1.5;
	}
	
	/**
	 * Sets this note's correctness.
	 * 
	 * @param b true if the note is correct, false otherwise.
	 */
	public void setCorrect(boolean b) {
		this.wrappedNote.setCorrect(b);
	}
	
	/**
	 * This method is not supported for this class.
	 * 
	 * @throws UnsupportedOperationException
	 */
	public DrawableNote spawn(Note n, int x, int y) {
		throw new UnsupportedOperationException("Dotted notes are not to be used for prototyping.");
	}
	
	/**
	 * Render this note in a given graphics context.
	 * 
	 * @param The graphics object
	 */
	public void draw(Graphics g) {
		this.wrappedNote.draw(g);
		g.fillOval(x+this.wrappedNote.getWidth()+2,y,5,5);
	}
}
