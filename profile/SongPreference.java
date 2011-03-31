package crescendo.base.profile;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * SongPreference
 * 
 * This storage class represents the preferences associated with a song. 
 * 
 * Features include:
 * 
 * 	filePath to the song
 *  numTracks of the song
 *  activeTrack for the user to play
 *  
 *  trackVolume for each track individually
 *  trackInstrument for each track individually
 * 
 * @author groszc
 *
 */
public class SongPreference implements Serializable{
	private static final long serialVersionUID=1L;

	//The absolute path to the song file
	private String filePath;
	
	//The number of tracks this song has
	private int numTracks;
	//The default active track
	private int activeTrack;
	
	//The volume of each track
	private ArrayList<Integer> trackVolume;
	//The instrument for each track
	private ArrayList<Integer> trackInstrument;
	//The top 10 scores for this song
	private ArrayList<Integer> songScores;
	
	//TODO: Comment name and creator vars and gets/sets
	
	String name;
	String creator;
	
	
	/**
	 * SongPreference
	 * 
	 * Default constructor, creates default values for instrument that should be overridden immediately
	 * after creation.
	 * 
	 * @param filePath - The absolute path to the song file
	 * @param numTracks - The number of tracks this song has
	 * @param activeTrack - The default active track (Above 0 unless no tracks need to be played by the user)
	 */
	public SongPreference(String filePath,int numTracks,int activeTrack)
	{
		//set filePath, numTracks, and activeTrack
		this.filePath = filePath;
		this.numTracks = numTracks;
		this.activeTrack = activeTrack;
		
		trackVolume = new ArrayList<Integer>();
		trackInstrument = new ArrayList<Integer>();
		
		//create defaults for volume and instrument
		for(int i=0;i<numTracks;i++)
		{
			//default volume
			trackVolume.add(50);
			
			//this one should be overridden after creation
			trackInstrument.add(50);
		}
		
		songScores=new ArrayList<Integer>();
		
		name = "";
		creator = "";
	}
	
	/** Returns the scores of the song **/
	public ArrayList<Integer> getScores()
	{
		return songScores;
	}
	
	/** Returns the absolute file path of the song **/
	public String getFilePath()
	{
		return filePath;
	}
	
	/** Sets the absolute file path of the song **/
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	
	/** Returns the active track of the song **/
	public int getActiveTrack()
	{
		return activeTrack;
	}
	
	/** Sets the active track of the song **/
	public void setActiveTrack(int activeTrack)
	{
		this.activeTrack = activeTrack;
	}
	
	/** Returns the number of tracks in the song **/
	public int getNumTracks()
	{
		return numTracks;
	}
	
	/** Sets the number of tracks in the song **/
	public void setNumTracks(int numTracks)
	{
		this.numTracks = numTracks;
	}
	
	
	
	/** Returns the volume of a specified track **/
	public int getTrackVolume(int track)
	{
		return trackVolume.get(track);
	}
	
	/** Sets the volume of a specified track **/
	public void setTrackVolume(int track,int volume)
	{
		trackVolume.set(track,volume);
	}
	
	
	/** Returns the instrument of a specified track **/
	public int getTrackInstrument(int track)
	{
		return trackInstrument.get(track);
	}
	
	/** Sets the instrument of a specified track **/
	public void setTrackInstrument(int track,int instrument)
	{
		trackVolume.set(track,instrument);
	}
	
	/** Sets the name of the song **/
	public void setSongName(String name){
		this.name = name;
	}
	
	/**	returns the name of the song **/
	public String getSongName(){
		return name;
	}
	
	/** sets the artist for the song **/
	public void setCreator(String creator){
		this.creator = creator;
	}
	
	/** returns the artist of the song **/
	public String getCreator(){
		return creator;
	}
	
	
}
