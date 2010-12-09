package crescendo.base.EventDispatcher;

/**
 * Modifier
 * 
 * 
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
	
	void setAlt(boolean a)
	{
		alt = a;
	}
	
	void setCtrl(boolean c)
	{
		ctrl = c;
	}
	
	void setShift(boolean s)
	{
		shift = s;
	}
	
	boolean getAlt()
	{
		return alt;
	}
	
	boolean getCtrl()
	{
		return ctrl;
	}
	
	boolean getShift()
	{
		return shift;
	}
}
