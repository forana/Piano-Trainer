package crescendo.base.profile;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import crescendo.base.EventDispatcher.EventDispatcher;
import crescendo.lesson.LessonData;

/**
 * Profile
 * 
 * This class represents a profile or user account of the application.
 * 
 * @author groszc
 * 
 */
public class Profile implements Serializable, Comparable<Profile> {

	private static final long serialVersionUID = -809118196452135843L;

	private boolean gradeDynamic;
	private boolean gradePitch;
	private boolean gradeTiming;

	/** name of profile **/
	private String name;

	private long secondsInGame;
	private long secondsInLesson;
	private long secondsInSheetMusic;

	private String lastModule;

	private String midiDeviceName;

	private List<SongPreference> songPreferences;
	private List<SongScore> gameScores;
	private SongPreference lastPlayedSong;

	private List<LessonData> lessonDataList;

	private File lastDirectoryFile;

	/**
	 * Profile
	 * 
	 * Constructor
	 * 
	 * @param n
	 *            - name of profile
	 */
	public Profile(String n) {
		name = n;
		secondsInGame = 0;
		secondsInLesson = 0;
		secondsInSheetMusic = 0;

		gradeDynamic = true;
		gradePitch = true;
		gradeTiming = true;

		songPreferences = new ArrayList<SongPreference>();
		gameScores = new LinkedList<SongScore>();

		lessonDataList = new LinkedList<LessonData>();

		this.midiDeviceName = null;

		this.lastDirectoryFile = null;

		lastModule = "";
	}

	public List<LessonData> getLessonData() {
		return this.lessonDataList;
	}

	public String getMidiDeviceName() {
		return this.midiDeviceName;
	}

	public void updateMidiDevice() {
		this.midiDeviceName = EventDispatcher.getInstance()
				.getCurrentTransmitterDevice().getDeviceInfo().getName();
	}

	/**
	 * getLastPlayedSong
	 * 
	 * @return the songpreference of the last played song
	 */
	public SongPreference getLastPlayedSong() {
		return lastPlayedSong;
	}

	/**
	 * setLastPlayedSong
	 * 
	 * @param lastPlayedSong
	 *            - the songpreference of the last played song
	 */
	public void setLastPlayedSong(SongPreference lastPlayedSong) {
		this.lastPlayedSong = lastPlayedSong;
	}

	/**
	 * getSongPreferences
	 * 
	 * @return a list of song preferences
	 */
	public List<SongPreference> getSongPreferences() {
		return songPreferences;
	}

	public List<SongScore> getGameScores() {
		return gameScores;
	}

	/**
	 * getIsDynamicGraded
	 * 
	 * @return true if dynamic is graded
	 */
	public boolean getIsDynamicGraded() {
		return gradeDynamic;
	}

	/**
	 * setIsDynamicGraded
	 * 
	 * @param true if dynamic should be graded
	 * @return
	 */
	public void setIsDynamicGraded(boolean isGraded) {
		gradeDynamic = isGraded;
	}

	/**
	 * getIsPitchGraded
	 * 
	 * @return true if pitch is graded
	 */
	public boolean getIsPitchGraded() {
		return gradePitch;
	}

	/**
	 * setIsTimingGraded
	 * 
	 * @param true if Timing should be graded
	 * @return
	 */
	public void setIsTimingGraded(boolean isGraded) {
		gradeTiming = isGraded;
	}

	/**
	 * getIsTimingGraded
	 * 
	 * @return true if Timing is graded
	 */
	public boolean getIsTimingGraded() {
		return gradeTiming;
	}

	/**
	 * setIsPitchGraded
	 * 
	 * @param true if Pitch should be graded
	 * @return
	 */
	public void setIsPitchGraded(boolean isGraded) {
		gradePitch = isGraded;
	}

	/**
	 * getName
	 * 
	 * @return name of profile
	 */
	public String getName() {
		return name;
	}

	/**
	 * setName
	 * 
	 * @param n
	 *            - name of profile
	 */
	public void setName(String n) {
		name = n;
	}

	public File getLastDirectory() {
		return this.lastDirectoryFile;
	}

	public void setLastDirectory(File dir) {
		this.lastDirectoryFile = dir;
	}

	/**
	 * getSecondsInGame
	 * 
	 * @return total number of seconds user has spent in the game module
	 */
	public long getSecondsInGame() {
		return secondsInGame;
	}

	/**
	 * setSecondsInGame
	 * 
	 * @param seconds
	 *            - total number of seconds user has spent in the game module
	 */
	public void setSecondsInGame(long seconds) {
		secondsInGame = seconds;
	}

	/**
	 * getSecondsInLesson
	 * 
	 * @return total number of seconds user has spent in the lesson module
	 */
	public long getSecondsInLesson() {
		return secondsInLesson;
	}

	/**
	 * setSecondsInLesson
	 * 
	 * @param seconds
	 *            - total number of seconds user has spent in the lesson module
	 */
	public void setSecondsInLesson(long seconds) {
		secondsInLesson = seconds;
	}

	/**
	 * getSecondsInSheetMusic
	 * 
	 * @return total number of seconds user has spent in the sheet music module
	 */
	public long getSecondsInSheetMusic() {
		return secondsInLesson;
	}

	/**
	 * setSecondsInSheetMusic
	 * 
	 * @param seconds
	 *            - total number of seconds user has spent in the sheet music
	 *            module
	 */
	public void setSecondsInSheetMusic(long seconds) {
		secondsInSheetMusic = seconds;
	}

	/**
	 * getLastModule
	 * 
	 * @return a string representatino of the last module the user was in
	 */
	public String getLastModule() {
		return lastModule;
	}

	/**
	 * setSecondsInSheetMusic
	 * 
	 * @param seconds
	 *            - total number of seconds user has spent in the sheet music
	 *            module
	 */
	public void setLastModule(String lastMod) {
		lastModule = lastMod;
	}

	@Override
	public int compareTo(Profile arg0) {
		int toRet = 0;

		if (!name.equals(arg0.name))
			toRet = 1;
		if (secondsInGame != arg0.secondsInGame)
			toRet = 1;
		if (secondsInLesson != arg0.secondsInLesson)
			toRet = 1;
		if (secondsInSheetMusic != arg0.secondsInSheetMusic)
			toRet = 1;

		return toRet;
	}
}
