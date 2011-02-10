package crescendo.sheetmusic;

import java.awt.Graphics;

import crescendo.base.song.Note;

public class Tie extends DrawableModifier{
	private Note startNote;
	private Note endNote;
	
	public Tie(Note startNote, Note endNote){
		this.startNote = startNote;
		this.endNote = endNote;
	}
	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
