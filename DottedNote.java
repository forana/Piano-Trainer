package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class DottedNote extends DrawableNote {
	
	private DrawableNote wrappedNote;
	
	public DottedNote(DrawableNote note)
	{
		super(note.getNote(),note.getX(),note.getY());
		this.wrappedNote=note;
	}
	
	public int getWidth()
	{
		return this.wrappedNote.getWidth()+6;
	}
	
	public double getBeatsCovered() {
		return this.wrappedNote.getBeatsCovered()*1.5;
	}
	
	public void setCorrect(boolean b) {
		this.wrappedNote.setCorrect(b);
	}
	
	public DrawableNote spawn(Note n, int x, int y) {
		throw new UnsupportedOperationException("Dotted notes are not to be used for prototyping.");
	}
	
	public void draw(Graphics g) {
		this.wrappedNote.draw(g);
		g.fillOval(x+this.wrappedNote.getWidth()+2,y,5,5);
	}
}
