package crescendo.lesson;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * An item that represents an image with possible additional information.
 * @author forana
 */
public class ImageItem implements PageItem
{
	// path to image
	private String source;
	// alt text
	private String alt;
	// footer text
	private String footer;
	
	/**
	 * @param source Filepath to image.
	 * @param alt Text to be shown on mouseover / if the path is broken.
	 * @param footer Footer text.
	 */
	public ImageItem(String source,String alt,String footer)
	{
		this.source=source;
		this.alt=alt;
		this.footer=footer;
	}
	
	/**
	 * @param module The calling module.
	 * @return A JPanel representation of this item.
	 */
	public JPanel getPanel(JComponent module) // this call should not need the module parameter
	{
		return new ImagePanel(this);
	}
	
	/**
	 * @return The filepath for the image.
	 */
	public String getSource()
	{
		return source;
	}
	
	/**
	 * @return The alt text, or null.
	 */
	public String getAlt()
	{
		return alt;
	}
	
	/**
	 * @return The footer text, or null.
	 */
	public String getFooter()
	{
		return footer;
	}
}
