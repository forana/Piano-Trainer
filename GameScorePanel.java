package crescendo.game;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

public class GameScorePanel extends JPanel{

	public GameScorePanel()
	{
		this.setPreferredSize(new Dimension(1024, 30));
		this.setBackground(Color.white);
		this.add(new JButton("LOL AT THIS SCORE BUTTON, SOOOO COOOOOL"));
		
	}
}
