package crescendo.base.EventDispatcher;

/**
 * Modifier
 * 
 * This class holds information pertaining to possible event modifiers,
 * such as hold ctrl or shift while an event occurs.
 * 
 * @author groszc
 *
 */
public class Modifier {

	/** true if alt was held when event was created **/
	private boolean alt;
	
	/** true if ctrl was held when event was created **/
	private boolean ctrl;
	
	/** true if shift was held when event was created **/
	private boolean shift;
	
	/**
	 * Modifier
	 * 
	 * 	Default constructor.
	 * 
	 * @param a - alt
	 * @param c - ctrl
	 * @param s - shift
	 */
	Modifier(boolean a, boolean c, boolean s)
	{
		alt = a;
		ctrl = c;
		shift = s;
	}
	
	/**
	 * setAlt
	 * 
	 * Sets whether alt is pushed currently or not
	 * 
	 * @param a - true if alt is currently pressed
	 */
	void setAlt(boolean a)
	{
		alt = a;
	}
	
	/**
	 * setCtrl
	 * 
	 * Sets whether ctrl is pushed currently or not
	 * 
	 * @param c - true if ctrl is currently pressed
	 */
	void setCtrl(boolean c)
	{
		ctrl = c;
	}
	
	/**
	 * setShift
	 * 
	 * Sets whether shift is pushed currently or not
	 * 
	 * @param s - true if shift is currently pressed
	 */
	void setShift(boolean s)
	{
		shift = s;
	}
	
	/**
	 * getAlt
	 * 
	 * Gets whether alt is pushed currently or not
	 * 
	 * @return true if alt is currently pressed
	 */
	boolean getAlt()
	{
		return alt;
	}
	
	/**
	 * getCtrl
	 * 
	 * Gets whether ctrl is pushed currently or not
	 * 
	 * @return true if ctrl is currently pressed
	 */
	boolean getCtrl()
	{
		return ctrl;
	}
	
	/**
	 * getShift
	 * 
	 * Gets whether shift is pushed currently or not
	 * 
	 * @return true if shift is currently pressed
	 */
	boolean getShift()
	{
		return shift;
	}
}
