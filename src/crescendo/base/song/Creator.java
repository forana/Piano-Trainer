package crescendo.base.song;

/**
 * Creator
 * A creator is someone who worked on a song. This could be 
 * a composer, author, poet, lyricist, or any other number of
 * arbitrarily defined type.
 * @author nickgartmann
 */
public class Creator {
	private String name;
	private String type;
	
	/**
	 * Constructor
	 * Create a creator object
	 * @param name name of the creator
	 * @param type type of the creator (composer, author, poet, etc)
	 */
	public Creator(String name, String type)
	{
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
