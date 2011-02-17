package crescendo.sheetmusic;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;
import java.awt.Toolkit;
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Icon;

import crescendo.base.NoteAction;
import crescendo.base.NoteEvent;
import crescendo.base.NoteEventListener;
import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;


public class FlowControllerBar extends JPanel implements NoteEventListener {

	{
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	// styling for labels
	private static final int BACKGROUND_COLOR=0x666666;
	private static final int TEXT_COLOR=0xFFFFFF;
	private static final Font FONT=new Font(Font.SANS_SERIF,Font.BOLD,16);
	private JButton playButton;
	private JButton stopButton;
	private JButton speedUpButton;
	private JButton slowDownButton;
	private JProgressBar songProgressBar;
	private SheetMusic musicEngine;
	private boolean isPlaying = false;
	private boolean canPlay = true;
	private int totalBeats;
	private int currentBeatCount = 0;
	private List<Note> timestampList;
	private FlowButtonEventListener fBEL= new FlowButtonEventListener();
	private Track listenTrack;
	private Icon playIcon;
	private Icon pauseIcon;

	public FlowControllerBar(int widthOffset, int heightOffset, int width, int height, SheetMusic m, SongModel model){
		musicEngine = m;
		totalBeats = (int) model.getDuration();
		listenTrack = model.getTracks().get(0);
		this.setBackground(new Color(BACKGROUND_COLOR));
		timestampList = new LinkedList<Note>();
		//this.setBounds(widthOffset,heightOffset,width, height);

		songProgressBar = new JProgressBar();
		add(songProgressBar);
		songProgressBar.setBounds(width/2, 0, width/2, height);
		songProgressBar.setMaximum(100);

		slowDownButton = new JButton();
		add(slowDownButton);
		slowDownButton.setBounds(width*3/20, 10, width/20, width/20);
		//this.slowDownButton.setText("<<");
		this.slowDownButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("resources/icons/backward.png")));

		playButton = new JButton();
		add(playButton);
		playButton.setBounds(width*3/20+width/10, 10, width/20, width/20);
		//this.playButton.setText("");
		playIcon=new ImageIcon(Toolkit.getDefaultToolkit().createImage("resources/icons/play.png"));
		pauseIcon=new ImageIcon(Toolkit.getDefaultToolkit().createImage("resources/icons/pause.png"));
		playButton.setIcon(playIcon);

		speedUpButton = new JButton();
		add(speedUpButton);
		speedUpButton.setBounds(width*3/20+(2*width/10), 10, width/20, width/20);
		//speedUpButton.setText(">>");
		this.speedUpButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("resources/icons/forward.png")));

		stopButton = new JButton();
		this.add(stopButton);
		stopButton.setBounds(width*3/20+(3*width/10), 10, width/20, width/20);
		//stopButton.setText("S");
		this.stopButton.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage("resources/icons/stop.png")));
		
		playButton.addActionListener(fBEL);
		stopButton.addActionListener(fBEL);
		


	}
	

	private void play(){
		if(isPlaying){
			musicEngine.pause();
			isPlaying = false;
			playButton.setIcon(playIcon);
		}else{
			if(canPlay){
				musicEngine.play(); 
				canPlay = false;
			}
			else {
				musicEngine.resume();
			}
			isPlaying = true;
			playButton.setIcon(pauseIcon);
		}
	}

	private void stop(){
		musicEngine.stop();
		isPlaying = false;
		canPlay = true;
	}

	private void speedUpPlayback(){

	}

	private void slowDownPlayback(){

	}


	private class FlowButtonEventListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource()== playButton){
				play();
			}else if (e.getSource() == stopButton){
				stop();
			}else if (e.getSource() == speedUpButton){
				speedUpPlayback();
			}else if (e.getSource() == slowDownButton){
				slowDownPlayback();
			}

		}

	}


	@Override
	public void handleNoteEvent(NoteEvent e) {
		Note n = e.getNote();
		
		if(!timestampList.contains(n) && e.getNote().getTrack().equals(listenTrack)){
			timestampList.add(n);
			currentBeatCount += n.getDuration();
		}
		songProgressBar.setValue(Math.round(100*currentBeatCount/totalBeats));
	}
}


