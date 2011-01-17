package crescendo.base.module;

import javax.swing.JPanel;

/**
 * Abstract class to define the functionality for modules. 
 * Modules should also implement a constructor which takes in a string. That
 * string is the return value from the saveState() method and should be used to
 * return the module to the state it was in the last time saveState() was called.
 * @author nickgartmann
 */
public abstract class Module extends JPanel{

	/**
	 * Save the state of the current module. Used to get important state information saved
	 * into the profile so that the module can be reloaded to its previous state after an unexpected
	 * close.
	 * @return string representation of the state of the module
	 */
	public abstract String saveState();
}
