package crescendo.sheetmusic;

import java.awt.Graphics;

/**
 * Drawable
 * 
 * This abstract class defines the commonalities of the drawable aspects of music so
 * that they can be rendered to the screen.
 * 
 * @author groszc
 * @author forana
 */
public abstract class Drawable {
		/** x position of the object **/
		protected int x;
		
		/** x position of the object **/
		protected int y;
		
		/**
		 * Render this note in a given graphics context.
		 * 
		 * @param The graphics object
		 */
		public abstract void draw(Graphics g);
		
		/**
		 * This note's x-position.
		 * 
		 * @return int
		 */
		int getX()
		{
			return x;
		}
		
		/**
		 * This note's y-position.
		 * 
		 * @return int
		 */
		int getY()
		{
			return y;
		}
		
		/**
		 * This note's width, in pixels.
		 * 
		 * @return int
		 */
		public int getWidth()
		{
			return 0;
		}
}
