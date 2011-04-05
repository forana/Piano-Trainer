package crescendo.sheetmusic;

import java.awt.Graphics;

import javax.swing.JPanel;

import crescendo.base.ProcessedNoteEvent;
import crescendo.base.ProcessedNoteEventListener;
import crescendo.base.song.Note;
import crescendo.base.song.SongModel;

public class MusicEngine extends JPanel implements ProcessedNoteEventListener {
	private static final long serialVersionUID=1L;

	public MusicEngine(SongModel model,int activeTrack,boolean showTitle){
	}

	public MusicEngine(SongModel model,int activeTrack){
	}

	public MusicEngine(SongModel model,int activeTrack, double currentPosition){
	}

	public MusicEngine(SongModel model,int activeTrack, Note currentNote){
	}

	public MusicEngine(SongModel model,int activeTrack, Note currentSectionBeginNote, Note currentSectionEndNote){
	}
	
	public void setSection(Note startNote,Note endNote) {
	}

	@Override
	public void paint(Graphics g){

	}

	public void play(){
	}

	public void pause(){
	}

	public void stop(){
	}

	public void resume(){
	}

	@Override
	public void handleProcessedNoteEvent(ProcessedNoteEvent e) {
	}
	
	private static boolean isSharp(Note n)
	{
		return n.getPitch()%12==1 || n.getPitch()%12==3 || n.getPitch()%12==6 || n.getPitch()%12==8 || n.getPitch()%12==10;
	}
}