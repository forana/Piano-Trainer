package crescendo.sheetmusic;

import java.awt.Color;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import crescendo.base.NoteEvent;
import crescendo.base.NoteEventListener;
import crescendo.base.song.Note;


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
	private MusicEngine musicEngine;
	private boolean isPlaying = false;
	private int totalBeats;
	private int currentBeatCount = 0;
	private List<Long> timestampList;
	private FlowButtonEventListener fBEL= new FlowButtonEventListener();

	public FlowControllerBar(int widthOffset, int heightOffset, int width, int height, MusicEngine m){
		musicEngine = m;

		this.setBackground(new Color(BACKGROUND_COLOR));
		this.setLayout(null);
		this.setBounds(widthOffset,heightOffset,width, height);

		songProgressBar = new JProgressBar();
		add(songProgressBar);
		songProgressBar.setBounds(width/2, 0, width/2, height);
		songProgressBar.setMaximum(100);

		slowDownButton = new JButton();
		add(slowDownButton);
		slowDownButton.setBounds(width*3/20, 10, width/20, width/20);
		this.slowDownButton.setText("<<");

		playButton = new JButton();
		add(playButton);
		playButton.setBounds(width*3/20+width/10, 10, width/20, width/20);
		this.playButton.setText("P");

		speedUpButton = new JButton();
		add(speedUpButton);
		speedUpButton.setBounds(width*3/20+(2*width/10), 10, width/20, width/20);
		speedUpButton.setText(">>");

		stopButton = new JButton();
		this.add(stopButton);
		stopButton.setBounds(width*3/20+(3*width/10), 10, width/20, width/20);
		stopButton.setText("S");
		
		playButton.addActionListener(fBEL);
		stopButton.addActionListener(fBEL);
		


	}
	

	private void play(){
		if(isPlaying){
			musicEngine.pause();
			isPlaying = false;
		}else{
			musicEngine.play();
			isPlaying = true;
		}
	}

	private void stop(){
		musicEngine.stop();
		isPlaying = false;
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

		if(!timestampList.contains(e.getTimestamp())){
			timestampList.add(e.getTimestamp());
			currentBeatCount += n.getDuration();
		}
	}
}


