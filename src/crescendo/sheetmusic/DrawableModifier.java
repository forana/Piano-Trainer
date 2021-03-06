package crescendo.sheetmusic;

import java.awt.Graphics;

/**
 * DrawableModifier
 * 
 * This abstract class represents a modifier from the view of the sheet music 
 * 	module. It contains a draw() method to implement how the modifier is 
 * 	rendered to the screen.
 * 
 * @author groszc
 *
 */
public abstract class DrawableModifier extends Drawable{
	
	/**
	 * draw
	 * 
	 * Draws this modifier to the given graphics context.
	 * 
	 * @param g - the graphics context to draw to.
	 */
	public abstract void draw(Graphics g);

}
