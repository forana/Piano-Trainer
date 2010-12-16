package crescendo.base.EventDispatcher;



/**
 * MouseEvent
 * 
 * This InputEvent type caters to the mouse, holding information
 * such as the button clicked and any modifier with the event.
 * 
 * @author groszc
 *
 */
public class MouseEvent extends InputEvent{
	
	/** The mouse button this event is accociated with. **/ 
	private int button;
	
	/** Any modifiers to this key (shift, strl, alt) **/
	private Modifier modifier;
	
	/** The x-axis the mouse event occurred at. **/
	private int x;
	
	/** The y-axis the mouse event occurred at. **/
	private int y;
	
	
	/**
	 * MouseEvent
	 * 
	 * 	Default constructor
	 * 
	 * @param a - action type
	 * @param t - timestamp 
	 * @param i - input type
	 * @param b - button
	 * @param x - x axis
	 * @param y - y axis
	 * @param m - modifiers
	 */
	public MouseEvent(ActionType a, long t, InputType i, int b, int x, int y, Modifier m) 
	{
		super(a, t, i);
		button = b;
		this.x = x;
		this.y = y;
		modifier = m;
	}
}
