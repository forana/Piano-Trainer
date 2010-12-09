package crescendo.base.profile;

/**
 * Profile
 * 
 * 	This class represents a profile or user account of the application.
 * 
 * @author groszc
 *
 */
public class Profile {
	/** name of profile **/
	private String name;
	
	/**
	 * Profile
	 * 
	 * 	Constructor
	 * 
	 * @param n - name of profile
	 */
	public Profile(String n)
	{
		name = n;
	}
	
	/**
	 * getName
	 * 
	 * @return name of profile
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * setName
	 * 
	 * @param n - name of profile
	 */
	public void setName(String n)
	{
		name = n;
	}
}
