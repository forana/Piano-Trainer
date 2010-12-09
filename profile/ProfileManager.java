package crescendo.base.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * ProfileManager
 * 
 * 
 * 
 * @author groszc
 *
 */
public class ProfileManager implements Serializable{
	
	/** UID for Serializable interface**/
	private static final long serialVersionUID = 1L;

	
	/** this is a singleton **/
	private static ProfileManager profileManager=null;
	
	/** the currently active profile **/
	private Profile activeProfile;
	
	/** a map to be able to reference a profile by name **/
	HashMap<String,Profile> profiles;
	
	/**
	 * ProfileManager
	 * 
	 * 	Default constructor, private since this class is a singleton
	 */
	private ProfileManager()
	{
		activeProfile = new Profile("default");
		profiles = new HashMap<String,Profile>();
	}
	
	/**
	 * getInstance
	 * 
	 * 
	 * @return singleton ProfileManager instance
	 */
	public static ProfileManager getInstance()
	{
		if(profileManager==null)
		{
			profileManager = new ProfileManager();
		}
		
		return profileManager;	
	}
	
	
	/**
	 * addProfile
	 * 
	 * @param p - profile to add
	 */
	public void addProfile(Profile p)
	{
		profiles.put(p.getName(), p);
	}
	
	/**
	 * removeProfile
	 * 
	 * @param p - profile to remove
	 */
	public void removeProfile(Profile p)
	{
		profiles.remove(p.getName());
	}
	
	
	/**
	 * renameProfile
	 * 
	 * 
	 * @param p - profile to rename
	 * @param newName - the new name to give the profile
	 */
	public void renameProfile(Profile p,String newName)
	{
		profiles.remove(p.getName());
		p.setName(newName);
		
		profiles.put(p.getName(), p);
	}
	
	
	/**
	 * getActiveProfile
	 * 
	 * @return activeProfile
	 */
	public Profile getActiveProfile()
	{
		return activeProfile;
	}
	
	/**
	 * switchProfile
	 * 
	 * @param p - the profile to switch to activeProfile
	 * @return
	 */
	public Profile switchProfile(Profile p)
	{
		profiles.put(activeProfile.getName(), activeProfile);
		profiles.remove(p.getName());
		
		activeProfile = p;
		
		return activeProfile;
	}
	
	
	/**
	 * getProfiles
	 * 
	 * 
	 * @return an ArrayList of Profiles
	 */
	public ArrayList<Profile> getProfiles()
	{
		ArrayList<Profile> listOfProfiles = new ArrayList<Profile>();
		
		for(Profile p: profiles.values())
		{
			listOfProfiles.add(p);
		}
		
		return listOfProfiles;
		
	}
	
	
	/**
	 * getProfileByName
	 * 
	 * 
	 * @param name - the name of the profile to retrieve
	 * @return the profile with the given name
	 */
	public Profile getProfileByName(String name)
	{
		return profiles.get(name);
	}

}
