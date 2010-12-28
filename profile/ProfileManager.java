package crescendo.base.profile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * ProfileManager
 * 
 * This singleton class represents the collection of profiles that
 * have been created by the application. It holds a profile as the
 * active profile and a collection of profiles for the rest. Since
 * the individual preferences exist within the profile class, this
 * essentially holds all of the information that needs to be 
 * persistent between runs, and therefore is serializable and has
 * some helper functionality to save its information to a file.
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
	
	/** list of profiles **/
	private ArrayList<Profile> profiles;
	
	/**
	 * ProfileManager
	 * 
	 * 	Default constructor, private since this class is a singleton
	 */
	private ProfileManager()
	{
		activeProfile = new Profile("default");
		profiles = new ArrayList<Profile>();
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
		profiles.add(p);
	}
	
	/**
	 * removeProfile
	 * 
	 * @param p - profile to remove
	 */
	public void removeProfile(Profile p)
	{
		profiles.remove(p);
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
		p.setName(newName);
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
	 * setActiveProfile
	 * 
	 * @param p - profile to set to activeProfile
	 */
	private void setActiveProfile(Profile p)
	{
		activeProfile = p;
	}
	
	/**
	 * switchProfile
	 * 
	 * @param p - the profile to switch to activeProfile
	 * @return
	 */
	public Profile switchProfile(Profile p)
	{
		profiles.add(activeProfile);
		profiles.remove(p);
		
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
		return profiles;
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
		Profile toRet = null;
		for(Profile p: profiles)if(p.getName().equals(name))toRet = p;
		return toRet;
	}
	
	
	
	/**
	 * saveToFile
	 * 
	 * 
	 * @param filename - the name of the file to write to
	 * @return boolean representing success if true
	 */
	public boolean saveToFile(String filename)
	{
		boolean toRet = true;
		
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try
		{
			fos = new FileOutputStream(filename);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			toRet = false;
		}
		
		return toRet;
	}
	
	
	/**
	 * loadFromFile
	 * 
	 * 
	 * @param filename - the name of the file to load from
	 * @return boolean representing success if true
	 */
	public boolean loadFromFile(String filename)
	{
		boolean toRet = true;
		
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try
		{
			fis = new FileInputStream(filename);
			in = new ObjectInputStream(fis);
			
			
			ProfileManager loaded = (ProfileManager) in.readObject();
			in.close();
			
			this.setActiveProfile(loaded.getActiveProfile());
			profiles.clear();
			for(Profile p:loaded.getProfiles())profiles.add(p);
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
			toRet = false;
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
			toRet = false;
		}
		
		return toRet;
	}

}
