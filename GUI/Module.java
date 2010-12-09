package crescendo.base.GUI;

import java.awt.Graphics;



/**
 * @author larkinp
 * @version 1.0
 * @created 26-Oct-2010 7:31:47 PM
 */
public interface Module {

	public void cleanUp();

	/**
	 * 
	 * @param g
	 */
	public void draw(Graphics g);

	public void setUp();

}