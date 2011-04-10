package crescendo.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import crescendo.base.FlowController;
import crescendo.base.ProcessedNoteEvent;
import crescendo.base.ProcessedNoteEventListener;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.sheetmusic.ScoreCalculator;

public class GameScorePanel extends JPanel implements FlowController,ProcessedNoteEventListener {
	private static final long serialVersionUID=1L;
	
	private GameModule module;
	private SongModel model;
	private Track activeTrack;
	private List<Track> audioTracks;
	private ScoreCalculator calc;
	
	private JLabel scoreLabel;
	
	public GameScorePanel(GameModule module,SongModel model,Track activeTrack,List<Track> audioTracks,ScoreCalculator calc)
	{
		this.module=module;
		this.model=model;
		this.activeTrack=activeTrack;
		this.audioTracks=audioTracks;
		this.calc=calc;
		
		this.setLayout(new GridLayout(1,2,5,0));
		JLabel legend=new JLabel("Score:");
		legend.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,16));
		legend.setHorizontalAlignment(JLabel.RIGHT);
		this.add(legend);
		scoreLabel=new JLabel("0");
		scoreLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,16));
		scoreLabel.setForeground(Color.BLUE);
		scoreLabel.setHorizontalAlignment(JLabel.LEFT);
		this.add(scoreLabel);
	}
	
	public void songEnd() {
		module.showScorePanel(model,activeTrack,audioTracks,calc);
	}
	
	public void handleProcessedNoteEvent(ProcessedNoteEvent e) {
		calc.handleProcessedNoteEvent(e);
		
		scoreLabel.setText(calc.getCurrentScore()+"");
		// this can get destroyed before the event propagates - hooray race conditions
		if (this.isVisible())
		{
			this.repaint();
		}
	}
	
	public void pause() {}
	public void resume() {}
	public void stop() {}
	public void suspend() {}
}
