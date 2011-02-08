package crescendo.sheetmusic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import crescendo.base.song.*;

import javax.swing.JPanel;

public class DragMouseAdapter extends JPanel implements MouseListener {
	private int Startx =0;
	private int Starty =0;
	private int Endx =0;
	private int Endy =0;
	private int recx =0;
	private int recy =0;
	private int recWidth =0;
	private int recHeight=0;
	private Graphics2D g;

	private boolean isPressed = false;
	
	private List<Drawable> selectedNoteList;
	private MusicEngine musicEngine;

	public DragMouseAdapter(int width, int height, MusicEngine m){
		this.setBounds(0,0,width, height);
		this.addMouseListener(this);
		this.setVisible(true);
		musicEngine = m;
		selectedNoteList = m.getDrawables();
	}

	public void mousePressed(MouseEvent e){
		isPressed = true;
		Startx = e.getX();
		Starty = e.getY();


	}

	public void mouseReleased(MouseEvent e){
		Endx = e.getX();
		Endy = e.getY();
		isPressed = false;
		//selectNotes();
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void paint(Graphics g){
		System.out.println("WTF");
		g.clearRect(recx, recy, recWidth+1, recHeight+1);

		if(!(Startx-Endx==0 && Starty-Endy == 0)){

			System.out.println("Rectangle");
			g.setColor(Color.RED);
			if(Startx<Endx){
				if(Starty<Endy){
					g.drawRect(Startx
							, Starty, 
							Math.abs(Endx-Startx), 
							Math.abs(Endy-Starty));
					recx = Startx;
					recy = Starty;
					recWidth = Math.abs(Endx-Startx);
					recHeight = Math.abs(Endy-Starty);
				}else if(Starty>Endy){
					g.drawRect(Startx
							, Endy, 
							Math.abs(Endx-Startx), 
							Math.abs(Endy-Starty));
					recx = Startx;
					recy = Endy;
					recWidth = Math.abs(Endx-Startx);
					recHeight = Math.abs(Endy-Starty);
				}

			}else{
				if(Starty<Endy){
					g.drawRect(Endx
							, Starty, 
							Math.abs(Endx-Startx), 
							Math.abs(Endy-Starty));
					recx = Endx;
					recy = Starty;
					recWidth = Math.abs(Endx-Startx);
					recHeight = Math.abs(Endy-Starty);
				}else if (Starty>Endy){
					g.drawRect(Endx
							, Endy, 
							Math.abs(Endx-Startx), 
							Math.abs(Endy-Starty));
					recx = Endx;
					recy = Endy;
					recWidth = Math.abs(Endx-Startx);
					recHeight = Math.abs(Endy-Starty);
				}
			}

		}else{

		}
	}
	
	private void selectNotes(){
		//selectedNoteList = new ArrayList<Drawable>();
		DrawableNote startNote = null;
		DrawableNote endNote = null;
		
		boolean firstFound = false;
		
		for(Drawable n: selectedNoteList){
			if(n.getX()>recx && n.getX()<recx+recWidth && n.getY()>recy && n.getY()<recy+recHeight && n instanceof DrawableNote && !firstFound){
				startNote = (DrawableNote)n;
				endNote = (DrawableNote)n;
				firstFound = true;
			}else if(n.getX()>recx && n.getX()<recx+recWidth && n.getY()>recy && n.getY()<recy+recHeight && n instanceof DrawableNote){
				if(n.getY()<=startNote.getY() && n.getX()<startNote.getX()){
					startNote = (DrawableNote) n;
				}else if(n.getY()>=endNote.getY() && n.getX()>endNote.getX()){
					endNote = (DrawableNote) n;
				}
			}
		}
		musicEngine.setSection(startNote.getNote(), endNote.getNote());
	}
	
	public void resize(int width, int height){
		//this.setSize(width, height);
	}
}


