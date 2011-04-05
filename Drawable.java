package crescendo.sheetmusic;

import java.awt.Color;
import java.awt.Graphics;


/**
 * Drawable
 * 
 * This abstract class defines the commonalities of the drawable aspects of music so
 * that they can be rendered to the screen.
 * 
 * @author groszc
 *
 */
public abstract class Drawable {

		/** visibility of the object **/
		private double alpha;
		
		/** color of the object **/
		private Color color;
		
		/** x position of the object **/
		protected int x;
		
		/** x position of the object **/
		protected int y;
		
		/**
		 * draw
		 * 
		 * @param g - the graphics context to draw to
		 */
		public abstract void draw(Graphics g);
		
		int getX()
		{
			return x;
		}
		
		int getY()
		{
			return y;
		}
		
		public int getWidth()
		{
			return 0;
		}
		
		
		protected int yPositionOfNote(int pitch)
		{
			int toRet = 0;
			
			for(int i=21;i<pitch;i++)
			{
				if(i==22 || i==25 || i==27 || i==30 || i==32 || i==34 || 
							i==37 || i==39 || i==42 || i==44 || i==46 || 
							i==49 || i==51 || i==54 || i==56 || i==58 ||
							i==61 || i==63 || i==66 || i==68 || i==70 ||
							i==73 || i==75 || i==78 || i==80 || i==82 ||
							i==85 || i==87 || i==90 || i==92 || i==94 ||
							i==97 || i==99 || i==102 || i==104 || i==106);
				else toRet++;
			}
			
			toRet = 4*16 - 8*(toRet-25);
			if(pitch<60)toRet+=34;
			
			return toRet;
		}
		
		
}
