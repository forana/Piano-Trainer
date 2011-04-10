package crescendo.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import crescendo.base.profile.ProfileManager;
import crescendo.base.profile.SongScore;
import crescendo.base.song.Creator;
import crescendo.base.song.SongModel;
import crescendo.base.song.Track;
import crescendo.sheetmusic.ScoreCalculator;

public class GameResultsPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID=1L;
	
	private GameModule module;
	private SongModel model;
	private Track activeTrack;
	private List<Track> audioTracks;
	
	private JButton playAgain;
	private JButton newSong;
	
	public GameResultsPanel(GameModule module,SongModel model,Track activeTrack,List<Track> audioTracks,ScoreCalculator calc) {
		this.module=module;
		this.model=model;
		this.activeTrack=activeTrack;
		this.audioTracks=audioTracks;
		
		// Build creator string; we need this twice
		String creator=null;
		for (Creator cr : model.getCreators())
		{
			if (creator==null)
			{
				creator="";
			}
			else
			{
				creator+=", ";
			}
			creator+=cr.getType()+": "+cr.getName();
		}
		
		// update the profile's stored scores
		boolean newHigh=false;
		for (SongScore score : ProfileManager.getInstance().getActiveProfile().getGameScores())
		{
			if (score.getTitle().equals(model.getTitle()) && score.getAuthor().equals(creator))
			{
				newHigh=score.setHighScore(calc.getCurrentScore());
			}
		}
		
		// build UI
		JPanel panel=new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c=new GridBagConstraints();
		c.weightx=1;
		c.weighty=1;
		c.gridwidth=GridBagConstraints.REMAINDER;
		
		JLabel titleLabel=new JLabel("Results for "+model.getTitle());
		JLabel creatorLabel=new JLabel(creator);
		JLabel scoreLabel=new JLabel(""+calc.getCurrentScore());
		JLabel detailedScoreLabel=new JLabel("Points");
		this.playAgain=new JButton("Play this Song Again");
		this.newSong=new JButton("Play a New Song");
		
		c.gridwidth=GridBagConstraints.REMAINDER;
		titleLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
		titleLabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(titleLabel,c);
		
		creatorLabel.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,14));
		creatorLabel.setHorizontalAlignment(JLabel.LEFT);
		panel.add(creatorLabel,c);
		
		c.anchor=GridBagConstraints.CENTER;
		scoreLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,48));
		scoreLabel.setForeground(new Color(100,200,100));
		panel.add(scoreLabel,c);
		
		detailedScoreLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,20));
		detailedScoreLabel.setForeground(new Color(100,200,100));
		panel.add(detailedScoreLabel,c);
		
		if (newHigh)
		{
			JLabel newLabel=new JLabel("New high score!");
			newLabel.setFont(new Font(Font.SANS_SERIF,Font.BOLD,24));
			newLabel.setForeground(Color.BLUE);
			panel.add(newLabel);
		}
		
		c.gridwidth=1;
		c.fill=GridBagConstraints.HORIZONTAL;
		panel.add(this.playAgain,c);
		c.gridwidth=GridBagConstraints.REMAINDER;
		panel.add(this.newSong,c);
		
		this.add(panel);
		
		this.playAgain.addActionListener(this);
		this.newSong.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==this.playAgain) {
			this.module.showGamePanel(model,activeTrack,audioTracks);
		}
		else if (e.getSource()==this.newSong) {
			this.module.showSongSelectionPanel();
		}
	}
}
