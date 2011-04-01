package crescendo.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JPanel;

import crescendo.base.AudioPlayer;
import crescendo.base.NoteAction;
import crescendo.base.NoteEvent;
import crescendo.base.NoteEventListener;
import crescendo.base.SongPlayer;
import crescendo.base.Updatable;
import crescendo.base.UpdateTimer;
import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.sheetmusic.Drawable;
import crescendo.sheetmusic.DrawableNote;

public class GameEngine extends JPanel{
	
	//The panels that make up this Screen
	private GameGraphicsPanel graphicsPanel;
	private GameScorePanel scorePanel;
	
	
	
	/*
	 * GameEngine
	 * 
	 * This class is made up of two main parts: the score frame at the top,
	 * and the graphics of the game itself.
	 * 
	 * @param model - A reference the the songmodel to work from
	 * @param activeTrack - the active track to display the notes of
	 */
	public GameEngine(SongModel model,Track activeTrack) {
		
		this.setLayout(new FlowLayout());
		
		
		scorePanel = new GameScorePanel();
		this.add(scorePanel);
		
		graphicsPanel = new GameGraphicsPanel(model, activeTrack);
		this.add(graphicsPanel);
		
		this.updateUI();
			
	}
}