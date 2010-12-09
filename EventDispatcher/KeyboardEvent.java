package crescendo.base.EventDispatcher;


/**
 * KeyboardEvent
 * 
 * 
 * 
 * @author groszc
 *
 */
public class KeyboardEvent extends InputEvent{

	/** The keyboard button this event is accociated with. **/ 
	private int key;
	
	/** Any modifiers to this key (shift, strl, alt) **/
	private Modifier modifier;
	
	
	/**
	 * KeyboardEvent
	 * 
	 * 	Default constructor
	 * 
	 * @param a - action type
	 * @param t - timestamp 
	 * @param i - input type
	 * @param k - key
	 * @param m - modifiers
	 */
	KeyboardEvent(ActionType a, long t, InputType i, int k, Modifier m) 
	{
		super(a, t, i);
		key = k;
		modifier = m;
	}

}
