package crescendo.sheetmusic;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Arc2D;

public class Tie extends DrawableModifier{
	private Drawable startNote;
	private Drawable endNote;
	
	public Tie(Drawable startNote, Drawable endNote){
		this.startNote = startNote;
		this.endNote = endNote;
	}
	@Override
	public void draw(Graphics g) {
		startNote.draw(g);
		Graphics2D g2=(Graphics2D)g;
		Stroke s=g2.getStroke();
		g2.setStroke(new BasicStroke(2));
		if (startNote.getY()==endNote.getY()) // same line
		{
			double x=startNote.getX()+startNote.getWidth()-(endNote.getX()-startNote.getX())/2;
			double y=startNote.getY();
			double w=2*(endNote.getX()-startNote.getX())-startNote.getWidth();
			double h=MusicEngine.STAFF_HEIGHT;
			Arc2D arc=new Arc2D.Double(x,y,w,h,60,60,Arc2D.OPEN);
			
			g2.draw(arc);
		}
		else
		{
			double x=startNote.getX()+startNote.getWidth()-20;
			double y=startNote.getY();
			double w=80-startNote.getWidth();
			double h=MusicEngine.STAFF_HEIGHT;
			Arc2D arc=new Arc2D.Double(x,y,w,h,90,30,Arc2D.OPEN);
			g2.draw(arc);
			x=endNote.getX()-60+startNote.getWidth();
			y=endNote.getY();
			arc=new Arc2D.Double(x,y,w,h,60,30,Arc2D.OPEN);
			g2.draw(arc);
		}
		
		g2.setStroke(s);
	}
}
