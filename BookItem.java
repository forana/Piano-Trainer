package crescendo.lesson;

import java.util.List;

public interface BookItem extends LessonTreeNode
{
	public List<PageItem> getItems();
}
