package crescendo.base.EventDispatcher;

/**
 * Gesture
 * 
 * This class represents a shortcut keyboard code to a common
 * task in a module.
 * 
 * TODO: The idea behind this is to give special piano key a meaning (like play song)
 *       Please avoid getting into the details of this class since it was not implemented,
 *       instead redesign a way to accomplish this task. 
 *       
 *       THIS IS NOT COMPLETE NOR RECOMMENDED
 * 
 * @author groszc
 */
public class Gesture {
	
	int numKeys;
	int gestureAction;
	
	
	public static final int RESUME=1;
	public static final int RESTART=2;
	public static final int END=3;
	public static final int STOP=4;
	
	
	
	
	/**
	 * Gesture
	 * 
	 * @param numKeys
	 * @param gestureAction
	 */
	Gesture(int numKeys,int gestureAction)
	{
		this.numKeys = numKeys;
		this.gestureAction = gestureAction;
	}
	
	
	
	/**
	 * getType
	 * 
	 * @return the action associated with this gesture
	 */
	int getType()
	{
		return gestureAction;
	}
	
	
	
	/**
	 * getNumkeys
	 * 
	 * @return the number of keys this gesture needs to execute the action
	 */
	int getNumkeys()
	{
		return numKeys;
	}
}
