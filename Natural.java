package crescendo.sheetmusic;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class Natural extends DrawableModifier{

	private DrawableNote note;
	
	public Natural(DrawableNote n){
		note = n;
		x = n.getX()+n.getWidth();
		y = n.getY();
	}

	@Override
	public void draw(Graphics g) {
		// draw note first and absorb its color
		note.draw(g);
		
		int q=(int)(MusicEngine.STAFF_LINE_HEIGHT*0.25);
		Graphics2D g2=(Graphics2D)g;
		Stroke stroke=g2.getStroke();
	
		// vertical lines
		g.drawLine(x+q,y+q,x+q,y+2*MusicEngine.STAFF_LINE_HEIGHT);
		g.drawLine(x+q+MusicEngine.STAFF_LINE_HEIGHT/2,y-MusicEngine.STAFF_LINE_HEIGHT,x+q+MusicEngine.STAFF_LINE_HEIGHT/2,y+MusicEngine.STAFF_LINE_HEIGHT-q);
		// horizontal lines
		g2.setStroke(new BasicStroke(2));
		g.drawLine(x+q+1,y+q,x+MusicEngine.STAFF_LINE_HEIGHT-q,y-q);
		g.drawLine(x+q+1,y+MusicEngine.STAFF_LINE_HEIGHT+q,x+MusicEngine.STAFF_LINE_HEIGHT-q,y+MusicEngine.STAFF_LINE_HEIGHT-q);
		g2.setStroke(stroke);
	}
}
