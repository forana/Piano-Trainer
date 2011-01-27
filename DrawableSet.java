package crescendo.sheetmusic;

import java.awt.Graphics;
import java.util.ArrayList;

import crescendo.base.song.Note;

public class DrawableSet extends Drawable{
	
	ArrayList<DrawableNote> notes;
	
	public DrawableSet()
	{
			notes = new ArrayList<DrawableNote>()	;
		
		
	}
	
	void addDrawableNote(DrawableNote n)
	{
		notes.add(n);
	}

	@Override
	public void draw(Graphics g) {
		
		int startX=-1;
		int startY=-1;
		
		int endX=-1;
		int endY=-1;
		
		//defines if all the notelines go up or down (if the bar is above or below the notes)
		boolean lineUp=true;
		
		//obtain the extreme note dimensions
		for(DrawableNote n: notes)
		{
			int x = (int)n.getX();
			int y = (int)n.getY();
			Note note = n.getNote();
			
			if(startX==-1 || startX>x)startX = x;
			if(endX==-1 || endX<x)endX = x;
			
			if(startY==-1 || startY>y)startY = y;
			if(endY==-1 || endY<y)endY = y;
		}
		
		//TODO: set lineUp accordingly
			
		
		//draw the notes, their lines, and the bar connecting them
		for(DrawableNote n: notes)
		{
			int x = (int)n.getX();
			int y = (int)n.getY();
			Note note = n.getNote();
			
			g.fillOval(x-4, y-4, 8, 8);
					
			//draw the line attached to it, and the tail
			if(!lineUp)
			{
				g.drawLine(x-4, y, x-4, y+4+16);
				g.drawLine(x-4, y+4+16,x-4+8, y+4+16-8);
			}
			else
			{
				g.drawLine(x+4, y, x+4, y+yPositionOfNote(note.getPitch())-4-16);
				g.drawLine(x+4, y-4-16,x+4+8, y-4-16+8);
			}
			
			
			
			//if off the staff,draw a line over it (if it lies on a bar)
			if(note.getPitch()<60)
			{
				if(((yPositionOfNote(note.getPitch())-34)%16==0))
					g.drawLine(x-8, y, x+8, y);
			}
			if((yPositionOfNote(note.getPitch())%16==0))
				g.drawLine(x-8, y, x+8, y);
		}
	}

}
