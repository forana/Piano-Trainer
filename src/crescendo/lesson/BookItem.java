package crescendo.lesson;

import java.util.List;

/**
 * Represents an item that can be contained in the top-level heirarchy of a lesson book.
 * 
 * @author forana
 */
public interface BookItem extends LessonTreeNode
{
	/**
	 * @return The page items within this item.
	 */
	public List<PageItem> getItems();
}
