package crescendo.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;

import javax.swing.JPanel;

import crescendo.base.AudioPlayer;
import crescendo.base.NoteAction;
import crescendo.base.NoteEvent;
import crescendo.base.NoteEventListener;
import crescendo.base.Updatable;
import crescendo.base.UpdateTimer;
import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.sheetmusic.Drawable;
import crescendo.sheetmusic.DrawableNote;

public class GameEngine extends JPanel implements NoteEventListener, Updatable{
	
	private AudioPlayer audioPlayer;
	private SongModel songModel;
	
	

	
	
	//graphics variables
	int height;
	int width;
	
	HashMap<Note,Long> fallingNotes;
	
	int lowestKey;
	int highestKey;
	
	UpdateTimer timer;
	Thread timerThread;
	
	public GameEngine(GameModule module,SongModel model) {
		songModel = model;
		
		width = 1024;
		height = 768;
		
		lowestKey = 21;
		highestKey = 108;
		
		this.setPreferredSize(new Dimension(width, height));
		this.setBackground(Color.WHITE);
		timer = new UpdateTimer(this);
		timerThread = new Thread(timer);
		timerThread.start();
		
		
		songModel = model;
		
		fallingNotes = new HashMap<Note,Long>();
		
			
	}
	
	//normalize X
	int nX(double x)
	{
		return (int) ((width/1024)*x);
	}
	
	//normalizeY
	int nY(double y)
	{
		return (int) ((height/768)*y);
	}
	
	
	public boolean isSharp(int n)
	{
		boolean toRet = false;
		
		if(n==22 || n==25 || n==27 || n==30 || n==32 || n==34 || 
				n==37 || n==39 || n==42 || n==44 || n==46 || 
				n==49 || n==51 || n==54 || n==56 || n==58 ||
				n==61 || n==63 || n==66 || n==68 || n==70 ||
				n==73 || n==75 || n==78 || n==80 || n==82 ||
				n==85 || n==87 || n==90 || n==92 || n==94 ||
				n==97 || n==99 || n==102 || n==104 || n==106)toRet = true;
		
		
		
		return toRet;
	}
	
	public char whichKey(int n)
	{
		//21='a'
		
		char toRet='a';
		
		for(int i=21;i<n;i++)
		{
			if(!isSharp(i))
			{
				if(toRet<'g')toRet++;
				else toRet='a';
			}
		}
		return toRet;
	}
	
	
	public int findXForKey(int n)
	{
		double toRet = 10;
		
		//count the white keys
		int numWhiteKeys=0;
		for(int i=lowestKey;i<=highestKey;i++)if(!isSharp(i))numWhiteKeys++;
		
		
		for(int i=lowestKey;i<n;i++)
		{
			if(!isSharp(i))
			{
				toRet+=(1004.0/numWhiteKeys);
			}
		}
		
		return (int)toRet;
	}
	
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		
		
		g.drawLine(nX(10), nY(100), nX(1014), nY(100));
		g.drawLine(nX(10), nY(600), nX(1014), nY(600));
		g.drawLine(nX(10), nY(700), nX(1014), nY(700));
		
		
		//count the white keys
		int numWhiteKeys=0;
		for(int i=lowestKey;i<=highestKey;i++)if(!isSharp(i))numWhiteKeys++;
		
		
		//Draw the black keys
		int x=0;
		for(int i=0;i<highestKey-lowestKey;i++)
		{
			if(isSharp(i+lowestKey))
			{
				g.fillRect(nX(10+x*1004/numWhiteKeys)-(1004/numWhiteKeys/4), nY(600), (1004/numWhiteKeys/2), 50);
			}
			else x++;
		}
		
		//Draw the white keys
		char keyName=whichKey(lowestKey);
		
		for(int i=0;i<=numWhiteKeys;i++)
		{
			g.drawLine(nX(10+i*1004/numWhiteKeys), nY(100), nX(10+i*1004/numWhiteKeys), nY(700));
			
			
			
			g.drawString(keyName+"", nX(10+i*1004/numWhiteKeys+(1004/numWhiteKeys*.5)), nY(690));
			
			if(keyName<'g')keyName++;
			else keyName='a';
		}
		
		
		//Draw falling Notes
		for(Note n: fallingNotes.keySet())
		{
			long y = System.currentTimeMillis()-fallingNotes.get(n);
			y = (y*440)/2000;
			y += 100;
			
			//BPM
			//BPS = BPM/60
			//BPMS = 
			
			long duration = (long) (n.getDuration()/(songModel.getBPM()/60.0/1000.0/440.0*2000.0));
			
			if(isSharp(n.getPitch()))
			{
				g.setColor(Color.BLACK);
				g.fillRect(nX(findXForKey(n.getPitch())-(1004/numWhiteKeys/4)), (int) (y-duration), (1004/numWhiteKeys/2), (int) duration);
			}
			else
			{
				g.setColor(new Color(240,240,240));
				g.fillRect(nX(findXForKey(n.getPitch())), (int) (y-duration), (1004/numWhiteKeys), (int) duration);
			}
		}
	}




	@Override
	public void update() {
		repaint();
	}

	@Override
	public void handleNoteEvent(NoteEvent e) {
		if(e.getAction()==NoteAction.BEGIN)
			fallingNotes.put(e.getNote(), System.currentTimeMillis());
		
	}
	
	
}

