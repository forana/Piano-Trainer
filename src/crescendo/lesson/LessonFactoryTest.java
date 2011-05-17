package crescendo.lesson;

import org.junit.Assert;
import org.junit.Test;

public class LessonFactoryTest
{
	@Test
	public void testLoad() throws Exception
	{
		LessonBook book=LessonFactory.createLessonBook("resources/samplebook.tlb");
		Assert.assertEquals(book.getData().getTitle(),"Sample Book");
	}
}
